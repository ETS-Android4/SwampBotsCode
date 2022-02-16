package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.Autonomous.*;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.opencv.core.Point;

public class Movement {
    
    //CONSTANTS FOR ENCODER TICK VALUES
    
    static final double TICKS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.77953;
    static final double TICKS_PER_INCH_REV = TICKS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);
    static final double TICKS_PER_HALF_INCH_REV = TICKS_PER_INCH_REV / 2;

    static final double TICKS_PER_MOTOR_HEX = 1993.6;
    static final double TICKS_PER_DEGREE_HEX = TICKS_PER_MOTOR_HEX / 360.0;

    //static final int RIGHT_REGION_SIDE_COORDINATE = 480;
    //static final int LEFT_REGION_SIDE_COORDINATE = 880;
    static final int TOP_REGION_SIDE_COORDINATE = 500;
    static final int BOTTOM_REGION_SIDE_COORDINATE = 420;
    
    
    
    //For movement forward, backward, pivoting, and strafing. SOON YOU HAVE TO GET NEW METHODS FOR PIVOTING AND STRAFING
    
    public void linearMoveDistance(SampleMecanumDrive drive, Robot robot, double power, double inches, double flSign, double frSign, double blSign, double brSign){
        robot.setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = robot.frontLeft.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_REV);

        robot.frontLeft.setTargetPosition((int)(flSign * targetPosition));
        robot.frontRight.setTargetPosition((int)(frSign * targetPosition));
        robot.backLeft.setTargetPosition((int)(blSign * targetPosition));
        robot.backRight.setTargetPosition((int)(brSign * targetPosition));

        robot.setWheelEncoderMode(RUN_TO_POSITION);
        robot.setAllWheelPower(power);

        while(robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy() && robot.backLeft.isBusy()) {
            drive.update();
        }

        robot.setAllWheelPower(0);
        robot.setWheelEncoderMode(RUN_USING_ENCODER);

    }

    public void linearMoveDistanceHalfInch(Robot robot, int flSign, int frSign, int blSign, int brSign){
        robot.setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = robot.frontLeft.getCurrentPosition() + (int)(TICKS_PER_HALF_INCH_REV);

        robot.frontLeft.setTargetPosition((int)(flSign * targetPosition));
        robot.frontRight.setTargetPosition((int)(frSign * targetPosition));
        robot.backLeft.setTargetPosition((int)(blSign * targetPosition));
        robot.backRight.setTargetPosition((int)(brSign * targetPosition));

        robot.setWheelEncoderMode(RUN_TO_POSITION);
        robot.setAllWheelPower(0.5);

        while(robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy() && robot.backLeft.isBusy()) {

        }

        robot.setAllWheelPower(0);
        robot.setWheelEncoderMode(RUN_USING_ENCODER);
    }

    public void linearMove(SampleMecanumDrive drive, Robot robot, double power, int flSign, int frSign, int blSign, int brSign){
        robot.frontLeft.setPower(power * flSign);
        robot.frontRight.setPower(power * frSign);
        robot.backLeft.setPower(power * blSign);
        robot.backRight.setPower(power * brSign);
        drive.update();

    }
    
    //Method for rotation of arm with encoders 
    
    public void rotateArm(Robot robot, double degrees){
        robot.setArmEncoderMode(STOP_AND_RESET_ENCODER);

        int targetAngle = robot.arm.getCurrentPosition() + (int)(-degrees * TICKS_PER_DEGREE_HEX);
        robot.arm.setTargetPosition(targetAngle);

        robot.setArmEncoderMode(RUN_TO_POSITION);
        robot.setArmPower(0.4);

        while(robot.arm.isBusy()) {

        }

        robot.setArmPower(0);
        robot.setArmEncoderMode(RUN_USING_ENCODER);
    }




    public int isBlockInXRegion(double x, double y){
        if(y < (3.42*x - 2500) && y > (2.09*x - 2000)){
            return 0;
        }else if(y > (3.42*x - 2628)) {
            return 1;
        }else if(y < (2.09*x - 1891)) {
            return -1;
        }
        return 8;
    }

    public int isBlockInYRegion(double y){
        if(y < BOTTOM_REGION_SIDE_COORDINATE){
            return BOTTOM_REGION_SIDE_COORDINATE - (int)y;
        } else if(y > TOP_REGION_SIDE_COORDINATE){
            return TOP_REGION_SIDE_COORDINATE - (int)y;
        } else {
            return 0;
        }
    }


}
