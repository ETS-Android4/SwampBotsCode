package org.firstinspires.ftc.teamcode.Tests;

import org.firstinspires.ftc.teamcode.Camera.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


public class ColorValueTest extends OpenCvPipeline {

    static final Point TOP_LEFT = new Point(620,300);
    static final int LENGTH = 200;

    Point pointA = new Point(TOP_LEFT.x, TOP_LEFT.y);
    Point pointB = new Point(TOP_LEFT.x + LENGTH, TOP_LEFT.y + LENGTH);

    Mat YCrCb = new Mat();
    Mat region;
    Mat Y = new Mat();
    Mat Cr = new Mat();
    Mat Cb = new Mat();

    int avg;

    public void inputConversion(Mat input){
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);

        Core.extractChannel(YCrCb, Y, 0);
        Core.extractChannel(YCrCb, Cr, 1);
        Core.extractChannel(YCrCb, Cb, 2);
    }

    public void init(Mat firstFrame){
        inputConversion(firstFrame);

        region = Cb.submat(new Rect(pointA, pointB));
    }

    public Mat processFrame(Mat input){
        inputConversion(input);

        Imgproc.rectangle(Cb, pointA, pointB, new Scalar(0,0,0), 2);

        avg = (int) Core.mean(region).val[0];
        return Cb;
    }

    public int getValue(){ return avg; }
}
