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

    public String get_text() {
        return this._text;
    }

    public void set_text(String _text) {
        this._text = _text;
    }

    public String getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public List<WitOutcomes> getOutcomes() {
        return this.outcomes;
    }

    public void setOutcomes(List<WitOutcomes> outcomes) {
        this.outcomes = outcomes;
    }

    public ArrayList<String> getIntent() {

        ArrayList<String> intents = new ArrayList<String>();

        for (WitOutcomes o : this.getOutcomes()) {
            intents.add(o.getIntent());
        }
        return intents;
    }

    public String getEntities() {

        ArrayList<String> entities = new ArrayList<String>();

//            for (WitOutcomes o : getOutcomes()) {
//
//                for (WitEntities e : o.getEntities()) {
//
//                    for (WitNumber n : e.getNumber()) {
//
//                        entities.add(n.getValue());
//
//                    }
//
//                }
//            }

//        return entities;

        return this.getOutcomes().get(0).getEntities().getNumber().get(0).getValue();

    }



}
