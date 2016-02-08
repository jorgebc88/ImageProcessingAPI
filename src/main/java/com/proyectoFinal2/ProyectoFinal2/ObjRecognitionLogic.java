package com.proyectoFinal2.ProyectoFinal2;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

import dataBaseConnection.ObjectRandom;

public class ObjRecognitionLogic {

	private Mat prevFrame = null;

	private int autos = 0, motos = 0, omnibus = 0;

	private Set<Vehicle> vehicleSet = new HashSet<>();

	private double yLinePosition = 0;

	public VideoCapture capture = new VideoCapture();

	private int cameraId;
	
	private boolean isWideScreen;

	BackgroundSubtractorKNN backgroundSubtractor = Video.createBackgroundSubtractorKNN();

	public ObjRecognitionLogic(VideoCapture capture, int cameraId, boolean isWideScreen) {
		super();
		this.capture = capture;
		this.cameraId = cameraId;
		this.isWideScreen = isWideScreen;
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Image} to show
	 * @throws Exception
	 */
	public Mat grabFrame() throws Exception {

		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened()) {
			try {
				// read the current frame
				this.capture.read(frame);
				this.yLinePosition = frame.height() * 0.6;
				// if the frame is not empty, process it
				if (!frame.empty()) {
					// init
					Mat blurredImage = new Mat();
					Mat grayImage = new Mat();

					// remove some noise
					Imgproc.blur(frame, blurredImage, new Size(7, 7));

					// convert the frame to GRAY
					Imgproc.cvtColor(blurredImage, grayImage, Imgproc.COLOR_BGR2GRAY);
					if (this.prevFrame == null) {
						this.prevFrame = grayImage;
					}
					// get thresholding values from the UI
					// remember: H ranges 0-180, S and V range 0-255

					Mat fgmask = new Mat();
					fgmask.create(frame.size(), frame.type());

					backgroundSubtractor.apply(frame, fgmask);

					Imgproc.GaussianBlur(fgmask, fgmask, new Size(11, 11), 3.5, 3.5);

					Imgproc.threshold(fgmask, fgmask, 128, 255, Imgproc.THRESH_BINARY);

					backgroundSubtractor.getBackgroundImage(grayImage);
					// find the vehicles contours and show them
					this.prevFrame = grayImage;
					// find the vehicles contours and show them
					frame = this.findAndDetectVehicles(fgmask, frame);

					return frame;
				}
			} catch (Exception e) {
				// log the (full) error
				System.err.print("ERROR");
				e.printStackTrace();
			}
		}
		return frame;

	}

	/**
	 * Given a binary image containing one or more closed surfaces, use it as a
	 * mask to find and highlight the objects contours
	 *
	 * @param maskedImage
	 *            the binary image to be used as a mask
	 * @param frame
	 *            the original {@link Mat} image to be used for drawing the
	 *            objects contours
	 * @return the {@link Mat} image with the objects contours framed
	 */
	private Mat findAndDetectVehicles(Mat maskedImage, Mat frame) {
		// init
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();

		// find contours
		Imgproc.findContours(maskedImage, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		Mat contour;
		Rect rect;
		String type;
		// if any contour exist...
		Imgproc.line(frame, new Point(0, this.yLinePosition), new Point((frame.width()), this.yLinePosition),
				new Scalar(0, 0, 250));
		if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
			// for each contour, display it in blue
			for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
				contour = contours.get(idx);
				rect = Imgproc.boundingRect(contours.get(idx));
				if (rect.area() > 2000) {
					if (rect.area() < 15000) {
						type = "Bike";
					} else if (rect.area() < 55000) {
						type = "Car";
					} else {
						type = "Bus";
					}
					try {
						this.defineVehicle(rect, frame.width(), frame.height(), type);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Imgproc.drawMarker(frame, this.calculateMassCenterRectangle(rect), new Scalar(0, 250, 0));
					Imgproc.rectangle(frame, new Point(rect.x, rect.y),
							new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 250, 0));
				}
			}
		}
		return frame;
	}

	private Point calculateMassCenterRectangle(Rect rect) {
		double x, y;
		x = rect.x + (rect.width / 2);
		y = rect.y + (rect.height / 2);
		return new Point(x, y);

	}

	private void defineVehicle(Rect rect, int frameWidth, int frameHeight, String type) throws Exception {
		Vehicle vehicleAux;
		boolean isANewVehicle = true;
		Date date = new Date();

		if (vehicleSet.isEmpty()) {
			if (this.calculateMassCenterRectangle(rect).y < (frameHeight * 0.5) ) {
				date.setTime(System.currentTimeMillis());
				vehicleAux = new Vehicle(this.calculateMassCenterRectangle(rect), rect.area(), false,
						this.isGoingDown(rect, frameWidth), date);
				vehicleSet.add(vehicleAux);
				return;
			}
		}
		for (Vehicle vehicle : vehicleSet) {
			if (this.isMoving(vehicle.getMassCenterLocation(), this.calculateMassCenterRectangle(rect))) {
				isANewVehicle = false;
				vehicle.setMassCenterLocation(this.calculateMassCenterRectangle(rect));
				vehicle.setVehicleSize(rect.area());
				date.setTime(System.currentTimeMillis());
				vehicle.setDetectionDate(date);
				if (!vehicle.isGoingUp()) {
					if (!vehicle.isCounted() && this.shouldBeCounted(rect)) {
						vehicle.setCounted(true);
						if (type.equals("Bike")) {
							this.motos++;
							System.out.println("Camara n°: " + this.cameraId + " tipo: " + type + ": " + this.motos
									+ " " + vehicle + " Tamaño video: " + (frameHeight * frameWidth));
						} else if (type.equals("Car")) {
							this.autos++; // modificar!!
							System.out.println("Camara n°: " + this.cameraId + " tipo: " + type + ": " + this.autos
									+ " " + vehicle + " Tamaño video: " + (frameHeight * frameWidth));
						} else {
							this.omnibus++; // modificar!!
							System.out.println("Camara n°: " + this.cameraId + " tipo: " + type + ": " + this.omnibus
									+ " " + vehicle + " Tamaño video: " + (frameHeight * frameWidth));

						}
						System.out.println("Camara n°: " + this.cameraId + " Total: " + (this.autos + this.motos));
						ObjectRandom.httpPost(type, vehicle.getDetectionDate(), cameraId);
						this.vehicleSet.remove(vehicle);
					}
				}
				break;
			}
		}
		if (isANewVehicle) {
			if (this.calculateMassCenterRectangle(rect).y < (frameHeight * 0.5)) {
				date.setTime(System.currentTimeMillis());
				vehicleAux = new Vehicle(this.calculateMassCenterRectangle(rect), rect.area(), false,
						this.isGoingDown(rect, frameWidth), date);
				vehicleSet.add(vehicleAux);
			}
		}
	}

//	private boolean xPosition(Rect rect) {
//		if(this.isWideScreen){
//			return 
//		}
//		return true;
//	}

	private boolean isGoingDown(Rect rect, int frameWidth) {
		if (this.calculateMassCenterRectangle(rect).x < frameWidth) {
			return true;
		}
		return false;
	}

	private boolean shouldBeCounted(Rect rect) {
		double newYPosition = (this.calculateMassCenterRectangle(rect)).y;
		if (newYPosition > this.yLinePosition) {
			return true;
		}
		return false;
	}

	private boolean isMoving(Point oldCalculateMassCenter, Point newMassCenterLocation) {
		if (newMassCenterLocation.x >= oldCalculateMassCenter.x * 0.80
				&& newMassCenterLocation.x <= oldCalculateMassCenter.x * 1.20) {
			if (newMassCenterLocation.y >= oldCalculateMassCenter.y * 0.8
					&& newMassCenterLocation.y <= oldCalculateMassCenter.y * 1.2) {
				return true;
			}
		}
		return false;
	}

	private boolean areTheSameSize(double oldVehicle, double newVehicle) {
		if (newVehicle >= oldVehicle * 0.80 && newVehicle <= oldVehicle * 1.20) {
			return true;
		}
		return false;

	}

}
