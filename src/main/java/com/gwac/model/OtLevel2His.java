package com.gwac.model;
// Generated Aug 25, 2014 8:19:59 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * OtLevel2His generated by hbm2java
 */
@Entity
@Table(name="ot_level2_his"
    ,schema="public"
)
public class OtLevel2His  implements java.io.Serializable {


     private long otId;
     private String name;
     private Float ra;
     private Float dec;
     private Date foundTimeUtc;
     private String identify;
     private Float xtemp;
     private Float ytemp;
     private Integer lastFfNumber;
     private Integer total;
     private Short succOccurTimes;
     private Short maxSuccOccurTimes;
     private Boolean isRecognize;
     private Short otType;
     private String comments;
     private String dpmName;

    public OtLevel2His() {
    }

	
    public OtLevel2His(long otId) {
        this.otId = otId;
    }
    public OtLevel2His(long otId, String name, Float ra, Float dec, Date foundTimeUtc, String identify, Float xtemp, Float ytemp, Integer lastFfNumber, Integer total, Short succOccurTimes, Short maxSuccOccurTimes, Boolean isRecognize, Short otType, String comments, String dpmName) {
       this.otId = otId;
       this.name = name;
       this.ra = ra;
       this.dec = dec;
       this.foundTimeUtc = foundTimeUtc;
       this.identify = identify;
       this.xtemp = xtemp;
       this.ytemp = ytemp;
       this.lastFfNumber = lastFfNumber;
       this.total = total;
       this.succOccurTimes = succOccurTimes;
       this.maxSuccOccurTimes = maxSuccOccurTimes;
       this.isRecognize = isRecognize;
       this.otType = otType;
       this.comments = comments;
       this.dpmName = dpmName;
    }
   
     @Id 
    
    @Column(name="ot_id", unique=true, nullable=false)
    public long getOtId() {
        return this.otId;
    }
    
    public void setOtId(long otId) {
        this.otId = otId;
    }
    
    @Column(name="name", length=12)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="found_time_utc", length=29)
    public Date getFoundTimeUtc() {
        return this.foundTimeUtc;
    }
    
    public void setFoundTimeUtc(Date foundTimeUtc) {
        this.foundTimeUtc = foundTimeUtc;
    }
    
    @Column(name="identify", length=21)
    public String getIdentify() {
        return this.identify;
    }
    
    public void setIdentify(String identify) {
        this.identify = identify;
    }
    
    @Column(name="xtemp", precision=8, scale=8)
    public Float getXtemp() {
        return this.xtemp;
    }
    
    public void setXtemp(Float xtemp) {
        this.xtemp = xtemp;
    }
    
    @Column(name="ytemp", precision=8, scale=8)
    public Float getYtemp() {
        return this.ytemp;
    }
    
    public void setYtemp(Float ytemp) {
        this.ytemp = ytemp;
    }
    
    @Column(name="last_ff_number")
    public Integer getLastFfNumber() {
        return this.lastFfNumber;
    }
    
    public void setLastFfNumber(Integer lastFfNumber) {
        this.lastFfNumber = lastFfNumber;
    }
    
    @Column(name="total")
    public Integer getTotal() {
        return this.total;
    }
    
    public void setTotal(Integer total) {
        this.total = total;
    }
    
    @Column(name="succ_occur_times")
    public Short getSuccOccurTimes() {
        return this.succOccurTimes;
    }
    
    public void setSuccOccurTimes(Short succOccurTimes) {
        this.succOccurTimes = succOccurTimes;
    }
    
    @Column(name="max_succ_occur_times")
    public Short getMaxSuccOccurTimes() {
        return this.maxSuccOccurTimes;
    }
    
    public void setMaxSuccOccurTimes(Short maxSuccOccurTimes) {
        this.maxSuccOccurTimes = maxSuccOccurTimes;
    }
    
    @Column(name="is_recognize")
    public Boolean getIsRecognize() {
        return this.isRecognize;
    }
    
    public void setIsRecognize(Boolean isRecognize) {
        this.isRecognize = isRecognize;
    }
    
    @Column(name="ot_type")
    public Short getOtType() {
        return this.otType;
    }
    
    public void setOtType(Short otType) {
        this.otType = otType;
    }
    
    @Column(name="comments", length=1024)
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    @Column(name="dpm_name", length=3)
    public String getDpmName() {
        return this.dpmName;
    }
    
    public void setDpmName(String dpmName) {
        this.dpmName = dpmName;
    }




}


