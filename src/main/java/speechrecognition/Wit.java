package main.java.speechrecognition;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;


public class Wit implements Runnable {

    private static final String TOKEN = "TD4IHRD3ANAK2YEMCJVN4UIL7RZWH3P4";
    private String witRawJSONString;
    private File audioFileWav;
    private String fileType;

    private final Set<WitThreadCompleteListener> listeners
            = new CopyOnWriteArraySet<WitThreadCompleteListener>();

    public Wit(File audioFileWav, String fileType) throws Exception {

        this.audioFileWav = audioFileWav;
        this.fileType = fileType;
    }


    public final void addListener(final WitThreadCompleteListener listener) {
        listeners.add(listener);
    }
    public final void removeListener(final WitThreadCompleteListener listener) {
        listeners.remove(listener);
    }
    private final void notifyListeners() {
        for (WitThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }


    public String getWitRawJSONString() {
        return witRawJSONString;
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

    // Using the integrated JSON framework
    public String getIntent() {


        JsonReader reader = Json.createReader(new StringReader(this.witRawJSONString));
        JsonObject jsonObject = reader.readObject();

        JsonArray outcomes = jsonObject.getJsonArray("outcomes");

        // TODO: Could be done better than with try ... catch
        try {
            return outcomes.getJsonObject(0).getString("intent");
        } catch (Exception e) {
            return "No intent";
        }
    }

    private JsonObject getEntities() {

        JsonReader reader = Json.createReader(new StringReader(this.witRawJSONString));
        JsonObject jsonObject = reader.readObject();

        JsonArray outcomes = jsonObject.getJsonArray("outcomes");

        try {
            return outcomes.getJsonObject(0).getJsonObject("entities");
        } catch (Exception e) {
            return null;
        }
    }

    private void askWit() throws IOException, URISyntaxException {

        // Speech recognition
        HttpClient client = HttpClients.createDefault();

        // URL for speech recognition
        URI urlSpeech = new URIBuilder("https://api.wit.ai/speech?v=20141201")
                .build();
        HttpPost post = new HttpPost(urlSpeech);

        InputStreamEntity reqEntity = new InputStreamEntity(
                new FileInputStream(audioFileWav), -1,
                ContentType.APPLICATION_OCTET_STREAM);
        reqEntity.setChunked(true);

        post.setHeader("Authorization", "Bearer " + TOKEN);
        post.setHeader("Content-Type", "audio/" + fileType);
        post.setEntity(reqEntity);

//        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
//        requestConfigBuilder.setSocketTimeout(6000);
//        requestConfigBuilder.setConnectionRequestTimeout(6000);
//        requestConfigBuilder.setConnectTimeout(6000);
//
//        RequestConfig requestConfig = requestConfigBuilder.build();
//
//        post.setConfig(requestConfig);

        System.out.println("filetype: " + fileType);
        long a = System.currentTimeMillis();

        // Get the data from wit.AI

            HttpResponse speechResponse = null;
            try {
                speechResponse = client.execute(post);
            } catch (IOException e) {
//                e.printStackTrace();

                System.out.println("Could not connect to wit");

            }

            long b = System.currentTimeMillis();
            System.out.println(b - a);

            // Extract String from HttpResponse.
            String speechResponseString = null;
            try {
                speechResponseString = new BasicResponseHandler()
                        .handleResponse(speechResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.witRawJSONString = speechResponseString;

    }


    /**
     * Shift extracted numbers by 10. See if it's better to use 10 as a parameter,
     * so that other shifts can be used as well.
     * @return
     */

    public ArrayList<Integer> extractNumbersShifted() {

        ArrayList<Integer> numbers = this.extractNumbers();
        ArrayList<Integer> shiftedNumbers = new ArrayList<>();

        for (Integer i : numbers) {
            shiftedNumbers.add(i - 10);
        }

        return shiftedNumbers;

    }

    /**
     * Extract the Integer values in a JSON object
     * @return
     */
    public ArrayList<Integer> extractNumbers() {

        ArrayList<Integer> imageNumbers = new ArrayList<>();

        if (this.getEntities().getJsonArray("number") != null) {

            JsonObject entities = this.getEntities();
            JsonArray jsonArray = entities.getJsonArray("number");

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject obj = jsonArray.getJsonObject(i);
                int value = obj.getInt("value");
                imageNumbers.add(value);

            }
        }
            return imageNumbers;
    }


    public ArrayList<Integer> extractWrongNumbersShifted() {


        HashMap<String, Integer> assignments = new HashMap<>();

        assignments.put("to self", 12);
        assignments.put("taken", 10);
        assignments.put("elf", 12);
        assignments.put("tells", 12);
        assignments.put("teen", 14);
        assignments.put("den", 10);
        assignments.put("hen", 10);


        ArrayList<Integer> imageNumbers = new ArrayList<>();

        JsonArray wrongnumbers = this.getEntities().getJsonArray("wrongnumber");

        if (wrongnumbers != null) {

            for (int i = 0; i < wrongnumbers.size(); i++) {

                JsonObject obj = wrongnumbers.getJsonObject(i);
                String value = obj.getString("value");

                if (isInteger(value)) {
                    imageNumbers.add(Integer.parseInt(value) - 10);
                } else if (assignments.containsKey(value)) {
                    imageNumbers.add(assignments.get(value) - 10);
                }

            }
        }
        return imageNumbers;
    }


    /**
     * Extract Color from Wit response
     * @return
     */
    public Color getBackgroundColor() {

        JsonObject entities = this.getEntities();
        Color color;
        JsonArray jsonArray = entities.getJsonArray("color");


        if (!jsonArray.isEmpty()) {
            JsonObject jsonObject = jsonArray.getJsonObject(0);
            String colorString = jsonObject.getString("value").toLowerCase();
            color = this.stringToColor(colorString);
        } else {
           color = null;
        }

        return color;
    }

    public Color stringToColor(String colorString) {
        // Convert String to color
        Color color;

        try {
            Field field = Class.forName("java.awt.Color").getField(colorString);
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null;
            System.out.println("Unknown colour: " + colorString);
        }

        return color;
    }


    // from http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }


    @Override
    public void run() {
        try {
            this.askWit();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            notifyListeners();
        }
    }



}
