package com.gwac.action;

import com.gwac.dao.TelescopeDAO;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

import com.gwac.model.Telescope;
import com.opensymphony.xwork2.ActionSupport;

@Actions({
  @Action(value = "/edit-telescope-entry", results = {
    @Result(location = "simpleecho.jsp", name = "success"),
    @Result(location = "simpleecho.jsp", name = "input")})})
public class EditTelescopeEntry extends ActionSupport {

  private static final long serialVersionUID = -3454448309088641394L;
  private static final Log log = LogFactory.getLog(EditTelescopeEntry.class);
  private String oper = "";
  private String tspId;
  private String name;
  private float ra;
  private float dec;
  private Short diameter;
  private Float focalRatio;
  private String ccdType;
  private List<Telescope> objList;
  private TelescopeDAO tspDao;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
    log.info("id :" + getTspId());
    log.info("name :" + name);
    log.info("ra :" + ra);
    log.info("dec :" + dec);
    log.info("diameter :" + diameter);
    log.info("focalRatio :" + focalRatio);
    log.info("ccdType :" + ccdType);

    Telescope obj = new Telescope();
    obj.setName(name);
    obj.setRa(ra);
    obj.setDec(dec);
    obj.setDiameter(diameter);
    obj.setFocalRatio(focalRatio);
    obj.setCcdType(ccdType);

    if (oper.equalsIgnoreCase("add")) {
      log.debug("Add DataProcessMachine");
      tspDao.save(obj);
    } else if (oper.equalsIgnoreCase("edit")) {
      log.debug("Edit DataProcessMachine");
      obj.setTspId(Short.parseShort(getTspId()));
      tspDao.update(obj);
    } else if (oper.equalsIgnoreCase("del")) {
      StringTokenizer ids = new StringTokenizer(getTspId(), ",");
      while (ids.hasMoreTokens()) {
	int removeId = Integer.parseInt(ids.nextToken());
	log.debug("Delete DataProcessMachine " + removeId);
	tspDao.deleteById((long) removeId);
      }
    }

    return SUCCESS;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOper(String oper) {
    this.oper = oper;
  }

  /**
   * @return the ra
   */
  public float getRa() {
    return ra;
  }

  /**
   * @param ra the ra to set
   */
  public void setRa(float ra) {
    this.ra = ra;
  }

  /**
   * @return the dec
   */
  public float getDec() {
    return dec;
  }

  /**
   * @param dec the dec to set
   */
  public void setDec(float dec) {
    this.dec = dec;
  }

  /**
   * @return the diameter
   */
  public Short getDiameter() {
    return diameter;
  }

  /**
   * @param diameter the diameter to set
   */
  public void setDiameter(Short diameter) {
    this.diameter = diameter;
  }

  /**
   * @return the focalRatio
   */
  public Float getFocalRatio() {
    return focalRatio;
  }

  /**
   * @param focalRatio the focalRatio to set
   */
  public void setFocalRatio(Float focalRatio) {
    this.focalRatio = focalRatio;
  }

  /**
   * @return the ccdType
   */
  public String getCcdType() {
    return ccdType;
  }

  /**
   * @param ccdType the ccdType to set
   */
  public void setCcdType(String ccdType) {
    this.ccdType = ccdType;
  }

  /**
   * @return the objList
   */
  public List<Telescope> getObjList() {
    return objList;
  }

  /**
   * @param objList the objList to set
   */
  public void setObjList(List<Telescope> objList) {
    this.objList = objList;
  }

  /**
   * @param tspDao the tspDao to set
   */
  public void setTspDao(TelescopeDAO tspDao) {
    this.tspDao = tspDao;
  }

  /**
   * @return the tspId
   */
  public String getTspId() {
    return tspId;
  }

  /**
   * @param tspId the tspId to set
   */
  public void setTspId(String tspId) {
    this.tspId = tspId;
  }

}
