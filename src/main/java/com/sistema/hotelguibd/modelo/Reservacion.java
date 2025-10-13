package com.sistema.hotelguibd.modelo;

import java.time.LocalDate;

public class Reservacion {
    private int id;
    private Habitacion habitacion;
    private Cliente cliente;
    private LocalDate fechaReservacion;
    private LocalDate fechaLlegada;
    private int cantidadNoches;
    private String fechaSalida;
    private double precioTotal;
    private double descuento;

    // Clase que representa una reservación de hotel.
    // Contiene información sobre la habitación, el cliente, las fechas de reservación y llegada, la cantidad de noches, el precio total y un posible descuento.

    // Constructor sin parámetros
    public Reservacion() {}

    // Constructor con parámetros
    public Reservacion(int id, Habitacion habitacion, Cliente cliente, LocalDate fechaReservacion, LocalDate fechaLlegada, int cantidadNoches) {
        this.id = id;
        this.habitacion = habitacion;
        this.cliente = cliente;
        this.fechaReservacion = fechaReservacion;
        this.fechaLlegada = fechaLlegada;
        this.cantidadNoches = cantidadNoches;
    }

    // Getters
    public int getCantidadNoches() { return cantidadNoches; }
    public Cliente getCliente() { return cliente; }
    public double getDescuento() { return descuento; }
    public LocalDate getFechaLlegada() { return fechaLlegada; }
    public LocalDate getFechaReservacion() { return fechaReservacion; }
    public String getFechaSalida() { return fechaSalida; }
    public Habitacion getHabitacion() { return habitacion; }
    public int getId() { return id; }
    public double getPrecioTotal() { return precioTotal; }

    // Setters
    public void setCantidadNoches(int cantidadNoches) { this.cantidadNoches = cantidadNoches; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setDescuento(double descuento) { this.descuento = descuento; }
    public void setFechaLlegada(LocalDate fechaLlegada) { this.fechaLlegada = fechaLlegada; }
    public void setFechaReservacion(LocalDate fechaReservacion) { this.fechaReservacion = fechaReservacion; }
    public void setFechaSalida(String fechaSalida) { this.fechaSalida = fechaSalida; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
    public void setId(int id) { this.id = id; }
    public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }


}
