package com.sistema.hotelguibd.logica;

import com.sistema.hotelguibd.datos.HabitacionDatos;
import com.sistema.hotelguibd.modelo.Habitacion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class HabitacionLogica {
    private HabitacionDatos store = new HabitacionDatos();

    // --------- Lectura ---------
    public List<Habitacion> findAll() throws SQLException {
        return store.findAll();
    }

    public Habitacion findById(int id) throws SQLException {
        return store.findById(id);
    }

    // --------- Escritura ---------
    public Habitacion create(Habitacion nuevo) throws SQLException {
        validarNuevo(nuevo);
        return store.insert(nuevo);
    }

    public Habitacion update(Habitacion cliente) throws SQLException {
        if (cliente == null || cliente.getId() <= 0)
            throw new IllegalArgumentException("El cliente a actualizar requiere un ID válido.");
        validarCampos(cliente);
        return store.update(cliente);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0 ? true : false;
    }

    // --------- Helpers ---------
    private void validarNuevo(Habitacion c) {
        if (c == null) throw new IllegalArgumentException("Cliente nulo.");
        validarCampos(c);
    }

    private void validarCampos(Habitacion h) {
        if (h == null)
            throw new IllegalArgumentException("Habitación nula.");
        if (h.getNumero() <= 0)
            throw new IllegalArgumentException("El número de habitación es obligatorio y debe ser mayor que 0.");
        if (h.getEstado() == null || h.getEstado().isBlank())
            throw new IllegalArgumentException("El estado de la habitación es obligatorio.");
        if (h.getPrecio() <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        if (h.getCapacidad() <= 0)
            throw new IllegalArgumentException("La capacidad debe ser mayor que 0.");
    }
}
