package main.java.userInterface;

import java.awt.Color;

public class ActionBackground extends Action {
	
	private Color oldBackground, newBackground;

	public ActionBackground(Color oldBackground, Color newBackground,
			OurController ourController) {
		super(null, "Background", ourController, 0, 0, 0, 0);

		this.oldBackground = oldBackground;
		this.newBackground = newBackground;
	}
	
	public void redo() {
		super.redo();
		ourController.contentPanel.setBackground(newBackground);
	}

	public void undo() {
		super.undo();
		ourController.contentPanel.setBackground(oldBackground);
	}
}