package com.gwac.action;

import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpRecordDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.FollowUpObject;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpRecord;
import com.gwac.model.OtLevel2;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetOtFollowupObjectMagAndPosition extends ActionSupport {

  private static final long serialVersionUID = -3454448234482641394L;
  private static final Log log = LogFactory.getLog(GetOtFollowupObjectMagAndPosition.class);

  private String otName;
  private Boolean queryHis;

  private OtLevel2Dao obDao;
  private FollowUpObjectDao fuoDao;
  private FollowUpRecordDao furDao;

  private OtLevel2 ot2;
  private FollowUpObject fuCheckObj;
  private List<Map<String, String>> mags;
  private List<Map<String, String>> poss;

  @Actions({
    @Action(value = "/get-ot-followup-object-magpos", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    List<Integer> tlist = obDao.hisOrCurExist(otName);
    if (!tlist.isEmpty()) {

      Integer his = tlist.get(0);
      queryHis = his == 1;
      ot2 = obDao.getOtLevel2ByName(otName, queryHis);

      mags = new ArrayList();
      poss = new ArrayList();

      Calendar cal = Calendar.getInstance();
      Date baseDate = null;
      List<FollowUpObject> objs = fuoDao.getByOtId(ot2.getOtId(), queryHis);
      if (objs.size() > 0) {
        baseDate = objs.get(0).getStartTimeUtc();
        for (FollowUpObject obj : objs) {
          if (baseDate.before(obj.getStartTimeUtc())) {
            baseDate = obj.getStartTimeUtc();
          }
        }
        cal.setTime(baseDate);
      }
      double baseDay = cal.getTimeInMillis() / 60000.0;

      int maxObjs = 6;
      int i = 1;
      //对有多个目标时，大部分情况是数据处理流程出错，值显示前6个目标
      for (FollowUpObject obj : objs) {
        if (i++ > maxObjs) {
          break;
        }
        if (obj.getFuoName().contains("CHECK")) {
          fuCheckObj = obj;
        }
//        if(obj.getRecordTotal()<2){
//          continue;
//        }

        List<FollowUpRecord> furs = furDao.getByFuoId(obj.getFuoId(), queryHis);
        Map<String, String> tmags = new HashMap();
        String fuoName = obj.getFuoName();
        StringBuilder magSb = new StringBuilder();
        magSb.append("[");
        for (FollowUpRecord fur : furs) {
          cal.setTime(fur.getDateUtc());
          double now = cal.getTimeInMillis() / 60000.0;
          magSb.append("[");
          magSb.append(now - baseDay);
          magSb.append(",");
          magSb.append(fur.getMagCalUsno());
          magSb.append("],");
        }
        magSb.append("]");
        tmags.put("objName", fuoName);
        tmags.put("objMag", magSb.toString());
        getMags().add(tmags);
      }

      //统计目标中非参考星CHECK的个数
      int targetObjNum = 0;
      for (FollowUpObject obj : objs) {
        if (!obj.getFuoName().contains("CHECK")) {
          if (++targetObjNum > 1) {
            break;
          }
        }
      }
      //如果非参考星个数为1，则计算该目标的位置变化，并最终在页面上显示
      if (targetObjNum == 1) {
        for (FollowUpObject obj : objs) {
          if (obj.getFuoName().contains("CHECK")) {
            continue;
          }
          List<FollowUpRecord> furs = furDao.getByFuoId(obj.getFuoId(), queryHis);
          String fuoName = obj.getFuoName();
          int j = 0;
          float x0 = 0, y0 = 0;
          Map<String, String> tposs = new HashMap();
          StringBuilder posSb = new StringBuilder();
          posSb.append("[");
          for (FollowUpRecord fur : furs) {
            if (j++ == 0) {
              x0 = fur.getX();
              y0 = fur.getY();
            }
            posSb.append("[");
            posSb.append(fur.getX() - x0);
            posSb.append(",");
            posSb.append(fur.getY() - y0);
            posSb.append("],");
          }
          posSb.append("]");
          tposs.put("objName", fuoName);
          tposs.put("objPos", posSb.toString());
          getPoss().add(tposs);
        }
      }
    } else {
      mags = new ArrayList();
      poss = new ArrayList();
    }

    return "json";
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

  /**
   * @param queryHis the queryHis to set
   */
  public void setQueryHis(Boolean queryHis) {
    this.queryHis = queryHis;
  }

  /**
   * @param fuoDao the fuoDao to set
   */
  public void setFuoDao(FollowUpObjectDao fuoDao) {
    this.fuoDao = fuoDao;
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtLevel2Dao obDao) {
    this.obDao = obDao;
  }

  /**
   * @return the ot2
   */
  public OtLevel2 getOt2() {
    return ot2;
  }

  /**
   * @param furDao the furDao to set
   */
  public void setFurDao(FollowUpRecordDao furDao) {
    this.furDao = furDao;
  }

  /**
   * @return the mags
   */
  public List<Map<String, String>> getMags() {
    return mags;
  }

  /**
   * @return the poss
   */
  public List<Map<String, String>> getPoss() {
    return poss;
  }

  /**
   * @return the fuCheckObj
   */
  public FollowUpObject getFuCheckObj() {
    return fuCheckObj;
  }

}
