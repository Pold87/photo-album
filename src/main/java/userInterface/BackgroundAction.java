package main.java.userInterface;

import java.awt.Color;

public class BackgroundAction implements Action {
	
	private Color oldBackground, newBackground;
	private Controller controller;

	public BackgroundAction(Color oldBackground, Color newBackground,
			Controller controller) {
		this.oldBackground = oldBackground;
		this.newBackground = newBackground;
		this.controller = controller;
	}
	
	public void redo() {
		controller.setBackground(newBackground);
	}

	public void undo() {
		controller.setBackground(oldBackground);
	}

}
