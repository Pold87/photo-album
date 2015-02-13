package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class ActionRotate implements UndoableEdit {
	private MyImage image;
	private int degrees;
	private OurController ourController;
	
	
	public ActionRotate(MyImage image, int degrees, OurController ourController) {
		this.image = image;
		this.degrees = degrees;
		this.ourController = ourController;
		long time = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Rotated picture " + image.getNum() + "  " + degrees + " degrees." + " Timestamp: " + time);
	}

	public void redo() {
		ourController.selectPicture(image);
		ourController.contentPanel.rotate(degrees);
		ourController.logger.logAction("Redo");
	}


	public void undo() {
		ourController.selectPicture(image);
		ourController.contentPanel.rotate(-degrees);
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
