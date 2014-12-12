package main.java.speechrecognition;

public class WitOutcomes{
   	private String _text;
   	private String intent;
   	private WitEntities entities;
   	private Float confidence;

 	public String get_text(){
		return this._text;
	}
	public void set_text(String _text){
		this._text = _text;
	}
 	public Float getConfidence(){
		return this.confidence;
	}
	public void setConfidence(Float confidence){
		this.confidence = confidence;
	}
 	public WitEntities getEntities(){
		return this.entities;
	}
	public void setEntities(WitEntities entities){
		this.entities = entities;
	}
 	public String getIntent(){
		return this.intent;
	}
	public void setIntent(String intent){
		this.intent = intent;
	}
}
