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

	//It's kinda nasty that I made global variables of these, but it saves a lot of parameter passing.
	public static TestMode testMode;
	public static int participantNr;

    /**
     * Launch the application.
     * @throws InterruptedException 
     * @throws InvocationTargetException 
     */
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new ModeSelector();        
    }
    
    public static void modeSelected(int partNr, TestMode testmode, String task){

        System.out.println(task);

        EventQueue.invokeLater(() -> {
            try {
                testMode = testmode;
                participantNr = partNr;
                new BasicDesign("/pictures/", task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
