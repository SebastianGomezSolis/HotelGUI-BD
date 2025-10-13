package com.sistema.hotelguibd.logica;

import com.sistema.hotelguibd.modelo.Cliente;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class EstadisticasClienteLogica {

    private final ClienteLogica clienteLogica;

    // Usa la misma ruta de tu app; pásala tal cual usas en tus controllers */
    public EstadisticasClienteLogica() {
        this.clienteLogica = new ClienteLogica();
    }

    // Alternativa si ya tienes una instancia de ClienteLogica creada */
    public EstadisticasClienteLogica(ClienteLogica logica) {
        this.clienteLogica = logica;
    }

    public List<Cliente> cargarClientes() throws SQLException {
        return clienteLogica.findAll();
    }

    /** Total de clientes */
    public int totalClientes() throws SQLException {
        return cargarClientes().size();
    }

    /** Mapa rangoEdad -> cantidad */
    public LinkedHashMap<String, Long> clientesPorRangoEdad() throws SQLException {
        // Mantener orden lógico de los rangos
        LinkedHashMap<String, Long> resultado = new LinkedHashMap<>();
        List<String> rangos = Arrays.asList("0-12", "13-19", "20-35", "36-50", "51-65", "66+", "N/D");

        Map<String, Long> conteos = cargarClientes().stream()
                .collect(Collectors.groupingBy(
                        c -> rangoEdad(c.getFechaNacimiento()),
                        Collectors.counting()
                ));

        for (String r : rangos) {
            resultado.put(r, conteos.getOrDefault(r, 0L));
        }
        return resultado;
    }

    /** Promedio de edad (excluye N/D) */
    public OptionalDouble edadPromedio() throws SQLException {
        LocalDate hoy = LocalDate.now();
        return cargarClientes().stream()
                .map(Cliente::getFechaNacimiento)
                .filter(Objects::nonNull)
                .mapToInt(fn -> Period.between(fn, hoy).getYears())
                .average();
    }

    private static String rangoEdad(LocalDate fn) {
        if (fn == null) return "N/D";
        int edad = Period.between(fn, LocalDate.now()).getYears();
        if (edad < 0) return "N/D";
        if (edad <= 12) return "0-12";
        if (edad <= 19) return "13-19";
        if (edad <= 35) return "20-35";
        if (edad <= 50) return "36-50";
        if (edad <= 65) return "51-65";
        return "66+";
    }
}
