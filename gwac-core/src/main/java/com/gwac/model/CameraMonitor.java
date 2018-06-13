/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.model;

import java.io.Serializable;
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

/**
 *
 * @author msw
 */
@Entity
@Table(name = "camera_monitor", schema = "public")
public class CameraMonitor implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "camm_id_seq")})
  @GeneratedValue(generator = "generator")
  @Column(name = "camm_id", unique = true, nullable = false)
  private Long cammId;
  @Column(name = "time_utc")
  @Temporal(TemporalType.DATE)
  private Date timeUtc;
  @Column(name = "mc_state")
  private Character mcState;
  @Column(name = "focus")
  private Integer focus;
  @Column(name = "coolget")
  private Float coolget;
  @Column(name = "filter")
  private String filter;
  @Column(name = "cam_state")
  private Character camState;
  @Column(name = "errcode")
  private Short errcode;
  @Column(name = "img_type")
  private String imgType;
  @Column(name = "obj_name")
  private String objName;
  @Column(name = "frm_no")
  private Integer frmNo;
  @Column(name = "file_name")
  private String fileName;
  @Column(name = "camera_id")
  private Integer cameraId;

  public CameraMonitor() {
  }

  public CameraMonitor(Long cammId) {
    this.cammId = cammId;
  }

  public Long getCammId() {
    return cammId;
  }

  public void setCammId(Long cammId) {
    this.cammId = cammId;
  }

  public Date getTimeUtc() {
    return timeUtc;
  }

  public void setTimeUtc(Date timeUtc) {
    this.timeUtc = timeUtc;
  }

  public Character getMcState() {
    return mcState;
  }

  public void setMcState(Character mcState) {
    this.mcState = mcState;
  }

  public Integer getFocus() {
    return focus;
  }

  public void setFocus(Integer focus) {
    this.focus = focus;
  }

  public Float getCoolget() {
    return coolget;
  }

  public void setCoolget(Float coolget) {
    this.coolget = coolget;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public Character getCamState() {
    return camState;
  }

  public void setCamState(Character camState) {
    this.camState = camState;
  }

  public Short getErrcode() {
    return errcode;
  }

  public void setErrcode(Short errcode) {
    this.errcode = errcode;
  }

  public String getImgType() {
    return imgType;
  }

  public void setImgType(String imgType) {
    this.imgType = imgType;
  }

  public String getObjName() {
    return objName;
  }

  public void setObjName(String objName) {
    this.objName = objName;
  }

  public Integer getFrmNo() {
    return frmNo;
  }

  public void setFrmNo(Integer frmNo) {
    this.frmNo = frmNo;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (cammId != null ? cammId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof CameraMonitor)) {
      return false;
    }
    CameraMonitor other = (CameraMonitor) object;
    if ((this.cammId == null && other.cammId != null) || (this.cammId != null && !this.cammId.equals(other.cammId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gwac.model.CameraMonitor[ cammId=" + cammId + " ]";
  }

  /**
   * @return the cameraId
   */
  public Integer getCameraId() {
    return cameraId;
  }

  /**
   * @param cameraId the cameraId to set
   */
  public void setCameraId(Integer cameraId) {
    this.cameraId = cameraId;
  }
  
}
