package com.example.mindfit.database

data class User(
    val id: Int = 0, // O puede ser Int
    val name: String,
    val email: String,
    val password: String
)
