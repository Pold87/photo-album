package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class Action implements UndoableEdit{
	protected int imageNr = -1;
	protected MyImage image;
	protected String actionName;
	protected OurController ourController;
	protected long timePerform, timeUndo, timeRedo;
	protected int oldX, oldY, newX, newY;
	
	public Action(MyImage image, String name, OurController ourController,
			int oldX, int oldY, int newX, int newY) {
		super();
		this.image = image;
		if(image != null)
			imageNr = image.getNum();
		this.actionName = name;
		this.ourController = ourController;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		
		timePerform = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction(actionName + " picture " + imageNr + " Timestamp: " + timePerform);
	}
	
	public void redo() {
		timeRedo = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Redo "+ actionName + " From Timestamp: " + timePerform + " at Timestamp: " + timeRedo);
	}


	public void undo() {
		timeUndo = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Undo "+ actionName + " From Timestamp: " + timePerform + " at Timestamp: " + timeUndo);
	}

	@Override
	public boolean addEdit(UndoableEdit anEdit) {
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
	public boolean replaceEdit(UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
