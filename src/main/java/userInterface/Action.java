package main.java.userInterface;

import java.awt.Color;

import javax.swing.undo.UndoableEdit;

public class Action implements UndoableEdit{
	private Action previousAction;
	protected OurController.Modality modality;
	protected int imageNr = -999;
	protected MyImage image;
	protected String actionName;
	protected OurController ourController;
	protected long timePerform, timeUndo, timeRedo;
	protected int oldX, oldY, newX, newY;
	protected Color background;
	
	
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

        // TODO: CAN THROW A NULL POINTER EXCEPTION

        if (image != null) {

		if(newX == 0)
			this.newX = image.getX();
		else
			this.newX = newX;
		if(newY == 0)
			this.newY = image.getY();
		else
			this.newY = newY;

        }
		background = ourController.contentPanel.getBackground();
		modality = ourController.currentModality;
		
		if(previousAction == null || !isSameRotate(previousAction))
			log(actionName);
		
		previousAction = this;
	}
	
	private void log (String action){
		timePerform = (System.currentTimeMillis() - App.startTime)/100;
		double time = timePerform*1.0/10;
		ourController.logger.logAction(modality + ", " + action + ", " + newX + ", " + newY + ", " + image.getRotationDegrees() + ", " + image.getWidth() + ", " + image.getHeight() + ", " + background + ", " + time);
		
	}
	
	public void redo() {
		log("Redo");
	}


	public void undo() {
		log("Undo");
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
	
	/**
	 * Really nasty hack to fix a log entry per partial rotate.
	 * @param a
	 * @return
	 */
	public boolean isSameRotate(Action a){
		if(a.actionName == "Rotate" && a.image == image)
			return true;
		else
			return false;
	}
	
	
	
	
}
