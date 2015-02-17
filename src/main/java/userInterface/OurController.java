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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import main.java.speechrecognition.Recorder;
import main.java.speechrecognition.ThreadCompleteListener;
import main.java.speechrecognition.Wit;


public class OurController implements MouseMotionListener, MouseListener, ActionListener, ToolBarListener, ThreadCompleteListener {

	private RotationThread rotateThread;
	private UndoManager undoManager;
	public Logger logger;
	public ContentPanel contentPanel;
	private BasicDesign basicDesign;
	private int previousCursorX = -1, previousCursorY = -1, oldXPos, oldYPos, oldWidth, oldHeight;
    private URL url;
    private File normalRecord;
    private Thread thread = null;
	private Thread thread_wit = null;
	private Recorder runnable = null;
    private Wit wit_runnable;
    private Timer timer = new Timer();
    private TimerTask task = new MyTimerTask();
    
    public enum Modality{
    	MOUSE, SPEECH, LEAP
    }
    public Modality currentModality =  Modality.MOUSE;

    @Override
    public void notifyOfThreadComplete(Wit wit) {

		this.contentPanel.setSpeechProcessing(false);

        recognizedText(wit.getWitRawJSONString());

        try {
            recognizedWitResponse(wit);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        recognizedText(wit.getWitRawJSONString());


		this.thread_wit = null;
		this.thread = null;

        if (runnable != null) {
            runnable.finish();
            runnable.terminate();
        }
    }

    // Tool Mode
	public enum ToolMode {
		MOVE, ENLARGE, REDUCE, RESIZE, ROTATE, CUT, SPEECH
	}
	public ToolMode toolModeIndex = ToolMode.MOVE;
    
	public OurController()  {
		super();
		undoManager = new UndoManager();
		rotateThread = new RotationThread(this);
    }
	
	public void initialize(ContentPanel cp, BasicDesign bd) throws URISyntaxException {
		contentPanel = cp;
		basicDesign = bd;
		logger  = new Logger();
        this.url = getClass().getResource("/recording.wav");
        this.normalRecord =  new File(this.url.toURI());
    }

	public void setToolMode(OurController.ToolMode toolMode) {
		this.toolModeIndex = toolMode;
	}


    public void startSpeech() throws URISyntaxException {

        this.contentPanel.setSpeechRecording(true);

        runnable = new Recorder(this.normalRecord);
        thread = new Thread(runnable);

        thread.start();
    }


    public void stopSpeech() {

        SwingUtilities.invokeLater(() -> {

			this.contentPanel.setSpeechRecording(false);
			wit_runnable = null;

			if (this.thread_wit == null) {
				try {
					wit_runnable = new Wit(this.normalRecord, "wav");
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.thread_wit = new Thread(wit_runnable);
				wit_runnable.addListener(this);
				thread_wit.start();
				contentPanel.setSpeechProcessing(true);
			}
		});
    }

    //START CommandInterface
	public void selectPicture(int nr) {
		contentPanel.selectPicture(nr);
	}
	
	public void addPictureByNumber(int nr) {

         ArrayList<Integer> nums = basicDesign.getPictureNums();

        if (nums.contains(nr)) {
            MyImage image = basicDesign.getLibrary()[nr];
            addPicture(image);
        }
    }

    public void deleteSelectedPicture(){
        SwingUtilities.invokeLater(() -> {
            MyImage image = contentPanel.getSelectedPicture();
            if(image != null){
                contentPanel.deleteSelectedPicture();
                undoManager.addEdit(new ActionDelete(image, OurController.this));
                checkUndoRedoButtons();
                addButtonToLibrary(image);
                basicDesign.photoBar.addButton(image);
                basicDesign.repaint();
            }
        });
	}

	public void deletePicture(MyImage img) {


		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				contentPanel.deletePicture(img);
			}
		});



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
		MyImage image = contentPanel.getSelectedPicture();
		if (image != null) {
			int oldX = image.getX(), oldY = image.getY();
			image.setX(x);
			image.setY(y);
			undoManager.addEdit(new ActionMove(image, oldX, oldY, x, y, this));
			checkUndoRedoButtons();
            contentPanel.repaint();
		}
	}

	public void setBackground(Color color) {
		Color oldColor = contentPanel.getBackground();
		contentPanel.setBackground(color);
		undoManager.addEdit(new ActionBackground(oldColor, color, this));
		checkUndoRedoButtons();
        contentPanel.repaint();
	}
	
	public void rotate(double degrees) {
		if (contentPanel.getSelectedPicture() != null) {
			contentPanel.rotate(degrees);
			undoManager.addEdit(new ActionRotate(contentPanel.getSelectedPicture(), (int) degrees, this));
			checkUndoRedoButtons();
            contentPanel.repaint();
		}
	}
	
	public void addRotateAction(double degrees) {
		if (contentPanel.getSelectedPicture() != null) {
			//contentPanel.rotate(degrees);
			undoManager.addEdit(new ActionRotate(contentPanel.getSelectedPicture(), (int) degrees, this));
			checkUndoRedoButtons();
            contentPanel.repaint();
		}
	}
	//END CommandInterface


	//START MouseListener
	
	/* MOUSE LISTENER */

	public void cursorPressed(int XPos, int YPos) {
		contentPanel.requestFocusInWindow();

		previousCursorY = YPos;
		previousCursorX = XPos;
		this.selectPictureAt(XPos, YPos);
		if (contentPanel.getSelectedPicture() != null) {
			
			MyImage selectedImage = contentPanel.getSelectedPicture();

			// Update mouse Coords
			oldXPos = selectedImage.getX();
			oldYPos = selectedImage.getY();
			oldWidth = selectedImage.getWidth();
			oldHeight = selectedImage.getHeight();

		}
	
		switch (toolModeIndex) {
		case MOVE:
		case RESIZE:
		case CUT:
			break;
		case ROTATE:
			Thread thread = new Thread(rotateThread);
			thread.start();
			break;
        case SPEECH:
            try {
				if (!contentPanel.isSpeechProcessing()) {
					this.startSpeech();
				}
			} catch (URISyntaxException e) {
                e.printStackTrace();
            }
            break;
            default:
			break;

		}
	}

	public void cursorReleased(int XPos, int YPos) {
        MyImage selectedImage = contentPanel.getSelectedPicture();

        // Stop voice recording
        if (this.contentPanel.isSpeechRecording()) {
            this.stopSpeech();
        }

        if(!(selectedImage == null)) {

            switch (toolModeIndex) {
                case MOVE:
                	undoManager.addEdit(new ActionMove(selectedImage, oldXPos, oldYPos, selectedImage.getX(), selectedImage.getY(), this));
                	checkUndoRedoButtons();
        			previousCursorX = -1;
        			previousCursorY = -1;
                    break;
                case RESIZE:
                	undoManager.addEdit(new ActionResize(selectedImage, "Resized", oldXPos, oldYPos, selectedImage.getX(), selectedImage.getY(), oldWidth, oldHeight, selectedImage.getWidth(), selectedImage.getHeight(), this));
                	break;
                case ROTATE:
                	rotateThread.keepGoing = false;
                    break;
                case CUT:
                    this.cut(XPos, YPos);
                    break;
                case SPEECH:
                    break;
                default:
                    System.out.println("Tool not found: " + toolModeIndex);
                    break;
            }
            checkUndoRedoButtons();
        }
	}

	public void cursorDragged(int XPos, int YPos) {
		int deltaY = YPos - previousCursorY, deltaX = XPos - previousCursorX;
		MyImage selectedImage = contentPanel.getSelectedPicture();
		if(!(selectedImage==null)) {
			switch (toolModeIndex) {
			case MOVE:
                
                // TODO: See if constraining the image is better
                
				if (selectedImage.contains(new Point(XPos, YPos))) {

                    int xDelimited = selectedImage.getX() + deltaX;
                    int yDelimited = selectedImage.getY() + deltaY;

                    selectedImage.setX(Math.max(0,Math.min(xDelimited, contentPanel.getWidth() - selectedImage.getWidth())));
                    selectedImage.setY(Math.max(0,Math.min(yDelimited, contentPanel.getHeight() - selectedImage.getHeight())));

				}
                
				break;
			case ROTATE:
				// do nothing
				break;
            case CUT:
                 // do nothing
                break;
            case SPEECH:
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
	
	public void mouseDragged(MouseEvent mouseEvent) {
		currentModality = Modality.MOUSE;
		cursorDragged(mouseEvent.getX(), mouseEvent.getY());
	}

	public void mouseMoved(MouseEvent arg0) {
		// Not using this.
	}
	
	public void mouseClicked(MouseEvent mouseEvent) {
	}

	public void mouseEntered(MouseEvent arg0) {
		// Not using this.
	}

	public void mouseExited(MouseEvent arg0) {
		// Not using this.	
	}

	public void mousePressed(MouseEvent e) {
		currentModality = Modality.MOUSE;
		System.out.println("Mouse Pressed");
		MyImage selectedImage = contentPanel.getSelectedPicture();
		//This boolean is to prevent use of the middle mouse button.
		if (toolModeIndex == ToolMode.RESIZE && (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e))) 
		{
			String mode = (SwingUtilities.isLeftMouseButton(e) ? "enlarge" : "reduce");
			((MyTimerTask) task).addVariables(selectedImage, mode,contentPanel.getWidth(),contentPanel.getHeight());
			timer.scheduleAtFixedRate(task, 0, 25);
		}
		else {
			if(SwingUtilities.isLeftMouseButton(e)){
				rotateThread.setClockwise(true);
			}else{
				rotateThread.setClockwise(false);
			}
		
			cursorPressed(e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
		currentModality = Modality.MOUSE;
		if (((MyTimerTask) task).isRunning()) {
			task.cancel();
			timer = new Timer();
		    task = new MyTimerTask();
		}
		cursorReleased(e.getX(), e.getY());
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
    	currentModality = Modality.SPEECH;
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
//				ArrayList<Integer> rotations = response.extractNumbers();
				if (rotations.isEmpty()) {
					this.rotate(90); // Rotate 90 degrees if nothing else is said.
				} else {
					// Only use first number if several rotation degrees have been detected.
					this.rotate(rotations.get(0));
				}

				break;
			case "select":
				ArrayList<Integer> pictureNumbers = response.extractNumbersShifted();
//				ArrayList<Integer> pictureNumbers = response.extractNumbers();

				for (int pic : pictureNumbers) {
					for (MyImage img : basicDesign.getLibrary()) {
						if (img.getNum() == pic) {
							this.addPicture(img);
						} else {
							this.selectPicture(img);
						}
					}
				}

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

            case "move":
                this.movePicture(contentPanel.getLeapRightX(), contentPanel.getLeapRightY());
                break;
			case "remove":
				ArrayList<Integer> pictureNumbersRemove = response.extractNumbersShifted();
//				ArrayList<Integer> pictureNumbersRemove = response.extractNumbers();

				if (pictureNumbersRemove.isEmpty()) {
					this.deleteSelectedPicture();
				} else {
					for (int pic : pictureNumbersRemove) {
						for (MyImage img : basicDesign.getLibrary()) {
							if (img.getNum() == pic) {
								this.deletePicture(img);
							}
						}
					}
				}

                break;
			default:
				System.out.println("The recognized intent is unknown: " + intent);
				break;
		}
		basicDesign.repaint();
	}

	private void undo() {
		undoManager.undo();
		checkUndoRedoButtons();
	}

	private void redo() {
		undoManager.redo();
		checkUndoRedoButtons();
	}

    public void toolbarButtonClicked(String button) {
        switch (button) {
            case "undo":
                this.undo();
                break;
            case "redo":
                this.redo();
                break;
            case "delete":
            	deleteSelectedPicture();
            	break;
            case "select":
            case "move":
            	toolModeIndex = ToolMode.MOVE;
            	break;
            case "cut":
            	toolModeIndex = ToolMode.CUT;
            	break;
            case "resize":
            	toolModeIndex = ToolMode.RESIZE;
            	break;
            case "rotate":
            	toolModeIndex = ToolMode.ROTATE;
            	break;
            default:
            	System.out.println("Default toolbar button clicked??");
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
		basicDesign.repaint();
	}

    public void selectPictureAt(int x, int y){
        contentPanel.selectPictureAt(x, y);
    }
    
    public void addButtonToLibrary(MyImage image){
    	basicDesign.photoBar.addButton(image);
        basicDesign.repaint();
    }
	
    public void removeButtonFromLibrary(MyImage image){
    	basicDesign.photoBar.removeButton(image);
        basicDesign.repaint();
    }
    
	public void addPicture(MyImage image){
        SwingUtilities.invokeLater(() -> {
            contentPanel.addPictureToCurrentPage(image);
            undoManager.addEdit(new ActionAddPic(image, OurController.this));
            checkUndoRedoButtons();
            removeButtonFromLibrary(image);
            selectPicture(image);
        });
	}
	
	private void checkUndoRedoButtons(){
		basicDesign.getToolbar().setEnabledUndoButton(undoManager.canUndo());
		basicDesign.getToolbar().setEnabledRedoButton(undoManager.canRedo());
		basicDesign.repaint();
	}

    public void cut(int x, int y) {
    	SwingUtilities.invokeLater(() -> {
    		if (contentPanel.getLines().size() == 4) {  
    			MyImage image = contentPanel.getSelectedPicture();
    			int oldX = image.getX(), oldY = image.getY();
    			contentPanel.cut();
    			undoManager.addEdit(new ActionCut(contentPanel.getSelectedPicture(), OurController.this, oldX, oldY));
    			contentPanel.setLines(new ArrayList<>());
    			checkUndoRedoButtons();
                contentPanel.repaint();
    		} else {
    			contentPanel.addLine(x, y);
    		}
    	});
    }
}
