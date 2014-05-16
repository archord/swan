/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.model;

/**
 *
 * @author xy
 */
public class ConfigFile  implements java.io.Serializable {
  
  private static final long serialVersionUID = 7863262232789607247L;
  
  private long cfId;
  private String storePath;
  private String fileName;
  private Boolean isSync;
  private Boolean isStore;

  public ConfigFile(){
    
  }
  
  public ConfigFile(long cfId){
    this.cfId = cfId;
  }
  
  /**
   * @return the cfId
   */
  public long getCfId() {
    return cfId;
  }

  /**
   * @param cfId the cfId to set
   */
  public void setCfId(long cfId) {
    this.cfId = cfId;
  }

  /**
   * @return the storePath
   */
  public String getStorePath() {
    return storePath;
  }

  /**
   * @param storePath the storePath to set
   */
  public void setStorePath(String storePath) {
    this.storePath = storePath;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param fileName the fileName to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * @return the isSync
   */
  public Boolean getIsSync() {
    return isSync;
  }

  /**
   * @param isSync the isSync to set
   */
  public void setIsSync(Boolean isSync) {
    this.isSync = isSync;
  }

  /**
   * @return the isStore
   */
  public Boolean getIsStore() {
    return isStore;
  }

  /**
   * @param isStore the isStore to set
   */
  public void setIsStore(Boolean isStore) {
    this.isStore = isStore;
  }
}
