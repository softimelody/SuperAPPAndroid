package com.example.prueba3;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OyenteCambioEstadoTarea {
        void alCambiarEstado(TaskModel tarea, boolean estaCompletada);
    }

    public interface OyenteEliminarTarea {
        void alEliminar(TaskModel tarea);
    }

    private List<TaskModel> listaTareas;
    private OyenteCambioEstadoTarea oyenteEstado;
    private OyenteEliminarTarea oyenteEliminar;

    public TaskAdapter(List<TaskModel> listaTareas, OyenteCambioEstadoTarea oyenteEstado, OyenteEliminarTarea oyenteEliminar) {
        this.listaTareas = listaTareas;
        this.oyenteEstado = oyenteEstado;
        this.oyenteEliminar = oyenteEliminar;
    }

    public void actualizarTareas(List<TaskModel> nuevasTareas) {
        this.listaTareas = nuevasTareas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup padre, int tipoVista) {
        View vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_task, padre, false);
        return new TaskViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder contenedor, int posicion) {
        TaskModel tarea = listaTareas.get(posicion);
        contenedor.vincular(tarea);
    }

    @Override
    public int getItemCount() {
        return listaTareas != null ? listaTareas.size() : 0;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        ImageView imgVIconoCategoria;
        CheckBox chkTareaCompletada;
        TextView txtVTituloTarea;
        TextView txtVCategoriaTarea;
        TextView txtVInsigniaImportante;
        RatingBar rtgBUrgenciaMuestra;
        Button btnEliminarTarea;

        public TaskViewHolder(@NonNull View vistaElemento) {
            super(vistaElemento);
            imgVIconoCategoria = vistaElemento.findViewById(R.id.imgVIconoCategoria);
            chkTareaCompletada = vistaElemento.findViewById(R.id.chkTareaCompletada);
            txtVTituloTarea = vistaElemento.findViewById(R.id.txtVTituloTarea);
            txtVCategoriaTarea = vistaElemento.findViewById(R.id.txtVCategoriaTarea);
            txtVInsigniaImportante = vistaElemento.findViewById(R.id.txtVInsigniaImportante);
            rtgBUrgenciaMuestra = vistaElemento.findViewById(R.id.rtgBUrgenciaMuestra);
            btnEliminarTarea = vistaElemento.findViewById(R.id.btnEliminarTarea);
        }

        public void vincular(final TaskModel tarea) {
            txtVTituloTarea.setText(tarea.getTitulo());
            txtVCategoriaTarea.setText(tarea.getCategoria());
            rtgBUrgenciaMuestra.setRating(tarea.getNivelUrgencia());

            // checkbox
            chkTareaCompletada.setOnCheckedChangeListener(null);
            chkTareaCompletada.setChecked(tarea.estaCompletada());

            if (tarea.estaCompletada()) {
                txtVTituloTarea.setPaintFlags(txtVTituloTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txtVTituloTarea.setAlpha(0.6f);
            } else {
                txtVTituloTarea.setPaintFlags(txtVTituloTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                txtVTituloTarea.setAlpha(1.0f);
            }

            chkTareaCompletada.setOnCheckedChangeListener((vistaBoton, estaMarcado) -> {
                tarea.setEstaCompletada(estaMarcado);
                if (estaMarcado) {
                    txtVTituloTarea.setPaintFlags(txtVTituloTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    txtVTituloTarea.setAlpha(0.6f);
                } else {
                    txtVTituloTarea.setPaintFlags(txtVTituloTarea.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    txtVTituloTarea.setAlpha(1.0f);
                }
                if (oyenteEstado != null) {
                    oyenteEstado.alCambiarEstado(tarea, estaMarcado);
                }
            });

            // Insignia de importante
            if (tarea.esImportante()) {
                txtVInsigniaImportante.setVisibility(View.VISIBLE);
            } else {
                txtVInsigniaImportante.setVisibility(View.GONE);
            }

            // Ícono de categoría
            if ("Trabajo".equalsIgnoreCase(tarea.getCategoria())) {
                imgVIconoCategoria.setImageResource(R.drawable.ic_work);
            } else if ("Estudios".equalsIgnoreCase(tarea.getCategoria())) {
                imgVIconoCategoria.setImageResource(R.drawable.ic_study);
            } else {
                imgVIconoCategoria.setImageResource(R.drawable.ic_personal);
            }

            // Botón eliminar
            btnEliminarTarea.setOnClickListener(vista -> {
                if (oyenteEliminar != null) {
                    oyenteEliminar.alEliminar(tarea);
                }
            });
        }
    }
}
