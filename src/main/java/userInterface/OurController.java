package main.java.userInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import main.java.gesturerecognition.DrawingPanel.ToolMode;
import main.java.speechrecognition.Recorder;
import main.java.speechrecognition.Wit;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;


public class OurController implements CommandInterface, MouseMotionListener, MouseListener, ActionListener, ToolBarListener {

	private ContentPanel contentPanel;
	private BasicDesign basicDesign;
    //private Map<Integer, Color> colors = new HashMap<Integer, Color>(); // Used in the WitResponseRecognized. Who made this and what is it for?
	private int previousCursorX = -1, previousCursorY = -1, oldXPos, oldYPos;
	private String currentAction = "select";
    private ArrayList<Action> performedActions = new ArrayList<Action>(), undoneActions = new ArrayList<Action>(); // I'm not completely satisfied with the location of these variables, but is saves a load of extra code.
    private MyImage draggingPicture = null;

	// Tool Mode
	public enum ToolMode {
		MOVE, ENLARGE, REDUCE, ROTATE, CUT
	}
	public ToolMode toolModeIndex = ToolMode.MOVE;
    
	public OurController(){
		super();
	}
	
	public void initialize(ContentPanel cp, BasicDesign bd){
		contentPanel = cp;
		basicDesign = bd;
	}

	public void setToolMode(OurController.ToolMode toolMode) {
		this.toolModeIndex = toolMode;
	}
	
	public void recognizeSpeech() throws Exception {
		// Url for recording speech input

		toggleSpeechProcessing();

		// Record wav with external program
		//Record.recordExtern(normalRecord);

		URL url = getClass().getResource("/recording.wav");
		File normalRecord = new File(url.toURI());
		Recorder recorder = new Recorder(normalRecord);

		Thread runnable = new Thread(recorder);

		runnable.start();

		Thread.sleep(5000);

		recorder.finish();

		Wit wit = new Wit(normalRecord, "wav");

		// Send recognized
		recognizedText(wit.getWitRawJSONString());
		recognizedWitResponse(wit);
		toggleSpeechProcessing();
//		Process p2 = new ProcessBuilder("killall", "xflux").start();
	}

	public void toggleSpeechProcessing() {

		this.contentPanel.setSpeechProcessing(!this.contentPanel.isSpeechProcessing());
		contentPanel.repaint();
	}

    //START CommandInterface
	public void selectPicture(int nr) {
		contentPanel.selectPicture(nr);
	}
	
	public void addPictureFromLibrary(int nr) {
		MyImage image = basicDesign.getLibrary()[nr];
        contentPanel.addPictureToCurrentPage(image);
        performedActions.add(new ActionAddPic(image, this));
		contentPanel.repaint();
	}

	public void selectPicture(int x, int y) {
		contentPanel.selectPictureAt(x, y);
	}

	public void deletePicture(int nr) {
		MyImage image = contentPanel.deletePictureFromCurrentPage(nr);
		performedActions.add(new ActionDelete(image, this));
		basicDesign.getToolbar().setEnabledUndoButton(true);
		contentPanel.repaint();
	}
	
	public void deleteSelectedPicture(){
		MyImage image = contentPanel.deleteSelectedPicture();
		performedActions.add(new ActionDelete(image, this));
		basicDesign.getToolbar().setEnabledUndoButton(true);
		contentPanel.repaint();
	}

	private void darker(){
		Color oldColor = contentPanel.getBackground();
		this.setBackground(oldColor.darker());
	}

	private void brighter(){
		Color oldColor = contentPanel.getBackground();
		this.setBackground(oldColor.brighter());
	}
	
	public void movePicture(int x, int y) {
		//Should probably communicate with the LEAP guys about this. 
		MyImage image = contentPanel.getSelectedPicture();
		if (image != null) {
			int oldX = image.getX(), oldY = image.getY();
			image.setX(x);
			image.setY(y);
			performedActions.add(new ActionMove(image, oldX, oldY, x, y, this));
			basicDesign.getToolbar().setEnabledUndoButton(true);
			contentPanel.repaint();
		}
	}

	public void setBackground(Color color) {
		Color oldColor = contentPanel.getBackground();
		contentPanel.setBackground(color);
		performedActions.add(new ActionBackground(oldColor, color, this));
		basicDesign.getToolbar().setEnabledUndoButton(true);
	}

	public void rotate(double degrees) {
		if (contentPanel.getSelectedPicture() != null) {
			contentPanel.rotate(degrees);
			//performedActions.add(new ActionRotate(contentPanel.getSelectedPicture(), (int) degrees, this));
			//basicDesign.getToolbar().setEnabledUndoButton(true);
		}

	}
	
	public void addRotateAction(double degrees) {
		if (contentPanel.getSelectedPicture() != null) {
			//contentPanel.rotate(degrees);
			System.out.println("actionrotate added");
			performedActions.add(new ActionRotate(contentPanel.getSelectedPicture(), (int) degrees, this));
			basicDesign.getToolbar().setEnabledUndoButton(true);
		}
	}
	//END CommandInterface

	
	//START MouseListeners
	public void mouseDragged(MouseEvent mouseEvent) {
		cursorDragged(mouseEvent.getX(), mouseEvent.getY());//fingerDragged(mouseEvent.getX(), mouseEvent.getY());
	}
	
	//public void fingerMoved(int x, int y){
	//	contentPanel.setLeapRightX(x);
	//	contentPanel.setLeapRightY(y);
	//}
	
	//Currently not used, but we're going to.
	public void fingerPressed(int XPos, int YPos) {
		contentPanel.requestFocusInWindow();

		// Update Finger Coords
		contentPanel.setLeapRightX(XPos);
		contentPanel.setLeapRightY(YPos);
		contentPanel.selectPictureAt(XPos, YPos);
	}
	
	public void fingerReleased(int XPos, int YPos) {
		switch (toolModeIndex) {
			case MOVE:
			case ENLARGE:
			case REDUCE:
			case ROTATE:
			case CUT:
				if(draggingPicture != null){
					performedActions.add(new ActionMove(draggingPicture, oldXPos, oldYPos, draggingPicture.getX(), draggingPicture.getY(), this));			
					draggingPicture = null;
					previousCursorX = -1;
					previousCursorY = -1;
				}
				break;
			default:
				System.out.println("Tool not found: " + toolModeIndex);
		}
	}
	
	
	/* MOUSE LISTENER */

	public void cursorPressed(int XPos, int YPos) {
		System.out.println("Cursor Pressed");
		contentPanel.requestFocusInWindow();
		MyImage selectedImage = contentPanel.getSelectedPicture();
		// Update mouse Coords
		oldXPos = XPos;
		oldYPos = YPos;
		
		previousCursorY = YPos;
		previousCursorX = XPos;
		ArrayList<MyImage> shapesList = contentPanel.getImageList();
		// Select active shape or image
		for (int i = 0; i < shapesList.size(); i++) {
			if (shapesList.get(i).contains(new Point(XPos, YPos))) {//.intersects(XPos, YPos, 5, 5)) {
				selectedImage = shapesList.get(i);
				System.out.println("New active shape: " + selectedImage);
			}
		}
		
		switch (toolModeIndex) {
		case MOVE:
		case ENLARGE:
		case REDUCE:
		case CUT:		
		case ROTATE:
			contentPanel.repaint();
			break;
		default:
			break;

		}
	}

	public void cursorReleased(int XPos, int YPos) {
		System.out.println("Cursor Released");
		MyImage selectedImage = contentPanel.getSelectedPicture();
		switch (toolModeIndex) {
		case MOVE:
		case ENLARGE:
		case REDUCE:
		case ROTATE:
		case CUT:
			performedActions.add(new ActionMove(selectedImage, oldXPos, oldYPos, XPos, YPos, this));
			basicDesign.getToolbar().setEnabledUndoButton(true);
			if (XPos < 0 || XPos > contentPanel.getWidth() || YPos < 0
					|| YPos > contentPanel.getHeight())
				contentPanel.deleteSelectedPicture();
			break;
		default:
			System.out.println("Tool not found: " + toolModeIndex);
			break;
		}
	}

	public void cursorDragged(int XPos, int YPos) {
		int deltaY = YPos - previousCursorY, deltaX = XPos - previousCursorX;
		//double deltaY, deltaX;
		double normalizerX, normalizerY;
		BufferedImage temp;
		MyImage selectedImage = contentPanel.getSelectedPicture();
		if(!(selectedImage==null)) {
			switch (toolModeIndex) {
			case ENLARGE:
				System.out.println("Enlarge");

				normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
				normalizerY = - ((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));
				
				selectedImage.setX((int) (selectedImage.getX() - normalizerX));
				selectedImage.setY((int) (selectedImage.getY() + normalizerY));
				
				selectedImage.resizeImg((int) (selectedImage.getWidth() + 6*normalizerX), (int) (selectedImage.getHeight() - 6*normalizerY));
				break;
			case REDUCE:
				System.out.println("Reduce");
				
				normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
				normalizerY = - ((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));
				
				if(selectedImage.getHeight() > 10 && selectedImage.getWidth() > 10) {
					selectedImage.setY((int) (selectedImage.getY() - 4*normalizerY));
					selectedImage.setX((int) (selectedImage.getX() + 4*normalizerX));
					
					selectedImage.resizeImg((int) (selectedImage.getWidth() - 8*normalizerX), (int) (selectedImage.getHeight() + 8*normalizerY));
				}
				break;
			case MOVE:
				System.out.println("Move");	
				selectedImage.setX((int) (selectedImage.getX() + deltaX));
				selectedImage.setY((int) (selectedImage.getY() + deltaY));
				break;
			case ROTATE:
				// do nothing
				break;
			default:
				System.out.println("No Tool selected");
				break;
			}
	
			// Update mouse Coords
			previousCursorY = YPos;
			previousCursorX = XPos;
			contentPanel.repaint();
		}
	}

	/*public void cursorMoved(int XPos, int YPos) {
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
	}*/
	
	
	

	/*public void cursorDragged(int XPos, int YPos) {
		int deltaY =YPos - previousCursorY, deltaX = XPos - previousCursorX;
		double normalizerX, normalizerY;
		MyImage selectedImage = contentPanel.getSelectedPicture();
		if(selectedImage != null) {
		       if (previousCursorX == -1) {
		            previousCursorX = XPos;
		            previousCursorY = YPos;
		            oldXPos = selectedImage.getX();
		            oldYPos = selectedImage.getY();
		        } else {
			switch (toolModeIndex) {
				case ENLARGE:
					System.out.println("Enlarge");

					normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
					normalizerY = - ((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));
					
					//TODO Fix this function. In our current implementation of MyImage, it doesn't work.
					/*
					// Moving up increases height, down decreases height
					selectedImage.setY((int) (selectedImage.getY() + normalizerY));
					selectedImage.setWidth((int) (selectedImage.getY2() - 2*normalizerY));

					// Moving right increases width
					selectedImage.setX1((int) (selectedImage.getX1() - normalizerX));
					selectedImage.setX2((int) (selectedImage.getX2() + 2*normalizerX));
					*/
	/*
					break;
				case REDUCE:
					System.out.println("Reduce");
					// Mouse movement since previous calculation

					normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
					normalizerY = - ((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));

					//TODO: Same goes here as for Enlarge.
					/*
					if(selectedImage.getHeight() > 10) {
						// Moving up increases height, down decreases height
						selectedImage.setY1((int) (selectedImage.getY1() - 2*normalizerY));
						selectedImage.setY2((int) (selectedImage.getY2() + 2*normalizerY));
					}
					if(selectedImage.getWidth() > 10) {
						// Moving right increases width
						selectedImage.setX1((int) (selectedImage.getX1() + 2*normalizerX));
						selectedImage.setX2((int) (selectedImage.getX2() - 2*normalizerX));
					}
					*/
	/*
					break;
				case MOVE:
					System.out.println("Move");
					selectedImage.setX(selectedImage.getX() + deltaX);
					selectedImage.setY(selectedImage.getY() + deltaY);

					break;
				case ROTATE:
					// do nothing
					break;
				default:
					System.out.println("No Tool selected");
					break;
			}

			// Update mouse Coords
			previousCursorY = YPos;
			previousCursorX = XPos;
			contentPanel.repaint();
		}
		}
	}
	*/
	
	public void fingerDragged(int x, int y) {
		System.out.println("Finger Dragged");
		draggingPicture = contentPanel.getSelectedPicture();
		
		if(draggingPicture != null)
        // Set mouse position, if there is no old Mouse position.
        if (previousCursorX == -1) {
            previousCursorX = x;
            previousCursorY = y;
            oldXPos = draggingPicture.getX();
            oldYPos = draggingPicture.getY();
        } else {
            // Get current mouse position
        	
            // Get difference between old mouse position and current position
            Integer diffX = x - previousCursorX;
            Integer diffY = y - previousCursorY;

            // Update position for every image in the image list.
            draggingPicture.setX(draggingPicture.getX() + diffX);
            draggingPicture.setY(draggingPicture.getY() + diffY);

            // Set old mouse position to current position.
            previousCursorX = x;
            previousCursorY = y;
        }

        // Repaint everything in order to see changes.
        contentPanel.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
		// Not using this.
	}
	
	public void mouseClicked(MouseEvent mouseEvent) {
		if(currentAction.equals("select")|| currentAction.equals("move")){ //Why do we have a separate move button anyway?
			contentPanel.selectPictureAt(mouseEvent.getX(), mouseEvent.getY());
		}else if(currentAction.equals("rotate")){
			rotate(45);
		}

	}

	public void mouseEntered(MouseEvent arg0) {
		// Not using this.
	}

	public void mouseExited(MouseEvent arg0) {
		// Not using this.	
	}

	public void mousePressed(MouseEvent arg0) {
		// Not using this.	
	}

	public void mouseReleased(MouseEvent e) {
		if(draggingPicture != null){
			performedActions.add(new ActionMove(draggingPicture, oldXPos, oldYPos, draggingPicture.getX(), draggingPicture.getY(), this));			
			draggingPicture = null;
			previousCursorX = -1;
			previousCursorY = -1;
		}
	}
	//END MouseListeners
	
	//START toolbarListener
	public void recognizedText(String text) {
        basicDesign.getDebugPanel().appendText(text);
    }

	/**
	 * This function determines what action should be taken after a response from wit.ai is received.
	 * @param response
	 */
    public void recognizedWitResponse(Wit response) throws FileNotFoundException {
		String intent = response.getIntent();
		System.out.println(response.getWitRawJSONString());

		switch (intent) {
			case "add":
				ArrayList<Integer> picturesToAdd = response.extractNumbersShifted(); // Extract all mentioned number
//				ArrayList<Integer> picturesToAdd = response.extractNumbers(); // Extract all mentioned number

				picturesToAdd.forEach(this :: addPictureFromLibrary); // Could be a Java 1.8 feature
				picturesToAdd.forEach(this :: selectPicture);
				break;
			case "rotate":
				ArrayList<Integer> rotations = response.extractNumbersShifted();
				if (rotations.isEmpty()) {
					this.rotate(90); // Rotate 90 degrees if nothing else is said.
				} else {
					// Only use first number if several rotation degrees have been detected.
					this.rotate(rotations.get(0));
				}

				break;
			case "select":
				ArrayList<Integer> pictureNumbers = response.extractNumbersShifted();
				System.out.println(pictureNumbers.toString());
				pictureNumbers.forEach(this :: selectPicture);
				break;
			case "background":
				Color color = response.getBackgroundColor();
				if (color != null) {
					contentPanel.setBackground(color);
				} else {
					System.out.println("Unknown color: ");
				}
				break;
			case "undo":
				this.undo();
				break;
			case "redo":
				this.redo();
				break;
			case "darker":
				this.darker();
				break;
			case "brighter":
				this.brighter();
				break;
			case "exit":
				System.exit(0);
				break;
			default:
				System.out.println("The recognized intent is unknown: " + intent);
				break;
		}
		basicDesign.repaint();
		}

	private void undo() {
		Action action = performedActions.remove(performedActions.size() - 1);
		action.undo();
		undoneActions.add(action);
		basicDesign.getToolbar().setEnabledRedoButton(true);
		if (performedActions.isEmpty()) {
			basicDesign.getToolbar().setEnabledUndoButton(false);
		}
	}

	private void redo() {
		Action action = undoneActions.remove(undoneActions.size() - 1);
		action.redo();
		basicDesign.getToolbar().setEnabledUndoButton(true);
		if (undoneActions.isEmpty()) {
			basicDesign.getToolbar().setEnabledRedoButton(false);
		}
	}

    public void toolbarButtonClicked(String button) {
		if (button == "undo") {
			this.undo();
		} else if (button == "redo") {
			this.redo();
		} else {
			currentAction = button;
		}
	}
    //END ToolbarListener

    
    //START ActionListener
    //Used Only by the PhotoBar
	public void actionPerformed(ActionEvent e) {
		int picNr = Integer.parseInt(e.getActionCommand());
		MyImage image = basicDesign.getLibrary()[picNr];
        contentPanel.addPictureToCurrentPage(image);
        performedActions.add(new ActionAddPic(image, this));
        basicDesign.getToolbar().setEnabledUndoButton(true);
        contentPanel.repaint();
	}
	//END ActionListener

	public void selectPicture(MyImage image){
		contentPanel.selectPicture(image);
		contentPanel.repaint();
	}
	
	public void addPictureToCurrentPage(MyImage image){
		contentPanel.addPictureToCurrentPage(image);
        performedActions.add(new ActionAddPic(image, this));
        basicDesign.getToolbar().setEnabledUndoButton(true);
        contentPanel.repaint();
	}
	
	/*
	 * Kinda nasty hack. I use this to make sure undone actions don't go back in the performedActions list.
	 */
	public void removeLastActionFromList(){
		performedActions.remove(performedActions.size()-1);
	}

	public ContentPanel getContentPanel() {
		return contentPanel;
	}


	// from
	// http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
	public int calcLevenshteinDistance (String s0, String s1) {
		int len0 = s0.length() + 1;
		int len1 = s1.length() + 1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for (int i = 0; i < len0; i++) cost[i] = i;

		// dynamicaly computing the array of distances

		// transformation cost for each letter in s1
		for (int j = 1; j < len1; j++) {
			// initial cost of skipping prefix in String s1
			newcost[0] = j;

			// transformation cost for each letter in s0
			for(int i = 1; i < len0; i++) {
				// matching current letters in both strings
				int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

				// computing cost for each transformation
				int cost_replace = cost[i - 1] + match;
				int cost_insert  = cost[i] + 1;
				int cost_delete  = newcost[i - 1] + 1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
			}

			// swap cost/newcost arrays
			int[] swap = cost; cost = newcost; newcost = swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0 - 1];
	}


}
