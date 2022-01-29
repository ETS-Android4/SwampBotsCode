package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp(name = "FirstBasicMovement", group = "Test")
public class FirstBasicMovement extends LinearOpMode {

    Robot robot = new Robot();
    boolean trigger_pressed;

    @Override
    public void runOpMode() throws InterruptedException{

        //Set up motors and initialization
        robot.init(hardwareMap);

        //Setting Initial Servo Positions
        robot.leftHand.setPosition(0.75);
        robot.rightHand.setPosition(0.44);

        trigger_pressed = false;

      //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            double vertical;
            double horizontal;
            double pivot;

            double armVertical;
            
            double carousselMotorPowerF = gamepad1.right_trigger;
            double carousselMotorPowerR = gamepad1.left_trigger;

            double leftHandCurrentPos;
            double rightHandCurrentPos;


            //Defines direction that motors move based on their orientation to one another
            vertical = -gamepad1.left_stick_y;
            horizontal = gamepad1.left_stick_x;
            pivot = gamepad1.right_stick_x;

            robot.frontRight.setPower(-pivot + vertical - horizontal);
            robot.backRight.setPower(-pivot + vertical + horizontal);
            robot.frontLeft.setPower(pivot + vertical + horizontal);
            robot.backLeft.setPower(pivot + vertical - horizontal);

            armVertical = gamepad2.left_stick_y;

            robot.arm.setPower(armVertical * 0.15);

            telemetry.addData("LeftFront", robot.frontLeft.getPower());
            telemetry.addData("RightFront", robot.frontRight.getPower());
            telemetry.addData("LeftBack", robot.backLeft.getPower());
            telemetry.addData("RightBack", robot.backRight.getPower());



            robot.carousel.setPower(gamepad2.right_stick_y);

            telemetry.addData("Left Trigger", gamepad1.left_trigger);
            telemetry.addData("Right Trigger", gamepad1.right_trigger);

            if (gamepad2.right_bumper){ //closed position
                robot.leftHand.setPosition(0.85);
                robot.rightHand.setPosition(0.54);
            }

            if (gamepad2.left_bumper){ //opened position
                robot.leftHand.setPosition(0.75);
                robot.rightHand.setPosition(0.44);
            }




            telemetry.update();
            idle();
        }
    }


}
