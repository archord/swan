/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
  
  public static List<Integer> getMissedNumber(List<Integer> nums){
    List<Integer> mNums = new ArrayList<Integer> ();
    for(int i=0; i<nums.size()-1; i++){
      for(int j= nums.get(i)+1; j<nums.get(i+1); j++){
        mNums.add(j);
      }
    }
    return mNums;
  }
  
}
