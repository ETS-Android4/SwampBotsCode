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


@Autonomous(name = "LeftBlueAuto", group = "Blue")
public class LeftBlueAuto extends LinearOpMode {

    Robot robot = new Robot();
    private ElapsedTime runtime = new ElapsedTime();

    public NormalizedColorSensor colorSensor;
    private BNO055IMU imu;

    static final double TICKS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.93701;
    static final double TICKS_PER_INCH_REV = TICKS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);

    static final double TICKS_PER_MOTOR_HEX = 288.0;
    static final double TICKS_PER_DEGREE_HEX = TICKS_PER_MOTOR_HEX / 360.0;


    @Override
    public void runOpMode() throws InterruptedException{

        //Defines motors and direction
        robot.init(hardwareMap, "auto");
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        //Encoders
        setWheelEncoderMode(STOP_AND_RESET_ENCODER);
        setWheelEncoderMode(RUN_USING_ENCODER);
        setArmEncoderMode(STOP_AND_RESET_ENCODER);
        setArmEncoderMode(RUN_USING_ENCODER);


        //Set Motors to Use No Power
        setWheelPower(0);
        setArmPower(0);
        robot.carousel.setPower(0);
        robot.leftHand.setPosition(0.75);
        robot.rightHand.setPosition(0.44);

        //set up imu
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        //Counters for movement methods
        int counter = 0;
        waitForStart();

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

             */

            linearMove(10, 1, 1, 1, 1);
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
        setWheelPower(0.5);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
                (robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy() && robot.backLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d ", targetPosition);
            telemetry.addData("Path2", "Current Position %7d:%7d:%7d:%7d",
                    robot.frontRight.getCurrentPosition(),
                    robot.frontLeft.getCurrentPosition(),
                    robot.backRight.getCurrentPosition(),
                    robot.backLeft.getCurrentPosition());

            telemetry.update();
        }

        setWheelPower(0);
        setWheelEncoderMode(RUN_USING_ENCODER);
    }

    public void rotateArm(int degrees){
        setArmEncoderMode(STOP_AND_RESET_ENCODER);

        int targetAngle = robot.leftArm.getCurrentPosition() + (int)(degrees * TICKS_PER_DEGREE_HEX);
        robot.leftArm.setTargetPosition(targetAngle);
        robot.rightArm.setTargetPosition(targetAngle);

        setArmEncoderMode(RUN_TO_POSITION);
        setArmPower(0.3);

        while (opModeIsActive() &&
                (runtime.seconds() < 30) &&
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


    public float getAngle(){
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    public void setWheelEncoderMode(RunMode r){
        robot.frontLeft.setMode(r);
        robot.frontRight.setMode(r);
        robot.backLeft.setMode(r);
        robot.backRight.setMode(r);
    }

    public void setWheelPower(double p){
        robot.frontLeft.setPower(p);
        robot.frontRight.setPower(p);
        robot.backLeft.setPower(p);
        robot.backRight.setPower(p);
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
