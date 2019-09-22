/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Daryl Ospina
 */
public class ClientConnection extends Thread implements Observer {

    private final Logger log;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    boolean conected = true;

    public ClientConnection(Socket socket) {
        this.log = Logger.getLogger(ClientConnection.class.getName());
        this.socket = socket;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error al crear los stream de entrada y salida : {0}", ex.getMessage());
        }
    }

    @Override
    public void run() {

        // Se apunta a la lista de observadores de mensajes
        Server.messages.addObserver(this);

        while (conected) {
            try {
                String mensaje = this.input.readUTF();
                JSONObject obj = ((JSONObject) (new JSONParser().parse(mensaje)));

                if ((boolean) obj.get("desconectar")) {
                    Server.conectados--;
                }
                
                Server.showMessage = (boolean) obj.get("show");

                if (Server.showMessage) {
                    Server.messages.addMensaje((String) obj.get("mensaje"));
                } else {
                    Server.messages.modifyScores(((JSONArray) (obj.get("mensaje"))));
                }
            } catch (IOException | ParseException ex) {
                log.log(Level.INFO, "Cliente con la IP {0} desconectado.", socket.getInetAddress().getHostAddress());
                conected = false;
                this.closeSocket();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            // Envia el mensaje al cliente
            JSONObject obj = new JSONObject();

            obj.put("mensaje", arg);
            obj.put("show", Server.showMessage);
            obj.put("conectados", Server.conectados);

            output.writeUTF(obj.toString());
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error al enviar mensaje al cliente ({0}).", ex.getMessage());
            conected = false;
            this.closeSocket();
        }
    }

    private void closeSocket() {
        try {
            this.input.close();
            this.output.close();
        } catch (IOException ex2) {
            this.log.log(Level.SEVERE, "Error al cerrar los stream de entrada y salida :{0}", ex2.getMessage());
        }
    }
}
