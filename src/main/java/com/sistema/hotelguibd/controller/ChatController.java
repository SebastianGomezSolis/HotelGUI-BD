package com.sistema.hotelguibd.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.sistema.hotelguibd.servicios.ChatClient;

public class ChatController {
    @FXML private TextArea txtArea;
    @FXML private Button btnConectar;
    @FXML private TextField txtInput;
    @FXML private TextField txtNombre;

    private ChatClient client;

    @FXML
    public void onConnect() {
        try {
          client = new ChatClient();
          client.conectar("localhost", 6000, txtNombre.getText(),
                  msg -> Platform.runLater(() -> txtArea.appendText(msg + "\n")));
          btnConectar.setDisable(true);
        } catch (Exception e) {
            txtArea.appendText("Error al conectar al servidor: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void onSend() {
        String msg = txtInput.getText().trim();
        if (msg.isEmpty()) return;
        client.enviarMensaje(msg);
        txtInput.clear();

    }

}
