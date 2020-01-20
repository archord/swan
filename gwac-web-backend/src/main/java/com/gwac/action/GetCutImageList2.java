/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.CameraDao;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.model.Camera;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Value;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* wget command example: */
/* wget http://190.168.1.25/getCutImageList.action?cameraName=M01 -O aa.list*/
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class GetCutImageList2 extends ActionSupport {
  
  private static final Log log = LogFactory.getLog(GetCutImageList2.class);
  private String cameraName;
  
  private InputStream fileInputStream;
  private String fileName;
  @Value("#{syscfg.gwacDataRootDirectoryWebmap}")
  private String rootWebDir;
  private String echo = "";
  
  @Resource
  private CameraDao camDao;
  @Resource
  private OtLevel2Dao ot2Dao;
  
  @Action(value = "getCutImageList2", results = {
    @Result(location = "forward.jsp", name = SUCCESS),
    @Result(location = "forward.jsp", name = INPUT),
    @Result(location = "forward.jsp", name = ERROR)})
  public String upload() {
    
    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须设置传输机器名称
    if (null == cameraName || cameraName.isEmpty()) {
      echo = echo + "Must set machine name(cameraName).\n";
      flag = false;
    }
    
    if (flag) {
      String rootPath = getText("gwacDataRootDirectory");
      String destPath = rootPath;
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/tmp/";
      } else {
        destPath += "tmp/";
      }
      File tmpDir = new File(destPath);
      if (!tmpDir.exists()) {
        tmpDir.mkdirs();
        log.debug("create dir " + tmpDir);
      }
      
      cameraName = cameraName.trim();
      
      try {
        String content = "";
        Camera tcamera = camDao.getByName(cameraName);
        if (tcamera != null) {
          content = ot2Dao.getOT2CutList(tcamera.getCameraId());
          if (!content.isEmpty()) {
            fileName = cameraName + "_" + CommonFunction.getCurDateTimeString() + ".lst";
            File file = new File(destPath, fileName);
            if (!file.exists()) {
              file.createNewFile();
              log.debug("create cut image list file " + file);
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            
          }
        } else {
          log.warn("cannot find camera: " + cameraName);
        }
        if (content.isEmpty()) {
          fileName = "empty.lst";
          File file = new File(destPath, fileName);
          if (!file.exists()) {
            file.createNewFile();
            log.debug("create empty file " + file);
          }
        }
      } catch (IOException ex) {
        log.error("create or write file error ", ex);
      }
    } else {
      fileName = "empty.lst";
      result = ERROR;
    }
    String imgList = rootWebDir + "/tmp/" + fileName;
    returnFile(imgList);
    return null;
  }
  
  public void returnFile(String fpath) {
    
    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");
    try {
      response.sendRedirect(fpath);
    } catch (IOException ex) {
      log.error("response error: ", ex);
    }
  }
  
  public String display() {
    return NONE;
  }
  
  public InputStream getFileInputStream() {
    return fileInputStream;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @param cameraName the cameraName to set
   */
  public void setCameraName(String cameraName) {
    this.cameraName = cameraName;
  }
}
