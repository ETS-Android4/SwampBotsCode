package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
@Autonomous(name = "LeftBlueAuto", group = "Blue")
public class LeftBlueAuto extends LinearOpMode {

    Robot robot = new Robot();
    Movement moves = new Movement();
    Webcam webcam = new Webcam();
    CSEDeterminationPipeline csePipeline = null;

    public static double TIMECONSTANT1 = 1.2;
    public static double TIMECONSTANT2 = 0.5;


    private int CSEPosition;
    //useless comment

    @Override
    public void runOpMode() throws InterruptedException{

        //Initialize hardware and other objects
        robot.init(hardwareMap);
        webcam.init(hardwareMap);
        csePipeline = new CSEDeterminationPipeline("blue", "bottom");
        webcam.camera.setPipeline(csePipeline);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(7.0, 58.1, Math.toRadians(270));
        drive.setPoseEstimate(startPose);

        Trajectory firstMove = drive.trajectoryBuilder(startPose)
                .forward(10)
                .build();
        Trajectory toShippingHub = drive.trajectoryBuilder(firstMove.end().plus(new Pose2d(0,0,Math.toRadians(180))))
                .splineToConstantHeading(new Vector2d(13, 20.0), Math.toRadians(0))
                .build();

        Trajectory beforeBarrier = drive.trajectoryBuilder(toShippingHub.end())
                .forward(25)
                .build();
        Trajectory park = drive.trajectoryBuilder(beforeBarrier.end().plus(new Pose2d(0,0,Math.toRadians(-90))))
                .forward(40)
                .build();




        //Event listener for CSEDetermination
        webcam.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                webcam.camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                webcam.camera.setPipeline(csePipeline);


                webcam.camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
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

        if(isStopRequested()) return;

        webcam.camera.setPipeline(csePipeline);
        sleep(200);
        CSEPosition = csePipeline.getAnalysis();
        telemetry.addData("Position", CSEPosition);
        telemetry.update();

        robot.grabBlock();

        drive.followTrajectory(firstMove);
        drive.turn(Math.toRadians(180));
        drive.followTrajectory(toShippingHub);

        if(CSEPosition == 0){

            rotateArmTime(1.476, 1);
            drive.turn(Math.toRadians(-80));
            robot.releaseBlock();
            sleep(200);
            rotateArmTime(0.05, -1);
            drive.turn(Math.toRadians(80));
            rotateArmTime(0.8, -1);

        } else if(CSEPosition == 1){


            rotateArmTime(1.326, 1);
            drive.turn(Math.toRadians(-80));
            robot.releaseBlock();
            sleep(200);
            rotateArmTime(0.05, -1);
            drive.turn(Math.toRadians(80));
            rotateArmTime(0.65, -1);

        } else {

            rotateArmTime(1.17, 1);
            drive.turn(Math.toRadians(-90));
            robot.releaseBlock();
            sleep(200);
            rotateArmTime(0.5, -1);
            drive.turn(Math.toRadians(90));

        }

        drive.followTrajectory(beforeBarrier);
        drive.turn(Math.toRadians(-90));
        drive.followTrajectory(park);
    }

    public void rotateArmTime(double t, int direction){

        double time = getRuntime();
        while(getRuntime() < time + t){
            robot.arm.setPower(-0.4 * direction);
        }
        robot.arm.setPower(0);
    }
}
