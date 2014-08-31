package com.gwac.model;
// Generated 2014-8-30 13:00:35 by Hibernate Tools 3.6.0


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DataProcessMachine generated by hbm2java
 */
@Entity
@Table(name="data_process_machine"
    ,schema="public"
)
public class DataProcessMachine  implements java.io.Serializable {


     private short dpmId;
     private String name;
     private String ip;
     private Short tspId;
     private Integer curProcessNumber;

    public DataProcessMachine() {
    }

	
    public DataProcessMachine(short dpmId) {
        this.dpmId = dpmId;
    }
    public DataProcessMachine(short dpmId, String name, String ip, Short tspId, Integer curProcessNumber) {
       this.dpmId = dpmId;
       this.name = name;
       this.ip = ip;
       this.tspId = tspId;
       this.curProcessNumber = curProcessNumber;
    }
   
     @Id 

    
    @Column(name="dpm_id", unique=true, nullable=false)
    public short getDpmId() {
        return this.dpmId;
    }
    
    public void setDpmId(short dpmId) {
        this.dpmId = dpmId;
    }

    
    @Column(name="name")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="ip", length=15)
    public String getIp() {
        return this.ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }

    
    @Column(name="tsp_id")
    public Short getTspId() {
        return this.tspId;
    }
    
    public void setTspId(Short tspId) {
        this.tspId = tspId;
    }

    
    @Column(name="cur_process_number")
    public Integer getCurProcessNumber() {
        return this.curProcessNumber;
    }
    
    public void setCurProcessNumber(Integer curProcessNumber) {
        this.curProcessNumber = curProcessNumber;
    }




}


