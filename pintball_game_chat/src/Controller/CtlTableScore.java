/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Score;
import View.FrmMain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nick_
 */
public class CtlTableScore {

    public static ArrayList<Score> listScore;

    public CtlTableScore() {
        CtlTableScore.listScore = new ArrayList();
    }

    public void listScore() {
        this.orderBubble(listScore);
        String[] nombreColumnas = {"#", "NickName", "Duración(segundos)"};

        DefaultTableModel modelo = new DefaultTableModel(new Object[][]{}, nombreColumnas);
        
        for (int i = 0; i < CtlTableScore.listScore.size(); i++) {
            modelo.addRow(new Object[]{
                i + 1,
                CtlTableScore.listScore.get(i).getName(),
                CtlTableScore.listScore.get(i).getTime()});
        }

        FrmMain.tblScore.setModel(modelo);
    }
    
    public boolean findReply(int score, String nickname) {
        return CtlTableScore.listScore.stream().anyMatch((objScore) -> (objScore.getName().equals(nickname) && 
                objScore.getTime() == score));
    }

    private void orderBubble(ArrayList<Score> list) {
        int iteration = 1;// establece el lugar hasta donde se iterará
        boolean permutation;
        do {
            permutation = false;// valida que el ciclo no sea infinito
            for (int i = 0; i < list.size() - iteration; i++) {
                if (list.get(i).getTime() <= (list.get(i + 1).getTime())) {
                    permutation = true;
                    this.changePosition(list, i, i + 1);
                }
            }
            iteration++;// elimina la iteracion de una posicion,
            // ya que el mayor ya estaria de ultimo
        } while (permutation);
    }

    private void changePosition(ArrayList<Score> array, int i, int j) {
        Score temp;
        temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

}
