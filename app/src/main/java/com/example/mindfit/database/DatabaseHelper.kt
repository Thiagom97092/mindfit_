package com.example.mindfit.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "MindFitDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, correo TEXT, contrasena TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS citas (id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT, fecha TEXT, hora TEXT, clase TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS citas")
        onCreate(db)
    }

    // --------- Usuarios ---------

    fun insertUser(nombre: String, correo: String, contrasena: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("contrasena", contrasena)
        }
        val result = db.insert("usuarios", null, values)
        return result != -1L
    }

    fun checkUserExists(correo: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE correo = ?", arrayOf(correo))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getUserNameByEmailAndPassword(correo: String, contrasena: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT nombre FROM usuarios WHERE correo = ? AND contrasena = ?",
            arrayOf(correo, contrasena)
        )
        var name: String? = null
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
        }
        cursor.close()
        return name
    }

    fun updatePasswordByEmail(correo: String, nuevaContrasena: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("contrasena", nuevaContrasena)
        }
        val result = db.update("usuarios", values, "correo = ?", arrayOf(correo))
        return result > 0
    }

    // --------- Citas ---------

    fun saveCita(tipo: String, fecha: String, hora: String, clase: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM citas WHERE fecha = ? AND hora = ?", arrayOf(fecha, hora))
        if (cursor.count > 0) {
            cursor.close()
            return false
        }
        cursor.close()

        val values = ContentValues().apply {
            put("tipo", tipo)
            put("fecha", fecha)
            put("hora", hora)
            put("clase", clase)
        }
        val result = db.insert("citas", null, values)
        return result != -1L
    }

    // --------- Historial de citas ---------

    fun getAllAppointments(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM citas ORDER BY fecha, hora", null)
    }

    fun getAllAppointmentsAsList(): List<String> {
        val citas = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM citas ORDER BY fecha, hora", null)

        if (cursor.moveToFirst()) {
            do {
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
                val clase = cursor.getString(cursor.getColumnIndexOrThrow("clase"))
                citas.add("Tipo: $tipo\nFecha: $fecha\nHora: $hora\nClase: $clase")
            } while (cursor.moveToNext())
        }

        cursor.close()
        return citas
    }
}
