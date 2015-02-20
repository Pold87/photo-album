package main.java.userInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	private BufferedWriter writer;
	private int nrOfActions;
	//Another nasty hacky way of doing stuff.
	private String previousRotate;
	
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
		//write the headers
		writer.write("Action nr, Modality, Action name, new X, new Y, Orientation, Width, Height, new backgroundColor, TimeStamp ");
		writer.newLine();
		writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public Logger(String task) {
        try {
            int x = 1;
            File file = new File("logs/" + App.testMode.toString() + "Log" + App.participantNr + "-" + task + ".txt");
            while(file.exists()){
                x++;
                file = new File("logs/"+ App.testMode.toString() +"Log" + App.participantNr + "-" + task + "-" + x + ".txt");
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            writer = new BufferedWriter (fw);
            //write the headers
            writer.write("Action nr, Modality, Action name, new X, new Y, Orientation, Width, Height, new backgroundColor, TimeStamp ");
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
	public void logAction(String s){
		try {
			if(s.contains("Rotate")){
				previousRotate = s;
			}else{
				if(previousRotate != null){
					nrOfActions++;
					String entry = nrOfActions + ", " + previousRotate;
					writer.write(entry);
					writer.newLine();
					previousRotate = null;
				}
				nrOfActions++;
				String entry = nrOfActions + ", " + s;
				writer.write(entry);
				writer.newLine();
				writer.flush();
			}		
		} catch (IOException e) {
			e.printStackTrace();
        }
    }

}
