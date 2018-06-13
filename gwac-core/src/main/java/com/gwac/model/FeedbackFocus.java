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
@Table(name = "feedback_focus", schema = "public")
public class FeedbackFocus implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "fbf_id_seq")})
  @GeneratedValue(generator = "generator")
  @Column(name = "fbf_id", unique = true, nullable = false)
  private Long fbfId;
  @Column(name = "send_time_utc")
  @Temporal(TemporalType.TIMESTAMP)
  private Date sendTimeUtc;
  @Column(name = "focus")
  private Integer focus;
  @Column(name = "recv_time_utc")
  @Temporal(TemporalType.TIMESTAMP)
  private Date recvTimeUtc;

  public FeedbackFocus() {
  }

  public FeedbackFocus(Long fbfId) {
    this.fbfId = fbfId;
  }

  public Long getFbfId() {
    return fbfId;
  }

  public void setFbfId(Long fbfId) {
    this.fbfId = fbfId;
  }

  public Date getSendTimeUtc() {
    return sendTimeUtc;
  }

  public void setSendTimeUtc(Date sendTimeUtc) {
    this.sendTimeUtc = sendTimeUtc;
  }

  public Integer getFocus() {
    return focus;
  }

  public void setFocus(Integer focus) {
    this.focus = focus;
  }

  public Date getRecvTimeUtc() {
    return recvTimeUtc;
  }

  public void setRecvTimeUtc(Date recvTimeUtc) {
    this.recvTimeUtc = recvTimeUtc;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (fbfId != null ? fbfId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof FeedbackFocus)) {
      return false;
    }
    FeedbackFocus other = (FeedbackFocus) object;
    if ((this.fbfId == null && other.fbfId != null) || (this.fbfId != null && !this.fbfId.equals(other.fbfId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gwac.model.FeedbackFocus[ fbfId=" + fbfId + " ]";
  }
  
}
