package com.sistema.hotelguibd.controller;

import com.sistema.hotelguibd.logica.EstadisticasClienteLogica;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstadisticasClientesController implements Initializable {
    @FXML private Label lblTotalClientes;
    @FXML private Label lblEdadPromedio;

    @FXML private BarChart<String, Number> chartRangosEdad;
    @FXML private CategoryAxis rangosXAxis;
    @FXML private NumberAxis rangosYAxis;

    @FXML private PieChart chartRangosEdadPie; // opcional: distribución tipo pie

    private EstadisticasClienteLogica service;

    // Permite setear la ruta desde quien carga la vista, si quieres inyectarla
    //public void setRutaXml(String ruta) { this.rutaXml = ruta; }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.service = new EstadisticasClienteLogica();
            cargarGraficos();
        } catch (SQLException e) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void cargarGraficos() throws SQLException {
        // Totales
        int total = service.totalClientes();
        lblTotalClientes.setText(String.valueOf(total));

        service.edadPromedio().ifPresentOrElse(
                avg -> lblEdadPromedio.setText(String.format("%.1f años", avg)),
                () -> lblEdadPromedio.setText("N/D")
        );

        // Rangos → Barras
        LinkedHashMap<String, Long> rangos = service.clientesPorRangoEdad();

        chartRangosEdad.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Clientes por rango de edad");

        for (Map.Entry<String, Long> e : rangos.entrySet()) {
            serie.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
        }
        chartRangosEdad.getData().add(serie);

        // Rangos → Pie
        chartRangosEdadPie.getData().clear();
        for (Map.Entry<String, Long> e : rangos.entrySet()) {
            if (e.getValue() > 0) { // evita sectores cero
                chartRangosEdadPie.getData().add(new PieChart.Data(e.getKey(), e.getValue()));
            }
        }
    }
}
