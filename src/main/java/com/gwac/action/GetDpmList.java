package com.gwac.action;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Result;

import com.gwac.model.DataProcessMachine;
import com.gwac.service.DataProcessMachineService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

@Result(name = "success", type = "json")
public class GetDpmList extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 5078264279068543593L;
  private static final Log log = LogFactory.getLog(GetDpmList.class);
  // Your result List
  private List<DataProcessMachine> gridModel;
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
//  private List<DataProcessMachine> dpmList;
  private DataProcessMachineService dpmService = null;

  @SuppressWarnings("unchecked")
  public String execute() {

    log.debug("Page " + getPage() + " Rows " + getRows()
            + " Sorting Order " + getSord() + " Index Row :" + getSidx());
    log.debug("Search :" + searchField + " " + searchOper + " "
            + searchString);

    // Count all record (select count(*) from your_custumers)
    Number tn = dpmService.count();
    if (tn != null) {
      records = tn.intValue();
    } else {
      records = 0;
    }
//    records = 1;

    if (totalrows != null) {
      records = totalrows;
    }

    // Calucalate until rows ware selected
    int to = (rows * page);

    // Calculate the first row to read
    int from = to - rows;

    // Set to = max rows
    if (to > records) {
      to = records;
    }

//    setGridModel(dpmDao.findAll(page, rows));
//    gridModel = dpmService.findAll();
    gridModel = dpmService.findAll(from, rows);
    log.debug("from="+from);
    log.debug("to="+to);
    log.debug("size=" + gridModel.size());
//    for(DataProcessMachine dpm: gridModel){
//      System.out.println("name="+dpm.getName());
//    }
    // Calculate total Pages
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
   * @return an collection that contains the actual data
   */
  public List<DataProcessMachine> getGridModel() {
    return gridModel;
  }

  /**
   * @param gridModel an collection that contains the actual data
   */
  public void setGridModel(List<DataProcessMachine> gridModel) {
    this.gridModel = gridModel;
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

  public void setTotalrows(Integer totalrows) {
    this.totalrows = totalrows;
  }

  /**
   * @return the dpmService
   */
//  public DataProcessMachineService getDpmService() {
//    return dpmService;
//  }

  /**
   * @param dpmService the dpmService to set
   */
  public void setDpmService(DataProcessMachineService dpmService) {
    this.dpmService = dpmService;
  }

  public void setSession(Map<String, Object> session) {
    this.session = session;
  }
}
