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
  private float x;
  private float y;
  private Date dateUtc;
  private String ffName;
  private double ra;
  private double dec;

  public void prePos(HoughtPoint tPoint) {
  }

  public String getAllInfo() {
    String tstr = String.format("%4d\t%5d\t%8.2f\t%8.2f\t%s",
            this.pIdx, this.frameNumber, this.x, this.y, this.dateUtc);
    return tstr;
  }

  public HoughtPoint(int pIdx, int frameNumber, float x, float y, Date dateUtc, String ffName, double ra, double dec) {
    this.pIdx = pIdx;
    this.frameNumber = frameNumber;
    this.x = x;
    this.y = y;
    this.dateUtc = dateUtc;
    this.ffName = ffName;
    this.ra = ra;
    this.dec = dec;
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
   * @return the ra
   */
  public double getRa() {
    return ra;
  }

  /**
   * @param ra the ra to set
   */
  public void setRa(double ra) {
    this.ra = ra;
  }

  /**
   * @return the dec
   */
  public double getDec() {
    return dec;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(double dec) {
    this.dec = dec;
  }

  /**
   * @return the ffName
   */
  public String getFfName() {
    return ffName;
  }

  /**
   * @param ffName the ffName to set
   */
  public void setFfName(String ffName) {
    this.ffName = ffName;
  }

}
