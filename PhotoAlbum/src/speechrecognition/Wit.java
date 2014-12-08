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
	private static final long RECORD_TIME = WitTest.recordTime;
	
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

		System.out.println("filetype: " + fileType);
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
	
}
