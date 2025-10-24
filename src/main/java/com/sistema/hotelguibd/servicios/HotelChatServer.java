package com.sistema.hotelguibd.servicios;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.*;

public class HotelChatServer {
    private final int port;
    private final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private static final Logger LOGGER = Logger.getLogger(HotelChatServer.class.getName());

    public HotelChatServer(int port) {
        this.port = port;
        configureLogger();
    }

    public void configureLogger() {
        try {
            LOGGER.setUseParentHandlers(false);
            var fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            System.err.println("No se a podido generar la bitacora del servidor");
        }
    }

    public void start() {
        LOGGER.info("Iniciando el servidor en el puerto: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);
                clientHandler.start();
                LOGGER.info("Conexion aceptado desde: " + socket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            LOGGER.info("Error en el servidor: " + e);
        }
    }

    // METODO DE COMUNICACION ENTRE EL SERVIDOR Y LOS CLIENTES

    public void remove(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        LOGGER.info("Cliente eliminado de la conexion: " + clientHandler.getName());
    }

    public static void main(String[] args) {
        new HotelChatServer(6000).start();
    }

    public void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.send(message);
            }
        }
    }
}
