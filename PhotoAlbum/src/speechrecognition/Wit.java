package speechrecognition;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;


public class Wit {

	private static final String TOKEN = "TD4IHRD3ANAK2YEMCJVN4UIL7RZWH3P4";
	private static final long RECORD_TIME = 3500;
	
	public static HttpResponse getSpeech(File audioFile, String fileType) throws Exception {

		// Speech recognition

		HttpClient client = HttpClients.createDefault();

		// URL for speech recognition
		URI urlSpeech = new URIBuilder("https://api.wit.ai/speech?v=20141201")
				.build();
		HttpPost post = new HttpPost(urlSpeech);

		InputStreamEntity reqEntity = new InputStreamEntity(
				new FileInputStream(audioFile), -1,
				ContentType.APPLICATION_OCTET_STREAM);
		reqEntity.setChunked(true);

		post.setHeader("Authorization", "Bearer " + TOKEN);
		post.setHeader("Content-Type", "audio/"+fileType);
		post.setEntity(reqEntity);

		long a = System.currentTimeMillis();
		

		// Get the data from wit.AI
		HttpResponse speechResponse = client.execute(post);

		long b = System.currentTimeMillis();
		
		System.out.println(b - a);
		
		
		return speechResponse;
	}

	public static HttpResponse getMeaning(String string) throws Exception {

		URI urlText = new URIBuilder(
				"https://api.wit.ai/message?v=20140916&q=remove").build();

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(urlText);
		request.setHeader("Authorization", "Bearer " + TOKEN);

		// Get the data from wit.AI
		CloseableHttpResponse meaningResponse = client.execute(request);
		
		return meaningResponse;

	}

	public static void main(String[] args) throws Exception {

		// Specify the location of the audio file
		File audioFileWav = new File("recording.wav");
		File audioFileMp3 = new File("recording.mp3");
		
		
        final Record recorder = new Record(audioFileWav);
        
        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
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
        

		HttpResponse speechResponse = getSpeech(audioFileWav, "wav");
		String speechResponseString = new BasicResponseHandler()
				.handleResponse(speechResponse);
		System.out.println(speechResponseString);

//		String remove1 = "Remove";
		
//		HttpResponse meaningResponse = getMeaning(remove1);
//		String meaningResponseString = new BasicResponseHandler()
//				.handleResponse(meaningResponse);
//		System.out.println(meaningResponseString);

	}
	
}
