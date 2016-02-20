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
}
