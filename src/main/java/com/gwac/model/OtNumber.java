/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.model;

/**
 *
 * @author xy
 */
public class OtNumber implements java.io.Serializable {

  private long otnId;
  private String date;
  private int number;

  public OtNumber() {
  }

  public OtNumber(long otnId) {
    this.otnId = otnId;
  }

  public OtNumber(long otnId, String date, int number) {
    this.otnId = otnId;
    this.date = date;
    this.number = number;
  }

  /**
   * @return the otnId
   */
  public long getOtnId() {
    return otnId;
  }

  /**
   * @param otnId the otnId to set
   */
  public void setOtnId(long otnId) {
    this.otnId = otnId;
  }

  /**
   * @return the date
   */
  public String getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * @return the number
   */
  public int getNumber() {
    return number;
  }

  /**
   * @param number the number to set
   */
  public void setNumber(int number) {
    this.number = number;
  }
}
