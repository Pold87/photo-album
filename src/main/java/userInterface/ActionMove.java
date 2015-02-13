package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class ActionMove implements UndoableEdit {

	private MyImage target;
	private int oldX, oldY, newX, newY;
	private OurController ourController;
	
	public ActionMove(MyImage target, int oldX, int oldY, int newX, int newY, OurController ourController) {
		this.target = target;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		this.ourController = ourController;
        // Added by Volker because I got a NullPointerException
        if (target != null) {
        	long time = (System.currentTimeMillis() - App.startTime)/1000;
            ourController.logger.logAction("Move picture " + target.getNum() + " to " + newX + " " + newY+ " Timestamp: " + time);
        }
	}

	public void redo() {
		ourController.selectPicture(target);
		target.setX(newX);
		target.setY(newY);
		ourController.logger.logAction("Redo");
	}

	public void undo() {
		ourController.selectPicture(target);
		target.setX(oldX);
		target.setY(oldY);
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
