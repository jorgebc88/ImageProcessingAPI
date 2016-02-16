package com.unt.ImageProcessingApplication.Processing;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

class ThreadServerConnectionHandler extends Thread {

	private static Logger LOGGER = Logger.getLogger(ThreadServerConnectionHandler.class);

	private String boundary;
	private ServerSocket ss;
	private Long cameraId;
	private ImageProcessed imageProcessed;

	public ThreadServerConnectionHandler(ServerSocket ss, long cameraId,
										  ImageProcessed imageProcessed) {
		this.ss = ss;
		this.cameraId = cameraId;
		this.imageProcessed = imageProcessed;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = ss.accept();

				imageProcessed.addSocket(socket);
				ThreadServerConnection threadServerConnection = new ThreadServerConnection(socket, imageProcessed);
//				threadServerConnection.start();
				boundary = "Thats all folks!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
