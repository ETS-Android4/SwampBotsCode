package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import com.qualcomm.robotcore.util.ElapsedTime;

public class TurnPIDController {

    private double targetAngle;
    private double Kp, Ki, Kd;
    private double integralSum, lastError, lastTime;

    private ElapsedTime runtime = new ElapsedTime();

    public TurnPIDController(double target, double p, double i, double d){
        targetAngle = target;
        Kp = p;
        Ki = i;
        Kd = d;
        integralSum = lastError = lastTime = 0;
    }

    public double update(double currentAngle){
        //P
        double error = targetAngle - currentAngle;
        error %= 360;
        error += 360;
        error %= 360;
        if(error > 180){
            error -= 360;
        }

        //I
        integralSum += error;
        if(Math.abs(error) < 1){
            integralSum = 0;
        }
        integralSum = Math.abs(integralSum) * Math.signum(error);

        //D
        double slope = 0;
        if(lastTime > 0){
            slope = (error - lastError) / (runtime.milliseconds() - lastTime);
        }
        lastTime = runtime.milliseconds();
        lastError = error;


        //motor power calculations
        double motorPower = 0.1 * Math.signum(error) + 0.9 * Math.tanh(Kp * error + Ki * integralSum + Kd * slope);
        return motorPower;
    }
}
