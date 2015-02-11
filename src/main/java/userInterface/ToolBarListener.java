package main.java.userInterface;


import main.java.speechrecognition.Wit;


/**
 * Created by pold on 12/11/14.
 */
public interface ToolBarListener{
    public void recognizedText(String text);
    public void recognizedWitResponse(Wit response) throws Exception;
    public void toolbarButtonClicked(String action) throws Exception;
}
