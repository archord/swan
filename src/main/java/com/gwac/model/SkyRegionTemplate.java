package com.gwac.model;
// Generated 2017-1-10 20:46:20 by Hibernate Tools 3.6.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "sky_region_template", schema = "public"
)
public class SkyRegionTemplate implements java.io.Serializable {

  private long tmptId;
  private String tmptName;
  private Integer groupId;
  private Integer unitId;
  private Integer camId;
  private Integer gridId;
  private Integer fieldId;
  private Float centerRa;
  private Float centerDec;
  private Float topLeftRa;
  private Float topLeftDec;
  private Float topRightRa;
  private Float topRightDec;
  private Float bottomRightRa;
  private Float bottomRightDec;
  private Float bottomLeftRa;
  private Float bottomLeftDec;
  private Date genTime;
  private String storePath;
  private Integer starNum;
  private Float fwhm;

  @Override
  public String toString() {
    return "tmptName=" + tmptName + ", groupId=" + groupId + ", unitId=" + unitId + ", camId=" + camId + ", gridId=" + gridId
            + ", fieldId=" + fieldId + ", centerRa=" + centerRa + ", centerDec=" + centerDec + ", topLeftRa=" + topLeftRa + ", topLeftDec=" + topLeftDec
            + ", topRightRa=" + topRightRa + ", topRightDec=" + topRightDec + ", bottomRightRa=" + bottomRightRa + ", bottomRightDec=" + bottomRightDec + ", bottomLeftRa=" + bottomLeftRa
            + ", bottomLeftDec=" + bottomLeftDec + ", genTime=" + genTime + ", storePath=" + storePath + ", starNum=" + starNum + ", fwhm=" + fwhm;
  }

  public SkyRegionTemplate() {
  }

  public SkyRegionTemplate(long tmptId) {
    this.tmptId = tmptId;
  }

  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49")
    ,
    @Parameter(name = "sequence", value = "tmpt_id_seq")})
  @Id
  @GeneratedValue(generator = "generator")
  @Column(name = "tmpt_id", unique = true, nullable = false)
  public long getTmptId() {
    return tmptId;
  }

  /**
   * @param tmptId the tmptId to set
   */
  public void setTmptId(long tmptId) {
    this.tmptId = tmptId;
  }

  /**
   * @return the tmptName
   */
  @Column(name = "tmpt_name")
  public String getTmptName() {
    return tmptName;
  }

  /**
   * @param tmptName the tmptName to set
   */
  public void setTmptName(String tmptName) {
    this.tmptName = tmptName;
  }

  /**
   * @return the groupId
   */
  @Column(name = "group_id")
  public Integer getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the unitId
   */
  @Column(name = "unit_id")
  public Integer getUnitId() {
    return unitId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  /**
   * @return the camId
   */
  @Column(name = "cam_id")
  public Integer getCamId() {
    return camId;
  }

  /**
   * @param camId the camId to set
   */
  public void setCamId(Integer camId) {
    this.camId = camId;
  }

  /**
   * @return the gridId
   */
  @Column(name = "grid_id")
  public Integer getGridId() {
    return gridId;
  }

  /**
   * @param gridId the gridId to set
   */
  public void setGridId(Integer gridId) {
    this.gridId = gridId;
  }

  /**
   * @return the fieldId
   */
  @Column(name = "field_id")
  public Integer getFieldId() {
    return fieldId;
  }

  /**
   * @param fieldId the fieldId to set
   */
  public void setFieldId(Integer fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * @return the centerRa
   */
  @Column(name = "center_ra")
  public Float getCenterRa() {
    return centerRa;
  }

  /**
   * @param centerRa the centerRa to set
   */
  public void setCenterRa(Float centerRa) {
    this.centerRa = centerRa;
  }

  /**
   * @return the centerDec
   */
  @Column(name = "center_dec")
  public Float getCenterDec() {
    return centerDec;
  }

  /**
   * @param centerDec the centerDec to set
   */
  public void setCenterDec(Float centerDec) {
    this.centerDec = centerDec;
  }

  /**
   * @return the topLeftRa
   */
  @Column(name = "top_left_ra")
  public Float getTopLeftRa() {
    return topLeftRa;
  }

  /**
   * @param topLeftRa the topLeftRa to set
   */
  public void setTopLeftRa(Float topLeftRa) {
    this.topLeftRa = topLeftRa;
  }

  /**
   * @return the topLeftDec
   */
  @Column(name = "top_left_dec")
  public Float getTopLeftDec() {
    return topLeftDec;
  }

  /**
   * @param topLeftDec the topLeftDec to set
   */
  public void setTopLeftDec(Float topLeftDec) {
    this.topLeftDec = topLeftDec;
  }

  /**
   * @return the topRightRa
   */
  @Column(name = "top_right_ra")
  public Float getTopRightRa() {
    return topRightRa;
  }

  /**
   * @param topRightRa the topRightRa to set
   */
  public void setTopRightRa(Float topRightRa) {
    this.topRightRa = topRightRa;
  }

  /**
   * @return the topRightDec
   */
  @Column(name = "top_right_dec")
  public Float getTopRightDec() {
    return topRightDec;
  }

  /**
   * @param topRightDec the topRightDec to set
   */
  public void setTopRightDec(Float topRightDec) {
    this.topRightDec = topRightDec;
  }

  /**
   * @return the bottomRightRa
   */
  @Column(name = "bottom_right_ra")
  public Float getBottomRightRa() {
    return bottomRightRa;
  }

  /**
   * @param bottomRightRa the bottomRightRa to set
   */
  public void setBottomRightRa(Float bottomRightRa) {
    this.bottomRightRa = bottomRightRa;
  }

  /**
   * @return the bottomRightDec
   */
  @Column(name = "bottom_right_dec")
  public Float getBottomRightDec() {
    return bottomRightDec;
  }

  /**
   * @param bottomRightDec the bottomRightDec to set
   */
  public void setBottomRightDec(Float bottomRightDec) {
    this.bottomRightDec = bottomRightDec;
  }

  /**
   * @return the bottomLeftRa
   */
  @Column(name = "bottom_left_ra")
  public Float getBottomLeftRa() {
    return bottomLeftRa;
  }

  /**
   * @param bottomLeftRa the bottomLeftRa to set
   */
  public void setBottomLeftRa(Float bottomLeftRa) {
    this.bottomLeftRa = bottomLeftRa;
  }

  /**
   * @return the bottomLeftDec
   */
  @Column(name = "bottom_left_dec")
  public Float getBottomLeftDec() {
    return bottomLeftDec;
  }

  /**
   * @param bottomLeftDec the bottomLeftDec to set
   */
  public void setBottomLeftDec(Float bottomLeftDec) {
    this.bottomLeftDec = bottomLeftDec;
  }

  /**
   * @return the genTime
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "gen_time", length = 29)
  public Date getGenTime() {
    return genTime;
  }

  /**
   * @param genTime the genTime to set
   */
  public void setGenTime(Date genTime) {
    this.genTime = genTime;
  }

  /**
   * @return the storePath
   */
  @Column(name = "store_path")
  public String getStorePath() {
    return storePath;
  }

  /**
   * @param storePath the storePath to set
   */
  public void setStorePath(String storePath) {
    this.storePath = storePath;
  }

  /**
   * @return the starNum
   */
  @Column(name = "star_num")
  public Integer getStarNum() {
    return starNum;
  }

  /**
   * @param starNum the starNum to set
   */
  public void setStarNum(Integer starNum) {
    this.starNum = starNum;
  }

  /**
   * @return the fwhm
   */
  @Column(name = "fwhm")
  public Float getFwhm() {
    return fwhm;
  }

  /**
   * @param fwhm the fwhm to set
   */
  public void setFwhm(Float fwhm) {
    this.fwhm = fwhm;
  }

}
