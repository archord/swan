package com.gwac.action;

import com.gwac.model.UserInfo;
import com.gwac.service.UserInfoService;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import java.util.Date;
import java.util.List;

@InterceptorRef("jsonValidationWorkflowStack")
@Validations(requiredStrings = {
  @RequiredStringValidator(fieldName = "abc", type = ValidatorType.FIELD, message = "用户名必须填写。"),
  @RequiredStringValidator(fieldName = "loginpassword", type = ValidatorType.FIELD, message = "密码必须填写。"),
  @RequiredStringValidator(fieldName = "loginpasswordrep", type = ValidatorType.FIELD, message = "重复密码必须填写。")},
	expressions = {
	  @ExpressionValidator(expression = "loginpassword.trim().equals(loginpasswordrep.trim()) == true", message = "两次输入密码必须相同。")
	})
public class UserRegister extends ActionSupport {

  private static final long serialVersionUID = 7968544374444173479L;
  private static final Log log = LogFactory.getLog(UserRegister.class);

  private String abc;
  private String loginpassword;
  private String loginpasswordrep;
  private String echo;

  private List<UserInfo> gridModel;

  private UserInfoService userService;

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
  private Integer total = 0;
  private Integer records = 0;

  private boolean loadonce = false;

  // simpleecho.jsp
  @Action(value = "registerAction", results = {
    @Result(location = "simpleecho.jsp", name = SUCCESS),
    @Result(location = "user/register.jsp", name = INPUT),
    @Result(location = "user/userlist.jsp", name = ERROR)})
  public String execute() throws Exception {

    UserInfo ui = new UserInfo();
    ui.setName(getAbc());
    ui.setPassword(loginpassword);
    ui.setCreateDate(new Date());
    userService.addUser(ui);

    echo = getAbc() + "注册成功！";
    log.info(echo);
    return SUCCESS;
  }

  @Action(value = "userListaaa", results = {
    @Result(name = SUCCESS, type = "json", location = "user/userlist.jsp")})
  public String userListaaaa() throws Exception {

    gridModel = userService.listUser();
    System.out.println("user number " + gridModel.size());
    return SUCCESS;
  }

  public String getEcho() {
    return echo;
  }

  public String getLoginpassword() {
    return loginpassword;
  }

  public void setLoginpassword(String loginpassword) {
    this.loginpassword = loginpassword;
  }

  /**
   * @return the loginpasswordrep
   */
  public String getLoginpasswordrep() {
    return loginpasswordrep;
  }

  /**
   * @param loginpasswordrep the loginpasswordrep to set
   */
  public void setLoginpasswordrep(String loginpasswordrep) {
    this.loginpasswordrep = loginpasswordrep;
  }

  /**
   * @return the userService
   */
  public UserInfoService getUserService() {
    return userService;
  }

  /**
   * @param userService the userService to set
   */
  public void setUserService(UserInfoService userService) {
    this.userService = userService;
  }

  /**
   * @return the abc
   */
  public String getAbc() {
    return abc;
  }

  /**
   * @param abc the abc to set
   */
  public void setAbc(String abc) {
    this.abc = abc;
  }

  /**
   * @return the rows
   */
  public Integer getRows() {
    return rows;
  }

  /**
   * @param rows the rows to set
   */
  public void setRows(Integer rows) {
    this.rows = rows;
  }

  /**
   * @return the page
   */
  public Integer getPage() {
    return page;
  }

  /**
   * @param page the page to set
   */
  public void setPage(Integer page) {
    this.page = page;
  }

  /**
   * @return the sord
   */
  public String getSord() {
    return sord;
  }

  /**
   * @param sord the sord to set
   */
  public void setSord(String sord) {
    this.sord = sord;
  }

  /**
   * @return the sidx
   */
  public String getSidx() {
    return sidx;
  }

  /**
   * @param sidx the sidx to set
   */
  public void setSidx(String sidx) {
    this.sidx = sidx;
  }

  /**
   * @return the searchField
   */
  public String getSearchField() {
    return searchField;
  }

  /**
   * @param searchField the searchField to set
   */
  public void setSearchField(String searchField) {
    this.searchField = searchField;
  }

  /**
   * @return the searchString
   */
  public String getSearchString() {
    return searchString;
  }

  /**
   * @param searchString the searchString to set
   */
  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  /**
   * @return the totalrows
   */
  public Integer getTotalrows() {
    return totalrows;
  }

  /**
   * @param totalrows the totalrows to set
   */
  public void setTotalrows(Integer totalrows) {
    this.totalrows = totalrows;
  }

  /**
   * @return the searchOper
   */
  public String getSearchOper() {
    return searchOper;
  }

  /**
   * @param searchOper the searchOper to set
   */
  public void setSearchOper(String searchOper) {
    this.searchOper = searchOper;
  }

  /**
   * @return the total
   */
  public Integer getTotal() {
    return total;
  }

  /**
   * @param total the total to set
   */
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   * @return the records
   */
  public Integer getRecords() {
    return records;
  }

  /**
   * @param records the records to set
   */
  public void setRecords(Integer records) {
    this.records = records;
  }

  /**
   * @return the loadonce
   */
  public boolean isLoadonce() {
    return loadonce;
  }

  /**
   * @param loadonce the loadonce to set
   */
  public void setLoadonce(boolean loadonce) {
    this.loadonce = loadonce;
  }

  /**
   * @return the gridModel
   */
  public List<UserInfo> getGridModel() {
    return gridModel;
  }

  /**
   * @param gridModel the gridModel to set
   */
  public void setGridModel(List<UserInfo> gridModel) {
    this.gridModel = gridModel;
  }
}
