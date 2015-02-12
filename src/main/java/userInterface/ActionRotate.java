package main.java.userInterface;

public class ActionRotate implements Action {
	private MyImage image;
	private int degrees;
	private OurController ourController;
	
	
	public ActionRotate(MyImage image, int degrees, OurController ourController) {
		this.image = image;
		this.degrees = degrees;
		this.ourController = ourController;
		long time = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction("Rotated picture " + image.getNum() + "  " + degrees + " degrees." + " Timestamp: " + time);
	}

	public void redo() {
		ourController.selectPicture(image);
		ourController.rotate(degrees);
		ourController.logger.logAction("Redo");
	}


	public void undo() {
		System.out.println("undo rotate");
		ourController.selectPicture(image);
		ourController.rotate(-degrees);
		ourController.removeLastActionFromList();
		ourController.logger.logAction("Undo");
	}

}
