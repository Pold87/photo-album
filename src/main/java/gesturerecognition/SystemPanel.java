package main.java.gesturerecognition;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SystemPanel extends JPanel {

	public SystemPanel(final Window window) {
		super();
		System.out.println("Systempanel created");

		JButton exitButton = new JButton("Exit");
		this.add(exitButton);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Exit application
				System.out.println("Exit");
				window.disconnectLeapMotion();
				window.dispose();
			}
		});

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
