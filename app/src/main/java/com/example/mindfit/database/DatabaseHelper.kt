package com.example.mindfit.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "mindfit.db"
        const val DATABASE_VERSION = 2

        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_EMAIL = "email"

        const val TABLE_APPOINTMENTS = "appointments"
        const val COLUMN_APPOINTMENT_ID = "appointment_id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"

        const val TABLE_CITAS = "citas"
        const val COLUMN_TIPO = "tipo"
        const val COLUMN_FECHA_HORA = "fecha_hora"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE
            );
        """.trimIndent()

        val createAppointmentsTable = """
            CREATE TABLE $TABLE_APPOINTMENTS (
                $COLUMN_APPOINTMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_TYPE TEXT NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_TIME TEXT NOT NULL,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)
            );
        """.trimIndent()

        val createCitasTable = """
        CREATE TABLE $TABLE_CITAS (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TIPO TEXT NOT NULL,
            $COLUMN_FECHA_HORA TEXT NOT NULL UNIQUE
            );
        """.trimIndent()
        db.execSQL(createCitasTable)


        db.execSQL(createUsersTable)
        db.execSQL(createAppointmentsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        onCreate(db)
    }

    // Insertar un usuario
    fun insertUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.name)
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_PASSWORD, user.password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    // Verificar si un usuario ya existe por email
    fun checkUserExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Verificar usuario por nombre y contraseña
    fun checkUser(username: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID, COLUMN_USERNAME, COLUMN_EMAIL),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            user = User(id, username, password, email)
        }
        cursor.close()
        return user
    }

    // Verificar usuario por email y contraseña (para LoginFragment)
    fun checkUserByEmailAndPassword(email: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID, COLUMN_USERNAME, COLUMN_EMAIL),
            "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
            user = User(id, username, password, email)
        }
        cursor.close()
        return user
    }

    // Obtener usuario por ID
    fun getUserById(id: Int): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            user = User(id, username, password, email)
        }
        cursor.close()
        return user
    }

    // Insertar cita
    fun insertAppointment(userId: Int, type: String, date: String, time: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_TYPE, type)
            put(COLUMN_DATE, date)
            put(COLUMN_TIME, time)
        }
        return db.insert(TABLE_APPOINTMENTS, null, values)
    }

    // Verificar si ya existe una cita en esa fecha y hora
    fun isAppointmentSlotTaken(date: String, time: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_APPOINTMENTS,
            arrayOf(COLUMN_APPOINTMENT_ID),
            "$COLUMN_DATE = ? AND $COLUMN_TIME = ?",
            arrayOf(date, time),
            null, null, null
        )
        val taken = cursor.count > 0
        cursor.close()
        return taken
    }

    fun citaYaExiste(fechaHora: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CITAS,
            arrayOf(COLUMN_ID),
            "$COLUMN_FECHA_HORA = ?",
            arrayOf(fechaHora),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertarCita(cita: Cita): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TIPO, cita.tipo)
            put(COLUMN_FECHA_HORA, cita.fechaHora)
        }
        return db.insert(TABLE_CITAS, null, values)
    }
    fun insertarCita(tipo: String, clase: String, fecha: String, hora: String): Boolean {
        val db = writableDatabase

        val cursor = db.query(
            "citas",
            arrayOf("id"),
            "tipo = ? AND fecha = ? AND hora = ?",
            arrayOf(tipo, fecha, hora),
            null, null, null
        )

        val yaExiste = cursor.moveToFirst()
        cursor.close()

        if (yaExiste) return false

        val values = ContentValues().apply {
            put("tipo", tipo)
            put("clase", clase)
            put("fecha", fecha)
            put("hora", hora)
        }

        return db.insert("citas", null, values) != -1L
    }

    fun checkUserExistsByEmail(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun updatePasswordByEmail(email: String, newPassword: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("password", newPassword)
        }
        val rows = db.update("users", values, "email = ?", arrayOf(email))
        db.close()
        return rows > 0
    }

    fun getAllAppointments(): List<String> {
        val citas = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT tipo, fechaHora FROM citas ORDER BY fechaHora", null)

        if (cursor.moveToFirst()) {
            do {
                val tipo = cursor.getString(0)
                val fechaHora = cursor.getString(1)
                citas.add("🗓️ $tipo - $fechaHora")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return citas
    }



}
