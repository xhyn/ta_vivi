/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JSlider;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 *
 * @author Yogha Pradana
 */
public class Process {
    public static final int AML=0;
    public static final int ALL=1;
    public static final double AMLMOMENTVAL=15699.75;
    public static final double ALLMOMENTVAL=24465.62;
    
    public static int svm(ArrayList<Mat> src){
        String momentValSequence=Process.findMoment(src);
        System.out.println(momentValSequence);
        String momentVal[]=momentValSequence.split("/");
            System.out.println("Detected object :"+momentVal.length);
        int val=0;
        for(int i=0;i<momentVal.length;i++){
            val+=Integer.parseInt(momentVal[i]);
        }
        //average value of all moments
        val=(int)(val/momentVal.length);
            System.out.println("val :"+val);
            //A
            int amlTest=(int)Math.abs(val-AMLMOMENTVAL);
            //B
            int allTest=(int)Math.abs(val-ALLMOMENTVAL);
            //DECISION
            if(amlTest>allTest)
                return ALL;
            else
                return AML;
    }

    public static Mat hsvThres(JLabel lLow, JLabel lHigh, JSlider sLow, JSlider sHigh, Mat img, JLabel labelToDraw,int type) {
        int sLowVal = sLow.getValue();
        int sHighVal = sHigh.getValue();
        Mat dst = new Mat();
        lLow.setText("" + sLowVal);
        lHigh.setText("" + sHighVal);

        switch (type) {
            case 0:
                Core.inRange(img, new Scalar(sLowVal,0, 0),new Scalar(sHighVal,255, 255), dst);
                break;
            case 1:
                Core.inRange(img, new Scalar(0,  sLowVal,0),new Scalar(180,  sHighVal,255), dst);
                break;
            case 2:
                Core.inRange(img, new Scalar(0, 0, sLowVal),new Scalar(180, 255, sHighVal), dst);
                break;
            default:
                break;
        }
        Draw.drawToLabelScaled(dst, labelToDraw);

        return dst;
    }

    public static String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index < 1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    public static int saveImage(ArrayList<Mat> src, String path) {
        Mat gray = new Mat();
        Mat rz = new Mat();
        Size sz = new Size(28, 28);
        for (int i = 0; i < (src.size() - 1); i++) {
            if (!Imgcodecs.imwrite(path + "crop-" + i + ".jpg", src.get(i))) {
                return 0;
            }

            Imgproc.cvtColor(src.get(i), gray, Imgproc.COLOR_BGR2GRAY);
            Imgcodecs.imwrite(path + "crop_gray-" + i + ".jpg", gray);

            Imgproc.resize(gray, rz, sz);
            Imgcodecs.imwrite(path + "crop_gray_rz-" + i + ".jpg", rz);

        }

        Imgcodecs.imwrite(path + "processed.jpg", src.get(src.size() - 1));
        return 1;
    }

    public static String findMoment(ArrayList<Mat> src) {
        Mat gray = new Mat();
        Mat rz = new Mat();
        Size sz = new Size(28, 28);
        String val = "";
        for (int i = 0; i < (src.size() - 1); i++) {
            Imgproc.cvtColor(src.get(i), gray, Imgproc.COLOR_BGR2GRAY);

            Imgproc.resize(gray, rz, sz);
            // Proses Blur
            Mat blur = new Mat();
            Imgproc.blur(rz, blur, new Size(3, 3));

            // Proses Canny
            Mat canny = new Mat();
            int tresh = 128;
            Imgproc.Canny(blur, canny, tresh, tresh * 2);

            Moments moments;
            moments = Imgproc.moments(canny);

            System.out.println(i);
            System.out.println("Moment " + i + " : = " + moments.m00);
            val = val + (int) moments.m00 + "/";
        }

        /* cek jika "val" kosong atau saat tidak ada obyek yang ditemukan
         proses pembentukan format akan dihentikan*/
        if (!val.equals("")) {
            val = val.substring(0, val.length() - 1);
        }
        return val;
    }
    
    public static ArrayList<Mat> cropROI(ArrayList<Rect> rect, Mat src){
        ArrayList<Mat> dst = new ArrayList<Mat>();
        Mat process = src.clone();
        Scalar color = new Scalar(0, 255, 0);
        for(int i=0;i<rect.size();i++){
            Imgproc.rectangle(process, rect.get(i).br(), rect.get(i).tl(), color, 2, 8, 0);
            dst.add(new Mat(src, rect.get(i)));
        }
        dst.add(process);
        return dst;
    }

    public static ArrayList<Rect> findROI(Mat mask, double limitArea, double overlap) {
        ArrayList<Rect> dst = new ArrayList<Rect>();
        Imgcodecs.imwrite("\temp\findROI-1mask.jpg", mask);
        
        List<MatOfPoint> kontur = new ArrayList<>();
        Mat hirarki = new Mat();
        Mat canny = new Mat();

        Imgproc.Laplacian(mask, canny, 8);
        Imgcodecs.imwrite("\temp\findROI-2prepos.jpg", canny);
        Imgproc.findContours(canny, kontur, hirarki, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println("kontur ditemukan :" + kontur.size());

        //sorting kontur berdasar x dan y
        Rect[] rect = new Rect[kontur.size()];
        int[] temp_data = new int[kontur.size()];
        for (int i = 0; i < kontur.size(); i++) {
            rect[i] = Imgproc.boundingRect(kontur.get(i));
            temp_data[i] = Integer.parseInt(rect[i].x + "" + rect[i].y);
        }

        //sort
        int n = kontur.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (temp_data[j] > temp_data[j + 1]) {
                    //swap data
                    int temp = temp_data[j];
                    temp_data[j] = temp_data[j + 1];
                    temp_data[j + 1] = temp;

                    //swap rect
                    Rect temp2 = rect[j];

                    rect[j] = rect[j + 1];
                    rect[j + 1] = temp2;
                }
            }
        }

        Rect prev = new Rect();

        if (rect.length > 0) {
            // kontur pertama
            if (rect[0].area() > limitArea) {
                
                System.out.println("kontur :" + 0 + " , x :" + rect[0].x + " , y :" + rect[0].y + " area :" + rect[0].area());
                
                dst.add(rect[0]);
                prev = rect[0];
            }

            //kontur kedua dst
            for (int i = 1; i < rect.length; i++) {

                double overlap_x = prev.x + (prev.width * overlap);
                double overlap_y = prev.y + (prev.height * overlap);

                if (rect[i].area() > limitArea) {
                    if (rect[i].x >= (int) overlap_x || rect[i].y >= (int) overlap_y) {

                        System.out.println("kontur :" + i + " , x :" + rect[i].x + " , y :" + rect[i].y + " area :" + rect[i].area());
                        dst.add(rect[i]);
                    }
                    prev = rect[i];
                }
            }
        }
        return dst;
    }
    
    public static Mat getMask(Mat h, Mat s, Mat v){
        Mat mask = new Mat();
        Core.bitwise_and(h, s, mask);
        Core.bitwise_and(mask, v, mask);
        return mask;
    }
}
