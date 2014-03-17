package com.gwac.service;

import com.gwac.enums.ServiceResult;
import java.util.List;

import com.gwac.model.UserInfo;

public interface UserInfoService {

  ServiceResult addUser(UserInfo customer);
  List<UserInfo> listUser();

}
