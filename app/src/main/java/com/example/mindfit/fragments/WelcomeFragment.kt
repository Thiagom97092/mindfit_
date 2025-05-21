package com.example.mindfit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mindfit.R
import com.example.mindfit.fragments.LoginFragment


class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botonEmpezar = view.findViewById<Button>(R.id.btn_empezar)

        botonEmpezar.setOnClickListener {
            // Aquí haces el cambio de fragmento
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment()) // El ID del contenedor debe estar en tu activity_main.xml
                .addToBackStack(null) // Permite volver atrás con el botón
                .commit()
        }
    }
}
