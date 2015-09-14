package com.gwac.model;
// Generated 2015-9-12 20:09:53 by Hibernate Tools 3.6.0


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * VarStarRecord generated by hbm2java
 */
@Entity
@Table(name="var_star_record"
    ,schema="public"
)
public class VarStarRecord  implements java.io.Serializable {


     private long vsrId;
     private Long ffId;
     private Long ffcId;
     private Short otTypeId;
     private Float raD;
     private Float decD;
     private Float x;
     private Float y;
     private Float XTemp;
     private Float YTemp;
     private Date dateUt;
     private Float flux;
     private Boolean flag;
     private Float flagChb;
     private Float background;
     private Float threshold;
     private Float magAper;
     private Float magerrAper;
     private Float ellipticity;
     private Float classStar;
     private Boolean otFlag;
     private Integer ffNumber;
     private Integer dpmId;
     private String dateStr;
     private Boolean requestCut;
     private Boolean successCut;
     private Long vsId;
     private Float distance;
     private Float deltamag;

    public VarStarRecord() {
    }

	
    public VarStarRecord(long vsrId) {
        this.vsrId = vsrId;
    }
    public VarStarRecord(long vsrId, Long ffId, Long ffcId, Short otTypeId, Float raD, Float decD, Float x, Float y, Float XTemp, Float YTemp, Date dateUt, Float flux, Boolean flag, Float flagChb, Float background, Float threshold, Float magAper, Float magerrAper, Float ellipticity, Float classStar, Boolean otFlag, Integer ffNumber, Integer dpmId, String dateStr, Boolean requestCut, Boolean successCut, Long vsId, Float distance, Float deltamag) {
       this.vsrId = vsrId;
       this.ffId = ffId;
       this.ffcId = ffcId;
       this.otTypeId = otTypeId;
       this.raD = raD;
       this.decD = decD;
       this.x = x;
       this.y = y;
       this.XTemp = XTemp;
       this.YTemp = YTemp;
       this.dateUt = dateUt;
       this.flux = flux;
       this.flag = flag;
       this.flagChb = flagChb;
       this.background = background;
       this.threshold = threshold;
       this.magAper = magAper;
       this.magerrAper = magerrAper;
       this.ellipticity = ellipticity;
       this.classStar = classStar;
       this.otFlag = otFlag;
       this.ffNumber = ffNumber;
       this.dpmId = dpmId;
       this.dateStr = dateStr;
       this.requestCut = requestCut;
       this.successCut = successCut;
       this.vsId = vsId;
       this.distance = distance;
       this.deltamag = deltamag;
    }
   
     @Id 

    
    @Column(name="vsr_id", unique=true, nullable=false)
    public long getVsrId() {
        return this.vsrId;
    }
    
    public void setVsrId(long vsrId) {
        this.vsrId = vsrId;
    }

    
    @Column(name="ff_id")
    public Long getFfId() {
        return this.ffId;
    }
    
    public void setFfId(Long ffId) {
        this.ffId = ffId;
    }

    
    @Column(name="ffc_id")
    public Long getFfcId() {
        return this.ffcId;
    }
    
    public void setFfcId(Long ffcId) {
        this.ffcId = ffcId;
    }

    
    @Column(name="ot_type_id")
    public Short getOtTypeId() {
        return this.otTypeId;
    }
    
    public void setOtTypeId(Short otTypeId) {
        this.otTypeId = otTypeId;
    }

    
    @Column(name="ra_d", precision=8, scale=8)
    public Float getRaD() {
        return this.raD;
    }
    
    public void setRaD(Float raD) {
        this.raD = raD;
    }

    
    @Column(name="dec_d", precision=8, scale=8)
    public Float getDecD() {
        return this.decD;
    }
    
    public void setDecD(Float decD) {
        this.decD = decD;
    }

    
    @Column(name="x", precision=8, scale=8)
    public Float getX() {
        return this.x;
    }
    
    public void setX(Float x) {
        this.x = x;
    }

    
    @Column(name="y", precision=8, scale=8)
    public Float getY() {
        return this.y;
    }
    
    public void setY(Float y) {
        this.y = y;
    }

    
    @Column(name="x_temp", precision=8, scale=8)
    public Float getXTemp() {
        return this.XTemp;
    }
    
    public void setXTemp(Float XTemp) {
        this.XTemp = XTemp;
    }

    
    @Column(name="y_temp", precision=8, scale=8)
    public Float getYTemp() {
        return this.YTemp;
    }
    
    public void setYTemp(Float YTemp) {
        this.YTemp = YTemp;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_ut", length=29)
    public Date getDateUt() {
        return this.dateUt;
    }
    
    public void setDateUt(Date dateUt) {
        this.dateUt = dateUt;
    }

    
    @Column(name="flux", precision=8, scale=8)
    public Float getFlux() {
        return this.flux;
    }
    
    public void setFlux(Float flux) {
        this.flux = flux;
    }

    
    @Column(name="flag")
    public Boolean getFlag() {
        return this.flag;
    }
    
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    
    @Column(name="flag_chb", precision=8, scale=8)
    public Float getFlagChb() {
        return this.flagChb;
    }
    
    public void setFlagChb(Float flagChb) {
        this.flagChb = flagChb;
    }

    
    @Column(name="background", precision=8, scale=8)
    public Float getBackground() {
        return this.background;
    }
    
    public void setBackground(Float background) {
        this.background = background;
    }

    
    @Column(name="threshold", precision=8, scale=8)
    public Float getThreshold() {
        return this.threshold;
    }
    
    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    
    @Column(name="mag_aper", precision=8, scale=8)
    public Float getMagAper() {
        return this.magAper;
    }
    
    public void setMagAper(Float magAper) {
        this.magAper = magAper;
    }

    
    @Column(name="magerr_aper", precision=8, scale=8)
    public Float getMagerrAper() {
        return this.magerrAper;
    }
    
    public void setMagerrAper(Float magerrAper) {
        this.magerrAper = magerrAper;
    }

    
    @Column(name="ellipticity", precision=8, scale=8)
    public Float getEllipticity() {
        return this.ellipticity;
    }
    
    public void setEllipticity(Float ellipticity) {
        this.ellipticity = ellipticity;
    }

    
    @Column(name="class_star", precision=8, scale=8)
    public Float getClassStar() {
        return this.classStar;
    }
    
    public void setClassStar(Float classStar) {
        this.classStar = classStar;
    }

    
    @Column(name="ot_flag")
    public Boolean getOtFlag() {
        return this.otFlag;
    }
    
    public void setOtFlag(Boolean otFlag) {
        this.otFlag = otFlag;
    }

    
    @Column(name="ff_number")
    public Integer getFfNumber() {
        return this.ffNumber;
    }
    
    public void setFfNumber(Integer ffNumber) {
        this.ffNumber = ffNumber;
    }

    
    @Column(name="dpm_id")
    public Integer getDpmId() {
        return this.dpmId;
    }
    
    public void setDpmId(Integer dpmId) {
        this.dpmId = dpmId;
    }

    
    @Column(name="date_str", length=6)
    public String getDateStr() {
        return this.dateStr;
    }
    
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    
    @Column(name="request_cut")
    public Boolean getRequestCut() {
        return this.requestCut;
    }
    
    public void setRequestCut(Boolean requestCut) {
        this.requestCut = requestCut;
    }

    
    @Column(name="success_cut")
    public Boolean getSuccessCut() {
        return this.successCut;
    }
    
    public void setSuccessCut(Boolean successCut) {
        this.successCut = successCut;
    }

    
    @Column(name="vs_id")
    public Long getVsId() {
        return this.vsId;
    }
    
    public void setVsId(Long vsId) {
        this.vsId = vsId;
    }

    
    @Column(name="distance", precision=8, scale=8)
    public Float getDistance() {
        return this.distance;
    }
    
    public void setDistance(Float distance) {
        this.distance = distance;
    }

    
    @Column(name="deltamag", precision=8, scale=8)
    public Float getDeltamag() {
        return this.deltamag;
    }
    
    public void setDeltamag(Float deltamag) {
        this.deltamag = deltamag;
    }




}


