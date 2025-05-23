package com.example.mindfit.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
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
        return inflater.inflate(R.layout.fragment_psicologia, container, false)
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
        btnGuardar.setOnClickListener { guardarCitaPsicologica() }
    }

    private fun mostrarSelectorFecha() {
        val ahora = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, day)
                actualizarTextoFechaHora()
            },
            ahora.get(Calendar.YEAR),
            ahora.get(Calendar.MONTH),
            ahora.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = ahora.timeInMillis
        }.show()
    }

    private fun mostrarSelectorHora() {
        val ahora = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val selectedTime = Calendar.getInstance().apply {
                    timeInMillis = selectedDate.timeInMillis
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }

                val esHoy = DateUtils.isToday(selectedTime.timeInMillis)
                if ((esHoy && selectedTime.after(ahora)) || !esHoy) {
                    selectedDate.set(Calendar.HOUR_OF_DAY, hour)
                    selectedDate.set(Calendar.MINUTE, minute)
                    selectedDate.set(Calendar.SECOND, 0)
                    actualizarTextoFechaHora()
                } else {
                    Toast.makeText(requireContext(), "Selecciona una hora futura válida.", Toast.LENGTH_SHORT).show()
                }
            },
            ahora.get(Calendar.HOUR_OF_DAY),
            ahora.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun actualizarTextoFechaHora() {
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        tvFechaHora.text = "Fecha y hora seleccionadas: ${formato.format(selectedDate.time)}"
    }

    private fun guardarCitaPsicologica() {
        val ahora = Calendar.getInstance()
        if (selectedDate.before(ahora) || selectedDate.time == ahora.time) {
            Toast.makeText(requireContext(), "No puedes seleccionar una fecha/hora pasada", Toast.LENGTH_SHORT).show()
            return
        }

        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val fechaHora = formato.format(selectedDate.time)

        if (databaseHelper.citaYaExiste(fechaHora)) {
            Toast.makeText(requireContext(), "Ya hay una cita registrada en ese horario. Elige otro.", Toast.LENGTH_SHORT).show()
        } else {
            val resultado = databaseHelper.insertarCita(
                Cita(tipo = "Psicología", fechaHora = fechaHora, clase = "")
            )
            val mensaje = if (resultado != -1L) "Cita guardada exitosamente" else "Error al guardar la cita"
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
        }
    }
}
