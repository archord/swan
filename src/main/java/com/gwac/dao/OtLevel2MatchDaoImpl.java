/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtLevel2Match;
import com.gwac.model.OtLevel2MatchShow;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtLevel2MatchDaoImpl extends BaseHibernateDaoImpl<OtLevel2Match> implements OtLevel2MatchDao {

  @Override
  public OtLevel2Match getByOt2Id(long ot2Id) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<OtLevel2MatchShow> getByOt2Name(String otName, Boolean queryHis) {
    Session session = getCurrentSession();
    String sql = "select mt.comments match_table_name, ot2h.name ot2_name, olm.* " //显示match_table_name不便于理解，偷懒，直接显示注释中文名
            + "from ot_level2_match olm "
            + "inner join match_table mt on mt.mt_id=olm.mt_id "  // and mt.match_table_name='ot_level2_his'
            + "left join ot_level2_his ot2h on ot2h.ot_id=olm.match_id and mt.match_table_name='ot_level2_his'"
            + "inner join ";
    
    if(queryHis){
      sql += " ot_level2_his ";
    }else{
      sql += " ot_level2 ";
    }
    sql += "ot2 on ot2.ot_id=olm.ot_id and ot2.name='" + otName + "';";
    
    Query q = session.createSQLQuery(sql).addEntity(OtLevel2MatchShow.class);
    return q.list();
  }

}
