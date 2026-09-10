package com.example.prueba3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskMasterActivity extends AppCompatActivity {

    private EditText txtETituloTarea;
    private RadioGroup rgCategoriaTarea;
    private RatingBar rtgBUrgenciaEntrada;
    private CheckBox chkTareaImportante;
    private Button btnGuardarTarea;

    private Spinner spnFiltroCategoria;
    private ProgressBar prgBProgresoTareas;
    private TextView txtVPorcentajeProgreso;

    private TextView txtVEstadisticaTotal;
    private TextView txtVEstadisticaPendientes;
    private TextView txtVEstadisticaCompletadas;
    private TextView txtVListaVacia;

    private RecyclerView recVListaTareas;
    private TaskAdapter adaptadorTareas;

    private final List<TaskModel> listaTareasCompleta = new ArrayList<>();
    private final List<TaskModel> listaTareasMostrada = new ArrayList<>();
    private long contadorIdTarea = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_master);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (vista, insets) -> {
            Insets barrasSistema = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            vista.setPadding(barrasSistema.left, barrasSistema.top, barrasSistema.right, barrasSistema.bottom);
            return insets;
        });

        inicializarVistas();
        configurarSpinner();
        configurarRecyclerView();
        agregarTareasIniciales();
        actualizarPanel();

        btnGuardarTarea.setOnClickListener(vista -> agregarNuevaTarea());
    }

    private void inicializarVistas() {
        txtETituloTarea = findViewById(R.id.txtETituloTarea);
        rgCategoriaTarea = findViewById(R.id.rgCategoriaTarea);
        rtgBUrgenciaEntrada = findViewById(R.id.rtgBUrgenciaEntrada);
        chkTareaImportante = findViewById(R.id.chkTareaImportante);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);

        spnFiltroCategoria = findViewById(R.id.spnFiltroCategoria);
        prgBProgresoTareas = findViewById(R.id.prgBProgresoTareas);
        txtVPorcentajeProgreso = findViewById(R.id.txtVPorcentajeProgreso);

        txtVEstadisticaTotal = findViewById(R.id.txtVEstadisticaTotal);
        txtVEstadisticaPendientes = findViewById(R.id.txtVEstadisticaPendientes);
        txtVEstadisticaCompletadas = findViewById(R.id.txtVEstadisticaCompletadas);
        txtVListaVacia = findViewById(R.id.txtVListaVacia);

        recVListaTareas = findViewById(R.id.recVListaTareas);
    }

    private void configurarSpinner() {
        String[] categorias = new String[]{
                getString(R.string.aw_tbl_act).equals("Disponible") ? "Todas las categorías" : "Todas",
                getString(R.string.tm_cat_trabajo),
                getString(R.string.tm_cat_personal),
                getString(R.string.tm_cat_estudios)
        };

        ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categorias
        );
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFiltroCategoria.setAdapter(adaptadorSpinner);

        spnFiltroCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> padre, View vista, int posicion, long id) {
                filtrarTareas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> padre) {
            }
        });
    }

    private void configurarRecyclerView() {
        recVListaTareas.setLayoutManager(new LinearLayoutManager(this));
        adaptadorTareas = new TaskAdapter(listaTareasMostrada,
                (tarea, estaCompletada) -> actualizarPanel(),
                tarea -> {
                    listaTareasCompleta.remove(tarea);
                    filtrarTareas();
                    Toast.makeText(TaskMasterActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                });
        recVListaTareas.setAdapter(adaptadorTareas);
    }

    private void agregarTareasIniciales() {
        listaTareasCompleta.add(new TaskModel(contadorIdTarea++, "Diseñar interfaz soft pastel y tornasol", "Trabajo", 5.0f, true, true));
        listaTareasCompleta.add(new TaskModel(contadorIdTarea++, "Organizar tareas de la semana", "Personal", 3.0f, false, false));
        listaTareasCompleta.add(new TaskModel(contadorIdTarea++, "Estudiar layouts en Android Java", "Estudios", 4.0f, false, true));
    }

    private void agregarNuevaTarea() {
        String titulo = txtETituloTarea.getText().toString().trim();
        if (titulo.isEmpty()) {
            Toast.makeText(this, "Por favor escribe una descripción para la tarea", Toast.LENGTH_SHORT).show();
            return;
        }

        int idRadioSeleccionado = rgCategoriaTarea.getCheckedRadioButtonId();
        String categoria;
        if (idRadioSeleccionado == R.id.rbPersonal) {
            categoria = getString(R.string.tm_cat_personal);
        } else if (idRadioSeleccionado == R.id.rbEstudios) {
            categoria = getString(R.string.tm_cat_estudios);
        } else {
            categoria = getString(R.string.tm_cat_trabajo);
        }

        float urgencia = rtgBUrgenciaEntrada.getRating();
        boolean esImportante = chkTareaImportante.isChecked();

        TaskModel nuevaTarea = new TaskModel(contadorIdTarea++, titulo, categoria, urgencia, false, esImportante);
        listaTareasCompleta.add(0, nuevaTarea);

        // Limpiar formulario
        txtETituloTarea.setText("");
        rgCategoriaTarea.check(R.id.rbTrabajo);
        rtgBUrgenciaEntrada.setRating(3.0f);
        chkTareaImportante.setChecked(false);

        filtrarTareas();
        Toast.makeText(this, "Tarea añadida con éxito", Toast.LENGTH_SHORT).show();
    }

    private void filtrarTareas() {
        String categoriaSeleccionada = spnFiltroCategoria.getSelectedItem().toString();
        listaTareasMostrada.clear();

        if (categoriaSeleccionada.startsWith("Todas")) {
            listaTareasMostrada.addAll(listaTareasCompleta);
        } else {
            for (TaskModel tarea : listaTareasCompleta) {
                if (tarea.getCategoria().equalsIgnoreCase(categoriaSeleccionada)) {
                    listaTareasMostrada.add(tarea);
                }
            }
        }

        adaptadorTareas.actualizarTareas(listaTareasMostrada);
        if (listaTareasMostrada.isEmpty()) {
            txtVListaVacia.setVisibility(View.VISIBLE);
        } else {
            txtVListaVacia.setVisibility(View.GONE);
        }

        actualizarPanel();
    }

    private void actualizarPanel() {
        int total = listaTareasCompleta.size();
        int conteoCompletadas = 0;

        for (TaskModel tarea : listaTareasCompleta) {
            if (tarea.estaCompletada()) {
                conteoCompletadas++;
            }
        }

        int conteoPendientes = total - conteoCompletadas;
        int porcentaje = total > 0 ? (conteoCompletadas * 100) / total : 0;

        prgBProgresoTareas.setProgress(porcentaje);
        txtVPorcentajeProgreso.setText(getString(R.string.tm_percent_format, porcentaje));

        txtVEstadisticaTotal.setText(getString(R.string.tm_stats_total, total));
        txtVEstadisticaPendientes.setText(getString(R.string.tm_stats_pendientes, conteoPendientes));
        txtVEstadisticaCompletadas.setText(getString(R.string.tm_stats_completadas, conteoCompletadas));
    }
}
