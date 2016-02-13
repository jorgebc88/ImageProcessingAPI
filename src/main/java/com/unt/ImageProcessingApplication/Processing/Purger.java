package com.unt.ImageProcessingApplication.Processing;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.apache.log4j.Logger;

import com.unt.ImageProcessingApplication.entities.Vehicle;

/**
 * 
 * This purger will delete the cached vehicles that haven't been erased on 5
 * minutes before the current time.
 *
 */
public class Purger extends Thread {

	private static Logger LOGGER = Logger.getLogger(Purger.class);
	private ImageProcessor cameraController;
	private Set<Vehicle> vehicles;
	private Date actualDate = new Date();

	public Purger(ImageProcessor cameraController) {
		this.cameraController = cameraController;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void run() {
		try {
			LOGGER.info("The Purger for the camera " + cameraController.getCameraId() + " is going to be executed!!");
			vehicles = cameraController.getVehicleSet();
			LOGGER.info(cameraController.getCameraId() + ": Set's size before the Purger: " + vehicles.size());
			actualDate.setTime(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5));
			vehicles.removeIf(this.isRemovable(actualDate));
			LOGGER.info(cameraController.getCameraId() + ": Set's size after the Purger: " + vehicles.size());

		} catch (Exception e) {
			e.printStackTrace();
			this.destroy();
		}
	}

	public static Predicate<Vehicle> isRemovable(Date actualDate) {
		return v -> v.getDetectionDate().compareTo(actualDate) < 0;
	}
}
