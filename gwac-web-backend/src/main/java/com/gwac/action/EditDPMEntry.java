package com.gwac.action;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.model.DataProcessMachine;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({ 
  @Action(value = "/edit-dpm-entry", results = {
    @Result(location = "simpleecho.jsp", name = "success"),
    @Result(location = "simpleecho.jsp", name = "input")})})
public class EditDPMEntry extends ActionSupport {

  private static final long serialVersionUID = -3454448309088641394L;
  private static final Log log = LogFactory.getLog(EditDPMEntry.class);
  private String oper = "";
  private String dpmId;
  private String name;
  private String ip;
  private short tspId;
  private List<DataProcessMachine> mchList;
  @Resource
  private DataProcessMachineDAO dpmDao;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
    log.info("id :" + dpmId);
    log.info("name :" + name);
    log.info("ip :" + getIp());
    log.info("telscope :" + getTspId());

    DataProcessMachine dpm = new DataProcessMachine();
    dpm.setName(name);
    dpm.setIp(ip);
    dpm.setTspId(getTspId());

    if (oper.equalsIgnoreCase("add")) {
      log.debug("Add DataProcessMachine");
      dpmDao.save(dpm);
    } else if (oper.equalsIgnoreCase("edit")) {
      log.debug("Edit DataProcessMachine");
      dpm.setDpmId(Short.parseShort(dpmId));
      dpmDao.update(dpm);
    } else if (oper.equalsIgnoreCase("del")) {
      StringTokenizer ids = new StringTokenizer(dpmId, ",");
      while (ids.hasMoreTokens()) {
        int removeId = Integer.parseInt(ids.nextToken());
        log.debug("Delete DataProcessMachine " + removeId);
        dpmDao.deleteById((long)removeId);
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
   * @return the ip
   */
  public String getIp() {
    return ip;
  }

  /**
   * @param ip the ip to set
   */
  public void setIp(String ip) {
    this.ip = ip;
  }

  /**
   * @return the mchList
   */
  public List<DataProcessMachine> getMchList() {
    return mchList;
  }

  /**
   * @param mchList the mchList to set
   */
  public void setMchList(List<DataProcessMachine> mchList) {
    this.mchList = mchList;
  }

  /**
   * @return the dpmDao
   */
//  public DataProcessMachineDAO getDpmDao() {
//    return dpmDao;
//  }

  /**
   * @return the tspId
   */
  public short getTspId() {
    return tspId;
  }

  /**
   * @param tspId the tspId to set
   */
  public void setTspId(short tspId) {
    this.tspId = tspId;
  }

  /**
   * @return the dpmId
   */
  public String getDpmId() {
    return dpmId;
  }

  /**
   * @param dpmId the dpmId to set
   */
  public void setDpmId(String dpmId) {
    this.dpmId = dpmId;
  }

}
