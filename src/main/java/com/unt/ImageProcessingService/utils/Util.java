package com.unt.ImageProcessingService.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Mat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unt.ImageProcessingService.entities.Camera;

public class Util {

	public static String toJson(Object data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		}
	}

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

}
