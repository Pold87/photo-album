package main.java.speechrecognition;

import java.awt.Toolkit;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cmu.sphinx.api.*;
import edu.cmu.sphinx.result.ConfidenceResult;
import edu.cmu.sphinx.result.Lattice;
import edu.cmu.sphinx.result.MAPConfidenceScorer;

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

	private static final String ACOUSTIC_MODEL = "resource:/speechrecognition/voxforge";
	private static final String DICTIONARY_PATH = "resource:/speechrecognition/voxforge/cmudict.0.6d";
	private static final String GRAMMAR_PATH = "resource:/speechrecognition/";
	private static final String GRAMMAR_NAME = "commands";
	private static final String LANGUAGE_MODEL = "resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp";
	
	// Recognize a command
	private static SpeechResult recognizeCommand(LiveSpeechRecognizer recognizer) {
		System.out.println("Command recognition (using grammar)");
		// System.out.println("--------------------------------");
		// System.out.println("Example: select one");
		// System.out.println("Say \"exit\" to exit");
		// System.out.println("--------------------------------");

		recognizer.startRecognition(true);
		SpeechResult utterance = recognizer.getResult();
		//MAPConfidenceScorer scorer = new MAPConfidenceScorer();
		//ConfidenceResult confidence = scorer.score(recognizer.getResult().);
		// Pause recognition process. It can be resumed then with
		// startRecognition(false).
		recognizer.stopRecognition();
		return utterance;
	}

	// Recognize a description
	private static SpeechResult recognizeDescription(LiveSpeechRecognizer recognizer) {
		System.out
				.println("Continuous speech recognition (using language model)");
		// System.out.println("--------------------------------");
		// System.out.println("Example: This is a very cool picture!");
		// System.out.println("Say \"exit\" to exit");
		// System.out.println("--------------------------------");

		recognizer.startRecognition(true);
		System.out.println("Description listening");
		SpeechResult utterance = recognizer.getResult();
		recognizer.stopRecognition();
		return utterance;
	}

	public static void main(String[] args) throws Exception {


		Configuration configuration = new Configuration();

		// Set acoustic model
		configuration.setAcousticModelPath(ACOUSTIC_MODEL);

		// This is the in-built acoustic model
		// configuration.setAcousticModelPath("resource:/WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz");

		// Set dictionary path
		configuration.setDictionaryPath(DICTIONARY_PATH);

		// Set grammar (for commands) language model
		configuration.setUseGrammar(true);
		configuration.setGrammarName(GRAMMAR_NAME);
		configuration.setGrammarPath(GRAMMAR_PATH);
		
//		Context context = new Context("file:/home/pold/Documents/Radboud/Java/photo-album/custom.config.xml", configuration);
//		context.setLocalProperty("logLevel", "WARNING");
	
	
		// Set language model (for continuous speech)
		configuration.setLanguageModelPath(LANGUAGE_MODEL);

		// Create new LiveSpeechRecognizer (for commands)
		LiveSpeechRecognizer commandRecognizer = new LiveSpeechRecognizer(
				configuration);

		// Create new LiveSpeechRecognizer (for descriptions, i.e. continuous
		// speech)
		configuration.setUseGrammar(false);
		LiveSpeechRecognizer descriptionRecognizer = new LiveSpeechRecognizer(
				configuration);

		System.out.println("Recognition started");

		// Play a sound (TODO: I can't hear anything)
		Toolkit.getDefaultToolkit().beep();

		// Get command and print to command line
		SpeechResult cmd = null;
		while (true) {
			cmd = recognizeCommand(commandRecognizer);
			System.out.println("Hypothesis" + cmd.getHypothesis());
			System.out.println("N best:" + cmd.getNbest(2));
			Lattice lattice = cmd.getLattice();
			System.out.println("Lattice: " + lattice.getNodes());
			System.out.println("Lattice: " + lattice.getEdges());
			//lattice.computeNodePosteriors(0);
			//System.out.println("Lattice: " + lattice.getViterbiPath());
			System.out.println("Words: " + cmd.getWords());
		}
	}
}