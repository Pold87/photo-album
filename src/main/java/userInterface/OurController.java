package main.java.userInterface;

import java.awt.*;
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

	private UndoManager undoManager;
	public Logger logger;
	public ContentPanel contentPanel;
	private BasicDesign basicDesign;
	private int previousCursorX = -1, previousCursorY = -1, oldXPos, oldYPos, oldWidth, oldHeight;
	private String currentAction = "select";
    private URL url;
    private File normalRecord;
    private Thread thread = null;
	private Thread thread_wit = null;
	private Recorder runnable = null;
    private Wit wit_runnable;
    private Timer timer = new Timer();
    private TimerTask task = new MyTimerTask();

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
		MOVE, ENLARGE, REDUCE, ROTATE, CUT,  SPEECH
	}
	public ToolMode toolModeIndex = ToolMode.MOVE;
    
	public OurController()  {
		super();
	    ((MyTimerTask) task).add(this);
		undoManager = new UndoManager();
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

	
	public void recognizeSpeech() throws Exception {
		// Url for recording speech input

        toggleSpeechRecording();

		// Record wav with external program
		//Record.recordExtern(normalRecord);

		URL url = getClass().getResource("/recording.wav");
		File normalRecord = new File(url.toURI());
		Recorder recorder = new Recorder(normalRecord);
		Thread runnable = new Thread(recorder);


        // TODO: runnable start and stop should be invoked by cursor pressed and released
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

    // TODO
//	public void recognizeSimpleSpeech() {

//		SpeechResult utterance = this.speechCommands.recognizeCommand();
//		System.out.println(utterance.getHypothesis());
//		System.out.println(utterance.getResult());
//		System.out.println(utterance.getNbest(3));
//		System.out.println(utterance.getLattice());
//		System.out.println(utterance.getWords());
//	}
	
	public void toggleSpeechRecording() {

		this.contentPanel.setSpeechRecording(!this.contentPanel.isSpeechRecording());
		contentPanel.repaint();
	}

	public void toggleSpeechProcessing() {

		this.contentPanel.setSpeechProcessing(!this.contentPanel.isSpeechProcessing());
		contentPanel.repaint();
	}

    //START CommandInterface
	public void selectPicture(int nr) {
		contentPanel.selectPicture(nr);
	}
	
	public void addPictureByNumber(int nr) {
		if (nr < basicDesign.getLibrary().length) {
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
                basicDesign.getToolbar().setEnabledUndoButton(true);
                basicDesign.getToolbar().setEnabledRedoButton(false);
                addButtonToLibrary(image);
                basicDesign.photoBar.addButton(image);
				basicDesign.photoBar.repaint();
                basicDesign.repaint();
                contentPanel.repaint();
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
			basicDesign.getToolbar().setEnabledUndoButton(true);
			basicDesign.getToolbar().setEnabledRedoButton(false);
			basicDesign.repaint();
            contentPanel.repaint();
		}
	}

	public void setBackground(Color color) {
		Color oldColor = contentPanel.getBackground();
		contentPanel.setBackground(color);
		undoManager.addEdit(new ActionBackground(oldColor, color, this));
		basicDesign.getToolbar().setEnabledRedoButton(false);
        basicDesign.getToolbar().setEnabledUndoButton(true);
        basicDesign.repaint();
        contentPanel.repaint();
	}
	
	public void rotate(double degrees) {
		if (contentPanel.getSelectedPicture() != null) {
			contentPanel.rotate(degrees);
			undoManager.addEdit(new ActionRotate(contentPanel.getSelectedPicture(), (int) degrees, this));
			basicDesign.getToolbar().setEnabledUndoButton(true);
			basicDesign.getToolbar().setEnabledRedoButton(false);
            basicDesign.repaint();
            contentPanel.repaint();
		}
	}
	
	public void addRotateAction(double degrees) {
		if (contentPanel.getSelectedPicture() != null) {
			//contentPanel.rotate(degrees);
			undoManager.addEdit(new ActionRotate(contentPanel.getSelectedPicture(), (int) degrees, this));
			basicDesign.getToolbar().setEnabledUndoButton(true);
			basicDesign.getToolbar().setEnabledRedoButton(false);
            basicDesign.repaint();
            contentPanel.repaint();
		}
	}
	//END CommandInterface
//			performedActions.add(new ActionRotate(contentPanel.getSelectedPicture(), degrees, this));


	//START MouseListeners
	public void mouseDragged(MouseEvent mouseEvent) {
		System.out.println("mousedragged");
		this.toolModeIndex = ToolMode.MOVE;
		cursorDragged(mouseEvent.getX(), mouseEvent.getY());
	}
	
	/* MOUSE LISTENER */

	public void cursorPressed(int XPos, int YPos) {
		contentPanel.requestFocusInWindow();

		//Point p = new Point(XPos, YPos);
		previousCursorY = YPos;
		previousCursorX = XPos;
		
		if (contentPanel.getSelectedPicture() != null) {
		this.selectPictureAt(XPos, YPos);
		MyImage selectedImage = contentPanel.getSelectedPicture();
		
		// Update mouse Coords
		oldXPos = selectedImage.getX();
		oldYPos = selectedImage.getY();
		oldWidth = selectedImage.getWidth();
		oldHeight = selectedImage.getHeight();
		
		}
		//else {
		//	contentPanel.unselectPicture(p);
		//}
	
		switch (toolModeIndex) {
		case MOVE:
		case ENLARGE:
		case REDUCE:
		case CUT:		
		case ROTATE:
			contentPanel.repaint();
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
        //int deltaY = YPos - previousCursorY, deltaX = XPos - previousCursorX;
        MyImage selectedImage = contentPanel.getSelectedPicture();

        // Stop voice recording
        if (this.contentPanel.isSpeechRecording()) {
            this.stopSpeech();
        }

        if(!(selectedImage == null)) {

            switch (toolModeIndex) {
                case MOVE:
                	undoManager.addEdit(new ActionMove(selectedImage, oldXPos, oldYPos, selectedImage.getX(), selectedImage.getY(), this));
                	basicDesign.getToolbar().setEnabledUndoButton(true);
        			basicDesign.getToolbar().setEnabledRedoButton(false);
                    basicDesign.repaint();
                    contentPanel.repaint();
        			previousCursorX = -1;
        			previousCursorY = -1;
                    break;
                case ENLARGE:
                	undoManager.addEdit(new ActionResize(selectedImage, "Enlarged", oldXPos, oldYPos, selectedImage.getX(), selectedImage.getY(), oldWidth, oldHeight, selectedImage.getWidth(), selectedImage.getHeight(), this));
                   	basicDesign.getToolbar().setEnabledUndoButton(true);
            		basicDesign.getToolbar().setEnabledRedoButton(false);
                    basicDesign.repaint();
                    contentPanel.repaint();
                	break;
                case REDUCE:
                	undoManager.addEdit(new ActionResize(selectedImage, "Reduced", oldXPos, oldYPos, selectedImage.getX(), selectedImage.getY(), oldWidth, oldHeight, selectedImage.getWidth(), selectedImage.getHeight(), this));
                	basicDesign.getToolbar().setEnabledUndoButton(true);
            		basicDesign.getToolbar().setEnabledRedoButton(false);
                    basicDesign.repaint();
                    contentPanel.repaint();
                	break;
                case ROTATE:
                    break;
                case CUT:
                    this.cut(contentPanel.getLeapRightX(), contentPanel.getLeapRightY());
                    basicDesign.getToolbar().setEnabledUndoButton(true);
            		basicDesign.getToolbar().setEnabledRedoButton(false);
                    basicDesign.repaint();
                    contentPanel.repaint();
                    break;
                case SPEECH:
                    break;
                default:
                    System.out.println("Tool not found: " + toolModeIndex);
                    break;
            }
        }
	}

	public void cursorDragged(int XPos, int YPos) {
		int deltaY = YPos - previousCursorY, deltaX = XPos - previousCursorX;
		double normalizerX, normalizerY;
		MyImage selectedImage = contentPanel.getSelectedPicture();
		if(!(selectedImage==null)) {
			switch (toolModeIndex) {
			case ENLARGE:
				// Specify maximum height of image
                if (selectedImage.getHeight() < 650) {
                    normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
                    normalizerY = -((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));

                    selectedImage.setX((int) (selectedImage.getX() - normalizerX));
                    selectedImage.setY((int) (selectedImage.getY() + normalizerY));

                    selectedImage.resizeImg((int) (selectedImage.getWidth() + 6 * normalizerX), (int) (selectedImage.getHeight() - 6 * normalizerY));
                }
				break;
			case REDUCE:
				normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
				normalizerY = - ((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));

                // Specify minimum height
				if(selectedImage.getHeight() > 120) {
					selectedImage.setY((int) (selectedImage.getY() - 4*normalizerY));
					selectedImage.setX((int) (selectedImage.getX() + 4*normalizerX));
					
					selectedImage.resizeImg((int) (selectedImage.getWidth() - 8 * normalizerX), (int) (selectedImage.getHeight() + 8*normalizerY));
				}
				break;
			case MOVE:
                
                // TODO: See if constraining the image is better
                
				if (selectedImage.contains(new Point(XPos, YPos))) {
//					selectedImage.setX(selectedImage.getX() + deltaX);
//					selectedImage.setY(selectedImage.getY() + deltaY);

                    int xDelimited = selectedImage.getX() + deltaX;
                    int yDelimited = selectedImage.getY() + deltaY;

                    selectedImage.setX(Math.max(0,Math.min(xDelimited, basicDesign.getScr_height() + selectedImage.getWidth())));
                    selectedImage.setY(Math.max(0,Math.min(yDelimited, basicDesign.getScr_width()) + selectedImage.getHeight()));
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

	public void mouseMoved(MouseEvent arg0) {
		// Not using this.
	}
	
	public void mouseClicked(MouseEvent mouseEvent) {
        switch (currentAction) {
            case "select":
                contentPanel.selectPictureAt(mouseEvent.getX(), mouseEvent.getY());
                break;
            case "rotate":
                rotate(45);
                break;
            case "cut":
                this.cut(mouseEvent.getX(), mouseEvent.getY());
                break;
            default:
                break;
        }

	}

	public void mouseEntered(MouseEvent arg0) {
		// Not using this.
	}

	public void mouseExited(MouseEvent arg0) {
		// Not using this.	
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("mouse pressed");
		MyImage selectedImage = contentPanel.getSelectedPicture();
		if (currentAction == "resize") {
			if (SwingUtilities.isLeftMouseButton(e)) {
				currentAction = "enlarge";
				toolModeIndex = ToolMode.ENLARGE;
				((MyTimerTask) task).addVariables(selectedImage, "enlarge");
				timer.scheduleAtFixedRate(task, 0, 25);
			}
			else if (SwingUtilities.isRightMouseButton(e)) {
				currentAction = "reduce";
				toolModeIndex = ToolMode.REDUCE;
				((MyTimerTask) task).addVariables(selectedImage, "reduce");
				timer.scheduleAtFixedRate(task, 0, 25);
			}
		}
		else {
			cursorPressed(e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
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
		undoManager.undo();
		basicDesign.getToolbar().setEnabledRedoButton(true);
		if (!undoManager.canUndo()) {
			basicDesign.getToolbar().setEnabledUndoButton(false);
		}
		basicDesign.repaint();
	}

	private void redo() {
		undoManager.redo();
		basicDesign.getToolbar().setEnabledUndoButton(true);
		if (!undoManager.canRedo()) {
			basicDesign.getToolbar().setEnabledRedoButton(false);
		}
		basicDesign.repaint();
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
            basicDesign.getToolbar().setEnabledUndoButton(true);
    		basicDesign.getToolbar().setEnabledRedoButton(false);
            removeButtonFromLibrary(image);
            selectPicture(image);
        });


	}

    public void cut(int x, int y) {
    	if (contentPanel.getLines().size() == 4) {
    		SwingUtilities.invokeLater(() -> {
    			undoManager.addEdit(new ActionCut(contentPanel.getSelectedPicture(), OurController.this));
    			contentPanel.cut();
    			contentPanel.setLines(new ArrayList<>());
    			basicDesign.getToolbar().setEnabledUndoButton(true);
        		basicDesign.getToolbar().setEnabledRedoButton(false);
                basicDesign.repaint();
                contentPanel.repaint();
    		});

    	} else {
    		contentPanel.addLine(x, y);
    	}
    }
}
