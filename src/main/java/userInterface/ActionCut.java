package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class ActionCut implements UndoableEdit{

    private MyImage picture;
    private OurController ourController;

    public ActionCut(MyImage picture, OurController ourController) {
        this.picture = picture;
        this.ourController = ourController;
        long time = (System.currentTimeMillis() - App.startTime)/1000;
        ourController.logger.logAction("Cut picture " + picture.getNum()+ "," + time);
    }

    //TODO this still needs to be implemented
    public void redo() {
        ourController.selectPicture(picture);
        ourController.logger.logAction("Redo");
    }

    //TODO this still needs to be implemented
    public void undo() {
        System.out.println("undo cut");
        ourController.selectPicture(picture);
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
