package com.example.mindfit.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mindfit.databinding.FragmentPsicologiaBinding
import com.example.mindfit.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class PsicologiaFragment : Fragment() {

    private var _binding: FragmentPsicologiaBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private var fechaHoraSeleccionada: Calendar? = null
    private var correo: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPsicologiaBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())

        val sharedPref = requireContext().getSharedPreferences("MindFitPrefs", android.content.Context.MODE_PRIVATE)
        correo = sharedPref.getString("user_email", null)


        binding.btnSeleccionarFecha.setOnClickListener { mostrarDatePicker() }
        binding.btnSeleccionarHora.setOnClickListener { mostrarTimePicker() }
        binding.btnGuardarCita.setOnClickListener { guardarCita() }

        return binding.root
    }

    private fun mostrarDatePicker() {
        val calendarioActual = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, año, mes, dia ->
                if (fechaHoraSeleccionada == null) fechaHoraSeleccionada = Calendar.getInstance()
                fechaHoraSeleccionada?.set(año, mes, dia)
                actualizarTexto()
            },
            calendarioActual.get(Calendar.YEAR),
            calendarioActual.get(Calendar.MONTH),
            calendarioActual.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = calendarioActual.timeInMillis
        }.show()
    }

    private fun mostrarTimePicker() {
        if (fechaHoraSeleccionada == null) {
            Toast.makeText(context, "Selecciona primero la fecha", Toast.LENGTH_SHORT).show()
            return
        }
        val ahora = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hora, minuto ->
                fechaHoraSeleccionada?.set(Calendar.HOUR_OF_DAY, hora)
                fechaHoraSeleccionada?.set(Calendar.MINUTE, minuto)
                actualizarTexto()
            },
            ahora.get(Calendar.HOUR_OF_DAY),
            ahora.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun actualizarTexto() {
        fechaHoraSeleccionada?.let {
            val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.tvFechaHoraSeleccionada.text = "Fecha y hora seleccionadas: ${formato.format(it.time)}"
        }
    }

    private fun guardarCita() {
        val fechaHora = fechaHoraSeleccionada
        if (fechaHora == null || fechaHora.before(Calendar.getInstance())) {
            Toast.makeText(context, "Fecha y hora inválidas o no seleccionadas", Toast.LENGTH_SHORT).show()
            return
        }

        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fechaHora.time)
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(fechaHora.time)

        val resultado = dbHelper.saveCita(
            correo.orEmpty(), "Psicología", fecha, hora, ""
        )

        if (resultado) {
            Toast.makeText(context, "Cita guardada exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Ya existe una cita para ese momento", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
