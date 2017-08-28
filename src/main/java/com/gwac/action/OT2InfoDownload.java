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
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.OtLevel2;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* wget command example: */
/* wget http://localhost:8080/gwac/downloadot2.action?otName=M151207_C00163*/
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
  private FitsFileCutRefDAO ffcrDao;
  private OtLevel2Dao ot2Dao;
  private OtObserveRecordDAO otorDao;

  private String echo;

  @Override
  public String execute() {

    contentType = "application/octet-stream";
    fileName = "empty.zip";

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //必须OT名称
    if (null != otName && !otName.isEmpty() && otName.length() == 14) {
      String dataRootDir = getText("gwacDataRootDirectory");
      fileName = otName + ".zip";

      List<Integer> tlist = ot2Dao.hisOrCurExist(otName);
      if (!tlist.isEmpty()) {
        Integer his = tlist.get(0);
        Boolean queryHis = his == 1;
        OtLevel2 ot2 = ot2Dao.getOtLevel2ByName(otName, queryHis);
        List<FitsFileCut> ffcList = ffcDao.getCutImageByOtId(ot2.getOtId(), queryHis);
        List<FitsFileCutRef> ffcrs = ffcrDao.getCutImageByOtId(ot2.getOtId());

        List<File> tfiles = new ArrayList();
        String tpath = "";
        if (ffcrs != null && ffcrs.size() > 0) {
          FitsFileCutRef ffcr = ffcrs.get(0);
          tpath = dataRootDir + "/" + ffcr.getStorePath() + "/";
          tfiles.add(new File(tpath, ffcr.getFileName() + ".fit"));
        }
        for (FitsFileCut tffc : ffcList) {
          if (tpath.isEmpty()) {
            tpath = dataRootDir + "/" + tffc.getStorePath() + "/";
          }
          tfiles.add(new File(tpath, tffc.getFileName() + ".fit"));
        }

        try {
          try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (File file : tfiles) {
//              System.out.println("Adding " + file.getName());
//            zos.putNextEntry(new ZipEntry("xml/"));
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
          log.error("ZIP ot2=" + otName + " info error.", e);
        }
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

  /**
   * @param ffcrDao the ffcrDao to set
   */
  public void setFfcrDao(FitsFileCutRefDAO ffcrDao) {
    this.ffcrDao = ffcrDao;
  }

  /**
   * @param otorDao the otorDao to set
   */
  public void setOtorDao(OtObserveRecordDAO otorDao) {
    this.otorDao = otorDao;
  }

  /**
   * @param ot2Dao the ot2Dao to set
   */
  public void setOt2Dao(OtLevel2Dao ot2Dao) {
    this.ot2Dao = ot2Dao;
  }

}
