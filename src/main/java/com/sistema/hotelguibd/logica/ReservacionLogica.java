package com.sistema.hotelguibd.logica;

import com.sistema.hotelguibd.datos.ReservacionDatos;
import com.sistema.hotelguibd.modelo.Reservacion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservacionLogica {

    private final ReservacionDatos store = new ReservacionDatos();

    // -------- Lectura --------
    public List<Reservacion> findAll() throws SQLException {
        return store.findAll();
    }

    public Reservacion findById(int id) throws SQLException {
        if (id <= 0) return null;
        return store.findById(id);
    }

    // -------- Escritura --------
    public Reservacion create(Reservacion nueva) throws SQLException {
        validar(nueva, true);
        return store.insert(nueva);
    }

    public Reservacion update(Reservacion r) throws SQLException {
        if (r == null || r.getId() <= 0)
            throw new IllegalArgumentException("La reservación a actualizar requiere un ID válido.");
        validar(r, false);
        return store.update(r);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // -------- Validación / Normalización --------
    private void validar(Reservacion r, boolean isCreate) {
        if (r == null)
            throw new IllegalArgumentException("Reservación nula.");
        if (r.getCliente() == null || r.getCliente().getId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un cliente válido.");
        if (r.getHabitacion() == null || r.getHabitacion().getId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar una habitación válida.");
        if (r.getFechaReservacion() == null)
            r.setFechaReservacion(LocalDate.now());

        if (r.getFechaLlegada() == null)
            throw new IllegalArgumentException("Debe indicar la fecha de llegada.");
        if (r.getFechaLlegada().isBefore(r.getFechaReservacion()))
            throw new IllegalArgumentException("La fecha de llegada no puede ser anterior a la fecha de reservación.");
        if (r.getCantidadNoches() <= 0)
            throw new IllegalArgumentException("La cantidad de noches debe ser mayor que 0.");

    }
}
