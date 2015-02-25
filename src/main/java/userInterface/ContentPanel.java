package main.java.userInterface;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.userInterface.OurController.ToolMode;

public class ContentPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

    private ArrayList<Integer> lines = new ArrayList<>();

	private OurController ourController;

	private ArrayList<MyImage> rectangles;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int scr_width = screenSize.width;
	private int scr_height = screenSize.height;

	// Speech Recording sign
	boolean speechRecording = false;
    // Speech processing sign
	boolean speechProcessing = false;

	public ArrayList<MyImage> imageList;
	private MyImage selectedImage;

	private boolean drawRectangles;
	private boolean drawPictures;

    public ContentPanel(OurController ourController, MyImage[] library) throws IOException {
    	setPreferredSize(new Dimension(600, 600));
        imageList = new ArrayList<>();
		this.rectangles = new ArrayList<>();
		setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.white);
        addMouseListener(ourController);
        addMouseMotionListener(ourController);
		this.ourController = ourController;
		if(App.testMode != App.TestMode.Train) {
			createPresetPage(library);
		}
    }

	// Leap Stuff
	ToolMode toolModeIndex = ToolMode.MOVE;

    // Leap cursor right
	private int leapRightX = 9999, leapRightY = 9999;
	private float leapRightScreenDist = 1.0f;
	private boolean leapRightClick = false;

	// Leap cursor left
	private float leapLeftScreenDist = 1.0f;


	public void setSpeechProcessing(boolean speechProcessing) {
		this.speechProcessing = speechProcessing;
	}

    public int getLeapRightX() {
        return leapRightX;
    }

    public int getLeapRightY() {
        return leapRightY;
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

	public void setLeapLeftScreenDist(float leapScreenDist) {
		this.leapLeftScreenDist = leapScreenDist;
		repaint();
	}

    public ArrayList<Integer> getLines() {
        return lines;
    }


    public boolean isSpeechProcessing() {
        return speechProcessing;
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


		// Draw rectangles and helper images for templates

		Iterator<MyImage> iterRect = this.rectangles.iterator();
		// Changed to iterator as I got a ConcurrentModificationException (Volker)
		// TODO: Still not working

		if (this.isDrawRectangles()) {
			while (iterRect.hasNext()) {
				MyImage i = iterRect.next();
				if (i.isActive()) {

					if (this.isDrawPictures()) {
						i.paint(g2d);
					} else {
						Graphics2D g2dCopy = (Graphics2D) g2d.create();
						g2dCopy.rotate(Math.toRadians(i.getRotationDegrees()), i.getX() + (i.getWidth() / 2), i.getY() + (i.getHeight() / 2));
						g2dCopy.drawRect(i.getX(), i.getY(), i.getWidth(), i.getHeight());
						g2dCopy.drawString(Integer.toString(i.getNum() + 10), i.getX(), i.getY() - 5);
						g2dCopy.setTransform(new AffineTransform());
						g2dCopy.dispose();
					}

				}
			}
		}


		// Show progress for speech
		// TODO! Or exclude (it displays a red rectangle). Maybe find a nice icon.

		if (this.speechRecording && !this.speechProcessing) {

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
    	if(imageList.contains(image)){
    	selectedImage = image;
    	image.setSelected(true);
    	imageList.remove(selectedImage);
		imageList.add(selectedImage);
		repaint();
    	}
    }
    
    public boolean isPictureAt(int x, int y){
    	for (MyImage i : imageList) {
			if (i.contains(new Point(x, y))) {
				return true;
			}
		}
    	return false;
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
    	selectedImage = null;
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
    
    public void setOrientation(int degrees) {
		if (this.selectedImage != null) {
			selectedImage.setOrientation(degrees);
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
	//Point[] positions, int[] width, int[] height
	private void createPresetPage(MyImage[] library){
		this.setBackground(Color.WHITE);
	}
	
	public void unselect(){
		System.out.println("Unselecting!!!");
		if(selectedImage != null){
		selectedImage.setSelected(false);
		selectedImage = null;
		}
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

        MyImage image = this.getSelectedPicture();

        if (image != null) {

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


            int w_intersect = xRight - xLeft;
            int h_intersect = yDown - yUp;
            Rectangle cut_rect = new Rectangle(xLeft, yUp, w_intersect, h_intersect);

                Rectangle img_rect = new Rectangle(image.getX(), image.getY(), image.getWidth(), image.getHeight());


                if (cut_rect.intersects(img_rect)) {

                    Rectangle intersection = cut_rect.intersection(new Rectangle(image.getX(), image.getY(), image.getWidth(), image.getHeight()));


                    int diff_x = (int) intersection.getX() - image.getX();
                    int diff_y = (int) intersection.getY() - image.getY();


                    BufferedImage enlarged_pic = this.getSelectedPicture().getScaledImage(this.getSelectedPicture().getWidth(), this.getSelectedPicture().getHeight());

                    this.getSelectedPicture().setImg(enlarged_pic.getSubimage(diff_x, diff_y, (int) intersection.getWidth(), (int) intersection.getHeight()));
                    this.getSelectedPicture().setWidth((int) intersection.getWidth());
                    this.getSelectedPicture().setHeight((int) intersection.getHeight());
                    this.getSelectedPicture().setX(this.getSelectedPicture().getX() + diff_x);
                    this.getSelectedPicture().setY(this.getSelectedPicture().getY() + diff_y);
            }
        }

    }

	public void saveContentPanel() throws IOException {

		int x = 1;
		String filename = "frames/" + "contentPanel-" + x + ".ser";
		File file = new File(filename);
		while(file.exists()) {
			x++;
			filename = "frames/" + "contentPanel-" + x + ".ser";
			file = new File(filename);
		}

		FileOutputStream fout = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(this.imageList);
	}

	public void loadContentPanel(String filename) throws IOException, ClassNotFoundException {
		//deserialize the quarks.ser file
		FileInputStream file = new FileInputStream(filename);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream (buffer);
		//deserialize the List
		this.rectangles = (ArrayList<MyImage>) input.readObject();
		//display its data
	}

	public boolean isDrawRectangles() {
		return this.drawRectangles;
	}

	public void setDrawRectangles(boolean rectangles) {
		this.drawRectangles = rectangles;
	}

	public void overwritePictures() {
		this.imageList = this.rectangles;
	}

	public boolean isDrawPictures() {
		return drawPictures;
	}

	public void setDrawPictures(boolean drawPictures) {
		this.drawPictures = drawPictures;
	}

	}
