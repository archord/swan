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
public class OtLevel2FollowParameter {

  private String userName;
  private String otName;
  private float ra;
  private float dec;
  private int expTime;
  private int frameCount;
  private String filter;
  private int priority;
  private String begineTime;
  private String endTime;
  private String followName;
  private String triggerType;
  private String epoch;
  private String imageType;
  
  public OtLevel2FollowParameter(){
    this.triggerType="2";
    this.epoch="2000";
    this.imageType="LIGHT";
    this.begineTime="-1";
    this.endTime="-1";
    this.priority = 10;
  }
  
  
  //"append_plan mini-GWAC 2\n";
  //"append_object M151013_C00001 12.1 13.1 2000 LIGHT 2 10 R 10 -1 -1 M151013_C00001_001
  public String getTriggerMsg(){
    StringBuilder sb = new StringBuilder();
    sb.append("append_plan ");
    sb.append(userName);
    sb.append(" ");
    sb.append(triggerType);
    sb.append("\n");
    sb.append("append_object ");
    sb.append(otName);
    sb.append(" ");
    sb.append(ra/15); //度转换为小时
    sb.append(" ");
    sb.append(dec);
    sb.append(" ");
    sb.append(epoch);
    sb.append(" ");
    sb.append(imageType);
    sb.append(" ");
    sb.append(expTime);
    sb.append(" ");
    sb.append(frameCount);
    sb.append(" "); 
    sb.append(filter);
    sb.append(" ");
    sb.append(getPriority());
    sb.append(" ");
    sb.append(begineTime);
    sb.append(" ");
    sb.append(endTime);
    sb.append(" ");
    sb.append(followName);
    sb.append("\n");
    return sb.toString();
  }
  
  @Override
  public String toString(){
      return "userName=" + userName + ""
              + "\n triggerType=" + triggerType
              + "\n otName=" + otName
              + "\n ra=" + ra
              + "\n dec=" + dec
              + "\n epoch=" + epoch
              + "\n imageType=" + imageType
              + "\n expTime=" + expTime
              + "\n frameCount=" + frameCount
              + "\n filter=" + filter
              + "\n begineTime=" + begineTime
              + "\n endTime=" + endTime
              + "\n followName=" + followName;
  }
  
  public Boolean isEmpty(){
    
    if (false) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * @return the userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * @param userName the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * @return the triggerType
   */
  public String getTriggerType() {
    return triggerType;
  }

  /**
   * @param triggerType the triggerType to set
   */
  public void setTriggerType(String triggerType) {
    this.triggerType = triggerType;
  }

  /**
   * @return the otName
   */
  public String getOtName() {
    return otName;
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

  /**
   * @return the ra
   */
  public float getRa() {
    return ra;
  }

  /**
   * @param ra the ra to set
   */
  public void setRa(float ra) {
    this.ra = ra;
  }

  /**
   * @return the dec
   */
  public float getDec() {
    return dec;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(float dec) {
    this.dec = dec;
  }

  /**
   * @return the epoch
   */
  public String getEpoch() {
    return epoch;
  }

  /**
   * @param epoch the epoch to set
   */
  public void setEpoch(String epoch) {
    this.epoch = epoch;
  }

  /**
   * @return the imageType
   */
  public String getImageType() {
    return imageType;
  }

  /**
   * @param imageType the imageType to set
   */
  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  /**
   * @return the expTime
   */
  public int getExpTime() {
    return expTime;
  }

  /**
   * @param expTime the expTime to set
   */
  public void setExpTime(int expTime) {
    this.expTime = expTime;
  }

  /**
   * @return the frameCount
   */
  public int getFrameCount() {
    return frameCount;
  }

  /**
   * @param frameCount the frameCount to set
   */
  public void setFrameCount(int frameCount) {
    this.frameCount = frameCount;
  }

  /**
   * @return the filter
   */
  public String getFilter() {
    return filter;
  }

  /**
   * @param filter the filter to set
   */
  public void setFilter(String filter) {
    this.filter = filter;
  }

  /**
   * @return the begineTime
   */
  public String getBegineTime() {
    return begineTime;
  }

  /**
   * @param begineTime the begineTime to set
   */
  public void setBegineTime(String begineTime) {
    this.begineTime = begineTime;
  }

  /**
   * @return the endTime
   */
  public String getEndTime() {
    return endTime;
  }

  /**
   * @param endTime the endTime to set
   */
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  /**
   * @return the followName
   */
  public String getFollowName() {
    return followName;
  }

  /**
   * @param followName the followName to set
   */
  public void setFollowName(String followName) {
    this.followName = followName;
  }

  /**
   * @return the priority
   */
  public int getPriority() {
    return priority;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }

}
