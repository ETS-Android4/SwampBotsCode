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

    static final Point TOP_LEFT = new Point(540, 260);
    static final int REGION_LENGTH = 200;

    Point pointA = new Point(TOP_LEFT.x, TOP_LEFT.y);
    Point pointB = new Point(TOP_LEFT.x + REGION_LENGTH, TOP_LEFT.y + REGION_LENGTH);

    Mat regionB, regionR;
    Mat YCrCb = new Mat();
    Mat Cb = new Mat();
    Mat Cr = new Mat();
    int avgB, avgR;


    public void inputConversions(Mat input){
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);

        Core.extractChannel(YCrCb, Cb, 2);
        Core.extractChannel(YCrCb, Cr, 1);

    }

    public void init(Mat firstFrame){
        inputConversions(firstFrame);

        regionB = Cb.submat(new Rect(pointA, pointB));
        regionR = Cr.submat(new Rect(pointA, pointB));
    }

    public Mat processFrame(Mat input){
        inputConversions(input);

        avgB = (int) Core.mean(regionB).val[0];
        avgR = (int) Core.mean(regionR).val[0];

        Imgproc.rectangle(input, pointA, pointB, new Scalar(0, 0, 255), 2);

        return input;
    }

    public int getCbValue(){ return avgB; }
    public int getCrValue(){ return avgR; }
}
