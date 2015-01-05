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