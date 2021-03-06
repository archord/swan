package com.gwac.model2;
// Generated 2015-1-31 21:02:51 by Hibernate Tools 3.6.0


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Rc3 generated by hbm2java
 */
@Entity
@Table(name="rc3"
    ,catalog="catalogue"
)
public class Rc3  implements java.io.Serializable {


     private Integer idnum;
     private String pgcid;
     private Float radeg;
     private Float dedeg;
     private Float gallongdeg;
     private Float gallatitdeg;
     private Float hubs;
     private Float mumag;
     private Float mbmag;
     private Float mvmag;
     private Float redshift;
     private Float d25;
     private Float d25err;
     private Float angularSize;

    public Rc3() {
    }

    public Rc3(String pgcid, Float radeg, Float dedeg, Float gallongdeg, Float gallatitdeg, Float hubs, Float mumag, Float mbmag, Float mvmag, Float redshift, Float d25, Float d25err) {
       this.pgcid = pgcid;
       this.radeg = radeg;
       this.dedeg = dedeg;
       this.gallongdeg = gallongdeg;
       this.gallatitdeg = gallatitdeg;
       this.hubs = hubs;
       this.mumag = mumag;
       this.mbmag = mbmag;
       this.mvmag = mvmag;
       this.redshift = redshift;
       this.d25 = d25;
       this.d25err = d25err;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="IDnum", unique=true, nullable=false)
    public Integer getIdnum() {
        return this.idnum;
    }
    
    public void setIdnum(Integer idnum) {
        this.idnum = idnum;
    }

    
    @Column(name="PGCID", length=10)
    public String getPgcid() {
        return this.pgcid;
    }
    
    public void setPgcid(String pgcid) {
        this.pgcid = pgcid;
    }

    
    @Column(name="RAdeg", precision=10, scale=6)
    public Float getRadeg() {
        return this.radeg;
    }
    
    public void setRadeg(Float radeg) {
        this.radeg = radeg;
    }

    
    @Column(name="DEdeg", precision=10, scale=6)
    public Float getDedeg() {
        return this.dedeg;
    }
    
    public void setDedeg(Float dedeg) {
        this.dedeg = dedeg;
    }

    
    @Column(name="gallongdeg", precision=9, scale=6)
    public Float getGallongdeg() {
        return this.gallongdeg;
    }
    
    public void setGallongdeg(Float gallongdeg) {
        this.gallongdeg = gallongdeg;
    }

    
    @Column(name="gallatitdeg", precision=9, scale=6)
    public Float getGallatitdeg() {
        return this.gallatitdeg;
    }
    
    public void setGallatitdeg(Float gallatitdeg) {
        this.gallatitdeg = gallatitdeg;
    }

    
    @Column(name="hubs", precision=9, scale=6)
    public Float getHubs() {
        return this.hubs;
    }
    
    public void setHubs(Float hubs) {
        this.hubs = hubs;
    }

    
    @Column(name="mUmag", precision=9, scale=6)
    public Float getMumag() {
        return this.mumag;
    }
    
    public void setMumag(Float mumag) {
        this.mumag = mumag;
    }

    
    @Column(name="mBmag", precision=9, scale=6)
    public Float getMbmag() {
        return this.mbmag;
    }
    
    public void setMbmag(Float mbmag) {
        this.mbmag = mbmag;
    }

    
    @Column(name="mVmag", precision=9, scale=6)
    public Float getMvmag() {
        return this.mvmag;
    }
    
    public void setMvmag(Float mvmag) {
        this.mvmag = mvmag;
    }

    
    @Column(name="redshift", precision=9, scale=6)
    public Float getRedshift() {
        return this.redshift;
    }
    
    public void setRedshift(Float redshift) {
        this.redshift = redshift;
    }

  /**
   * @return the d25
   */
    @Column(name="d25", precision=9, scale=6)
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
   * @return the d25err
   */
    @Column(name="d25err", precision=9, scale=6)
  public Float getD25err() {
    return d25err;
  }

  /**
   * @param d25err the d25err to set
   */
  public void setD25err(Float d25err) {
    this.d25err = d25err;
  }

  /**
   * @return the angularSize 
   */
    @Column(name="angular_size", precision=9, scale=6)
  public Float getAngularSize() {
    return angularSize;
  }

  /**
   * @param angularSize the angularSize to set
   */
  public void setAngularSize(Float angularSize) {
    this.angularSize = angularSize;
  }




}


