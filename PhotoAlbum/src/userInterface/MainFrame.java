package userInterface;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	
	public MainFrame() {
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel libraryPanel = new JPanel();
		libraryPanel.setBackground(Color.BLUE);
		getContentPane().add(libraryPanel);
		
		JPanel workspacePanel = new JPanel();
		workspacePanel.setBackground(Color.ORANGE);
		getContentPane().add(workspacePanel);
	}


	
}
