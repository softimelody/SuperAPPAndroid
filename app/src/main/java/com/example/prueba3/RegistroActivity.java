package com.example.prueba3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtECorreo;
    private EditText txtEContrasena;
    private CheckBox chkTTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (vista, insets) -> {
            Insets barrasSistema = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            vista.setPadding(barrasSistema.left, barrasSistema.top, barrasSistema.right, barrasSistema.bottom);
            return insets;
        });

        txtECorreo = findViewById(R.id.txtECorreo);
        txtEContrasena = findViewById(R.id.txtEContrasena);
        chkTTerminos = findViewById(R.id.chkTTerminos);
    }

    public void btnRegistrarse(View vista) {
        String correo = txtECorreo != null ? txtECorreo.getText().toString().trim() : "";
        String contrasena = txtEContrasena != null ? txtEContrasena.getText().toString().trim() : "";

        if (correo.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (chkTTerminos != null && !chkTTerminos.isChecked()) {
            Toast.makeText(this, "Por favor acepta las políticas y términos para continuar", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Registro completado exitosamente", Toast.LENGTH_SHORT).show();
        Intent intencion = new Intent(this, TaskMasterActivity.class);
        startActivity(intencion);
        finish();
    }
}
