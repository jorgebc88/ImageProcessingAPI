package com.unt.ImageProcessingApplication.utils.cameraUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unt.ImageProcessingApplication.entities.Camera;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marco on 2/18/2016.
 */
public class CameraUtils {
    public static ArrayList<Camera> cameraListFromJson(String data) {
        ArrayList<Camera> cameras = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            cameras = objectMapper.readValue(data,
                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Camera.class));

        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cameras;
    }
}
