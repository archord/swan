/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xy
 */
public class HoughTransformOrig {

  // cache of values of sin and cos for different theta values. Has a significant performance improvement. 
  private double[] sinCache;
  private double[] cosCache;

  // the number of points that have been added 
  protected int numOT1s;
  int[][] houghArray;

  int maxHoughFrameNunmber;
  int minValidPoint;
  int minValidFrame = 2; //完成第minValidFrame帧的添加之后，在添加下一帧的第一个点之前，计算所有直线的速度

  int imgWidth;
  int imgHeight;
  float imgXCenter;
  float imgYCenter;

  double maxTheta;
  double maxRho;
  double halfRho;
  int thetaSize;
  int rhoSize;
  int thetaRange;
  int rhoRange;
  double thetaStep;
  double rhoStep;

  float maxDistance;
  float rhoErrorTimes;
  int validLineMinPoint;

  /**
   *
   * @param imgWidth 图像宽
   * @param imgHeight 图像高
   * @param thetaSize hough变换中，theta取值个数
   * @param rhoSize hough变换中，rho取值个数
   * @param thetaRange 在hough变化矩阵中，搜寻极大值时，theta方向的搜寻半径
   * @param rhoRange 在hough变化矩阵中，搜寻极大值时，rho方向的搜寻半径
   * @param maxHoughFrameNunmber
   * hough变换中，一条hough直线的中间能够缺失的最大帧数量；如果超过这个数量，则后续的点被处理为一条新的直线。
   * @param minValidPoint hough变换中，有效hough直线中最小有效点的个数
   * @param maxDistance
   * hough变换中，一条hough直线的中间能够缺失的最大直线距离；如果超过这个数量，则后续的点被处理为一条新的直线。
   * @param rhoErrorTimes
   * 取值最好不要大于1，一个新的点P，使用一条直线L（theta0,rho0）的theta0计算过P点的直线rho，如果abs(rho0-rho)小于rhoErrorTimes*rhoStep，则认为这个点P是直线L上的点。
   */
  public HoughTransformOrig(int imgWidth, int imgHeight, int thetaSize, int rhoSize, int thetaRange, int rhoRange,
          int maxHoughFrameNunmber, int minValidPoint, float maxDistance, float rhoErrorTimes, int validLineMinPoint) {
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.maxTheta = Math.PI;
    this.thetaSize = thetaSize;
    this.rhoSize = rhoSize;
    this.thetaRange = thetaRange;
    this.rhoRange = rhoRange;
    this.maxHoughFrameNunmber = maxHoughFrameNunmber;
    this.minValidPoint = minValidPoint;
    this.maxDistance = maxDistance;
    this.rhoErrorTimes = rhoErrorTimes;
    this.validLineMinPoint = validLineMinPoint;

    this.imgXCenter = this.imgWidth / 2;
    this.imgYCenter = this.imgHeight / 2;
    this.maxRho = Math.sqrt(Math.pow(imgWidth, 2) + Math.pow(imgHeight, 2));
    this.halfRho = this.maxRho / 2;
    this.thetaStep = this.maxTheta / this.thetaSize;
    this.rhoStep = this.maxRho / this.rhoSize;

    initialise();
    this.rhoErrorTimes = rhoErrorTimes;
    this.validLineMinPoint = validLineMinPoint;
  }

  private void initialise() {
    houghArray = new int[thetaSize][rhoSize];
    // Count how many points there are 
    numOT1s = 0;

    // cache the values of sin and cos for faster processing 
    sinCache = new double[thetaSize];
    cosCache = sinCache.clone();
    for (int t = 0; t < thetaSize; t++) {
      double realTheta = t * thetaStep;
      sinCache[t] = Math.sin(realTheta);
      cosCache[t] = Math.cos(realTheta);
    }

    for (int t = 0; t < thetaSize; t++) {
      for (int r = 0; r < rhoSize; r++) {
        houghArray[t][r] = 0;
      }
    }

  }

  /**
   * Adds a single point to the hough transform. You can use this method
   * directly if your data isn't represented as a buffered image.
   *
   * @param ot1
   */
  public void houghAddPoint(OT1 ot1) {

    // Go through each value of theta 
    for (int t = 0; t < thetaSize; t++) {

      //Work out the r values for each theta step 
      float fr = (float) ((((ot1.getX() - imgXCenter) * cosCache[t]) + ((ot1.getY() - imgYCenter) * sinCache[t]) + halfRho) / rhoStep);
      int r = (int) fr;
//      int r = Math.round(fr);

      if (r < 0 || r >= this.rhoSize) {
        System.out.println("x=" + ot1.getX() + ",y=" + ot1.getY() + ",theta=" + t + ",rho=" + r);
        continue;
      }

      // Increment the hough array 
      houghArray[t][r]++;
    }
  }

  /**
   * Gets the highest value in the hough array
   */
  public int getHighestValue() {
    int max = 0;
    for (int t = 0; t < thetaSize; t++) {
      for (int r = 0; r < rhoSize; r++) {
        if (houghArray[t][r] > max) {
          max = houghArray[t][r];
        }
      }
    }
    return max;
  }

  /**
   * Gets the hough array as an image, in case you want to have a look at it.
   */
  public void drawHoughImage(String fName) {
    int max = getHighestValue();
    BufferedImage image = new BufferedImage(thetaSize, rhoSize, BufferedImage.TYPE_INT_ARGB);
    for (int t = 0; t < thetaSize; t++) {
      for (int r = 0; r < rhoSize; r++) {
        double value = 255 * ((double) houghArray[t][r]) / max;
        int v = 255 - (int) value;
        int c = new Color(v, v, v).getRGB();
        image.setRGB(t, r, c);
      }
    }

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(HoughTransform.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
