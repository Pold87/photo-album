package main.java.userInterface;

public class ActionRotate implements Action {
	private MyImage image;
	private int degrees;
	private Controller controller;
	
	
	public ActionRotate(MyImage image, int degrees, Controller controller) {
		this.image = image;
		this.degrees = degrees;
		this.controller = controller;
	}

	@Override
	public void redo() {
		controller.selectPicture(image);
		controller.rotate(degrees);
	}

	@Override
	public void undo() {
		controller.selectPicture(image);
		controller.rotate(-degrees);
		controller.removeLastActionFromList();
	}

}
