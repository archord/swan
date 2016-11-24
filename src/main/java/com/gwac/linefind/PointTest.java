/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

/**
 *
 * @author xy
 */
public class PointTest {

  public double x;
  public double y;
  public double theta;
  public double rho;
  public double kTheta;
  
  public PointTest(double x, double y){
    this.x = x;
    this.y = y;
  }

  public void printInfo() {
    System.out.println(String.format("%7.2f,%7.2f,%6.2f,%6.2f,%6.2f", x, y, theta, rho, kTheta));
  }

  public void getKTheta(PointTest tpoint) {

    double xDelta = tpoint.x - x;
    double yDelta = tpoint.y - y;
    kTheta = Math.atan2(yDelta, xDelta) * 180 / Math.PI;

    if (kTheta < 90) {
      theta = kTheta + 90;
    } else {
      theta = kTheta - 90;
    }

    rho = x * Math.cos(theta*Math.PI/180) + y * Math.sin(theta*Math.PI/180);
  }

}
