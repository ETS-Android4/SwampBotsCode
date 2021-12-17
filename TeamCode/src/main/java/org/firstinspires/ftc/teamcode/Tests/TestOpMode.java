package org.firstinspires.ftc.teamcode.Tests;

import org.firstinspires.ftc.teamcode.Camera.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.Camera.Webcam;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "TestOpMode", group = "tests")
public class TestOpMode extends LinearOpMode {

    Webcam webcam = new Webcam();
    ColorValueTest testPipeline = null;

    @Override
    public void runOpMode(){

        webcam.init(hardwareMap);
        testPipeline = new ColorValueTest();

        webcam.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                webcam.camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                webcam.camera.setPipeline(testPipeline);

                webcam.camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        waitForStart();


        webcam.camera.setPipeline(testPipeline);
        sleep(3000);
        telemetry.addData("Blue", testPipeline.getValue());
        telemetry.update();
        sleep(5000);

    }
}
