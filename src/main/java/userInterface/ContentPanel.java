package main.java.userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.userInterface.OurController.ToolMode;

public class ContentPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

    private ArrayList<Integer> lines = new ArrayList<>();

	private OurController ourController;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int scr_width = screenSize.width;
	private int scr_height = screenSize.height;

	// Speech Recording sign
	boolean speechRecording = false;

	// Speech processing sign
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


	public boolean isSpeechProcessing() {
		return speechProcessing;
	}

	public void setSpeechProcessing(boolean speechProcessing) {
		this.speechProcessing = speechProcessing;
	}

    public int getLeapRightX() {
        return leapRightX;
    }

    public int getLeapRightY() {
        return leapRightY;
    }

    public int getLeapLeftX() {
        return leapLeftX;
    }

    public int getLeapLeftY() {
        return leapLeftY;
    }

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

    public ArrayList<Integer> getLines() {
        return lines;
    }


    public void paintComponent(Graphics g) {

        // Casting to 2D seems to be beneficial.
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);

        // Draw each image in the image list (if it's active)

        Iterator<MyImage> iter = imageList.iterator();
        // Changed to iterator as I got a ConcurrentModificationException (Volker)
        // TODO: Still not working
        while (iter.hasNext()) {
            MyImage i = iter.next();

            if (i.isActive()) {
                i.paint(g2d);
                drawDashedBox(g2d, i);
            }
        }

		// Show progress for speech
		// TODO! Or exclude (it displays a red rectangle). Maybe find a nice icon.

		if (this.speechRecording) {

			g2d.setColor(Color.RED);
			g2d.fillOval(30, 30, 50, 50);

			Font font = new Font("Verdana", Font.BOLD, 30);
			g2d.setFont(font);

			g2d.drawString("REC", 95, 65);

		}

		if (this.speechProcessing) {

			g2d.setColor(Color.BLACK);
			Font font = new Font("Verdana", Font.BOLD, 30);
			g2d.setFont(font);
			g2d.drawString("Processing...", 800, 65);
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

        font = new Font("Verdana", Font.PLAIN, 12);
        g2d.setFont(font);
        g2d.drawString("R", leapRightX - 4, leapRightY - 15);

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

        font = new Font("Verdana", Font.BOLD, 18);
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


        font = new Font("Verdana", Font.PLAIN, 12);
        g2d.setFont(font);
        g2d.drawString("L", leapLeftX - 4, leapLeftY - 15);



        // Draw lines for cutting
        g2d.setStroke(new BasicStroke());

        for (int i = 0; i < lines.size(); i++) {
            if (i == 0 || i == 1) {
                // Draw horizontal line
                int x = lines.get(i);
                g2d.drawLine(x, 0, x, this.scr_height);
            }

            if (i == 2 || i == 3) {
                // Draw vertical line
                int y = lines.get(i);
                g2d.drawLine(0, y, this.scr_width, y);

            }

        }

        repaint();

    }

    public MyImage getSelectedPicture(){
    	return selectedImage;
    }

    public void unselectPicture(Point p) {
    	if (!selectedImage.contains(p) && toolModeIndex == OurController.ToolMode.MOVE) {
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

    public void selectPictureAt(int x, int y){
		// For each image in the image list, get its area and determine if the mouse click occurred in this area.
		for (MyImage i : imageList) {
			if (i.contains(new Point(x, y))) {
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
    }

    public void addPictureToCurrentPage(MyImage image) {
		image.setActive(!image.isActive());
		if (image.isActive()){
			if (leapRightX > 0 && leapRightX < scr_width && leapRightY > 0 && leapRightY < scr_height){
				image.setX(leapRightX);
				image.setY(leapRightY);
			}
			this.imageList.add(image);
		} else {
			this.imageList.remove(image);
		}
		repaint();
	}
    
    public void deleteSelectedPicture(){
    	selectedImage.setActive(false);
    	selectedImage.setSelected(false);
    	imageList.remove(selectedImage);
    	selectedImage = null;
    	repaint();
    }

    public void deletePicture(MyImage img) {

    	if (this.imageList.contains(img)) {
    		img.setActive(false);
    		img.setSelected(false);
    		imageList.remove(img);
    	}

    	repaint();
    }

    public void rotate(double degrees) {
		if (this.selectedImage != null) {
			selectedImage.incrementRotation(degrees);
		}
		repaint();
	}

	public boolean isSpeechRecording() {
		return speechRecording;
	}

	public void setSpeechRecording(boolean speechRecording) {
		this.speechRecording = speechRecording;
	}
	
	private void drawDashedBox(Graphics2D g2d, MyImage i){
//		Rectangle2D sprite = new Rectangle2D.Double(i.getX(), i.getY(), i.getWidth(), i.getHeight());
//		Rectangle2D leapPos = new Rectangle2D.Double(leapRightX, leapRightY, 5, 5);
//		if (sprite.intersects(leapPos)) {
//			Stroke oldStroke = g2d.getStroke();
//			g2d.setColor(Color.black);
//			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
//			g2d.setStroke(dashed);
//            g2d.rotate(Math.toRadians(i.getRotationDegrees()), i.getX() + (i.getWidth() / 2), i.getY() +(i.getHeight() / 2));
//			g2d.drawRect(i.getX() - 10, i.getY() - 10, i.getWidth() + 20, i.getHeight() + 20);
//			g2d.setStroke(oldStroke);
//		}
//		g2d.rotate(- Math.toRadians(i.getRotationDegrees()), i.getX() + (i.getWidth() / 2), i.getY() +(i.getHeight() / 2));
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
		selectedImage.setSelected(false);
		selectedImage = null;
	}


    public void emptyLineList() {
        this.lines = new ArrayList<Integer>();
    }

    public void addLine(int x, int y) {

        int size = this.lines.size();

        // Reset lines
        if (this.lines.size() == 4) {
            this.lines = new ArrayList<>();
        }


        if (size == 0 || size == 1) {
            this.lines.add(x);
        } else {
            this.lines.add(y);
        }


        this.repaint();
    }

    public void cut() {

        if (this.getSelectedPicture() != null) {

            int x1 = this.lines.get(0);
            int x2 = this.lines.get(1);
            int y1 = this.lines.get(2);
            int y2 = this.lines.get(3);

            int xLeft, xRight, yUp, yDown;

            if (x1 < x2) {
                xLeft = x1;
                xRight = x2;
            } else {
                xLeft = x2;
                xRight = x1;
            }


            if (y1 < y2) {
                yUp = y1;
                yDown = y2;
            } else {
                yUp = y2;
                yDown = y1;
            }

            MyImage picture = this.getSelectedPicture();
            int picX = picture.getX();
            int picY = picture.getY();
            boolean isOn = (xLeft > picX && xLeft < picX + picture.getWidth()) && (xRight > picX && xRight < picX + picture.getWidth()) && (yUp > picY && yUp < picY + picture.getHeight()) && (yDown > picY && yDown < picY + picture.getHeight());
            if (isOn) {
            
	            int rightOverlap, leftOverlap, upOverlap, downOverlap;
	
	            rightOverlap = Math.max(picX + this.getSelectedPicture().getWidth() - xRight,0);
	            leftOverlap = Math.max(xLeft - picX,0);
	
	            upOverlap = Math.max(yUp - picY,0);
	            downOverlap = Math.max(picY + this.getSelectedPicture().getHeight() - yDown,0);
	
	            int x;
	            int y;
	            int w;
	            int h;
	            
	            boolean left = xLeft > picX && xLeft < picX + picture.getWidth();
	            boolean up = yUp > picY && yUp < picY + picture.getHeight();
	            boolean width = xRight > picX && xRight < picX + picture.getWidth();
	            boolean height = yDown > picY && yDown < picY + picture.getHeight();
	            System.out.println("Left: " + left + ", up: " + up + ", width: " + width + ", height: " + height);
	            if (left && up || left && height) {
	            	x = leftOverlap;
	            	System.out.println("leftIn");
	            }
	            else {
	            	x = picX;
	            	System.out.println("leftOut");
	            }
	            if (up && left || up && width) {
	            	y = upOverlap;
	            	System.out.println("upIn");
	            }
	            else {
	            	y = picY;
	            	System.out.println("upOut");
	            }
	            if (width && up || up && left) {
	            	w = xRight - xLeft;
	            	System.out.println("widthIn");
	            }
	            else {
	            	w = picture.getWidth();
	            	System.out.println("widthOut");
	            }
	            if (height && left || height && width) {
	            	h = yDown - yUp;
	            	System.out.println("heightIn");
	            }
	            else {
	            	h = picture.getHeight();
	            	System.out.println("heightOut");
	            }
	
	            System.out.println("x" + x);
	            System.out.println("y" + y);
	            System.out.println("w" + w);
	            System.out.println("h" + h);
	
	            System.out.println("Picture x is: " + this.getSelectedPicture().getX());
	            System.out.println("Picture y is: " + this.getSelectedPicture().getY());
	            System.out.println("Picture w is: " + this.getSelectedPicture().getImg().getWidth());
	            System.out.println("Picture h is: " + this.getSelectedPicture().getImg().getHeight());
	
	            for (int i : this.lines) {
	                System.out.println("Line is " + i);
	            }
	            BufferedImage subImage;
	            if (height && left && width && up) {
	            	subImage = this.getSelectedPicture().getImg().getSubimage(x, y, w, h);
	            }
	            else
	            	subImage = this.getSelectedPicture().getImg();
	            this.getSelectedPicture().setImg(subImage);
	            this.getSelectedPicture().setWidth(w);
	            this.getSelectedPicture().setHeight(h);
	            if (height && left && width && up) {
		            this.getSelectedPicture().setX(this.getSelectedPicture().getX() + x);
		            this.getSelectedPicture().setY(this.getSelectedPicture().getY() + y);
	            }
	            else {
	            	this.getSelectedPicture().setX(this.getSelectedPicture().getX());
		            this.getSelectedPicture().setY(this.getSelectedPicture().getY());
	            }
            }
        }

    }

}
