package org.firstinspires.ftc.teamcode.tests;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "RightBlueAutoTimerTest", group = "Blue")
public class RightBlueAutoTimerTest extends LinearOpMode {
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

            counter++;

            //moveForward(20);
            //sleep(500);
            //moveBackward(20);
            //sleep(500);
            //strafeRight(20);
            //sleep(500);
            pivotRight(19);


        }
    }

    public void moveForward(int inches){
        int rightTargetInches = inches;
        int leftTargetInches = inches;

        int FLtargetPosition = frontLeft.getCurrentPosition() + (int)(leftTargetInches * COUNTS_PER_INCH);
        int FRtargetPosition = frontRight.getCurrentPosition() + (int)(rightTargetInches * COUNTS_PER_INCH);
        int BLtargetPosition = backLeft.getCurrentPosition() + (int)(leftTargetInches * COUNTS_PER_INCH);
        int BRtargetPosition = backRight.getCurrentPosition() + (int)(rightTargetInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(FLtargetPosition);
        frontRight.setTargetPosition(FRtargetPosition);
        backLeft.setTargetPosition(BLtargetPosition);
        backRight.setTargetPosition(BRtargetPosition);

        telemetry.addData("Front Left Target Position: ", FLtargetPosition);
        telemetry.addData("Front Right Target Position: ", FRtargetPosition);
        telemetry.addData("Back Left Target Position: ", BLtargetPosition);
        telemetry.addData("Back Right Target Position: ", BRtargetPosition);
        telemetry.update();

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(0.5);
        backLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backRight.setPower(0.5);

        while (opModeIsActive() &&
                (runtime.seconds() < 5) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d : %7d", FRtargetPosition, FLtargetPosition, BRtargetPosition, BLtargetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());

            telemetry.update();
        }

        // Stop all motion;

        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        // Turn off RUN_TO_POSITION
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveBackward(int inches){
        int targetInches = inches;
        
        int FLtargetPosition = frontLeft.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);
        int FRtargetPosition = frontRight.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);
        int BLtargetPosition = backLeft.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);
        int BRtargetPosition = backRight.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(FLtargetPosition);
        frontRight.setTargetPosition(FRtargetPosition);
        backLeft.setTargetPosition(BLtargetPosition);
        backRight.setTargetPosition(BRtargetPosition);

        telemetry.addData("Front Left Target Position: ", FLtargetPosition);
        telemetry.addData("Front Right Target Position: ", FRtargetPosition);
        telemetry.addData("Back Left Target Position: ", BLtargetPosition);
        telemetry.addData("Back Right Target Position: ", BRtargetPosition);
        telemetry.update();
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontLeft.setPower(-0.5);
        backLeft.setPower(-0.5);
        frontRight.setPower(-0.5);
        backRight.setPower(-0.5);

        while (opModeIsActive() &&
                (runtime.seconds() < 5) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d : %7d", FRtargetPosition, FLtargetPosition, BRtargetPosition, BLtargetPosition);
            telemetry.addData("Path2",  "Running at %7d :%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());

            telemetry.update();
        }

        // Stop all motion;

        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        // Turn off RUN_TO_POSITION
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void strafeRight(int inches){
        int targetInches = inches;

        int FLtargetPosition = frontLeft.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
        int FRtargetPosition = frontRight.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);
        int BLtargetPosition = backLeft.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);
        int BRtargetPosition = backRight.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(FLtargetPosition);
        frontRight.setTargetPosition(FRtargetPosition);
        backLeft.setTargetPosition(BLtargetPosition);
        backRight.setTargetPosition(BRtargetPosition);

        telemetry.addData("Front Left Target Position: ", FLtargetPosition);
        telemetry.addData("Front Right Target Position: ", FRtargetPosition);
        telemetry.addData("Back Left Target Position: ", BLtargetPosition);
        telemetry.addData("Back Right Target Position: ", BRtargetPosition);
        telemetry.update();

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(0.5);
        backLeft.setPower(-0.3);
        frontRight.setPower(-0.);
        backRight.setPower(0.24);

        while (opModeIsActive() &&
                (runtime.seconds() < 10) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d : %7d", FRtargetPosition, FLtargetPosition, BRtargetPosition, BLtargetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());

            telemetry.update();
        }

        // Stop all motion;

        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        // Turn off RUN_TO_POSITION
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void strafeLeft(int inches){
        int targetInches = inches;

        int FLtargetPosition = frontLeft.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);
        int FRtargetPosition = frontRight.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
        int BLtargetPosition = backLeft.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
        int BRtargetPosition = backRight.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(FLtargetPosition);
        frontRight.setTargetPosition(FRtargetPosition);
        backLeft.setTargetPosition(BLtargetPosition);
        backRight.setTargetPosition(BRtargetPosition);

        telemetry.addData("Front Left Target Position: ", FLtargetPosition);
        telemetry.addData("Front Right Target Position: ", FRtargetPosition);
        telemetry.addData("Back Left Target Position: ", BLtargetPosition);
        telemetry.addData("Back Right Target Position: ", BRtargetPosition);
        telemetry.update();

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(-1);
        backLeft.setPower(1);
        frontRight.setPower(1);
        backRight.setPower(-1);

        if (frontLeft.getCurrentPosition() == FLtargetPosition){
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void pivotRight(int inches){
        int targetInches = inches;

        int FLtargetPosition = frontLeft.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
        int FRtargetPosition = frontRight.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);
        int BLtargetPosition = backLeft.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
        int BRtargetPosition = backRight.getCurrentPosition() + (int)(-targetInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(FLtargetPosition);
        frontRight.setTargetPosition(FRtargetPosition);
        backLeft.setTargetPosition(BLtargetPosition);
        backRight.setTargetPosition(BRtargetPosition);

        telemetry.addData("Front Left Target Position: ", FLtargetPosition);
        telemetry.addData("Front Right Target Position: ", FRtargetPosition);
        telemetry.addData("Back Left Target Position: ", BLtargetPosition);
        telemetry.addData("Back Right Target Position: ", BRtargetPosition);
        telemetry.update();

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(0.1);
        backLeft.setPower(0.1);
        frontRight.setPower(0.1);
        backRight.setPower(0.1);

        while (opModeIsActive() &&
                (runtime.seconds() < 10) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backRight.isBusy() && backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d : %7d", FRtargetPosition, FLtargetPosition, BRtargetPosition, BLtargetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    frontRight.getCurrentPosition(),
                    frontLeft.getCurrentPosition(),
                    backRight.getCurrentPosition(),
                    backLeft.getCurrentPosition());

            telemetry.update();
        }

        // Stop all motion;

        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        // Turn off RUN_TO_POSITION
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

}
