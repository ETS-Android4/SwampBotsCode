package org.firstinspires.ftc.teamcode.tests;

import android.graphics.Color;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "RightRedAuto", group = "Red")
public class RightRedAuto extends LinearOpMode {

    Robot robot = new Robot();
    CSEDetermination cseDetermination = new CSEDetermination();

    public NormalizedColorSensor colorSensor;

    private int CSEPosition;

    static final double TICKS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.93701;
    static final double TICKS_PER_INCH_REV = TICKS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);

    static final double TICKS_PER_MOTOR_HEX = 288.0;
    static final double TICKS_PER_DEGREE_HEX = TICKS_PER_MOTOR_HEX / 360.0;


    @Override
    public void runOpMode() throws InterruptedException{

        //Defines motors and direction
        robot.init(hardwareMap, "auto");
        cseDetermination.init(hardwareMap, "red");

        //Encoders
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);
        setWheelEncoderMode(RUN_USING_ENCODER);
        setArmEncoderMode(STOP_AND_RESET_ENCODER);
        setArmEncoderMode(RUN_USING_ENCODER);


        //Set Motors to Use No Power
        robot.setAllWheelPower(0);
        setArmPower(0);
        robot.carousel.setPower(0);
        robot.leftHand.setPosition(0.75);
        robot.rightHand.setPosition(0.44);

        cseDetermination.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                cseDetermination.camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                cseDetermination.camera.setPipeline(cseDetermination.csePipelineR);

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
            grabBlock();
            sleep(100);
            linearMove(5, -1, -1, -1, -1);
            sleep(100);
            rotateArm(110);
            sleep(100);
            linearMove(4,1,1,1,1);

            cseDetermination.camera.setPipeline(cseDetermination.csePipelineR);
            sleep(3000);
            int position = cseDetermination.csePipelineR.getAnalysis();
            telemetry.addData("Position", position);
            telemetry.update();

            linearMove(3, -1, -1, -1, -1);
            sleep(100);
            linearMove(3,-1,1,1,-1);
            sleep(100);
            linearMove(9, -1, 1, -1, 1);
            sleep(100);

            if (position == 0){
                rotateArm(90);
                sleep(100);
                linearMove(21,-1,-1,-1,-1);
                releaseBlock();


            } else if (position == 1){
                rotateArm(60);
                sleep(300);
                linearMove(22, -1, -1, -1, -1);
                releaseBlock();

            } else {
                rotateArm(50);
                sleep(100);
                linearMove(24, -1, -1, -1, -1);
                releaseBlock();

            }

            telemetry.update();
            sleep(100);
            linearMove(11,1,1,1,1);
            sleep(100);
            linearMove(7,-1,1,-1,1);
            sleep(100);
            linearMove(45,1,1,1,1);
            sleep(100);
            rotateArm(-180);

            counter++;
        }
    }

    public void linearMove(int inches, int flSign, int frSign, int blSign, int brSign){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = robot.frontLeft.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_REV);

        robot.frontLeft.setTargetPosition(flSign * targetPosition);
        robot.frontRight.setTargetPosition(frSign * targetPosition);
        robot.backLeft.setTargetPosition(blSign * targetPosition);
        robot.backRight.setTargetPosition(brSign * targetPosition);

        setWheelEncoderMode(RUN_TO_POSITION);
        robot.setAllWheelPower(0.5);

        while (opModeIsActive() &&
                (robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy() && robot.backLeft.isBusy())) {

        }

        robot.setAllWheelPower(0);
        setWheelEncoderMode(RUN_USING_ENCODER);
    }

    public void rotateArm(int degrees){
        setArmEncoderMode(STOP_AND_RESET_ENCODER);

        int targetAngle = robot.leftArm.getCurrentPosition() + (int)(-(degrees - 12) * TICKS_PER_DEGREE_HEX);
        robot.leftArm.setTargetPosition(targetAngle);
        robot.rightArm.setTargetPosition(targetAngle);

        setArmEncoderMode(RUN_TO_POSITION);
        setArmPower(0.3);

        while (opModeIsActive() &&
                (robot.leftArm.isBusy() && robot.rightArm.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Rotating to %7d ", targetAngle);
            telemetry.addData("Path2", "Current Position %7d:%7d",
                    robot.leftArm.getCurrentPosition(),
                    robot.rightArm.getCurrentPosition());
            telemetry.update();
        }

        setArmPower(0);
        setArmEncoderMode(RUN_USING_ENCODER);
    }

    public void grabBlock(){
        robot.leftHand.setPosition(0.85);
        robot.rightHand.setPosition(0.54);
    }

    public void releaseBlock(){
        robot.leftHand.setPosition(0.75);
        robot.rightHand.setPosition(0.44);
    }


    public void setWheelEncoderMode(RunMode r){
        robot.frontLeft.setMode(r);
        robot.frontRight.setMode(r);
        robot.backLeft.setMode(r);
        robot.backRight.setMode(r);
    }

    public void setArmEncoderMode(RunMode r){
        robot.rightArm.setMode(r);
        robot.leftArm.setMode(r);
    }

    public void setArmPower(double p){
        robot.rightArm.setPower(p);
        robot.leftArm.setPower(p);
    }
}
