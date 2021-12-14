package org.firstinspires.ftc.teamcode.Camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class CSEDeterminationPipeline extends OpenCvPipeline {

    //Either red or blue, represents which side of field we start on
    String color;

    //Just for color of rectangles we draw on frame
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    //Tested points for where the regions should be on the frame. Adjust if camera is off
    static final Point REGION1_TOP_LEFT_ANCHOR_POINT = new Point(65, 380);
    static final Point REGION2_TOP_LEFT_ANCHOR_POINT = new Point(620, 410);
    static final Point REGION3_TOP_LEFT_ANCHOR_POINT = new Point(1075, 440);

    static final int REGION_WIDTH = 200;
    static final int REGION_HEIGHT = 200;
    
    
    //Create points for corners of rectangles
    Point region1_PointA = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x, REGION1_TOP_LEFT_ANCHOR_POINT.y);
    Point region1_PointB = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION1_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    Point region2_PointA = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x, REGION2_TOP_LEFT_ANCHOR_POINT.y);
    Point region2_PointB = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION2_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    Point region3_PointA = new Point(REGION3_TOP_LEFT_ANCHOR_POINT.x, REGION3_TOP_LEFT_ANCHOR_POINT.y);
    Point region3_PointB = new Point(REGION3_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION3_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);


    Mat region1_Cb, region2_Cb, region3_Cb;   //Mats for cb of each region
    Mat YCrCb = new Mat();
    Mat Cb = new Mat();
    int avg1, avg2, avg3;    //cb values

    int position;  //cse position. Either 0,1,2

    public CSEDeterminationPipeline(String c){
        color = c;
    }

    
    //Conversion of initial frame into YCrCb
    void inputToCb(Mat input){
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_BGR2YCrCb);
        Core.extractChannel(YCrCb, Cb, 2);
    }

    
    @Override
    public void init(Mat firstFrame){
        inputToCb(firstFrame);

        region1_Cb = Cb.submat(new Rect(region1_PointA, region1_PointB));
        region2_Cb = Cb.submat(new Rect(region2_PointA, region2_PointB));
        region3_Cb = Cb.submat(new Rect(region3_PointA, region3_PointB));
    }

    public Mat processFrame(Mat input){
        inputToCb(input);

        avg1 = (int) Core.mean(region1_Cb).val[0];
        avg2 = (int) Core.mean(region2_Cb).val[0];
        avg3 = (int) Core.mean(region3_Cb).val[0];

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

        int max = 0, min = 0;   //Either using a max or a min based on what color tape we're looking at

        if(color.equals("blue")){

            int maxOneTwo = Math.max(avg1, avg2);
            max = Math.max(maxOneTwo, avg3);

        } else {

            int minOneTwo = Math.min(avg1, avg2);
            min = Math.min(minOneTwo, avg3);

        }



        if(min == avg1 || max == avg1)
        {
            
            //Give a value to position and highlight the rectangle that is being selected
            position = 0;

            Imgproc.rectangle(
                    input,
                    region1_PointA,
                    region1_PointB,
                    GREEN,
                    -1);
        }
        else if(min == avg2 || max == avg2)
        {
            position = 1;

            Imgproc.rectangle(
                    input,
                    region2_PointA,
                    region2_PointB,
                    GREEN,
                    -1);
        }
        else if(min == avg3 || max == avg3)
        {
            position = 2;

            Imgproc.rectangle(
                    input,
                    region3_PointA,
                    region3_PointB,
                    GREEN,
                    -1);
        }

        return input;
    }

    //Function used to give opmode the information about position
    public int getAnalysis(){
        return position;
    }

}
