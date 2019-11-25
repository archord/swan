/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtHistoryRepeat;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtHistoryRepeatDao extends BaseHibernateDao<OtHistoryRepeat> {

  public List<OtHistoryRepeat> exist(Integer camId, float xTemp, float yTemp, float x, float y);
}
