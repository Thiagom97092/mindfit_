package com.example.mindfit.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mindfit.databinding.FragmentRegisterBinding
import com.example.mindfit.database.DatabaseHelper

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        return binding.root
    }

    private fun registerUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val userExists = dbHelper.checkUserExists(email)
        if (userExists) {
            Toast.makeText(requireContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show()
            return
        }

        val inserted = dbHelper.insertUser(name, email, password)
        if (inserted) {
            Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
            // Aquí puedes navegar al login si deseas
        } else {
            Toast.makeText(requireContext(), "Error al registrar", Toast.LENGTH_SHORT).show()
        }
    }
}
