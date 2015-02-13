package main.java.speechrecognition;

//import it.sauronsoftware.jave.AudioAttributes;
//import it.sauronsoftware.jave.Encoder;
//import it.sauronsoftware.jave.EncodingAttributes;

import main.java.userInterface.OurController;

import javax.sound.sampled.*;

import java.io.*;

/**
 * A sample program is to demonstrate how to record sound in Java author:
 * www.codejava.net
 * For determining the end of an utterance, we could have a look at:
 * http://stackoverflow.com/questions/5800649/detect-silence-when-recording
 */
public class Recorder implements Runnable{


    volatile boolean finished = false;
	static final long RECORD_TIME = 6000; // Record duration, in milliseconds
	File wavFile; // Path of the wav file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE; // Format of audio file
	TargetDataLine line; // The line from which audio data is captured
    private volatile boolean running = true;

	public Recorder(File wavFile) {
		this.wavFile = wavFile;
	}

	/**
	 * Defines an audio format
	 */
	private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }

    public void terminate() {
        running = false;
    }

    public void run() {
        while (running) {
            this.start_rec();
        }
    }

	/**
     * Captures the sound and record into a WAV file
	 */
	public void start_rec() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			this.line = (TargetDataLine) AudioSystem.getLine(info);
			this.line.open(format);
			this.line.start(); // start capturing

			System.out.println("Start capturing...");

			AudioInputStream ais = new AudioInputStream(this.line);

			System.out.println("Start recording...");

			// start recording
			System.out.println("Path of wav File is " + wavFile.getAbsolutePath());
			AudioSystem.write(ais, fileType, wavFile);

		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}


	/**
	 * Closes the target data line to finish capturing and recording
	 */
	public void finish() {
        this.finished = true;
        System.out.println("Started to finish");
        this.line.stop();
        this.line.close();
        System.out.println("Finished");
    }

	public static void recordExtern(File file) throws IOException, InterruptedException {

		Process p = new ProcessBuilder("sox", "-d", "-r", "8000", "-c", "2", "-t", "wav", file.toString(), "trim", "0", Long.toString(RECORD_TIME / 1000)).start();
		p.waitFor();
	}

}