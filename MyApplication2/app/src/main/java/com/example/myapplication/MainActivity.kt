package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    companion object {
        const val GEMAIL = "kiarita@gmail.com"
        const val GPASS = "12345"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        val emailInput = findViewById<EditText>(R.id.email)
        val passInput = findViewById<EditText>(R.id.pass)
        val btn = findViewById<Button>(R.id.button)
        val estado = findViewById<TextView>(R.id.estado)

        btn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val pass = passInput.text.toString().trim()
            when {
                email.isEmpty() || pass.isEmpty() ->
                    estado.text = getString(R.string.vacio)

                email != GEMAIL ->
                    estado.text = getString(R.string.email_incorrecto)

                pass != GPASS ->
                    estado.text = getString(R.string.pass_incorrecto)

                else -> {
                    val intent = Intent(this, ActivityTwo::class.java)
                    intent.putExtra("Email", email)
                    startActivity(intent)
                }

            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
    }
}