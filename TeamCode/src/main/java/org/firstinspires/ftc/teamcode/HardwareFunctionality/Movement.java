package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.Autonomous.*;

public class Movement {

    Robot robot = new Robot();

    
    //CONSTANTS FOR ENCODER TICK VALUES
    
    static final double TICKS_PER_MOTOR_REV = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.77953;
    static final double TICKS_PER_INCH_REV = TICKS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.141592);

    static final double TICKS_PER_MOTOR_HEX = 288.0;
    static final double TICKS_PER_DEGREE_HEX = TICKS_PER_MOTOR_HEX / 360.0;
    
    
    
    //For movement forward, backward, pivoting, and strafing. SOON YOU HAVE TO GET NEW METHODS FOR PIVOTING AND STRAFING
    
    public void linearMove(int inches, int flSign, int frSign, int blSign, int brSign){
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
}
