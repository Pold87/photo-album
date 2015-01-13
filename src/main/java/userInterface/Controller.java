package main.java.userInterface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import main.java.speechrecognition.Wit;


public class Controller implements CommandInterface, MouseMotionListener, MouseListener, ActionListener, ToolBarListener {
	private ContentPanel contentPanel;
	private BasicDesign basicDesign;
    //private Map<Integer, Color> colors = new HashMap<Integer, Color>(); // Used in the WitResponseRecognized. Who made this and what is it for?
	private int previousMouseX = -1, previousMouseY = -1, oldXPos, oldYPos;
	private String currentAction = "select";
    private ArrayList<Action> performedActions = new ArrayList<Action>(), undoneActions = new ArrayList<Action>(); // I'm not completely satisfied with the location of these variables, but is saves a load of extra code.
    private MyImage draggingPicture = null;
	
    
	public Controller(){
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

    // TODO: definitely improve theses functions, they are just dirty hacks
    // Hmm, I might have moved too much functionality to the controller now.. Not sure
	
    public void recognizedWitResponse(Wit response) {
		String intent = response.getIntent();
		System.out.println(response.getWitRawJSONString());
		switch (intent) {
			case "select":
				ArrayList<Integer> pictureNumbers = response.getPictureNumbers();
				for (MyImage i : contentPanel.getImageList()) {
					if (pictureNumbers.contains(i.getNum())) {
						i.setSelected(!i.isSelected());
					}
				}
				break;
			case "background":
				Color color = response.getBackgroundColor();
				if (color != null) {
					contentPanel.setBackground(color);
				} else {
					System.out.println("Unknown color: " + color.toString());
				}
				break;
			default:
				System.out.println("The recognized intent is unknown: " + intent);
				System.out.println("But I'm also in default");
				break;
		}

		// TODO: See if this is really necessary
		basicDesign.repaint();

		}


    
    public void toolbarButtonClicked(String button) {
    	if(button == "undo"){
    		Action action = performedActions.remove(performedActions.size()-1);
    		action.undo();
    		undoneActions.add(action);
    		basicDesign.getToolbar().setEnabledRedoButton(true);
    		if(performedActions.isEmpty()){
    			basicDesign.getToolbar().setEnabledUndoButton(false);
    		}
    	}else if(button == "redo"){
    		Action action = undoneActions.remove(undoneActions.size()-1);
    		action.redo();
    		basicDesign.getToolbar().setEnabledUndoButton(true);
    		if(undoneActions.isEmpty()){
    			basicDesign.getToolbar().setEnabledRedoButton(false);
    		}
    	}else{
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
	
}
