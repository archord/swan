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
@Table(name = "mount_monitor", schema = "public")
public class MountMonitor implements Serializable {

  private static final long serialVersionUID = 1L;  @Id
  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "mm_id_seq")})
  @GeneratedValue(generator = "generator")
  @Column(name = "mm_id", unique = true, nullable = false)
  private Long mmId;
  @Column(name = "mount_id")
  private Integer mountId;
  @Column(name = "time_utc")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timeUtc;
  @Column(name = "state")
  private Character state;
  @Column(name = "errcode")
  private Short errcode;
  @Column(name = "ra")
  private Float ra;
  @Column(name = "dec")
  private Float dec;
  @Column(name = "obj_ra")
  private Float objRa;
  @Column(name = "obj_dec")
  private Float objDec;

  public MountMonitor() {
  }

  public MountMonitor(Long mmId) {
    this.mmId = mmId;
  }

  public Long getMmId() {
    return mmId;
  }

  public void setMmId(Long mmId) {
    this.mmId = mmId;
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

  public Character getState() {
    return state;
  }

  public void setState(Character state) {
    this.state = state;
  }

  public Short getErrcode() {
    return errcode;
  }

  public void setErrcode(Short errcode) {
    this.errcode = errcode;
  }

  public Float getRa() {
    return ra;
  }

  public void setRa(Float ra) {
    this.ra = ra;
  }

  public Float getDec() {
    return dec;
  }

  public void setDec(Float dec) {
    this.dec = dec;
  }

  public Float getObjRa() {
    return objRa;
  }

  public void setObjRa(Float objRa) {
    this.objRa = objRa;
  }

  public Float getObjDec() {
    return objDec;
  }

  public void setObjDec(Float objDec) {
    this.objDec = objDec;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (mmId != null ? mmId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof MountMonitor)) {
      return false;
    }
    MountMonitor other = (MountMonitor) object;
    if ((this.mmId == null && other.mmId != null) || (this.mmId != null && !this.mmId.equals(other.mmId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gwac.model.MountMonitor[ mmId=" + mmId + " ]";
  }
  
}
