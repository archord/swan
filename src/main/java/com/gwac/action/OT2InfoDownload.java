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
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* wget command example: */
/* wget http://190.168.1.25/getCutImageList.action?dpmName=M01 -O aa.list*/
/**
 * @author xy
 */
public class OT2InfoDownload extends ActionSupport {

  private static final Log log = LogFactory.getLog(OT2InfoDownload.class);

  private InputStream inputStream;
//  private long contentLength; //该变量要么设置一个对的值，要不不设置
  private String fileName;
  private String contentType;

  private String otName;
  private FitsFileCutDAO ffcDao;
  private String rootWebDir;
  private String echo;

  @Override
  public String execute() {

    contentType = "application/octet-stream";
    fileName = "myZip.zip";

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //必须OT名称
//    if (null != otName && !otName.isEmpty() && otName.length() == 14) {
    if (true) {

      List<File> files = new ArrayList();
      files.add(new File("e:/aa.png"));
      files.add(new File("e:/bb.png"));
      files.add(new File("e:/bgregin.png"));

      try {
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
          for (File file : files) {

            System.out.println("Adding " + file.getName());
            zos.putNextEntry(new ZipEntry(file.getName()));
            try (FileInputStream fis = new FileInputStream(file)) {
              int data;
              BufferedInputStream fif = new BufferedInputStream(fis);
              while ((data = fif.read()) != -1) {
                zos.write(data);
              }
              fif.close();
              zos.closeEntry();
            } catch (FileNotFoundException e) {
              zos.write(("error: not find file " + file.getName()).getBytes());
              zos.closeEntry();
              log.error("cannot find file " + file.getAbsolutePath(), e);
            }
          }

          zos.flush();
          zos.close();
        }
      } catch (IOException e) {
        log.error(e);
      }
    }
    inputStream = new ByteArrayInputStream(baos.toByteArray());

    return SUCCESS;
  }

  public String display() {
    return NONE;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @param rootWebDir the rootWebDir to set
   */
  public void setRootWebDir(String rootWebDir) {
    this.rootWebDir = rootWebDir;
  }

  /**
   * @return the inputStream
   */
  public InputStream getInputStream() throws Exception {
    return inputStream;
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

}
