package main.java.userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by pold on 1/25/15.
 */
public class BackgroundBar extends JToolBar{

    private static final long serialVersionUID = 1L;
    private OurController listener;

    public BackgroundBar(OurController listener) throws Exception {
        this.listener = listener;
        setOrientation(SwingConstants.VERTICAL);
        setBackground(Color.white);

        this.createIcon();
        this.addSeparator();


    }

    private void createIcon() {

        Color[] colorArray = {
                  Color.WHITE
                , Color.BLACK
                , Color.BLUE
                , Color.CYAN
                , Color.DARK_GRAY
                , Color.MAGENTA
                , Color.PINK
                , Color.GREEN
                , Color.YELLOW
                , Color.GRAY
                , Color.ORANGE
        };


        for (Color c : colorArray) {

            JButton imageButton = new JButton();
            imageButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    listener.setBackground(c);
                }
            });

            imageButton.setPreferredSize(new Dimension(100, 100));

            imageButton.setBackground(c);
            imageButton.setOpaque(true);
            this.add(imageButton);

        }


    }


}
