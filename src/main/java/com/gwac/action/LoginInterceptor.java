/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.util.Map;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author xy
 */
public class LoginInterceptor extends AbstractInterceptor {

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    
    // 取得请求的Action名
    String name = invocation.getInvocationContext().getName();
    System.out.println(name);
    // 如果用户想登录，则使之通过
    if (!name.equals("pgwacOtDetail")) {
      System.out.println("action name: "+ name);
      return invocation.invoke();
    } else {

      // 取得Session
      ActionContext ac = invocation.getInvocationContext();
      Map session = (Map) ac.get(ServletActionContext.SESSION);
      if (session == null) {
        // 如果Session为空，则让用户登陆。
        return "login";
      } else {
        String username = (String) session.get("username");
        if (username == null) {
          // Session不为空，但Session中没有用户信息，
          // 则让用户登陆
          return "login";
        } else {
          // 用户已经登陆，放行~
          return invocation.invoke();
        }
      }
    }
  }

}
