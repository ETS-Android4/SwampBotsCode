package org.firstinspires.ftc.teamcode.Camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.A;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;



public class ObjectOrientationAnalysisPipeline extends OpenCvPipeline {

        //Transition mats for storing different stages in process of finding contours.
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        Mat threshold = new Mat();
        Mat morphedThreshold = new Mat();
        Mat contoursOnPlainImage = new Mat();
        ArrayList<MatOfPoint> cList = new ArrayList<MatOfPoint>();


        static final int CB_CHAN_MASK_THRESHOLD = 108;
        static final double DENSITY_UPRIGHT_THRESHOLD = 0.03;

        //Used in morphing mask
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6,6));


        //Color constants for shapes we are manually drawing on frame
        static final Scalar TEAL = new Scalar(3, 148, 252);
        static final Scalar PURPLE = new Scalar(158, 52, 235);
        static final Scalar RED = new Scalar(255, 0, 0);
        static final Scalar GREEN = new Scalar(0, 255, 0);
        static final Scalar BLUE = new Scalar(0, 0, 255);

        static final int CONTOUR_LINE_THICKNESS = 2;
        static final int CB_CHAN_IDX = 2;

        static class AnalyzedObject{
            ObjectOrientation orientation;
            double angle;
        }

        //PROBABLY NOT SAME FOR OUR OBJECTS! BLOCKS AND BALLS ARE ALWAYS UPRIGHT, research this
        enum ObjectOrientation{
            UPRIGHT,
            NOT_UPRIGHT
        }

        //List containing all of the detected objects. One copy for use on this file, the other's purpose is to be sent to the opmode
        ArrayList<AnalyzedObject> internalObjectList = new ArrayList<>();
        volatile ArrayList<AnalyzedObject> clientObjectList = new ArrayList<>();

        //Stages of detecting object
        enum Stage {
            FINAL,
            Cb,
            MASK,
            MASK_NR,
            CONTOURS
        }
        Stage[] stages = Stage.values();
        int stageNum = 0;

        public void onViewportTapped() {
            int nextStageNum = stageNum + 1;

            if(nextStageNum >= stages.length){
                nextStageNum = 0;
            }

            stageNum = nextStageNum;
        }


        public Mat processFrame(Mat input){
            internalObjectList.clear();
            int xSum = 0, ySum = 0;
            int numPoints = 0;
            cList = findContours(input);

            for(MatOfPoint contour : cList){

                //int[] tempInfo = analyzeContours(contour, input);
                Point[] points = contour.toArray();
                numPoints += points.length;

                for(Point p : points){
                    xSum += (int)p.x;
                    ySum += (int)p.y;
                }

            }

            Point midPointOfObject = new Point((int)xSum/numPoints, (int)ySum/numPoints);

            Imgproc.circle(contoursOnPlainImage, midPointOfObject, 5, BLUE, -1);
            return contoursOnPlainImage;
        }


        public ArrayList<MatOfPoint> findContours(Mat input){
            //Initialize list we will eventually return
            ArrayList<MatOfPoint> contoursList = new ArrayList<>();

            //Convert color space from default RGB to YCbCr
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, CB_CHAN_IDX);

            //Cleans out the noise in the frame for pixels with too small or too large of values.
            //However, since we are doing THRESH_BINARY_INV, all pixels values that are greater than the maxval will be set to zero.
            //And vice versa if it's under the threshold value.
            //Then erode and dilate the image so the edges are cleaner and only object stands out
            Imgproc.threshold(Cb, threshold, CB_CHAN_MASK_THRESHOLD, 255, Imgproc.THRESH_BINARY_INV);

            morphMask(threshold, morphedThreshold);

            //Algorithm to find contours for you. 3rd parameter contains information about contour hierarchy. RETR_EXTERNAL only
            //retrieves the outmost layer of contours. CHAIN_APPROX_NONE is just algorithm method.
            Imgproc.findContours(morphedThreshold, contoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

            input.copyTo(contoursOnPlainImage);
            Imgproc.drawContours(contoursOnPlainImage, contoursList, -1, BLUE, CONTOUR_LINE_THICKNESS, 8);

            return contoursList;
        }

        public void morphMask(Mat input, Mat output){

            //Eroding convolutes a kernel, which is the element size we declared earlier
            //and it will set the value of the rest of the pixels in the kernel to the minimum value found.
            Imgproc.erode(input, output, erodeElement);
            Imgproc.erode(input, output, erodeElement);

            //Dilation is identical to erosion, however, we just set the pixels values to the highest value in the kernel.
            Imgproc.dilate(output, output, dilateElement);
            Imgproc.dilate(output, output, dilateElement);
        }

        public void analyzeContours(MatOfPoint contour, Mat input){
            
        }

        public int getContoursListLength(){
            return cList.size();
        }
}




