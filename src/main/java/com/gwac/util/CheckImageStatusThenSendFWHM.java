/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.util;

import com.gwac.util.SendMessage;
import com.gwac.model.ImageStatusParameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class CheckImageStatusThenSendFWHM {

    private static final Log log = LogFactory.getLog(CheckImageStatusThenSendFWHM.class);
    private ImageStatusParameter isp;
    private Boolean status;

    public CheckImageStatusThenSendFWHM() {
        this.status = false;
    }

    public CheckImageStatusThenSendFWHM(ImageStatusParameter isp) {
        this.isp = isp;
        this.status = false;
    }

    public void chechStatus() {
        Boolean s1 = isp.getXrms() < 0.13 && isp.getYrms() < 0.13 && isp.getAvgEllipticity() > 0
                && isp.getAvgEllipticity() < 0.26 && isp.getObjNum() > 5000 && isp.getBgBright() < 10000
                && isp.getS2n() < 0.5 && isp.getAvgLimit() > 11;
        Boolean s2 = (isp.getXshift() + 99) < 0.00001 && isp.getAvgEllipticity() > 0 && isp.getAvgEllipticity() < 0.26
                && isp.getObjNum() > 5000 && isp.getBgBright() < 10000;
        if (s1 || s2) {
            int dpmId = isp.getDpmId();
            String msg = "d#fwhm" + (int) Math.ceil(dpmId / 2.0);
            if (dpmId % 2 == 0) {
                msg += "N";
            } else {
                msg += "S";
            }
            msg += String.format("%04.0f", isp.getFwhm() * 100);
            msg += "%";
            SendMessage sm = new SendMessage();
            sm.send(msg);
            log.debug("send message: "+msg);
        }
    }

    /**
     * @return the isp
     */
    public ImageStatusParameter getIsp() {
        return isp;
    }

    /**
     * @param isp the isp to set
     */
    public void setIsp(ImageStatusParameter isp) {
        this.isp = isp;
    }

}
