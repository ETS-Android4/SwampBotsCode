package org.firstinspires.ftc.teamcode.Tests;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TestOpModeTeleop", group = "test")
public class TestOpModeTeleop extends LinearOpMode {

    Robot robot = new Robot();


    public void runOpMode(){
        robot.init(hardwareMap);

        double vertical, horizontal, pivot;
        double angle;

        waitForStart();

        while(opModeIsActive()){
            angle = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
            telemetry.addData("Angle", angle);
            telemetry.update();

            vertical = -gamepad1.left_stick_y;
            horizontal = gamepad1.left_stick_x;
            pivot = gamepad1.right_stick_x;

            robot.frontRight.setPower(-pivot + vertical - horizontal);
            robot.backRight.setPower(-pivot + vertical + horizontal);
            robot.frontLeft.setPower(pivot + vertical + horizontal);
            robot.backLeft.setPower(pivot + vertical - horizontal);
        }
    }

}
