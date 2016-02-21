package com.unt.ImageProcessingApplication.Processing;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.opencv.core.Core;

import java.net.ConnectException;

public class Application {
	static final Logger LOGGER = Logger.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BasicConfigurator.configure();

		try {
			ApplicationController.start();
		} catch (ConnectException e) {
			LOGGER.info("There was a problem trying to connect to the REST-API! Make sure that tie REST-API is up and running. If you verify that and continue having this error please Call Technical Support!");
		} catch (Exception e) {
			LOGGER.info("There was a problem initialization the application! Please Call Technical Support!");
			e.printStackTrace();
		}
	}
}
