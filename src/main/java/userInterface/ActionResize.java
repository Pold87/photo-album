package main.java.userInterface;

public class ActionResize implements Action {

	private MyImage image;
	private String form;
	private OurController ourController;
	private int oldX;
	private int oldY;
	private int newX;
	private int newY;
	private int oldWidth;
	private int oldHeight;
	private int newWidth;
	private int newHeight;
	
	public ActionResize(MyImage image, String form, int oldX, int oldY, int newX, int newY, int oldWidth, int oldHeight, int newWidth, int newHeight, OurController ourController) {
		this.image = image;
		this.form = form;
		this.ourController = ourController;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		this.oldWidth = oldWidth;
		this.oldHeight = oldHeight;
		this.newWidth = newWidth;
		this.newHeight = newHeight;
		long time = (System.currentTimeMillis() - App.startTime)/1000;
		ourController.logger.logAction(this.form + " picture " + image.getNum() + " Timestamp: " + time);
	}
	
	@Override
	public void redo() {
		ourController.selectPicture(image);
		image.setX(newX);
		image.setY(newY);
		image.resizeImg(newWidth, newHeight);
		ourController.logger.logAction("Redo");
	}

	@Override
	public void undo() {
		System.out.println("undo resize");
		ourController.selectPicture(image);
		image.setX(oldX);
		image.setY(oldY);
		image.resizeImg(oldWidth, oldHeight);
		ourController.removeLastActionFromList();
		ourController.logger.logAction("Undo");
	}

}