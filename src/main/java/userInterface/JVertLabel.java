package main.java.userInterface;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pold on 12/10/14.
 */

public class JVertLabel extends JComponent {
    private String text;

    public JVertLabel(String s){
        text = s;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        g2d.rotate(Math.toRadians(270.0));
        g2d.drawString(text, 0, 0);
    }
}