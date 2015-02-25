package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class ActionRotate extends Action {
	int oldOrientation, newOrientation;	
	
	public ActionRotate(MyImage image, int oldOrientation, OurController ourController) {
		super(image, "Rotate", ourController, 0, 0, 0, 0);
		this.oldOrientation = oldOrientation;
	}

	public void redo() {
		super.redo();
		ourController.selectPicture(image);
		ourController.contentPanel.setOrientation(newOrientation);
	}


	public void undo() {
		super.undo();
		ourController.selectPicture(image);
		ourController.contentPanel.setOrientation(oldOrientation);
	}
	
	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		Action nextEdit = (Action) anEdit;
		if(nextEdit.actionName != "Rotate" || nextEdit.image != image)
			return false;
		else{
			ActionRotate nextRotate = (ActionRotate) nextEdit;
			newOrientation = nextRotate.newOrientation;
			return true;
		}
	}
}