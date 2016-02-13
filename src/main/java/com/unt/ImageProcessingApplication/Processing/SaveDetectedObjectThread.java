package com.unt.ImageProcessingApplication.Processing;

import java.util.Date;

import org.apache.log4j.Logger;

import com.unt.ImageProcessingApplication.dao.DetectedObjectDAO;

public class SaveDetectedObjectThread extends Thread {
	static final Logger LOGGER = Logger.getLogger(SaveDetectedObjectThread.class);

	private String type;
	private Date detectionDate;
	private long cameraId;

	private String objectDetected;

	public SaveDetectedObjectThread(String type, Date detectionDate, long cameraId) {
		super();
		this.type = type;
		this.detectionDate = detectionDate;
		this.cameraId = cameraId;
	}

	@Override
	public void run() {
		try {
			// LOGGER.info("thread: " +
			// DetectedObjectController.saveDetectedObject(type, detectionDate,
			// cameraId));
			objectDetected = DetectedObjectDAO.saveDetectedObject(type, detectionDate, cameraId);
			// LOGGER.info(objectDetected);
		} catch (Exception e) {
			LOGGER.error("There was a problem in the save of the Object");
			this.destroy();
		}
	}

}
