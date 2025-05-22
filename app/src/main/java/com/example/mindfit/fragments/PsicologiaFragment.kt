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

class PsicologiaFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var selectedDate: Calendar
    private lateinit var tvFechaHora: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        databaseHelper = DatabaseHelper(requireContext())
        selectedDate = Calendar.getInstance()
        tvFechaHora = view.findViewById(R.id.tvFechaHoraSeleccionada)

        val btnFecha = view.findViewById<Button>(R.id.btnSeleccionarFecha)
        val btnHora = view.findViewById<Button>(R.id.btnSeleccionarHora)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCita)

        btnFecha.setOnClickListener { mostrarSelectorFecha() }
        btnHora.setOnClickListener { mostrarSelectorHora() }

        btnGuardar.setOnClickListener {
            val ahora = Calendar.getInstance()
            if (selectedDate.before(ahora) || selectedDate.time == ahora.time) {
                Toast.makeText(requireContext(), "No puedes seleccionar una fecha/hora pasada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val fechaHora = formato.format(selectedDate.time)

            if (databaseHelper.citaYaExiste(fechaHora)) {
                Toast.makeText(requireContext(), "Ya hay una cita registrada en ese horario. Elige otro.", Toast.LENGTH_SHORT).show()
            } else {
                val resultado = databaseHelper.insertarCita(Cita(tipo = "Psicología", fechaHora = fechaHora))

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
