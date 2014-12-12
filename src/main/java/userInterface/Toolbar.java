package main.java.userInterface;

import main.java.speechrecognition.Wit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

/**
 * Created by pold on 12/11/14.
 */
public class Toolbar extends JToolBar {

    private JButton selectButton;
    private JButton speechButton;
    private JButton moveButton;
    private JButton rotateButton;
    private ToolBarListener listener;

    public Toolbar() {

        // Set toolbar layout.
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create buttons and add listeners.
        selectButton = new JButton(new AbstractAction("select") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        speechButton = new JButton(new AbstractAction("speech") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    // Extract String from speechResponse
                    Wit wit= new Wit(new File("recording.wav"), "wav");
                    listener.recognizedText(wit.getWitRawJSONString());
                    listener.recognizedWitResponse(wit.getWitResponse());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        moveButton = new JButton(new AbstractAction("move") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        rotateButton = new JButton(new AbstractAction("rotate") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // Set Icons for buttons.
        selectButton.setIcon(createIcon("/icons/Write Document.png"));
        speechButton.setIcon(createIcon("/icons/Discussion.png"));
        moveButton.setIcon(createIcon("/icons/Import Picture Document.png"));
        rotateButton.setIcon(createIcon("/icons/Backup Green Button.png"));


        // Set ToolTips.
        selectButton.setToolTipText("Select pictures");
        speechButton.setToolTipText("Start and stop speech recognition");
        moveButton.setToolTipText("Start moving selected pictures");
        rotateButton.setToolTipText("Rotate selected pictures");

        // Add buttons to toolbar.
        add(selectButton);
        add(speechButton);
        add(moveButton);
        add(rotateButton);

        // Use non-draggable toolbar.
        setFloatable(false);

    }

    // Create a ImageIcon for buttons
    private ImageIcon createIcon(String path) {
        URL url = getClass().getResource(path);
        ImageIcon icon = new ImageIcon(url);
        return icon;
    }


    public void setToolBarListener(ToolBarListener listener) {
        this.listener = listener;
    }
}
