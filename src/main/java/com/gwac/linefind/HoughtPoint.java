/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import java.util.Date;

/**
 *
 * @author xy
 */
public class HoughtPoint implements Comparable, Cloneable {

  private int pIdx;
  private int frameNumber;
  private float theta;
  private float rho;
  private float ktheta;
  private float theta2;
  private float rho2;
  private float x;
  private float y;
  private Date dateUtc;
  private float xDelta;
  private float yDelta;
  private int fnDelta;
  private float timeDelta;
  private float xSpeedfn;
  private float ySpeedfn;
  private float xSpeedt;
  private float ySpeedt;
  private float preX;
  private float preY;
  private float preDeltaX;
  private float preDeltaY;
  private long oorId;

  public void calSpeed(HoughtPoint tPoint) {
    this.xDelta = this.getX() - tPoint.getX();
    this.yDelta = this.getY() - tPoint.getY();
    this.fnDelta = this.getFrameNumber() - tPoint.getFrameNumber();
    this.timeDelta = (float) ((this.getDateUtc().getTime() - tPoint.getDateUtc().getTime()) * 1.0 / 1000);

    this.xSpeedfn = xDelta / fnDelta;
    this.ySpeedfn = yDelta / fnDelta;
    this.xSpeedt = xDelta / timeDelta;
    this.ySpeedt = yDelta / timeDelta;

    this.preX = tPoint.x + tPoint.xSpeedt * timeDelta;
    this.preY = tPoint.y + tPoint.ySpeedt * timeDelta;
    this.preDeltaX = this.preX - this.x;
    this.preDeltaY = this.preY - this.y;
  }

  public void prePos(HoughtPoint tPoint) {
  }

  public void printInfo() {
    System.out.println(String.format("%4d %5d %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f",
            this.pIdx, this.frameNumber, this.x, this.y, this.rho, this.getRho2(),
            this.theta * 180 / Math.PI, this.ktheta * 180 / Math.PI, this.getTheta2() * 180 / Math.PI,
            this.preX, this.preY, this.preDeltaX, this.preDeltaY));
  }

  public String getAllInfo() {
    String tstr = String.format("%4d\t%5d\t%8.2f\t%8.2f\t%8.2f\t%8.2f\t%5d\t%8.2f\t%8.4f\t%8.4f\t%8.2f\t%8.2f\t%8.2f\t%8.2f",
            this.pIdx, this.frameNumber, this.x, this.y, this.xDelta, this.yDelta,
            this.fnDelta, this.timeDelta, this.xSpeedt, this.ySpeedt,
            this.preX, this.preY, this.preDeltaX, this.preDeltaY);
    return tstr;
  }

  public HoughtPoint(int pIdx, int frameNumber, float theta, float rho, float x, float y, Date dateUtc, long oorId) {
    this.pIdx = pIdx;
    this.frameNumber = frameNumber;
    this.theta = theta;
    this.rho = rho;
    this.x = x;
    this.y = y;
    this.dateUtc = dateUtc;
    this.oorId = oorId;
  }

  public void calKtheta(HoughtPoint hp, float imgXCenter, float imgYCenter, float halfRho) {

    double xDelta = hp.x - x;
    double yDelta = hp.y - y;
    ktheta = (float) (Math.atan2(yDelta, xDelta));

    if (ktheta < 0) {
      ktheta += Math.PI;
    }

    if (ktheta < Math.PI / 2) {
      theta2 = (float) (ktheta + Math.PI / 2);
    } else {
      theta2 = (float) (ktheta - Math.PI / 2);
    }

    rho2 = (float) ((x - imgXCenter) * Math.cos(theta2) + (y - imgYCenter) * Math.sin(theta2)) + halfRho;
  }

  /**
   * @return the pIdx
   */
  public int getpIdx() {
    return pIdx;
  }

  /**
   * @param pIdx the pIdx to set
   */
  public void setpIdx(int pIdx) {
    this.pIdx = pIdx;
  }

  /**
   * @return the frameNumber
   */
  public int getFrameNumber() {
    return frameNumber;
  }

  /**
   * @param frameNumber the frameNumber to set
   */
  public void setFrameNumber(int frameNumber) {
    this.frameNumber = frameNumber;
  }

  /**
   * @return the theta
   */
  public float getTheta() {
    return theta;
  }

  /**
   * @param theta the theta to set
   */
  public void setTheta(float theta) {
    this.theta = theta;
  }

  /**
   * @return the rho
   */
  public float getRho() {
    return rho;
  }

  /**
   * @param rho the rho to set
   */
  public void setRho(float rho) {
    this.rho = rho;
  }

  @Override
  public int compareTo(Object o) {
    HoughtPoint tpoint = (HoughtPoint) o;
    int result = 1;
    if (this.frameNumber < tpoint.frameNumber) {
      result = -1;
    } else if (Math.abs(this.frameNumber - tpoint.frameNumber) == 0) {
      if (this.x < tpoint.x) {
        result = -1;
      }
    }
    return result;
  }

  /**
   * @return the x
   */
  public float getX() {
    return x;
  }

  /**
   * @param x the x to set
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * @return the y
   */
  public float getY() {
    return y;
  }

  /**
   * @param y the y to set
   */
  public void setY(float y) {
    this.y = y;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  /**
   * @return the ktheta
   */
  public float getKtheta() {
    return ktheta;
  }

  /**
   * @param ktheta the ktheta to set
   */
  public void setKtheta(float ktheta) {
    this.ktheta = ktheta;
  }

  /**
   * @return the theta2
   */
  public float getTheta2() {
    return theta2;
  }

  /**
   * @param theta2 the theta2 to set
   */
  public void setTheta2(float theta2) {
    this.theta2 = theta2;
  }

  /**
   * @return the rho2
   */
  public float getRho2() {
    return rho2;
  }

  /**
   * @param rho2 the rho2 to set
   */
  public void setRho2(float rho2) {
    this.rho2 = rho2;
  }

  /**
   * @return the dateUtc
   */
  public Date getDateUtc() {
    return dateUtc;
  }

  /**
   * @param dateUtc the dateUtc to set
   */
  public void setDateUtc(Date dateUtc) {
    this.dateUtc = dateUtc;
  }

  /**
   * @return the xDelta
   */
  public float getxDelta() {
    return xDelta;
  }

  /**
   * @param xDelta the xDelta to set
   */
  public void setxDelta(float xDelta) {
    this.xDelta = xDelta;
  }

  /**
   * @return the yDelta
   */
  public float getyDelta() {
    return yDelta;
  }

  /**
   * @param yDelta the yDelta to set
   */
  public void setyDelta(float yDelta) {
    this.yDelta = yDelta;
  }

  /**
   * @return the fnDelta
   */
  public int getFnDelta() {
    return fnDelta;
  }

  /**
   * @param fnDelta the fnDelta to set
   */
  public void setFnDelta(int fnDelta) {
    this.fnDelta = fnDelta;
  }

  /**
   * @return the timeDelta
   */
  public float getTimeDelta() {
    return timeDelta;
  }

  /**
   * @param timeDelta the timeDelta to set
   */
  public void setTimeDelta(long timeDelta) {
    this.timeDelta = timeDelta;
  }

  /**
   * @return the xSpeedfn
   */
  public float getxSpeedfn() {
    return xSpeedfn;
  }

  /**
   * @param xSpeedfn the xSpeedfn to set
   */
  public void setxSpeedfn(float xSpeedfn) {
    this.xSpeedfn = xSpeedfn;
  }

  /**
   * @return the ySpeedfn
   */
  public float getySpeedfn() {
    return ySpeedfn;
  }

  /**
   * @param ySpeedfn the ySpeedfn to set
   */
  public void setySpeedfn(float ySpeedfn) {
    this.ySpeedfn = ySpeedfn;
  }

  /**
   * @return the xSpeedt
   */
  public float getxSpeedt() {
    return xSpeedt;
  }

  /**
   * @param xSpeedt the xSpeedt to set
   */
  public void setxSpeedt(float xSpeedt) {
    this.xSpeedt = xSpeedt;
  }

  /**
   * @return the ySpeedt
   */
  public float getySpeedt() {
    return ySpeedt;
  }

  /**
   * @param ySpeedt the ySpeedt to set
   */
  public void setySpeedt(float ySpeedt) {
    this.ySpeedt = ySpeedt;
  }

  /**
   * @return the preX
   */
  public float getPreX() {
    return preX;
  }

  /**
   * @param preX the preX to set
   */
  public void setPreX(float preX) {
    this.preX = preX;
  }

  /**
   * @return the preY
   */
  public float getPreY() {
    return preY;
  }

  /**
   * @param preY the preY to set
   */
  public void setPreY(float preY) {
    this.preY = preY;
  }

  /**
   * @return the preDeltaX
   */
  public float getPreDeltaX() {
    return preDeltaX;
  }

  /**
   * @param preDeltaX the preDeltaX to set
   */
  public void setPreDeltaX(float preDeltaX) {
    this.preDeltaX = preDeltaX;
  }

  /**
   * @return the preDeltaY
   */
  public float getPreDeltaY() {
    return preDeltaY;
  }

  /**
   * @param preDeltaY the preDeltaY to set
   */
  public void setPreDeltaY(float preDeltaY) {
    this.preDeltaY = preDeltaY;
  }

  /**
   * @return the oorId
   */
  public long getOorId() {
    return oorId;
  }

  /**
   * @param oorId the oorId to set
   */
  public void setOorId(long oorId) {
    this.oorId = oorId;
  }
}
