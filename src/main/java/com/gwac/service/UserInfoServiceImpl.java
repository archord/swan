package com.gwac.service;

import java.util.List;

import com.gwac.dao.UserInfoDAO;
import com.gwac.enums.ServiceResult;
import com.gwac.model.UserInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserInfoServiceImpl implements UserInfoService {

  UserInfoDAO userDAO;

  //DI via Spring
  public void setUserDAO(UserInfoDAO userDAO) {
    this.userDAO = userDAO;
  }

  //call DAO to save user
  public ServiceResult addUser(UserInfo user) {
    ServiceResult serviceResult;
    try {
      userDAO.save(user);
      serviceResult = ServiceResult.RESULT_OK;
    } catch (Exception ex) {
      serviceResult = ServiceResult.RESULT_ERROR;
      Logger.getLogger(UserInfoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    return serviceResult;
  }

  //call DAO to return users
  public List<UserInfo> listUser(int start, int resultSize) {
    String order[] = {"name"};
    return userDAO.findRecord(start, resultSize, order);
  }

  public int count() {
    return userDAO.count().intValue();
  }

}
