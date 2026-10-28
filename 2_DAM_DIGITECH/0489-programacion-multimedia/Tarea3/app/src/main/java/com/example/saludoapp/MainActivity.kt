package com.example.saludoapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val btnSaludar = findViewById<Button>(R.id.btnSaludar)
        val showResultado = findViewById<TextView>(R.id.tvResultado)

        btnSaludar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()

            if (nombre.isNotEmpty()) {
                val mensaje = ">> HOLA, ${nombre.uppercase()} ★"
                showResultado.text = mensaje

                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, escribe un nombre", Toast.LENGTH_SHORT).show()
            }
        }
    }
}