package speechrecognition;

import java.util.List;

public class WitResponse{
   	
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
}
