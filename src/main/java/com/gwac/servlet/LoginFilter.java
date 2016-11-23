/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author xy
 */
public class LoginFilter implements Filter {

  private static final String LOGON_URI = "/login.jsp";
  private String logon_page;

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    // TODO Auto-generated method stub
    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpResp = (HttpServletResponse) response;
    httpResp.setContentType("text/html");
    httpResp.setCharacterEncoding("utf-8");
    HttpSession session = httpReq.getSession();
    PrintWriter out = httpResp.getWriter();
    // 得到用户请求的URI
    String request_uri = httpReq.getRequestURI();
    // 得到web应用程序的上下文路径
    String ctxPath = httpReq.getContextPath();
    // 去除上下文路径，得到剩余部分的路径
    String uri = request_uri.substring(ctxPath.length());
    // 判断用户访问的是否是登录页面
    if (uri.equals(logon_page)) {
      chain.doFilter(request, response);
    } else {
      // 如果访问的不是登录页面，则判断用户是否已经登录
      if (session.getAttribute("userInfo") != null) {
        chain.doFilter(request, response);
      } else {
        out.println("<script language=\"JavaScript\">"
                + "parent.location.href='" + ctxPath + logon_page + "'"
                + "</script>");
        // httpReq.getRequestDispatcher(logon_page).forward(httpReq,httpResp);
      }
    }
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    // TODO Auto-generated method stub
    // 从部署描述符中获取登录页面和首页的URI
    logon_page = config.getInitParameter(LOGON_URI);
    // System.out.println(logon_page);
    if (null == logon_page) {
      throw new ServletException("没有找到登录页面或主页");
    }
  }
}
