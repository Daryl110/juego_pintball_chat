/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Observable;
import org.json.simple.JSONArray;

/**
 *
 * @author Daryl Ospina
 */
public class Message extends Observable{
    
    private String message;
    private final JSONArray array;
    
    public Message(){
        this.message = "";
        this.array = new JSONArray();
    }
    
    public String getMenssage(){
        return message;
    }

    public JSONArray getScores() {
        return this.array;
    }
    
    public void addMensaje(String message){
        this.message += message+"\n";
        // Indica que el mensaje ha cambiado
        this.setChanged();
        // Notifica a los observadores que el mensaje ha cambiado y se lo pasa
        // (Internamente notifyObservers llama al metodo update del observador)mensaje
        this.notifyObservers(this.getMenssage());
    }
    
    public void modifyScores(JSONArray scores){
        scores.forEach((obj) -> {
            this.array.add(obj);
        });
        // Indica que el mensaje ha cambiado
        this.setChanged();
        // Notifica a los observadores que el mensaje ha cambiado y se lo pasa
        // (Internamente notifyObservers llama al metodo update del observador)mensaje
        this.notifyObservers(this.getScores());
    }
}
