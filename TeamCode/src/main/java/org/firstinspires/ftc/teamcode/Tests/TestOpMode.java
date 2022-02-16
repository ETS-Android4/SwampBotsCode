package org.firstinspires.ftc.teamcode.Tests;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

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


        int counter = 0;

        waitForStart();
        while(opModeIsActive() && counter == 0){

                /*webcam.camera.setPipeline(testPipeline);
                midpoint = testPipeline.getMidpoint();


                int xError = moves.isBlockInXRegion(midpoint.x, midpoint.y);

                if(xError > 0){
                    while(moves.isBlockInXRegion(testPipeline.getMidpoint().x, testPipeline.getMidpoint().y) != 0){
                        telemetry.addData("pos", moves.isBlockInXRegion(testPipeline.getMidpoint().x, testPipeline.getMidpoint().y));
                        telemetry.update();
                        moves.linearMove(robot,0.2, -1, 1, 1, -1);
                        webcam.camera.setPipeline(testPipeline);
                    }
                    robot.setAllWheelPower(0);
                }
                else if(xError < 0){
                    while(moves.isBlockInXRegion(testPipeline.getMidpoint().x, testPipeline.getMidpoint().y) != 0){
                        moves.linearMove(robot,0.2, 1, -1, -1, 1);
                        webcam.camera.setPipeline(testPipeline);
                    }
                    robot.setAllWheelPower(0);
                }

                sleep(500);
                midpoint = testPipeline.getMidpoint();
                int yError = moves.isBlockInYRegion(midpoint.y);

                if(yError > 0){
                    while(moves.isBlockInYRegion(testPipeline.getMidpoint().y) != 0){
                        moves.linearMove(robot,0.2, 1, 1, 1, 1);
                        webcam.camera.setPipeline(testPipeline);
                    }
                    robot.setAllWheelPower(0);
                }
                else if(yError < 0){
                    while(moves.isBlockInYRegion(testPipeline.getMidpoint().y) != 0){
                        moves.linearMove(robot,0.2, -1, -1, -1, -1);
                        webcam.camera.setPipeline(testPipeline);
                    }
                    robot.setAllWheelPower(0);
                }

                //moves.linearMoveDistance(robot, 0.3, 11, 1, 1, 1, 1);
                sleep(200);
                robot.grabBlock();


                sleep(10000);
                counter++;*/

        }
    }



}
