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
	private ArrayList<MyImage> imageList;

    public ContentPanel(Controller controller) throws IOException {
    	setPreferredSize(new Dimension(600, 600));
        imageList = new ArrayList<MyImage>();
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }

    public ArrayList<MyImage> getImageList() { 
    	return imageList;
    }

    public void setImage(MyImage image) {
        if (image.isActive()) {
            this.imageList.add(image);
        }
        else {
            this.imageList.remove(image);
        }
    }

    public void paintComponent(Graphics g) {

        // Casting to 2D seems to be beneficial.
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // Draw each image in the image list (if it's active)
        System.out.println("Draw all the things.");
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

    public MyImage selectPictureAt(int x, int y){
		
    	// For each image in the image list, get its area and determine if the mouse click occurred in this area.
    	MyImage selectedImage = null;
    	for (MyImage i : imageList) {
    		//Does this still work when the picture is rotated?
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
    

}
