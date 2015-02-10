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
		ourController.logger.logAction("Background Changed to " + newBackground.toString());
	}
	
	public void redo() {
		ourController.setBackground(newBackground);
		ourController.logger.logAction("Redo");
	}

	public void undo() {
		ourController.setBackground(oldBackground);
		ourController.removeLastActionFromList();
		ourController.logger.logAction("Undo");
	}

}
