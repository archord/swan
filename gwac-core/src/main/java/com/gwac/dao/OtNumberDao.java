/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtNumber;

/**
 *
 * @author xy
 */
public interface OtNumberDao extends BaseHibernateDao<OtNumber>{
  public int getNumberByDate(String date);
  public int getSubNumberByDate(String date);
  public int getJfovNumberByDate(String date);
  public int getJfovSubNumberByDate(String date);
}
