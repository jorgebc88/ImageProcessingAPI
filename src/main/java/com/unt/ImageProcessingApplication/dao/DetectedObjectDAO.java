package com.unt.ImageProcessingApplication.dao;

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

import com.unt.ImageProcessingApplication.entities.DetectedObject;
import com.unt.ImageProcessingApplication.utils.objectUtils.ObjectUtils;

public class DetectedObjectDAO {

	public static String saveDetectedObject(String detectedObjectDirection, String type, Date date, long cameraId) throws Exception {

		DetectedObject detectedObject = new DetectedObject(detectedObjectDirection, type, date, cameraId);

		HttpURLConnection conn = prepareHttpURLConnection();

		// Create the form content
		OutputStream out = conn.getOutputStream();
		Writer writer = new OutputStreamWriter(out, "UTF-8");
		writer.write(ObjectUtils.toJson(detectedObject));
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

		String urlStr = "http://localhost:8080/REST-API/detectedObject/DetectedObject";

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

}
