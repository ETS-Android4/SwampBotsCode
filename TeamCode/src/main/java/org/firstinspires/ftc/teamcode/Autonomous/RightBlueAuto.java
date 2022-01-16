package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "RightBlueAuto", group = "Blue")
public class RightBlueAuto extends LinearOpMode {

    Robot robot = new Robot();
    Movement moves = new Movement();
    Webcam webcam = new Webcam();
    CSEDeterminationPipeline csePipeline = null;

    private int CSEPosition;

    @Override
    public void runOpMode() throws InterruptedException{

        //Defines motors and direction
        robot.init(hardwareMap);
        webcam.init(hardwareMap);
        csePipeline = new CSEDeterminationPipeline("blue");

        webcam.camera.setPipeline(csePipeline);



        webcam.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                webcam.camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                webcam.camera.setPipeline(csePipeline);

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

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
        waitForStart();

        int counter = 0;
        while (opModeIsActive() && counter == 0){


            robot.grabBlock();
            sleep(500);

            moves.linearMoveDistance(robot, 7, -1, -1, -1, -1);
            sleep(100);
            moves.linearMoveDistance(robot, 36, 1, -1, 1, -1);
            sleep(100);
            moves.linearMoveDistanceHalfInch(robot, 1, -1, 1, -1);
            sleep(100);

            webcam.camera.setPipeline(csePipeline);
            sleep(200);
            CSEPosition = csePipeline.getAnalysis();
            telemetry.addData("Position", CSEPosition);
            telemetry.update();

            moves.linearMoveDistance(robot, 29, 1, 1, 1, 1);
            sleep(100);
            moves.linearMoveDistance(robot, 19, 1, -1, 1, -1);
            sleep(100);
            moves.rotateArm(robot, 120);
            sleep(300);

            if(CSEPosition == 2){

                moves.linearMoveDistance(robot, 3, -1, -1, -1, -1);
                sleep(100);
                moves.rotateArm(robot, 45);
                robot.releaseBlock();
                sleep(100);
                moves.rotateArm(robot, -45);
                moves.linearMoveDistance(robot, 18, 1, -1, 1, -1);
                sleep(100);
                moves.linearMoveDistance(robot, 32, -1, 1, 1, -1);


            } else if(CSEPosition == 1){

                moves.linearMoveDistance(robot, 6, 1, 1, 1, 1);
                moves.rotateArm(robot, 60);
                moves.linearMoveDistance(robot, 6, -1, -1, -1, -1);
                robot.releaseBlock();

                moves.linearMoveDistance(robot, 6, 1, 1, 1, 1);
                moves.linearMoveDistance(robot, 18, 1,-1, 1, -1);

                moves.linearMoveDistance(robot, 20, -1, 1, 1, -1);
                moves.rotateArm(robot, -90);

            } else if(CSEPosition == 0) {

                moves.linearMoveDistance(robot, 6, 1, 1, 1, 1);
                moves.rotateArm(robot, 80);
                sleep(200);
                moves.linearMoveDistance(robot, 6, -1, -1, -1, -1);
                robot.releaseBlock();

                moves.linearMoveDistance(robot, 6, 1, 1, 1, 1);
                moves.linearMoveDistance(robot, 18, 1,-1, 1, -1);

                moves.linearMoveDistance(robot, 21, -1, 1, 1, -1);
                moves.rotateArm(robot, -110);
                sleep(200);

            }

            sleep(100);
            moves.linearMoveDistance(robot, 31, 1, 1,1, 1);
            moves.linearMoveDistanceHalfInch(robot, 1, 1, 1, 1);

            double time = getRuntime();
            while(getRuntime() < time + 4){
                robot.carousel.setPower(-0.8);
            }
            robot.carousel.setPower(0);

            moves.linearMoveDistance(robot, 20, -1, -1, -1, -1);

            sleep(3000);
            counter++;
        }
    }
}
