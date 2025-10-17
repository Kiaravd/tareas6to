package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class ActivityTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        val emailView = findViewById<TextView>(R.id.textViewEmail)
        val mensajeView = findViewById<TextView>(R.id.textViewMensaje)

        val email = intent.getStringExtra("Email")

        emailView.text = "Bienvenido, $email"
        mensajeView.text = "Login exitoso"
    }
}
