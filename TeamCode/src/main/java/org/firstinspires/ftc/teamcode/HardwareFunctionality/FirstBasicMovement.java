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
        robot.init(hardwareMap, "teleop");

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
            horizontal = -gamepad1.left_stick_x;
            pivot = gamepad1.right_stick_x;

            robot.frontRight.setPower(-pivot + vertical - horizontal);
            robot.backRight.setPower(-pivot + vertical + horizontal);
            robot.frontLeft.setPower(pivot + vertical + horizontal);
            robot.backLeft.setPower(pivot + vertical - horizontal);

            armVertical = gamepad2.left_stick_y;
            robot.leftArm.setPower(armVertical * 0.3);
            robot.rightArm.setPower(armVertical * 0.3);


            //YOU NEED TO CHECK IF OUR MOTOR CAN BE A DCMOTOREX OBJECT. USE THE instanceOf TO CHECK
            if(gamepad1.right_trigger > 0){
                if(!trigger_pressed){
                    robot.carousel.setVelocity(120, AngleUnit.DEGREES);
                    time = 0;
                    trigger_pressed = true;
                } else {
                    robot.carousel.setVelocity(getAngularVelocity(time), AngleUnit.DEGREES);
                }

            } else if(gamepad1.left_trigger > 0){
                if(!trigger_pressed){
                    robot.carousel.setVelocity(-120, AngleUnit.DEGREES);
                    time = 0;
                    trigger_pressed = true;
                } else {
                    robot.carousel.setVelocity(-getAngularVelocity(time), AngleUnit.DEGREES);
                }


            } else {
                robot.carousel.setPower(0);
                trigger_pressed = false;
            }

            //Servo Controls
            telemetry.addData("Right Hand Position: ", robot.rightHand.getPosition());
            telemetry.addData("Left Hand Position: ", robot.leftHand.getPosition());

            if (gamepad2.right_bumper){ //closed position
                robot.leftHand.setPosition(0.85);
                robot.rightHand.setPosition(0.54);
            }

            if (gamepad2.left_bumper){ //opened position
                robot.leftHand.setPosition(0.75);
                robot.rightHand.setPosition(0.44);
            }


            //Outputs how much power each motor is experiencing to the driver hub
            telemetry.addData("frontRight Power: ", robot.frontRight.getPower());
            telemetry.addData("backRight Power: ", robot.backRight.getPower());
            telemetry.addData("frontLeft Power: ", robot.frontLeft.getPower());
            telemetry.addData("backLeft Power: " , robot.backLeft.getPower());
            telemetry.addData("left Arm Motor Power: ", robot.leftArm.getPower());
            telemetry.addData("Right Arm Motor Power: ", robot.rightArm.getPower());
            telemetry.addData("left Hand Servo Position: ", robot.leftHand.getPosition());
            telemetry.addData("Right Hand Servo Position", robot.rightHand.getPosition());

            telemetry.update();
            idle();
        }
    }

    public double getAngularVelocity(double seconds){
        //Linear equation that will return the wanted angular velocity in degrees/second.
        return 20*seconds + 120;
    }
}
