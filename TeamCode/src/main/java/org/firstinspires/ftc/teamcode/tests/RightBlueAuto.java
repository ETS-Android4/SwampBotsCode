package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "RightBlueAuto", group = "Blue")
public class RightBlueAuto extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

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

        //Counters for movement methods
        int forwardCount = 0;
        int backwardCount = 0;
        int strafeRightCount = 0;
        int strafeleftCount = 0;

        waitForStart();

        while (opModeIsActive()){
            telemetry.addData("Path0",  "Starting at %7d ", frontRight.getCurrentPosition());
            telemetry.update();

            if (backwardCount == 0){
                moveBackward(5);
                backwardCount++;
            }

            if (strafeleftCount == 0){
                strafeLeft(22);
                strafeleftCount++;
            }




        }
    }

    public void moveForward(int inches){
        int targetInches = inches;

        int FLtargetPosition = frontLeft.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
        int FRtargetPosition = frontRight.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
        int BLtargetPosition = backLeft.getCurrentPosition() + (int)(targetInches * COUNTS_PER_INCH);
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

        frontLeft.setPower(1);
        backLeft.setPower(1);
        frontRight.setPower(1);
        backRight.setPower(1);

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
        
        frontLeft.setPower(-1);
        backLeft.setPower(-1);
        frontRight.setPower(-1);
        backRight.setPower(-1);
        
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

        frontLeft.setPower(1);
        backLeft.setPower(-1);
        frontRight.setPower(-1);
        backRight.setPower(1);

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

    }

}
