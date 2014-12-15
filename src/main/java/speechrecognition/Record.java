package main.java.speechrecognition;

//import it.sauronsoftware.jave.AudioAttributes;
//import it.sauronsoftware.jave.Encoder;
//import it.sauronsoftware.jave.EncodingAttributes;

import javax.sound.sampled.*;

import java.io.*;

/**
 * A sample program is to demonstrate how to record sound in Java author:
 * www.codejava.net
 * For determining the end of an utterance, we could have a look at:
 * http://stackoverflow.com/questions/5800649/detect-silence-when-recording
 */
public class Record {
	// record duration, in milliseconds
	static final long RECORD_TIME = 6000; // 1 minute

	// path of the wav file
	File wavFile;

	// format of audio file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	// the line from which audio data is captured
	TargetDataLine line;

	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
        

    }
	
	// Constructor (one needs to specify the file)
    public Record(File wavFile) {
		this.wavFile = wavFile;
	}    

	/**
	 * Captures the sound and record into a WAV file
	 */
	public void start() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start(); // start capturing

			System.out.println("Start capturing...");

			AudioInputStream ais = new AudioInputStream(line);

			System.out.println("Start recording...");

			// start recording
			AudioSystem.write(ais, fileType, wavFile);

		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void recordExtern(File file) throws IOException, InterruptedException {

		Process p = new ProcessBuilder("rec", "-r", "8000", "-c", "2", "-t", "wav", file.toString(), "trim", "0", Long.toString(RECORD_TIME / 1000)).start();
		p.waitFor();
	}

	/**
	 * Closes the target data line to finish capturing and recording
	 */
	void finish() {
		line.stop();
		line.close();
		System.out.println("Finished");
	}

	public void convertWavToMp3External(File source, File target) throws Exception {

		Process p = new ProcessBuilder("ffmpeg", "-i", source.getName(), "-codec:a", "libmp3lame", "-qscale:a", "2", target.getName()).start();
		p.waitFor();

	}

//	public void convertWavToMp3(File source, File target) throws Exception {
//		AudioAttributes audio = new AudioAttributes();
//		audio.setCodec("libmp3lame");
//		audio.setBitRate(new Integer(128000));
//		audio.setChannels(new Integer(2));
//		audio.setSamplingRate(new Integer(44100));
//		EncodingAttributes attrs = new EncodingAttributes();
//		attrs.setFormat("mp3");
//		attrs.setAudioAttributes(audio);
//		Encoder encoder = new Encoder();
//		encoder.encode(source, target, attrs);
//	}

}