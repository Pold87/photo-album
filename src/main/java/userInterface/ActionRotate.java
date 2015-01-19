package main.java.userInterface;

public class ActionRotate implements Action {
	private MyImage image;
	private int degrees;
	private OurController ourController;
	
	
	public ActionRotate(MyImage image, int degrees, OurController ourController) {
		this.image = image;
		this.degrees = degrees;
		this.ourController = ourController;
	}

	@Override
	public void redo() {
		ourController.selectPicture(image);
		ourController.rotate(degrees);
	}

	@Override
	public void undo() {
		ourController.selectPicture(image);
		ourController.rotate(-degrees);
		ourController.removeLastActionFromList();
	}

}
