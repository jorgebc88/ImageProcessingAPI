package com.unt.ImageProcessingApplication.Processing;

import java.util.Date;

import org.apache.log4j.Logger;

import com.unt.ImageProcessingApplication.dao.DetectedObjectDAO;

public class SaveDetectedObjectThread extends Thread {
	static final Logger LOGGER = Logger.getLogger(SaveDetectedObjectThread.class);

	private String type;
	private Date detectionDate;
	private long cameraId;
	private String detectedObjectDirection;

	private String objectDetected;

	public SaveDetectedObjectThread(String type, Date detectionDate, long cameraId, String detectedObjectDirection) {
		super();
		this.type = type;
		this.detectionDate = detectionDate;
		this.cameraId = cameraId;
		this.detectedObjectDirection = detectedObjectDirection;
	}

	@Override
	public void run() {
		try {
			objectDetected = DetectedObjectDAO.saveDetectedObject(detectedObjectDirection, type, detectionDate, cameraId);
		} catch (Exception e) {
			LOGGER.error("There was a problem in the save of the Object");
			this.destroy();
		}
	}

}
