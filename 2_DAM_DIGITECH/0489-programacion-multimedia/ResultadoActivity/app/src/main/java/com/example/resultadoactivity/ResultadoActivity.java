package com.example.resultadoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultadoActivity extends AppCompatActivity {
    private TextView tvResultado;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        initComponents();
        setupListeners();
        mostrarDatos();
    }

    private void initComponents() {
        tvResultado = findViewById(R.id.tvResultado);
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void setupListeners() {
        btnVolver.setOnClickListener(v -> finish());
    }

    private void mostrarDatos() {
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("NOMBRE");
        String edad = intent.getStringExtra("EDAD");
        String cursos = intent.getStringExtra("CURSOS");
        String turno = intent.getStringExtra("TURNO");

        String textoFinal = "Nombre: " + nombre +
                "\nEdad: " + edad +
                "\nCursos: " + cursos +
                "\nTurno: " + turno;

        tvResultado.setText(textoFinal);
    }
}