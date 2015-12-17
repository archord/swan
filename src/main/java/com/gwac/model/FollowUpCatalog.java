/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.model;

import java.util.Date;

/**
 *
 * @author xy
 */
public class FollowUpCatalog {

  private Float jd;
  private Date dateUt;
  private String filter;
  private Float ra;
  private Float dec;
  private Float x;
  private Float y;
  private Float magClbtUsno;
  private Float magErr;
  private Float ellipticity;
  private Float classStar;
  private Float fwhm;
  private Short flag;
  private Float b2;
  private Float r2;
  private Float i;
  private String otType;
  private String ffName;
  private Short objLabel;

  //OT第N次的后随编号
  public int getFuSerialNumber() {
    return Integer.parseInt(ffName.substring(26,31));
  }

  @Override
  public String toString() {
    return " jd=" + getJd()
            + " dateUt=" + getDateUt()
            + " filter=" + getFilter()
            + " ra=" + getRa()
            + " dec=" + getDec()
            + " x=" + getX()
            + " y=" + getY()
            + " magClbtUsno=" + getMagClbtUsno()
            + " magErr=" + getMagErr()
            + " ellipticity=" + getEllipticity()
            + " classStar=" + getClassStar()
            + " fwhm=" + getFwhm()
            + " flag=" + getFlag()
            + " b2=" + getB2()
            + " r2=" + getR2()
            + " i=" + getI()
            + " classStar=" + getClassStar()
            + " otType=" + getOtType()
            + " ffName=" + getFfName()
            + " objLabel=" + getObjLabel();
  }

  /**
   * @return the jd
   */
  public Float getJd() {
    return jd;
  }

  /**
   * @param jd the jd to set
   */
  public void setJd(Float jd) {
    this.jd = jd;
  }

  /**
   * @return the dateUt
   */
  public Date getDateUt() {
    return dateUt;
  }

  /**
   * @param dateUt the dateUt to set
   */
  public void setDateUt(Date dateUt) {
    this.dateUt = dateUt;
  }

  /**
   * @return the filter
   */
  public String getFilter() {
    return filter;
  }

  /**
   * @param filter the filter to set
   */
  public void setFilter(String filter) {
    this.filter = filter;
  }

  /**
   * @return the ra
   */
  public Float getRa() {
    return ra;
  }

  /**
   * @param ra the ra to set
   */
  public void setRa(Float ra) {
    this.ra = ra;
  }

  /**
   * @return the dec
   */
  public Float getDec() {
    return dec;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(Float dec) {
    this.dec = dec;
  }

  /**
   * @return the x
   */
  public Float getX() {
    return x;
  }

  /**
   * @param x the x to set
   */
  public void setX(Float x) {
    this.x = x;
  }

  /**
   * @return the y
   */
  public Float getY() {
    return y;
  }

  /**
   * @param y the y to set
   */
  public void setY(Float y) {
    this.y = y;
  }

  /**
   * @return the magClbtUsno
   */
  public Float getMagClbtUsno() {
    return magClbtUsno;
  }

  /**
   * @param magClbtUsno the magClbtUsno to set
   */
  public void setMagClbtUsno(Float magClbtUsno) {
    this.magClbtUsno = magClbtUsno;
  }

  /**
   * @return the magErr
   */
  public Float getMagErr() {
    return magErr;
  }

  /**
   * @param magErr the magErr to set
   */
  public void setMagErr(Float magErr) {
    this.magErr = magErr;
  }

  /**
   * @return the ellipticity
   */
  public Float getEllipticity() {
    return ellipticity;
  }

  /**
   * @param ellipticity the ellipticity to set
   */
  public void setEllipticity(Float ellipticity) {
    this.ellipticity = ellipticity;
  }

  /**
   * @return the classStar
   */
  public Float getClassStar() {
    return classStar;
  }

  /**
   * @param classStar the classStar to set
   */
  public void setClassStar(Float classStar) {
    this.classStar = classStar;
  }

  /**
   * @return the fwhm
   */
  public Float getFwhm() {
    return fwhm;
  }

  /**
   * @param fwhm the fwhm to set
   */
  public void setFwhm(Float fwhm) {
    this.fwhm = fwhm;
  }

  /**
   * @return the flag
   */
  public Short getFlag() {
    return flag;
  }

  /**
   * @param flag the flag to set
   */
  public void setFlag(Short flag) {
    this.flag = flag;
  }

  /**
   * @return the b2
   */
  public Float getB2() {
    return b2;
  }

  /**
   * @param b2 the b2 to set
   */
  public void setB2(Float b2) {
    this.b2 = b2;
  }

  /**
   * @return the r2
   */
  public Float getR2() {
    return r2;
  }

  /**
   * @param r2 the r2 to set
   */
  public void setR2(Float r2) {
    this.r2 = r2;
  }

  /**
   * @return the i
   */
  public Float getI() {
    return i;
  }

  /**
   * @param i the i to set
   */
  public void setI(Float i) {
    this.i = i;
  }

  /**
   * @return the otType
   */
  public String getOtType() {
    return otType;
  }

  /**
   * @param otType the otType to set
   */
  public void setOtType(String otType) {
    this.otType = otType;
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

  /**
   * @return the objLabel
   */
  public Short getObjLabel() {
    return objLabel;
  }

  /**
   * @param objLabel the objLabel to set
   */
  public void setObjLabel(Short objLabel) {
    this.objLabel = objLabel;
  }

}
