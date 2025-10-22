package com.sistema.hotelguibd.controller;

import com.sistema.hotelguibd.logica.ClienteLogica;
import com.sistema.hotelguibd.logica.HabitacionLogica;
import com.sistema.hotelguibd.modelo.Cliente;
import com.sistema.hotelguibd.modelo.Habitacion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioHabitacionController {
    // Controllers del formulario
    @FXML private TextField txtNumeroHabitacion;
    @FXML private TextField txtEstadoHabitacion;
    @FXML private TextField txtPrecioHabitacion;
    @FXML private TextField txtCapacidadHabitacion;
    @FXML private Button btnGuardarHabitacion;
    @FXML private ProgressIndicator progress;

    private Habitacion habitacion;

    private final HabitacionLogica habitacionLogica = new HabitacionLogica();
    private boolean modoEdicion = false;

    private TableView<Habitacion> tablaDestino;
    public void setTablaDestino(TableView<Habitacion> t) { this.tablaDestino = t; }

    public void setHabitacion(Habitacion habitacion, boolean editar) {
        //Si la habitacion es nueva, es decir, si venimos de un "Agregar Habitacion"
        //Entonces solo seteamos la habitacion nueva que vamos a guardar
        //Y el modo edición se mantiene en falso
        this.habitacion = habitacion;
        this.modoEdicion = editar;

        //Si el cliente no es nuevo, es decir, venimos de un "Modificar Cliente"
        //Entonces modificamos los datos y los guardamos en el cliente ya existente
        //Para esto, en la pantalla del formulario debemos cargar los datos que estaban
        //previamente guardados
        if (editar && habitacion != null) {
            txtNumeroHabitacion.setText(String.valueOf(habitacion.getNumero()));
            txtEstadoHabitacion.setText(habitacion.getEstado());
            txtPrecioHabitacion.setText(String.valueOf(habitacion.getPrecio()));
            txtCapacidadHabitacion.setText(String.valueOf(habitacion.getCapacidad()));
        }
    }

    @FXML
    private void guardarHabitacion() {
        try {
            int numero = Integer.parseInt(txtNumeroHabitacion.getText().trim());
            String estado = txtEstadoHabitacion.getText().trim();
            double precio = Double.parseDouble(txtPrecioHabitacion.getText().trim());
            int capacidad = Integer.parseInt(txtCapacidadHabitacion.getText().trim());

            // Verificamos que el formulario este completo
            if (numero == 0 || estado.isEmpty() || precio == 0 || capacidad == 0) {
                // Se va a lanzar un error (alert)
                mostrarAlerta("Campos incompletos", "Por favor, complete todos los campos del formulario.");
                return;
            }

            //Vamos a verificar si es un cliente nuevo o si estamos editando un cliente ya existente
            if (!modoEdicion) {
                //Entonces es un cliente nuevo
                habitacion = new Habitacion(0, numero, estado, precio, capacidad);
            } else {
                //Entonces estamos modificando un cliente existente
                habitacion.setNumero(numero);
                habitacion.setEstado(estado);
                habitacion.setPrecio(precio);
                habitacion.setCapacidad(capacidad);
            }

            // Aquí vamos a controlar el movimiento de las ventanas
            // Se debe cerrar la ventana del formulario y se debe regresar a la principal
            // Stage stage = (Stage) txtNumeroHabitacion.getScene().getWindow();
            // stage.setUserData(habitacion);
            // stage.close();
            guardarHabitacionAsync(habitacion, tablaDestino);
        }
        catch (Exception error) {
            mostrarAlerta("Error al guardar los datos", error.getMessage());
        }
    }

    public void guardarHabitacionAsync(Habitacion h, TableView<Habitacion> tablaHabitaciones) {
        btnGuardarHabitacion.setDisable(true);
        progress.setVisible(true);

        Async.run(
                () -> {
                    try {
                        if (modoEdicion) {
                            habitacionLogica.update(h);   // <- implementa este método si no existe
                            return h;
                        } else {
                            int nuevoId = habitacionLogica.create(h).getId(); // o create(...) que devuelva Cliente
                            h.setId(nuevoId);
                            return h;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                guardado -> {
                    progress.setVisible(false);
                    btnGuardarHabitacion.setDisable(false);

                    if (tablaHabitaciones != null) {
                        if (modoEdicion) {
                            tablaHabitaciones.refresh();
                        } else {
                            tablaHabitaciones.getItems().add(guardado);
                        }
                    }

                    new Alert(Alert.AlertType.INFORMATION,
                            (modoEdicion ? "Habitacion actualizada (ID: " : "Habitacion guardado (ID: ")
                                    + guardado.getId() + ")").showAndWait();

                    ((Stage) btnGuardarHabitacion.getScene().getWindow()).close();
                },
                ex -> {
                    progress.setVisible(false);
                    btnGuardarHabitacion.setDisable(false);
                    new Alert(Alert.AlertType.ERROR, "No se pudo guardar: " + ex.getMessage()).showAndWait();
                }
        );
    }

    @FXML
    private void cancelarHabitacion()
    {
        try
        {
            Stage stage = (Stage) txtNumeroHabitacion.getScene().getWindow();
            stage.setUserData(null);
            stage.close();
        } catch (Exception error) {
            mostrarAlerta("Error al guardar los datos", error.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNumeroHabitacion.clear();
        txtEstadoHabitacion.clear();
        txtPrecioHabitacion.clear();
        txtCapacidadHabitacion.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
