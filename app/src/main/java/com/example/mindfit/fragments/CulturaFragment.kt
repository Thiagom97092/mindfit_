package com.example.mindfit.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mindfit.databinding.FragmentCulturaBinding
import com.example.mindfit.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*



class CulturaFragment : Fragment() {

    private var _binding: FragmentCulturaBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private var fechaHoraSeleccionada: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCulturaBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())

        val clases = listOf("Música", "Pintura", "Danza", "Teatro")
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clases)
        binding.spinnerClasesCultura.adapter = adaptador

        binding.btnSelectDate.setOnClickListener { mostrarDatePicker() }
        binding.btnSelectTime.setOnClickListener { mostrarTimePicker() }
        binding.btnGuardarReservaCultura.setOnClickListener { guardarCita() }

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
        val clase = binding.spinnerClasesCultura.selectedItem?.toString().orEmpty()

        if (fechaHora == null || clase.isBlank() || fechaHora.before(Calendar.getInstance())) {
            Toast.makeText(context, "Clase, fecha u hora inválidas", Toast.LENGTH_SHORT).show()
            return
        }

        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fechaHora.time)
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(fechaHora.time)

        val resultado = dbHelper.saveCita("Cultura", fecha, hora, clase)
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
