/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.ObjectIdentityDao;
import com.gwac.dao.ObjectTypeDao;
import com.gwac.model.ObjectIdentity;
import com.gwac.model.ObjectType;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;
import org.springframework.beans.factory.annotation.Value;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* wget command example: */
/* wget http://190.168.1.25/getCutImageList.action?cameraName=M01 -O aa.list*/
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class GetCutImageList extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(GetCutImageList.class);
  private String cameraName;
  @Resource
  private FitsFileCutDAO ffcDao;
  private InputStream fileInputStream;
  private String fileName;
  @Value("#{syscfg.gwacDataRootDirectoryWebmap}")
  private String rootWebDir;
  private String echo = "";

  @Resource
  private ObjectIdentityDao objIdtyDao;
  @Resource
  private ObjectTypeDao objTypeDao;
  private Map<String, Object> appMap = null;

  @Action(value = "getCutImageList", results = {
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
      ObjectType cameraType;
      if (Integer.parseInt(cameraName) % 5 == 0) {
        cameraType = (ObjectType) appMap.get("FFoV");
      } else {
        cameraType = (ObjectType) appMap.get("JFoV");
      }

      try {
        String content = "";
        if (cameraType != null) {
          ObjectIdentity objId = objIdtyDao.getByName(cameraType, cameraName);
          content = ffcDao.getUnCuttedStarList(objId.getObjId(), 6, Short.MAX_VALUE); //Short.MAX_VALUE, 最初取值为6，即最多只裁剪优先级编号小于6的切图
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
      result = ERROR;
    }
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);
    ctx.getSession().put("fileName", rootWebDir + "/tmp/" + fileName);
    return result;
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

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }
}
