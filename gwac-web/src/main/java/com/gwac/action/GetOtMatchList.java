package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtLevel2MatchDao;
import com.gwac.model.OtLevel2MatchShow;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

@Result(name = "success", type = "json")
public class GetOtMatchList extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 5078264277068533593L;
  private static final Log log = LogFactory.getLog(GetOtMatchList.class);
  // Your result List
  private List<OtLevel2MatchShow> gridModel;
  // get how many rows we want to have into the grid - rowNum attribute in the
  // grid
  private Integer rows = 0;
  // Get the requested page. By default grid sets this to 1.
  private Integer page = 0;
  // sorting order - asc or desc
  private String sord;
  // get index row - i.e. user click to sort.
  private String sidx;
  // Search Field
  private String searchField;
  // The Search String
  private String searchString;
  // Limit the result when using local data, value form attribute rowTotal
  private Integer totalrows;
  // he Search Operation
  // ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
  private String searchOper;
  // Your Total Pages
  private Integer total = 0;
  // All Records
  private Integer records = 0;
  private boolean loadonce = false;
  private Map<String, Object> session;
  @Resource
  private OtLevel2MatchDao otmDao;
  @Resource
  private OtLevel2Dao obDao;
  private String otName;
  private Boolean queryHis;

  @SuppressWarnings("unchecked")
  public String execute() {

    int to = (rows * page);
    int from = to - rows;

    List<Integer> tlist = obDao.hisOrCurExist(otName);
    if (!tlist.isEmpty()) {
      Integer his = tlist.get(0);
      queryHis = his == 1;
      gridModel = otmDao.getByOt2Name(otName, queryHis);
    } else {
      gridModel = new ArrayList();
    }
    records = gridModel.size();

    if (totalrows != null) {
      records = totalrows;
    }
    if (to > records) {
      to = records;
    }
    total = (int) Math.ceil((double) records / (double) rows);

    return SUCCESS;
  }

//  public String getJSON() {
//    return execute();
//  }
  /**
   * @return how many rows we want to have into the grid
   */
  public Integer getRows() {
    return rows;
  }

  /**
   * @param rows how many rows we want to have into the grid
   */
  public void setRows(Integer rows) {
    this.rows = rows;
  }

  /**
   * @return current page of the query
   */
  public Integer getPage() {
    return page;
  }

  /**
   * @param page current page of the query
   */
  public void setPage(Integer page) {
    this.page = page;
  }

  /**
   * @return total pages for the query
   */
  public Integer getTotal() {
    return total;
  }

  /**
   * @param total total pages for the query
   */
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   * @return total number of records for the query. e.g. select count(*) from
   * table
   */
  public Integer getRecords() {
    return records;
  }

  /**
   * @param records total number of records for the query. e.g. select count(*)
   * from table
   */
  public void setRecords(Integer records) {

    this.records = records;

    if (this.records > 0 && this.rows > 0) {
      this.total = (int) Math.ceil((double) this.records
              / (double) this.rows);
    } else {
      this.total = 0;
    }
  }

  /**
   * @return sorting order
   */
  public String getSord() {
    return sord;
  }

  /**
   * @param sord sorting order
   */
  public void setSord(String sord) {
    this.sord = sord;
  }

  /**
   * @return get index row - i.e. user click to sort.
   */
  public String getSidx() {
    return sidx;
  }

  /**
   * @param sidx get index row - i.e. user click to sort.
   */
  public void setSidx(String sidx) {
    this.sidx = sidx;
  }

  public void setSearchField(String searchField) {
    this.searchField = searchField;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public void setSearchOper(String searchOper) {
    this.searchOper = searchOper;
  }

  public void setLoadonce(boolean loadonce) {
    this.loadonce = loadonce;
  }

  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

  public void setTotalrows(Integer totalrows) {
    this.totalrows = totalrows;
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
   * @return the gridModel
   */
  public List<OtLevel2MatchShow> getGridModel() {
    return gridModel;
  }

  /**
   * @param gridModel the gridModel to set
   */
  public void setGridModel(List<OtLevel2MatchShow> gridModel) {
    this.gridModel = gridModel;
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

}
