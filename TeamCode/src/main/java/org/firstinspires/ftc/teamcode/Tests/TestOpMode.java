package org.firstinspires.ftc.teamcode.Tests;

import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

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

    Robot robot = new Robot();
    Webcam webcam = new Webcam();
    Movement moves = new Movement();
    ObjectOrientationAnalysisPipeline testPipeline = null;

    @Override
    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap);
        webcam.init(hardwareMap);
        testPipeline = new ObjectOrientationAnalysisPipeline();
        webcam.camera.setPipeline(testPipeline);

        Point midpoint;


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




        //TEST STRAFE DIRECTION FIRST BEFORE TRYING THE MIDPOINT MOVEMENT


        waitForStart();
        while(opModeIsActive()){

                sleep(3000);
                webcam.camera.setPipeline(testPipeline);
                midpoint = testPipeline.getMidpoints().get(0);


                int xError = moves.isBlockInXRegion(midpoint.x);

                if(xError > 0){
                    while(moves.isBlockInXRegion(testPipeline.getMidpoints().get(0).x) != 0){
                        moves.linearMove(robot,0.3, -1, 1, 1, -1);
                        webcam.camera.setPipeline(testPipeline);
                    }
                    robot.setAllWheelPower(0);
                }
                else if(xError < 0){
                    while(moves.isBlockInXRegion(testPipeline.getMidpoints().get(0).x) != 0){
                        moves.linearMove(robot,0.3, 1, -1, -1, 1);
                        webcam.camera.setPipeline(testPipeline);
                    }
                    robot.setAllWheelPower(0);
                }

                /*midpoint = testPipeline.getMidpoints().get(0);
                int yError = moves.isBlockInYRegion(midpoint.y);

                if(yError > 0){
                    while(moves.isBlockInXRegion(testPipeline.getMidpoints().get(0).x) != 0){
                        moves.linearMove(robot,0.5, 1, 1, 1, 1);
                    }
                }
                else if(xError < 0){
                    while(moves.isBlockInXRegion(testPipeline.getMidpoints().get(0).x) != 0){
                        moves.linearMove(robot,0.5, -1, -1, -1, -1);
                    }
                }*/

        }
    }



}
