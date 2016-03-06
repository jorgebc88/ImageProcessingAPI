package com.unt.ImageProcessingApplication.processing;

import java.awt.image.BufferedImage;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by marco on 2/17/2016.
 */
public class ImageProcessed {
	private BufferedImage bufferedImage;

	private Map<Socket, Boolean> socketMap = new ConcurrentHashMap<>();

	synchronized void addSocket(Socket socket) {
		socketMap.put(socket, false);
	}

	public BufferedImage get(Socket socket) {

		socketMap.replace(socket, false);
		return bufferedImage;

	}

	public void put(BufferedImage bufferedImage) {
		Socket socket;
		Iterator it = socketMap.keySet().iterator();
		this.bufferedImage = bufferedImage;

		while (it.hasNext()) {
			socket = (Socket) it.next();
			socketMap.replace(socket, true);
		}
	}

	public Map<Socket, Boolean> getSocketMap() {
		return socketMap;
	}

}
