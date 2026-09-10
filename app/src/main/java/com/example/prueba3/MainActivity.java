package com.example.prueba3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText txtECorreo;
    private EditText txtEContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (vista, insets) -> {
            Insets barrasSistema = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            vista.setPadding(barrasSistema.left, barrasSistema.top, barrasSistema.right, barrasSistema.bottom);
            return insets;
        });

        txtECorreo = findViewById(R.id.txtECorreo);
        txtEContrasena = findViewById(R.id.txtEContrasena);
    }

    public void abrirRegistro(View vista) {
        Intent intencion = new Intent(this, RegistroActivity.class);
        startActivity(intencion);
    }

    public void ingresarTaskMaster(View vista) {
        String correo = txtECorreo.getText().toString().trim();
        String contrasena = txtEContrasena.getText().toString().trim();

        if (correo.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Por favor ingresa un correo electrónico valido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
        Intent intencion = new Intent(this, TaskMasterActivity.class);
        startActivity(intencion);
    }
}
