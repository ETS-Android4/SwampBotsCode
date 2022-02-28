package org.firstinspires.ftc.teamcode.Camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;



public class ObjectOrientationAnalysisPipeline extends OpenCvPipeline {

        //Transition mats for storing different stages in process of finding contours.
        //Please know what a Mat is before reading this program lol
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        Mat threshold = new Mat();
        Mat morphedThreshold = new Mat();
        Mat contoursOnPlainImage = new Mat();
        ArrayList<Point> midpoints = new ArrayList<>();


        //The threshold value for detecting ducks (doesn't apply to blocks)
        static final int CB_CHAN_MASK_THRESHOLD = 85;

        //Size of the kernels in the morph masks functions
        //Look up a video to understand what dilating and eroding are
        //Idk what the ideal kernel size is...this was just already on the program
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6,6));


        //Color constants for shapes we are manually drawing on frame
        static final Scalar RED = new Scalar(255, 0, 0);
        static final Scalar BLUE = new Scalar(0, 0, 255);

        //In Y-Cr-Cb, Cb is 3rd in order, so technically the 2nd index
        static final int CONTOUR_LINE_THICKNESS = 2;
        static final int CB_CHAN_IDX = 2;



        public Mat processFrame(Mat input){
            midpoints.clear();

            ArrayList<MatOfPoint> contourList = findContours(input);

            if(contourList.size() != 0){
                for(MatOfPoint contour : contourList){
                    //The special function that does everything
                    analyzeContour(contour, input);
                }
            }

            return contoursOnPlainImage;
        }

        //Will find all the contours (hypothetically one per object)
        //Will return an array of contours(represented as an array of points)
        public ArrayList<MatOfPoint> findContours(Mat input){

            //Initialize list we will eventually return
            ArrayList<MatOfPoint> contoursList = new ArrayList<>();

            //Convert color space from default RGB to YCbCr
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, CB_CHAN_IDX);

            //Cleans out the noise in the frame for pixels with too small or too large of values.
            //However, since we are doing THRESH_BINARY_INV, all pixels values that are greater than the maxval will be set to zero.
            //And vice versa if it's under the threshold value.
            Imgproc.threshold(Cb, threshold, CB_CHAN_MASK_THRESHOLD, 255, Imgproc.THRESH_BINARY_INV);

            //Then erode and dilate the image so the edges are cleaner and only object stands out
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

        public void analyzeContour(MatOfPoint contour, Mat input){
            //this entire function will find the midpoints of a contour, add the midpoint
            //to our array, and then draw the midpoint on the screen

            //This function may be way more complex if you're analyzing other factors

            Point[] points = contour.toArray();

            Point mid = findMidpoint(points);
            midpoints.add(mid);
            Imgproc.circle(contoursOnPlainImage, mid, 5, BLUE, -1);
        }

        public Point findMidpoint(Point[] p){

            //Takes all the x-coords, sums them up, and divides by the number of points
            //Same thing for the y-coords
            double xSum = 0, ySum = 0;
            int numPoints = 0;

            numPoints += p.length;

            for(Point point : p) {
                xSum += point.x;
                ySum += point.y;
            }

            //Coordinates can't be decimal, so we have to round
            return new Point(Math.round(xSum/numPoints), Math.round(ySum/numPoints));
        }


        //The getter method that returns the midpoint back to the opmode
        //We don't want to return a null list, so just return a blank point if the array is empty
        public Point getMidpoint(){
            if(midpoints.size() > 0){
                return midpoints.get(0);
            }
            return new Point(0,0);
        }
}
