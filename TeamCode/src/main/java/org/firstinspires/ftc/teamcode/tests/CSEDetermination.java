package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

public class CSEDetermination extends LinearOpMode {
    OpenCvWebcam camera;
    CSEDeterminationPipeline pipeline;

    public void runOpMode(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        pipeline = new CSEDeterminationPipeline();

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                camera.setPipeline(pipeline);

                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
    }

    public static class CSEDeterminationPipeline extends OpenCvPipeline{

        public enum CSEPosition {
            LEFT,
            CENTER,
            RIGHT
        }

        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        static final Point REGION1_TOP_LEFT_ANCHOR_POINT = new Point(436, 294);
        static final Point REGION2_TOP_LEFT_ANCHOR_POINT = new Point(724, 294);
        static final Point REGION3_TOP_LEFT_ANCHOR_POINT = new Point(1012, 294);

        static final int REGION_WIDTH = 50;
        static final int REGION_HEIGHT = 50;



        Point region1_PointA = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x, REGION1_TOP_LEFT_ANCHOR_POINT.y);
        Point region1_PointB = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION1_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        Point region2_PointA = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x, REGION2_TOP_LEFT_ANCHOR_POINT.y);
        Point region2_PointB = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION2_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        Point region3_PointA = new Point(REGION3_TOP_LEFT_ANCHOR_POINT.x, REGION3_TOP_LEFT_ANCHOR_POINT.y);
        Point region3_PointB = new Point(REGION3_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION3_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);


        Mat region1_G, region2_G, region3_G;
        Mat RBG = new Mat();
        Mat G = new Mat();
        int avg1, avg2, avg3;

        private volatile CSEPosition position = CSEPosition.LEFT;

        void inputToG(Mat input){
            Core.extractChannel(input, G, 1);
        }

        @Override
        public void init(Mat firstFrame){
            inputToG(firstFrame);

            region1_G = G.submat(new Rect(region1_PointA, region1_PointB));
            region2_G = G.submat(new Rect(region2_PointA, region2_PointB));
            region3_G = G.submat(new Rect(region3_PointA, region3_PointB));


        }

        public Mat processFrame(Mat input){
            inputToG(input);

            avg1 = (int) Core.mean(region1_G).val[0];
            avg2 = (int) Core.mean(region2_G).val[0];
            avg3 = (int) Core.mean(region3_G).val[0];


            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_PointA, // First point which defines the rectangle
                    region1_PointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            Imgproc.rectangle(
                    input,
                    region2_PointA,
                    region2_PointB,
                    BLUE,
                    2);

            Imgproc.rectangle(
                    input,
                    region3_PointA,
                    region3_PointB,
                    BLUE,
                    2);

            int maxOneTwo = Math.max(avg1, avg2);
            int max = Math.max(maxOneTwo, avg3);

            if(max == avg1)
            {
                position = CSEPosition.LEFT;

                Imgproc.rectangle(
                        input,
                        region1_PointA,
                        region1_PointB,
                        GREEN,
                        -1);
            }
            else if(max == avg2)
            {
                position = CSEPosition.CENTER;

                Imgproc.rectangle(
                        input,
                        region2_PointA,
                        region2_PointB,
                        GREEN,
                        -1);
            }
            else if(max == avg3)
            {
                position = CSEPosition.RIGHT;

                Imgproc.rectangle(
                        input,
                        region3_PointA,
                        region3_PointB,
                        GREEN,
                        -1);
            }

            return input;
        }

        public CSEPosition getAnalysis(){
            return position;
        }

    }
}
