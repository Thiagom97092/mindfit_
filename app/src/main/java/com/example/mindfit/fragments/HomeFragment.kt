package com.example.mindfit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mindfit.R
import com.example.mindfit.activities.MainActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnReservas = view.findViewById<Button>(R.id.btn_reservas)
        val btnQr = view.findViewById<Button>(R.id.btn_qr)
        val btnHistorial = view.findViewById<Button>(R.id.btn_historial)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        btnReservas.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir Reservas", Toast.LENGTH_SHORT).show()

            // Reemplaza esto con la lógica para abrir el fragmento de reservas
            // Ejemplo:
            // parentFragmentManager.beginTransaction()
            //     .replace(R.id.fragment_container, ReservasFragment())
            //     .addToBackStack(null)
            //     .commit()
        }

        btnQr.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir QR", Toast.LENGTH_SHORT).show()
            // Similar a arriba: abre QRFragment si lo tienes creado
        }

        btnHistorial.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir Historial", Toast.LENGTH_SHORT).show()
            // Abre HistorialFragment aquí si ya existe
        }

        btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Cerrando sesión", Toast.LENGTH_SHORT).show()

            // Ejemplo de volver al LoginFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        return view
    }
}
