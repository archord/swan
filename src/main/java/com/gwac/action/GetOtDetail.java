package com.gwac.action;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.OtLevel2;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-ot-detail", results = {
    @Result(location = "gwac/pgwac-ot-detail.jsp", name = "success"),
    @Result(location = "gwac/pgwac-ot-detail.jsp", name = "input")})})
public class GetOtDetail extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetOtDetail.class);

  private FitsFileCutDAO ffcDao;
  private FitsFileCutRefDAO ffcrDao;
  private OtLevel2Dao obDao;
  private OtObserveRecordDAO otorDao;
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
  private int totalImage;
  private int startImgNum;
  private String ra;
  private String dec;
  private String ffcrStorePath;
  private String ffcrFileName;
  private String ffcrGenerateTime;
  private String otOpticalVaration;
  private String otPositionVaration;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
    String dataRoot = getText("gwac.data.root.directory");
    String dataRootWebMap = getText("gwac.data.root.directory.webmap");
    
    if(queryHis==null){
      queryHis = false;
    }

    ob = obDao.getOtLevel2ByName(otName, queryHis);
    ffcList = ffcDao.getCutImageByOtId(ob.getOtId(), queryHis);

    if (ob != null) {
      setRa(ob.getRa() + "");
      setDec(ob.getDec() + "");
      setStartImgNum(ob.getFirstFfNumber());
    } else {
      setRa("");
      setDec("");
      setStartImgNum(0);
    }

    totalImage = ffcList.size();
    for (FitsFileCut ffc : ffcList) {
      ffc.setFileName(ffc.getFileName() + ".jpg");
      ffc.setStorePath(dataRootWebMap + "/" + ffc.getStorePath());
    }

    List<FitsFileCutRef> ffcrs = ffcrDao.getCutImageByOtId(ob.getOtId());
    if (ffcrs != null && ffcrs.size() > 0) {
      setFfcrStorePath(dataRootWebMap + "/" + ffcrs.get(0).getStorePath() + "/");
      setFfcrFileName(ffcrs.get(0).getFileName() + ".jpg");
      setFfcrGenerateTime(CommonFunction.getDateTimeString(ffcrs.get(0).getGenerateTime(), "yyyy-MM-dd HH:mm:ss") + "(U)");
    } else {
      setFfcrStorePath("");
      setFfcrFileName("");
      setFfcrGenerateTime("");
    }

    String tmp[] = otorDao.getOtOpticalVaration(ob, queryHis).split("=");
    otOpticalVaration = tmp[0];
    otPositionVaration = tmp[1];

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
   * @return the ra
   */
  public String getRa() {
    return ra;
  }

  /**
   * @param ra the ra to set
   */
  public void setRa(String ra) {
    this.ra = ra;
  }

  /**
   * @return the dec
   */
  public String getDec() {
    return dec;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(String dec) {
    this.dec = dec;
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
   * @return the ffcrFileName
   */
  public String getFfcrFileName() {
    return ffcrFileName;
  }

  /**
   * @param ffcrFileName the ffcrFileName to set
   */
  public void setFfcrFileName(String ffcrFileName) {
    this.ffcrFileName = ffcrFileName;
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
   * @return the otOpticalVaration
   */
  public String getOtOpticalVaration() {
    return otOpticalVaration;
  }

  /**
   * @param otOpticalVaration the otOpticalVaration to set
   */
  public void setOtOpticalVaration(String otOpticalVaration) {
    this.otOpticalVaration = otOpticalVaration;
  }

  /**
   * @param otorDao the otorDao to set
   */
  public void setOtorDao(OtObserveRecordDAO otorDao) {
    this.otorDao = otorDao;
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
   * @return the otPositionVaration
   */
  public String getOtPositionVaration() {
    return otPositionVaration;
  }

}
