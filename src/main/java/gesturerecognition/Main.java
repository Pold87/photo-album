package main.java.gesturerecognition;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String... args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				System.out.println("Main running...");
				new Window();	
				System.out.println("End of Main");
			}
			
		});
		
	}
}
