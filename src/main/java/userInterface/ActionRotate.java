package main.java.userInterface;

public class ActionRotate extends Action {
	private int degrees;	
	
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
}