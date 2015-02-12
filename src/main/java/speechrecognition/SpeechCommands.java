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

	public static final String ACOUSTIC_MODEL = "resource:/voxforge";
	public static final String DICTIONARY_PATH = "resource:/voxforge/cmudict.0.6d";
	public static final String GRAMMAR_PATH = "resource:/voxforge";
	public static final String GRAMMAR_NAME = "commands";


	private Configuration configuration = new Configuration();
	private LiveSpeechRecognizer commandRecognizer;

	public SpeechCommands() throws IOException {

		// Set acoustic model
		this.configuration.setAcousticModelPath(ACOUSTIC_MODEL);

		// This is the in-built acoustic model
		// configuration.setAcousticModelPath("resource:/WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz");

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

		System.out.println(utterance.getNbest(3));

		return utterance;

	}

    public static void main(String[] args) throws IOException {

        SpeechCommands sc = new SpeechCommands();

        sc.recognizeCommand();

    }

}