package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous
public class Gyro extends LinearOpMode {

    /*
        To be honest, if you're using RoadRunner you don't need this class at all.
        This was going to be using my custom PID Controller before I knew
        RoadRunner was a thing. Please, use RoadRunner.
        This is just example of how to use the PID, and it wouldn't hurt you
        to understand Orientation objects and how the angles are formatted
        because I ran into bugs that wouldn't have been solved without that
        knowledge.

        I'm not going to comment this code...go through and digest it by yourself.
        All this code was from copied from a Youtube video, so you can find explanations
        there if you need. Otherwise, after a bit of thought after looking at this,
        you can probably figure out how the methods work, it's pretty simple.
     */


    Robot robot = new Robot();
    private ElapsedTime runtime = new ElapsedTime();

    private Orientation lastAngles = new Orientation();
    private double currAngle = 0.0;


    @Override
    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap);

        waitForStart();

        while(opModeIsActive()){

        }
    }



    public void resetAngle(){
        lastAngles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        currAngle = 0;
    }

    public void detectAngle(){
        telemetry.addData("Angle", robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
        telemetry.update();
    }

    public double getAngle(){
        Orientation orientation = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = orientation.thirdAngle - lastAngles.thirdAngle;

        if(deltaAngle > 180){
            deltaAngle -= 360;
        } else if(deltaAngle < -180){
            deltaAngle += 360;
        }

        currAngle += deltaAngle;
        lastAngles = orientation;
        return currAngle;
    }

    public void turn(double degrees){
        resetAngle();

        double error = degrees;

        while(opModeIsActive() && Math.abs(error) > 15){
            double motorPower = (error < 0 ? -0.3 : 0.3);
            robot.setWheelPower(-motorPower, motorPower, -motorPower, motorPower);
            error = degrees - getAngle();
        }

        robot.setAllWheelPower(0);
    }

    public void turnTo(double degrees){
        Orientation orientation = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double error = degrees - orientation.firstAngle;

        if(error > 180){
            error -= 360;
        } else if(error < 180){
            error += 360;
        }

        turn(error);
    }

    public double getAbsoluteAngle(){
        return robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }


    public void turnToPID(double targetAngle){
        TurnPIDController pid = new TurnPIDController(targetAngle, 0.01, 0, 0.003);
        while(opModeIsActive() && Math.abs(targetAngle - getAbsoluteAngle()) > 1){
            double motorPower = pid.update(getAbsoluteAngle());
            robot.setWheelPower(motorPower, -motorPower, motorPower, -motorPower);
        }
        robot.setAllWheelPower(0);
    }

    public void turnPID(double degrees){
        turnToPID(degrees + getAbsoluteAngle());
    }
}
