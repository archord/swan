/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

/**
 *
 * @author xy
 */
public class LineParameterConfig {
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
  /**
   * image
   */
  public static final int imgWidth = 3072;
  public static final int imgHeight = 3072;
  /**
   * hough
   */
  public static final int thetaSize = 180;
  public static final int rhoSize = 100;
  public static final int thetaRange = 36;
  public static final int rhoRange = 10;
  public static final int maxHoughFrameNunmber = 10;
  /**
   * frame
   */
  public static final int singleFrameLineMinPoint = 3;
  /**
   * obj
   */
  public static final int objInitMinPoint = 5;
  public static final int validLineMinPoint = 5;
  public static final int maxSecnod = 150;
  public static final float maxDistance = 100;
  public static final float rhoErrorTimes = (float) 0.2;
}
