/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.gwac.action;

import com.gwac.dao.DataProcessMachineDAO;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

import com.gwac.model.DataProcessMachine;
import com.opensymphony.xwork2.ActionSupport;

@Actions({
  @Action(value = "/edit-dpm-entry", results = {
    @Result(location = "simpleecho.jsp", name = "success"),
    @Result(location = "simpleecho.jsp", name = "input")})})
public class EditDPMEntry extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = -3454448309088641394L;
  private static final Log log = LogFactory.getLog(EditDPMEntry.class);
  private String oper = "";
  private String id;
  private String name;
  private String ip;
  private String telscope;
  private List<DataProcessMachine> mchList;
  private DataProcessMachineDAO dpmDao;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
    log.debug("id :" + getId());
    log.debug("name :" + name);
    log.debug("ip :" + getIp());
    log.debug("telscope :" + getTelscope());

    DataProcessMachine dpm = new DataProcessMachine();
    dpm.setName(name);
    dpm.setIp(ip);

    if (oper.equalsIgnoreCase("add")) {
      log.debug("Add DataProcessMachine");
      dpmDao.save(dpm);
    } else if (oper.equalsIgnoreCase("edit")) {
      log.debug("Edit DataProcessMachine");
      dpm.setDpmId(Short.parseShort(getId()));
      dpmDao.update(dpm);
    } else if (oper.equalsIgnoreCase("del")) {
      StringTokenizer ids = new StringTokenizer(getId(), ",");
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
   * @return the telscope
   */
  public String getTelscope() {
    return telscope;
  }

  /**
   * @param telscope the telscope to set
   */
  public void setTelscope(String telscope) {
    this.telscope = telscope;
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
  public DataProcessMachineDAO getDpmDao() {
    return dpmDao;
  }

  /**
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  public void setSession(Map<String, Object> map) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
