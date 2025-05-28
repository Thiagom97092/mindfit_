package com.example.mindfit.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mindfit.R
import com.example.mindfit.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class CulturaReservaFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private lateinit var spinner: Spinner
    private var correo: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cultura, container, false)
        dbHelper = DatabaseHelper(requireContext())

        correo = arguments?.getString("correo")

        spinner = view.findViewById(R.id.spinner_clases_cultura)
        val clases = arrayOf("Pintura", "Baile", "Teatro", "Música")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clases)

        val btnFecha = view.findViewById<Button>(R.id.btn_select_date)
        val btnHora = view.findViewById<Button>(R.id.btn_select_time)
        val btnGuardar = view.findViewById<Button>(R.id.btn_guardar_reserva_cultura)

        btnFecha.setOnClickListener { mostrarSelectorFecha() }
        btnHora.setOnClickListener { mostrarSelectorHora() }

        btnGuardar.setOnClickListener {
            val clase = spinner.selectedItem.toString()
            if (::selectedDate.isInitialized && ::selectedTime.isInitialized) {
                val resultado = dbHelper.saveCita(
                    correo.orEmpty(), "Cultura", selectedDate, selectedTime, clase
                )
                if (resultado) {
                    Toast.makeText(requireContext(), "Cita guardada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Ya existe una cita para esa fecha y hora", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Selecciona fecha y hora", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun mostrarSelectorFecha() {
        val calendario = Calendar.getInstance()
        val datePicker = DatePickerDialog(requireContext(),
            { _, año, mes, dia ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                calendario.set(año, mes, dia)
                if (calendario.time.after(Date())) {
                    selectedDate = sdf.format(calendario.time)
                } else {
                    Toast.makeText(requireContext(), "Selecciona una fecha futura", Toast.LENGTH_SHORT).show()
                }
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun mostrarSelectorHora() {
        val ahora = Calendar.getInstance()
        val timePicker = TimePickerDialog(requireContext(),
            { _, hora, minuto ->
                val seleccionada = Calendar.getInstance()
                seleccionada.set(Calendar.HOUR_OF_DAY, hora)
                seleccionada.set(Calendar.MINUTE, minuto)
                if (seleccionada.time.after(Date())) {
                    selectedTime = String.format("%02d:%02d", hora, minuto)
                } else {
                    Toast.makeText(requireContext(), "Selecciona una hora futura", Toast.LENGTH_SHORT).show()
                }
            },
            ahora.get(Calendar.HOUR_OF_DAY),
            ahora.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }
}
