/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.dao;

import com.gwac.model.FollowUpObject;
import java.util.List;

/**
 *
 * @author xy
 */
public interface FollowUpObjectDao extends BaseHibernateDao<FollowUpObject>{
  
  public int countTypeNumberByOtId(FollowUpObject obj);
  
  public int countTypeNumberByFoId(FollowUpObject obj);
  
  public List<FollowUpObject> exist(FollowUpObject obj, float errorBox);
  
  public List<FollowUpObject> getByOtId(long otId, Boolean queryHis);
  
   public List<FollowUpObject> getByOtIdTypeId(long otId, short fuoTypeId, Boolean queryHis);
}
