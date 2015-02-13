package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class ActionAddPic implements UndoableEdit {
	private MyImage image;
	private OurController ourController;
	//private Page page; //Add when page functionality is available.
	
	public ActionAddPic(MyImage picture, OurController ourController) {
		this.image = picture;
		this.ourController = ourController;
		long time = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Added picture " + picture.getNum()+ " Timestamp: " + time);

	}
	

	public void redo() {
		ourController.contentPanel.addPictureToCurrentPage(image);
		ourController.selectPicture(image);
		ourController.logger.logAction("Redo");
		ourController.removeButtonFromLibrary(image);
	}


	public void undo() {
		ourController.selectPicture(image);
		ourController.contentPanel.deleteSelectedPicture();
		ourController.logger.logAction("Undo");
		ourController.addButtonToLibrary(image);
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
