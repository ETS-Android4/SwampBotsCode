package org.firstinspires.ftc.teamcode.Camera;

import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Config
public class CSEDeterminationPipeline extends OpenCvPipeline {

    //Either red or blue, represents which side of field we start on
    String color;
    String half;

    //Just for color of rectangles we draw on frame
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    static final int BLUE_THRESHOLD = 135;
    static final int RED_THRESHOLD = 135;

    //Tested points for where the regions should be on the frame. Adjust if camera is off
    public static Point REGION1_TOP_LEFT_ANCHOR_POINT = new Point(425, 200);
    public static Point REGION2_TOP_LEFT_ANCHOR_POINT = new Point(1050, 210);


    public static int REGION_WIDTH = 200;
    public static int REGION_HEIGHT = 120;
    
    
    //Create points for corners of rectangles
    Point region1_PointA = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x, REGION1_TOP_LEFT_ANCHOR_POINT.y);
    Point region1_PointB = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION1_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    Point region2_PointA = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x, REGION2_TOP_LEFT_ANCHOR_POINT.y);
    Point region2_PointB = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION2_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);


    Mat region1_Cb, region2_Cb;   //Mats for cb of each region
    Mat region1_Cr, region2_Cr;
    Mat YCrCb = new Mat();
    Mat Cr = new Mat();
    Mat Cb = new Mat();
    int avg1B, avg2B;    //cb values
    int avg1R, avg2R;

    int position;  //cse position. Either 0,1,2

    public CSEDeterminationPipeline(String c, String h){
        color = c;
        half = h;
    }

    
    //Conversion of initial frame into YCrCb
    void inputToCb(Mat input){
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cb, 2);
        Core.extractChannel(YCrCb, Cr, 1);
    }

    
    @Override
    public void init(Mat firstFrame){
        inputToCb(firstFrame);

        region1_Cb = Cb.submat(new Rect(region1_PointA, region1_PointB));
        region2_Cb = Cb.submat(new Rect(region2_PointA, region2_PointB));

        region1_Cr = Cr.submat(new Rect(region1_PointA, region1_PointB));
        region2_Cr = Cr.submat(new Rect(region2_PointA, region2_PointB));
    }

    public Mat processFrame(Mat input){
        inputToCb(input);

        avg1B = (int) Core.mean(region1_Cb).val[0];
        avg2B = (int) Core.mean(region2_Cb).val[0];

        avg1R = (int) Core.mean(region1_Cr).val[0];
        avg2R = (int) Core.mean(region2_Cr).val[0];

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


        int min = 0;  //Either using a max or a min based on what color tape we're looking at

        if(color.equals("red")){

            if(avg1R < RED_THRESHOLD){
                min = avg1R;
            } else if(avg2R < RED_THRESHOLD){
                min = avg2R;
            }

        } else {

            if(avg1B < BLUE_THRESHOLD){
                min = avg1B;
            } else if(avg2B < BLUE_THRESHOLD){
                min = avg2B;
            }

        }



        if(min == avg1B || min == avg1R)
        {
            if(half.equals("bottom") && color.equals("red")){
                position = 0;
            } else {
                position = 1;
            }



            Imgproc.rectangle(input, region1_PointA, region1_PointB, GREEN, -1);
        }
        else if(min == avg2B || min == avg2R)
        {
            if(half.equals("bottom") && color.equals("red")){
                position = 1;
            } else {
                position = 2;
            }


            Imgproc.rectangle(input, region2_PointA, region2_PointB, GREEN, -1);
        }
        else if(min == 0){
            if(half.equals("bottom") && color.equals("red")){
                position = 2;
            } else {
                position = 0;
            }
        }


        return input;
    }

    //Function used to give opmode the information about position
    public int getAnalysis(){ return position; }
    public double getAnalysis1(){
        return avg1R;
    }
    public double getAnalysis2(){
        return avg2R;
    }

}
