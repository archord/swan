/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.gwac.action;

import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class Ajax1 extends ActionSupport {

    private static final long serialVersionUID = -7895258309088641394L;

    @Action(value = "/ajax1", results = { @Result(location = "ajax1.jsp", name = "success") })
    public String execute() throws Exception {
      Date date =  new Date();
      System.out.println("date="+date);
      double jd = CommonFunction.dateToJulian(CommonFunction.getUTCDate(date));
      System.out.println("jd="+jd);
      System.out.println("jd="+(jd-2400000.5));
	return SUCCESS;
    }
}
