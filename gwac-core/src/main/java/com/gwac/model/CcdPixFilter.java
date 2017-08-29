/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author xy
 */
@Entity
@Table(name = "ccd_pix_filter", schema = "public"
)
public class CcdPixFilter implements java.io.Serializable {

  private Long cpfId;
  private Short dpmId;
  private float minX;
  private float maxX;
  private float minY;
  private float maxY;
  private String comment;
  private Short otTypeId;

  public CcdPixFilter() {
  }

  public CcdPixFilter(long cpfId) {
    this.cpfId = cpfId;
  }

  /**
   * @return the cpfId
   */
  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "cpf_id_seq")})
  @Id
  @GeneratedValue(generator = "generator")
  @Column(name = "cpf_id", unique = true, nullable = false)
  public Long getCpfId() {
    return cpfId;
  }

  /**
   * @param cpfId the cpfId to set
   */
  public void setCpfId(Long cpfId) {
    this.cpfId = cpfId;
  }

  /**
   * @return the minX
   */
  @Column(name = "min_x")
  public float getMinX() {
    return minX;
  }

  /**
   * @param minX the minX to set
   */
  public void setMinX(float minX) {
    this.minX = minX;
  }

  /**
   * @return the maxX
   */
  @Column(name = "max_x")
  public float getMaxX() {
    return maxX;
  }

  /**
   * @param maxX the maxX to set
   */
  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  /**
   * @return the minY
   */
  @Column(name = "min_y")
  public float getMinY() {
    return minY;
  }

  /**
   * @param minY the minY to set
   */
  public void setMinY(float minY) {
    this.minY = minY;
  }

  /**
   * @return the maxY
   */
  @Column(name = "max_y")
  public float getMaxY() {
    return maxY;
  }

  /**
   * @param maxY the maxY to set
   */
  public void setMaxY(float maxY) {
    this.maxY = maxY;
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
   * @return the dpmId
   */
  @Column(name = "dpm_id")
  public Short getDpmId() {
    return dpmId;
  }

  /**
   * @param dpmId the dpmId to set
   */
  public void setDpmId(Short dpmId) {
    this.dpmId = dpmId;
  }

  /**
   * @return the otTypeId
   */
  @Column(name = "ot_type_id")
  public Short getOtTypeId() {
    return otTypeId;
  }

  /**
   * @param otTypeId the otTypeId to set
   */
  public void setOtTypeId(Short otTypeId) {
    this.otTypeId = otTypeId;
  }
}
