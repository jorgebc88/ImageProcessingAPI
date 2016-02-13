package com.unt.ImageProcessingService.Processing;

import org.apache.log4j.BasicConfigurator;
import org.opencv.core.Core;

public class Application {

	public static void main(String[] args) throws Exception {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BasicConfigurator.configure();

		try {
			ApplicationController.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
