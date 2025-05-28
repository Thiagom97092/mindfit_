package com.example.mindfit.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mindfit.R
import com.example.mindfit.utils.DatabaseHelper

class AppointmentHistoryFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())
        listView = view.findViewById(R.id.listview_historial)

        val sharedPref = requireContext().getSharedPreferences("MindFitPrefs", Context.MODE_PRIVATE)
        val correo = sharedPref.getString("user_email", null)

        if (correo != null) {
            val citas = databaseHelper.obtenerCitasPorCorreo(correo)

            if (citas.isEmpty()) {
                Toast.makeText(requireContext(), "No hay citas registradas", Toast.LENGTH_SHORT).show()
            } else {
                val adapter = ArrayAdapter(requireContext(), R.layout.list_item_white, R.id.text1, citas)
                listView.adapter = adapter
            }
        } else {
            Toast.makeText(requireContext(), "Usuario no identificado", Toast.LENGTH_SHORT).show()
        }
    }
}
