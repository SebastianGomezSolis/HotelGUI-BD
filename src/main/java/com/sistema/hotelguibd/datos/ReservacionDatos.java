package com.sistema.hotelguibd.datos;

import com.sistema.hotelguibd.modelo.Cliente;
import com.sistema.hotelguibd.modelo.Habitacion;
import com.sistema.hotelguibd.modelo.Reservacion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservacionDatos {
    public List<Reservacion> findAll() throws SQLException {
        String sql = "SELECT * FROM reservacion ORDER BY id";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Reservacion> list = new ArrayList<>();
            HabitacionDatos habDAO = new HabitacionDatos();
            ClienteDatos    cliDAO = new ClienteDatos();
            Map<Integer,Habitacion> habCache = new HashMap<>();
            Map<Integer,Cliente>    cliCache = new HashMap<>();

            while (rs.next()) {
                int idH = rs.getInt("idHabitacion");
                int idC = rs.getInt("idCliente");

                Habitacion hab = habCache.computeIfAbsent(idH, k -> {
                    try { return habDAO.findById(k); } catch (SQLException e) { throw new RuntimeException(e); }
                });
                Cliente cli = cliCache.computeIfAbsent(idC, k -> {
                    try { return cliDAO.findById(k); } catch (SQLException e) { throw new RuntimeException(e); }
                });

                Reservacion r = new Reservacion(
                        rs.getInt("id"),
                        hab,
                        cli,
                        rs.getDate("fechaReservacion").toLocalDate(),
                        rs.getDate("fechaLlegada").toLocalDate(),
                        rs.getInt("cantidadNoches")
                );
                Date fs = rs.getDate("fechaSalida");
                r.setFechaSalida(fs != null ? fs.toLocalDate().toString() : null);
                r.setDescuento(rs.getDouble("descuento"));
                r.setPrecioTotal(rs.getDouble("precioTotal"));
                list.add(r);
            }
            return list;
        }
    }

    public Reservacion findById(int id) throws SQLException {
        String sql = "SELECT * FROM reservacion WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                int idH = rs.getInt("idHabitacion");
                int idC = rs.getInt("idCliente");

                HabitacionDatos habDAO = new HabitacionDatos();
                ClienteDatos    cliDAO = new ClienteDatos();

                Habitacion hab = habDAO.findById(idH);
                Cliente cli = cliDAO.findById(idC);

                Reservacion r = new Reservacion(
                        rs.getInt("id"), hab, cli,
                        rs.getDate("fechaReservacion").toLocalDate(),
                        rs.getDate("fechaLlegada").toLocalDate(),
                        rs.getInt("cantidadNoches")
                );

                Date fs = rs.getDate("fechaSalida");
                r.setFechaSalida(fs != null ? fs.toLocalDate().toString() : null);
                r.setDescuento(rs.getDouble("descuento"));
                r.setPrecioTotal(rs.getDouble("precioTotal"));
                return r;
            }
        }
    }

    public Reservacion insert(Reservacion r) throws SQLException {
        String sql = "INSERT INTO reservacion (idHabitacion, idCliente, fechaReservacion, fechaLlegada, cantidadNoches, preciototal, descuento) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getHabitacion().getId());
            ps.setInt(2, r.getCliente().getId());
            ps.setDate(3, Date.valueOf(r.getFechaReservacion()));
            ps.setDate(4, Date.valueOf(r.getFechaLlegada()));
            ps.setInt(5, r.getCantidadNoches());
            ps.setDouble(6, r.getPrecioTotal()); // columna: preciototal
            ps.setDouble(7, r.getDescuento());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getInt(1));
            }
            return r;
        }
    }


    public Reservacion update(Reservacion r) throws SQLException {
        String sql = "UPDATE reservacion SET idHabitacion = ?, idCliente = ?, fechaReservacion = ?, fechaLlegada = ?, cantidadNoches = ?, preciototal = ?, descuento = ? WHERE id=?";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, r.getHabitacion().getId());
            ps.setInt(2, r.getCliente().getId());
            ps.setDate(3, Date.valueOf(r.getFechaReservacion()));
            ps.setDate(4, Date.valueOf(r.getFechaLlegada()));
            ps.setInt(5, r.getCantidadNoches());
            ps.setDouble(6, r.getPrecioTotal());
            ps.setDouble(7, r.getDescuento());
            ps.setInt(8, r.getId());

            if (ps.executeUpdate() > 0) {
                return r;
            } else {
                return null;
            }
        }
    }


    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM reservacion WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}


