package org.firstinspires.ftc.teamcode.Camera;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvWebcam;


public class Webcam {
    public OpenCvWebcam camera = null;

    //We need a constructor, but we're not doing any initializing for custom
    //variables, so leave empty
    public Webcam(){

    }
    
    public void init(HardwareMap hw){

        //Camera name and id stuff that needs to be set up in order to initialize the camera object
        int cameraMonitorViewId = hw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hw.appContext.getPackageName());

        //Get the name from the configuration
        WebcamName webcamName = hw.get(WebcamName.class, "Webcam 1");


        //Initialize the camera object with parameters above
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);


    }
}
