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
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="otLevel2MatchDao")
public class OtLevel2MatchDaoImpl extends BaseHibernateDaoImpl<OtLevel2Match> implements OtLevel2MatchDao {

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
  public OtLevel2Match getByOt2Id(long ot2Id) {
    String sql1 = "select * from ot_level2_match where ot_id=" + ot2Id;

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql1).addEntity(OtLevel2Match.class);
    OtLevel2Match rst=null;
    if(q.list().size()>0){
      rst = (OtLevel2Match)q.list().get(0);
    }
    return rst;
  }
  
  @Override
  public OtLevel2Match getByOt2IdVariStar(long ot2Id) {
    String sql1 = "select * from ot_level2_match where mt_id=9 and ot_id=" + ot2Id;

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql1).addEntity(OtLevel2Match.class);
    OtLevel2Match rst=null;
    if(q.list().size()>0){
      rst = (OtLevel2Match)q.list().get(0);
    }
    return rst;
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
    Query q = session.createSQLQuery(sql1).addEntity(OtLevel2Match.class);
    if (q.list().isEmpty()) {
      session.createSQLQuery(sql2).executeUpdate();
    } else {
      session.createSQLQuery(sql3).executeUpdate();
    }
  }

 @Override
  public List<OtLevel2MatchShow> getByOt2Name(String otName, Boolean queryHis) {
    Session session = getCurrentSession();
    String sql = "select mt.comments match_table_name, ot2h.name ot2_name, olm.* " //显示match_table_name不便于理解，偷懒，直接显示注释中文名
            + "from ot_level2_match olm "
            + "inner join match_table mt on mt.mt_id=olm.mt_id " // and mt.match_table_name='ot_level2_his'
            + "left join ot_level2_his ot2h on ot2h.ot_id=olm.match_id and mt.match_table_name='ot_level2_his'"
            + "inner join ";

    if (queryHis) {
      sql += " ot_level2_his ";
    } else {
      sql += " ot_level2 ";
    }
    sql += "ot2 on ot2.ot_id=olm.ot_id and ot2.name='" + otName + "';";

    Query q = session.createSQLQuery(sql).addEntity(OtLevel2MatchShow.class);
    return q.list();
  }

}
