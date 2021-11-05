package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "FirstBasicMovement", group = "Test")
public class FirstBasicMovement extends LinearOpMode {

    //Chassis Motors
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;
    //Arm Motors
    private DcMotor rightArm;
    private DcMotor leftArm;
    //Hand Servos
    private Servo leftHand;
    private Servo rightHand;


    @Override
    public void runOpMode() throws InterruptedException{
      //Hardware Map:
        //Defines motors, servos, and direction
        frontRight = hardwareMap.dcMotor.get("motor1");
        frontLeft = hardwareMap.dcMotor.get("motor2");
        backRight = hardwareMap.dcMotor.get("motor3");
        backLeft = hardwareMap.dcMotor.get("motor4");

        rightArm = hardwareMap.dcMotor.get("motor5");
        leftArm = hardwareMap.dcMotor.get("motor6");

        leftHand = hardwareMap.servo.get("leftHand");
        rightHand = hardwareMap.servo.get("rightHand");


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection((DcMotor.Direction.FORWARD));
        backRight.setDirection(DcMotor.Direction.FORWARD);

        rightArm.setDirection(DcMotor.Direction.FORWARD);
        leftArm.setDirection(DcMotor.Direction.FORWARD);


        leftHand.setDirection(Servo.Direction.FORWARD);
        rightHand.setDirection(Servo.Direction.REVERSE);

        //Encoders
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Set ZERO POWER BEHAVIOR
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set Motors to Use No Power
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        rightArm.setPower(0);
        leftArm.setPower(0);

        //Setting Initial Servo Positions
        leftHand.setPosition(0.6);
        rightHand.setPosition(0.26);

      //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            double vertical;
            double horizontal;
            double pivot;

            double armVertical;

            double leftHandCurrentPos;
            double rightHandCurrentPos;


            //Defines direction that motors move based on their orientation to one another
            vertical = gamepad1.left_stick_y;
            horizontal = -gamepad1.left_stick_x;
            pivot = -gamepad1.right_stick_x;

            frontRight.setPower(-(-pivot + vertical - horizontal));
            backRight.setPower(-(-pivot + vertical + horizontal));
            frontLeft.setPower(-(pivot + vertical + horizontal));
            backLeft.setPower(-(pivot + vertical - horizontal));

            armVertical = gamepad2.left_stick_y;
            leftArm.setPower(armVertical * 0.6);
            rightArm.setPower(armVertical * 0.6);

            //Servo Controls
            telemetry.addData("Right Hand Position: ", rightHand.getPosition());
            telemetry.addData("Left Hand Position: ", leftHand.getPosition());

            if (gamepad2.left_bumper){ //open position
                leftHand.setPosition(0.6);
                rightHand.setPosition(0.26);
                leftHandCurrentPos = leftHand.getPosition();
                rightHandCurrentPos = rightHand.getPosition();
            }

            if (gamepad2.right_bumper){ //closed position
                leftHand.setPosition(0.52);
                rightHand.setPosition(0.19);
                leftHandCurrentPos = leftHand.getPosition();
                rightHandCurrentPos = rightHand.getPosition();
            }


            //Outputs how much power each motor is experiencing to the driver hub
            telemetry.addData("frontRight Power: ", frontRight.getPower());
            telemetry.addData("backRight Power: ", backRight.getPower());
            telemetry.addData("frontLeft Power: ", frontLeft.getPower());
            telemetry.addData("backLeft Power: " , backLeft.getPower());
            telemetry.addData("left Arm Motor Power: ", leftArm.getPower());
            telemetry.addData("Right Arm Motor Power: ", rightArm.getPower());
            telemetry.addData("left Hand Servo Position: ", leftHand.getPosition());
            telemetry.addData("Right Hand Servo Position", rightHand.getPosition());

            telemetry.update();
            idle();
        }
    }
}
