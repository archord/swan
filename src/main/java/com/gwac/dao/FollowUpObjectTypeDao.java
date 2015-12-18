/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.dao;

import com.gwac.model.FollowUpObjectType;
import java.util.List;

/**
 *
 * @author xy
 */
public interface FollowUpObjectTypeDao extends BaseHibernateDao<FollowUpObjectType>{
  
  public FollowUpObjectType getOtTypeByTypeName(String typeName);
  
  public List<FollowUpObjectType> getOtTypes();
}
