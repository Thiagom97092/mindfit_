package com.example.mindfit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mindfit.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnReservas = view.findViewById<Button>(R.id.btn_reservas)
        val btnQr = view.findViewById<Button>(R.id.btn_qr)
        val btnHistorial = view.findViewById<Button>(R.id.btn_historial)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        btnReservas.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir Reservas", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReservasFragment())
                .addToBackStack(null)
                .commit()
        }

        btnQr.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, QRFragment())
                .addToBackStack(null)
                .commit()
        }

        btnHistorial.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir Historial", Toast.LENGTH_SHORT).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AppointmentHistoryFragment())
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
