package main.java.userInterface;

import javax.swing.undo.UndoableEdit;

public class ActionRotate extends Action {
	int degrees;	
	
	public ActionRotate(MyImage image, int degrees, OurController ourController) {
		super(image, "Rotate", ourController, 0, 0, 0, 0);
		this.degrees = degrees;
	}

	public void redo() {
		super.redo();
		ourController.selectPicture(image);
		ourController.contentPanel.rotate(degrees);
	}


	public void undo() {
		super.undo();
		ourController.selectPicture(image);
		ourController.contentPanel.rotate(-degrees);
	}
	
	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		Action nextEdit = (Action) anEdit;
		if(nextEdit.actionName != "Rotate" || nextEdit.image != image)
			return false;
		else{
			ActionRotate nextRotate = (ActionRotate) nextEdit;
			degrees += nextRotate.degrees;
			return true;
		}
	}
}