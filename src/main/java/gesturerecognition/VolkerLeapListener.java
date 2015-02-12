package main.java.gesturerecognition;

import main.java.userInterface.ContentPanel;
import main.java.userInterface.OurController;
import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Vector;

public class VolkerLeapListener extends com.leapmotion.leap.Listener {

	/** Members **/
    private ContentPanel contentPanel;
    
    double totalDegrees = 0;

	private int scrWidth, scrHeight;
	float clickThresholdRight = 0.0f;
	float clickThresholdLeft = 0.0f;
	boolean prevRightClick = false;
	OurController ourController;

    int timeNotMovedLeft = 0;
    int timeNotMovedRight = 0;

	public void setScrWidth(int scrWidth) {
		this.scrWidth = scrWidth;
	}

	public void setScrHeight(int scrHeight) {
		this.scrHeight = scrHeight;
	}

	/** Methods **/
	public VolkerLeapListener() {
		super();
	}

	public void onConnect(Controller controller) {
		System.out.println("Leap Motion Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
//		controller.config().setFloat("Gesture.Swipe.MinLength", 250.0f);
		controller.config().save();
	}

	public void onDisconnect(Controller controller) {
		System.out.println("Leap Motion Disconnected");
	}

	public void onInit(Controller controller) {
		System.out.println("Leap Motion Initialization");
		// Enable recognition of circle gesture
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
	}

	public void onFrame(Controller controller) {
		// System.out.println("Frame available");
		Frame frame = controller.frame();

        // Nr of extended fingers on left hand
        int leftHandFingerCount = 0;

        // Nr of extended fingers on right hand
        int rightHandFingerCount = 0;

		// Update Hand
		for (Hand h : frame.hands()) {
				try {
                    if (h.isLeft()) {

                        this.timeNotMovedLeft = 0;

                        /** UPDATE LEFT HAND **/

                        for (int i = 0; i < h.fingers().count(); i++) {
                            if (h.fingers().get(i).isExtended())
                                leftHandFingerCount += 1;
                        }

                        InteractionBox iBox = frame.interactionBox();
                        Vector normalizedPos = iBox.normalizePoint(h.fingers().frontmost()
                                .stabilizedTipPosition());
                        int leftHandXPos = (int) (normalizedPos.getX() * scrWidth);
                        int leftHandYPos = (int) (scrHeight - normalizedPos.getY()
                                * scrHeight);

                        // Distance to screen ( rounded to 2 decimals)
                        Finger frontmostFinger = h.fingers().frontmost();
                        float leftHandDistanceToScreen = (float) Math.round(frontmostFinger
                                .touchDistance() * 100) / 100;

                        // To click or not to click
                        boolean leftHandClick = leftHandDistanceToScreen < clickThresholdLeft;

                        // Cursor Pressed
                        if (leftHandClick && !prevRightClick) {
                            ourController.cursorPressed(leftHandXPos, leftHandYPos);
                            prevRightClick = true;
                        }

                        // Cursor Released
                        if (!leftHandClick && prevRightClick) {
                            ourController.cursorReleased(leftHandXPos, leftHandYPos);
                            prevRightClick = false;
                        }

                        // Cursor dragged
                        if (leftHandClick && prevRightClick) {
                            ourController.cursorDragged(leftHandXPos, leftHandYPos);
                        }

                        // Shape Mode
                        switch (leftHandFingerCount) {
                            case 0: // REDUCE
                                contentPanel.setToolMode(OurController.ToolMode.REDUCE);
                                break;
                            case 1: // MOVE
                                contentPanel.setToolMode(OurController.ToolMode.MOVE);
                                break;
                            case 2:
                                break;
                            case 3: // ROTATE
                                contentPanel.setToolMode(OurController.ToolMode.ROTATE);
                                break;
                            case 4:

                                // TODO

//                                ourController.recognizeSpeech();
                                break;
                            case 5: // ENLARGE
                                contentPanel.setToolMode(OurController.ToolMode.ENLARGE);
                                break;
                            default:
                                System.out.println("Hoeveel vingers heb je eigenlijk?");
                        }

                        // Update drawpanel
                        contentPanel.setLeapLeftX(leftHandXPos);
                        contentPanel.setLeapLeftY(leftHandYPos);
                        contentPanel.setLeapLeftScreenDist(leftHandDistanceToScreen);
                        contentPanel.setLeapLeftClick(leftHandClick);
                    }


                    this.timeNotMovedRight += 1;

					contentPanel.setLeapRightClick(false);
				} catch (Exception e) {
					e.printStackTrace();
				}

			if (h.isRight()) {

                this.timeNotMovedRight = 0;

             /** UPDATE RIGHT HAND **/

                for (int i = 0; i < h.fingers().count(); i++) {
                    if (h.fingers().get(i).isExtended())
                        rightHandFingerCount += 1;
                }

                // Hand (finger) position
                InteractionBox iBox = frame.interactionBox();
                Vector normalizedPos = iBox.normalizePoint(h.fingers().get(1)
                        .stabilizedTipPosition());

                int rightHandXPos = (int) (normalizedPos.getX() * scrWidth) - 250;
                int rightHandYPos = (int) (scrHeight - normalizedPos.getY()
                        * scrHeight) - 30;

//        Vector normalizedPosPalm = iBox.normalizePoint(hand.fingers().fingerType(Finger.Type.TYPE_INDEX)
//                .stabilizedTipPosition());

                int rightHandXPosPalm = (int) (normalizedPos.getX() * scrWidth) - 250;
                int rightHandYPosPalm = (int) (scrHeight - normalizedPos.getY()
                        * scrHeight) - 30;

                // Distance to screen ( rounded to 2 decimals)
                Finger frontMostFinger = h.fingers().frontmost();
                float rightHandDistanceToScreen = (float) Math
                        .round(frontMostFinger.touchDistance() * 100) / 100;

                // To click or not to click
                boolean rightHandClick = rightHandDistanceToScreen < clickThresholdRight;

                // Cursor Pressed
                if (rightHandClick && !prevRightClick) {
                    ourController.cursorPressed(rightHandXPos, rightHandYPos);
                    prevRightClick = true;
                }

                // Cursor Released
                if (!rightHandClick && prevRightClick) {
                    ourController.cursorReleased(rightHandXPos, rightHandYPos);
                    prevRightClick = false;
                }

                // Cursor dragged
                if (rightHandClick && prevRightClick) {
                    ourController.cursorDragged(rightHandXPos, rightHandYPos);
                }

                // Shape Mode
                switch (rightHandFingerCount) {
                    case 0: // REDUCE
                        contentPanel.setToolMode(OurController.ToolMode.REDUCE);
                        break;
                    case 1: // MOVE
                        contentPanel.setToolMode(OurController.ToolMode.MOVE);
                        break;
                    case 2:
                        break;
                    case 3: // ROTATE
                        contentPanel.setToolMode(OurController.ToolMode.CUT);
                        break;
                    case 4:
                        contentPanel.setToolMode(OurController.ToolMode.ROTATE);
                        break;
                    case 5: // ENLARGE
                        contentPanel.setToolMode(OurController.ToolMode.ENLARGE);
                        break;
                    default:
                        System.out.println("Hoeveel vingers heb je eigenlijk?");
                }


                // Update drawpanel
                contentPanel.setLeapRightX(Math.max(0,Math.min(rightHandXPos, this.scrWidth - 200)));
                contentPanel.setLeapRightY(Math.max(0, Math.min(rightHandYPos, this.scrHeight - 200)));
                contentPanel.setLeapRightScreenDist(rightHandDistanceToScreen);
                contentPanel.setLeapRightClick(rightHandClick);


                this.timeNotMovedLeft += 1;


				contentPanel.setLeapLeftClick(false);
			}
		}
		if (frame.hands().isEmpty()) {
			contentPanel.setLeapRightClick(false);
			contentPanel.setLeapLeftClick(false);


            this.timeNotMovedLeft += 1;
            this.timeNotMovedRight += 1;

        }

        /** UPDATE GESTURES **/

        // Check if gesture is circle
        for(Gesture gesture : frame.gestures()) {
            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    if(ourController.toolModeIndex == OurController.ToolMode.ROTATE) {
                        switch (gesture.state()) {
                            case STATE_START:
                                //Handle starting gestures
                                break;
                            case STATE_UPDATE:
                                //Handle continuing gestures
                                // Determine direction
                                CircleGesture circle = new CircleGesture(gesture);
                                //boolean clockwise;
                                if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
                                    //clockwise = true;
                                    ourController.rotate(0.30);
                                    totalDegrees += 0.30;
                                }
                                else {
                                    //clockwise = false;
                                    ourController.rotate(-0.30);
                                    totalDegrees -= 0.30;
                                }
                                //contentPanel.rotate(clockwise);
                                break;
                            case STATE_STOP:
                                //Handle ending gestures
                                System.out.println("Degrees rotated: " + totalDegrees);
                                ourController.addRotateAction(totalDegrees);
                                totalDegrees = 0;
                                break;
                            default:
                                //Handle unrecognized states
                                break;
                        }
                    }
                    break;
                case TYPE_SWIPE:
                    if (rightHandFingerCount == 5 || leftHandFingerCount == 5) {
                        ourController.deleteSelectedPicture();
                        System.out.println("Swipe !!!");
                    }
                    break;
                default:
                    System.out.println(gesture.toString() + " detected");
                    break;
            }
        }

		contentPanel.repaint();
		}

	public void setOurController(OurController ourController) {
		this.ourController = ourController;
	}
	public void setContentPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
	}

    public int getTimeNotMovedLeft() {
        return timeNotMovedLeft;
    }

    public void setTimeNotMovedLeft(int timeNotMovedLeft) {
        this.timeNotMovedLeft = timeNotMovedLeft;
    }

    public int getTimeNotMovedRight() {
        return timeNotMovedRight;
    }

    public void setTimeNotMovedRight(int timeNotMovedRight) {
        this.timeNotMovedRight = timeNotMovedRight;
    }


}