package dataBaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DetectedObjectController {

	public static String saveDetectedObject(String type, Date date, long cameraId) throws Exception {

		DetectedObject detectedObject = null;
		detectedObject = new DetectedObject("South", type, date, cameraId);

		HttpURLConnection conn = prepareHttpURLConnection();

		// Create the form content
		OutputStream out = conn.getOutputStream();
		Writer writer = new OutputStreamWriter(out, "UTF-8");
		writer.write(toJson(detectedObject));
		writer.close();
		out.close();

		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		return sb.toString();
	}

	private static HttpURLConnection prepareHttpURLConnection()
			throws MalformedURLException, IOException, ProtocolException {

		String urlStr = "http://localhost:8080/FinalProject/detectedObject/DetectedObject";

		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type", "application/json");
		return conn;
	}

	public static String toJson(Object data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static boolean isAnObject(double objectSize, double frameSize) {
		if (objectSize > (0.006 * frameSize)) {
			return true;
		}
		return false;
	}

	public static boolean isABike(double objectSize, double frameSize) {
		if (objectSize < (0.05 * frameSize)) {
			return true;
		}
		return false;
	}
	
	public static boolean isACar(double objectSize, double frameSize) {
		if (objectSize < (0.18 * frameSize)) {
			return true;
		}
		return false;
	}

	public static boolean isABus(double objectSize, double frameSize) {
		if (objectSize > (0.18 * frameSize)) {
			return true;
		}
		return false;
	}

}
