package com.example.prueba3;

public class TaskModel {
    private long id;
    private String titulo;
    private String categoria;
    private float nivelUrgencia;
    private boolean estaCompletada;
    private boolean esImportante;

    public TaskModel(long id, String titulo, String categoria, float nivelUrgencia, boolean estaCompletada, boolean esImportante) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.nivelUrgencia = nivelUrgencia;
        this.estaCompletada = estaCompletada;
        this.esImportante = esImportante;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(float nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public boolean estaCompletada() {
        return estaCompletada;
    }

    public void setEstaCompletada(boolean estaCompletada) {
        this.estaCompletada = estaCompletada;
    }

    public boolean esImportante() {
        return esImportante;
    }

    public void setEsImportante(boolean esImportante) {
        this.esImportante = esImportante;
    }
}
