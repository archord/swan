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
@Table(name = "coordinate_show", schema = "public"
)
public class CoordinateShow implements java.io.Serializable {

  private long id;
  private Float ra;
  private Float dec;
  private Float mag;
  private String name;

  public CoordinateShow() {
  }

  public CoordinateShow(long id) {
    this.id = id;
  }


  @Id
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
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
   * @return the mag
   */
  @Column(name = "mag")
  public Float getMag() {
    return mag;
  }

  /**
   * @param mag the mag to set
   */
  public void setMag(Float mag) {
    this.mag = mag;
  }

  /**
   * @return the name
   */
  @Column(name = "name")
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

}