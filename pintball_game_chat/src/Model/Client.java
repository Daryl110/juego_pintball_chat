/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.CtlTableScore;
import View.FrmMain;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
public class Client extends Thread {

    private Socket client;
    public String ip;
    public int puerto;
    private DataOutputStream output;
    private DataInputStream input;
    private Logger log = Logger.getLogger(Client.class.getName());
    boolean conectado, showMessage;

    public Client(String ip, int puerto) {
        try {
            this.ip = ip;
            this.puerto = puerto;
            this.client = new Socket(this.ip, this.puerto);
            this.conectado = true;
            this.output = new DataOutputStream(this.client.getOutputStream());
            this.input = new DataInputStream(this.client.getInputStream());

            JSONObject obj = new JSONObject();

            obj.put("show", true);
            obj.put("mensaje", FrmMain.nickName + " Conectado.");
            obj.put("desconectar", false);

            this.output.writeUTF(obj.toString());
            
            this.sendScores();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "{0}", ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (conectado) {
            try {
                String mensaje = this.input.readUTF();
                JSONObject obj = ((JSONObject) (new JSONParser().parse(mensaje)));
                
                long connected = ((long)(obj.get("conectados")));
                
                if ((boolean) obj.get("show")) {
                    FrmMain.txtChat.setText((String) obj.get("mensaje") + System.lineSeparator());
                } else {
                    Gson gson = new Gson();
                    JSONArray array = ((JSONArray) (new JSONParser().parse(obj.get("mensaje").toString())));
                    for (Object score : array) {
                        Score objScore = gson.fromJson(((JSONObject) score).toString(), Score.class);
                        if (!FrmMain.ctl.findReply(objScore.getTime(), objScore.getName())) {
                            CtlTableScore.listScore.add(objScore);
                        }
                    };
                    FrmMain.ctl.listScore();
                }
                
                FrmMain.lblUsers.setText(connected+"");
            } catch (IOException ex) {
                log.log(Level.SEVERE, "Error al leer del stream de entrada: {0}", ex.getMessage());
            } catch (NullPointerException ex) {
                log.severe("El socket no se creo correctamente. ");
                conectado = false;
            } catch (ParseException ex) {
                log.log(Level.SEVERE, null, ex.getMessage());
                conectado = false;
            }
        }
    }

    public void sendMesagge(String message, boolean desconectar) {
        try {
            JSONObject obj = new JSONObject();

            obj.put("mensaje", message);
            obj.put("show", true);
            obj.put("desconectar", desconectar);

            this.output.writeUTF(obj.toString());
        } catch (IOException e) {
            log.log(Level.SEVERE, "{0}", e.getMessage());
        }
    }

    public final void sendScores() {
        try {
            Gson gson = new Gson();
            JSONArray array = new JSONArray();

            CtlTableScore.listScore.forEach((score) -> {
                try {
                    array.add((JSONObject) (new JSONParser().parse(gson.toJson(score))));
                } catch (ParseException ex) {
                    this.log.log(Level.SEVERE, null, ex);
                }
            });

            JSONObject joRequest = new JSONObject();
            
            joRequest.put("mensaje", array);
            joRequest.put("show", false);
            joRequest.put("desconectar", false);

            this.output.writeUTF(joRequest.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconect() {
        try {
            String message = FrmMain.nickName + " desconectado";
            this.sendMesagge(message, true);
            Thread.sleep(5000);
            conectado = false;
            if (this.output != null) {
                this.output.close();
            }
            if (this.client != null) {
                this.client.close();
            }
            log.info(message);
        } catch (IOException | InterruptedException e) {
            log.log(Level.SEVERE, "{0}", e.getMessage());
        }
    }
}
