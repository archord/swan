/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtObserveRecord;
import com.gwac.model.OtObserveRecordShow;
import com.gwac.util.CommonFunction;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtObserveRecordDAOImpl extends BaseHibernateDaoImpl<OtObserveRecord> implements OtObserveRecordDAO {

  private static final Log log = LogFactory.getLog(OtObserveRecordDAOImpl.class);

  @Override
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM ot_observe_record RETURNING * ) INSERT INTO ot_observe_record_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String getOtOpticalVaration(String otName) {
    Session session = getCurrentSession();
    String sql1 = "select oor.date_ut, oor.mag_aper, oor.oor_id "
            + " from ot_observe_record oor "
            + " inner join ot_level2 ot2 on ot2.ot_id=oor.ot_id and ot2.name='" + otName + "' "
            + " order by oor.oor_id asc";
    
    String sql2 = "select oorh.date_ut, oorh.mag_aper, oorh.oor_id "
            + " from ot_observe_record_his oorh "
            + " inner join ot_level2_his ot2h on ot2h.ot_id=oorh.ot_id and ot2h.name='" + otName + "' "
            + " order by oorh.oor_id asc";
    
    String unionSql = "(" + sql1 + ") union (" + sql2 + ")";

    Query q = session.createSQLQuery(sql1);
    Iterator itor = q.list().iterator();

    Date baseDate = CommonFunction.stringToDate("2015-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    cal.setTime(baseDate);
    double baseDay = cal.getTimeInMillis() / (1000.0 * 3600 * 24);

    StringBuilder sb = new StringBuilder();
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      try {
        cal.setTime((Date) row[0]);
        double now = cal.getTimeInMillis() / (1000.0 * 3600 * 24);
        sb.append("[");
        sb.append(now - baseDay);
        sb.append(",");
        sb.append(row[1]);
        sb.append("],");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    String tStr = sb.toString();
    if (tStr.isEmpty()) {
      tStr = "[]";
    } else {
      tStr = "[" + tStr + "]";
    }
    return tStr;
  }

  public String getUnCuttedStarList(int dpmId) {
    Session session = getCurrentSession();
    String sql = "select ff.file_name ffname, oor.x, oor.y, ffc.file_name ffcname "
            + " from ot_observe_record oor "
            + " inner join fits_file ff on oor.ff_id=ff.ff_id "
            + " inner join fits_file_cut ffc on oor.ffc_id=ffc.ffc_id "
            + " where oor.ot_id>0 and oor.request_cut=false and oor.dpm_id=" + dpmId;
    Query q = session.createSQLQuery(sql);
    Iterator itor = q.list().iterator();

    StringBuilder rst = new StringBuilder();
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      try {
        rst.append(row[0]);
        rst.append(" ");
        rst.append(row[1]);
        rst.append(" ");
        rst.append(row[2]);
        rst.append(" ");
        rst.append(row[3]);
        rst.append("\n");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    sql = "update ot_observe_record set request_cut=true where ot_id>0 and request_cut=false and dpm_id=" + dpmId;
    session.createSQLQuery(sql).executeUpdate();
    return rst.toString();
  }

  public String getUnCuttedStarList_bck(int dpmId) {
    Session session = getCurrentSession();
    String sql = "select ff.file_name ffname, oor.x, oor.y, ffc.file_name ffcname "
            + " from ot_observe_record oor "
            + " inner join fits_file ff on oor.ff_id=ff.ff_id "
            + " inner join fits_file_cut ffc on oor.ffc_id=ffc.ffc_id "
            + " where oor.ot_id>0 and oor.request_cut=false and oor.dpm_id=" + dpmId;
    Query q = session.createSQLQuery(sql);
    Iterator itor = q.list().iterator();

    StringBuilder rst = new StringBuilder();
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      try {
        rst.append(row[0]);
        rst.append(" ");
        rst.append(row[1]);
        rst.append(" ");
        rst.append(row[2]);
        rst.append(" ");
        rst.append(row[3]);
        rst.append("\n");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    sql = "update ot_observe_record set request_cut=true where ot_id>0 and request_cut=false and dpm_id=" + dpmId;
    session.createSQLQuery(sql).executeUpdate();
    return rst.toString();
  }

  public List<OtObserveRecord> getLatestNLv1OT(int n) {
    Session session = getCurrentSession();
    String sql = "select oor.* "
            + "from ot_observe_record oor "
            + "inner join data_process_machine dpm on oor.dpm_id = dpm.dpm_id and oor.ff_number>dpm.cur_process_number-" + n + " "
            + "where oor.ot_id=0;";
    Query q = session.createSQLQuery(sql).addEntity(OtObserveRecord.class);
    return q.list();
  }

  @Override
  public Boolean exist(OtObserveRecord obj, float errorBox) {
    log.debug("************************");
    log.debug("errorBox=" + errorBox);
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select oort_id from ot_observe_record where ff_id="
            + obj.getFfId()
            //            + " and abs(x_temp-" + obj.getXTemp() + ")<" + errorBox + " "
            //            + " and abs(y_temp-" + obj.getYTemp() + ")<" + errorBox + " "
            + " and sqrt(power(x_temp-" + obj.getXTemp() + ", 2)+power(y_temp-" + obj.getYTemp() + ", 2))<" + errorBox + " ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger objId = (BigInteger) q.list().get(0);
      obj.setOorId(objId.longValue());
      flag = true;
    }
    return flag;
  }

  public List<OtObserveRecord> matchLatestN(OtObserveRecord obj, float errorBox, int n) {
    Session session = getCurrentSession();
    String sql = "select * from ot_observe_record "
            + " where ff_number>" + (obj.getFfNumber() - n)
            + " and ot_id=0"
            + " and dpm_id=" + obj.getDpmId()
            + " and sqrt(power(x_temp-" + obj.getXTemp() + ", 2)+power(y_temp-" + obj.getYTemp() + ", 2))<" + errorBox + " "
            + " order by ff_number asc";
    Query q = session.createSQLQuery(sql).addEntity(OtObserveRecord.class);
    return q.list();
  }

  public int countRecordByOtName(String otName) {

    int tNum = 0;
    Session session = getCurrentSession();
    String sql = "select count(*) "
            + "from ot_observe_record oor "
            + "where oor.ot_id=(select ob.ot_id from ot_level2 ob where name='" + otName + "') ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger objId = (BigInteger) q.list().get(0);
      tNum = objId.intValue();
    }
    return tNum;
  }

  public List<OtObserveRecordShow> getRecordByOtName(String otName, int start, int resultSize) {

    ArrayList<OtObserveRecordShow> oorss = new ArrayList<OtObserveRecordShow>();
    Session session = getCurrentSession();
    String sql = "select oor.*, ff.file_name ffname, ff.store_path ffpath, ffc.file_name ffcname, ffc.store_path ffcpath "
            + "from ot_observe_record oor "
            + "left join fits_file ff on oor.ff_id=ff.ff_id "
            + "left join fits_file_cut ffc on oor.ffc_id=ffc.ffc_id "
            + "where oor.ot_id=(select ob.ot_id from ot_level2 ob where name='" + otName + "') "
            + "order by oor.date_ut";
    Query q = session.createSQLQuery(sql);
    q.setFirstResult(start);
    q.setMaxResults(resultSize);
    Iterator itor = q.list().iterator();
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      try {
        BigInteger otID = (BigInteger) row[0];
        BigInteger ffId = (BigInteger) row[1];
        BigInteger ffcId = (BigInteger) row[2];
        BigInteger oortId = (BigInteger) row[3];
        Short otTypeId = (Short) row[4];
        float raD = (Float) row[5];
        float decD = (Float) row[6];
        float x = (Float) row[7];
        float y = (Float) row[8];
        float xTemp = (Float) row[9];
        float yTemp = (Float) row[10];
        Date dateUt = (Date) row[11];
        float flux = (Float) row[12];
        Boolean flag = (Boolean) row[13];
//        float flagChb = (Float) row[14];
        float background = (Float) row[15];
        float threshold = (Float) row[16];
        float magAper = (Float) row[17];
        float magerrAper = (Float) row[18];
        float ellipticity = (Float) row[19];
        float ClassStar = (Float) row[20];
        Boolean otFlag = (Boolean) row[21];
        //ff_number,dpm_id,date_str,request_cut,success_cut
        String ffFileName = (String) row[27];
        String ffStorePath = (String) row[28];
        String ffcFileName = (String) row[29];
        String ffcStorePath = (String) row[30];

        OtObserveRecordShow oors = new OtObserveRecordShow();
        oors.setBackground(background);
        oors.setClassStar(ClassStar);
        oors.setDateUt(dateUt);
        oors.setDecD(decD);
        oors.setEllipticity(ellipticity);
        oors.setFfId(ffId.longValue());
        oors.setFfName(ffFileName);
        oors.setFfPath(ffStorePath);
        oors.setFfcId(ffcId.longValue());
        oors.setFfcName(ffcFileName);
        oors.setFfcPath(ffcStorePath);
        oors.setFlag(flag);
//        oors.setFlagChb(flagChb);
        oors.setFlux(flux);
        oors.setMagAper(magAper);
        oors.setMagerrAper(magerrAper);
        oors.setOortId(oortId.longValue());
        oors.setOtFlag(otFlag);
        oors.setOtId(otID.longValue());
        oors.setRaD(raD);
        oors.setThreshold(threshold);
        oors.setX(x);
        oors.setXTemp(xTemp);
        oors.setY(y);
        oors.setYTemp(yTemp);
        oors.setOtTypeId(otTypeId);
        oorss.add(oors);
      } catch (ClassCastException cce) {
        cce.printStackTrace();
      }
    }
    return oorss;
  }

  public List<OtObserveRecordShow> getRecordByOtNameFromHis(String otName, int start, int resultSize) {

    ArrayList<OtObserveRecordShow> oorss = new ArrayList<OtObserveRecordShow>();
    Session session = getCurrentSession();
    String sql = "select oor.*, ff.file_name ffname, ff.store_path ffpath, ffc.file_name ffcname, ffc.store_path ffcpath "
            + "from ot_observe_record_his oor "
            + "left join fits_file ff on oor.ff_id=ff.ff_id "
            + "left join fits_file_cut_his ffc on oor.ffc_id=ffc.ffc_id "
            + "where oor.ot_id=(select ob.ot_id from ot_level2_his ob where name='" + otName + "') "
            + "order by oor.date_ut";
    Query q = session.createSQLQuery(sql);
    q.setFirstResult(start);
    q.setMaxResults(resultSize);
    Iterator itor = q.list().iterator();
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      try {
        BigInteger otID = (BigInteger) row[0];
        BigInteger ffId = (BigInteger) row[1];
        BigInteger ffcId = (BigInteger) row[2];
        BigInteger oortId = (BigInteger) row[3];
        Short otTypeId = (Short) row[4];
        float raD = (Float) row[5];
        float decD = (Float) row[6];
        float x = (Float) row[7];
        float y = (Float) row[8];
        float xTemp = (Float) row[9];
        float yTemp = (Float) row[10];
        Date dateUt = (Date) row[11];
        float flux = (Float) row[12];
        Boolean flag = (Boolean) row[13];
//        float flagChb = (Float) row[14];
        float background = (Float) row[15];
        float threshold = (Float) row[16];
        float magAper = (Float) row[17];
        float magerrAper = (Float) row[18];
        float ellipticity = (Float) row[19];
        float ClassStar = (Float) row[20];
        Boolean otFlag = (Boolean) row[21];
        //ff_number,dpm_id,date_str,request_cut,success_cut
        String ffFileName = (String) row[27];
        String ffStorePath = (String) row[28];
        String ffcFileName = (String) row[29];
        String ffcStorePath = (String) row[30];

        OtObserveRecordShow oors = new OtObserveRecordShow();
        oors.setBackground(background);
        oors.setClassStar(ClassStar);
        oors.setDateUt(dateUt);
        oors.setDecD(decD);
        oors.setEllipticity(ellipticity);
        oors.setFfId(ffId.longValue());
        oors.setFfName(ffFileName);
        oors.setFfPath(ffStorePath);
        oors.setFfcId(ffcId.longValue());
        oors.setFfcName(ffcFileName);
        oors.setFfcPath(ffcStorePath);
        oors.setFlag(flag);
//        oors.setFlagChb(flagChb);
        oors.setFlux(flux);
        oors.setMagAper(magAper);
        oors.setMagerrAper(magerrAper);
        oors.setOortId(oortId.longValue());
        oors.setOtFlag(otFlag);
        oors.setOtId(otID.longValue());
        oors.setRaD(raD);
        oors.setThreshold(threshold);
        oors.setX(x);
        oors.setXTemp(xTemp);
        oors.setY(y);
        oors.setYTemp(yTemp);
        oors.setOtTypeId(otTypeId);
        oorss.add(oors);
      } catch (ClassCastException cce) {
        cce.printStackTrace();
      }
    }
    return oorss;
  }

  public List<OtObserveRecordShow> getRecordByOtId(long otId) {

    ArrayList<OtObserveRecordShow> oorss = new ArrayList<OtObserveRecordShow>();
    Session session = getCurrentSession();
    String sql = "select oor.*, ff.file_name, ff.store_path, ffc.file_name, ffc.store_path "
            + "from ot_observe_record oor "
            + "left join fits_file ff on oor.ff_id=ff.ff_id "
            + "left join fits_file_cut ffc on oor.ffc_id=ffc.ffc_id "
            + "where ot_id='" + otId + "'";
    Iterator itor = session.createSQLQuery(sql).list().iterator();
    while (itor.hasNext()) {
      Object[] row = (Object[]) itor.next();
      try {
        BigInteger otID = (BigInteger) row[0];
        BigInteger ffId = (BigInteger) row[1];
        BigInteger ffcId = (BigInteger) row[2];
        BigInteger oortId = (BigInteger) row[3];
        Short otTypeId = (Short) row[4];
        float raD = (Float) row[5];
        float decD = (Float) row[6];
        float x = (Float) row[7];
        float y = (Float) row[8];
        float xTemp = (Float) row[9];
        float yTemp = (Float) row[10];
        Date dateUt = (Date) row[11];
        float flux = (Float) row[12];
        Boolean flag = (Boolean) row[13];
//        float flagChb = (Float) row[14];
        float background = (Float) row[15];
        float threshold = (Float) row[16];
        float magAper = (Float) row[17];
        float magerrAper = (Float) row[18];
        float ellipticity = (Float) row[19];
        float ClassStar = (Float) row[20];
        Boolean otFlag = (Boolean) row[21];
        //ff_number,dpm_id,date_str,request_cut,success_cut
        String ffFileName = (String) row[27];
        String ffStorePath = (String) row[28];
        String ffcFileName = (String) row[29];
        String ffcStorePath = (String) row[30];

        OtObserveRecordShow oors = new OtObserveRecordShow();
        oors.setBackground(background);
        oors.setClassStar(ClassStar);
        oors.setDateUt(dateUt);
        oors.setDecD(decD);
        oors.setEllipticity(ellipticity);
        oors.setFfId(ffId.longValue());
        oors.setFfName(ffFileName);
        oors.setFfPath(ffStorePath);
        oors.setFfcId(ffcId.longValue());
        oors.setFfcName(ffcFileName);
        oors.setFfcPath(ffcStorePath);
        oors.setFlag(flag);
//        oors.setFlagChb(flagChb);
        oors.setFlux(flux);
        oors.setMagAper(magAper);
        oors.setMagerrAper(magerrAper);
        oors.setOortId(oortId.longValue());
        oors.setOtFlag(otFlag);
        oors.setOtId(otId);
        oors.setRaD(raD);
        oors.setThreshold(threshold);
        oors.setX(x);
        oors.setXTemp(xTemp);
        oors.setY(y);
        oors.setYTemp(yTemp);
        oors.setOtTypeId(otTypeId);
        oorss.add(oors);
      } catch (ClassCastException cce) {
        cce.printStackTrace();
      }
    }
    return oorss;
  }
}
