package main.java.userInterface;

import java.awt.Color;

import javax.swing.undo.UndoableEdit;

public class ActionBackground implements UndoableEdit {
	
	private Color oldBackground, newBackground;
	private OurController ourController;

	public ActionBackground(Color oldBackground, Color newBackground,
			OurController ourController) {
		this.oldBackground = oldBackground;
		this.newBackground = newBackground;
		this.ourController = ourController;
		long time = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Background Changed to " + newBackground.toString()+ " Timestamp: " + time);
	}
	
	public void redo() {
		ourController.contentPanel.setBackground(newBackground);
		ourController.logger.logAction("Redo");
	}

	public void undo() {
		ourController.contentPanel.setBackground(oldBackground);
		ourController.logger.logAction("Undo");
	}

	@Override
	public boolean addEdit(UndoableEdit arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRedoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUndoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSignificant() {
		return true;
	}

	@Override
	public boolean replaceEdit(UndoableEdit arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
