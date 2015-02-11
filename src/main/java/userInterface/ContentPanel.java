package main.java.userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import main.java.userInterface.OurController.ToolMode;

public class ContentPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private OurController ourController;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int scr_width = screenSize.width;
	private int scr_height = screenSize.height;

	// Speech Processing sign
	boolean speechProcessing = false;

	//Needs edit when we make multiple pages
	private ArrayList<MyImage> imageList;
	private MyImage selectedImage;

    public ContentPanel(OurController ourController, MyImage[] library) throws IOException {
    	setPreferredSize(new Dimension(600, 600));
        imageList = new ArrayList<>();
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        addMouseListener(ourController);
        addMouseMotionListener(ourController);
		this.ourController = ourController;
		if(App.testMode != App.TestMode.Train){
			createPresetPage(library);
		}
    }

    public ArrayList<MyImage> getImageList() { 
    	return imageList;
    }

	// Leap Stuff
	ToolMode toolModeIndex = ToolMode.MOVE;

	// Leap cursor right
	private int leapRightX = 9999, leapRightY = 9999;
	private float leapRightScreenDist = 1.0f;
	private boolean leapRightClick = false;

	// Leap cursor left
	private int leapLeftX = 9999, leapLeftY = 9999;
	private float leapLeftScreenDist = 1.0f;
	private boolean leapLeftClick = false;

	public void setToolMode(ToolMode toolModeIndex) {
		this.toolModeIndex = toolModeIndex;
		ourController.setToolMode(toolModeIndex);
	}

	public void setLeapRightClick(boolean leapClick) {
		this.leapRightClick = leapClick;
		repaint();
	}

	public void setLeapRightX(int leapX) {
		this.leapRightX = leapX;
		repaint();
	}

	public void setLeapRightY(int leapY) {
		this.leapRightY = leapY;
		repaint();
	}

	public void setLeapRightScreenDist(float leapScreenDist) {
		this.leapRightScreenDist = leapScreenDist;
		repaint();
	}

	public void setLeapLeftClick(boolean leapClick) {
		this.leapLeftClick = leapClick;
		repaint();
	}

	public void setLeapLeftX(int leapX) {
		this.leapLeftX = leapX;
		repaint();
	}

	public void setLeapLeftY(int leapY) {
		this.leapLeftY = leapY;
		repaint();
	}

	public void setLeapLeftScreenDist(float leapScreenDist) {
		this.leapLeftScreenDist = leapScreenDist;
		repaint();
	}

    public void paintComponent(Graphics g) {

        // Casting to 2D seems to be beneficial.
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);

        // Draw each image in the image list (if it's active)

        Iterator<MyImage> iter = imageList.iterator();
        // Changed to iterator as I got a ConcurrentModificationException (Volker)
        while (iter.hasNext()) {
            MyImage i = iter.next();

            if (i.isActive()) {
                i.paint(g2d);
                drawDashedBox(g2d, i);
            }
        }

		// Show progress for speech
		// TODO! Or exclude (it displays a red rectangle). Maybe find a nice icon.

		if (this.speechProcessing) {

			g2d.fillRect(300, 300, 100, 100);
			g2d.fillRect(370, 300, 100, 100);
		}

		// Finger count indicator
		g2d.setColor(Color.black);
		Font font = new Font("Verdana", Font.BOLD, 18);
		g2d.setFont(font);

		//////// Leap cursor right ////////
		g2d.setStroke(new BasicStroke(1)); // Thickness
		// Outline - border
		g2d.setColor(Color.darkGray);
		int leapRightCursorSize = (int) (40 * (leapRightScreenDist + 1));

		g2d.drawOval(leapRightX - leapRightCursorSize / 2, leapRightY - leapRightCursorSize / 2,
				leapRightCursorSize, leapRightCursorSize);
		// Filling
		//System.out.println("leapRightClick: " + leapRightClick);
		g2d.setColor(this.leapRightClick ? new Color(0, 150, 0, 50) : new Color(
				255, 0, 0, 50));
		g2d.fillOval(leapRightX - leapRightCursorSize / 2, leapRightY - leapRightCursorSize / 2,
				leapRightCursorSize, leapRightCursorSize);


		// Finger count indicator
		g2d.setColor(Color.black);
		g2d.setFont(font);

		// Show which action
		switch (toolModeIndex) {
			case MOVE:
				g2d.drawString("M", leapRightX - 6, leapRightY + 6);
				break;
			case ENLARGE:
				g2d.drawString("E", leapRightX - 6, leapRightY + 6);
				break;
			case REDUCE:
				g2d.drawString("Re", leapRightX - 6, leapRightY + 6);
				break;
			case ROTATE:
				g2d.drawString("Ro", leapRightX - 6, leapRightY + 6);
				break;
			case CUT:
				g2d.drawString("C", leapRightX - 6, leapRightY + 6);
				break;
			default:
				break;
		}

        ////// Leap cursor left /////////
        g2d.setStroke(new BasicStroke(1)); // Thickness
        // Outline - border
        g2d.setColor(Color.darkGray);
        int leapLeftCursorSize = (int) (40 * (leapLeftScreenDist + 1));

        g2d.drawOval(leapLeftX - leapLeftCursorSize / 2, leapLeftY - leapLeftCursorSize / 2,
                leapLeftCursorSize, leapLeftCursorSize);
        // Filling
        g2d.setColor(this.leapLeftClick ? new Color(0, 150, 0, 50) : new Color(
                255, 0, 0, 50));
        g2d.fillOval(leapLeftX - leapLeftCursorSize / 2, leapLeftY - leapLeftCursorSize / 2,
                leapLeftCursorSize, leapLeftCursorSize);

        // Finger count indicator
        g2d.setColor(Color.darkGray);
        g2d.setFont(font);


        // Show which action
		switch (toolModeIndex) {
			case MOVE:
				g2d.drawString("M", leapLeftX - 6, leapLeftY + 6);
				break;
			case ENLARGE:
				g2d.drawString("E", leapLeftX - 6, leapLeftY + 6);
				break;
			case REDUCE:
				g2d.drawString("Re", leapLeftX - 6, leapLeftY + 6);
				break;
			case ROTATE:
				g2d.drawString("Ro", leapLeftX - 6, leapLeftY + 6);
				break;
			case CUT:
				g2d.drawString("C", leapLeftX - 6, leapLeftY + 6);
				break;
			default:
				break;
		}

		// Finger count indicator
		g2d.setColor(Color.darkGray);
		repaint();
    }

    public MyImage getSelectedPicture(){
    	for(MyImage i: imageList){
    		if(i.isSelected())
    			return i;
    	}
    	return null;
    }

    public void unselectPicture(Point p) {
    	if (!selectedImage.contains(p)) {
    		selectedImage.setSelected(false);
    		imageList.remove(selectedImage);
    		imageList.add(selectedImage);
    	}
		repaint();
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

    public void addPictureToCurrentPage(MyImage image) {
		image.setActive(!image.isActive());
		if (image.isActive()) {

			// TODO this is for the position !!!

			if (leapRightX < 0 || leapRightX > scr_width ) {
				image.setX(20);
			}

			else if (leapRightY < 0 || leapRightY > scr_height) {
				image.setY(20);

			} else {
				image.setX(leapRightX);
				image.setY(leapRightY);
			}

			this.imageList.add(image);
			
		} else {
			this.imageList.remove(image);
		}
		repaint();
	}
    
    public MyImage deleteSelectedPicture(){
    	selectedImage.setActive(false);
    	selectedImage.setSelected(false);
    	imageList.remove(selectedImage);
    	repaint();
    	return selectedImage;
    }

    public void rotate(double degrees) {
		if (this.selectedImage != null) {
			selectedImage.incrementRotation(degrees);
		}
		repaint();
	}

	public boolean isSpeechProcessing() {
		return speechProcessing;
	}

	public void setSpeechProcessing(boolean speechProcessing) {
		this.speechProcessing = speechProcessing;
	}
	
	private void drawDashedBox(Graphics2D g2d, MyImage i){
		Rectangle2D sprite = new Rectangle2D.Double(i.getX(), i.getY(), i.getWidth(), i.getHeight());
		Rectangle2D leapPos = new Rectangle2D.Double(leapRightX, leapRightY, 5, 5);
		if (sprite.intersects(leapPos)) {
			Stroke oldStroke = g2d.getStroke();
			g2d.setColor(Color.black);
			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
			g2d.setStroke(dashed);
            g2d.rotate(Math.toRadians(i.getRotationDegrees()), i.getX() + (i.getWidth() / 2), i.getY() +(i.getHeight() / 2));
			g2d.drawRect(i.getX() - 10, i.getY() - 10, i.getWidth() + 20, i.getHeight() + 20);
			g2d.setStroke(oldStroke);
		}
		g2d.rotate(- Math.toRadians(i.getRotationDegrees()), i.getX() + (i.getWidth() / 2), i.getY() +(i.getHeight() / 2));
	}
	
	//Edit this here if you want a different starting page for the test conditions.
	private void createPresetPage(MyImage[] library){
		setBackground(Color.cyan);
		addPictureToCurrentPage(library[2]);
		imageList.get(0).setX(800);
		imageList.get(0).setY(100);
		imageList.get(0).incrementRotation(45);
		addPictureToCurrentPage(library[4]);
		
	}
	
	public void unselect(){
		selectedImage = null;
	}
	
	public void removeSelectedPicture(){
		removePicture(selectedImage);
	}
	
	public void removePicture(MyImage image){
		
	}

}
