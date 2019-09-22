/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.FrmMain;
import java.awt.HeadlessException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Daryl Ospina
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String nickname;
        try {
            nickname = JOptionPane.showInputDialog("Por favor ingrese su nickname");
        } catch (HeadlessException e) {
            Logger.getLogger(Main.class.getName()).severe("El nickname no se definio");
            return;
        }

        FrmMain windowMain = new FrmMain(nickname);
        windowMain.setVisible(true);
    }

}
