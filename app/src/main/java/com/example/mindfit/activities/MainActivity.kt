package com.example.mindfit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mindfit.R
import com.example.mindfit.fragments.WelcomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mostrar el fragmento de bienvenida al iniciar
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, WelcomeFragment())
            .commit()
    }
}
