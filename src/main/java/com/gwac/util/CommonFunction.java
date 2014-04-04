/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author xy
 */
public class CommonFunction {
  
  public static String getCurDateString(){
    return getDateString(new Date());
  }
  
  public static String getDateString(Date date){
    SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd"); 
    return formatter.format(date);
  }
  
  public static String getCurDateTimeString(){
    return getDateTimeString(new Date());
  }
  
  public static String getDateTimeString(Date date){
    SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss"); 
    return formatter.format(date);
  }
  
}
