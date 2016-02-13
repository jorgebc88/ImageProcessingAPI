package com.unt.ImageProcessingApplication.Processing;

import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.unt.ImageProcessingApplication.streaming.VideoStreaming;

public class ThreadServerConnection extends Thread {

	private static Logger LOGGER = Logger.getLogger(ThreadServerConnection.class);

	private Socket socket;
	private String boundary;
	private ImageProcessorDispatcher imageProcessorDispatcher;

	public ThreadServerConnection(Socket socket, ImageProcessorDispatcher imageProcessorDispatcher) {
		this.socket = socket;
		this.boundary = "Thats it folks!";
		;
		this.imageProcessorDispatcher = imageProcessorDispatcher;
	}

	@Override
	public void run() {
		try {
			VideoStreaming.writeHeader(socket.getOutputStream(), boundary);
			while (true) {
				VideoStreaming.writeJpg(socket.getOutputStream(), imageProcessorDispatcher.getImage(), boundary);
			}
		} catch (SocketException e) {
			LOGGER.error("The connection was terminated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
