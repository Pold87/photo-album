package main.java.gesturerecognition;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Vector;

import main.java.userInterface.*;

public class LeapListener extends com.leapmotion.leap.Listener {
	/** Members **/
	private DrawingPanel contentPanel;
	private int scrWidth, scrHeight;
	float clickThresholdRight = 0.0f;
	float clickThresholdLeft = 0.0f;
	boolean prevRightClick = false;

	public void setScrWidth(int scrWidth) {
		this.scrWidth = scrWidth;
	}

	public void setScrHeight(int scrHeight) {
		this.scrHeight = scrHeight;
	}

	/** Methods **/
	public LeapListener() {
		super();
	}

	public void onConnect(Controller controller) {
		System.out.println("Leap Motion Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.config().setFloat("Gesture.Swipe.MinLength", 250.0f);
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

		// Update hands
		int nrHands = frame.hands().count();
		if (nrHands == 1) {
			if (frame.hands().get(0).isLeft()) {
				// links
				this.updateLeftHand(frame, frame.hands().get(0));
				contentPanel.setLeapRightClick(false);
			} else if (frame.hands().get(0).isRight()) {
				// rechts
				this.updateRightHand(frame, frame.hands().get(0));
				contentPanel.setLeapLeftClick(false);
			}
		} else if (nrHands == 2) {
			this.updateLeftHand(frame, frame.hands().leftmost());
			this.updateRightHand(frame, frame.hands().rightmost());
		} else {
			contentPanel.setLeapRightClick(false);
			contentPanel.setLeapLeftClick(false);
		}
		// Update gestures
		updateGestures(frame);
	}

	private void updateGestures(Frame frame) {
		// Check if gesture is circle
		for(Gesture gesture : frame.gestures()) {
			switch (gesture.type()) {
			case TYPE_CIRCLE:
				switch (gesture.state()) {
		        case STATE_START:
		            //Handle starting gestures
		            break;
		        case STATE_UPDATE:
		            //Handle continuing gestures
		        	// Determine direction
		        	CircleGesture circle = new CircleGesture(gesture);
		        	boolean clockwise;
		        	if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
		        	        clockwise = true;
		        	}
		        	else {
		        	    clockwise = false;
		        	}
					contentPanel.rotate(clockwise);
		            break;
		        case STATE_STOP:
		            //Handle ending gestures
		            break;
		        default:
		            //Handle unrecognized states
		            break;
		    }
			break;
			default:
				System.out.println(gesture.toString() + " detected");
				break;
			}
		}
	}

	private void updateRightHand(Frame frame, Hand hand) {
		// Right Hand
		if (hand.isRight()) {
			// Nr of extended fingers on left hand
			int rightHandFingerCount = 0;
			for (int i = 0; i < hand.fingers().count(); i++) {
				if (hand.fingers().get(i).isExtended())
					rightHandFingerCount += 1;
			}
			////////////////////////////////// Previous (palmposition) //////////////////////////////
			/*
			// Right hand finger coordinates

			InteractionBox iBox = frame.interactionBox();
			Vector normalizedPos = iBox.normalizePoint(hand
				.stabilizedPalmPosition());
			*/
			////////////////////////////////// Now (fingertipposition) //////////////////////////////
			InteractionBox iBox = frame.interactionBox();
			Vector normalizedPos = iBox.normalizePoint(hand.fingers().frontmost()
					.stabilizedTipPosition());

			int rightHandXPos = (int) (normalizedPos.getX() * scrWidth) - 250;
			int rightHandYPos = (int) (scrHeight - normalizedPos.getY()
					* scrHeight) - 30;

			// Distance to screen ( rounded to 2 decimals)
			Finger frontMostFinger = hand.fingers().frontmost();
			float rightHandDistanceToScreen = (float) Math
					.round(frontMostFinger.touchDistance() * 100) / 100;

			// To click or not to click
			boolean rightHandClick = rightHandDistanceToScreen < clickThresholdRight;

			// Cursor Pressed
			if (rightHandClick && !prevRightClick) {
				contentPanel.cursorPressed(rightHandXPos, rightHandYPos);
				prevRightClick = true;
			}

			// Cursor Released
			if (!rightHandClick && prevRightClick) {
				contentPanel.cursorReleased(rightHandXPos, rightHandYPos);
				prevRightClick = false;
			}

			// Cursor dragged
			if (rightHandClick && prevRightClick) {
				contentPanel.cursorDragged(rightHandXPos, rightHandYPos);
			}

			// Shape Mode
			switch (rightHandFingerCount) {
			case 0: // REDUCE
				contentPanel.setToolMode(DrawingPanel.ToolMode.REDUCE);
				break;
			case 1: // MOVE
				contentPanel.setToolMode(DrawingPanel.ToolMode.MOVE);
				break;
			case 2: // ROTATE
				contentPanel.setToolMode(DrawingPanel.ToolMode.ROTATE);
				break;
			case 3:

				break;
			case 4:
				break;
			case 5: // ENLARGE
				contentPanel.setToolMode(DrawingPanel.ToolMode.ENLARGE);
				break;
			default:
				System.out.println("Hoeveel vingers heb je eigenlijk?");
			}

			// Update drawpanel
			contentPanel.setLeapRightX(rightHandXPos);
			contentPanel.setLeapRightY(rightHandYPos);
			contentPanel.setLeapRightScreenDist(rightHandDistanceToScreen);
			contentPanel.setLeapRightClick(rightHandClick);
			contentPanel.setLeapRightFingers(rightHandFingerCount);
		}
	}

	private void updateLeftHand(Frame frame, Hand hand) {
		// Left hand
		if (hand.isLeft()) {
			// Nr of extended fingers on left hand
			int leftHandFingerCount = 0;
			for (int i = 0; i < hand.fingers().count(); i++) {
				if (hand.fingers().get(i).isExtended())
					leftHandFingerCount += 1;
			}

			////////////////////////////////// Previous (palmposition) //////////////////////////////
			/*
			// Left hand finger coordinates

			InteractionBox iBox = frame.interactionBox();
			Vector normalizedPos = iBox.normalizePoint(hand
				.stabilizedPalmPosition());
			*/
			////////////////////////////////// Now (fingertipposition) //////////////////////////////
			InteractionBox iBox = frame.interactionBox();
			Vector normalizedPos = iBox.normalizePoint(hand.fingers().frontmost()
					.stabilizedTipPosition());
			int leftHandXPos = (int) (normalizedPos.getX() * scrWidth);
			int leftHandYPos = (int) (scrHeight - normalizedPos.getY()
					* scrHeight);

			// Distance to screen ( rounded to 2 decimals)
			Finger frontmostFinger = hand.fingers().frontmost();
			float leftHandDistanceToScreen = (float) Math.round(frontmostFinger
					.touchDistance() * 100) / 100;

			// To click or not to click
			boolean leftHandClick = leftHandDistanceToScreen < clickThresholdLeft;
			//boolean leftHandClick = leftHandDistanceToScreen < clickThresholdLeft
				//	&& leftHandFingerCount > 0 && leftHandFingerCount <= 2;

			// Cursor Pressed
			if (leftHandClick && !prevRightClick) {
				contentPanel.cursorPressed(leftHandXPos, leftHandYPos);
				prevRightClick = true;
			}

			// Cursor Released
			if (!leftHandClick && prevRightClick) {
				contentPanel.cursorReleased(leftHandXPos, leftHandYPos);
				prevRightClick = false;
			}

			// Cursor dragged
			if (leftHandClick && prevRightClick) {
				contentPanel.cursorDragged(leftHandXPos, leftHandYPos);
			}

			// Shape Mode
			switch (leftHandFingerCount) {
			case 0: // REDUCE
				contentPanel.setToolMode(DrawingPanel.ToolMode.REDUCE);
				break;
			case 1: // MOVE
				contentPanel.setToolMode(DrawingPanel.ToolMode.MOVE);
				break;
			case 2: // ROTATE
				contentPanel.setToolMode(DrawingPanel.ToolMode.ROTATE);
				break;
			case 3:

				break;
			case 4:
				break;
			case 5: // ENLARGE
				contentPanel.setToolMode(DrawingPanel.ToolMode.ENLARGE);
				break;
			default:
				System.out.println("Hoeveel vingers heb je eigenlijk?");
			}

			// Update drawpanel
			contentPanel.setLeapLeftX(leftHandXPos);
			contentPanel.setLeapLeftY(leftHandYPos);
			contentPanel.setLeapLeftScreenDist(leftHandDistanceToScreen);
			contentPanel.setLeapLeftClick(leftHandClick);
			contentPanel.setLeapLeftFingers(leftHandFingerCount);
		}
	}

	public void setContentPanel(DrawingPanel contentPanel) {
		this.contentPanel = contentPanel;
	}

}
