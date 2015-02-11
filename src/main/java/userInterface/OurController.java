package main.java.userInterface;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import main.java.speechrecognition.Recorder;
import main.java.speechrecognition.Wit;


public class OurController implements MouseMotionListener, MouseListener, ActionListener, ToolBarListener {

	public Logger logger;
	private ContentPanel contentPanel;
	private BasicDesign basicDesign;
	private int previousCursorX = -1, previousCursorY = -1, oldXPos, oldYPos;
	private String currentAction = "select";
    private ArrayList<Action> performedActions = new ArrayList<>(), undoneActions = new ArrayList<>();
    private MyImage draggingPicture = null;

	// Tool Mode
	public enum ToolMode {
		MOVE, ENLARGE, REDUCE, ROTATE, CUT
	}
	public ToolMode toolModeIndex = ToolMode.MOVE;
    
	public OurController() throws URISyntaxException {
		super();
    }
	
	public void initialize(ContentPanel cp, BasicDesign bd){
		contentPanel = cp;
		basicDesign = bd;
		logger  = new Logger();
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

//	public void recognizeSimpleSpeech() {

//		SpeechResult utterance = this.speechCommands.recognizeCommand();
//		System.out.println(utterance.getHypothesis());
//		System.out.println(utterance.getResult());
//		System.out.println(utterance.getNbest(3));
//		System.out.println(utterance.getLattice());
//		System.out.println(utterance.getWords());
//	}
	
	public void toggleSpeechProcessing() {

		this.contentPanel.setSpeechProcessing(!this.contentPanel.isSpeechProcessing());
		contentPanel.repaint();
	}

    //START CommandInterface
	public void selectPicture(int nr) {
		contentPanel.selectPicture(nr);
	}
	
	public void addPictureByNumber(int nr) {
		System.out.println("Adding picture from this other method");
		MyImage image = basicDesign.getLibrary()[nr];
        addPicture(image);
	}
	
	public void deleteSelectedPicture(){
		MyImage image = contentPanel.getSelectedPicture();
		if(image != null){
			performedActions.add(new ActionDelete(image, this));
			basicDesign.getToolbar().setEnabledUndoButton(true);
			basicDesign.photoBar.addButton(image);
			basicDesign.repaint();
			contentPanel.repaint();
		}
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
//			performedActions.add(new ActionRotate(contentPanel.getSelectedPicture(), degrees, this));


	//START MouseListeners
	public void mouseDragged(MouseEvent mouseEvent) {
		this.toolModeIndex = ToolMode.MOVE;
		cursorDragged(mouseEvent.getX(), mouseEvent.getY());
	}
	
	/* MOUSE LISTENER */

	public void cursorPressed(int XPos, int YPos) {
		System.out.println("Cursor Pressed");
		contentPanel.requestFocusInWindow();

		// Update mouse Coords
		oldXPos = XPos;
		oldYPos = YPos;
		Point p = new Point(XPos, YPos);
		previousCursorY = YPos;
		previousCursorX = XPos;
		
		if (contentPanel.getSelectedPicture() == null) {
			contentPanel.selectPictureAt(XPos, YPos);
		}
		else {
			contentPanel.unselectPicture(p);
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
		double normalizerX, normalizerY;
		MyImage selectedImage = contentPanel.getSelectedPicture();
		if(!(selectedImage==null)) {
			switch (toolModeIndex) {
			case ENLARGE:
				System.out.println("Enlarge");

                // Specify maximum height
                if (selectedImage.getHeight() < 650) {
                    normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
                    normalizerY = -((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));

                    selectedImage.setX((int) (selectedImage.getX() - normalizerX));
                    selectedImage.setY((int) (selectedImage.getY() + normalizerY));

                    selectedImage.resizeImg((int) (selectedImage.getWidth() + 6 * normalizerX), (int) (selectedImage.getHeight() - 6 * normalizerY));
                }
				break;
			case REDUCE:
				System.out.println("Reduce");
				
				normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
				normalizerY = - ((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));

                // Specify minimum height
				if(selectedImage.getHeight() > 120) {
					selectedImage.setY((int) (selectedImage.getY() - 4*normalizerY));
					selectedImage.setX((int) (selectedImage.getX() + 4*normalizerX));
					
					selectedImage.resizeImg((int) (selectedImage.getWidth() - 8*normalizerX), (int) (selectedImage.getHeight() + 8*normalizerY));
				}
				break;
			case MOVE:
				if (selectedImage.contains(new Point(XPos, YPos))) {
					System.out.println("Move");	
					selectedImage.setX(selectedImage.getX() + deltaX);
					selectedImage.setY(selectedImage.getY() + deltaY);
				}
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

	public void mousePressed(MouseEvent e) {
		cursorPressed(e.getX(), e.getY());
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
	 */
    public void recognizedWitResponse(Wit response) throws FileNotFoundException {
		String intent = response.getIntent();
		System.out.println(response.getWitRawJSONString());

		switch (intent) {
			case "add":
				ArrayList<Integer> picturesToAdd = response.extractNumbersShifted(); // Extract all mentioned number
//				ArrayList<Integer> picturesToAdd = response.extractNumbers(); // Extract all mentioned number

				picturesToAdd.forEach(this :: addPictureByNumber); // Could be a Java 1.8 feature
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
        switch (button) {
            case "undo":
                this.undo();
                break;
            case "redo":
                this.redo();
                break;
            default:
                currentAction = button;
                break;
        }
	}
    //END ToolbarListener
    
    //START ActionListener
    //Used Only by the PhotoBar
	public void actionPerformed(ActionEvent e) {
		int picNr = Integer.parseInt(e.getActionCommand());
		MyImage image = basicDesign.getLibrary()[picNr];
        addPicture(image);
	}
	//END ActionListener

	public void selectPicture(MyImage image){
		contentPanel.selectPicture(image);
		contentPanel.repaint();
	}
	
	public void addPicture(MyImage image){
		System.out.println("Adding picture!!!!!!!");
		contentPanel.addPictureToCurrentPage(image);
        performedActions.add(new ActionAddPic(image, this));
        basicDesign.getToolbar().setEnabledUndoButton(true);
        basicDesign.photoBar.removeButton(image);
        basicDesign.repaint();
        contentPanel.repaint();
	}
	
	/*
	 * Kinda nasty hack. I use this to make sure undone actions don't go back in the performedActions list.
	 */
	public void removeLastActionFromList(){
		performedActions.remove(performedActions.size()-1);
	}

}
