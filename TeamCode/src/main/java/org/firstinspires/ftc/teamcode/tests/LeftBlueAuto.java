package org.firstinspires.ftc.teamcode.tests;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "RightRedAuto", group = "Red")
public class RightRedAuto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor cMotor;
    private NormalizedColorSensor colorSensor;

    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.93701;
    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);


    @Override
    public void runOpMode() throws InterruptedException{
        //Hardware Map:
        //Defines motors and direction
        frontRight = hardwareMap.dcMotor.get("motor1");
        frontLeft = hardwareMap.dcMotor.get("motor2");
        backRight = hardwareMap.dcMotor.get("motor3");
        backLeft = hardwareMap.dcMotor.get("motor4");
        cMotor = hardwareMap.dcMotor.get("carrouselMotor");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "frontRightColor");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection((DcMotor.Direction.FORWARD));
        backRight.setDirection(DcMotor.Direction.FORWARD);
        cMotor.setDirection(DcMotor.Direction.FORWARD);

        //Encoders
        frontRight.setMode(RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(RunMode.RUN_USING_ENCODER);
        backRight.setMode(RunMode.RUN_USING_ENCODER);
        backLeft.setMode(RunMode.RUN_USING_ENCODER);

        //Set ZERO POWER BEHAVIOR
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        cMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set Motors to Use No Power
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        cMotor.setPower(0);

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        //Counters for movement methods
        int counter = 0;
        waitForStart();

        while (opModeIsActive() && counter == 0){


            if (colorSensor instanceof SwitchableLight) {
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
            telemetry.update();

            strafeLeft(18);
            sleep(500);
            moveForward(60);
            counter++;
        }
    }






    public void moveForward(int inches){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = frontLeft.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(targetPosition);
        frontRight.setTargetPosition(targetPosition);
        backLeft.setTargetPosition(targetPosition);
        backRight.setTargetPosition(targetPosition);

        telemetry.addData("Front Left Target Position: ", targetPosition);
        telemetry.addData("Front Right Target Position: ", targetPosition);
        telemetry.addData("Back Left Target Position: ", targetPosition);
        telemetry.addData("Back Right Target Position: ", targetPosition);
        telemetry.update();

        //Run the encoder and set power for robot to move
        setWheelEncoderMode(RUN_TO_POSITION);
        setWheelPower(1);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d ", targetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());

            telemetry.update();
        }

        // Stop all motion and stop running the encoder
        setWheelPower(0);
        setWheelEncoderMode(RUN_USING_ENCODER);
    }

    public void moveBackward(int inches){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = frontLeft.getCurrentPosition() + (int)(-inches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(targetPosition);
        frontRight.setTargetPosition(targetPosition);
        backLeft.setTargetPosition(targetPosition);
        backRight.setTargetPosition(targetPosition);

        telemetry.addData("Front Left Target Position: ", targetPosition);
        telemetry.addData("Front Right Target Position: ", targetPosition);
        telemetry.addData("Back Left Target Position: ", targetPosition);
        telemetry.addData("Back Right Target Position: ", targetPosition);
        telemetry.update();

        setWheelEncoderMode(RUN_TO_POSITION);

        setWheelPower(0.5);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d ", targetPosition);
            telemetry.addData("Path2",  "Running at %7d :%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());

            telemetry.update();
        }

        // Stop all motion;
        setWheelPower(0);

        // Turn off RUN_TO_POSITION
        setWheelEncoderMode(RUN_USING_ENCODER);
    }

    public void strafeRight(int inches){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int forwardTargetPosition = frontLeft.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        int backwardTargetPosition = frontRight.getCurrentPosition() + (int)(-inches * COUNTS_PER_INCH);


        frontLeft.setTargetPosition(backwardTargetPosition);
        frontRight.setTargetPosition(forwardTargetPosition);
        backLeft.setTargetPosition(forwardTargetPosition);
        backRight.setTargetPosition(backwardTargetPosition);

        telemetry.addData("Front Left Target Position: ", forwardTargetPosition);
        telemetry.addData("Front Right Target Position: ", backwardTargetPosition);
        telemetry.addData("Back Left Target Position: ", backwardTargetPosition);
        telemetry.addData("Back Right Target Position: ", forwardTargetPosition);
        telemetry.update();

        setWheelEncoderMode(RUN_TO_POSITION);
        setWheelPower(0.5);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d : %7d", forwardTargetPosition, backwardTargetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());
            telemetry.addData("FrontR Pow", frontRight.getPower());
            telemetry.addData("BackR Pow", backRight.getPower());
            telemetry.addData("FrontL Pow", frontLeft.getPower());
            telemetry.addData("BackL Pow", backLeft.getPower());
            telemetry.update();
        }

        // Stop all motion;
        setWheelPower(0);
        setWheelEncoderMode(RUN_USING_ENCODER);

    }

    public void strafeLeft(int inches){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int forwardTargetPosition = frontLeft.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        int backwardTargetPosition = frontRight.getCurrentPosition() + (int)(-inches * COUNTS_PER_INCH);


        frontLeft.setTargetPosition(forwardTargetPosition);
        frontRight.setTargetPosition(backwardTargetPosition);
        backLeft.setTargetPosition(backwardTargetPosition);
        backRight.setTargetPosition(forwardTargetPosition);

        telemetry.addData("Front Left Target Position: ", forwardTargetPosition);
        telemetry.addData("Front Right Target Position: ", backwardTargetPosition);
        telemetry.addData("Back Left Target Position: ", backwardTargetPosition);
        telemetry.addData("Back Right Target Position: ", forwardTargetPosition);
        telemetry.update();

        setWheelEncoderMode(RUN_TO_POSITION);
        setWheelPower(0.35);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d : %7d", forwardTargetPosition, backwardTargetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());
            telemetry.addData("FrontR Pow", frontRight.getPower());
            telemetry.addData("BackR Pow", backRight.getPower());
            telemetry.addData("FrontL Pow", frontLeft.getPower());
            telemetry.addData("BackL Pow", backLeft.getPower());
            telemetry.update();
        }

        // Stop all motion;
        setWheelPower(0);
        setWheelEncoderMode(RUN_USING_ENCODER);

    }

    public void pivotRight(int inches){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = frontLeft.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(-targetPosition);
        frontRight.setTargetPosition(targetPosition);
        backLeft.setTargetPosition(-targetPosition);
        backRight.setTargetPosition(targetPosition);

        telemetry.addData("Front Left Target Position: ", -targetPosition);
        telemetry.addData("Front Right Target Position: ", targetPosition);
        telemetry.addData("Back Left Target Position: ", -targetPosition);
        telemetry.addData("Back Right Target Position: ", targetPosition);
        telemetry.update();

        setWheelEncoderMode(RUN_TO_POSITION);
        setWheelPower(0.5);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d : %7d", targetPosition, -targetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());
            telemetry.addData("RightF Pow", frontRight.getPower());
            telemetry.addData("RightB Pow", backRight.getPower());
            telemetry.addData("RightL Pow", frontLeft.getPower());
            telemetry.addData("BackL Pow", backLeft.getPower());

            telemetry.update();
        }

        // Stop all motion;
        setWheelPower(0);

        // Turn off RUN_TO_POSITION
        setWheelEncoderMode(RUN_USING_ENCODER);

    }

    public void setWheelEncoderMode(RunMode r){
        frontLeft.setMode(r);
        frontRight.setMode(r);
        backLeft.setMode(r);
        backRight.setMode(r);
    }

    public void setWheelPower(double p){
        frontLeft.setPower(p);
        frontRight.setPower(p);
        backLeft.setPower(p);
        backRight.setPower(p);
    }
}
