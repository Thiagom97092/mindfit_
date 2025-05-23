package com.example.mindfit.database

data class Cita(
    val id: Int = 0,
    val tipo: String,
    val fechaHora: String,
    val clase: String = "",
    val fecha: String = "",
    val hora: String = ""
)
