package com.example.prueba3;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private EditText txtECorreo;
    private EditText txtEContrasena;
    private Button btnHabla;

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
        btnHabla = findViewById(R.id.btnHabla);


        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.JAPANESE);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts.setLanguage(Locale.forLanguageTag("es-ES"));
                }
            }
        });

        btnHabla.setOnClickListener(v -> hablar());
    }

    private void hablar() {
        if (tts != null) {
            tts.setPitch(1.0f);
            tts.setSpeechRate(1.0f);
            tts.speak("Hola Papus", TextToSpeech.QUEUE_FLUSH, null, "TTS_MSG_ID");
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
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
