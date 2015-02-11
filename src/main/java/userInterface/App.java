package main.java.userInterface;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import main.java.userInterface.BasicDesign;

/**
 * Created by pold on 12/10/14.
 */
public class App {

	public enum TestMode{
		Train, TestLeap, TestMouse;
	}
	public static TestMode testMode;
	public static int participantNr;
	
    /**
     * Launch the application.
     * @throws InterruptedException 
     * @throws InvocationTargetException 
     */
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        ModeSelector ms = new ModeSelector();
        

        
    }
    
    public static void modeSelected(int partNr, TestMode testmode){
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	testMode = testmode;
                	String s = testmode.toString();
                	participantNr = partNr;
                    new BasicDesign("/pictures/");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
