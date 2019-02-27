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
import com.gwac.model.OtObserveRecord;
import com.gwac.util.CommonFunction;
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
import javax.annotation.Resource;
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
  @Resource
  private FitsFileCutDAO ffcDao;
  @Resource
  private FitsFileCutRefDAO ffcrDao;
  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
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
        List<FitsFileCutRef> ffcrs = ffcrDao.getCutImageByOtId(ot2.getOtId(), queryHis);
        List<OtObserveRecord> oors = otorDao.getRecordByOt2Id(ot2.getOtId(), queryHis);

        List<File> tfiles = new ArrayList();
        String tpath = "";
        if (ffcrs != null && ffcrs.size() > 0) {
          FitsFileCutRef ffcr = ffcrs.get(0);
          tpath = dataRootDir + "/" + ffcr.getStorePath() + "/";
          tfiles.add(new File(tpath, ffcr.getFileName() + ".fit"));
          tfiles.add(new File(tpath, ffcr.getFileName() + ".jpg"));
        }
        for (FitsFileCut tffc : ffcList) {
          if (tpath.isEmpty()) {
            tpath = dataRootDir + "/" + tffc.getStorePath() + "/";
          }
          tfiles.add(new File(tpath, tffc.getFileName() + ".fit"));
          tfiles.add(new File(tpath, tffc.getFileName() + ".jpg"));
        }

        try {
          try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (File file : tfiles) {
              if (file.exists()) {
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
            }
            zos.putNextEntry(new ZipEntry("catalog.txt"));
            zos.write("dateUt, ra, dec, x, y, xtemp, ytemp, mag, magerr, fwhm, ff_number\n".getBytes());
            for (OtObserveRecord oor : oors) {
              String tstr = String.format("%s, %.6f, %.6f, %.3f, %.3f, %.3f, %.3f, %f, %f, %f, %d\n",
                      CommonFunction.getDateTimeString2(oor.getDateUt()), oor.getRaD(), oor.getDecD(),
                      oor.getX(), oor.getY(), oor.getXTemp(), oor.getYTemp(), oor.getMagAper(),
                      oor.getMagerrAper(), oor.getThreshold(), oor.getFfNumber());
              zos.write(tstr.getBytes());
            }
            zos.closeEntry();

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
