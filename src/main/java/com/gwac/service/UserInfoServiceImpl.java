package com.gwac.service;

import com.gwac.dao.UserInfoDAO;
import com.gwac.enums.ServiceResult;
import com.gwac.model.UserInfo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

  @Resource
  UserInfoDAO userDAO;

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
    int[] sorts = {1};
    return userDAO.findRecord(start, resultSize, order, sorts);
  }

  public int count() {
    return userDAO.count().intValue();
  }

}
