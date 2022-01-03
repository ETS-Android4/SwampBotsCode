package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.Autonomous.*;
import org.opencv.core.Point;

public class Movement {

    Robot robot = new Robot();

    
    //CONSTANTS FOR ENCODER TICK VALUES
    
    static final double TICKS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.77953;
    static final double TICKS_PER_INCH_REV = TICKS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);

    static final double TICKS_PER_MOTOR_HEX = 288.0;
    static final double TICKS_PER_DEGREE_HEX = TICKS_PER_MOTOR_HEX / 360.0;

    static final int RIGHT_REGION_SIDE_COORDINATE = 480;
    static final int LEFT_REGION_SIDE_COORDINATE = 880;
    static final int TOP_REGION_SIDE_COORDINATE = 400;
    static final int BOTTOM_REGION_SIDE_COORDINATE = 200;
    
    
    
    //For movement forward, backward, pivoting, and strafing. SOON YOU HAVE TO GET NEW METHODS FOR PIVOTING AND STRAFING
    
    public void linearMoveDistance(int inches, int flSign, int frSign, int blSign, int brSign){
        robot.setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        int targetPosition = robot.frontLeft.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_REV);

        robot.frontLeft.setTargetPosition(flSign * targetPosition);
        robot.frontRight.setTargetPosition(frSign * targetPosition);
        robot.backLeft.setTargetPosition(blSign * targetPosition);
        robot.backRight.setTargetPosition(brSign * targetPosition);

        robot.setWheelEncoderMode(RUN_TO_POSITION);
        robot.setAllWheelPower(0.5);

        while(robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy() && robot.backLeft.isBusy()) {

        }

        robot.setAllWheelPower(0);
        robot.setWheelEncoderMode(RUN_USING_ENCODER);
    }

    public void linearMove(double power, int flSign, int frSign, int blSign, int brSign){
        robot.frontLeft.setPower(power * flSign);
        robot.frontRight.setPower(power * frSign);
        robot.backLeft.setPower(power * blSign);
        robot.backRight.setPower(power * brSign);
    }
    
    //Method for rotation of arm with encoders 
    
    public void rotateArm(int degrees){
        robot.setArmEncoderMode(STOP_AND_RESET_ENCODER);

        int targetAngle = robot.leftArm.getCurrentPosition() + (int)(-(degrees - 12) * TICKS_PER_DEGREE_HEX);
        robot.leftArm.setTargetPosition(targetAngle);
        robot.rightArm.setTargetPosition(targetAngle);

        robot.setArmEncoderMode(RUN_TO_POSITION);
        robot.setArmPower(0.3);

        while(robot.leftArm.isBusy() && robot.rightArm.isBusy()) {

        }

        robot.setArmPower(0);
        robot.setArmEncoderMode(RUN_USING_ENCODER);
    }



    public int isBlockInXRegion(double x){
        if(x < RIGHT_REGION_SIDE_COORDINATE){
            return RIGHT_REGION_SIDE_COORDINATE - (int)x;
        } else if(x > LEFT_REGION_SIDE_COORDINATE){
            return LEFT_REGION_SIDE_COORDINATE - (int)x;
        } else {
            return 0;
        }
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
