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
@Table(name = "obs_ctl_sys_monitor", schema = "public")
public class ObsCtlSysMonitor implements Serializable {

  private static final long serialVersionUID = 1L;  @Id
  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "ocsm_id_seq")})
  @GeneratedValue(generator = "generator")
  @Column(name = "ocsm_id", unique = true, nullable = false)
  private Long ocsmId;
  @Column(name = "mount_id")
  private Integer mountId;
  @Column(name = "time_utc")
  @Temporal(TemporalType.DATE)
  private Date timeUtc;
  @Column(name = "state")
  private String state;
  @Column(name = "op_sn")
  private Long opSn;
  @Column(name = "op_real_start_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date opRealStartTime;
  @Column(name = "mount_status")
  private Integer mountStatus;
  @Column(name = "camera_status")
  private String cameraStatus;

  public ObsCtlSysMonitor() {
  }

  public ObsCtlSysMonitor(Long ocsmId) {
    this.ocsmId = ocsmId;
  }

  public Long getOcsmId() {
    return ocsmId;
  }

  public void setOcsmId(Long ocsmId) {
    this.ocsmId = ocsmId;
  }

  public Integer getMountId() {
    return mountId;
  }

  public void setMountId(Integer mountId) {
    this.mountId = mountId;
  }

  public Date getTimeUtc() {
    return timeUtc;
  }

  public void setTimeUtc(Date timeUtc) {
    this.timeUtc = timeUtc;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Long getOpSn() {
    return opSn;
  }

  public void setOpSn(Long opSn) {
    this.opSn = opSn;
  }

  public Date getOpRealStartTime() {
    return opRealStartTime;
  }

  public void setOpRealStartTime(Date opRealStartTime) {
    this.opRealStartTime = opRealStartTime;
  }

  public Integer getMountStatus() {
    return mountStatus;
  }

  public void setMountStatus(Integer mountStatus) {
    this.mountStatus = mountStatus;
  }

  public String getCameraStatus() {
    return cameraStatus;
  }

  public void setCameraStatus(String cameraStatus) {
    this.cameraStatus = cameraStatus;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (ocsmId != null ? ocsmId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ObsCtlSysMonitor)) {
      return false;
    }
    ObsCtlSysMonitor other = (ObsCtlSysMonitor) object;
    if ((this.ocsmId == null && other.ocsmId != null) || (this.ocsmId != null && !this.ocsmId.equals(other.ocsmId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gwac.model.ObsCtlSysMonitor[ ocsmId=" + ocsmId + " ]";
  }
  
}
