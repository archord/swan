package com.gwac.action;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-ot-image-list", results = {
    @Result(location = "gwac/pgwac-ot-detail.jsp", name = "success"),
    @Result(location = "gwac/pgwac-ot-detail.jsp", name = "input")})})
public class GetOtImageList extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetOtImageList.class);
  private String otName;
  private List<FitsFileCut> ffcList;
  private FitsFileCutDAO ffcDao;
  private OtLevel2Dao obDao;
  private int totalImage;
  private int startImgNum;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
    String dataRoot = getText("gwac.data.root.directory");
    String dataRootWebMap = getText("gwac.data.root.directory.webmap");
    ffcList = ffcDao.getCutImageByOtName(getOtName());
    OtLevel2 ob = obDao.getOtLevel2ByName(otName);
    setStartImgNum(ob.getLastFfNumber());
    totalImage = ffcList.size();
    for (FitsFileCut ffc : ffcList) {
      ffc.setFileName(ffc.getFileName()+".jpg");
      ffc.setStorePath(dataRootWebMap + "/" + ffc.getStorePath());
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
}
