package com.sistema.hotelguibd.datos;

import com.sistema.hotelguibd.modelo.Habitacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDatos {
    public List<Habitacion> findAll() throws SQLException {
        // Construir el query que se ejecute en MySQL
        String sql = "Select * from habitacion ORDER BY id";
        // String sqlAlternativo = "Select * from cliente where nombre = 'Juan' ";

        // Conectamos a la base de datos
        try (Connection cn = DataBase.getConnection();                // 1. Conexion
             PreparedStatement ps = cn.prepareStatement(sql);         // 2. Preparar la sentencia sql
             ResultSet rs = ps.executeQuery()) {                      // 3. Ejecutar la sentencia

            List<Habitacion> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Habitacion(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getString("estado"),
                        rs.getDouble("precio"),
                        rs.getInt("capacidad")
                        )
                );
            }
            return list;
        }
    }

    public Habitacion findById(int id) throws SQLException {
        String sql = "Select * from habitacion WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Habitacion encontrado = null;
            if (rs.next()) {
                encontrado = new Habitacion(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getString("estado"),
                        rs.getDouble("precio"),
                        rs.getInt("capacidad")
                );
            }
            return encontrado;
        }
    }

    public Habitacion insert(Habitacion habitacion) throws SQLException {
        String sql = "INSERT INTO habitacion (numero, tipo, descripcionTipo, estado, descripcionEstado, precio, capacidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, habitacion.getNumero());
            ps.setString(2, habitacion.getTipo());
            ps.setString(3, habitacion.getDescripcionTipo());
            ps.setString(4, habitacion.getEstado());
            ps.setString(5, habitacion.getDescripcionEstado());
            ps.setDouble(6, habitacion.getPrecio());
            ps.setInt(7, habitacion.getCapacidad());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return habitacion;
                }
            }
            return null;
        }
    }

    public Habitacion update(Habitacion habitacion) throws SQLException {
        String sql = "UPDATE habitacion set numero = ?, estado = ?, precio = ?, capacidad = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, habitacion.getNumero());
            ps.setString(2, habitacion.getEstado());
            ps.setDouble(3, habitacion.getPrecio());
            ps.setInt(4, habitacion.getCapacidad());
            ps.setInt(5, habitacion.getId());
            if (ps.executeUpdate() > 0) {
                return habitacion;
            } else {
                return null;
            }
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM habitacion WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
