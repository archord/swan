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
public class OTCatalog {

  private Float raD;
  private Float decD;
  private Float x;
  private Float y;
  private Float XTemp;
  private Float YTemp;
  private Date dateUt;
  private String imageName;
  private Float flux;
  private Boolean flag;
  private Float flagChb;
  private Float background;
  private Float threshold;
  private Float magAper;
  private Float magerrAper;
  private Float ellipticity;
  private Float classStar;
  private Boolean otFlag;
  private Float distance;
  private Float deltamag;
  
  public String getFileDate(){
    return imageName.substring(6, 12);
  }

  @Override
  public String toString() {
    return "raD=" + raD
            + " decD=" + decD
            + " x=" + x
            + " y=" + y
            + " XTemp=" + XTemp
            + " YTemp=" + YTemp
            + " dateUt=" + dateUt
            + " imageName=" + imageName
            + " flux=" + flux
            + " flag=" + flag
            + " flagChb=" + flagChb
            + " background=" + background
            + " threshold=" + threshold
            + " magAper=" + magAper
            + " magerrAper=" + magerrAper
            + " ellipticity=" + ellipticity
            + " classStar=" + classStar
            + " otFlag=" + otFlag
            + " distance=" + getDistance()
            + " deltamag=" + getDeltamag();
  }

  /**
   * @return the raD
   */
  public Float getRaD() {
    return raD;
  }

  /**
   * @param raD the raD to set
   */
  public void setRaD(Float raD) {
    this.raD = raD;
  }

  /**
   * @return the decD
   */
  public Float getDecD() {
    return decD;
  }

  /**
   * @param decD the decD to set
   */
  public void setDecD(Float decD) {
    this.decD = decD;
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
   * @return the XTemp
   */
  public Float getXTemp() {
    return XTemp;
  }

  /**
   * @param XTemp the XTemp to set
   */
  public void setXTemp(Float XTemp) {
    this.XTemp = XTemp;
  }

  /**
   * @return the YTemp
   */
  public Float getYTemp() {
    return YTemp;
  }

  /**
   * @param YTemp the YTemp to set
   */
  public void setYTemp(Float YTemp) {
    this.YTemp = YTemp;
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
   * @return the imageName
   */
  public String getImageName() {
    return imageName;
  }

  /**
   * @param imageName the imageName to set
   */
  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  /**
   * @return the flux
   */
  public Float getFlux() {
    return flux;
  }

  /**
   * @param flux the flux to set
   */
  public void setFlux(Float flux) {
    this.flux = flux;
  }

  /**
   * @return the flag
   */
  public Boolean getFlag() {
    return flag;
  }

  /**
   * @param flag the flag to set
   */
  public void setFlag(Boolean flag) {
    this.flag = flag;
  }

  /**
   * @return the flagChb
   */
  public Float getFlagChb() {
    return flagChb;
  }

  /**
   * @param flagChb the flagChb to set
   */
  public void setFlagChb(Float flagChb) {
    this.flagChb = flagChb;
  }

  /**
   * @return the background
   */
  public Float getBackground() {
    return background;
  }

  /**
   * @param background the background to set
   */
  public void setBackground(Float background) {
    this.background = background;
  }

  /**
   * @return the threshold
   */
  public Float getThreshold() {
    return threshold;
  }

  /**
   * @param threshold the threshold to set
   */
  public void setThreshold(Float threshold) {
    this.threshold = threshold;
  }

  /**
   * @return the magAper
   */
  public Float getMagAper() {
    return magAper;
  }

  /**
   * @param magAper the magAper to set
   */
  public void setMagAper(Float magAper) {
    this.magAper = magAper;
  }

  /**
   * @return the magerrAper
   */
  public Float getMagerrAper() {
    return magerrAper;
  }

  /**
   * @param magerrAper the magerrAper to set
   */
  public void setMagerrAper(Float magerrAper) {
    this.magerrAper = magerrAper;
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
   * @return the otFlag
   */
  public Boolean getOtFlag() {
    return otFlag;
  }

  /**
   * @param otFlag the otFlag to set
   */
  public void setOtFlag(Boolean otFlag) {
    this.otFlag = otFlag;
  }

  /**
   * @return the distance
   */
  public Float getDistance() {
    return distance;
  }

  /**
   * @param distance the distance to set
   */
  public void setDistance(Float distance) {
    this.distance = distance;
  }

  /**
   * @return the deltamag
   */
  public Float getDeltamag() {
    return deltamag;
  }

  /**
   * @param deltamag the deltamag to set
   */
  public void setDeltamag(Float deltamag) {
    this.deltamag = deltamag;
  }
}
