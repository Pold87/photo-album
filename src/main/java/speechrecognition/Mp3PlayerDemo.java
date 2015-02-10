package main.java.speechrecognition;

/**
 * Created by pold on 2/8/15.
 */
import javax.media.*;
import java.net.URL;

public class Mp3PlayerDemo extends Thread {

    private String filename;
    Player player;

    public Mp3PlayerDemo(String mp3Filename) {
        this.filename = mp3Filename;
    }

    public void run() {
        try {
            URL url = this.getClass().getResource(filename);
            MediaLocator locator = new MediaLocator(url);
            player = Manager.createPlayer(locator);
            player.addControllerListener(new ControllerListener() {
                public void controllerUpdate(ControllerEvent event) {
                    if (event instanceof EndOfMediaEvent) {
                        player.stop();
                        player.close();
                    }
                }
            });
            player.realize();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Mp3PlayerDemo("/main/java/mysound.mp3").start();
    }
}