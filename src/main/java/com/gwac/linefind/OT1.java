/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2318.9	381.724	170.284	4.01298	2015/12/18 18:25:28	10.7607	1777	34
 *
 * @author xy
 */
public class OT1 implements Comparable, Cloneable {

  private int frameNumber;
  private float x;
  private float y;
  private float xTemp;
  private float yTemp;
  private float ra;
  private float dec;
  private float mag;
  private Date date;
  private String dateStr;

  public OT1(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public OT1(int frameNumber, float x, float y, float xTemp, float yTemp, float ra, float dec, float mag, Date date, String dateStr) {
    this.frameNumber = frameNumber;
    this.x = x;
    this.y = y;
    this.xTemp = xTemp;
    this.yTemp = yTemp;
    this.ra = ra;
    this.dec = dec;
    this.mag = mag;
    this.date = date;
    this.dateStr = dateStr;
  }

  public double distance(OT1 p) {
    return this.distance(p.getX(), p.getY());
  }

  public double distance(float x0, float y0) {
    return Math.sqrt(Math.pow(x0 - getX(), 2) + Math.pow(y0 - getY(), 2));
  }

  @Override
  public int compareTo(Object o) {
    OT1 tpoint = (OT1) o;
    return this.getFrameNumber() - tpoint.getFrameNumber();
  }

  public int compareTo1(Object o) {
    OT1 tpoint = (OT1) o;
    int result = 1;
    if (this.getX() < tpoint.getX()) {
      result = -1;
    } else if (Math.abs(this.getX() - tpoint.getX()) == 0) {
      if (this.getY() < tpoint.getY()) {
        result = -1;
      }
    }
    return result;
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

  /**
   * @return the ra
   */
  public float getRa() {
    return ra;
  }

  /**
   * @param ra the ra to set
   */
  public void setRa(float ra) {
    this.ra = ra;
  }

  /**
   * @return the dec
   */
  public float getDec() {
    return dec;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(float dec) {
    this.dec = dec;
  }

  /**
   * @return the mag
   */
  public float getMag() {
    return mag;
  }

  /**
   * @param mag the mag to set
   */
  public void setMag(float mag) {
    this.mag = mag;
  }

  /**
   * @return the date
   */
  public Date getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * @return the dateStr
   */
  public String getDateStr() {
    return dateStr;
  }

  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

  /**
   * @return the xTemp
   */
  public float getxTemp() {
    return xTemp;
  }

  /**
   * @param xTemp the xTemp to set
   */
  public void setxTemp(float xTemp) {
    this.xTemp = xTemp;
  }

  /**
   * @return the yTemp
   */
  public float getyTemp() {
    return yTemp;
  }

  /**
   * @param yTemp the yTemp to set
   */
  public void setyTemp(float yTemp) {
    this.yTemp = yTemp;
  }

  //2318.9	381.724	170.284	4.01298	2015/12/18 18:25:28	10.7607	1777	34	1626.55	2273.18
  public void printInfo() {
    System.out.println(String.format("%f\t%f\t%f\t%f\t%s\t%f\t%d\t0\t%f\t%f",
            this.x, this.y, this.ra, this.dec, this.dateStr, this.mag, this.frameNumber, this.xTemp, this.yTemp));
  }

  @Override
  public String toString() {
    return String.format("%f\t%f\t%f\t%f\t%s\t%f\t%d\t0\t%f\t%f",
            this.x, this.y, this.ra, this.dec, this.dateStr, this.mag, this.frameNumber, this.xTemp, this.yTemp);
  }

  public String getStr(int idx, SimpleDateFormat sdf) {
    return String.format("%4d %4d 0 %s %7.2f %7.2f %8.5f %9.5f",
            idx, this.frameNumber, sdf.format(date), this.x, this.y, this.ra, this.dec);
  }
}
