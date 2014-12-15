package main.java.userInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pold on 12/10/14.
 */
public class ContentPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<MyImage> imageList;
    private Integer oldMouseX, oldMouseY;
    private String whichButton;

    public ContentPanel() throws IOException {
    	setPreferredSize(new Dimension(600, 600));
        imageList = new ArrayList<MyImage>();
        whichButton = "none"; //at the beginning no button is selected
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
    }

    public ArrayList<MyImage> getImageList() { return imageList; }

    public String getWhichButton() {
        return whichButton;
    }

    public void setWhichButton(String whichButton) {
        this.whichButton = whichButton;
    }

    public void setImage(MyImage image) {
        if (image.isActive()) {
            this.imageList.add(image);
        }
        else {
            this.imageList.remove(image);
        }
    }

    // For mouse click events (to select and unselect pictures)
    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        	super.mouseClicked(mouseEvent);

        	// Get current mouse position.
        	Point clickPoint = mouseEvent.getPoint();

        	// For each image in the image list, get its area and determine if the mouse click occurred in this area.
        	MyImage selectedImage = null;
        	for (MyImage i : imageList) {
        		//Does this still work when the picture is rotated?
        		Rectangle pictureArea = new Rectangle(i.getX(), i.getY(), i.getImg().getWidth(), i.getImg().getHeight());
        		if (pictureArea.contains(clickPoint)) {
        			selectedImage = i;
        		}
        	}
        	if(selectedImage != null){
        		if (getWhichButton().equals("rotate")) {
        			System.out.println("Lets rotate this stuff.");
        			selectedImage.incrementRotation(45);
        		} else {
        			for(MyImage i2: imageList){
        				i2.setSelected(false);
        			}                        	
        			selectedImage.setSelected(true);
        			imageList.remove(selectedImage);
        			imageList.add(selectedImage);
        		}
        	}
        	// Repaint everything in order to see changes
        	repaint();
        }
        
        public void mouseReleased(MouseEvent event){
        	super.mouseReleased(event);
        	oldMouseX = null;
        	oldMouseY = null;
        	
        }
    }

    class MyMouseMotionListener implements MouseMotionListener {

        
        public void mouseDragged(MouseEvent mouseEvent){
            // Set mouse position, if there is no old Mouse position.
            if (oldMouseX == null | oldMouseY == null) {
                oldMouseX = mouseEvent.getX();
                oldMouseY = mouseEvent.getY();
            } else {
                // Get current mouse position
                int mouseX = mouseEvent.getX();
                int mouseY = mouseEvent.getY();

                // Get difference between old mouse position and current position
                Integer diffX = mouseX - oldMouseX;
                Integer diffY = mouseY - oldMouseY;

                // Update position for every image in the image list.
                for(MyImage i:imageList)
                    if(i.isSelected()){
                        i.setX(i.getX() + diffX);
                        i.setY(i.getY() + diffY);
                    }

                // Set old mouse position to current position.
                oldMouseX = mouseX;
                oldMouseY = mouseY;
            }

            // Repaint everything in order to see changes.
            repaint();
        }

        
        public void mouseMoved(MouseEvent mouseEvent) {
        }
    }


    public void paintComponent(Graphics g) {

        // Casting to 2D seems to be beneficial.
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        System.out.println("Draw all the things.");

        // Draw each image in the image list (if it's active)
        for (MyImage i : imageList) {
            if (i.isActive()) {
            	i.paint(g2);
            }

        }

    }


}
