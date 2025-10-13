package com.sistema.hotelguibd.modelo;

public class Habitacion {
    private int id;
    private int numero;
    private String tipo;
    private String descripcionTipo;
    private String estado;
    private String descripcionEstado;
    private double precio;
    private int capacidad;

    // Clase que representa una habitación de hotel.
    // Contiene información sobre el ID, número, tipo, estado, descripción del tipo y estado, precio y capacidad de la habitación.

    // Constructor sin parámetros
    public Habitacion() {}

    // Constructor con parámetros
    public Habitacion(int id, int numero, String estado, double precio, int capacidad) {
        this.estado = estado;
        this.id = id;
        this.numero = numero;
        this.precio = precio;
        this.capacidad = capacidad;
    }

    // Getters
    public String getDescripcionEstado() { return descripcionEstado; }
    public String getDescripcionTipo() { return descripcionTipo; }
    public String getEstado() { return estado; }
    public int getId() { return id; }
    public int getNumero() { return numero; }
    public double getPrecio() { return precio; }
    public String getTipo() { return tipo; }
    public int getCapacidad() { return capacidad; }

    // Setters
    public void setDescripcionEstado(String descripcionEstado) { this.descripcionEstado = descripcionEstado; }
    public void setDescripcionTipo(String descripcionTipo) { this.descripcionTipo = descripcionTipo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setId(int id) { this.id = id; }
    public void setNumero(int numero) { this.numero = numero; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

}