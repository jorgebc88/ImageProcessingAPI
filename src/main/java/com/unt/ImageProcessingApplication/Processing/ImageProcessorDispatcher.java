package com.unt.ImageProcessingApplication.processing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.unt.ImageProcessingApplication.utils.imageUtils.ImageUtils;
import org.apache.log4j.Logger;

public class ImageProcessorDispatcher extends Thread {

	private static Logger LOGGER = Logger.getLogger(ImageProcessorDispatcher.class);

	private JLabel jLabel;
	private ImageProcessor imageProcessor;
	private JFrame window;
	private BufferedImage image;
	private ServerSocket ss;
	private ThreadServerConnectionHandler threadServerConnectionHandler;
	private ImageProcessed imageProcessed = new ImageProcessed();

	public ImageProcessorDispatcher(JLabel jLabel, ImageProcessor imageProcessor, JFrame window, int port, ImageProcessed imageProcessed)
			throws IOException {
		this.jLabel = jLabel;
		this.imageProcessor = imageProcessor;
		this.window = window;
		this.ss = new ServerSocket(port);
		this.imageProcessed = imageProcessed;
		try {
			threadServerConnectionHandler = new ThreadServerConnectionHandler(ss, this.imageProcessor.getCameraId(),
					 imageProcessed);
			threadServerConnectionHandler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			image = ImageUtils.createBufferedImage(imageProcessor.processFrame());
			imageProcessed.put(image);
			jLabel.setIcon(new ImageIcon(image));
			jLabel.updateUI();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			window.dispose();
			try {
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
