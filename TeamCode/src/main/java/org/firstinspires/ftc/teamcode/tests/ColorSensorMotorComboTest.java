package org.firstinspires.ftc.teamcode.tests;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@Autonomous(name = "ColorSensorMotorComboTest", group = "Test")
public class ColorSensorMotorComboTest extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;
   // OpticalDistanceSensor odsSensor;  // Hardware Device Object

    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.93701;
    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);


    @Override
    public void runOpMode() throws InterruptedException{
      //Motor Hardware Map:
        //Defines motors and direction
        frontRight = hardwareMap.dcMotor.get("motor1");
        frontLeft = hardwareMap.dcMotor.get("motor2");
        backRight = hardwareMap.dcMotor.get("motor3");
        backLeft = hardwareMap.dcMotor.get("motor4");

        //odsSensor = hardwareMap.get(OpticalDistanceSensor.class, "sensor_ods");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection((DcMotor.Direction.FORWARD));
        backRight.setDirection(DcMotor.Direction.FORWARD);

        //Encoders
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Set ZERO POWER BEHAVIOR
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set Motors to Use No Power
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
        //Motor Run to Position

        /*
        int leftTargetPosition;
        int rightTargetPosition;
        int leftInches = 280;
        int rightInches = 280;

        leftTargetPosition = frontLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
        rightTargetPosition = frontRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
        frontLeft.setTargetPosition(leftTargetPosition);
        frontRight.setTargetPosition(rightTargetPosition);
        */
        waitForStart();

        while (opModeIsActive()){

            //Color Sensor Hardwaremap

            NormalizedColorSensor colorSensor;
            colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color_sensor");
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


            //telemetry.addData("Target Position: ", leftTargetPosition);
            //telemetry.addData("Target Position: ", rightTargetPosition);

            //frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if ((colors.red/colors.blue) > 1.5 && (colors.red/colors.green) > 1.5){
                frontRight.setPower(0);
                frontLeft.setPower(0);
            } else {
                frontLeft.setPower(Math.abs(0.6));
                frontRight.setPower(Math.abs(0.6));
            }

        }
    }
}
