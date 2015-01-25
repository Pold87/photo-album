package main.java.userInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pold on 12/10/14.
 */
public class ContentPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	//Needs edit when we make multiple pages
	private ArrayList<MyImage> imageList;
	private MyImage selectedImage;

    public ContentPanel(OurController ourController) throws IOException {
    	setPreferredSize(new Dimension(600, 600));
        imageList = new ArrayList<MyImage>();
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        addMouseListener(ourController);
        addMouseMotionListener(ourController);
    }

    public ArrayList<MyImage> getImageList() { 
    	return imageList;
    }

    public void paintComponent(Graphics g) {

        // Casting to 2D seems to be beneficial.
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // Draw each image in the image list (if it's active)
        System.out.println("Draw all the things!!");
        for (MyImage i : imageList) {
            if (i.isActive()) {
            	i.paint(g2);
            }

        }
    }
    
    /**
     *  Needs update if we want to select multiple images at once.
     * @return null if no picture is selected.
     */
    public MyImage getSelectedPicture(){
    	for(MyImage i: imageList){
    		if(i.isSelected())
    			return i;
    	}
    	return null;
    }
    
    public void selectPicture(MyImage image){
    	if(selectedImage != null){
    		selectedImage.setSelected(false);
    	}
    	selectedImage = image;
    	image.setSelected(true);
    	imageList.remove(selectedImage);
		imageList.add(selectedImage);
		repaint();
    }
    
    public void selectPicture(int nr){
    	if(selectedImage != null){
    		selectedImage.setSelected(false);
    	}

		for (MyImage i : this.imageList) {

			if (i.getNum() == nr) {
				i.setSelected(true);
				imageList.remove(i);
				imageList.add(i);
				this.selectedImage = i;
			}
		}
		repaint();
    }

    public MyImage selectPictureAt(int x, int y){
		
    	// For each image in the image list, get its area and determine if the mouse click occurred in this area.
    	selectedImage = null;
    	for (MyImage i : imageList) {
    		//Does this still work when the picture is rotated? Nope, fix pending.
    		Rectangle pictureArea = new Rectangle(i.getX(), i.getY(), i.getImg().getWidth(), i.getImg().getHeight());
    		if (pictureArea.contains(new Point(x, y))) {
    			selectedImage = i;
    		}
    	}
    	if(selectedImage != null){
    			for(MyImage i2: imageList){
    				i2.setSelected(false);
    			}                        	
    			selectedImage.setSelected(true);
    			imageList.remove(selectedImage);
    			imageList.add(selectedImage);
    	}
    	// Repaint everything in order to see changes
    	repaint();
    	
    	
    	return selectedImage;
    }
    
    /**
     * Edit when functionality for more pages is created.
     */
    public void addPictureToCurrentPage(MyImage image) {

		System.out.println(image.getNum());

		image.setActive(!image.isActive());
		if (image.isActive()) {
			this.imageList.add(image);
		} else {
			this.imageList.remove(image);
		}

		repaint();
	}

	/**
     * Also needs an edit when we have multiple pages.
     * @param nr
     * @return The deleted Image, can be useful. 
     */
    public MyImage deletePictureFromCurrentPage(int nr){
    	MyImage image = imageList.remove(nr);
    	image.setActive(false);
    	image.setSelected(false);
    	repaint();
    	return image;
    }
    public MyImage deleteSelectedPicture(){
    	selectedImage.setActive(false);
    	selectedImage.setSelected(false);
    	imageList.remove(selectedImage);
    	repaint();
    	return selectedImage;
    }
    
    public void rotate(){
    	selectedImage.incrementRotation(45);
    	repaint();
    }

    public void rotate(int degrees){
    	selectedImage.incrementRotation(degrees);
    	repaint();
    }
}
