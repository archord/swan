/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.servlet;

import com.gwac.model.UserInfo;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class LoginFilter implements Filter {

  private static final Log log = LogFactory.getLog(LoginFilter.class);

  private static final String EXCEPTIONLIST = "exception-list";
  private static final String LOGINURL = "login-url";
  private static final String INDEXURL = "login-url";
  private String exceptionList;
  private String loginUrl;
  private String indexUrl;

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpResp = (HttpServletResponse) response;
    httpResp.setContentType("text/html");
    httpResp.setCharacterEncoding("utf-8");
    HttpSession session = httpReq.getSession();
//    PrintWriter out = httpResp.getWriter();

    String requestUri = httpReq.getRequestURI();
    String ctxPath = httpReq.getContextPath(); // 项目名/gwac
    String uri = requestUri.substring(ctxPath.length()); //页面名

    log.debug(uri);
    if (exceptionList.indexOf(uri) >= 0) {
      chain.doFilter(request, response);
    } else {
      UserInfo tuser = (UserInfo) session.getAttribute("userInfo");
      if (tuser != null) {
        chain.doFilter(request, response);
      } else {
        httpResp.sendRedirect(ctxPath + loginUrl);
      }
    }
  }

  @Override
  public void init(FilterConfig config) throws ServletException {

    exceptionList = config.getInitParameter(EXCEPTIONLIST);
    loginUrl = config.getInitParameter(LOGINURL);
    indexUrl = config.getInitParameter(INDEXURL);
  }
}
