package com.gwac.model;
// Generated 2015-10-2 9:40:37 by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * DataProcessMachine generated by hbm2java
 */
@Entity
@Table(name = "mount", schema = "public"
)
public class Mount implements java.io.Serializable {

  private int mountId;
  private String name;
  private String unitId;
  private String groupId;
  private Integer status;
  private String comment;
  private String opSn;
  private String skyName;
  private Float ra;
  private Float dec;
  private String obsType;

  public Mount() {
  }

  public Mount(int mountId) {
    this.mountId = mountId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mount_seq")
  @SequenceGenerator(name = "mount_seq", sequenceName = "mount_id_seq")
  @Column(name = "mount_id", unique = true, nullable = false)
  public int getMountId() {
    return this.mountId;
  }

  public void setMountId(int mountId) {
    this.mountId = mountId;
  }

  @Column(name = "name")
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the unitId
   */
  @Column(name = "unit_id")
  public String getUnitId() {
    return unitId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  /**
   * @return the groupId
   */
  @Column(name = "group_id")
  public String getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the status
   */
  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the comment
   */
  @Column(name = "comment")
  public String getComment() {
    return comment;
  }

  /**
   * @param comment the comment to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * @return the opSn
   */
  @Column(name = "op_sn")
  public String getOpSn() {
    return opSn;
  }

  /**
   * @param opSn the opSn to set
   */
  public void setOpSn(String opSn) {
    this.opSn = opSn;
  }

  /**
   * @return the skyName
   */
  @Column(name = "sky_name")
  public String getSkyName() {
    return skyName;
  }

  /**
   * @param skyName the skyName to set
   */
  public void setSkyName(String skyName) {
    this.skyName = skyName;
  }

  /**
   * @return the ra
   */
  @Column(name = "ra")
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
  @Column(name = "dec")
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
   * @return the obsType
   */
  @Column(name = "obs_type")
  public String getObsType() {
    return obsType;
  }

  /**
   * @param obsType the obsType to set
   */
  public void setObsType(String obsType) {
    this.obsType = obsType;
  }

}