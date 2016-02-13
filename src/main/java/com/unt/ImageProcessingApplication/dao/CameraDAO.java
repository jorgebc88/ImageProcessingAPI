package com.unt.ImageProcessingApplication.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import com.unt.ImageProcessingApplication.entities.Camera;
import com.unt.ImageProcessingApplication.utils.Util;

public class CameraDAO {

	public static List<Camera> getCameraList() throws Exception {

		HttpURLConnection conn = prepareHttpURLConnection();

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
		return Util.cameraListFromJson(sb.toString());
	}

	private static HttpURLConnection prepareHttpURLConnection()
			throws MalformedURLException, IOException, ProtocolException {

		String urlStr = "http://localhost:8080/FinalProject/camera/list";

		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type", "application/json");
		return conn;
	}

}
