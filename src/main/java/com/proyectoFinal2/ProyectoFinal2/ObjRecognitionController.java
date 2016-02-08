package com.proyectoFinal2.ProyectoFinal2;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

/**
 * The controller associated with the only view of our application. The
 * application logic is implemented here. It handles the button for
 * starting/stopping the camera, the acquired video stream, the relative
 * controls and the image segmentation process.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @version 1.5 (2015-11-26)
 * @since 1.0 (2015-01-13)
 */
public class ObjRecognitionController {

	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	FFmpegFrameGrabber grabber;

	private Map<Integer, ObjRecognitionLogic> camerasMap = new HashMap<>();

	/**
	 * 
	 * @throws Exception
	 */
	public void startCamera(int cameraId, String ip) throws Exception {
		if (!camerasMap.containsKey(cameraId)) {
			VideoCapture capture = new VideoCapture();
			capture.open(ip);
			// is the video stream available?
			if (capture.isOpened()) {
				// grab a frame every 33 ms (30 frames/sec)
				JFrame ventana = new JFrame();
				Mat mat = new Mat();
				capture.read(mat);
				ventana.setSize(mat.width(), mat.height());
				System.out.println(
						cameraId + " - " + mat.width() + " - " + mat.height() + " - " + this.isWideScreen(mat));
				ventana.setLocationRelativeTo(null);
				ventana.setVisible(true);
				ObjRecognitionLogic objRecognitionLogic = new ObjRecognitionLogic(capture, cameraId,
						this.isWideScreen(mat));
				camerasMap.put(cameraId, objRecognitionLogic);
				JLabel jLabel = new JLabel();
				Thread frameGrabber = new Thread() {
					@Override
					public void run() {
						try {
							jLabel.setIcon(new ImageIcon(createAwtImage(objRecognitionLogic.grabFrame())));
							ventana.getContentPane().add(jLabel);
							jLabel.updateUI();
						} catch (Exception e) {
							e.printStackTrace();
							ventana.dispose();
							this.destroy();
						}
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);// 33

				// update the button content
			} else {
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		} else {
			System.out.println("Ya se estaba procesando esa camara!!");
		}
	}

	private boolean isWideScreen(Mat mat) {
		double resolution = (double) mat.width() / (double) mat.height();
		if (resolution > 1.5) {
			return true;
		}
		return false;
	}

	public static BufferedImage createAwtImage(Mat mat) {
		int type = 0;
		if (mat.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (mat.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		} else {
			return null;
		}

		BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		mat.get(0, 0, data);

		return image;
	}

}
