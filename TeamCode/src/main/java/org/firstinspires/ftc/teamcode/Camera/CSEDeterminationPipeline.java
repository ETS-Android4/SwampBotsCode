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

    //Either red or blue, bottom or top... please don't do halves (it was just a quick solution)
    String color;
    String half;

    //Just for color of rectangles we draw on frame
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    /*
       THRESHOLDS FOR DETECTING
       If the color values of the region are below these thresholds, I know that
       the CSE is located there. I only used one threshold for each side,
       NOT BOTH AT THE SAME TIME

       I knew the color values of the floor were about 126. However, with our egg
       sometimes the corners of the tape were showing, so I estimated the color
       value could be about 132...I made it 4 larger just to be safe. The
       color value of an unoccupied region was in the 140-150 range, so be
       smart about what the threshold is.

     */
    static final int BLUE_THRESHOLD = 135;
    static final int RED_THRESHOLD = 135;


    //Tested points for where the regions should be on the frame. Adjust if camera is off
    // (left corners of the regions). Also, height is up/down and width is left/right
    public static Point REGION1_TOP_LEFT_ANCHOR_POINT = new Point(425, 200);
    public static Point REGION2_TOP_LEFT_ANCHOR_POINT = new Point(1050, 210);

    public static int REGION_WIDTH = 200;
    public static int REGION_HEIGHT = 120;
    
    
    //Create points for corners of rectangles
    Point region1_PointA = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x, REGION1_TOP_LEFT_ANCHOR_POINT.y);
    Point region1_PointB = new Point(REGION1_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION1_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    Point region2_PointA = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x, REGION2_TOP_LEFT_ANCHOR_POINT.y);
    Point region2_PointB = new Point(REGION2_TOP_LEFT_ANCHOR_POINT.x + REGION_WIDTH, REGION2_TOP_LEFT_ANCHOR_POINT.y + REGION_HEIGHT);


    Mat region1_Cb, region2_Cb;   //Creating all the mats I'm going to use here
    Mat region1_Cr, region2_Cr;   //Mats are basically just frames (MATrix of pixels)
    Mat YCrCb = new Mat();
    Mat Cr = new Mat();
    Mat Cb = new Mat();
    int avg1B, avg2B;    //color values of the frames stored here
    int avg1R, avg2R;

    int position;  //cse position. Either 0,1,2

    public CSEDeterminationPipeline(String c, String h){
        //If I wasn't using the custom variables I made, the constructor would just be empty
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
        //Honestly, don't know the purpose of this function, but it was mandatory to have
        //I think it just obtains the submats (regions) for me
        inputToCb(firstFrame);

        region1_Cb = Cb.submat(new Rect(region1_PointA, region1_PointB));
        region2_Cb = Cb.submat(new Rect(region2_PointA, region2_PointB));

        region1_Cr = Cr.submat(new Rect(region1_PointA, region1_PointB));
        region2_Cr = Cr.submat(new Rect(region2_PointA, region2_PointB));
    }

    public Mat processFrame(Mat input){
        /*
            Don't call this method ever--as in like pipeline.processFrame()
            It is automatically ran in the command webcam.setPipeline()
            This is the meat and potatoes of the pipeline. All the drawing, logic,
            and changes in variables are done in this method. Also, be aware of
            what Mat you are returning. The parameter "input" is just the raw frame
            the camera sends over. Most of the time you'll just be editing the input
            frame and then returning it, but sometimes during testing I returned
            the other Mat variables I made just to see what is going on.
         */

        inputToCb(input);

        //Core.mean returns a scalar (a single pixel value) that represents the
        //average amount of color. .val[0] just fetches the value from the scalar
        avg1B = (int) Core.mean(region1_Cb).val[0];
        avg2B = (int) Core.mean(region2_Cb).val[0];

        avg1R = (int) Core.mean(region1_Cr).val[0];
        avg2R = (int) Core.mean(region2_Cr).val[0];

        //Draws the region rectangles we defined earlier
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

        //Variable to store which region had a "minimum value"
        int min = 0;

        if(color.equals("red")){

            if(avg1R < RED_THRESHOLD){

                //If the color value is below the threshold, assign it to the min variable
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


        //If statement is checking which region the min variable corresponds to
        if(min == avg1B || min == avg1R)
        {
            //Specific logic, bc I wasn't always looking at the same squares
            if(half.equals("bottom") && color.equals("red")){
                position = 0;
            } else {
                position = 1;
            }

            //Draw the rectangle green if CSE is there
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

            //If min wasn't changed, then the CSE is in the region we weren't looking at
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

    //These were used in testing...to return the color values
    //Important to use when testing what the threshold value should be
    public double getAnalysis1(){
        return avg1R;
    }
    public double getAnalysis2(){
        return avg2R;
    }

}
