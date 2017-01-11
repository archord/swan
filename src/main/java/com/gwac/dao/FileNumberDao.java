/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FileNumber;

/**
 *
 * @author xy
 */
public interface FileNumberDao extends BaseHibernateDao<FileNumber> {

  public int getNextNumber(FileNumber fnum);
}
