/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.util;

/**
 *
 * @author xy
 */
public class SearchBoxSphere {

  private double ra;
  private double dec;
  private double minRa;
  private double maxRa;
  private double minDec;
  private double maxDec;
  private double searchRadius;

  public SearchBoxSphere() {
    ra = 0;
    dec = 0;
    minRa = 0;
    maxRa = 360;
    minDec = -90;
    maxDec = 90;
    searchRadius = 0;
  }

  public String toString() {
    return "ra=" + ra + ",dec=" + dec + ",minRa=" + minRa + ",maxRa=" + maxRa + ",minDec=" + minDec + ",maxDec=" + maxDec + ",searchRadius=" + searchRadius + "\n";
  }

  public SearchBoxSphere(double ra, double dec, double searchRadius) {
    this.ra = ra;
    this.dec = dec;
    this.searchRadius = searchRadius;
  }

  public int calSearchBox() {
    return calSearchBox(ra, dec, searchRadius);
  }

  /**
   * 根据搜索半径searchRadius计算星(ra,dec)的搜索半径。
   *
   * @param ra 赤经，单位度
   * @param dec 赤纬，单位度
   * @param searchRadius 搜寻半径， 单位度
   * @return 0,1,2. 0，输入参数不合法，输入坐标ra，dec，查找半径searchRadius，超过允许范围。
   * 1，计算正确，赤经范围没有跨越0（360），搜寻半径为[minRa, maxRa]
   * 2，计算正确，赤经范围跨越0（360），搜寻半径为[minRa,360]和[0, maxRa]
   */
  public int calSearchBox(double ra, double dec, double searchRadius) {

    int flag = 0;
    if (!(ra > 360.0 || ra < 0.0 || dec > 90.0 || dec < -90.0 || searchRadius > 20.0)) {
      minDec = dec - searchRadius;
      maxDec = dec + searchRadius;
      if (getMaxDec() > 90.0) {
        maxDec = 90;
      }
      if (getMinDec() < -90.0) {
        minDec = -90;
      }

      double tDec = Math.abs(maxDec) > Math.abs(minDec) ? Math.abs(maxDec) : Math.abs(minDec);
      //π/180
      double cosd = Math.cos(tDec * 0.0174532925);
      if (cosd > searchRadius / 180.0) {
        maxRa = (ra + searchRadius / cosd + 360.0) % 360.0;
        minRa = (ra - searchRadius / cosd + 360.0) % 360.0;
      } else {
        maxRa = 360;
        minRa = 0;
      }
      if (getMinRa() > getMaxRa()) {
        /**
         * 赤经范围跨越0（360）
         */
        flag = 2;
      } else {
        flag = 1;
      }
    }
    return flag;
  }

  /**
   * @return the minRa
   */
  public double getMinRa() {
    return minRa;
  }

  /**
   * @return the maxRa
   */
  public double getMaxRa() {
    return maxRa;
  }

  /**
   * @return the minDec
   */
  public double getMinDec() {
    return minDec;
  }

  /**
   * @return the maxDec
   */
  public double getMaxDec() {
    return maxDec;
  }

}
