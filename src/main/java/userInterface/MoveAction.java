package main.java.userInterface;

public class MoveAction implements Action {

	private MyImage target;
	private int oldX, oldY, newX, newY;
	private Controller controller;
	
	public MoveAction(MyImage target, int oldX, int oldY, int newX, int newY, Controller controller) {
		this.target = target;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		this.controller = controller;
	}

	public void redo() {
		controller.selectPicture(target);
		controller.movePicture(newX, newY);
	}

	public void undo() {
		controller.selectPicture(target);
		controller.movePicture(oldX, oldY);
	}

}
