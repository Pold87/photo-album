package main.java.speechrecognition;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by pold on 2/8/15.
 */
public class Production {


    private static final String FILEPATH = "/home/pold/Documents/Radboud/Java/photo-album/mysound.mp3";

    public void tts(String utterance) throws URISyntaxException {
        try{

            utterance = java.net.URLEncoder.encode(utterance, "UTF-8");
            URL url = new URL("http://translate.google.com/translate_tts?tl=en&q=" + utterance);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.76");
            InputStream audioSrc = urlConn.getInputStream();
            DataInputStream read = new DataInputStream(audioSrc);

            String pathName = "mysound.mp3";

            OutputStream outstream = new FileOutputStream(pathName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = read.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void playMp3(String pathName) {

        try{

            URL path = getClass().getResource(pathName);

            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(path.openStream());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            do {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } while (clip.isActive());

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void play() throws Exception {

        Process p = new ProcessBuilder("mpg123", FILEPATH).start();
        p.waitFor();

    }

}
