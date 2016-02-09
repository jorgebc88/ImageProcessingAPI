package com.proyectoFinal2.ProyectoFinal2;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.opencv.core.Core;

import dataBaseConnection.Camera;
import dataBaseConnection.CameraController2;

public class ObjRecognition {

	static final Logger LOGGER = Logger.getLogger(ObjRecognition.class);

	public static void main(String[] args) throws Exception {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BasicConfigurator.configure();
		List<Camera> cameras = new ArrayList<Camera>();
		ObjRecognitionController controller = new ObjRecognitionController();
		cameras = CameraController2.getCameraList();
		while (cameras.isEmpty()){
			Thread.sleep(5000);
			cameras = CameraController2.getCameraList();
		}
		
		for(Camera camera : cameras){
			controller.startCamera(camera);
			LOGGER.info(camera);

		}
	}
}
