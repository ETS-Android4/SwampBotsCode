package org.firstinspires.ftc.teamcode.HardwareFunctionality;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FirstBasicMovement", group = "Test")
public class FirstBasicMovement extends LinearOpMode {

    /*
        If you read the online resources I gave you, you probably have an understanding
        of how Tele-op opmodes function. To be honest, we used a LinearOpMode here,
        but this is probably the most applicable place for a normal opmode.
        It doesn't matter which one you do (in my opinion I really like LinearOpModes).
        Be careful not to put too many commands and instructions in this while
        loop because if you do then it will start running slower than expected
        and things will go slow or wrong. I recommend that if you make a 'slow mode'
        or any type of 'mode' that is special to a certain situation (probably
        making drivers feel more comfortable), then you implement a switch
        statement with a button pressed as the conditional. An example of this
        "switching of modes" can be found in some of the RoadRunner tuning classes.

     */

    Robot robot = new Robot();

    @Override
    public void runOpMode() throws InterruptedException{

        //Initialize hardware
        robot.init(hardwareMap);


      //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        telemetry.addData("status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            double vertical;
            double horizontal;
            double pivot;

            double armVertical;

            //Defines direction that motors move based on their orientation to one another
            //This is super important and very standard
            vertical = -gamepad1.left_stick_y;
            horizontal = gamepad1.left_stick_x;
            pivot = gamepad1.right_stick_x;

            //Same thing here... this is standard and shouldn't be messed with much
            robot.frontRight.setPower(-pivot + vertical - horizontal);
            robot.backRight.setPower(-pivot + vertical + horizontal);
            robot.frontLeft.setPower(pivot + vertical + horizontal);
            robot.backLeft.setPower(pivot + vertical - horizontal);

            armVertical = gamepad2.left_stick_y;

            //If you need to slow down a motor, just multiply it by a factor
            robot.arm.setPower(armVertical * 0.4);

            //Throw in telemetry statements if motor powers are not what you expect
            robot.carousel.setPower(gamepad2.right_stick_y);


            if (gamepad2.right_bumper){ //closed position
                robot.leftHand.setPosition(0.85);
                robot.rightHand.setPosition(0.54);
            }

            if (gamepad2.left_bumper){ //opened position
                robot.leftHand.setPosition(0.75);
                robot.rightHand.setPosition(0.44);
            }
            
            //I don't know what this command is for...but it works
            idle();
        }
    }
}
