package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "pidControl",group = "infrastructure")
public class PIDControl extends LinearOpMode {

    DcMotorEx motor;
    double integralSum, Kp, Ki, Kd, lastError;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException{

        motor = hardwareMap.get(DcMotorEx.class, "motor1");
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        integralSum = Kp = Ki = Kd = lastError = 0;
        waitForStart();
        while(opModeIsActive()){
            double power = pid(100, motor.getCurrentPosition());
            motor.setPower(power);
        }
    }


    public double pid(double reference, double state){
        double error = reference - state;
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki);
        return output;
    }
}
