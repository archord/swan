package com.gwac.action;

import com.gwac.dao.ScienceObjectFinalDao;
import com.gwac.model.ScienceObjectFinal;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * 目前已经被GetOtLevel2List取代
 *
 * @author xy
 */
@Action(value = "saveSciObjFinal", results = {
  @Result(name = "success", type = "json")})
public class SaveScienceObjectFinal extends ActionSupport {

  private static final long serialVersionUID = 1437264279538549317L;
  private static final Log log = LogFactory.getLog(SaveScienceObjectFinal.class);

  private long sofId;
  private String name;
  private String discoveryTime;
  private String triggerTime;
  private String ra;
  private String dec;
  private String magDetect;
  private String magCatalog;
  private String magAbsolute;
  private String amplitude;
  private String dutyScientist;
  private String followup;
  private String type;
  private String comments;
  private String source;
  private String publicMsg;
  private Short gwacType;
  private String result = "";

  @Resource
  private ScienceObjectFinalDao sciObjFinalDao;

  @SuppressWarnings("unchecked")
  public String execute() {
    if (name != null && !name.isEmpty()) {
      ScienceObjectFinal sciObj = new ScienceObjectFinal();
      if(sofId!=0){
	sciObj.setSofId(sofId);
      }
      sciObj.setName(name);
      sciObj.setDiscoveryTime(discoveryTime);
      sciObj.setTriggerTime(triggerTime);
      sciObj.setRa(ra);
      sciObj.setDec(dec);
      sciObj.setMagDetect(magDetect);
      sciObj.setMagCatalog(magCatalog);
      sciObj.setMagAbsolute(magAbsolute);
      sciObj.setAmplitude(amplitude);
      sciObj.setDutyScientist(dutyScientist);
      sciObj.setFollowup(followup);
      sciObj.setType(type);
      sciObj.setComments(comments);
      sciObj.setSource(source);
      sciObj.setPublicMsg(publicMsg);
      sciObj.setGwacType(gwacType);
      if(sofId!=0){
      sciObjFinalDao.update(sciObj);
      }else{
      sciObjFinalDao.save(sciObj);
      }
      result = "success.";
    } else {
      result = "parameter error.";
    }

    return SUCCESS;
  }

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }


  /**
   * @param sofId the sofId to set
   */
  public void setSofId(long sofId) {
    this.sofId = sofId;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @param discoveryTime the discoveryTime to set
   */
  public void setDiscoveryTime(String discoveryTime) {
    this.discoveryTime = discoveryTime;
  }

  /**
   * @param triggerTime the triggerTime to set
   */
  public void setTriggerTime(String triggerTime) {
    this.triggerTime = triggerTime;
  }

  /**
   * @param ra the ra to set
   */
  public void setRa(String ra) {
    this.ra = ra;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(String dec) {
    this.dec = dec;
  }

  /**
   * @param magDetect the magDetect to set
   */
  public void setMagDetect(String magDetect) {
    this.magDetect = magDetect;
  }

  /**
   * @param magCatalog the magCatalog to set
   */
  public void setMagCatalog(String magCatalog) {
    this.magCatalog = magCatalog;
  }

  /**
   * @param magAbsolute the magAbsolute to set
   */
  public void setMagAbsolute(String magAbsolute) {
    this.magAbsolute = magAbsolute;
  }

  /**
   * @param amplitude the amplitude to set
   */
  public void setAmplitude(String amplitude) {
    this.amplitude = amplitude;
  }

  /**
   * @param dutyScientist the dutyScientist to set
   */
  public void setDutyScientist(String dutyScientist) {
    this.dutyScientist = dutyScientist;
  }

  /**
   * @param followup the followup to set
   */
  public void setFollowup(String followup) {
    this.followup = followup;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(String comments) {
    this.comments = comments;
  }

  /**
   * @param source the source to set
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * @param publicMsg the publicMsg to set
   */
  public void setPublicMsg(String publicMsg) {
    this.publicMsg = publicMsg;
  }

  /**
   * @param gwacType the gwacType to set
   */
  public void setGwacType(Short gwacType) {
    this.gwacType = gwacType;
  }
}
