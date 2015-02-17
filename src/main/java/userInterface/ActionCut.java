package main.java.userInterface;

import java.awt.image.BufferedImage;

public class ActionCut extends Action{
	BufferedImage newImage;
	int newWidth, newHeight;
	
    public ActionCut(MyImage image, OurController ourController, int oldX, int oldY) {
		super(image, "Cut", ourController, oldX, oldY, image.getX(), image.getY());
		newImage = image.getImg();
		newWidth = image.getWidth();
		newHeight = image.getHeight();
    }

    //TODO this still needs to be implemented
    public void redo() {
    	super.redo();
        ourController.selectPicture(image);
        image.setImg(newImage);
        image.setHeight(newHeight);
        image.setWidth(newWidth);
        image.setX(newX);
        image.setY(newY);
    }

    //TODO this still needs to be implemented
    public void undo() {
    	super.undo();
		ourController.selectPicture(image);
		image.resetToOriginalImage();
		image.setX(oldX);
		image.setY(oldY);
    }
}