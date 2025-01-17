package main.java.userInterface;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by pold on 1/25/15.
 */
public class BackgroundBar extends JToolBar{

    private static final long serialVersionUID = 1L;
    private OurController listener;
    private static Color[] colorArray = {
            Color.WHITE
            , Color.BLACK
            , Color.RED
            , Color.BLUE
            , Color.CYAN
            , Color.MAGENTA
            , Color.PINK
            , Color.GREEN
            , Color.YELLOW
            , Color.GRAY
            , Color.ORANGE
    };
    public static Map<Color, String> colorMap = new HashMap<Color, String>();


    public BackgroundBar(OurController listener) throws Exception {
        this.listener = listener;
        setOrientation(SwingConstants.VERTICAL);
        setBackground(Color.white);

        colorMap.put(Color.WHITE, "White");
        colorMap.put(Color.BLACK, "Black");
        colorMap.put(Color.RED, "Red");
        colorMap.put(Color.BLUE, "Blue");
        colorMap.put(Color.CYAN, "Cyan");
        colorMap.put(Color.MAGENTA, "Magenta");
        colorMap.put(Color.PINK, "Pink");
        colorMap.put(Color.GREEN, "Green");
        colorMap.put(Color.YELLOW, "Yellow");
        colorMap.put(Color.GRAY, "Gray");
        colorMap.put(Color.ORANGE, "Orange");

        this.createIcon();
        this.addSeparator();


    }

    public Map<Color, String> getColorMap() {
        return colorMap;
    }


    private void createIcon() {

        for (final Color c : colorArray) {

            JButton imageButton = new JButton();
            imageButton.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent actionEvent) {
                    listener.setBackground(c);
                }
            });

            imageButton.setPreferredSize(new Dimension(100, 100));
            imageButton.setBackground(c);
            if (c.equals(Color.BLACK)) {
                imageButton.setForeground(Color.WHITE);
            }
            imageButton.setForeground(Color.BLACK);
            imageButton.setText(colorMap.get(c));
            imageButton.setOpaque(true);
            this.add(imageButton);

        }


    }


}
