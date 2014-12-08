package speechrecognition;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.File;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;

public class WitTest {
	
	public static final int recordTime = 4000;
	public static void main(String[] args) throws Exception {

		// Specify the location of the audio file
		File audioFileWav = new File("recording.wav");
		File audioFileMp3 = new File("Vocaroo_s1zIdkevNvhl.mp3");

		final Record recorder = new Record(audioFileWav);

		// creates a new thread that waits for a specified
		// of time before stopping
		Thread stopper = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(recordTime);
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
		//recorder.convertWavToMp3(audioFileWav, audioFileMp3);

		HttpResponse speechResponse = Wit.getSpeech(audioFileWav, "wav");
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

		System.out.println("It was recognized with a confidence of " + response.getOutcomes().get(0).getConfidence());

		System.out.println("Your intent was " + response.getOutcomes().get(0).getIntent());
		
		try {
			System.out.println("The first entity was " + response
					.getOutcomes()
					.get(0)
					.getEntities()
					.getNumber()
					.get(0)
					.getValue());
		} catch (Exception e) {
//			System.out.println("No entity");
		}
		
		try {
			System.out.println("The second entity was " + response
					.getOutcomes()
					.get(0)
					.getEntities()
					.getNumber()
					.get(1)
					.getValue());
		} catch (Exception e) {
//			System.out.println("No entity");
		}
		speechResponse = Wit.getSpeech(audioFileMp3, "mpeg3");
		speechResponseString = new BasicResponseHandler()
				.handleResponse(speechResponse);
	System.out.println("The raw text was " + speechResponseString);
		
		
		response = mapper.readValue(speechResponseString, WitResponse.class);
		System.out.println("The recognized text was " + response.get_text());

		System.out.println("It was recognized with a confidence of " + response.getOutcomes().get(0).getConfidence());

		System.out.println("Your intent was " + response.getOutcomes().get(0).getIntent());
		
		try {
			System.out.println("The first entity was " + response
					.getOutcomes()
					.get(0)
					.getEntities()
					.getNumber()
					.get(0)
					.getValue());
		} catch (Exception e) {
//			System.out.println("No entity");
		}
		
		try {
			System.out.println("The second entity was " + response
					.getOutcomes()
					.get(0)
					.getEntities()
					.getNumber()
					.get(1)
					.getValue());
		} catch (Exception e) {
//			System.out.println("No entity");
		}
	}
	
//	HttpResponse meaningResponse = getMeaning(remove1);
//	String meaningResponseString = new BasicResponseHandler()
//			.handleResponse(meaningResponse);
//	System.out.println(meaningResponseString);
	
}
