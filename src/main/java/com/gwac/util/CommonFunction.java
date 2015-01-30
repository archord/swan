/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class CommonFunction {

  public static final float MINFLOAT = (float) 0.000001;

  private static final Log log = LogFactory.getLog(CommonFunction.class);

  public static String getCurTimeString() {
    return getTimeString(new Date());
  }

  public static String getTimeString(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
    return formatter.format(date);
  }

  public static String getCurDateString() {
    return getDateString(new Date());
  }

  public static String getDateString(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    return formatter.format(date);
  }

  public static String getCurDateTimeString() {
    return getDateTimeString(new Date());
  }

  public static String getDateTimeString(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    return formatter.format(date);
  }

  public static String getDateTimeString(Date date, String formater) {
    SimpleDateFormat formatter = new SimpleDateFormat(formater);
    return formatter.format(date);
  }

  /**
   *
   * @param dateStr
   * @param formater yyyy-MM-dd HH:mm:ss
   * @return
   */
  public static Date stringToDate(String dateStr, String formater) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(formater);
      return sdf.parse(dateStr);
    } catch (ParseException ex) {
      log.error("string to date error.", ex);
    }
    return new Date();
  }

  public static List<Integer> getMissedNumber(List<Integer> nums) {
    List<Integer> mNums = new ArrayList<Integer>();
    for (int i = 0; i < nums.size() - 1; i++) {
      for (int j = nums.get(i) + 1; j < nums.get(i + 1); j++) {
        mNums.add(j);
      }
    }
    return mNums;
  }

  public static Date getUTCDate(Date date) {
    SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
    SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date dateStr = null;
    try {
      dateStr = dateFormatLocal.parse(dateFormatGmt.format(date));
    } catch (ParseException ex) {
      Logger.getLogger(CommonFunction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return dateStr;
  }

  public static double dateToJulian(Date date) {

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);

    int year;
    int month;
    float day;
    int a;
    int b;
    double d;
    double frac;

    frac = (calendar.get(Calendar.HOUR_OF_DAY) / 0.000024 + calendar.get(Calendar.MINUTE) / 0.001440);

    b = 0;

    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH) + 1;

    DecimalFormat ceroPlaces = new DecimalFormat("0");
    day = calendar.get(Calendar.DAY_OF_MONTH);
    day = Float.parseFloat(ceroPlaces.format(day) + "." + ceroPlaces.format(Math.round(frac)));

    if (month < 3) {
      year--;
      month += 12;
    }
    if (FuncionSoporte.compararFechas(calendar.getTime(), calendar.getGregorianChange()) > 0) {
      a = year / 100;
      b = 2 - a + a / 4;
    }
    d = Math.floor(365.25 * year) + Math.floor(30.6001 * (month + 1)) + day + 1720994.5 + b;

    return (d);
  }

  public static Date julianToDate(double jd) {

    double z, f, a, b, c, d, e, m, aux;
    Date date = new Date();
    jd += 0.5;
    z = Math.floor(jd);
    f = jd - z;

    if (z >= 2299161.0) {
      a = Math.floor((z - 1867216.25) / 36524.25);
      a = z + 1 + a - Math.floor(a / 4);
    } else {
      a = z;
    }

    b = a + 1524;
    c = Math.floor((b - 122.1) / 365.25);
    d = Math.floor(365.25 * c);
    e = Math.floor((b - d) / 30.6001);
    aux = b - d - Math.floor(30.6001 * e) + f;

    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, (int) aux);
    aux = ((aux - calendar.get(Calendar.DAY_OF_MONTH)) * 24);
    calendar.set(Calendar.HOUR_OF_DAY, (int) aux);
    calendar.set(Calendar.MINUTE, (int) ((aux - calendar.get(Calendar.HOUR_OF_DAY)) * 60));

    if (e < 13.5) {
      m = e - 1;
    } else {
      m = e - 13;
    }
    // Se le resta uno al mes por el manejo de JAVA, donde los meses empiezan en 0.
    calendar.set(Calendar.MONTH, (int) m - 1);
    if (m > 2.5) {
      calendar.set(Calendar.YEAR, (int) (c - 4716));
    } else {
      calendar.set(Calendar.YEAR, (int) (c - 4715));
    }
    return calendar.getTime();
  }

  public static double getGreatCircleDistance(double ra1, double dec1, double ra2, double dec2) {
    double rst = 57.295779513 * Math.acos(Math.sin(0.017453293 * dec1) * Math.sin(0.017453293 * dec2)
            + Math.cos(0.017453293 * dec1) * Math.cos(0.017453293 * dec2) * Math.cos(0.017453293 * (Math.abs(ra1 - ra2))));
    return rst;
  }

  public static double getLineDistance(double x1, double y1, double x2, double y2) {
    double tmp = (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    return Math.sqrt(tmp);
  }
}
