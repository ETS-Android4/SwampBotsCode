package org.firstinspires.ftc.teamcode.tests;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "AutoStraferTest", group = "Test")
public class AutoStraferTest extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private DcMotor rightArm;
    private DcMotor leftArm;

    private Servo rightHand;
    private Servo leftHand;

    private NormalizedColorSensor colorSensor;

    static final double TICKS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.93701;
    static final double TICKS_PER_INCH = TICKS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);

    //Movement Methods

    @Override
    public void runOpMode() throws InterruptedException{

        //Setting up and initializing hardware
        configureMotors(hardwareMap, "motor1", "motor2", "motor3", "motor4", "motor5", "motor6", "rightHand", "leftHand");
        setMotorDirection(FORWARD, REVERSE);
        setZeroPowerBehavior(BRAKE);
        setMotorPower(0);

        setMotorEncoderMode(STOP_AND_RESET_ENCODER);
        setMotorEncoderMode(RUN_USING_ENCODER);

        leftHand.setPosition(0.6);
        rightHand.setPosition(0.26);

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color_sensor");

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();
        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        //Motor Run to Position

        /* Encoder for Movement
        int leftTargetPosition;
        int rightTargetPosition;
        int leftInches = 280;
        int rightInches = 280;

        leftTargetPosition = frontLeft.getCurrentPosition() + (int)(leftInches * TICKS_PER_INCH);
        rightTargetPosition = frontRight.getCurrentPosition() + (int)(rightInches * TICKS_PER_INCH);
        frontLeft.setTargetPosition(leftTargetPosition);
        frontRight.setTargetPosition(rightTargetPosition);

        telemetry.addData("Target Position: ", leftTargetPosition);
        telemetry.addData("Target Position: ", rightTargetPosition);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        */

        waitForStart();

        while (opModeIsActive()){



            colorSensor.setGain(5);
            final float[] hsvValues = new float[3];

            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            Color.colorToHSV(colors.toColor(), hsvValues);

            /*Outputs Color Data
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




            if ((colors.red/colors.blue) > 1.5 && (colors.red/colors.green) > 1.5){
                //Stops Motors
                stopMoving();
            } else {
                //Move Forward
                moveForward(0.6);
            }

        }
    }

    public void strafeLeft(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public void strafeRight(double power){
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    public void moveForward(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    public void moveBackward(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(-power);
    }

    public void stopMoving(){
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }

    public void configureMotors(HardwareMap hw, String rfName, String lfName, String rbName, String lbName, String raName, String laName, String rhName, String lhName){
        frontRight = hw.dcMotor.get(rfName);
        frontLeft = hw.dcMotor.get(lfName);
        backRight = hw.dcMotor.get(rbName);
        backLeft = hw.dcMotor.get(lbName);
        rightArm = hw.dcMotor.get(raName);
        leftArm = hw.dcMotor.get(laName);
        rightHand = hw.servo.get(rhName);
        leftHand = hw.servo.get(lhName);
    }

    public void setMotorDirection(Direction f, Direction r){
        frontLeft.setDirection(r);
        backLeft.setDirection(r);
        frontRight.setDirection(f);
        backRight.setDirection(f);
        rightArm.setDirection(f);
        leftArm.setDirection(f);
        rightHand.setDirection(Servo.Direction.FORWARD);
        leftHand.setDirection(Servo.Direction.REVERSE);
    }

    public void setZeroPowerBehavior(ZeroPowerBehavior z){
        frontRight.setZeroPowerBehavior(z);
        frontLeft.setZeroPowerBehavior(z);
        backRight.setZeroPowerBehavior(z);
        backLeft.setZeroPowerBehavior(z);
        rightArm.setZeroPowerBehavior(z);
        leftArm.setZeroPowerBehavior(z);
    }

    public void setMotorPower(int n){
        frontRight.setPower(n);
        frontLeft.setPower(n);
        backRight.setPower(n);
        backLeft.setPower(n);
        rightArm.setPower(n);
        leftArm.setPower(n);
    }

    public void setMotorEncoderMode(RunMode r) {
        frontRight.setMode(r);
        frontLeft.setMode(r);
        backRight.setMode(r);
        backLeft.setMode(r);
        rightArm.setMode(r);
        leftArm.setMode(r);
    }
}
