package com.example.mindfit.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mindfit.R
import com.example.mindfit.database.Cita
import com.example.mindfit.database.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class CulturaFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var selectedDate: Calendar
    private lateinit var tvFechaHora: TextView
    private lateinit var spinnerClase: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cultura, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        databaseHelper = DatabaseHelper(requireContext())
        selectedDate = Calendar.getInstance()
        tvFechaHora = view.findViewById(R.id.tvFechaHoraSeleccionada)
        spinnerClase = view.findViewById(R.id.spinner_clases_cultura)

        // Configurar el Spinner con una lista de clases
        val clases = listOf("Yoga", "Pintura", "Baile", "Fotografía") // Puedes personalizar esta lista
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clases)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerClase.adapter = adapter

        val btnFecha = view.findViewById<Button>(R.id.btn_select_date)
        val btnHora = view.findViewById<Button>(R.id.btn_select_time)
        val btnGuardar = view.findViewById<Button>(R.id.btn_guardar_reserva_cultura)

        btnFecha.setOnClickListener { mostrarSelectorFecha() }
        btnHora.setOnClickListener { mostrarSelectorHora() }

        btnGuardar.setOnClickListener {
            val ahora = Calendar.getInstance()
            if (selectedDate.before(ahora) || selectedDate.time == ahora.time) {
                Toast.makeText(requireContext(), "No puedes seleccionar una fecha/hora pasada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val claseSeleccionada = spinnerClase.selectedItem?.toString()?.trim() ?: ""
            if (claseSeleccionada.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor selecciona una clase", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val fechaHora = formato.format(selectedDate.time)

            if (databaseHelper.citaYaExiste(fechaHora)) {
                Toast.makeText(requireContext(), "Ya hay una cita registrada en ese horario. Elige otro.", Toast.LENGTH_SHORT).show()
            } else {
                val resultado = databaseHelper.insertarCita(
                    Cita(tipo = "Cultura", fechaHora = fechaHora, clase = claseSeleccionada)
                )
                if (resultado != -1L) {
                    Toast.makeText(requireContext(), "Cita guardada exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al guardar la cita", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarSelectorFecha() {
        val ahora = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, año, mes, dia ->
                selectedDate.set(Calendar.YEAR, año)
                selectedDate.set(Calendar.MONTH, mes)
                selectedDate.set(Calendar.DAY_OF_MONTH, dia)
                actualizarTextoFechaHora()
            },
            ahora.get(Calendar.YEAR), ahora.get(Calendar.MONTH), ahora.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun mostrarSelectorHora() {
        val ahora = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hora, minuto ->
                selectedDate.set(Calendar.HOUR_OF_DAY, hora)
                selectedDate.set(Calendar.MINUTE, minuto)
                selectedDate.set(Calendar.SECOND, 0)
                actualizarTextoFechaHora()
            },
            ahora.get(Calendar.HOUR_OF_DAY), ahora.get(Calendar.MINUTE), true
        ).show()
    }

    private fun actualizarTextoFechaHora() {
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        tvFechaHora.text = "Fecha y hora seleccionadas: ${formato.format(selectedDate.time)}"
    }
}
