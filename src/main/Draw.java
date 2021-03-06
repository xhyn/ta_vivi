/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Yogha Pradana
 */
public class Draw {
    public static void drawToLabelScaled(Mat image, JLabel label) {
        Mat imshow = image.clone();
        double h_scale,w_scale,scale;
        h_scale = (label.getHeight()) / (image.size().height);
            w_scale = (label.getWidth()) / (image.size().width);

            if (h_scale <= w_scale) {
                scale = h_scale;
            } else {
                scale = w_scale;
            }
        Imgproc.resize(imshow, imshow, new Size(0, 0), scale, scale, Imgproc.INTER_LINEAR);
        drawToLabel(imshow,label);
        
    }
    
    public static void drawToLabel(Mat image, JLabel label) {
        BufferedImage buff = toBufferedImage(image);
        //ImageIcon icon = new ImageIcon();

        label.setIcon(new ImageIcon(buff));

    }
    
    public static BufferedImage toBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
    
    public static void drawToPanel(Mat image, javax.swing.JPanel panel) {
        BufferedImage buff = toBufferedImage(image);

        Graphics g1 = panel.getGraphics();

        if (g1.drawImage(buff, 0, 0, buff.getWidth(), buff.getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null));
    }
    
    private void drawToPanelScaled(Mat image, javax.swing.JPanel panel, double h_scale) {
        Mat imshow = image.clone();
        Imgproc.resize(imshow, imshow, new Size(0, 0), h_scale, h_scale, Imgproc.INTER_LINEAR);
        drawToPanel(imshow, panel);
    }
}
