package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.OtLevel2;
import com.gwac.model4.DataTableColumn;
import com.gwac.model4.DataTableOrder;
import com.gwac.model4.DataTableSearch;
import com.gwac.model4.OtLevel2QueryParameter;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Result;

/**
 * 对OT2列表分页显示
 * @author xy
 */
@Result(name = "success", type = "json")
public class GetOtLevel2List4 extends ActionSupport {

  private static final long serialVersionUID = 5073694279068576893L;
  private static final Log log = LogFactory.getLog(GetOtLevel2List4.class);

  /**
   * search condition
   */
  private int draw;

  /**
   * search result
   */
  private List<OtLevel2> gridModel = new ArrayList();
  private int recordsTotal;
  private int recordsFiltered;

  @Resource
  private OtLevel2Dao obDao = null;
  private OtLevel2QueryParameter ot2qp;

  @SuppressWarnings("unchecked")
//  @Transactional(readOnly=true)
  public String execute() {

    log.debug(draw);
    log.debug(ot2qp.toString());

//    log.debug("get ot level2 list");
    ot2qp.setQueryHis(false);
//    log.debug(ot2qp.toString());

    gridModel = obDao.queryOtLevel2(ot2qp);
    recordsTotal = obDao.countOtLevel2(ot2qp);
    recordsFiltered = recordsTotal;
    log.debug(gridModel.size());

    return SUCCESS;
  }

  public void checkIsHistory(OtLevel2QueryParameter ot2qp) {
    String curUtc = CommonFunction.getCurUTCDateString();
    System.out.println(curUtc);
    if (ot2qp.getStartDate().isEmpty() && ot2qp.getEndDate().isEmpty()) {
      ot2qp.setQueryHis(false);
    } else if (ot2qp.getStartDate().equals(curUtc) && ot2qp.getEndDate().equals(curUtc)) {
      ot2qp.setQueryHis(false);
    } else {
      ot2qp.setQueryHis(true);
    }
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
   * @param ot2qp the ot2qp to set
   */
  public void setOt2qp(OtLevel2QueryParameter ot2qp) {
    this.ot2qp = ot2qp;
  }

  /**
   * @return the gridModel
   */
  public List<OtLevel2> getGridModel() {
    return gridModel;
  }

  /**
   * @return the ot2qp
   */
  public OtLevel2QueryParameter getOt2qp() {
    return ot2qp;
  }


}
