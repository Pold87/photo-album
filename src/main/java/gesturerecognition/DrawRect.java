package main.java.gesturerecognition;

import java.awt.Rectangle;
import java.io.IOException;
import javax.swing.JFrame;
import com.leapmotion.leap.*;

public class DrawRect {
	public static void main(String[] a) {
	    JFrame window = new JFrame();
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //window.setBounds(1, 1, 1500, 800);
	    window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	    Field frame = new Field();
	    window.getContentPane().add(frame);
	    window.setVisible(true);
	    
	    Rectangle bounds = window.getGraphicsConfiguration().getBounds();
	    double maxx = bounds.getWidth();
	    double maxy = bounds.getHeight();
	    
	    // Create a sample listener and controller
        LeapListener listener = new LeapListener();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        for (int i = 0 ; i < 100 ; i++) {
			    Gestures ges = new Gestures(listener);
			    System.out.println("Gesture found: " + ges.gestureStartFound());
			    if (ges.gestureStartFound()) {
			    	int g = ges.getGesture();
			    	// while gesture (z is bigger than 50)
			    	while(listener.getMax(listener.getPositions()) <= 70) {
			    		float[] positionChanges = ges.doGesture(g);
			    		//System.out.println(positionChanges[0] + ", " + positionChanges[1]);
			    		frame.set(positionChanges, maxx, maxy);
			    		//frame.set(positionChanges);
			    		window.validate();
			    		window.repaint();
			    	}
			    	System.out.println(listener.getMax(listener.getPositions()));
			    }
			    ges.set();
        }
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
	    
	  }
	
	
	
}