package com.gwac.model;
// Generated Aug 25, 2014 8:19:59 AM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConfigFile generated by hbm2java
 */
@Entity
@Table(name="config_file"
    ,schema="public"
)
public class ConfigFile  implements java.io.Serializable {


     private long cfId;
     private String storePath;
     private String fileName;
     private Boolean isSync;
     private Boolean isStore;

    public ConfigFile() {
    }

	
    public ConfigFile(long cfId) {
        this.cfId = cfId;
    }
    public ConfigFile(long cfId, String storePath, String fileName, Boolean isSync, Boolean isStore) {
       this.cfId = cfId;
       this.storePath = storePath;
       this.fileName = fileName;
       this.isSync = isSync;
       this.isStore = isStore;
    }
   
     @Id 
    
    @Column(name="cf_id", unique=true, nullable=false)
    public long getCfId() {
        return this.cfId;
    }
    
    public void setCfId(long cfId) {
        this.cfId = cfId;
    }
    
    @Column(name="store_path")
    public String getStorePath() {
        return this.storePath;
    }
    
    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }
    
    @Column(name="file_name")
    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Column(name="is_sync")
    public Boolean getIsSync() {
        return this.isSync;
    }
    
    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }
    
    @Column(name="is_store")
    public Boolean getIsStore() {
        return this.isStore;
    }
    
    public void setIsStore(Boolean isStore) {
        this.isStore = isStore;
    }




}


