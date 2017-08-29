package com.gwac.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwac.service.UserInfoService;
import com.gwac.model.UserInfo;
import com.opensymphony.xwork2.ModelDriven;
import javax.annotation.Resource;

public class UserInfoAction implements ModelDriven {

  UserInfo user = new UserInfo();
  List<UserInfo> userList = new ArrayList<UserInfo>();
  @Resource
  UserInfoService userService;
  //DI via Spring

  public void setUserService(UserInfoService userService) {
    this.userService = userService;
  }

  public Object getModel() {
    return user;
  }

  public List<UserInfo> getUserList() {
    return userList;
  }

  public void setUserList(List<UserInfo> userList) {
    this.userList = userList;
  }

  //save user
  public String addUser() throws Exception {
    user.setRegisterDate(new Date());
    userService.addUser(user);
    userList = null;
//    userList = userService.listUser();
    return "success";
  }

  //list all users
  public String listUser() throws Exception {
//    userList = userService.listUser();
    return "success";
  }
}