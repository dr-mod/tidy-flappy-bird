package presentation;

import javax.swing.*;

public class Window extends JFrame {


    public Window() {
        add(new Display());

//        setResizable(false);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);
        setVisible(true); 
        pack();

        setTitle("Flap");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}