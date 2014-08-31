package com.gwac.model;
// Generated 2014-8-30 13:00:35 by Hibernate Tools 3.6.0


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SystemStatus generated by hbm2java
 */
@Entity
@Table(name="system_status"
    ,schema="public"
)
public class SystemStatus  implements java.io.Serializable {


     private int id;
     private String dateStr;
     private Float totalStorageSize;
     private Float usedStorageSize;

    public SystemStatus() {
    }

	
    public SystemStatus(int id) {
        this.id = id;
    }
    public SystemStatus(int id, String dateStr, Float totalStorageSize, Float usedStorageSize) {
       this.id = id;
       this.dateStr = dateStr;
       this.totalStorageSize = totalStorageSize;
       this.usedStorageSize = usedStorageSize;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="date_str", length=6)
    public String getDateStr() {
        return this.dateStr;
    }
    
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    
    @Column(name="total_storage_size", precision=8, scale=8)
    public Float getTotalStorageSize() {
        return this.totalStorageSize;
    }
    
    public void setTotalStorageSize(Float totalStorageSize) {
        this.totalStorageSize = totalStorageSize;
    }

    
    @Column(name="used_storage_size", precision=8, scale=8)
    public Float getUsedStorageSize() {
        return this.usedStorageSize;
    }
    
    public void setUsedStorageSize(Float usedStorageSize) {
        this.usedStorageSize = usedStorageSize;
    }




}


