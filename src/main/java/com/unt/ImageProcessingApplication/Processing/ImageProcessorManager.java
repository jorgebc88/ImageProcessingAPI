package com.unt.ImageProcessingApplication.Processing;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.unt.ImageProcessingApplication.entities.Camera;

/**
 * The controller associated with the only view of our application. The
 * application logic is implemented here. It handles the button for
 * starting/stopping the camera, the acquired video stream, the relative
 * controls and the image segmentation process.
 *
 * @version 1.5 (2015-11-26)
 * @since 1.0 (2015-01-13)
 */
public class ImageProcessorManager {

	private static Logger LOGGER = Logger.getLogger(ImageProcessorManager.class);
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// a timer for acquiring the video stream
	private ScheduledExecutorService timerPurger;
	// the OpenCV object that performs the video capture

	private int socket = 8090;

	private Map<Long, ImageProcessor> camerasMap = new HashMap<>();

	private Set<JFrame> windowsSet = new HashSet<>();

	/**
	 * 
	 * @throws Exception
	 */
	public void startCameraProcessing(Camera camera) throws Exception {
		long cameraId = camera.getId();
		String ip = camera.getIp();
		if (!camerasMap.containsKey(cameraId)) {
			VideoCapture capture = new VideoCapture();
			LOGGER.info(ip);
			capture.open(ip);
			// is the video stream available?
			if (capture.isOpened()) {

				// grab a frame every 33 ms (30 frames/sec)
				Mat mat = new Mat();
				capture.read(mat);

				ImageProcessed imageProcessed = new ImageProcessed();

				JFrame window = createFrame(camera, mat);

				ImageProcessor imageProcessor = new ImageProcessor(capture, cameraId, this.isWideScreen(mat));

				camerasMap.put(cameraId, imageProcessor);
				JLabel jLabel = new JLabel();
				window.getContentPane().add(jLabel);
				ImageProcessorDispatcher imageProcessorDispatcher = new ImageProcessorDispatcher(jLabel, imageProcessor,
						window, this.socket, imageProcessed);
				this.socket++;

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(imageProcessorDispatcher, 0, 33, TimeUnit.MILLISECONDS);

				Purger purger = new Purger(imageProcessor);

				this.timerPurger = Executors.newSingleThreadScheduledExecutor();
				this.timerPurger.scheduleAtFixedRate(purger, 5, 5, TimeUnit.MINUTES);

			} else {
				// log the error
				LOGGER.error("Failed to open the camera connection...");
			}
		} else {
			LOGGER.info("This camera is already been processed!!");
		}
	}

	private JFrame createFrame(Camera camera, Mat mat) {
		JFrame ventana = new JFrame(camera.getLocation());
		ventana.setLocation(0, 0);
		ventana.setSize(mat.width(), mat.height());
		ventana.setLocationRelativeTo(null);
		ventana.setLocation((windowsSet.size()) * mat.width(), 0);
		ventana.setVisible(true);
		ventana.setResizable(false);
		ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		windowsSet.add(ventana);
		return ventana;
	}

	private boolean isWideScreen(Mat mat) {
		double resolution = (double) mat.width() / (double) mat.height();
		if (resolution > 1.5) {
			return true;
		}
		return false;
	}

}
