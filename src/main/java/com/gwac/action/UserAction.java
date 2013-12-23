package com.gwac.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwac.service.UserService;
import com.gwac.model.User;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction implements ModelDriven {

  User user = new User();
  List<User> userList = new ArrayList<User>();
  UserService userService;
  //DI via Spring

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public Object getModel() {
    return user;
  }

  public List<User> getUserList() {
    return userList;
  }

  public void setUserList(List<User> userList) {
    this.userList = userList;
  }

  //save user
  public String addUser() throws Exception {
    user.setCreatedDate(new Date());
    userService.addUser(user);
    userList = null;
    userList = userService.listUser();
    return "success";
  }

  //list all users
  public String listUser() throws Exception {
    userList = userService.listUser();
    return "success";
  }
}