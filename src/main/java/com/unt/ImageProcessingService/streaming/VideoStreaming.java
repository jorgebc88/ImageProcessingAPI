package com.unt.ImageProcessingService.streaming;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class VideoStreaming {

	public static void writeHeader(OutputStream stream, String boundary) throws IOException {
		stream.write(("HTTP/1.0 200 OK\r\n" + "Connection: close\r\n" + "Max-Age: 0\r\n" + "Expires: 0\r\n"
				+ "Cache-Control: no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0\r\n"
				+ "Pragma: no-cache\r\n" + "Content-Type: multipart/x-mixed-replace; " + "boundary=" + boundary + "\r\n"
				+ "\r\n" + "--" + boundary + "\r\n").getBytes());
	}

	public static void writeJpg(OutputStream stream, BufferedImage img, String boundary) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "jpg", baos);
		byte[] imageBytes = baos.toByteArray();
		stream.write(
				("Content-type: image/jpeg\r\n" + "Content-Length: " + imageBytes.length + "\r\n" + "\r\n").getBytes());
		stream.write(imageBytes);
		stream.write(("\r\n--" + boundary + "\r\n").getBytes());
	}
}
