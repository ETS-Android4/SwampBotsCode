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
@Autonomous(name = "RightBlueAuto", group = "Blue")
public class RightBlueAuto extends LinearOpMode {

    Robot robot = new Robot();
    Movement moves = new Movement();
    Webcam webcam = new Webcam();
    CSEDeterminationPipeline csePipeline = null;

    public static int XDISTANCE = 30;
    public static int YDISTANCE = 30;
    public static int HEADING = 0;

    private int CSEPosition;

    @Override
    public void runOpMode() throws InterruptedException{



        //Defines motors and direction
        robot.init(hardwareMap);
        webcam.init(hardwareMap);
        csePipeline = new CSEDeterminationPipeline("blue");

        webcam.camera.setPipeline(csePipeline);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d();
        drive.setPoseEstimate(startPose);

        Trajectory test = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(XDISTANCE, YDISTANCE), HEADING)
                .build();



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

        telemetry.addData("status", "Initialized");
        telemetry.update();

        //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectory(test);

    }
}
