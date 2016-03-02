package com.unt.ImageProcessingApplication.processing;

import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.unt.ImageProcessingApplication.streaming.VideoStreaming;

public class ThreadServerConnection implements Runnable{

	private static Logger LOGGER = Logger.getLogger(ThreadServerConnection.class);

	private Socket socket;
	private String boundary;
	private ImageProcessed imageProcessed;

	public ThreadServerConnection(Socket socket, ImageProcessed imageProcessed) {
		this.socket = socket;
		this.boundary = "Thats it folks!";
		this.imageProcessed = imageProcessed;
		(new Thread(this)).start();
	}

	@Override
	public void run() {
		try {
			VideoStreaming.writeHeader(socket.getOutputStream(), boundary);
			while (true) {
				if (imageProcessed.getSocketMap().get(socket)) {
					VideoStreaming.writeJpg(socket.getOutputStream(), imageProcessed.get(socket), boundary);
				}
			}
		} catch (SocketException e) {
			LOGGER.error("The connection was terminated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
