package main.java.speechrecognition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.leapmotion.leap.Controller;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import main.java.userInterface.App;
import main.java.userInterface.DebugPanel;
import main.java.userInterface.OurController;

/**
 * The speech recognition class. It is composed of two parts:
 * <ul>
 * <li>A grammar for specific commands</i>
 * <li>A language model</li>
 * </ul>
 */

/*
 * TODO:
 * 
 * - Create configuration file - Tune continuous speech recognition
 */

public class SpeechCommands implements Runnable {

    public static final String ACOUSTIC_MODEL = "resource:/voxforge/acousticmodel";
    public static final String DICTIONARY_PATH = "resource:/voxforge/cmudict.0.6d";
    public static final String GRAMMAR_PATH = "resource:/voxforge";
    public static final String GRAMMAR_NAME = "commands";

    private Configuration configuration = new Configuration();
    private StreamSpeechRecognizer commandRecognizer;

    private String[] intent = new String[2];

    private final Set<SimpleSpeechThreadCompleteListener> listeners
            = new CopyOnWriteArraySet<>();

    public final void addListener(final SimpleSpeechThreadCompleteListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final SimpleSpeechThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners() {


        for (SimpleSpeechThreadCompleteListener listener : listeners) {
            System.out.println(listener);
            System.out.println(this);
            System.out.println(this.intent[0]);
            System.out.println(this.intent[1]);
            listener.notifyOfSimpleSpeech(this);

        }

        this.intent[0] = "unknown";
    }

    public String[] getIntent() {
        return intent;
    }

    public SpeechCommands() throws IOException {

        // Set acoustic model
        this.configuration.setAcousticModelPath(ACOUSTIC_MODEL);

        // This is the in-built acoustic model
//		configuration.setAcousticModelPath("resource:/WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz");

        // Set dictionary path
        this.configuration.setDictionaryPath(DICTIONARY_PATH);

        // Set grammar (for commands) language model
        this.configuration.setUseGrammar(true);
        this.configuration.setGrammarName(GRAMMAR_NAME);
        this.configuration.setGrammarPath(GRAMMAR_PATH);


        // Create new LiveSpeechRecognizer (for commands)
        this.commandRecognizer = new StreamSpeechRecognizer(this.configuration);

    }

    // Recognize a command
    public void recognizeCommand() {

        InputStream stream = SpeechCommands.class.getResourceAsStream("/recording.wav");


//        InputStream stream = App.class.getResourceAsStream("/recording.wav");


        this.commandRecognizer.startRecognition(stream);

        SpeechResult result;


        while ((result = commandRecognizer.getResult()) != null) {
            String hypo = result.getHypothesis();
            this.parseCommand(hypo);
            break;
        }

        this.commandRecognizer.stopRecognition();

    }

    public void parseCommand(String hypo) {

        System.out.println("Yep");

        String[] words;
        String intention = "unknown";
        String val = "default";
        if (!hypo.equals("<unk>")) {
            words = hypo.trim().split("\\s+");
            for (String s : words) {
                switch (s) {
                    case "ten":
                        val = "10";
                        break;
                    case "eleven":
                        val = "11";
                        break;
                    case "twelve":
                        val = "12";
                        break;
                    case "thirteen":
                        val = "11";
                        break;
                    case "fourteen":
                        val = "14";
                        break;
                    case "fifteen":
                        val = "15";
                        break;
                    case "sixteen":
                        val = "16";
                        break;
                }
            }
            for (String w : words) {
                switch (w) {
                    case "add":
                        intention = "add";
                        break;
                    case "select":
                    case "choose":
                    case "activate":
                    case "pick":
                    case "use":
                        intention = "select";
                        break;
                    case "move":
                    case "shift":
                    case "relocate":
                    case "position":
                        intention = "move";
                        break;
                    case "delete":
                    case "remove":
                    case "trashcan":
                    case "garbage":
                    case "bin":
                        intention = "remove";
                        break;
                    case "rotate":
                    case "turn":
                    case "spin":
                    case "tilt":
                    case "orientation":
                        intention = "rotate";
                        break;
                    case "enlarge":
                    case "increase":
                    case "larger":
                    case "bigger":
                        intention = "enlarge";
                        break;
                    case "decrease":
                    case "reduce":
                    case "shrink":
                    case "smaller":
                        intention = "smaller";
                        break;
                    case "redo":
                    case "forward":
                        intention = "redo";
                        break;
                    case "undo":
                    case "back":
                        intention = "undo";
                        break;
                    case "black":
                    case "blue":
                    case "red":
                    case "yellow":
                    case "white":
                    case "orange":
                    case "green":
                    case "pink":
                    case "magenta":
                    case "cyan":
                        intention = "background";
                        val = w;
                        break;
                }
            }
        }

        this.intent[0] = intention;
        this.intent[1] = val;
    }

    @Override
    public void run() {

        try {
            this.recognizeCommand();
        } finally {
            notifyListeners();
        }

    }
}