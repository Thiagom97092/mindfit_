package com.example.mindfit.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import com.example.mindfit.R
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendar_view)
        selectedDateText = view.findViewById(R.id.tv_selected_date)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val date = dateFormat.format(calendar.time)
            selectedDateText.text = "Fecha seleccionada: $date"
        }

        return view
    }
}
