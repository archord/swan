/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FeedbackFocus;

/**
 *
 * @author msw
 */
public interface FeedbackFocusDao extends BaseHibernateDao<FeedbackFocus> {
  public FeedbackFocus getByFbfId(Long ispId);
  public String getRecords(String camera, int days);
}
