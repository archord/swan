/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.MoveObjectRecordDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.MoveObject;
import com.gwac.model.MoveObjectRecord;
import com.gwac.model.OtObserveRecord;
import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class SaveMovObjAction extends ActionSupport {

  private static final Log log = LogFactory.getLog(SaveMovObjAction.class);

  private File fileUpload;

  @Resource
  private MoveObjectDao moDao;
  @Resource
  private MoveObjectRecordDao morDao;
  @Resource
  private OtObserveRecordDAO otorDao;

  private String echo = "";

  @Action(value = "saveMovObj")
  public void upload() {

    echo = "success";
    if (fileUpload == null) {
      echo = "must uplod one file.";
      log.warn(echo);
    } else {

      BufferedReader br = null;
      String line;
      String splitBy = " +";  //正则表达式一到多个空格
      try {
	br = new BufferedReader(new FileReader(fileUpload));
	while ((line = br.readLine()) != null) {
	  if (line.charAt(0) == '#') {
	    continue;
	  }
	  String[] strs = line.split(splitBy);
	  Integer todayMovId = Integer.parseInt(strs[0]);
	  Long oorId = Long.parseLong(strs[1]);
	  OtObserveRecord oor = otorDao.getById(oorId);
	  MoveObject mo0 = moDao.getMovObj(oor.getDateStr(), todayMovId);
	  if (mo0 == null) {
	    MoveObject mo = new MoveObject();
	    mo.setTodayMovId(todayMovId);
	    mo.setDateStr(oor.getDateStr());
	    mo.setDpmId(oor.getDpmId());
	    mo.setSkyId(oor.getSkyId().intValue());
	    mo.setFirstFrameNum(oor.getFfNumber());
	    mo.setFirstFrameTime(oor.getDateUt());
	    mo.setPointNumber(1);
	    mo.setLastFrameNum(oor.getFfNumber());
	    mo.setLastFrameTime(oor.getDateUt());
	    mo.setTotalFrameNumber(1);
	    mo.setAvgFramePointNumber((float)1.0);
	    mo.setMovType('1');
	    moDao.save(mo);
	    mo0=mo;
	  } else {
	    mo0.setPointNumber(mo0.getPointNumber()+1);
	    mo0.setLastFrameNum(oor.getFfNumber());
	    mo0.setLastFrameTime(oor.getDateUt());
	    mo0.setTotalFrameNumber(mo0.getTotalFrameNumber()+1);
	    moDao.update(mo0);
	  }
	  MoveObjectRecord mor = new MoveObjectRecord();
	  mor.setMovId(mo0.getMovId());
	  mor.setOorId(oor.getOorId());
	  morDao.save(mor);
	}
      } catch (Exception e) {
	echo = "save move obj error.";
	log.error(echo, e);
      } finally {
	if (br != null) {
	  try {
	    br.close();
	  } catch (IOException e) {
	    log.error(e);
	  }
	}
      }
    }

    sendResultMsg(echo);
  }

  public void sendResultMsg(String msg) {

    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out;
    try {
      out = response.getWriter();
      out.print(msg);
    } catch (IOException ex) {
      log.error("response error: ", ex);
    }
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param fileUpload the fileUpload to set
   */
  public void setFileUpload(File fileUpload) {
    this.fileUpload = fileUpload;
  }

}
