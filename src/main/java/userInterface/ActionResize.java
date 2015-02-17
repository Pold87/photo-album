package main.java.userInterface;

public class ActionResize extends Action {

	private int oldWidth, oldHeight, newWidth, newHeight;
	
	public ActionResize(MyImage image, String form, int oldX, int oldY, int newX, int newY, int oldWidth, int oldHeight, int newWidth, int newHeight, OurController ourController) {
		super(image, form, ourController, oldX, oldY, newX, newY);
		this.oldWidth = oldWidth;
		this.oldHeight = oldHeight;
		this.newWidth = newWidth;
		this.newHeight = newHeight;
	}
	
	@Override
	public void redo() {
		super.redo();
		ourController.selectPicture(image);
		image.setX(newX);
		image.setY(newY);
		image.resizeImg(newWidth, newHeight);
	}

	@Override
	public void undo() {
		super.undo();
		ourController.selectPicture(image);
		image.setX(oldX);
		image.setY(oldY);
		image.resizeImg(oldWidth, oldHeight);
	}
}