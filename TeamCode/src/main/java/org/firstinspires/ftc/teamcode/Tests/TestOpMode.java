package org.firstinspires.ftc.teamcode.Tests;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.Camera.ObjectOrientationAnalysisPipeline;
import org.firstinspires.ftc.teamcode.Camera.Webcam;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "TestOpMode", group = "tests")
public class TestOpMode extends LinearOpMode {

    public void runOpMode(){

        waitForStart();

        //Need to test to see how 'time' works for the carousel acceleration. Does setting time = 0 reset the time?
    }



}
