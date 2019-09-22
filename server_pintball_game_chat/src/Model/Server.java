/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daryl Ospina
 */
public class Server implements Runnable {

    private final int PORT;
    private ServerSocket server;
    private Socket client;
    public static int conectados;
    public static Message messages = new Message();
    public static boolean showMessage;

    public final Logger log;

    public Server() {
        this.log = Logger.getLogger(Server.class.getName());
        this.PORT = 5000;
        this.client = new Socket();
        Server.conectados = 0;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(this.PORT);
            log.info("Servidor a la espera de conexiones");
            while (true) {
                this.client = this.server.accept();
                log.log(Level.INFO, "Cliente con la [ IP: {0}] conectado.", this.client.getInetAddress().getHostAddress());
                Server.conectados++;

                ClientConnection clientConnection = new ClientConnection(this.client);
                clientConnection.start();
            }
        } catch (IOException e) {
            this.log.log(Level.SEVERE, null, e.getMessage());
        }
    }

}
