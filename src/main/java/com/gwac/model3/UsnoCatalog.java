/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.model3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author xy
 */
@Entity
@Table(name = "1799_nomad", catalog = "catalogue"
)
public class UsnoCatalog implements java.io.Serializable{
  
  private Integer rcdid;
  private String flag;
  private Float rAdeg;
  private Float dEdeg;
  private String source;
  private Float pmRA;
  private Float pmDEC;
  private Float bmag;
  private String bsource;
  private Float vmag;
  private String vsource;
  private Float rmag;
  private String rsource;
  private Float jmag;
  private Float hmag;
  private Float kmag;

  public UsnoCatalog() {
  }

  /**
   * @return the rcdid
   */
  @Id
  @Column(name = "RCDID", unique = true, nullable = false)
  public Integer getRcdid() {
    return rcdid;
  }

  /**
   * @param rcdid the rcdid to set
   */
  public void setRcdid(Integer rcdid) {
    this.rcdid = rcdid;
  }

  /**
   * @return the flag
   */
  @Column(name = "Flag")
  public String getFlag() {
    return flag;
  }

  /**
   * @param flag the flag to set
   */
  public void setFlag(String flag) {
    this.flag = flag;
  }

  /**
   * @return the rAdeg
   */
  @Column(name = "RAdeg")
  public Float getrAdeg() {
    return rAdeg;
  }

  /**
   * @param rAdeg the rAdeg to set
   */
  public void setrAdeg(Float rAdeg) {
    this.rAdeg = rAdeg;
  }

  /**
   * @return the dEdeg
   */
  @Column(name = "DEdeg")
  public Float getdEdeg() {
    return dEdeg;
  }

  /**
   * @param dEdeg the dEdeg to set
   */
  public void setdEdeg(Float dEdeg) {
    this.dEdeg = dEdeg;
  }

  /**
   * @return the source
   */
  @Column(name = "source")
  public String getSource() {
    return source;
  }

  /**
   * @param source the source to set
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * @return the pmRA
   */
  @Column(name = "pmRA")
  public Float getPmRA() {
    return pmRA;
  }

  /**
   * @param pmRA the pmRA to set
   */
  public void setPmRA(Float pmRA) {
    this.pmRA = pmRA;
  }

  /**
   * @return the pmDEC
   */
  @Column(name = "pmDEC")
  public Float getPmDEC() {
    return pmDEC;
  }

  /**
   * @param pmDEC the pmDEC to set
   */
  public void setPmDEC(Float pmDEC) {
    this.pmDEC = pmDEC;
  }

  /**
   * @return the bmag
   */
  @Column(name = "Bmag")
  public Float getBmag() {
    return bmag;
  }

  /**
   * @param bmag the bmag to set
   */
  public void setBmag(Float bmag) {
    this.bmag = bmag;
  }

  /**
   * @return the bsource
   */
  @Column(name = "Bsource")
  public String getBsource() {
    return bsource;
  }

  /**
   * @param bsource the bsource to set
   */
  public void setBsource(String bsource) {
    this.bsource = bsource;
  }

  /**
   * @return the vmag
   */
  @Column(name = "Vmag")
  public Float getVmag() {
    return vmag;
  }

  /**
   * @param vmag the vmag to set
   */
  public void setVmag(Float vmag) {
    this.vmag = vmag;
  }

  /**
   * @return the vsource
   */
  @Column(name = "Vsource")
  public String getVsource() {
    return vsource;
  }

  /**
   * @param vsource the vsource to set
   */
  public void setVsource(String vsource) {
    this.vsource = vsource;
  }

  /**
   * @return the rmag
   */
  @Column(name = "Rmag")
  public Float getRmag() {
    return rmag;
  }

  /**
   * @param rmag the rmag to set
   */
  public void setRmag(Float rmag) {
    this.rmag = rmag;
  }

  /**
   * @return the rsource
   */
  @Column(name = "Rsource")
  public String getRsource() {
    return rsource;
  }

  /**
   * @param rsource the rsource to set
   */
  public void setRsource(String rsource) {
    this.rsource = rsource;
  }

  /**
   * @return the jmag
   */
  @Column(name = "Jmag")
  public Float getJmag() {
    return jmag;
  }

  /**
   * @param jmag the jmag to set
   */
  public void setJmag(Float jmag) {
    this.jmag = jmag;
  }

  /**
   * @return the hmag
   */
  @Column(name = "Hmag")
  public Float getHmag() {
    return hmag;
  }

  /**
   * @param hmag the hmag to set
   */
  public void setHmag(Float hmag) {
    this.hmag = hmag;
  }

  /**
   * @return the kmag
   */
  @Column(name = "Kmag")
  public Float getKmag() {
    return kmag;
  }

  /**
   * @param kmag the kmag to set
   */
  public void setKmag(Float kmag) {
    this.kmag = kmag;
  }
}
