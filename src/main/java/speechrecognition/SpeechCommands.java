package main.java.speechrecognition;

import java.io.IOException;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

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

public class SpeechCommands {

	public static final String ACOUSTIC_MODEL = "resource:/voxforge/acousticmodel/";
	public static final String DICTIONARY_PATH = "resource:/voxforge/cmudict.0.6d";
	public static final String GRAMMAR_PATH = "resource:/voxforge";
	public static final String GRAMMAR_NAME = "commands";

	private Configuration configuration = new Configuration();
	private LiveSpeechRecognizer commandRecognizer;

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
		this.commandRecognizer = new LiveSpeechRecognizer(this.configuration);
	}

	// Recognize a command
	public SpeechResult recognizeCommand() {
		System.out.println("Command recognition (using grammar)");

		this.commandRecognizer.startRecognition(true);
		SpeechResult utterance = this.commandRecognizer.getResult();
		this.commandRecognizer.stopRecognition();

		System.out.println(utterance.getHypothesis());

		return utterance;

	}
	public String[] parseCommand(String hypo) {
		String[] words;
		String intention = "unknown";
		String val = "default";
		String[] intent = {intention,val};
		if (!hypo.equals("<unk>")){
			words = hypo.trim().split("\\s+");
			for (String s : words) {
				System.out.println(s);
			}
			for (String w : words) {
				switch (w) {
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
						intention = "delete";
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
		intent[0] = intention;
		intent[1] = val;
		return intent;
	}

    public static void main(String[] args) throws IOException {

		SpeechCommands sc = new SpeechCommands();
		SpeechResult result;

		while ((result = sc.recognizeCommand()) != null) {
			String hypo = result.getHypothesis();
			System.out.format("Hypothesis: %s\n", hypo);
			String[] intent = sc.parseCommand(hypo);
			System.out.format("Intention: %s\n",intent[0]);
			System.out.format("Value: %s\n", intent[1]);

		}

	}

}