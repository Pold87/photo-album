package main.java.userInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private BufferedWriter writer;
	
	public Logger(int participantNumber){
		try {
			int x = 1;
			File file = new File("Log" + participantNumber + ".txt");		
			while(file.exists()){
				x++;
				file = new File("Log" + participantNumber + "-" + x + ".txt");
		}
		file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		writer = new BufferedWriter (fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void logAction(String s){
		try {
			writer.write(s);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
