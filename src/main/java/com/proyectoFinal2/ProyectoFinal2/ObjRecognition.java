package com.proyectoFinal2.ProyectoFinal2;

import org.opencv.core.Core;


public class ObjRecognition  {

    public static void main(String[] args) throws Exception {
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ObjRecognitionController controller = new ObjRecognitionController();
//        controller.startCamera(1, "F:/proyectoFinalV2/src/main/resources/video/55 grados JE.avi");
//        controller.startCamera(2, "F:/proyectoFinalV2/src/main/resources/video/MVI_3481.avi");
        controller.startCamera(2, "http://192.168.2.106:8081/Documents/GitHub/55gradosJE.avi");

    }
}
