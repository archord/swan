/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author xy
 */
public class FuncionSoporte {

  /**
   * Compare 2 dates. If the first is after the second result will be positive,
   * if the second is after then negative, 0 if they are equal.
   */
  public static int compararFechas(Date d1, Date d2) {

    Calendar c1 = new GregorianCalendar();
    c1.setTime(d1);
    Calendar c2 = new GregorianCalendar();
    c2.setTime(d2);

    if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
      if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
        return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
      } else {
        return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
      }
    } else {
      return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
    }
  }
}
