package main.java.userInterface;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

/**
 * Created by pold on 12/11/14.
 */
public class Toolbar extends JToolBar {

	private static final long serialVersionUID = 1L;
	private JButton selectButton;
    private JButton speechButton;
    private JButton rotateButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton resizeButton;
    private JButton simpleSpeechButton;
    private JButton deleteButton;
    private JButton cutButton;
    private ToolBarListener listener;
    private OurController controller;

    @SuppressWarnings("serial")
	public Toolbar() {

        super();

        // Set toolbar layout.
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create buttons and add listeners.
        resizeButton = new JButton(new AbstractAction("resize") {
        	public void actionPerformed(ActionEvent actionEvent) {
        		try {
        			listener.toolbarButtonClicked("resize");
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        });
        selectButton = new JButton(new AbstractAction("select") {
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    listener.toolbarButtonClicked("select");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        speechButton = new JButton(new AbstractAction("speech") {

            public void actionPerformed(ActionEvent actionEvent) {

                Thread speechThread = new Thread() {
                    public void run() {
                        try {
                            controller.recognizeSpeech();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                speechThread.start();

            }
        });

        rotateButton = new JButton(new AbstractAction("rotate") {

            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    listener.toolbarButtonClicked("rotate");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        undoButton = new JButton(new AbstractAction("undo") {

            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    listener.toolbarButtonClicked("undo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        redoButton = new JButton(new AbstractAction("redo") {

            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    listener.toolbarButtonClicked("redo");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        simpleSpeechButton = new JButton(new AbstractAction("simpleSpeech") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    listener.toolbarButtonClicked("simpleSpeech");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        deleteButton = new JButton(new AbstractAction("delete") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    listener.toolbarButtonClicked("delete");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cutButton = new JButton(new AbstractAction("cut") {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    listener.toolbarButtonClicked("cut");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });

        // Set Icons for buttons.
        selectButton.setIcon(createIcon("/icons/Write Document.png"));
        speechButton.setIcon(createIcon("/icons/Discussion.png"));
        rotateButton.setIcon(createIcon("/icons/Backup Green Button.png"));
        undoButton.setIcon(createIcon("/icons/Undo.png"));
        redoButton.setIcon(createIcon("/icons/Redo.png"));
        deleteButton.setIcon(createIcon("/icons/Trash Full.png"));
        cutButton.setIcon(createIcon("/icons/cut.png"));
        resizeButton.setIcon(createIcon("/icons/resize.png"));

        // Set ToolTips.
        selectButton.setToolTipText("Select pictures");
        speechButton.setToolTipText("Start and stop speech recognition");
        rotateButton.setToolTipText("Rotate selected pictures");
        undoButton.setToolTipText("Undo the last action");
        redoButton.setToolTipText("Redo the last undone action");
        deleteButton.setToolTipText("Delete the selected picture");
        cutButton.setToolTipText("Choose lines and cut pictures");
        resizeButton.setToolTipText("Resize the selected picture");

        // Add buttons to toolbar.
        add(selectButton);
        add(speechButton);
        add(rotateButton);
        add(undoButton);
        add(redoButton);
//        add(simpleSpeechButton);
        add(deleteButton);
        add(cutButton);
        add(resizeButton);

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

    public void setController(OurController ourController) {
        this.controller = ourController;
    }
}
