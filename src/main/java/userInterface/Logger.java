package main.java.userInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {
	private BufferedWriter writer;
	private int nrOfActions;
	//Another nasty hacky way of doing stuff.
	private List<String> previousRotate;
    private int taskNumber = 0;


    public Logger(){
		try {
			int x = 1;
			File file = new File("logs/" + App.testMode.toString() + "Log_" + App.participantNr + ".csv");
			while(file.exists()){
				x++;
				file = new File("logs/"+ App.testMode.toString() +"Log_" + App.participantNr + "-" + x + ".csv");
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		writer = new BufferedWriter (fw);
		//write the headers
		writer.write("task_nr,action_nr,modality,action_name,picture_nr,new_x,new_y,orientation,width,height,new_bg,timestamp");
		writer.newLine();
		writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = Integer.parseInt(taskNumber);
    }

    public void incTaskNumber() {
        this.taskNumber++;
    }
	
	public void logAction(List<String> s){
		try {
			if(s.contains("Rotate")){
				previousRotate = s;
			}else{
                if(previousRotate != null) {
                    nrOfActions++;
                    previousRotate.add(0, Integer.toString(nrOfActions));
                    previousRotate.add(0, Integer.toString(this.taskNumber));
                    String entry = String.join(",", previousRotate.toArray(new String[previousRotate.size()]));
                    writer.write(entry);
                    writer.newLine();
                    previousRotate = null;
                }
                nrOfActions++;
                s.add(0, Integer.toString(nrOfActions));
                s.add(0, Integer.toString(taskNumber));
                String entry = String.join(",", s.toArray(new String[s.size()]));
				writer.write(entry);
				writer.newLine();
				writer.flush();
			}		
		} catch (IOException e) {
			e.printStackTrace();
        }
    }

}
