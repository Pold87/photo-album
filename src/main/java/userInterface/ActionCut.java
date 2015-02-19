package main.java.userInterface;

import java.awt.image.BufferedImage;

public class ActionCut extends Action{
	BufferedImage newImage, oldImage;
	int newWidth, newHeight, oldHeight, oldWidth;
	
    public ActionCut(MyImage image, OurController ourController, int oldX, int oldY, BufferedImage oldImage, int oldWidth, int oldHeight) {
		super(image, "Cut", ourController, oldX, oldY, image.getX(), image.getY());
		newImage = image.getImg();
		newWidth = image.getWidth();
		newHeight = image.getHeight();
		this.oldHeight = oldHeight;
		this.oldWidth = oldWidth;
		this.oldImage = oldImage;
    }

    public void redo() {
    	super.redo();
        ourController.selectPicture(image);
        image.setImg(newImage);
        image.setHeight(newHeight);
        image.setWidth(newWidth);
        image.setX(newX);
        image.setY(newY);
    }

    public void undo() {
    	super.undo();
		ourController.selectPicture(image);
		image.setImg(oldImage);
		image.setWidth(oldWidth);
		image.setHeight(oldHeight);
		image.setX(oldX);
		image.setY(oldY);
    }
}