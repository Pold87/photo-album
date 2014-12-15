package main.java.userInterface;

import main.java.speechrecognition.WitResponse;

import java.awt.event.ActionListener;

/**
 * Created by pold on 12/11/14.
 */
public interface ToolBarListener{
    public void recognizedText(String text);
    public void recognizedWitResponse(WitResponse response);
    public void whichAction(String action);

}
