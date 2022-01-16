package org.firstinspires.ftc.teamcode.Tests;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestOpModeTeleop", group = "test")
public class TestOpModeTeleop extends LinearOpMode {

    Robot robot = new Robot();

    public void runOpMode(){
        robot.init(hardwareMap);

        waitForStart();
        while(opModeIsActive()){

        }
    }

}
