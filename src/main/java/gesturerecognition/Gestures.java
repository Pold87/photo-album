package main.java.gesturerecognition;

public class Gestures {

	private LeapListener listener;
	private int g = -1;
	private boolean GestureStartFound = false;
	private float[] gesture = new float[4];
	float startxr, startyr, startxl, startyl;
	int speed = 3000;
	
	public Gestures(LeapListener listener) {
		this.listener = listener;
		while(!listener.determineGestureStart()) {
			// wait for startgesture
		}
		g = listener.getGesture();
		GestureStartFound = true;
		System.out.println("GestureStart found! " + g);
		startxr = listener.getStartPositions()[6].getX();
		startyr = listener.getStartPositions()[6].getY();
		startxl = listener.getStartPositions()[3].getX();
		startyl = listener.getStartPositions()[3].getY();
	}
	
	public boolean gestureStartFound() {
		return GestureStartFound;
	}
	
	public void set() {
        setGesture();
        setGestureStartFoundFalse();
	}
	
	private void setGestureStartFoundFalse() {
		GestureStartFound = false;
	}

	private void setGesture() {
		g = -1;
	}
	
	public int getGesture() {
		return g;
	}
	
	public float[] doGesture(int g) {
		switch(g) {
		case 0:	// Move
		
			gesture[0] = (listener.getChanges())[6].getX();
			//System.out.println("pos x:" + gesture[0]);
			gesture[1] = (listener.getChanges())[6].getY();
			//System.out.println("pos y:" + gesture[1]);

			gesture[2] = 0;
			gesture[3] = 0;
		
			return gesture;
		case 1:	// Resize
			float posxr = listener.getPositions()[6].getX();
			float posyr = listener.getPositions()[6].getY();
			//System.out.println("posxr:"+posxr+"\n posyr:" + posyr);
			float posxl = listener.getPositions()[3].getX();
			float posyl = listener.getPositions()[3].getY();
			//System.out.println("posxl:"+posxl+"\n posyl:" + posyl);
//			gesture[0] = (posxl - startxl)/50000;
//			gesture[1] = (-(posyl - startyl))/50000;
//			gesture[2] = (posxr - startxr + posyr)/50000;
//			gesture[3] = (-(posyr - startyr) - posyl)/50000;
			gesture[0] = listener.getPositions()[3].getX()/speed;
			gesture[1] = listener.getPositions()[3].getY()/speed;
			gesture[2] = (-1*(listener.getPositions()[3].getX() - listener.getPositions()[6].getX()))/speed;
			gesture[3] = (listener.getPositions()[3].getY() - listener.getPositions()[6].getY())/speed;
			return gesture;
		case 2: // Rotate
			//gesture[0]=listener.getPositions()[6].getX()-listener.getChanges()[6]
			return null;
		case 3:	// Cut
			return null;
		default:
				System.out.println("error fout");
				return null;
		}
	}
	
}
