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

class CulturaFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var selectedDate: Calendar
    private lateinit var tvFechaHora: TextView
    private lateinit var spinnerClases: Spinner

    private val clasesCultura = listOf("Danza", "Teatro", "Pintura", "Música")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        databaseHelper = DatabaseHelper(requireContext())
        selectedDate = Calendar.getInstance()
        tvFechaHora = view.findViewById(R.id.tvFechaHoraSeleccionada)
        spinnerClases = view.findViewById(R.id.spinner_clases)

        val btnFecha = view.findViewById<Button>(R.id.btnSeleccionarFecha)
        val btnHora = view.findViewById<Button>(R.id.btnSeleccionarHora)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCita)

        // Configurar spinner
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, clasesCultura)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerClases.adapter = adapter

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
            val claseSeleccionada = spinnerClases.selectedItem.toString()

            if (databaseHelper.citaYaExiste(fechaHora)) {
                Toast.makeText(requireContext(), "Ya hay una cita registrada en ese horario. Elige otro.", Toast.LENGTH_SHORT).show()
            } else {
                val resultado = databaseHelper.insertarCita(Cita(tipo = "Cultura", fechaHora = fechaHora, clase = claseSeleccionada))
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
            ahora.get(Calendar.YEAR),
            ahora.get(Calendar.MONTH),
            ahora.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = ahora.timeInMillis
        }.show()
    }

    private fun mostrarSelectorHora() {
        val ahora = Calendar.getInstance()

        val hour = ahora.get(Calendar.HOUR_OF_DAY)
        val minute = ahora.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = Calendar.getInstance().apply {
                    timeInMillis = selectedDate.timeInMillis
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                }

                val esHoy = DateUtils.isToday(selectedTime.timeInMillis)
                if ((esHoy && selectedTime.after(ahora)) || !esHoy) {
                    selectedDate.set(Calendar.HOUR_OF_DAY, selectedHour)
                    selectedDate.set(Calendar.MINUTE, selectedMinute)
                    selectedDate.set(Calendar.SECOND, 0)
                    actualizarTextoFechaHora()
                } else {
                    Toast.makeText(requireContext(), "Selecciona una hora futura válida.", Toast.LENGTH_SHORT).show()
                }
            },
            hour, minute, true
        ).show()
    }

    private fun actualizarTextoFechaHora() {
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        tvFechaHora.text = "Fecha y hora seleccionadas: ${formato.format(selectedDate.time)}"
    }
}
