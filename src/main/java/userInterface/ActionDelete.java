package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class ActionDelete implements UndoableEdit {
	private MyImage image;
	private OurController ourController;
	//private Page page; //Implement when page functionality is added.
	
	public ActionDelete(MyImage image, OurController ourController) {
		this.image = image;
		this.ourController = ourController;
		long time = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Delete picture " + image.getNum()+ " Timestamp: " + time);
	}


	public void redo() {
		ourController.selectPicture(image);
		ourController.contentPanel.deleteSelectedPicture();
		ourController.logger.logAction("Redo");
		ourController.addButtonToLibrary(image);
	}


	public void undo() {
		ourController.contentPanel.addPictureToCurrentPage(image);
		ourController.selectPicture(image);
		ourController.logger.logAction("Undo");
		ourController.removeButtonFromLibrary(image);
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
