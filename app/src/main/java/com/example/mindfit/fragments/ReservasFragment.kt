package com.example.mindfit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mindfit.R

class ReservasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCultura = view.findViewById<Button>(R.id.btn_cultura)
        val btnPsicologia = view.findViewById<Button>(R.id.btn_psicologia)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        btnCultura.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CulturaFragment())
                .addToBackStack(null)
                .commit()
        }

        btnPsicologia.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PsicologiaFragment())
                .addToBackStack(null)
                .commit()
        }

        btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Cerrando sesión", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }
}
