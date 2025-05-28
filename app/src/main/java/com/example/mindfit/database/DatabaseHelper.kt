package com.example.mindfit.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mindfit.database.Cita

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "MindFitDB", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT," +
                    "correo TEXT UNIQUE," +
                    "contrasena TEXT)"
        )

        db.execSQL(
            "CREATE TABLE citas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "correo TEXT," +
                    "tipo TEXT," +
                    "fecha TEXT," +
                    "hora TEXT," +
                    "detalle TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS citas")
        onCreate(db)
    }

    fun insertarUsuario(nombre: String, correo: String, contrasena: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("contrasena", contrasena)
        }
        val resultado = db.insert("usuarios", null, values)
        return resultado != -1L
    }

    fun validarUsuario(correo: String, contrasena: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?",
            arrayOf(correo, contrasena)
        )
        val valido = cursor.moveToFirst()
        cursor.close()
        return valido
    }

    fun existeUsuario(correo: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE correo = ?", arrayOf(correo))
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    fun actualizarContrasena(correo: String, nuevaContrasena: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("contrasena", nuevaContrasena)
        }
        val resultado = db.update("usuarios", values, "correo = ?", arrayOf(correo))
        return resultado > 0
    }

    fun saveCita(correo: String, tipo: String, fecha: String, hora: String, detalle: String): Boolean {
        val db = this.writableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM citas WHERE fecha = ? AND hora = ? AND correo = ?",
            arrayOf(fecha, hora, correo)
        )
        if (cursor.moveToFirst()) {
            cursor.close()
            return false
        }
        cursor.close()

        val values = ContentValues().apply {
            put("correo", correo)
            put("tipo", tipo)
            put("fecha", fecha)
            put("hora", hora)
            put("detalle", detalle)
        }

        val resultado = db.insert("citas", null, values)
        return resultado != -1L
    }

    fun obtenerCitasPorCorreo(correo: String): List<Cita> {
        val db = this.readableDatabase
        val listaCitas = mutableListOf<Cita>()

        val cursor = db.rawQuery(
            "SELECT id, tipo, fecha, hora, detalle FROM citas WHERE correo = ?",
            arrayOf(correo)
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val tipo = cursor.getString(1)
                val fecha = cursor.getString(2)
                val hora = cursor.getString(3)
                val detalle = cursor.getString(4)
                val cita = Cita(
                    id = id,
                    tipo = tipo,
                    fechaHora = "$fecha $hora",
                    clase = detalle,
                    fecha = fecha,
                    hora = hora
                )
                listaCitas.add(cita)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaCitas
    }

    fun obtenerNombrePorCorreoYContrasena(correo: String, contrasena: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT nombre FROM usuarios WHERE correo = ? AND contrasena = ?",
            arrayOf(correo, contrasena)
        )
        var nombre: String? = null
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0)
        }
        cursor.close()
        return nombre
    }
}
