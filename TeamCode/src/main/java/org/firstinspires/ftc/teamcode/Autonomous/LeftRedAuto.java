package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "LeftRedAuto", group = "Red")
public class LeftRedAuto extends LinearOpMode {

    Robot robot = new Robot();
    Movement moves = new Movement();
    Webcam webcam = new Webcam();
    CSEDeterminationPipeline csePipeline = null;
    ObjectOrientationAnalysisPipeline objectPipeline = null;

    private int CSEPosition;

    @Override
    public void runOpMode() throws InterruptedException {

        //Defines motors and direction
        robot.init(hardwareMap);
        webcam.init(hardwareMap);
        objectPipeline = new ObjectOrientationAnalysisPipeline();
        csePipeline = new CSEDeterminationPipeline("red", "top");
        webcam.camera.setPipeline(csePipeline);

        boolean objectDetected = false;


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(-30.6, -65, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

        Trajectory firstMove = drive.trajectoryBuilder(startPose)
                .forward(10)
                .build();

        Trajectory toCarousel = drive.trajectoryBuilder(firstMove.end().plus(new Pose2d(0,0, Math.toRadians(90))))
                .lineTo(new Vector2d(-60.75, -60.7))
                .build();

        Trajectory toShippingHub0 = drive.trajectoryBuilder(toCarousel.end())
                .lineToSplineHeading(new Pose2d(-33.3, -27, Math.toRadians(225)))
                .build();
        Trajectory toShippingHub1 = drive.trajectoryBuilder(toCarousel.end())
                .lineToSplineHeading(new Pose2d(-32.5, -27, Math.toRadians(225)))
                .build();
        Trajectory toShippingHub2 = drive.trajectoryBuilder(toCarousel.end())
                .lineToSplineHeading(new Pose2d(-28.8, -27, Math.toRadians(180)))
                .build();

        Trajectory toScan2 = drive.trajectoryBuilder(toShippingHub2.end())
                .splineTo(new Vector2d(-57.8, -42.3), Math.toRadians(265))
                .build();
        Trajectory toScan1 = drive.trajectoryBuilder(toShippingHub1.end())
                .splineTo(new Vector2d(-57.8, -42.3), Math.toRadians(265))
                .build();
        Trajectory toScan0 = drive.trajectoryBuilder(toShippingHub0.end())
                .splineTo(new Vector2d(-57.8, -42.3), Math.toRadians(265))
                .build();



        webcam.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                webcam.camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                webcam.camera.setPipeline(csePipeline);

                webcam.camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });


        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

        sleep(3000);
        telemetry.addData("status", "Initialized");
        telemetry.update();

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
        waitForStart();


        if (isStopRequested()) return;

        webcam.camera.setPipeline(csePipeline);
        sleep(200);
        CSEPosition = csePipeline.getAnalysis();
        telemetry.addData("Position", CSEPosition);
        telemetry.update();

        robot.grabBlock();

        drive.followTrajectory(firstMove);
        drive.turn(Math.toRadians(90));
        drive.followTrajectory(toCarousel);

        double time = getRuntime();
        while(getRuntime() < time + 3.2){
            robot.carousel.setPower(0.8);
        }
        robot.carousel.setPower(0);


        if(CSEPosition == 0){

            drive.followTrajectory(toShippingHub0);
            rotateArmTime(1.50, 1);
            drive.turn(-Math.toRadians(40));
            robot.releaseBlock();
            sleep(200);
            rotateArmTime(0.05, -1);
            drive.turn(Math.toRadians(40));
            rotateArmTime(1.71, -1);
            drive.followTrajectory(toScan0);


        } else if(CSEPosition == 1){

            drive.followTrajectory(toShippingHub1);
            rotateArmTime(1.348, 1);
            drive.turn(-Math.toRadians(40));
            robot.releaseBlock();
            sleep(200);
            rotateArmTime(0.05, -1);
            drive.turn(Math.toRadians(40));
            rotateArmTime(1.548, -1);
            drive.followTrajectory(toScan1);

        } else {

            drive.followTrajectory(toShippingHub2);
            rotateArmTime(1.2, 1);
            robot.releaseBlock();
            sleep(200);
            rotateArmTime(1.4, -1);
            drive.followTrajectory(toScan2);

        }

        webcam.camera.setPipeline(objectPipeline);
        time = getRuntime();


        while(moves.isBlockInXRegion(objectPipeline.getMidpoint().x, objectPipeline.getMidpoint().y) != 0){
            moves.linearMove(drive, robot, 0.15, -1, 1, 1, -1);
            webcam.camera.setPipeline(objectPipeline);
            if(getRuntime() >= (time + 3.25)){
                objectDetected = true;
                break;
            }
        }
        robot.setAllWheelPower(0);

        time = getRuntime();


        if(!objectDetected){
            while(moves.isBlockInYRegion(objectPipeline.getMidpoint().y) != 0){
                moves.linearMove(drive, robot, 0.1, 1, 1,1, 1);
                webcam.camera.setPipeline(objectPipeline);
                if(getRuntime() >= (time + 3.5)){
                    objectDetected = true;
                    break;
                }
            }
            robot.setAllWheelPower(0);
        }

        if(!objectDetected){
            Pose2d poseEstimate = drive.getPoseEstimate();
            Trajectory toDuck = drive.trajectoryBuilder(new Pose2d(poseEstimate.getX(), poseEstimate.getY(), poseEstimate.getHeading()))
                    .forward(13.75,
                            SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
            Trajectory placeDuck = drive.trajectoryBuilder(toDuck.end())
                    .lineToLinearHeading(new Pose2d(-33, -25, Math.toRadians(180)))
                    .build();

            drive.followTrajectory(toDuck);
            robot.grabBlock();
            drive.followTrajectory(placeDuck);
            rotateArmTime(1.255, 1);
            robot.releaseBlock();
            sleep(200);
            rotateArmTime(0.5, -1);
            Trajectory park2 = drive.trajectoryBuilder(placeDuck.end())
                    .lineToLinearHeading(new Pose2d(-62.8, -37.2, Math.toRadians(180)))
                    .build();
            drive.followTrajectory(park2);

        } else {
            Pose2d timeUpPose = drive.getPoseEstimate();
            Trajectory park1 = drive.trajectoryBuilder(new Pose2d(timeUpPose.getX(), timeUpPose.getY(), timeUpPose.getHeading()))
                    .lineToLinearHeading(new Pose2d(-62.8, -37.2, Math.toRadians(180)))
                    .build();
            drive.followTrajectory(park1);
        }

    }





    public void rotateArmTime(double t, int direction){

        double time = getRuntime();
        while(getRuntime() < time + t){
            robot.arm.setPower(-0.4 * direction);
        }
        robot.arm.setPower(0);
    }
}
