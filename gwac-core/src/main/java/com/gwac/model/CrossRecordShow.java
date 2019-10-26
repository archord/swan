package com.gwac.model;
// Generated May 5, 2014 8:56:32 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * OtObserveRecordShow generated by hbm2java
 */
@Entity
@Table(name = "cross_record", schema = "public"
)
public class CrossRecordShow implements java.io.Serializable {

  private long crId;
  private Long coId;
  private Long ctId;
  private Date dateUtc;
  private Float x;
  private Float y;
  private Float xTemp;
  private Float yTemp;
  private Float ra;
  private Float dec;
  private Float mag;
  private Float magerr;
  private Float fwhm;
  private Float ellipticity;
  private String origRecord;
  private Long cfId;
  private Integer timeSubSecond;
  private Integer ffNumber;
  private Float probability;
  private String stampName;
  private String stampPath;
  private Boolean isUpload;
  private Boolean isMatch;
  private String ffName;
  private String ffPath;
  private String ffcName;
  private String ffcPath;

  public CrossRecordShow() {
  }

  public CrossRecordShow(long crId) {
    this.crId = crId;
  }

  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49")
    ,
    @Parameter(name = "sequence", value = "cross_r_id_seq")})
  @Id
  @GeneratedValue(generator = "generator")

  @Column(name = "cr_id", unique = true, nullable = false)
  public long getCrId() {
    return this.crId;
  }

  public void setCrId(long crId) {
    this.crId = crId;
  }

  @Column(name = "co_id")
  public Long getCoId() {
    return this.coId;
  }

  public void setCoId(Long coId) {
    this.coId = coId;
  }

  @Column(name = "ct_id")
  public Long getCtId() {
    return this.ctId;
  }

  public void setCtId(Long ctId) {
    this.ctId = ctId;
  }

//  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_utc")
  public Date getDateUtc() {
    return this.dateUtc;
  }

  public void setDateUtc(Date dateUtc) {
    this.dateUtc = dateUtc;
  }

  @Column(name = "x", precision = 8, scale = 8)
  public Float getX() {
    return this.x;
  }

  public void setX(Float x) {
    this.x = x;
  }

  @Column(name = "y", precision = 8, scale = 8)
  public Float getY() {
    return this.y;
  }

  public void setY(Float y) {
    this.y = y;
  }

  @Column(name = "x_temp", precision = 8, scale = 8)
  public Float getXTemp() {
    return this.xTemp;
  }

  public void setXTemp(Float xTemp) {
    this.xTemp = xTemp;
  }

  @Column(name = "y_temp", precision = 8, scale = 8)
  public Float getYTemp() {
    return this.yTemp;
  }

  public void setYTemp(Float yTemp) {
    this.yTemp = yTemp;
  }

  @Column(name = "ra", precision = 8, scale = 8)
  public Float getRa() {
    return this.ra;
  }

  public void setRa(Float ra) {
    this.ra = ra;
  }

  @Column(name = "dec", precision = 8, scale = 8)
  public Float getDec() {
    return this.dec;
  }

  public void setDec(Float dec) {
    this.dec = dec;
  }

  @Column(name = "mag", precision = 8, scale = 8)
  public Float getMag() {
    return this.mag;
  }

  public void setMag(Float mag) {
    this.mag = mag;
  }

  @Column(name = "magerr", precision = 8, scale = 8)
  public Float getMagerr() {
    return this.magerr;
  }

  public void setMagerr(Float magerr) {
    this.magerr = magerr;
  }

  @Column(name = "fwhm", precision = 8, scale = 8)
  public Float getFwhm() {
    return this.fwhm;
  }

  public void setFwhm(Float fwhm) {
    this.fwhm = fwhm;
  }

  @Column(name = "ellipticity", precision = 8, scale = 8)
  public Float getEllipticity() {
    return this.ellipticity;
  }

  public void setEllipticity(Float ellipticity) {
    this.ellipticity = ellipticity;
  }

  @Column(name = "orig_record")
  public String getOrigRecord() {
    return this.origRecord;
  }

  public void setOrigRecord(String origRecord) {
    this.origRecord = origRecord;
  }

  @Column(name = "cf_id")
  public Long getCfId() {
    return this.cfId;
  }

  public void setCfId(Long cfId) {
    this.cfId = cfId;
  }

  /**
   * @return the timeSubSecond
   */
  @Column(name = "time_sub_second")
  public Integer getTimeSubSecond() {
    return timeSubSecond;
  }

  /**
   * @param timeSubSecond the timeSubSecond to set
   */
  public void setTimeSubSecond(Integer timeSubSecond) {
    this.timeSubSecond = timeSubSecond;
  }

  @Column(name = "ff_number")
  public Integer getFfNumber() {
    return this.ffNumber;
  }

  public void setFfNumber(Integer ffNumber) {
    this.ffNumber = ffNumber;
  }

  /**
   * @return the probability
   */
  @Column(name = "probability")
  public Float getProbability() {
    return probability;
  }

  /**
   * @param probability the probability to set
   */
  public void setProbability(Float probability) {
    this.probability = probability;
  }

  /**
   * @return the stampName
   */
  @Column(name = "stamp_name")
  public String getStampName() {
    return stampName;
  }

  /**
   * @param stampName the stampName to set
   */
  public void setStampName(String stampName) {
    this.stampName = stampName;
  }

  /**
   * @return the stampPath
   */
  @Column(name = "stamp_path")
  public String getStampPath() {
    return stampPath;
  }

  /**
   * @param stampPath the stampPath to set
   */
  public void setStampPath(String stampPath) {
    this.stampPath = stampPath;
  }

  /**
   * @return the isUpload
   */
  @Column(name = "is_upload")
  public Boolean getIsUpload() {
    return isUpload;
  }

  /**
   * @param isUpload the isUpload to set
   */
  public void setIsUpload(Boolean isUpload) {
    this.isUpload = isUpload;
  }

  @Column(name = "is_match")
  public Boolean getIsMatch() {
    return this.isMatch;
  }

  public void setIsMatch(Boolean isMatch) {
    this.isMatch = isMatch;
  }

  /**
   * @return the ffName
   */
  @Column(name = "ff_name")
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
   * @return the ffPath
   */
  @Column(name = "ff_path")
  public String getFfPath() {
    return ffPath;
  }

  /**
   * @param ffPath the ffPath to set
   */
  public void setFfPath(String ffPath) {
    this.ffPath = ffPath;
  }

  /**
   * @return the ffcName
   */
  @Column(name = "ffc_name")
  public String getFfcName() {
    return ffcName;
  }

  /**
   * @param ffcName the ffcName to set
   */
  public void setFfcName(String ffcName) {
    this.ffcName = ffcName;
  }

  /**
   * @return the ffcPath
   */
  @Column(name = "ffc_path")
  public String getFfcPath() {
    return ffcPath;
  }

  /**
   * @param ffcPath the ffcPath to set
   */
  public void setFfcPath(String ffcPath) {
    this.ffcPath = ffcPath;
  }

}