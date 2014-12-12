package main.java.userInterface;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Photo {
	private String imagedir;
	private String pic;
	private int buttonWidth;
	private int buttonHeight;
	private float sideLength;
	BufferedImage bImage;
    ImageIcon thumbnailIcon; 
    ImageIcon dispPic;
	
	public Photo(String imagedir, String pic, int buttonWidth, int buttonHeight, float sideLength) throws IOException {
		this.imagedir = imagedir;
		this.pic = pic;
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;
		this.sideLength = sideLength;
		URL url = getClass().getResource(imagedir + pic);
		this.bImage = ImageIO.read(url);
		this.thumbnailIcon = new ImageIcon(getScaledImage(bImage, buttonWidth, buttonHeight));
		this.dispPic = displayPic(bImage, sideLength);
	}
	
	
	public String getImagedir() {
		return imagedir;
	}

	public void setImagedir(String imagedir) {
		this.imagedir = imagedir;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getButtonWidth() {
		return buttonWidth;
	}

	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
	}

	public int getButtonHeight() {
		return buttonHeight;
	}

	public void setButtonHeight(int buttonHeight) {
		this.buttonHeight = buttonHeight;
	}

	public float getSideLength() {
		return sideLength;
	}

	public void setSideLength(float sideLength) {
		this.sideLength = sideLength;
	}


	public BufferedImage getbImage() {
		return bImage;
	}

	public void setbImage(BufferedImage bImage) {
		this.bImage = bImage;
	}

	public ImageIcon getThumbnailIcon() {
		return thumbnailIcon;
	}

	public void setThumbnailIcon(ImageIcon thumbnailIcon) {
		this.thumbnailIcon = thumbnailIcon;
	}

	public ImageIcon getDispPic() {
		return dispPic;
	}

	public void setDispPic(ImageIcon dispPic) {
		this.dispPic = dispPic;
	}

	//Resizes an image to have the given width and height
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
        
    //scales image for display
    private ImageIcon displayPic(BufferedImage photo, float sideLength){
            float width = (float) photo.getWidth();
            float height = (float) photo.getHeight();
            float h;
            float w;
            //horizontal image
            if (width>height) {
            	w= sideLength;
            	h = (w/width)*height;
            }
            //vertical image
            else if (height>width){
            	h=sideLength;
                w = (h/height)*width;
            }
            else{
            	h=sideLength;
                w=sideLength;
            }
            final ImageIcon displayPhoto = new ImageIcon(getScaledImage(photo, (int) w, (int) h));
            return displayPhoto;
           
        }

}
