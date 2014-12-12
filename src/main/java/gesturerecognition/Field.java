package main.java.gesturerecognition;

import java.awt.Graphics;

import javax.swing.JComponent;

public class Field extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Initialization (positions to be changed)
	float x = 650;
	float y = 400;
	float width = 200;
	float height = 200;
	private final int SPEED = 5000;
	
	public void paint(Graphics g) {
		g.fillRect((int)x, (int)y, (int)width, (int)height);
	}
	
	public void set(float[] pC, double maxx, double maxy) {
		//System.out.println(pC[0] + ", " + pC[1]);
		if ((double) (x + pC[0]/SPEED + width) < maxx && (double) (x + pC[0]/SPEED) > 0) {
			x += pC[0]/SPEED;
			width += pC[2]/SPEED;
		}
		if ((double) (y - pC[1]/SPEED + height) < maxy && (double) (y - pC[1]/SPEED) > 0) {
			y -= pC[1]/SPEED;
			height += pC[3]/SPEED;
		}
		//System.out.println("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
	}
	
	
	
}
