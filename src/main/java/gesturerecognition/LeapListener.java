package main.java.gesturerecognition;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Finger.Type;

public class LeapListener extends Listener {
	
	private Vector[] fingerpositions = new Vector[10]; //List of positions of all fingers
	private Vector[] oldfingerpositions = new Vector[10];
	private Vector[] startpositionsresize = new Vector[10];
	private Vector[] changes = new Vector[10];
	private Vector[] startpositions = new Vector[10];
	private boolean start = true;
	private int nrFingers = 10;
	
	public void onInit(Controller controller) {
        System.out.println("Initialized");
        // initialize vectors
        for (int i = 0; i < nrFingers; i++) {
        	fingerpositions[i] = new Vector(-1000, -1000, -1000);
        	startpositions[i] = new Vector(-1000, -1000, -1000);
        	startpositionsresize[i] = new Vector(-1000, -1000, -1000);
        }
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        //System.out.println("begin change");
        oldfingerpositions = fingerpositions;
        //System.out.println("end change");
        setPositions(frame);
        //System.out.println("list: " + fingerpositions.toString());

    }
    
    private void setPositions(Frame frame) {
    	// if nothing is seen, clear positionlist
    	if (frame.hands().isEmpty() && frame.fingers().isEmpty()) {
    		//System.out.println("No finger seen");
            for (int i = 0; i < nrFingers; i++) {
            	fingerpositions[i] = new Vector(-1000, -1000, -1000);
            	startpositions[i] = new Vector(-1000, -1000, -1000);
            	oldfingerpositions[i] = new Vector(-1000, -1000, -1000);
            }
            start = true;
    	}
    	else {
    		//System.out.println("Finger seen");
	        //Get hands
	        for(Hand hand : frame.hands()) {
	            String handType = hand.isLeft() ? "Left hand" : "Right hand";
	
	            // Get fingers
	            for (Finger finger : hand.fingers()) {
	            	Vector victor = finger.tipPosition();
	            	Type fingerType = finger.type();
	            	int position = -1;
	            	// Retrieve position in arraylist
	        		// positions 0 - 4
	        		if(fingerType == Type.TYPE_THUMB) {
	            		// if thumb, position 0
	        			position = 0;
	            	}
	            	if(fingerType == Type.TYPE_INDEX) {
	            		// if index finger, position 1
	            		position = 1;
	            	}
	            	if(fingerType == Type.TYPE_MIDDLE) {
	            		// if middle finger, position 2
	            		position = 2;
	            	}
	            	if(fingerType == Type.TYPE_RING) {
	            		// if ring finger, position 3
	            		position = 3;
	            	}
	            	if(fingerType == Type.TYPE_PINKY) {
	            		// if pinky, position 4
	            		position = 4;
	            	}
	            	if(handType == "Right hand") {
	            		// if right hand, add 5 to position
	            		position += 5;
	            	}
	            	if (start) {
	            		startpositions[position] = victor;
	            	}
	            	float changex = victor.getX() - oldfingerpositions[position].getX();
	            	float changey = victor.getY() - oldfingerpositions[position].getY();
	            	changes[position] = new Vector(changex, changey, 0);
	            	//System.out.println(changes[position].getX());
	            	// Add new vector to list
	            	fingerpositions[position] = victor;
	            	//System.out.println(victor.getX() + ", " + oldfingerpositions[position].getX() + ", " + fingerpositions[position].getX());
	            	//System.out.println(fingerpositions[position].getX() + " - " + oldfingerpositions[position].getX() + " = " + (fingerpositions[position].getX()-oldfingerpositions[position].getX()));
	            }
	        }
	        start = false;
    	}
		
	}
    
    public boolean determineGestureStart() {
		/* The gesture can start at 2 different moments:
		 * 1. If start z-position > 0, gesture starts at (start z-position) + 30. 
		 * 2. If start z-position < 0, gesture starts at z-position 30.
		 */
    	float fmax = getMax(fingerpositions);
    	float smax = getMax(startpositions);
    	//System.out.println("fmax: " + fmax + ", smax: " + smax);
    	//System.out.println(smax-fmax);
    	if ((smax - fmax) >= 50 && fmax <= 70 && fmax > -1000 && smax > -1000) {
    		startpositionsresize = fingerpositions;
    		//System.out.println("yes");
    		return true;
    	}
    	return false;
    }

	public int getGesture() {
		// TODO: change test
		return 1;
    	//return g;
    }
	
	public float getMax(Vector[] v) {
		float max = -1000;
		for (int i = 0 ; i < v.length ; i++) {
			if (v[i].getZ() > max) {
				max = v[i].getZ();
			}
		}
		return max;
	}
	
	public Vector[] getPositions() {
		return fingerpositions;
	}
    
	public Vector[] getOldPositions() {
		return oldfingerpositions;
	}
	
	public Vector[] getChanges() {
		return changes;
	}

	
	public void setStartPositions(float xr, float yr, float xl, float yl) {
		startpositionsresize[3] = new Vector(xl, yl, 0);
		startpositionsresize[6] = new Vector(xr, yr, 0);
	}

	public Vector[] getStartPositions() {
		return startpositionsresize;
	}
}