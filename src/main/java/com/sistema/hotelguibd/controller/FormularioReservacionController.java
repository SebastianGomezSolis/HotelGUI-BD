package com.sistema.hotelguibd.controller;

import com.sistema.hotelguibd.logica.ClienteLogica;
import com.sistema.hotelguibd.logica.HabitacionLogica;
import com.sistema.hotelguibd.logica.ReservacionLogica;
import com.sistema.hotelguibd.modelo.Cliente;
import com.sistema.hotelguibd.modelo.Habitacion;
import com.sistema.hotelguibd.modelo.Reservacion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FormularioReservacionController {

    // FXML
    @FXML private ComboBox<Habitacion> cmbHabitacion;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private DatePicker dtpFechaReservacion;
    @FXML private DatePicker dtpFechaLlegada;
    @FXML private TextField txtCantidadNochesReservacion;
    @FXML private Button btnGuardarReservacion;
    @FXML private ProgressIndicator progress;

    // Estado
    private Reservacion reservacion;
    private boolean modoEdicion = false;

    // Lógica
    private final ClienteLogica clienteLogica = new ClienteLogica();
    private final HabitacionLogica habitacionLogica = new HabitacionLogica();
    private final ReservacionLogica reservacionLogica = new ReservacionLogica();

    private TableView<Reservacion> tablaDestino;
    public void setTablaDestino(TableView<Reservacion> t) { this.tablaDestino = t; }

    public void setReservacion(Reservacion reservacion, boolean editar) {
        this.reservacion = reservacion;
        this.modoEdicion = editar;

        try {
            // cargar combos
            List<Cliente> clientes = clienteLogica.findAll();
            List<Habitacion> habitaciones = habitacionLogica.findAll();
            cmbCliente.setItems(FXCollections.observableArrayList(clientes));
            cmbHabitacion.setItems(FXCollections.observableArrayList(habitaciones));

            // converters para mostrar textos legibles
            cmbCliente.setConverter(new StringConverter<>() {
                @Override public String toString(Cliente c) {
                    return c == null ? "" : c.getNombre() + " " + c.getPrimerApellido();
                }
                @Override public Cliente fromString(String s) { return null; }
            });
            cmbHabitacion.setConverter(new StringConverter<>() {
                @Override public String toString(Habitacion h) {
                    return h == null ? "" : "H: " + h.getNumero() + " / Precio ₡" + h.getPrecio();
                }
                @Override public Habitacion fromString(String s) { return null; }
            });

            if (editar && reservacion != null) {
                // rellenar campos
                cmbCliente.getSelectionModel().select(
                        clientes.stream().filter(c -> c.getId() == reservacion.getCliente().getId()).findFirst().orElse(null)
                );
                cmbHabitacion.getSelectionModel().select(
                        habitaciones.stream().filter(h -> h.getId() == reservacion.getHabitacion().getId()).findFirst().orElse(null)
                );

                dtpFechaReservacion.setValue(reservacion.getFechaReservacion());
                dtpFechaLlegada.setValue(reservacion.getFechaLlegada());
                txtCantidadNochesReservacion.setText(String.valueOf(reservacion.getCantidadNoches()));
            } else {
                // defaults
                dtpFechaReservacion.setValue(LocalDate.now());
                dtpFechaLlegada.setValue(LocalDate.now());
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos: " + e.getMessage());
        }
    }

    @FXML
    private void guardarReservacion() {
        try {
            Habitacion hab = cmbHabitacion.getValue();
            Cliente cli = cmbCliente.getValue();
            LocalDate fRes = dtpFechaReservacion.getValue();
            LocalDate fLle = dtpFechaLlegada.getValue();
            int noches = Integer.parseInt(txtCantidadNochesReservacion.getText().trim());

            if (hab == null || cli == null || fRes == null || fLle == null || noches <= 0) {
                mostrarAlerta("Campos incompletos", "Complete todos los campos y asegúrese de que las noches sean > 0.");
                return;
            }

            if (!modoEdicion) {
                reservacion = new Reservacion(0, hab, cli, fRes, fLle, noches);
            } else {
                reservacion.setHabitacion(hab);
                reservacion.setCliente(cli);
                reservacion.setFechaReservacion(fRes);
                reservacion.setFechaLlegada(fLle);
                reservacion.setCantidadNoches(noches);
            }

            // Stage stage = (Stage) dtpFechaReservacion.getScene().getWindow();
            // stage.setUserData(reservacion);
            // stage.close();
            guardarReservacionAsync(reservacion, tablaDestino);
        } catch (NumberFormatException nfe) {
            mostrarAlerta("Dato inválido", "La cantidad de noches debe ser un número entero.");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar la reservación: " + e.getMessage());
        }
    }

    public void guardarReservacionAsync(Reservacion r, TableView<Reservacion> tablaReservaciones) {
        btnGuardarReservacion.setDisable(true);
        progress.setVisible(true);

        Async.run(
                () -> {
                    try {
                        if (modoEdicion) {
                            reservacionLogica.update(r);   // <- implementa este método si no existe
                            return r;
                        } else {
                            int nuevoId = reservacionLogica.create(r).getId(); // o create(...) que devuelva Cliente
                            r.setId(nuevoId);
                            return r;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                guardado -> {
                    progress.setVisible(false);
                    btnGuardarReservacion.setDisable(false);

                    if (tablaReservaciones != null) {
                        if (modoEdicion) {
                            tablaReservaciones.refresh();
                        } else {
                            tablaReservaciones.getItems().add(guardado);
                        }
                    }

                    new Alert(Alert.AlertType.INFORMATION,
                            (modoEdicion ? "Reservacion actualizada (ID: " : "Reservacion guardado (ID: ")
                                    + guardado.getId() + ")").showAndWait();

                    ((Stage) btnGuardarReservacion.getScene().getWindow()).close();
                },
                ex -> {
                    progress.setVisible(false);
                    btnGuardarReservacion.setDisable(false);
                    new Alert(Alert.AlertType.ERROR, "No se pudo guardar: " + ex.getMessage()).showAndWait();
                }
        );
    }

    @FXML
    private void cancelarReservacion() {
        Stage stage = (Stage) dtpFechaReservacion.getScene().getWindow();
        stage.setUserData(null);
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
