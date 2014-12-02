package speechrecognition;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.File;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;

public class WitTest {
	public static void main(String[] args) throws Exception {

		// Specify the location of the audio file
		File audioFileWav = new File("recording.wav");
//		File audioFileMp3 = new File("recording.mp3");

		final Record recorder = new Record(audioFileWav);

		// creates a new thread that waits for a specified
		// of time before stopping
		Thread stopper = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				recorder.finish();
			}
		});

		stopper.start();

		// start recording
		recorder.start();

		// Convert Wav to Mp3
		// recorder.convertWavToMp3(audioFileWav, audioFileMp3);

		HttpResponse speechResponse = Wit.getSpeech(audioFileWav);
		String speechResponseString = new BasicResponseHandler()
				.handleResponse(speechResponse);

		WitResponse response = null;

		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

		// IMPORTANT
		// without this option set adding new fields breaks old code
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		
		System.out.println("The raw text was " + speechResponseString);
		
		
		response = mapper.readValue(speechResponseString, WitResponse.class);
		System.out.println("The recognized text was " + response.get_text());

		response = mapper.readValue(speechResponseString, WitResponse.class);
		System.out.println("It was recognized with a confidence of " + response.getOutcomes().get(0).getConfidence());

		response = mapper.readValue(speechResponseString, WitResponse.class);
		System.out.println("Your intent was " + response.getOutcomes().get(0).getIntent());
	}
}
