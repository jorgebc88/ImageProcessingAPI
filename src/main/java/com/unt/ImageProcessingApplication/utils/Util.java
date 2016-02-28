package com.unt.ImageProcessingApplication.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Mat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unt.ImageProcessingApplication.entities.Camera;

public class Util {

	/**
	 * This method convert an Object to json
	 * @param data Object to be transformed in json
	 * @return Object transformed to json
	 */
	public static String toJson(Object data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * This method transform a Camera object in json to Camera
	 * @param data json of the camera
	 * @return A list of cameras
	 */
	public static ArrayList<Camera> cameraListFromJson(String data) {
		ArrayList<Camera> cameras = new ArrayList<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			cameras = objectMapper.readValue(data,
					objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Camera.class));

		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cameras;
	}

	/**
	 * This method verify if the detected object is an Object according to the object size and the frame size
	 * @param objectSize Size of the detected object
	 * @param frameSize Size of the frame
	 * @return True if it is a Object, False if not
	 */
	public static boolean isAnObject(double objectSize, double frameSize) {
		if (objectSize > (0.006 * frameSize)) {
			return true;
		}
		return false;
	}

	/**
	 * This method verify if the detected object is a Bike according to the object size and the frame size
	 * @param objectSize Size of the detected object
	 * @param frameSize Size of the frame
	 * @return True if it is a Bike, False if not
	 */
	public static boolean isABike(double objectSize, double frameSize) {
		if (objectSize < (0.05 * frameSize)) {
			return true;
		}
		return false;
	}

	/**
	 * This method verify if the detected object is a Car according to the object size and the frame size
	 * @param objectSize Size of the detected object
	 * @param frameSize Size of the frame
	 * @return True if it is a Car, False if not
	 */
	public static boolean isACar(double objectSize, double frameSize) {
		if (objectSize < (0.18 * frameSize)) {
			return true;
		}
		return false;
	}

	/**
	 * This method verify if the detected object is a Bus according to the object size and the frame size
	 * @param objectSize Size of the detected object
	 * @param frameSize Size of the frame
	 * @return True if it is a Bus, False if not
	 */
	public static boolean isABus(double objectSize, double frameSize) {
		if (objectSize > (0.18 * frameSize)) {
			return true;
		}
		return false;
	}

	/**
	 * This method convert a Mat Frame into a BufferedImage
	 * @param mat Frame to convert
	 * @return The frame converted to BuferredImage
	 */
	public static BufferedImage createBufferedImage(Mat mat) {
		int type = 0;
		if (mat.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (mat.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		} else {
			return null;
		}

		BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		mat.get(0, 0, data);

		return image;
	}

	/**
	 * This method calculate the direction of the detected objects
	 * @param cameraPointingAt The direction pointed by the camera
	 */
	public static String calculateDetectedObjectDirection(String cameraPointingAt) {
		if(cameraPointingAt == null){
			throw new RuntimeException("The camera must have the field pointing_at.");
		}
		switch (cameraPointingAt.trim().toLowerCase()){
			case "north" : return "South";
			case "west" : return "East";
			case "south" : return "North";
			case "east" : return "South";
			default: return "South";
		}
	}

}
