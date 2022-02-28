package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


public class Movement {


    /*
        This class was mainly for movements during autonomous before I started
        using RoadRunner. If you are using drive encoders the simple way,
        I recommend you use these classes. Also, if you are using RoadRunner
        and need to make a custom 'RUN_TO_POSITION' function because
        of the problem I had that's discussed in the guide, then you
        could put that here. Again, this is not standard, this is just
        how I organize our software system...it's your preference what
        you want to write classes for.

        In my opinion, if there are any hardware components that have to move
        to a certain position, and that accumulate error, you should probably
        make a controller for them (like a PID). I wish I coded a controller
        for the arm this season, and I should've.

     */



    //CONSTANTS FOR ENCODER TICK VALUES
    static final double TICKS_PER_MOTOR_GOBUILDA = 537.7;
    static final double WHEEL_DIAMETER_INCHES = 3.77953;
    static final double TICKS_PER_INCH_GOBUILDA = TICKS_PER_MOTOR_GOBUILDA / (WHEEL_DIAMETER_INCHES * 3.141592);

    //This was actually Gobuilda...we made a switch to one arm motor
    static final double TICKS_PER_MOTOR_HEX = 1993.6;
    static final double TICKS_PER_DEGREE_HEX = TICKS_PER_MOTOR_HEX / 360.0;


    //Constants for the custom object detection constraints...I don't know why
    //I put those methods in this class to be honest
    static final int TOP_REGION_SIDE_COORDINATE = 500;
    static final int BOTTOM_REGION_SIDE_COORDINATE = 420;
    
    
    
    //This method was for any linear movement...forward/backward, strafing, diagonals
    //If you're using RoadRunner, please don't pay attention to this
    //This method would be for people using encoders the simple way
    public void linearMoveDistance(SampleMecanumDrive drive, Robot robot, double power, double inches, double flSign, double frSign, double blSign, double brSign){
        robot.setWheelEncoderMode(STOP_AND_RESET_ENCODER);

        //How far we want to go
        int targetPosition = robot.frontLeft.getCurrentPosition() + (int)(inches * TICKS_PER_INCH_GOBUILDA);

        //the signs for each motor is just a 1 or -1 --> to represent forward or backward
        robot.frontLeft.setTargetPosition((int)(flSign * targetPosition));
        robot.frontRight.setTargetPosition((int)(frSign * targetPosition));
        robot.backLeft.setTargetPosition((int)(blSign * targetPosition));
        robot.backRight.setTargetPosition((int)(brSign * targetPosition));

        //Tell the wheels to start moving to their wanted target
        robot.setWheelEncoderMode(RUN_TO_POSITION);
        robot.setAllWheelPower(power);

        while(robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backRight.isBusy() && robot.backLeft.isBusy()) {
            //drive.update() was specifically for localization...you can leave the inside of this
            //while loop empty
            drive.update();
        }

        //Once wheels get to the specified target, stop moving
        robot.setAllWheelPower(0);
        robot.setWheelEncoderMode(RUN_USING_ENCODER);

    }

    //Same as the method above, however, it doesn't take distance as a parameter
    public void linearMove(SampleMecanumDrive drive, Robot robot, double power, int flSign, int frSign, int blSign, int brSign){
        robot.frontLeft.setPower(power * flSign);
        robot.frontRight.setPower(power * frSign);
        robot.backLeft.setPower(power * blSign);
        robot.backRight.setPower(power * brSign);

        //Again, this is for localization...you don't need this.
        drive.update();

    }


    //Method for rotation of arm with encoders. It gets fucked up by RoadRunner
    //You might want to create a controller for this (Like PID) during the season
    //so it reduces error
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



    //This has to do with my object detection. You can graph the linear equations
    //visualize what region the block needs to fit inside. If it was left of this
    //region it would return negative...to the right positive, and inside would return 0.
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

    //Same thing here except it was for functions like "y= a constant"
    //You should probably make these values constant variables instead of
    //writing the numbers...can be more easily adjustable in FTC Dashboard.
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
