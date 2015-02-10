package main.java.gesturerecognition;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyEventListener implements KeyListener{
	private DrawingPanel drawingPanel;
	public KeyEventListener(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		drawingPanel.keyTyped(e.getKeyChar());
		
	}
}