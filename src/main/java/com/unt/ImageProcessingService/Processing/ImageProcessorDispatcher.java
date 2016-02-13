package com.unt.ImageProcessingService.Processing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.unt.ImageProcessingService.utils.Util;

public class ImageProcessorDispatcher extends Thread {

	private static Logger LOGGER = Logger.getLogger(ImageProcessorDispatcher.class);

	private JLabel jLabel;
	private ImageProcessor imageProcessor;
	private JFrame window;
	private BufferedImage image;
	private Socket socket;
	private ServerSocket ss;
	private ThreadServerConnectionHandler threadServerConnectionHandler;

	public ImageProcessorDispatcher(JLabel jLabel, ImageProcessor imageProcessor, JFrame window, int port)
			throws IOException {
		this.jLabel = jLabel;
		this.imageProcessor = imageProcessor;
		this.window = window;
		ss = new ServerSocket(port);
		try {
			threadServerConnectionHandler = new ThreadServerConnectionHandler(ss, this.imageProcessor.getCameraId(),
					this);
			threadServerConnectionHandler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// LOGGER.info(imageProcessor.getCameraId() + "-" );
			image = Util.createBufferedImage(imageProcessor.processFrame());

			jLabel.setIcon(new ImageIcon(image));
			jLabel.updateUI();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			window.dispose();
			try {
				socket.close();
				ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();

			}
			this.destroy();
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
