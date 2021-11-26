package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Robot {

    //Hardware
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;

    public DcMotor leftArm = null;
    public DcMotor rightArm = null;

    public DcMotor carousel = null;

    public Servo leftHand = null;
    public Servo rightHand = null;

    public BNO055IMU imu = null;

    //local OpMode members
    HardwareMap hw = null;
    private ElapsedTime runtime = new ElapsedTime();

    public Robot(){

    }

    public void init(HardwareMap h, String mode){
        hw = h;

        //Initialize hardware
        frontRight = hw.dcMotor.get("motor1");
        frontLeft = hw.dcMotor.get("motor2");
        backRight = hw.dcMotor.get("motor3");
        backLeft = hw.dcMotor.get("motor4");
        rightArm = hw.dcMotor.get("motor5");
        leftArm = hw.dcMotor.get("motor6");
        rightHand = hw.servo.get("rightHand");
        leftHand = hw.servo.get("leftHand");
        carousel = hw.dcMotor.get("carrouselMotor");
        imu = hw.get(BNO055IMU.class, "imu");


        //Set motor direction
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection((DcMotor.Direction.FORWARD));
        backRight.setDirection(DcMotor.Direction.FORWARD);
        rightArm.setDirection(DcMotor.Direction.FORWARD);
        leftArm.setDirection(DcMotor.Direction.FORWARD);
        rightHand.setDirection(Servo.Direction.FORWARD);
        leftHand.setDirection(Servo.Direction.REVERSE);
        carousel.setDirection(DcMotor.Direction.FORWARD);

        //Set motor power to zero
        setAllWheelPower(0);
        rightArm.setPower(0);
        leftArm.setPower(0);
        carousel.setPower(0);

        //Set zero power behavior on motors
        if(mode.equals("auto")){
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        rightArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        carousel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Initialize imu and parameters
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;


    }

    public void setWheelPower(double p1, double p2, double p3, double p4){
        frontLeft.setPower(p1);
        frontRight.setPower(p2);
        backLeft.setPower(p3);
        backRight.setPower(p4);
    }

    public void setAllWheelPower(double p){
        frontRight.setPower(p);
        frontLeft.setPower(p);
        backRight.setPower(p);
        backLeft.setPower(p);
    }
}
