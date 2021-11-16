package org.firstinspires.ftc.teamcode.tests;

import android.graphics.Color;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "LeftRedAuto", group = "Red")
public class LeftRedAuto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private DcMotor rightArm;
    private DcMotor leftArm;

    private Servo rightHand;
    private Servo leftHand;

    private DcMotor cMotor;
    private NormalizedColorSensor colorSensor;

    static final double TICKS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.93701;
    static final double TICKS_PER_INCH_REV = TICKS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);

    static final double TICKS_PER_MOTOR_HEX = 288.0;
    static final double TICKS_PER_DEGREE_HEX = TICKS_PER_MOTOR_HEX / 360.0;

    @Override
    public void runOpMode() throws InterruptedException{

        //Defines motors and direction
        configureMotors();
        setMotorDirection();
        setZeroPowerBehavior();

        //Encoders
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);
        setWheelEncoderMode(RUN_USING_ENCODER);
        setArmEncoderMode(STOP_AND_RESET_ENCODER);
        setArmEncoderMode(RUN_USING_ENCODER);


        //Set Motors to Use No Power
        setWheelPower(0);
        setArmPower(0);
        cMotor.setPower(0);
        leftHand.setPosition(0.75);
        rightHand.setPosition(0.44);

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

            /* KEY FOR LINEAR MOVES

                FORWARD -- All 1's
                BACKWARD -- All -1's
                RIGHT -- -1, 1, 1, -1
                LEFT -- 1, -1, -1, 1

             */

            rotateArm(30);
            counter++;
        }
    }

    public void linearMove(int inches, int flSign, int frSign, int blSign, int brSign){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = frontLeft.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_REV);

        frontLeft.setTargetPosition(flSign * targetPosition);
        frontRight.setTargetPosition(frSign * targetPosition);
        backLeft.setTargetPosition(blSign * targetPosition);
        backRight.setTargetPosition(brSign * targetPosition);

        setWheelEncoderMode(RUN_TO_POSITION);
        setWheelPower(0.5);

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

        setWheelPower(0);
        setWheelEncoderMode(RUN_USING_ENCODER);
    }

    public void rotateArm(int degrees){
        setArmEncoderMode(STOP_AND_RESET_ENCODER);

        int targetAngle = leftArm.getCurrentPosition() + (int)(degrees * TICKS_PER_DEGREE_HEX);
        leftArm.setTargetPosition(targetAngle);
        rightArm.setTargetPosition(targetAngle);

        setArmEncoderMode(RUN_TO_POSITION);
        setArmPower(0.3);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
                (leftArm.isBusy() && rightArm.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Rotating to %7d ", targetAngle);
            telemetry.addData("Path2", "Current Position %7d:%7d",
                    leftArm.getCurrentPosition(),
                    rightArm.getCurrentPosition());
            telemetry.update();
        }

        setArmPower(0);
        setArmEncoderMode(RUN_USING_ENCODER);
    }

    public void grabBlock(){
        leftHand.setPosition(0.85);
        rightHand.setPosition(0.54);
    }

    public void releaseBlock(){
        leftHand.setPosition(0.75);
        rightHand.setPosition(0.44);
    }

    public void pivotRight(int inches){
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = frontLeft.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_REV);

        //Run the encoder and set power for robot to move
        frontLeft.setTargetPosition(-targetPosition);
        frontRight.setTargetPosition(targetPosition);
        backLeft.setTargetPosition(-targetPosition);
        backRight.setTargetPosition(targetPosition);

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

    public void setArmEncoderMode(RunMode r){
        rightArm.setMode(r);
        leftArm.setMode(r);
    }

    public void setArmPower(double p){
        rightArm.setPower(p);
        leftArm.setPower(p);
    }



    //Stuff to do before initialization
    public void configureMotors(){
        frontRight = hardwareMap.dcMotor.get("motor1");
        frontLeft = hardwareMap.dcMotor.get("motor2");
        backRight = hardwareMap.dcMotor.get("motor3");
        backLeft = hardwareMap.dcMotor.get("motor4");
        rightArm = hardwareMap.dcMotor.get("motor5");
        leftArm = hardwareMap.dcMotor.get("motor6");
        rightHand = hardwareMap.servo.get("rightHand");
        leftHand = hardwareMap.servo.get("leftHand");
        cMotor = hardwareMap.dcMotor.get("carrouselMotor");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "frontRightColor");
    }

    public void setMotorDirection(){
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection((DcMotor.Direction.FORWARD));
        backRight.setDirection(DcMotor.Direction.FORWARD);
        rightArm.setDirection(DcMotor.Direction.FORWARD);
        leftArm.setDirection(DcMotor.Direction.FORWARD);
        rightHand.setDirection(Servo.Direction.FORWARD);
        leftHand.setDirection(Servo.Direction.REVERSE);
        cMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    public void setZeroPowerBehavior(){
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        cMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
