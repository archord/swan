/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.CrossObjectMatch;
import com.gwac.model.OtLevel2MatchShow;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="crossObjectMatchDao")
public class CrossObjectMatchDaoImpl extends BaseHibernateDaoImpl<CrossObjectMatch> implements CrossObjectMatchDao {

    @Override
  public List<Long> getIdsByStarType(int starType) {

    Session session = getCurrentSession();
    String sql = "select DISTINCT match_id "
            + "from ot_level2_match "
            + "where mt_id=" + starType
            + " ORDER BY match_id;";

    Query q = session.createSQLQuery(sql);
    return q.list();
  }
  
  @Override
  public CrossObjectMatch getByOt2Id(long ot2Id) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * ot2历史匹配，可能存在一个OT2匹配多个ot2历史模板目标的情况，
   * 在后续的模板更新过程中，OT2匹配的多个ot2历史模板目标或许会被合并为一个，
   * ot2历史模板目标合并之后，需要对ot2匹配记录进行更新：将晚出现的ot2模板id
   * 更新为早出现的otid，如果早出现的已经存在，则直接删除晚出现的记录，
   * 如果不存在，则更新为早出现的ot2目标id
   * 
   * @param curId 目标Ot
   * @param fromId 晚出现的ot2模板id
   * @param toId 早出现的ot2模板id
   */
  @Override
  public void updateOt2HisMatchId(long curId, long fromId, long toId) {

    String sql1 = "select * from ot_level2_match where mt_id=6 and ot_id=" + curId + " and match_id=" + toId;
    String sql2 = "update ot_level2_match set match_id=" + toId + " where mt_id=6 and match_id=" + fromId;
    String sql3 = "delete from ot_level2_match where mt_id=6 and ot_id=" + curId + " and match_id=" + toId;

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql1).addEntity(CrossObjectMatch.class);
    if (q.list().isEmpty()) {
      session.createSQLQuery(sql2).executeUpdate();
    } else {
      session.createSQLQuery(sql3).executeUpdate();
    }
  }

}
