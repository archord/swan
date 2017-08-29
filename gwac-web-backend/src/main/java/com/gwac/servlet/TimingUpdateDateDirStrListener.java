
/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.servlet;

import com.gwac.util.CommonFunction;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class TimingUpdateDateDirStrListener implements ServletContextListener {

  private static final Log log = LogFactory.getLog(TimingUpdateDateDirStrListener.class);
  private Timer timer;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    updateDateStr(sce.getServletContext());
    timer = new Timer(true);
    timer.schedule(new TimingUpdateDateDirStrTask(sce.getServletContext()), 0, 1800 * 1000);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    timer.cancel();
  }

  class TimingUpdateDateDirStrTask extends TimerTask {

    private static final int C_SCHEDULE_HOUR = 13; //每天下午1点更新日期目录名
    private ServletContext context = null;

    public TimingUpdateDateDirStrTask(ServletContext context) {
      this.context = context;
    }

    @Override
    public void run() {
      Calendar cal = Calendar.getInstance();
      if (C_SCHEDULE_HOUR == cal.get(Calendar.HOUR_OF_DAY)) {
        updateDateStr(context);
      }
    }
  }

  public void updateDateStr(ServletContext context) {
    String dateStr = CommonFunction.getUniqueDateStr();
    context.setAttribute("datestr", dateStr);
    log.debug("update dateStr=" + context.getAttribute("datestr"));
  }
}
