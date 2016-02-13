package com.unt.ImageProcessingApplication.Processing;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

class ThreadServerConnectionHandler extends Thread {

	private static Logger LOGGER = Logger.getLogger(ThreadServerConnectionHandler.class);

	private String boundary;
	private ServerSocket ss;
	private ImageProcessorDispatcher imageProcessorDispatcher;
	private Long cameraId;

	public ThreadServerConnectionHandler(ServerSocket ss, long cameraId,
			ImageProcessorDispatcher imageProcessorDispatcher) {
		this.ss = ss;
		this.cameraId = cameraId;
		this.imageProcessorDispatcher = imageProcessorDispatcher;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = ss.accept();

				LOGGER.info(this.cameraId + "-" + boundary);

				ThreadServerConnection threadServerConnection = new ThreadServerConnection(socket,
						imageProcessorDispatcher);
				threadServerConnection.start();
				boundary = "Thats all folks!";
				LOGGER.info(boundary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
