/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Score;
import View.FrmMain;

/**
 *
 * @author Daryl Ospina
 */
public class CtlScore implements Runnable {

    private boolean running;
    private final Thread hilo;
    private int score;

    public CtlScore(String nickNameGamer) {
        this.running = true;
        this.score = 0;
        this.hilo = new Thread(this);
        this.hilo.start();
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                this.incrementScore();
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("[Error] : " + ex);
            }
        }
        if (!FrmMain.ctl.findReply(score, FrmMain.nickName)) {
            FrmMain.ctl.listScore.add(new Score(FrmMain.nickName, score));//Se añade a la lista de score
            FrmMain.ctl.listScore();//Se actualiza
        }
    }

    public synchronized void stopScore() {
        this.running = false;
    }

    private synchronized void incrementScore() throws InterruptedException {
        FrmMain.lblScore.setText(score + "sg");//Se cambia el texto del score por el actual
        Thread.sleep(1000);
        this.score += 1;
        FrmMain.lblScore.setText(score + "sg");//Se cambia el texto del score por el actual
    }

}
