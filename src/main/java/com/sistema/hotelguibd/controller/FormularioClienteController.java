package com.sistema.hotelguibd.controller;

import com.sistema.hotelguibd.logica.ClienteLogica;
import com.sistema.hotelguibd.modelo.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioClienteController {
    // Controllers del formulario
    @FXML private TextField txtIdentificacionCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtPrimerApellidoCliente;
    @FXML private DatePicker dtpFechaNacimientoCliente;
    @FXML private Button btnGuardarCliente;
    @FXML private ProgressIndicator progress;

    private Cliente cliente;

    private final ClienteLogica clienteLogica = new ClienteLogica();
    private boolean modoEdicion = false;

    private TableView<Cliente> tablaDestino;
    public void setTablaDestino(TableView<Cliente> t) {
        this.tablaDestino = t;
    }

    public void setCliente(Cliente cliente, boolean editar) {
        //Si el cliente es nuevo, es decir, si venimos de un "Agregar Cliente"
        //Entonces solo seteamos el cliente nuevo que vamos a guardar
        //Y el modo edición se mantiene en falso
        this.cliente = cliente;
        this.modoEdicion = editar;

        //Si el cliente no es nuevo, es decir, venimos de un "Modificar Cliente"
        //Entonces modificamos los datos y los guardamos en el cliente ya existente
        //Para esto, en la pantalla del formulario debemos cargar los datos que estaban
        //previamente guardados
        if (editar && cliente != null) {
            txtIdentificacionCliente.setText(cliente.getIdentificacion());
            txtNombreCliente.setText(cliente.getNombre());
            txtPrimerApellidoCliente.setText(cliente.getPrimerApellido());
            dtpFechaNacimientoCliente.setValue(cliente.getFechaNacimiento());
        }
    }

    @FXML
    private void guardarCliente() {
        try {
            String identificacion = txtIdentificacionCliente.getText().trim();
            String nombre = txtNombreCliente.getText().trim();
            String primerApellido = txtPrimerApellidoCliente.getText().trim();
            LocalDate fechaNacimiento = dtpFechaNacimientoCliente.getValue();

            // Verificamos que el formulario este completo
            if (identificacion.isEmpty() || nombre.isEmpty() || primerApellido.isEmpty() || fechaNacimiento == null) {
                // Se va a lanzar un error (alert)
                mostrarAlerta("Campos incompletos", "Por favor, complete todos los campos del formulario.");
                return;
            }

            //Vamos a verificar si es un cliente nuevo o si estamos editando un cliente ya existente
            if (!modoEdicion) {
                //Entonces es un cliente nuevo
                cliente = new Cliente(0, nombre, primerApellido, identificacion, fechaNacimiento);
            } else {
                //Entonces estamos modificando un cliente existente
                cliente.setIdentificacion(identificacion);
                cliente.setNombre(nombre);
                cliente.setPrimerApellido(primerApellido);
                cliente.setFechaNacimiento(fechaNacimiento);
            }

            // Aquí vamos a controlar el movimiento de las ventanas
            // Se debe cerrar la ventana del formulario y se debe regresar a la principal
            // Stage stage = (Stage) txtNombreCliente.getScene().getWindow();
            // stage.setUserData(cliente);
            // stage.close();
            guardarClienteAsync(cliente, tablaDestino);
        }
        catch (Exception error) {
            mostrarAlerta("Error al guardar los datos", error.getMessage());
        }
    }

    public void guardarClienteAsync(Cliente c, TableView<Cliente> tablaClientes) {
        btnGuardarCliente.setDisable(true);
        progress.setVisible(true);

        Async.run(
                () -> {
                    try {
                        if (modoEdicion) {
                            clienteLogica.update(c);   // <- implementa este método si no existe
                            return c;
                        } else {
                            int nuevoId = clienteLogica.create(c).getId(); // o create(...) que devuelva Cliente
                            c.setId(nuevoId);
                            return c;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                guardado -> {
                    progress.setVisible(false);
                    btnGuardarCliente.setDisable(false);

                    if (tablaClientes != null) {
                        if (modoEdicion) {
                            tablaClientes.refresh();
                        } else {
                            tablaClientes.getItems().add(guardado);
                        }
                    }

                    new Alert(Alert.AlertType.INFORMATION,
                            (modoEdicion ? "Cliente actualizado (ID: " : "Cliente guardado (ID: ")
                                    + guardado.getId() + ")").showAndWait();

                    ((Stage) btnGuardarCliente.getScene().getWindow()).close();
                },
                ex -> {
                    progress.setVisible(false);
                    btnGuardarCliente.setDisable(false);
                    new Alert(Alert.AlertType.ERROR, "No se pudo guardar: " + ex.getMessage()).showAndWait();
                }
        );
    }

    @FXML
    private void cancelarCliente()
    {
        try
        {
            Stage stage = (Stage) txtNombreCliente.getScene().getWindow();
            stage.setUserData(null);
            stage.close();
        } catch (Exception error) {
            mostrarAlerta("Error al guardar los datos", error.getMessage());
        }
    }

    private void limpiarCampos() {
        txtIdentificacionCliente.clear();
        txtNombreCliente.clear();
        txtPrimerApellidoCliente.clear();
        dtpFechaNacimientoCliente.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
