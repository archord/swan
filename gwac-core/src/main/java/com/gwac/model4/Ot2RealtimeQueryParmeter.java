/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.model4;

import java.util.List;

/**
 *
 * @author dell
 */
public class Ot2RealtimeQueryParmeter {

  private List<Integer> processType;
  private List<Integer> isMatch;
  private List<Integer> otType;
  private List<Integer> ccd;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("processType=");
    sb.append(processType);
    sb.append(";");
    sb.append("isMatch=");
    sb.append(isMatch);
    sb.append(";");
    sb.append("otType=");
    sb.append(otType);
    sb.append(";");
    sb.append("ccd=");
    sb.append(ccd);
    sb.append("\n");
    return sb.toString();
  }

  public Ot2RealtimeQueryParmeter() {

  }

  /**
   * @return the processType
   */
  public List<Integer> getProcessType() {
    return processType;
  }

  /**
   * @param processType the processType to set
   */
  public void setProcessType(List<Integer> processType) {
    this.processType = processType;
  }

  /**
   * @return the isMatch
   */
  public List<Integer> getIsMatch() {
    return isMatch;
  }

  /**
   * @param isMatch the isMatch to set
   */
  public void setIsMatch(List<Integer> isMatch) {
    this.isMatch = isMatch;
  }

  /**
   * @return the otType
   */
  public List<Integer> getOtType() {
    return otType;
  }

  /**
   * @param otType the otType to set
   */
  public void setOtType(List<Integer> otType) {
    this.otType = otType;
  }

  /**
   * @return the ccd
   */
  public List<Integer> getCcd() {
    return ccd;
  }

  /**
   * @param ccd the ccd to set
   */
  public void setCcd(List<Integer> ccd) {
    this.ccd = ccd;
  }
}
