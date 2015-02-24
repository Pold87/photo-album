package main.java.userInterface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import edu.cmu.sphinx.api.SpeechResult;
import main.java.speechrecognition.*;


public class OurController implements MouseMotionListener, MouseListener, ActionListener, ToolBarListener, WitThreadCompleteListener, SimpleSpeechThreadCompleteListener {

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
    private SpeechCommands sc;
    private Thread threadSimpleSpeech;
    private SpeechResult result;
    private boolean killed;
    public boolean waitForWit;

    @Override
    public void notifyOfSimpleSpeech(SpeechCommands sc) {

        if (sc == null || sc.getIntent() == null || sc.getIntent()[0].equals("unknown")) {

            System.out.println(sc);
            System.out.println(sc.getIntent());
//                    sc.getIntent()[0].equals("unknown")

            this.waitForWit = true;
            System.out.println("Unknown");

        }
        else {

            System.out.println(sc.getIntent()[0]);

            this.waitForWit = false;
            recognizedSimpleResponse(sc.getIntent());


            this.contentPanel.setSpeechProcessing(false);

            thread_wit = null;
            thread = null;

            if (runnable != null) {
                runnable.finish();
                runnable.terminate();

            }
        }

    }

    public enum Modality{
    	MOUSE, SPEECH, LEAP
    }
    public Modality currentModality =  Modality.MOUSE;

    @Override
    public void notifyOfThreadComplete(Wit wit) {

        if (!killed) {

            if (waitForWit) {
                try {
                    recognizedWitResponse(wit);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                recognizedText(wit.getWitRawJSONString());

                this.endWit();

            }
        }

    }

    public void killWit() {
        this.killed = true;
        this.endWit();
    }

    public void endWit() {
        thread_wit = null;
        thread = null;

        if (runnable != null) {
            runnable.finish();
            runnable.terminate();

        }

        this.contentPanel.setSpeechProcessing(false);
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

	public void initialize(ContentPanel cp, BasicDesign bd) throws URISyntaxException, IOException {
		contentPanel = cp;
		basicDesign = bd;
		logger  = new Logger();
        this.url = getClass().getResource("/recording.wav");
        this.normalRecord =  new File(this.url.toURI());
        this.sc = new SpeechCommands();
        this.sc.setOurController(this);
        this.threadSimpleSpeech = new Thread(this.sc);
        sc.addListener(this);

    }

	public void setToolMode(OurController.ToolMode toolMode) {
		this.toolModeIndex = toolMode;
	}

    public void startSpeech() throws URISyntaxException {
//        SwingUtilities.invokeLater(() -> {

            this.killed = false;

            if (!this.contentPanel.isSpeechProcessing() && !this.contentPanel.isSpeechRecording()) {
                runnable = new Recorder(this.normalRecord, this.contentPanel);
                thread = new Thread(runnable);
                thread.start();
                this.contentPanel.setSpeechRecording(true);
            }
//        });
    }


    public void stopSpeech() {
        SwingUtilities.invokeLater(() -> {

			wit_runnable = null;
			if (this.thread_wit == null) {
				try {
                    this.contentPanel.setSpeechRecording(false);

                    wit_runnable = new Wit(this.normalRecord, "wav");


				} catch (Exception e) {
					e.printStackTrace();
				}


                this.threadSimpleSpeech = new Thread(sc);
                threadSimpleSpeech.start();

                this.thread_wit = new Thread(wit_runnable);
				wit_runnable.addListener(this);
				thread_wit.start();

				contentPanel.setSpeechProcessing(true);
			}
		});
    }

    //START CommandInterface
    public void selectPicture(int nr) {
        SwingUtilities.invokeLater(() -> contentPanel.selectPicture(nr));
    }

    public void addPictureByNumber(int nr) {

        SwingUtilities.invokeLater(() -> {

            ArrayList<Integer> nums = basicDesign.getPictureNums();
            if (nums.contains(nr)) {
                MyImage image = basicDesign.getLibrary()[nr];
                addPicture(image);
                basicDesign.repaint();
            }

        });
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
                image.resetToOriginalImage();
            }
        });
	}

	public void deletePicture(MyImage img) {

		SwingUtilities.invokeLater(() -> contentPanel.deletePicture(img));
	}

	public void movePicture(int x, int y) {

        SwingUtilities.invokeLater(() -> {
            MyImage image = contentPanel.getSelectedPicture();
            if (image != null) {
                int oldX = image.getX(), oldY = image.getY();
                image.setX(x);
                image.setY(y);
                undoManager.addEdit(new ActionMove(image, oldX, oldY, x, y, this));
                checkUndoRedoButtons();
                contentPanel.repaint();
            }
        });
	}

	public void setBackground(Color color) {
        SwingUtilities.invokeLater(() -> {
            Color oldColor = contentPanel.getBackground();
            contentPanel.setBackground(color);
            undoManager.addEdit(new ActionBackground(oldColor, color, this));
            checkUndoRedoButtons();
            contentPanel.repaint();
        });
	}

	public void rotate(double degrees) {
        SwingUtilities.invokeLater(() -> {
            if (contentPanel.getSelectedPicture() != null) {
                contentPanel.rotate(degrees);
                undoManager.addEdit(new ActionRotate(contentPanel.getSelectedPicture(), (int) contentPanel.getSelectedPicture().getRotationDegrees(), this));
                checkUndoRedoButtons();
                contentPanel.repaint();
            }
        });
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


            switch (toolModeIndex) {
                case MOVE:
                    break;
                case ENLARGE:

                    ((MyTimerTask) task).addVariables(selectedImage, "enlarge", contentPanel.getWidth(), contentPanel.getHeight());
                    timer.scheduleAtFixedRate(task, 0, 25);
                    break;

                case REDUCE:
                    ((MyTimerTask) task).addVariables(selectedImage, "reduce", contentPanel.getWidth(), contentPanel.getHeight());
                    timer.scheduleAtFixedRate(task, 0, 25);
                    break;
                case CUT:
                    break;
                case ROTATE:
                    Thread thread = new Thread(rotateThread);
                    thread.start();
                    break;
                default:
                    break;

            }
        }
	}

    public Logger getLogger() {
        return logger;
    }

	public void cursorReleased(int XPos, int YPos) {
        MyImage selectedImage = contentPanel.getSelectedPicture();

        if (((MyTimerTask) task).isRunning()) {
            task.cancel();
            timer = new Timer();
            task = new MyTimerTask();
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
                case ENLARGE:
                    break;
                case REDUCE:
                    break;
                default:
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

//				if (selectedImage.contains(new Point(XPos + 100, YPos + 100))) {

                    int xDelimited = selectedImage.getX() + deltaX;
                    int yDelimited = selectedImage.getY() + deltaY;

                    selectedImage.setX(Math.max(0,Math.min(xDelimited, contentPanel.getWidth() - selectedImage.getWidth())));
                    selectedImage.setY(Math.max(0,Math.min(yDelimited, contentPanel.getHeight() - selectedImage.getHeight())));

//				}

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
                //
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
		MyImage selectedImage = contentPanel.getSelectedPicture();
		//This boolean is to prevent use of the middle mouse button.
		if (toolModeIndex == ToolMode.RESIZE && (SwingUtilities.isLeftMouseButton(e) ^ SwingUtilities.isRightMouseButton(e)))
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
                ArrayList<Integer> picturesWrongNumbersToAdd = response.extractWrongNumbersShifted(); // Extract all mentioned number

                picturesToAdd.forEach(this :: addPictureByNumber); // Could be a Java 1.8 feature
                picturesToAdd.forEach(this :: selectPicture);

                picturesWrongNumbersToAdd.forEach(this :: addPictureByNumber); // Could be a Java 1.8 feature
                picturesWrongNumbersToAdd.forEach(this :: selectPicture);

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
                ArrayList<Integer> picturesWrongNumbersToSelect = response.extractWrongNumbersShifted(); // Extract all mentioned number
//				ArrayList<Integer> pictureNumbers = response.extractNumbers();

                for (int pic : pictureNumbers) {
                    for (MyImage img : basicDesign.getLibrary()) {
                        if (img.getNum() == pic) {
                            if(contentPanel.imageList.contains(img))
                                selectPicture(img);
                            else
                                addPicture(img);
                        }
                    }
                }

                for (int pic : picturesWrongNumbersToSelect) {
                    for (MyImage img : basicDesign.getLibrary()) {
                        if (img.getNum() == pic) {
                            if(contentPanel.imageList.contains(img))
                                selectPicture(img);
                            else
                                addPicture(img);
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


    public void recognizedSimpleResponse(String[] intent) {
        currentModality = Modality.SPEECH;

        switch (intent[0]) {
            case "add":
                if (Wit.isInteger(intent[1])) {
                    int pictureToAdd = Integer.parseInt(intent[1]) - 10; // Extract all mentioned number
                    System.out.println("Parsed num is " + pictureToAdd);
                    addPictureByNumber(pictureToAdd); // Could be a Java 1.8 feature
//                selectPicture(pictureToAdd);
                    System.out.println("Added picture " + pictureToAdd);
                }
                break;
            case "rotate":
                this.rotate(45); // Rotate 90 degrees if nothing else is said.
                break;
            case "select":
                System.out.println("Select picture " + intent[1]);
                if (intent[1] != "default") {
                    int pictureNumber = Integer.parseInt(intent[1]);
                    for (MyImage img : basicDesign.getLibrary()) {
                        if (img.getNum() == pictureNumber) {
                            if(contentPanel.imageList.contains(img))
                                selectPicture(img);
                            else
                                addPicture(img);
                        }
                    }
                }
                break;
            case "background":
                Color color;
                color = this.wit_runnable.stringToColor(intent[1]);
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
            case "move":
                this.movePicture(contentPanel.getLeapRightX(), contentPanel.getLeapRightY());
                break;
            case "remove":
                if (intent[1].equals("default")) {
                    this.deleteSelectedPicture();
                } else {
                    if (Wit.isInteger(intent[1])) {
                        int pictureToRemove = Integer.parseInt(intent[1]);
                        for (MyImage img : basicDesign.getLibrary()) {
                            if (img.getNum() == pictureToRemove) {
                                this.deletePicture(img);
                            }
                        }
                    }
                }
                break;
            default:
                System.out.println("The recognized intent is unknown: " + intent[0]);
                break;
        }
        basicDesign.repaint();
    }


	private void undo() {
		ArrayList<Integer> cutLines = contentPanel.getLines();
		if(!cutLines.isEmpty()){
			cutLines.remove(cutLines.size()-1);
		}else{
			if(undoManager.canUndo())
				undoManager.undo();
			checkUndoRedoButtons();
		}
	}

	private void redo() {
		if(undoManager.canRedo())
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
        SwingUtilities.invokeLater(() -> {
                    contentPanel.selectPicture(image);
                    basicDesign.repaint();
                });
    }

    public void selectPictureAt(int x, int y) {
        SwingUtilities.invokeLater(() -> {
            contentPanel.selectPictureAt(x, y);
        });
    }

    public void addButtonToLibrary(MyImage image) {
        SwingUtilities.invokeLater(() -> {
            basicDesign.photoBar.addButton(image);
            basicDesign.repaint();
        });
    }


    public void removeButtonFromLibrary(MyImage image) {
        SwingUtilities.invokeLater(() -> {
            basicDesign.photoBar.removeButton(image);
            basicDesign.repaint();
        });
    }

    public void addPicture(MyImage image){
        SwingUtilities.invokeLater(() -> {
            contentPanel.addPictureToCurrentPage(image);
            undoManager.addEdit(new ActionAddPic(image, OurController.this));
            checkUndoRedoButtons();
            removeButtonFromLibrary(image);
            selectPicture(image);
            toolModeIndex = ToolMode.MOVE;
        });
	}

	private void checkUndoRedoButtons(){
        SwingUtilities.invokeLater(() -> {
            basicDesign.getToolbar().setEnabledUndoButton(undoManager.canUndo());
            basicDesign.getToolbar().setEnabledRedoButton(undoManager.canRedo());
            basicDesign.repaint();
        });

	}


    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void cut(int x, int y) {
    	SwingUtilities.invokeLater(() -> {
    		if (contentPanel.getLines().size() == 4) {
    			MyImage image = contentPanel.getSelectedPicture();
    			int oldX = image.getX(), oldY = image.getY(), oldWidth = image.getWidth(), oldHeight = image.getHeight();
    			BufferedImage oldImage = image.getImg();
    			contentPanel.cut();
    			undoManager.addEdit(new ActionCut(contentPanel.getSelectedPicture(), OurController.this, oldX, oldY, oldImage, oldWidth, oldHeight));
    			contentPanel.emptyLineList();
    			checkUndoRedoButtons();
                contentPanel.repaint();
    		} else {
    			contentPanel.addLine(x, y);
    		}
    	});
    }

    public void snapPictureRotation(){
        SwingUtilities.invokeLater(() -> {
            MyImage selectedImage = contentPanel.getSelectedPicture();
            if(selectedImage != null){
                selectedImage.snapRotation();
                contentPanel.repaint();
            }
        });

    }


    public void addToDebugPanel(String s) {

        this.basicDesign.getDebugPanel().appendText(s + "\n");

    }

    public void loadContentPanel(String task, char num) throws IOException, ClassNotFoundException {

        String file_base = "frames/contentPanel-";
        this.contentPanel.loadContentPanel(file_base + task + "-" + num + ".ser");
    }

}
