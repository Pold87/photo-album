package main.java.userInterface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.speechrecognition.WitResponse;

public class Controller implements CommandInterface, MouseMotionListener, MouseListener, ActionListener, ToolBarListener {
	private ContentPanel contentPanel;
	private BasicDesign basicDesign;
    private Map<Integer, Color> colors = new HashMap<Integer, Color>(); // Used in the WitResponseRecognized. Who made this and what is it for?
	private int oldMouseX = -1, oldMouseY = -1;
	private String currentAction = "";
	
    
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
	}

	public void selectPicture(int x, int y) {
		contentPanel.selectPictureAt(x, y);
	}

	public void deletePicture(int nr) {
		contentPanel.deletePictureFromCurrentPage(nr);		
	}
	
	public void deleteSelectedPicture(){
		contentPanel.deleteSelectedPicture();
	}

	
	public void movePicture(int x, int y) {
		// TODO Auto-generated method stub
		//Should probably communicate with the LEAP guys about this. 
		
	}

	//Also needs edit when we got multiple pages
	public void setBackground(Color color) {
		contentPanel.setBackground(color);
		
	}

	public void rotate(int degrees) {
		contentPanel.rotate(degrees);
		
	}
	//END CommandInterface

	
	//START MouseListeners
	public void mouseDragged(MouseEvent mouseEvent) {
        // Set mouse position, if there is no old Mouse position.
        if (oldMouseX == -1) {
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
            MyImage i = contentPanel.getSelectedPicture();
                if(i != null){
                    i.setX(i.getX() + diffX);
                    i.setY(i.getY() + diffY);
                }

            // Set old mouse position to current position.
            oldMouseX = mouseX;
            oldMouseY = mouseY;
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
			contentPanel.rotate();
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

	public void mouseReleased(MouseEvent arg0) {
    	oldMouseX = -1;
    	oldMouseY = -1;
	}
	//END MouseListeners
	
	//START toolbarListener
	public void recognizedText(String text) {
        basicDesign.getDebugPanel().appendText(text);
    }

    // TODO: definitely improve theses functions, they are just dirty hacks
    // Hmm, I might have moved too much functionality to the controller now.. Not sure
	
    public void recognizedWitResponse(WitResponse response) {
        if (response.getIntent().contains("select")) {
            ArrayList<Integer> entity = response.getEntities();
            for (MyImage i : contentPanel.getImageList()) {
                if (entity.contains(i.getNum())) {
                    i.setSelected(!i.isSelected());
                }
            }
        }
        if (response.getIntent().contains("background")) {
            ArrayList<Integer> entity = response.getEntities();
            if (entity != null) {
                contentPanel.setBackground(colors.get(entity.get(0)));
            }

            }
        basicDesign.repaint();
    }

    
    public void setAction(String action) {
        currentAction = action;
    }
    //END ToolbarListener

    
    //START ActionListener
    //Used Only by the PhotoBar
	public void actionPerformed(ActionEvent e) {
		int picNr = Integer.parseInt(e.getActionCommand());
		MyImage image = basicDesign.getLibrary()[picNr];
        contentPanel.addPictureToCurrentPage(image);
	}
	//END ActionListener


	
}
