package com.unt.ImageProcessingApplication.utils.objectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by marco on 2/18/2016.
 */
public class ObjectUtils {
    public static boolean isAnObject(double objectSize, double frameSize) {
        if (objectSize > (0.006 * frameSize)) {
            return true;
        }
        return false;
    }

    public static boolean isABike(double objectSize, double frameSize) {
        if (objectSize < (0.05 * frameSize)) {
            return true;
        }
        return false;
    }

    public static boolean isACar(double objectSize, double frameSize) {
        if (objectSize < (0.18 * frameSize)) {
            return true;
        }
        return false;
    }

    public static boolean isABus(double objectSize, double frameSize) {
        if (objectSize > (0.18 * frameSize)) {
            return true;
        }
        return false;
    }

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
