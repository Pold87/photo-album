package main.java.userInterface;

import java.awt.Color;

public class ActionBackground implements Action {
	
	private Color oldBackground, newBackground;
	private OurController ourController;

	public ActionBackground(Color oldBackground, Color newBackground,
			OurController ourController) {
		this.oldBackground = oldBackground;
		this.newBackground = newBackground;
		this.ourController = ourController;
	}
	
	public void redo() {
		ourController.setBackground(newBackground);
	}

	public void undo() {
		ourController.setBackground(oldBackground);
		ourController.removeLastActionFromList();
	}

}
