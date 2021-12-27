package org.firstinspires.ftc.teamcode.Tests;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.Camera.CSEDeterminationPipeline;
import org.firstinspires.ftc.teamcode.Camera.ObjectOrientationAnalysisPipeline;
import org.firstinspires.ftc.teamcode.Camera.Webcam;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Autonomous(name = "TestOpMode", group = "tests")
public class TestOpMode extends LinearOpMode {

    Webcam webcam = new Webcam();
    ObjectOrientationAnalysisPipeline testPipeline = null;
    ArrayList<Point> midpoints = new ArrayList<Point>();

    @Override
    public void runOpMode() throws InterruptedException{
        webcam.init(hardwareMap);
        testPipeline = new ObjectOrientationAnalysisPipeline();
        webcam.camera.setPipeline(testPipeline);


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
        while(opModeIsActive()){
            midpoints = testPipeline.getMidpoints();
            telemetry.addData("Midpoints", midpoints);
            telemetry.update();

        }
    }



}
