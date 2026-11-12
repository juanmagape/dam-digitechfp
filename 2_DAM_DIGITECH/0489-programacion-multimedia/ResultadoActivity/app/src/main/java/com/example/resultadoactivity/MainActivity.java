package com.example.resultadoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre, etEdad;
    private CheckBox cbJava, cbKotlin;
    private RadioGroup rgTurno;
    private RadioButton rbManana;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setupListeners();
    }

    private void initComponents() {
        etNombre = findViewById(R.id.etNombre);
        etEdad = findViewById(R.id.etEdad);
        cbJava = findViewById(R.id.cbJava);
        cbKotlin = findViewById(R.id.cbKotlin);
        rgTurno = findViewById(R.id.rgTurno);
        rbManana = findViewById(R.id.rbManana);
        btnEnviar = findViewById(R.id.btnEnviar);
    }

    private void setupListeners() {
        btnEnviar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String edad = etEdad.getText().toString().trim();

            if (nombre.isEmpty() || edad.isEmpty()) {
                Toast.makeText(this, "Nombre y edad son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!cbJava.isChecked() && !cbKotlin.isChecked()) {
                Toast.makeText(this, "Debes seleccionar al menos un curso", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (rgTurno.getCheckedRadioButtonId()) {
                case -1:
                    Toast.makeText(this, "Debes seleccionar un turno", Toast.LENGTH_SHORT).show();
                    return;
            }

            String cursos = "";
            if (cbJava.isChecked()) cursos += "Java ";
            if (cbKotlin.isChecked()) cursos += "Kotlin";

            String turno = rbManana.isChecked() ? "Mañana" : "Tarde";

            Intent intent = new Intent(this, ResultadoActivity.class);
            intent.putExtra("NOMBRE", nombre);
            intent.putExtra("EDAD", edad);
            intent.putExtra("CURSOS", cursos.trim());
            intent.putExtra("TURNO", turno);
            startActivity(intent);
        });
    }
}