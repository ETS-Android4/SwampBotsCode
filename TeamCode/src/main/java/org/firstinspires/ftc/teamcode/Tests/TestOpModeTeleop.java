package org.firstinspires.ftc.teamcode.Tests;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Camera.CSEDeterminationPipeline;
import org.firstinspires.ftc.teamcode.Camera.Webcam;
import org.firstinspires.ftc.teamcode.HardwareFunctionality.*;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TestOpModeTeleop", group = "test")
public class TestOpModeTeleop extends LinearOpMode {
    
    //Basically blank and for whatever testing uses you want
    
    Robot robot = new Robot();
    Webcam webcam = new Webcam();
    CSEDeterminationPipeline csePipeline = null;
    int position;


    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap);
        webcam.init(hardwareMap);
        csePipeline = new CSEDeterminationPipeline("blue", "top");
        position = 0;


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

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Position", robot.arm.getCurrentPosition());
            telemetry.update();
        }
    }

}
