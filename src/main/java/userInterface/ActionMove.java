package main.java.userInterface;

public class ActionMove extends Action {
	
	public ActionMove(MyImage image, int oldX, int oldY, int newX, int newY, OurController ourController) {
		super(image, "Move", ourController, oldX, oldY, newX, newY);
	}

	public void redo() {
		super.redo();
		ourController.selectPicture(image);
		image.setX(newX);
		image.setY(newY);
	}

	public void undo() {
		super.undo();
		ourController.selectPicture(image);
		image.setX(oldX);
		image.setY(oldY);
	}
}