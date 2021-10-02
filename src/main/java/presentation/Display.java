package presentation;

import game.Game;
import game.GameNN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Display extends JPanel implements MouseListener, ActionListener, KeyListener {

    Game game;

    public Display() {
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(480,720));
        addKeyListener(this);
        addMouseListener(this);
        this.game = new Game();
        for (int i = 0; i < 1000000; i++){
            this.game.step();
        }
       
//        new Timer(40, this).start();
        new Timer(20, this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        game.show(g);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.game.step();
        repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        this.game.action();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.game.action();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}