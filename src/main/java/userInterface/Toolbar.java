package main.java.userInterface;

import main.java.speechrecognition.Record;
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

	private static final long serialVersionUID = 1L;
	private JButton selectButton;
    private JButton speechButton;
    private JButton moveButton;
    private JButton rotateButton;
    private JButton undoButton;
    private JButton redoButton;
    private ToolBarListener listener;

    @SuppressWarnings("serial")
	public Toolbar() {

        // Set toolbar layout.
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create buttons and add listeners.
        selectButton = new JButton(new AbstractAction("select") {
            public void actionPerformed(ActionEvent actionEvent) {
                listener.toolbarButtonClicked("select");
            }
        });
        speechButton = new JButton(new AbstractAction("speech") {
            
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    // Url for recording speech input
                    URL url = getClass().getResource("/recording.wav");

                    // Record wav with external program
                    Record.recordExtern(new File(url.toURI()));
                    Wit wit = new Wit(new File(url.toURI()), "wav");

                    // Send recognized
                    listener.recognizedText(wit.getWitRawJSONString());
                    listener.recognizedWitResponse(wit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        moveButton = new JButton(new AbstractAction("move") {
            
            public void actionPerformed(ActionEvent actionEvent) {
                listener.toolbarButtonClicked("move");
            }
        });

        rotateButton = new JButton(new AbstractAction("rotate") {
            
            public void actionPerformed(ActionEvent actionEvent) {
                listener.toolbarButtonClicked("rotate");
            }
        });
        
        undoButton = new JButton(new AbstractAction("undo") {
            
            public void actionPerformed(ActionEvent actionEvent) {
                listener.toolbarButtonClicked("undo");
            }
        });
        
        redoButton = new JButton(new AbstractAction("redo") {
            
            public void actionPerformed(ActionEvent actionEvent) {
                listener.toolbarButtonClicked("redo");
            }
        });

        // Set Icons for buttons.
        selectButton.setIcon(createIcon("/icons/Write Document.png"));
        speechButton.setIcon(createIcon("/icons/Discussion.png"));
        moveButton.setIcon(createIcon("/icons/Import Picture Document.png"));
        rotateButton.setIcon(createIcon("/icons/Backup Green Button.png"));
        undoButton.setIcon(createIcon("/icons/Undo.png"));
        redoButton.setIcon(createIcon("/icons/Redo.png"));
        
        // Set ToolTips.
        selectButton.setToolTipText("Select pictures");
        speechButton.setToolTipText("Start and stop speech recognition");
        moveButton.setToolTipText("Start moving selected pictures");
        rotateButton.setToolTipText("Rotate selected pictures");
        undoButton.setToolTipText("Undo the last action");
        redoButton.setToolTipText("Redo the last undone action");

        // Add buttons to toolbar.
        add(selectButton);
        add(speechButton);
        add(moveButton);
        add(rotateButton);
        add(undoButton);
        add(redoButton);
        
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

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
    
    public void setEnabledUndoButton(boolean b){
    	undoButton.setEnabled(b);
    }
    
    public void setEnabledRedoButton(boolean b){
    	redoButton.setEnabled(b);
    }
}
