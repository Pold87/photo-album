package main.java.gesturerecognition;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.userInterface.*;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {

	private List<Imagedata> shapesList = new ArrayList<Imagedata>();

	public List<Imagedata> getShapesList() {
		return shapesList;
	}

	// Which object is selected
	private Imagedata activeShape;

	// Resize mode
	private double cursorX;
	private double cursorY;

	// Leap cursor right
	private int leapRightX = 9999, leapRightY = 9999;
	private float leapRightScreenDist = 1.0f;
	private boolean leapRightClick = false;
	private int leapRightFingers = 0;
	
	// Leap cursor left
	private int leapLeftX = 9999, leapLeftY = 9999;
	private float leapLeftScreenDist = 1.0f;
	private boolean leapLeftClick = false;
	private int leapLeftFingers = 0;

	// Transform for rotation
	AffineTransformOp op;
	
	// Shape Mode
	public enum ShapeMode {
		IMAGE
	};

	private ShapeMode shapeModeIndex = ShapeMode.IMAGE;


	public ShapeMode getShapeModeIndex() {
		return shapeModeIndex;
	}

	public ToolMode getToolModeIndex() {
		return toolModeIndex;
	}

	@SuppressWarnings("unused")
	private ShapeMode randomShape() {
		int index = new Random().nextInt(ShapeMode.values().length);
		return ShapeMode.values()[index];
	}

	// Tool Mode
	public enum ToolMode {
		MOVE, ENLARGE, REDUCE, ROTATERIGHT, ROTATELEFT, CUT
	};

	ToolMode toolModeIndex = ToolMode.MOVE;

	// Screen width & height
	private int screenWidth, screenHeight;

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	/**********************************************************************************************/

	// Constructor
	public DrawingPanel(int screenWidth, int screenHeight) {

		super();
		repaint();
		System.out.println("Drawingpanel created");
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		this.addKeyListener(new KeyEventListener(this));
	}

	public void setShapeMode(ShapeMode shapeMode) {
		this.shapeModeIndex = shapeMode;
	}

	public void setToolMode(ToolMode toolMode) {
		this.toolModeIndex = toolMode;
	}

	public void setLeapRightFingers(int leapFingers) {
		this.leapRightFingers = leapFingers;
		repaint();
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
	
	public void setLeapLeftFingers(int leapFingers) {
		this.leapLeftFingers = leapFingers;
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

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		// Loop through the shapesList and draw every shape
		for (Imagedata shape : shapesList) {

			// Draw outline
			shape.draw(g2d);
			
			// Draw selection markers
			if (shape.equals(activeShape)) {
				// Coords
				int newx1, newx2, newy1, newy2;
				newx1 = shape.getX1() > shape.getX2() ? shape.getX2() : shape
						.getX1();
				newx2 = shape.getX1() > shape.getX2() ? shape.getX1() : shape
						.getX2();
				newy1 = shape.getY1() > shape.getY2() ? shape.getY2() : shape
						.getY1();
				newy2 = shape.getY1() > shape.getY2() ? shape.getY1() : shape
						.getY2();

				// Create lines
				List<Line> lineList = new ArrayList<Line>();
				int offset = 18;
				lineList.add(new Line(newx1 - offset, newy1 - offset, newx1
						+ offset, newy1 - offset));
				lineList.add(new Line(newx1 - offset, newy1 - offset, newx1
						- offset, newy1 + offset));
				lineList.add(new Line(newx2 + offset, newy2 + offset, newx2
						+ offset, newy2 - offset));
				lineList.add(new Line(newx2 + offset, newy2 + offset, newx2
						- offset, newy2 + offset));

				// Draw lines
				for (Line line : lineList)
					line.draw(g2d);

			}

		}
		//////// Leap cursor right ////////
		g2d.setStroke(new BasicStroke(1)); // Thickness
		// Outline - border
		g2d.setColor(Color.darkGray);
		int leapRightCursorSize = (int) (40 * (leapRightScreenDist + 1));

		g2d.drawOval(leapRightX - leapRightCursorSize / 2, leapRightY - leapRightCursorSize / 2,
				leapRightCursorSize, leapRightCursorSize);
		// Filling
		g2d.setColor(this.leapRightClick ? new Color(0, 150, 0, 50) : new Color(
				255, 0, 0, 50));
		g2d.fillOval(leapRightX - leapRightCursorSize / 2, leapRightY - leapRightCursorSize / 2,
				leapRightCursorSize, leapRightCursorSize);

		// Finger count indicator
		g2d.setColor(Color.black);
		Font font = new Font("Verdana", Font.BOLD, 18);
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
			g2d.drawString("R", leapRightX - 6, leapRightY + 6);
			break;
		case ROTATERIGHT:
			g2d.drawString("RR", leapRightX - 6, leapRightY + 6);
			break;
		case ROTATELEFT:
			g2d.drawString("RL", leapRightX - 6, leapRightY + 6);
			break;
		case CUT:
			g2d.drawString("C", leapRightX - 6, leapRightY + 6);
			break;
		default:
			break;
		}
		//g2d.drawString(String.valueOf(this.leapRightFingers), leapRightX - 6, leapRightY + 6);

		/////// Leap cursor left /////////
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
			g2d.drawString("R", leapLeftX - 6, leapLeftY + 6);
			break;
		case ROTATERIGHT:
			g2d.drawString("RR", leapLeftX - 6, leapLeftY + 6);
			break;
		case ROTATELEFT:
			g2d.drawString("RL", leapLeftX - 6, leapLeftY + 6);
			break;
		case CUT:
			g2d.drawString("C", leapLeftX - 6, leapLeftY + 6);
			break;
		default:
			break;
		}
		//g2d.drawString(String.valueOf(this.leapLeftFingers), leapLeftX - 6, leapLeftY + 6);

		// Outline
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.black);
		g2d.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2d.setColor(Color.white);
		g2d.drawLine(1, 0, 298, 0);
	}

	// Adds a shape to the shapeslist
	private void addShape(Imagedata shape) {
		shapesList.add(shape);
		repaint();
	}
	
	public Image importImage() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Choose Image..");

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG Images", "jpg", "png", "bmp", "gif");
		fc.setFileFilter(filter);

		JFrame newframe = new JFrame("imagechoiceframe");
		int returnVal = fc.showDialog(newframe, "Choose..");
		File file = null;

		if (returnVal == JFileChooser.APPROVE_OPTION)
			file = fc.getSelectedFile();
		BufferedImage image = null;

		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("error");
		}

		// Image resizeimage = image.getScaledInstance(image.getWidth(),
		// image.getHeight()/3, Image.SCALE_SMOOTH);
		return image;

	}

	/*
	 * Cleares the shapesList
	 */
	public void clearShapesList() {
		shapesList.clear();
		repaint();
	}

	/* MOUSE LISTENER */

	public void cursorPressed(int XPos, int YPos) {
		this.requestFocusInWindow();

		// Update mouse Coords
		cursorY = YPos;
		cursorX = XPos;

		// Select active shape or image
		for (int i = 0; i < shapesList.size(); i++) {
			if (shapesList.get(i).getSprite().intersects(XPos, YPos, 5, 5)) {
				activeShape = shapesList.get(i);
				System.out.println("New active shape: " + activeShape);
			}
		}
		
		switch (toolModeIndex) {
		case MOVE:
		case ENLARGE:
		case REDUCE:
		case CUT:
			repaint();
			break;
		case ROTATERIGHT:
			// Rotation information
			activeShape.increaseRotation(30);
			repaint();
			break;
		case ROTATELEFT:
			// Rotation information
			activeShape.increaseRotation(-30);
			repaint();
			break;
		default:
			break;

		}
	}

	public void cursorReleased(int XPos, int YPos) {
		switch (toolModeIndex) {
		case MOVE:
		case ENLARGE:
		case REDUCE:
		case ROTATERIGHT:
		case ROTATELEFT:
		case CUT:
			if (XPos < 0 || XPos > this.getWidth() || YPos < 0
					|| YPos > this.getHeight())
				shapesList.remove(activeShape);
			break;
		default:
			System.out.println("Tool not found: " + toolModeIndex);
			break;
		}
	}

	public void cursorDragged(int XPos, int YPos) {
		double deltaY, deltaX;
		double normalizerX, normalizerY;
		if(!(activeShape==null)) {
			switch (toolModeIndex) {
			case ENLARGE:
				System.out.println("Enlarge");
				// Mouse movement since previous calculation
				deltaY = YPos - cursorY;
				deltaX = XPos - cursorX;

				normalizerX = (double) activeShape.getWidth() / (double) (activeShape.getWidth() + activeShape.getHeight());
				normalizerY = - ((double) activeShape.getHeight() / (double) (activeShape.getWidth() + activeShape.getHeight()));
				
				// Moving up increases height, down decreases height
				activeShape.setY1((int) (activeShape.getY1() + normalizerY));
				activeShape.setY2((int) (activeShape.getY2() - 2*normalizerY));
	
				// Moving right increases width
				activeShape.setX1((int) (activeShape.getX1() - normalizerX));
				activeShape.setX2((int) (activeShape.getX2() + 2*normalizerX));
				break;
			case REDUCE:
				System.out.println("Reduce");
				// Mouse movement since previous calculation
				deltaY = YPos - cursorY;
				deltaX = XPos - cursorX;

				normalizerX = (double) activeShape.getWidth() / (double) (activeShape.getWidth() + activeShape.getHeight());
				normalizerY = - ((double) activeShape.getHeight() / (double) (activeShape.getWidth() + activeShape.getHeight()));
				
				if(activeShape.getHeight() > 10) {
					// Moving up increases height, down decreases height
					activeShape.setY1((int) (activeShape.getY1() - 2*normalizerY));
					activeShape.setY2((int) (activeShape.getY2() + 2*normalizerY));
				}
				if(activeShape.getWidth() > 10) {
					// Moving right increases width
					activeShape.setX1((int) (activeShape.getX1() + 2*normalizerX));
					activeShape.setX2((int) (activeShape.getX2() - 2*normalizerX));
				}
				break;
			case MOVE:
				System.out.println("Move");
				// Mouse-movement since previous calculation
				deltaY = YPos - cursorY;
				deltaX = XPos - cursorX;
	
				// Moving up increases height, down decreases height
				activeShape.setY1((int) (activeShape.getY1() + deltaY));
				activeShape.setY2((int) (activeShape.getY2() + deltaY));
	
				// Moving right increases width
				activeShape.setX1((int) (activeShape.getX1() + deltaX));
				activeShape.setX2((int) (activeShape.getX2() + deltaX));
				break;
			case ROTATERIGHT:
				// do nothing
				break;
			case ROTATELEFT:
				// do nothing
				break;
			default:
				System.out.println("No Tool selected");
				break;
			}
	
			// Update mouse Coords
			cursorY = YPos;
			cursorX = XPos;
			repaint();
		}
	}

	public void cursorMoved(int XPos, int YPos) {
		final int x = XPos;
		final int y = YPos;
		// Only display a hand if the cursor is hovering over the items
		boolean foundobject = false;
		for (int i = shapesList.size() - 1; i >= 0; i--) {
			if (shapesList.get(i).getSprite().intersects(x, y, 5, 5)
					&& foundobject == false) {
				foundobject = true;
			}
		}

		if (foundobject && toolModeIndex != ToolMode.MOVE)
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void keyTyped(char key) {
		System.out.println("Key Typed: " + key);
		switch (key) {
		case '1':
			int randomX = (int) (Math.random() * (this.screenWidth - 200));
			int randomY = (int) (Math.random() * (this.screenHeight - 200));
			Imagedata imagedata = new Imagedata(randomX, randomY, 200 + randomX,
					200 + randomY);
			Image image = importImage();
			imagedata.setImage((BufferedImage) image);
			addShape(imagedata);
			break;
		case '8':
			this.clearShapesList();
			break;
		case '9':
			addLouis();
			break;
		case '0':
			this.setToolMode(ToolMode.MOVE);
			break;
		case '.':
			this.setToolMode(ToolMode.ENLARGE);
			break;
		case '\n':
			setShapeMode(ShapeMode.IMAGE);
			setToolMode(ToolMode.MOVE);
			break;
		default:
			System.out.println("Key not assigned");
			break;
		}

	}

	private void addLouis() {
		int randomX = (int) (Math.random() * (this.screenWidth - 200));
		int randomY = (int) (Math.random() * (this.screenHeight - 200));
		Imagedata imagedata = new Imagedata(randomX, randomY, 200 + randomX,
				200 + randomY);
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/louis.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		imagedata.setImage(image);
		addShape(imagedata);
	}

}
