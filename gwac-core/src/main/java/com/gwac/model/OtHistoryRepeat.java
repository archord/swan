package com.gwac.model;
// Generated 2015-10-2 9:40:37 by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ConfigFile generated by hbm2java
 */
@Entity
@Table(name = "ot_history_repeat", schema = "public"
)
public class OtHistoryRepeat implements java.io.Serializable {


  private long ohrId;
  private Integer camId;
  private Integer x;
  private Integer y;
  private Integer num;

  public OtHistoryRepeat() {
  }

  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "ohr_id_seq")})
  @Id
  @GeneratedValue(generator = "generator")
  @Column(name = "ohr_id", unique = true, nullable = false)
  public long getOhrId() {
    return ohrId;
  }

  /**
   * @param ohrId the ohrId to set
   */
  public void setOhrId(long ohrId) {
    this.ohrId = ohrId;
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
   * @return the x
   */
  @Column(name = "x")
  public Integer getX() {
    return x;
  }

  /**
   * @param x the x to set
   */
  public void setX(Integer x) {
    this.x = x;
  }

  /**
   * @return the y
   */
  @Column(name = "y")
  public Integer getY() {
    return y;
  }

  /**
   * @param y the y to set
   */
  public void setY(Integer y) {
    this.y = y;
  }

  /**
   * @return the num
   */
  @Column(name = "num")
  public Integer getNum() {
    return num;
  }

  /**
   * @param num the num to set
   */
  public void setNum(Integer num) {
    this.num = num;
  }

}
