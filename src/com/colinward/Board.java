package com.colinward;

/**
 * Created by CW046360 on 6/28/2016.
 * Base game code provided by zetcode.com
 * I took said code and implemented a neural network in it
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final boolean NEURAL_NETWORK = true;
    private boolean initBrain = false;

    private GeneticAlgorithm brain;

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 26;
    private final int LAZY_CONSTANT = 1000;
    private int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int lazyCounter = 0;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean lazy = false;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    private double[] currentOutput;
    private long start;

    public Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/com/colinward/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/com/colinward/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/com/colinward/head.png");
        head = iih.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();
        timer = new Timer(DELAY, this);
        start = System.currentTimeMillis();
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        int timeInMillis = (int)(System.currentTimeMillis() - start);
        brain.currentGenFitness(timeInMillis, dots - 3);
        brain.nextGeneration();
        System.out.println(brain.printStats());
        inGame = true;
        initGame();
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            lazy = false;
            lazyCounter = 0;
            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if(!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS)+(B_WIDTH/10 - RAND_POS)/2;
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    private void think(){
        lazyCounter++;
        double nextToWall = 0.0;
        if(x[0] <= DOT_SIZE || x[0] >= B_WIDTH - DOT_SIZE || y[0] <= DOT_SIZE || y[0] >= B_HEIGHT - DOT_SIZE) {
            nextToWall = 1.0;
        }
        double distance = Math.sqrt(Math.pow(apple_x - x[0],2) + Math.pow(apple_y - y[0], 2));
        double[] inputs = {distance, x[0] - apple_x, y[0] - apple_y, nextToWall};
        if(!initBrain) {
            brain = new GeneticAlgorithm(inputs);
            initBrain = true;
        }
        currentOutput = brain.updateCurrentGen(inputs);
        if(currentOutput[0] >= currentOutput[1] && currentOutput[0] > currentOutput[2] && currentOutput[0] > currentOutput[3] && !rightDirection){
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
        else if(currentOutput[1] >= currentOutput[0] && currentOutput[1] > currentOutput[2] && currentOutput[1] > currentOutput[3] && !leftDirection){
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
        else if(currentOutput[2] >= currentOutput[0] && currentOutput[2] > currentOutput[1] && currentOutput[2] > currentOutput[3] && !downDirection){
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
        else if(currentOutput[3] >= currentOutput[0] && currentOutput[3] > currentOutput[1] && currentOutput[3] > currentOutput[2] && !upDirection){
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
        if(lazyCounter > LAZY_CONSTANT)
            lazy = true;
        if(lazy) {
            inGame = false;
            System.out.println("LAZY!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkApple();
            checkCollision();
            think();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if (key == KeyEvent.VK_R) {
                inGame = true;
                timer.stop();
                initGame();
            }

            if (key == KeyEvent.VK_W && DELAY > 10) {
                DELAY -= 10;
                timer.setDelay(DELAY);
            }

            if (key == KeyEvent.VK_Q && DELAY < 200) {
                DELAY += 10;
                timer.setDelay(DELAY);
            }
        }
    }
}
