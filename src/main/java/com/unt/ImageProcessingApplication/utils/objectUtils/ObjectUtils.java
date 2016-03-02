package com.unt.ImageProcessingApplication.utils.objectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectUtils {
    /**
     * This method verify if the detected object is an Object according to the object size and the frame size
     * @param objectSize Size of the detected object
     * @param frameSize Size of the frame
     * @return True if it is a Object, False if not
     */
    public static boolean isAnObject(double objectSize, double frameSize) {
        if (objectSize > (0.006 * frameSize)) {
            return true;
        }
        return false;
    }

    /**
     * This method verify if the detected object is a Bike according to the object size and the frame size
     * @param objectSize Size of the detected object
     * @param frameSize Size of the frame
     * @return True if it is a Bike, False if not
     */
    public static boolean isABike(double objectSize, double frameSize) {
        if (objectSize < (0.05 * frameSize)) {
            return true;
        }
        return false;
    }

    /**
     * This method verify if the detected object is a Car according to the object size and the frame size
     * @param objectSize Size of the detected object
     * @param frameSize Size of the frame
     * @return True if it is a Car, False if not
     */
    public static boolean isACar(double objectSize, double frameSize) {
        if (objectSize < (0.18 * frameSize)) {
            return true;
        }
        return false;
    }

    /**
     * This method verify if the detected object is a Bus according to the object size and the frame size
     * @param objectSize Size of the detected object
     * @param frameSize Size of the frame
     * @return True if it is a Bus, False if not
     */
    public static boolean isABus(double objectSize, double frameSize) {
        if (objectSize > (0.18 * frameSize)) {
            return true;
        }
        return false;
    }

    /**
     * This method convert an Object to json
     * @param data Object to be transformed in json
     * @return Object transformed to json
     */
    public static String toJson(Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method calculate the direction of the detected objects
     * @param cameraPointingAt The direction pointed by the camera
     */
    public static String calculateDetectedObjectDirection(String cameraPointingAt) {
        if(cameraPointingAt == null){
            throw new RuntimeException("The camera must have the field pointing_at.");
        }
        switch (cameraPointingAt.trim().toLowerCase()){
            case "north" : return "South";
            case "west" : return "East";
            case "south" : return "North";
            case "east" : return "South";
            default: return "South";
        }
    }
}
