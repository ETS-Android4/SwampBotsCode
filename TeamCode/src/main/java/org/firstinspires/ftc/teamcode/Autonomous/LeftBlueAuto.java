package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "LeftBlueAuto", group = "Blue")
public class LeftBlueAuto extends LinearOpMode {

    Robot robot = new Robot();
    Movement moves = new Movement();
    CSEDetermination cseDetermination = new CSEDetermination();

    private int CSEPosition;

    @Override
    public void runOpMode() throws InterruptedException{

        //Defines motors and direction
        robot.init(hardwareMap, "auto");
        cseDetermination.init(hardwareMap, "blue");


        //Event listener for CSEDetermination
        cseDetermination.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                cseDetermination.camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                cseDetermination.camera.setPipeline(cseDetermination.csePipeline);

                cseDetermination.camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
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
            sleep(100);
            moves.linearMove(5, -1, -1, -1, -1);
            sleep(100);
            moves.rotateArm(110);
            sleep(100);
            moves.linearMove(4,1,1,1,1);

            cseDetermination.camera.setPipeline(cseDetermination.csePipeline);
            sleep(3000);
            int position = cseDetermination.csePipeline.getAnalysis();
            telemetry.addData("Position", position);
            telemetry.update();

            moves.linearMove(3, -1, -1, -1, -1);
            sleep(100);
            moves.linearMove(3,1,-1,-1,1);
            sleep(100);
            moves.linearMove(9, 1, -1, 1, -1);
            sleep(100);

            if (position == 0){
                moves.rotateArm(90);
                sleep(100);
                moves.linearMove(21,-1,-1,-1,-1);


            } else if (position == 1){
                moves.rotateArm(60);
                sleep(300);
                moves.linearMove(22, -1, -1, -1, -1);

            } else {
                moves.rotateArm(50);
                sleep(100);
                moves.linearMove(24, -1, -1, -1, -1);

            }

            robot.releaseBlock();
            sleep(100);
            moves.linearMove(10,1,1,1,1);
            sleep(100);
            moves.linearMove(10,1,-1,1,-1);
            sleep(100);
            moves.linearMove(45,1,1,1,1);
            sleep(100);
            moves.rotateArm(-180);

            telemetry.update();

            counter++;
        }
    }
}
