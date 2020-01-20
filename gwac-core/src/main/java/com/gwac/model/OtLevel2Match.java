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
 * OtLevel2Match generated by hbm2java
 */
@Entity
@Table(name="ot_level2_match"
    ,schema="public"
)
public class OtLevel2Match  implements java.io.Serializable {

     private long olmId;
     private Long otId;
     private Short mtId;
     private Long matchId;
     private String comments;
     private Float ra;
     private Float dec;
     private Float mag;
     private Float distance;
     private Float d25;
     private Float period;
     private String type;

    public OtLevel2Match() {
    }
   
  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "ot_level2_match_id_seq")})
  @Id
  @GeneratedValue(generator = "generator")
  @Column(name = "olm_id", unique = true, nullable = false)
    public long getOlmId() {
        return this.olmId;
    }
    
    public void setOlmId(long olmId) {
        this.olmId = olmId;
    }

    
    @Column(name="ot_id")
    public Long getOtId() {
        return this.otId;
    }
    
    public void setOtId(Long otId) {
        this.otId = otId;
    }

    
    @Column(name="mt_id")
    public Short getMtId() {
        return this.mtId;
    }
    
    public void setMtId(Short mtId) {
        this.mtId = mtId;
    }

    
    @Column(name="match_id")
    public Long getMatchId() {
        return this.matchId;
    }
    
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    
    @Column(name="comments", length=1024)
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    
    @Column(name="ra", precision=8, scale=8)
    public Float getRa() {
        return this.ra;
    }
    
    public void setRa(Float ra) {
        this.ra = ra;
    }

    
    @Column(name="dec", precision=8, scale=8)
    public Float getDec() {
        return this.dec;
    }
    
    public void setDec(Float dec) {
        this.dec = dec;
    }

    
    @Column(name="mag", precision=8, scale=8)
    public Float getMag() {
        return this.mag;
    }
    
    public void setMag(Float mag) {
        this.mag = mag;
    }

  /**
   * @return the distance
   */
    @Column(name="distance", precision=8, scale=8)
  public Float getDistance() {
    return distance;
  }

  /**
   * @param distance the distance to set
   */
  public void setDistance(Float distance) {
    this.distance = distance;
  }

  /**
   * @return the d25
   */
    @Column(name="d25", precision=8, scale=8)
  public Float getD25() {
    return d25;
  }

  /**
   * @param d25 the d25 to set
   */
  public void setD25(Float d25) {
    this.d25 = d25;
  }

  /**
   * @return the period
   */
    @Column(name="period", precision=8, scale=8)
  public Float getPeriod() {
    return period;
  }

  /**
   * @param period the period to set
   */
  public void setPeriod(Float period) {
    this.period = period;
  }

  /**
   * @return the type
   */
    @Column(name="type")
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }


}


