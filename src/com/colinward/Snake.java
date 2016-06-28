package com.colinward;

/**
 * Created by CW046360 on 6/28/2016.
 * Base game code provided by zetcode.com
 * I took said code and implemented a neural network in it
 */
import java.awt.EventQueue;
import javax.swing.JFrame;


public class Snake extends JFrame {

    public Snake() {


        add(new Board());

        setResizable(false);
        pack();

        setTitle("snA.I.ke");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame ex = new Snake();
                ex.setVisible(true);
            }
        });
    }
}