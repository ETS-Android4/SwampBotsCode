package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "LeftRedAuto", group = "Red")
public class LeftRedAuto extends LinearOpMode {

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
        csePipeline = new CSEDeterminationPipeline("red");

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


            /*if (colorSensor instanceof SwitchableLight) {
                ((SwitchableLight)colorSensor).enableLight(true);
            }

            colorSensor.setGain(5);
            final float[] hsvValues = new float[3];

            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            Color.colorToHSV(colors.toColor(), hsvValues);


            telemetry.addData("Path0",  "Starting at %7d ",
                    frontRight.getCurrentPosition());
            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            telemetry.addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", colors.alpha);
            telemetry.update();*/

            /* KEY FOR LINEAR MOVES

                FORWARD -- All 1's
                BACKWARD -- All -1's
                RIGHT -- -1, 1, 1, -1
                LEFT -- 1, -1, -1, 1
                CCW TURN -- -1, 1, -1, 1
                CW TURN -- 1, -1, 1, -1

                24 INCHES = 90 DEGREE TURN WHEEl
             */


            robot.grabBlock();
            sleep(500);

            moves.linearMoveDistance(robot, 7, -1, -1, -1, -1);
            moves.linearMoveDistance(robot, 36, 1, -1, 1, -1);
            moves.linearMoveDistanceHalfInch(robot, 1, -1, 1, -1);
            sleep(100);

            webcam.camera.setPipeline(csePipeline);
            sleep(200);
            CSEPosition = csePipeline.getAnalysis();
            telemetry.addData("Position", CSEPosition);
            telemetry.update();

            moves.linearMoveDistanceHalfInch(robot, 1, -1, 1, -1);
            moves.linearMoveDistance(robot, 29, 1, 1, 1, 1);
            moves.linearMoveDistance(robot, 18, -1, 1, -1, 1);


            moves.rotateArm(robot, 120);
            sleep(300);

            if(CSEPosition == 2){

                moves.linearMoveDistance(robot, 4, -1, -1, -1, -1);
                moves.rotateArm(robot, 60);
                robot.releaseBlock();
                moves.rotateArm(robot, -45);

                moves.linearMoveDistance(robot, 28, 1, 1, 1, 1);



            } else if(CSEPosition == 1){

                moves.linearMoveDistance(robot, 6, 1, 1, 1, 1);
                moves.rotateArm(robot, 60);
                moves.linearMoveDistance(robot, 6, -1, -1, -1, -1);
                robot.releaseBlock();
                sleep(100);

                moves.linearMoveDistance(robot, 6, 1, 1, 1, 1);
                moves.rotateArm(robot, -78);
                sleep(300);
                moves.linearMoveDistance(robot, 19, 1,1, 1, 1);


            } else if(CSEPosition == 0) {

                moves.linearMoveDistance(robot, 6, 1, 1, 1, 1);
                moves.rotateArm(robot, 77);
                moves.linearMoveDistance(robot, 8, -1, -1, -1, -1);
                robot.releaseBlock();
                sleep(100);

                moves.linearMoveDistance(robot, 8, 1, 1, 1, 1);
                moves.rotateArm(robot, -105);
                sleep(300);
                moves.linearMoveDistance(robot, 18, 1,1, 1, 1);




            }

            moves.linearMoveDistance(robot, 33, -1, 1, 1, -1);
            moves.linearMoveDistanceHalfInch(robot, -1, 1, 1, -1);


            double time = getRuntime();
            while(getRuntime() < time + 4){
                robot.carousel.setPower(0.8);
            }
            robot.carousel.setPower(0);

            moves.linearMoveDistance(robot, 22, 1, -1, -1, 1);
            counter++;
        }
    }
}
