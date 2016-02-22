package com.unt.ImageProcessingApplication.entities;

import java.io.Serializable;

/**
 */
public class Camera implements Serializable {

	private static final long serialVersionUID = 2607403988611110166L;

	private Long id;

	private String location;

	private String latitude;

	private String longitude;

	private String ip;

	private boolean active;

	private String pointingAt;

	public Camera() {
	}

	public Camera(String location) {
		super();
		this.location = location;
	}

	public Camera( String location, String latitude, String longitude, boolean active, String pointingAt) {
		super();
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.active = active;
		this.pointingAt = pointingAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPointingAt() {
		return pointingAt;
	}

	public void setPointingAt(String pointingAt) {
		this.pointingAt = pointingAt;
	}

	@Override
	public String toString() {
		return "Camera{" +
				"id=" + id +
				", location='" + location + '\'' +
				", latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				", ip='" + ip + '\'' +
				", active=" + active +
				", pointing_at='" + pointingAt + '\'' +
				'}';
	}
}