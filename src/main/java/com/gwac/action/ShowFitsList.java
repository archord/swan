package com.gwac.action;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.OtLevel2;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/show-fits-list", results = {
    @Result(location = "gwac/pgwac-ot-fits.jsp", name = "success"),
    @Result(location = "gwac/pgwac-ot-fits.jsp", name = "input")})})
public class ShowFitsList extends ActionSupport {

  private static final long serialVersionUID = -3454448234588657934L;
  private static final Log log = LogFactory.getLog(ShowFitsList.class);

  private FitsFileCutDAO ffcDao;
  private FitsFileCutRefDAO ffcrDao;
  private OtLevel2Dao obDao;
  /**
   * 查询条件
   */
  private String otName;
  private Boolean queryHis;
  /**
   * 返回结果
   */
  private OtLevel2 ob;
  private List<FitsFileCut> ffcList;
  private String ffcListStr;
  private String ffcPath;
  private int totalImage;
  private int startImgNum;
  private String ffcrStorePath;
  private String ffcrName;
  private String ffcrGenerateTime;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    String dataRootWebMap = getText("gwac.data.root.directory.webmap");

    if (queryHis == null) {
      queryHis = false;
    }

    ob = obDao.getOtLevel2ByName(otName, queryHis);

    if (ob != null) {
      startImgNum = ob.getFirstFfNumber();

      ffcList = ffcDao.getCutImageByOtId(ob.getOtId(), queryHis);
      totalImage = ffcList.size();
      
      if (totalImage > 0) {
        FitsFileCut tffc = ffcList.get(0);
        ffcPath = dataRootWebMap + "/" + tffc.getStorePath() + "/";
      }else{
        ffcPath = "";
      }
      
      StringBuilder tsb = new StringBuilder("");
      for (FitsFileCut ffc : ffcList) {
//        tsb.append(dataRootWebMap);
//        tsb.append("/");
//        tsb.append(ffc.getStorePath());
//        tsb.append("/");
        tsb.append(ffc.getFileName());
        tsb.append(".fit,");
      }
      ffcListStr = tsb.toString();

      List<FitsFileCutRef> ffcrs = ffcrDao.getCutImageByOtId(ob.getOtId());
      if (ffcrs != null && ffcrs.size() > 0) {
        ffcrStorePath = dataRootWebMap + "/" + ffcrs.get(0).getStorePath() + "/";
        ffcrName = ffcrs.get(0).getFileName() + ".fit";
        ffcrGenerateTime = CommonFunction.getDateTimeString(ffcrs.get(0).getGenerateTime(), "yyyy-MM-dd HH:mm:ss") + "(U)";
      } else {
        ffcrStorePath = "";
        ffcrGenerateTime = "";
      }

    } else {
      startImgNum = 0;
      ffcList = new ArrayList();
      totalImage = 0;
      ffcrStorePath = "";
      ffcrGenerateTime = "";
    }

    return SUCCESS;
  }

  /**
   * @return the ffcList
   */
  public List<FitsFileCut> getFfcList() {
    return ffcList;
  }

  /**
   * @param ffcList the ffcList to set
   */
  public void setFfcList(List<FitsFileCut> ffcList) {
    this.ffcList = ffcList;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @return the otName
   */
  public String getOtName() {
    return otName;
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

  /**
   * @return the totalImage
   */
  public int getTotalImage() {
    return totalImage;
  }

  /**
   * @param totalImage the totalImage to set
   */
  public void setTotalImage(int totalImage) {
    this.totalImage = totalImage;
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtLevel2Dao obDao) {
    this.obDao = obDao;
  }

  /**
   * @return the startImgNum
   */
  public int getStartImgNum() {
    return startImgNum;
  }

  /**
   * @param startImgNum the startImgNum to set
   */
  public void setStartImgNum(int startImgNum) {
    this.startImgNum = startImgNum;
  }

  /**
   * @return the ffcrDao
   */
  public FitsFileCutRefDAO getFfcrDao() {
    return ffcrDao;
  }

  /**
   * @param ffcrDao the ffcrDao to set
   */
  public void setFfcrDao(FitsFileCutRefDAO ffcrDao) {
    this.ffcrDao = ffcrDao;
  }

  /**
   * @return the ffcrStorePath
   */
  public String getFfcrStorePath() {
    return ffcrStorePath;
  }

  /**
   * @param ffcrStorePath the ffcrStorePath to set
   */
  public void setFfcrStorePath(String ffcrStorePath) {
    this.ffcrStorePath = ffcrStorePath;
  }

  /**
   * @return the ffcrGenerateTime
   */
  public String getFfcrGenerateTime() {
    return ffcrGenerateTime;
  }

  /**
   * @param ffcrGenerateTime the ffcrGenerateTime to set
   */
  public void setFfcrGenerateTime(String ffcrGenerateTime) {
    this.ffcrGenerateTime = ffcrGenerateTime;
  }

  /**
   * @return the queryHis
   */
  public Boolean getQueryHis() {
    return queryHis;
  }

  /**
   * @param queryHis the queryHis to set
   */
  public void setQueryHis(Boolean queryHis) {
    this.queryHis = queryHis;
  }

  /**
   * @return the ob
   */
  public OtLevel2 getOb() {
    return ob;
  }

  /**
   * @param ob the ob to set
   */
  public void setOb(OtLevel2 ob) {
    this.ob = ob;
  }

  /**
   * @return the ffcListStr
   */
  public String getFfcListStr() {
    return ffcListStr;
  }

  /**
   * @param ffcListStr the ffcListStr to set
   */
  public void setFfcListStr(String ffcListStr) {
    this.ffcListStr = ffcListStr;
  }

  /**
   * @return the ffcPath
   */
  public String getFfcPath() {
    return ffcPath;
  }

  /**
   * @param ffcPath the ffcPath to set
   */
  public void setFfcPath(String ffcPath) {
    this.ffcPath = ffcPath;
  }

  /**
   * @return the ffcrName
   */
  public String getFfcrName() {
    return ffcrName;
  }

  /**
   * @param ffcrName the ffcrName to set
   */
  public void setFfcrName(String ffcrName) {
    this.ffcrName = ffcrName;
  }

}
