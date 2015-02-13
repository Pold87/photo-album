package main.java.userInterface;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
	
	private OurController controller;
	private String s; 
	private MyImage selectedImage;
	
	public void run() {
		if (s == "enlarge") {
			// Specify maximum height of image
	        if (selectedImage.getHeight() < 650) {
	            double normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
	            double normalizerY = -((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));
	
	            selectedImage.setX((int) (selectedImage.getX() - normalizerX));
	            selectedImage.setY((int) (selectedImage.getY() + normalizerY));
	
	            selectedImage.resizeImg((int) (selectedImage.getWidth() + 6 * normalizerX), (int) (selectedImage.getHeight() - 6 * normalizerY));
	        }
		}
		else if (s == "reduce") {
			double normalizerX = (double) selectedImage.getWidth() / (double) (selectedImage.getWidth() + selectedImage.getHeight());
			double normalizerY = - ((double) selectedImage.getHeight() / (double) (selectedImage.getWidth() + selectedImage.getHeight()));

            // Specify minimum height
			if(selectedImage.getHeight() > 120) {
				selectedImage.setY((int) (selectedImage.getY() - 4*normalizerY));
				selectedImage.setX((int) (selectedImage.getX() + 4*normalizerX));
				
				selectedImage.resizeImg((int) (selectedImage.getWidth() - 8 * normalizerX), (int) (selectedImage.getHeight() + 8*normalizerY));
			}
		}
	}
	
	public void add(OurController oc) {
		this.controller = oc;
	}
	
	public void addVariables(MyImage selectedImage, String s) {
		this.selectedImage = selectedImage;
		this.s = s;
	}
	
	public boolean isRunning() {
		if(s == null) {
			return false;
		}
		return true;
	}
}
