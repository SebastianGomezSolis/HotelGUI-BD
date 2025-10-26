package com.sistema.hotelguibd.controller;

import com.sistema.hotelguibd.logica.ClienteLogica;
import com.sistema.hotelguibd.logica.HabitacionLogica;
import com.sistema.hotelguibd.logica.ReservacionLogica;
import com.sistema.hotelguibd.modelo.Cliente;
import com.sistema.hotelguibd.modelo.Habitacion;
import com.sistema.hotelguibd.modelo.Reservacion;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InicioController implements Initializable {
    // Controller de la tabla cliente
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, String> colNombreCliente;
    @FXML private TableColumn<Cliente, String> colIdentificacionCliente;
    @FXML private TableColumn<Cliente, String> colIdCliente;
    @FXML private ProgressIndicator progressCliente;

    // Controller de la tabla habitacion
    @FXML private TableView<Habitacion> tblHabitaciones;
    @FXML private TableColumn<Habitacion, String> colIdHabitacion;
    @FXML private TableColumn<Habitacion, String> colNumeroHabitacion;
    @FXML private TableColumn<Habitacion, String> colPrecioHabitacion;
    @FXML private ProgressIndicator progressHabitaciones;

    // Reservaciones
    @FXML private TableView<Reservacion> tblReservaciones;
    @FXML private TableColumn<Reservacion, String> colIdReservacion;
    @FXML private TableColumn<Reservacion, String> colClienteReservacion;
    @FXML private TableColumn<Reservacion, String> colHabitacionReservacion;
    @FXML private TableColumn<Reservacion, String> colNochesReservacion;
    @FXML private ProgressIndicator progressReservaciones;

    // Controllers de la busqueda cliente
    @FXML private TextField txtBuscarCliente;
    @FXML private TextField txtBuscarHabitacion;
    @FXML private TextField txtBuscarReservacion;

    // Alamacenamiento de datos
    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private final ObservableList<Habitacion> listaHabitaciones = FXCollections.observableArrayList();
    private final ObservableList<Reservacion> listaReservaciones = FXCollections.observableArrayList();

    private final ClienteLogica clienteLogica = new ClienteLogica();
    private final HabitacionLogica habitacionLogica = new HabitacionLogica();
    private final ReservacionLogica reservacionLogica = new ReservacionLogica();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Clientes
            colIdCliente.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getId()))
            );
            colIdentificacionCliente.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(cd.getValue().getIdentificacion())
            );
            colNombreCliente.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(
                            cd.getValue().getNombre() + " " + cd.getValue().getPrimerApellido()
                    )
            );
            // listaClientes.addAll(clienteLogica.findAll());
            tblClientes.setItems(listaClientes);
            cargarClientesAsync();

            // Habitaciones
            colIdHabitacion.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getId()))
            );
            colNumeroHabitacion.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getNumero()))
            );
            colPrecioHabitacion.setCellValueFactory(cd ->
                    new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getPrecio()))
            );
            // listaHabitaciones.addAll(habitacionLogica.findAll());
            tblHabitaciones.setItems(listaHabitaciones);
            cargarHabitacionesAsync();

            // Reservaciones
            colIdReservacion.setCellValueFactory(cd -> new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getId())));
            colClienteReservacion.setCellValueFactory(cd -> {
                Cliente c = cd.getValue().getCliente();
                String txt = (c == null) ? "" : c.getNombre() + " " + c.getPrimerApellido();
                return new ReadOnlyStringWrapper(txt);
            });
            colHabitacionReservacion.setCellValueFactory(cd -> {
                Habitacion h = cd.getValue().getHabitacion();
                String txt = (h == null) ? "" : String.valueOf(h.getNumero());
                return new ReadOnlyStringWrapper(txt);
            });
            colNochesReservacion.setCellValueFactory(cd -> new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getCantidadNoches())));
            // listaReservaciones.addAll(reservacionLogica.findAll());
            tblReservaciones.setItems(listaReservaciones);
            cargarReservacionesAsync();

        } catch (Exception e) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    @FXML
    private void agregarCliente() throws SQLException {
        mostrarFormulario(null, false);
//        if(nuevo != null) {
//            nuevo.setId(listaClientes.getLast().getId() + 1);
//
//            // Verificamos que el cliente no se repita
//            for (Cliente cliente : listaClientes) {
//                if (cliente.getIdentificacion().equals(nuevo.getIdentificacion())) {
//                    // Error de identificación repetida
//                    mostrarAlerta("Cliente ya existe", "El cliente con la identificación " + nuevo.getIdentificacion() + " ya existe.");
//                    return;
//                }
//            }
//
//            listaClientes.add(nuevo);
//        }
    }

    @FXML
    private void agregarHabitacion() throws SQLException {
        mostrarFormularioHabitacion(null, false);
//        if (nueva != null) {
//            nueva.setId(listaHabitaciones.isEmpty() ? 1 : listaHabitaciones.getLast().getId() + 1);
//
//            // Verificamos que la habitacion no se repita
//            for (Habitacion habitacion : listaHabitaciones) {
//                if (habitacion.getNumero() == nueva.getNumero()) {
//                    // Error de número de habitación repetido
//                    mostrarAlerta("Habitación ya existe", "La habitación con el número " + nueva.getNumero() + " ya existe.");
//                    return;
//                }
//            }
//
//            listaHabitaciones.add(nueva);
//        }
    }

    @FXML
    private void agregarReservacion() throws SQLException {
        mostrarFormularioReservacion(null, false);
//        if (nueva != null) {
//            nueva.setId(listaReservaciones.isEmpty() ? 1 : listaReservaciones.getLast().getId() + 1);
//            listaReservaciones.add(nueva);
//        }
    }

    private Cliente mostrarFormulario(Cliente cliente, boolean editar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/hotelguibd/formulario-cliente-view.fxml"));
            Parent root = loader.load();

            //LLamar a la clase de FormularioClienteController
            FormularioClienteController controller = loader.getController();
            //Vamos a setear la información del cliente que vamos a agregar
            //Pero para eso necesitamos el metodo respectivo en FormularioClienteController
            controller.setCliente(cliente, editar);
            controller.setTablaDestino(tblClientes);

            Stage stage = new Stage();
            stage.setTitle(editar ? "Modificar Cliente" : "Agregar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            return (Cliente) stage.getUserData();
        }
        catch (IOException error) {
            mostrarAlerta("Error al abrir el formulario", error.getMessage());
            return null;
        }
    }

    private Habitacion mostrarFormularioHabitacion(Habitacion habitacion, boolean editar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/hotelguibd/formulario-habitacion-view.fxml"));
            Parent root = loader.load();

            // Llamar a la clase FormularioHabitacionController
            FormularioHabitacionController controller = loader.getController();
            //Vamos a setear la información de la habitacion que vamos a agregar
            //Pero para eso necesitamos el metodo respectivo en FormularioHabitacionController
            controller.setHabitacion(habitacion, editar);
            controller.setTablaDestino(tblHabitaciones);

            Stage stage = new Stage();
            stage.setTitle(editar ? "Modificar Habitacion" : "Agregar Habitacion");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            return (Habitacion) stage.getUserData();
        } catch (Exception error) {
            mostrarAlerta("Error al abrir el formulario", error.getMessage());
            return null;
        }
    }

    private Reservacion mostrarFormularioReservacion(Reservacion reservacion, boolean editar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sistema/hotelguibd/formulario-reservacion-view.fxml"));
            Parent root = loader.load();
            FormularioReservacionController controller = loader.getController();
            controller.setReservacion(reservacion, editar);
            controller.setTablaDestino(tblReservaciones);

            Stage stage = new Stage();
            stage.setTitle(editar ? "Modificar Reservación" : "Agregar Reservación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            return (Reservacion) stage.getUserData();
        } catch (IOException e) {
            mostrarAlerta("Error al abrir el formulario", e.getMessage());
            return null;
        }
    }

    @FXML
    private void abrirEstadisticas() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/sistema/hotelguibd/estadisticas-cliente.fxml")
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Estadísticas de Clientes");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tblClientes.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error al abrir estadísticas", e.getMessage());
        }
    }


    @FXML
    private void modificarCliente() {
        Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un cliente", "Por favor, seleccione un cliente de la tabla para modificar.");
            return;
        }

        Cliente modificado = mostrarFormulario(seleccionado, true);
        if (modificado != null) {
            tblClientes.refresh();
        }
    }

    @FXML
    private void modificarHabitacion() {
        Habitacion seleccionada = tblHabitaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Seleccione una habitacion", "Por favor, seleccione una habitacion de la tabla para modificar.");
            return;
        }

        Habitacion modificada = mostrarFormularioHabitacion(seleccionada, true);
        if (modificada != null) {
            tblHabitaciones.refresh();
        }
    }

    @FXML
    private void modificarReservacion() {
        Reservacion sel = tblReservaciones.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Seleccione una reservación", "Seleccione una reservación para modificar.");
            return;
        }

        Reservacion modificada = mostrarFormularioReservacion(sel, true);
        if (modificada != null) {
            tblReservaciones.refresh();
        }
    }

    @FXML
    private void eliminarCliente() {
        try {
            Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Seleccione un cliente", "Por favor, seleccione un cliente de la tabla para eliminar.");
                return;
            }

            listaClientes.remove(seleccionado);
            clienteLogica.deleteById(seleccionado.getId());
        }
        catch (Exception error)
        {
            mostrarAlerta("Error al eliminar el cliente", error.getMessage());
        }
    }

    @FXML
    private void eliminarHabitacion() {
        try {
            Habitacion seleccionada = tblHabitaciones.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarAlerta("Seleccione una habitacion", "Por favor, seleccione una habitacion de la tabla para eliminar.");
                return;
            }

            listaHabitaciones.remove(seleccionada);
            habitacionLogica.deleteById(seleccionada.getId());
        } catch (Exception error) {
            mostrarAlerta("Error al eliminar la habitacion", error.getMessage());
        }
    }

    @FXML
    private void eliminarReservacion() {
        try {
            Reservacion seleleccionada = tblReservaciones.getSelectionModel().getSelectedItem();
            if (seleleccionada == null) {
                mostrarAlerta("Seleccione una reservación", "Seleccione una reservación para eliminar.");
                return;
            }

            listaReservaciones.remove(seleleccionada);
            reservacionLogica.deleteById(seleleccionada.getId());
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void buscarCliente() {
        try {
            String criterio = txtBuscarCliente.getText().trim().toLowerCase();
            if(criterio.isEmpty())
            {
                tblClientes.setItems(listaClientes);
                return;
            }

            ObservableList<Cliente> filtrados =
                    FXCollections.observableArrayList(
                            listaClientes.stream()
                                    .filter(c -> c.getIdentificacion().toLowerCase().contains(criterio)
                                            || c.getNombre().toLowerCase().contains(criterio)
                                            || c.getPrimerApellido().toLowerCase().contains(criterio))
                                    .collect(Collectors.toList())
                    );

            tblClientes.setItems(filtrados);
        }
        catch (Exception error)
        {
            mostrarAlerta("Error al buscar el cliente", error.getMessage());
        }
    }

    @FXML
    private void buscarHabitacion() {
        try {
            String criterio = txtBuscarHabitacion.getText().trim().toLowerCase();
            if (criterio.isEmpty()) {
                tblHabitaciones.setItems(listaHabitaciones);
                return;
            }

            ObservableList<Habitacion> filtrados =
                    FXCollections.observableArrayList(
                            listaHabitaciones.stream()
                                    .filter(h -> h.getNumero() == Integer.parseInt(criterio))
                                    .collect(Collectors.toList())
                    );

            tblHabitaciones.setItems(filtrados);
        } catch (Exception error) {
            mostrarAlerta("Error al buscar la habitacion", error.getMessage());
        }
    }

    @FXML
    private void buscarReservacion() {
        try {
            String criterio = (txtBuscarReservacion.getText() == null ? "" : txtBuscarReservacion.getText()).trim().toLowerCase();
            if (criterio.isEmpty()) {
                tblReservaciones.setItems(listaReservaciones);
                return;
            }

            ObservableList<Reservacion> filtrados =
                    FXCollections.observableArrayList(
                            listaReservaciones.stream()
                                    .filter(r -> r.getId() == Integer.parseInt(criterio))
                                    .collect(Collectors.toList())
                    );

            tblReservaciones.setItems(filtrados);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo buscar: " + e.getMessage());
        }
    }

    public void cargarClientesAsync() {
        progressCliente.setVisible(true);
        Async.run(
                () -> { // supplier (bg)
                    try {
                        return clienteLogica.findAll();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> { // onSuccess (UI)
                    listaClientes.setAll(lista);
                    progressCliente.setVisible(false);
                },
                ex -> { // onError (UI)
                    progressCliente.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar clientes");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    public void cargarHabitacionesAsync() {
        progressHabitaciones.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return habitacionLogica.findAll();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> {
                    listaHabitaciones.setAll(lista);
                    progressHabitaciones.setVisible(false);
                },
                ex -> {
                    progressHabitaciones.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar habitaciones");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    public void cargarReservacionesAsync() {
        progressReservaciones.setVisible(true);
        Async.run(
                () -> {
                    try {
                        return reservacionLogica.findAll();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                lista -> {
                    listaReservaciones.setAll(lista);
                    progressReservaciones.setVisible(false);
                },
                ex -> {
                    progressReservaciones.setVisible(false);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error al cargar reservaciones");
                    a.setHeaderText(null);
                    a.setContentText(ex.getMessage());
                    a.showAndWait();
                }
        );
    }

    @FXML
    public void abrirChat() {
        try {
            var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/sistema/hotelguibd/chat.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(root));
            stage.initOwner(progressCliente.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Aqui podriamos poner un alert con un mensaje bonito :)

        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
