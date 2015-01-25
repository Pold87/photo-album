package main.java.userInterface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import main.java.speechrecognition.Wit;


public class OurController implements CommandInterface, MouseMotionListener, MouseListener, ActionListener, ToolBarListener {

	private ContentPanel contentPanel;
	private BasicDesign basicDesign;
    //private Map<Integer, Color> colors = new HashMap<Integer, Color>(); // Used in the WitResponseRecognized. Who made this and what is it for?
	private int previousMouseX = -1, previousMouseY = -1, oldXPos, oldYPos;
	private String currentAction = "select";
    private ArrayList<Action> performedActions = new ArrayList<Action>(), undoneActions = new ArrayList<Action>(); // I'm not completely satisfied with the location of these variables, but is saves a load of extra code.
    private MyImage draggingPicture = null;
	
    
	public OurController(){
		super();
	}
	
	public void initialize(ContentPanel cp, BasicDesign bd){
		contentPanel = cp;
		basicDesign = bd;
	}
    
    //START CommandInterface
	public void selectPicture(int nr) {
		contentPanel.selectPicture(nr);
	}

	public void nextPage() {
		// TODO Auto-generated method stub

	}

	public void previousPage() {
		// TODO Auto-generated method stub

	}

	public void selectSpecificPage(int nr) {
		// TODO Auto-generated method stub

	}

	public void insertPages() {
		// TODO Auto-generated method stub

	}

	public void removeCurrentPages() {
		// TODO Auto-generated method stub

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
		int oldX = image.getX(), oldY = image.getY();
		image.setX(x);
		image.setY(y);
		
		performedActions.add(new ActionMove(image, oldX, oldY, x, y, this));
		basicDesign.getToolbar().setEnabledUndoButton(true);
		contentPanel.repaint();
	}

	//Also needs edit when we got multiple pages
	public void setBackground(Color color) {
		Color oldColor = contentPanel.getBackground();
		contentPanel.setBackground(color);
		performedActions.add(new ActionBackground(oldColor, color, this));
		basicDesign.getToolbar().setEnabledUndoButton(true);
	}

	public void rotate(int degrees) {
		contentPanel.rotate(degrees);
		performedActions.add(new ActionRotate(contentPanel.getSelectedPicture(), degrees, this));
		basicDesign.getToolbar().setEnabledUndoButton(true);
	}
	//END CommandInterface

	
	//START MouseListeners
	public void mouseDragged(MouseEvent mouseEvent) {
		draggingPicture = contentPanel.getSelectedPicture();
		
		if(draggingPicture != null)
        // Set mouse position, if there is no old Mouse position.
        if (previousMouseX == -1) {
            previousMouseX = mouseEvent.getX();
            previousMouseY = mouseEvent.getY();
            oldXPos = draggingPicture.getX();
            oldYPos = draggingPicture.getY();
        } else {
            // Get current mouse position
            int mouseX = mouseEvent.getX();
            int mouseY = mouseEvent.getY();

            // Get difference between old mouse position and current position
            Integer diffX = mouseX - previousMouseX;
            Integer diffY = mouseY - previousMouseY;

            // Update position for every image in the image list.
            draggingPicture.setX(draggingPicture.getX() + diffX);
            draggingPicture.setY(draggingPicture.getY() + diffY);

            // Set old mouse position to current position.
            previousMouseX = mouseX;
            previousMouseY = mouseY;
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
			previousMouseX = -1;
			previousMouseY = -1;
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
    public void recognizedWitResponse(Wit response) {
		String intent = response.getIntent();
		System.out.println(response.getWitRawJSONString());
		switch (intent) {
			case "add":
				ArrayList<Integer> picturesToAdd = response.extractNumbersShifted(); // Extract all mentioned number
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
					System.out.println("Unknown color: " + color.toString());
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
			default:
				System.out.println("The recognized intent is unknown: " + intent);
				System.out.println("But I'm also in default");
				break;
		}

		// TODO: See if this is really necessary
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
}
