package main.java.userInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private BufferedWriter writer;
	
	public Logger(){
		try {
			int x = 1;
			File file = new File("logs/" + App.testMode.toString() + "Log" + App.participantNr + ".txt");
			while(file.exists()){
				x++;
				file = new File("logs/"+ App.testMode.toString() +"Log" + App.participantNr + "-" + x + ".txt");
		}

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
