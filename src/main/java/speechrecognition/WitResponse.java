package main.java.speechrecognition;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static main.java.speechrecognition.Record.recordExtern;

public class WitResponse {

   	private String msg_id;
	private String _text;
   	private List<WitOutcomes> outcomes;

 	public String get_text(){
		return this._text;
	}
	public void set_text(String _text){
		this._text = _text;
	}
 	public String getMsg_id(){
		return this.msg_id;
	}
	public void setMsg_id(String msg_id){
		this.msg_id = msg_id;
	}
 	public List<WitOutcomes> getOutcomes(){
		return this.outcomes;
	}
	public void setOutcomes(List<WitOutcomes> outcomes){
		this.outcomes = outcomes;
	}

	public String getIntent() {
		String intent = getOutcomes().get(0).getIntent();
		return intent;
	}

	public ArrayList<Integer> getEntities() {

		ArrayList<Integer> entities = new ArrayList<Integer>();


		if (getOutcomes().get(0).getEntities() != null)

		for (WitNumber w : getOutcomes().get(0).getEntities().getNumber()) {
			entities.add(w.getValue());
		}

		return entities;
	}

}
