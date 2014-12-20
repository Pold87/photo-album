package main.java.userInterface;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pold on 12/11/14.
 */
public class DebugPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;

    public DebugPanel() {

        textArea = new JTextArea();
        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);

    }

    public void appendText(String text) {
        textArea.append(text);
    }

}
