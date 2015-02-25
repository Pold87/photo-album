package main.java.userInterface;

import java.awt.image.BufferedImage;

public class ActionDelete extends Action {
	int oldHeight, oldWidth, oldOrientation;
	BufferedImage oldBufImage;
	
	public ActionDelete(MyImage image, OurController ourController) {
		super(image, "Delete", ourController, image.getX(), image.getY(), 0, 0);
		oldHeight = image.getHeight();
		oldWidth = image.getWidth();
		oldOrientation = (int)image.getRotationDegrees();
		oldBufImage = image.getImg();
	}
	

	public void redo() {
		super.redo();
		ourController.selectPicture(image);
		ourController.contentPanel.deleteSelectedPicture();
		ourController.addButtonToLibrary(image);
	}


	public void undo() {
		super.undo();
		ourController.contentPanel.addPictureToCurrentPage(image);
		ourController.selectPicture(image);
		image.setX(oldX);
		image.setY(oldY);
		image.setHeight(oldHeight);
		image.setWidth(oldWidth);
		image.setImg(oldBufImage);
		image.setOrientation(oldOrientation);
		ourController.removeButtonFromLibrary(image);
	}
}