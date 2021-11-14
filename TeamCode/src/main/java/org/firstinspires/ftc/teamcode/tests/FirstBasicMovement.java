package org.firstinspires.ftc.teamcode.tests;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
    
    private DcMotor carousselMotor;


    @Override
    public void runOpMode() throws InterruptedException{

        //Set up motors and initialization
        configureMotors(hardwareMap, "motor1", "motor2", "motor3", "motor4", "motor5", "motor6", "rightHand", "leftHand", "carrouselMotor");
        setMotorDirection(FORWARD, REVERSE);
        setZeroPowerBehavior(BRAKE);
        setMotorPower(0);

        setMotorEncoderMode(STOP_AND_RESET_ENCODER);
        setMotorEncoderMode(RUN_USING_ENCODER);

        //Setting Initial Servo Positions
        leftHand.setPosition(0.75);
        rightHand.setPosition(0.44);

      //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            double vertical;
            double horizontal;
            double pivot;

            double armVertical;
            
            double carousselMotorPowerF = gamepad1.right_trigger;
            double carousselMotorPowerR = gamepad1.left_trigger;

            double leftHandCurrentPos;
            double rightHandCurrentPos;


            //Defines direction that motors move based on their orientation to one another
            vertical = -gamepad1.left_stick_y;
            horizontal = -gamepad1.left_stick_x;
            pivot = gamepad1.right_stick_x;

            frontRight.setPower(-pivot + vertical - horizontal);
            backRight.setPower(-pivot + vertical + horizontal);
            frontLeft.setPower(pivot + vertical + horizontal);
            backLeft.setPower(pivot + vertical - horizontal);

            armVertical = gamepad2.left_stick_y;
            leftArm.setPower(armVertical * 0.6);
            rightArm.setPower(armVertical * 0.6);
            
            if(gamepad1.right_trigger > 0){
                carousselMotor.setPower(carousselMotorPowerF);
            } else if(gamepad1.left_trigger > 0){
                carousselMotor.setPower(-carousselMotorPowerR);
            } else {
                carousselMotor.setPower(0);
            }

            //Servo Controls
            telemetry.addData("Right Hand Position: ", rightHand.getPosition());
            telemetry.addData("Left Hand Position: ", leftHand.getPosition());

            if (gamepad2.left_bumper){ //closed position
                leftHand.setPosition(0.85);
                rightHand.setPosition(0.54);
                leftHandCurrentPos = leftHand.getPosition();
                rightHandCurrentPos = rightHand.getPosition();
            }

            if (gamepad2.right_bumper){ //opened position
                leftHand.setPosition(0.75);
                rightHand.setPosition(0.44);
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

    public void configureMotors(HardwareMap hw, String rfName, String lfName, String rbName, String lbName, String raName, String laName, String rhName, String lhName, String cName){
        frontRight = hw.dcMotor.get(rfName);
        frontLeft = hw.dcMotor.get(lfName);
        backRight = hw.dcMotor.get(rbName);
        backLeft = hw.dcMotor.get(lbName);
        rightArm = hw.dcMotor.get(raName);
        leftArm = hw.dcMotor.get(laName);
        rightHand = hw.servo.get(rhName);
        leftHand = hw.servo.get(lhName);
        carousselMotor = hw.dcMotor.get(cName);
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
        carousselMotor.setDirection(r);
    }

    public void setZeroPowerBehavior(ZeroPowerBehavior z){
        frontRight.setZeroPowerBehavior(z);
        frontLeft.setZeroPowerBehavior(z);
        backRight.setZeroPowerBehavior(z);
        backLeft.setZeroPowerBehavior(z);
        rightArm.setZeroPowerBehavior(z);
        leftArm.setZeroPowerBehavior(z);
        carousselMotor.setZeroPowerBehavior(z);
    }

    public void setMotorPower(int n){
        frontRight.setPower(n);
        frontLeft.setPower(n);
        backRight.setPower(n);
        backLeft.setPower(n);
        rightArm.setPower(n);
        leftArm.setPower(n);
        carousselMotor.setPower(n);
    }

    public void setMotorEncoderMode(RunMode r) {
        frontRight.setMode(r);
        frontLeft.setMode(r);
        backRight.setMode(r);
        backLeft.setMode(r);
        rightArm.setMode(r);
        leftArm.setMode(r);
        carousselMotor.setMode(r);
    }
}
