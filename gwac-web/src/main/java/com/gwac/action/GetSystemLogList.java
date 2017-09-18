package com.gwac.action;

import com.gwac.dao.SystemLogDao;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Result;

/**
 * 对OT2列表分页显示
 *
 * @author xy
 */
@Result(name = "success", type = "json")
public class GetSystemLogList extends ActionSupport {

  private static final long serialVersionUID = 5073694279068591293L;
  private static final Log log = LogFactory.getLog(GetSystemLogList.class);

  /**
   * search condition
   */
  private int draw;
  private int start;
  private int length;
  private String dateStart;
  private String dateEnd;
  private String logCode;
  private String msgIp;

  /**
   * search result
   */
  private String dataStr;
  private int recordsTotal;
  private int recordsFiltered;

  @Resource
  private SystemLogDao dao = null;

  @SuppressWarnings("unchecked")
//  @Transactional(readOnly=true)
  public String execute() {
    
//    log.debug("draw="+draw);
//    log.debug("start="+start);
//    log.debug("length="+length);
//    log.debug("dateStart="+dateStart);
//    log.debug("dateEnd="+dateEnd);
//    log.debug("logCode="+logCode);
//    log.debug("msgIp="+msgIp);

    dataStr = dao.findRecord(start, length, dateStart, dateEnd, logCode, msgIp);
    if (dataStr == null) {
      dataStr = "[]";
    }
    recordsTotal = dao.count().intValue();
    recordsFiltered = recordsTotal;

    return SUCCESS;
  }

  /**
   * @return the draw
   */
  public int getDraw() {
    return draw;
  }

  /**
   * @param draw the draw to set
   */
  public void setDraw(int draw) {
    this.draw = draw;
  }

  /**
   * @return the recordsTotal
   */
  public int getRecordsTotal() {
    return recordsTotal;
  }

  /**
   * @return the recordsFiltered
   */
  public int getRecordsFiltered() {
    return recordsFiltered;
  }

  /**
   * @param start the start to set
   */
  public void setStart(int start) {
    this.start = start;
  }

  /**
   * @param length the length to set
   */
  public void setLength(int length) {
    this.length = length;
  }

  /**
   * @return the dataStr
   */
  public String getDataStr() {
    return dataStr;
  }

  /**
   * @param dateStart the dateStart to set
   */
  public void setDateStart(String dateStart) {
    this.dateStart = dateStart;
  }

  /**
   * @param dateEnd the dateEnd to set
   */
  public void setDateEnd(String dateEnd) {
    this.dateEnd = dateEnd;
  }

  /**
   * @param logCode the logCode to set
   */
  public void setLogCode(String logCode) {
    this.logCode = logCode;
  }

  /**
   * @param msgIp the msgIp to set
   */
  public void setMsgIp(String msgIp) {
    this.msgIp = msgIp;
  }

}
