/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.activemq.OTListMessageCreator;
import com.gwac.dao.CameraDao;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.Camera;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class CommonFileUpload extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(CommonFileUpload.class);
  private static final Set<String> typeSet = new HashSet(Arrays.asList(new String[]{"crsot1", "imqty", "subot1", "impre", "magclb", "subot2im"}));

  @Resource
  private CameraDao cameraDao;
  @Resource
  private FitsFileCutDAO ffcDao;
  @Resource
  private FitsFileCutRefDAO ffcrDao;
  @Resource
  private UploadFileUnstoreDao ufuDao;
  @Resource
  private SystemStatusMonitorDao ssmDao;
  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "otlistDest")
  private Destination otlistDest;

  private Map<String, Object> appmap;

  private String fileType;
  private String sendTime; //yyyyMMddHHmmssSSS
  private Date sendTimeObj;
  private List<File> fileUpload = new ArrayList<>();
  private List<String> fileUploadContentType = new ArrayList<>();
  private List<String> fileUploadFileName = new ArrayList<>();
  private String echo = "";

  @Action(value = "commonFileUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    long startTime = System.nanoTime();
    long endTime = 0;

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    log.debug("sendTime=" + sendTime + ", fileType=" + fileType + ": " + fileUpload.size() + " files.");

    //必须设置传输机器名称
    if (null == fileType || fileType.isEmpty()) {
      echo = echo + "Error, must set file type(fileType).\n";
      flag = false;
    }

    //必须传输数据文件
    //Error, must transform data file
    if (fileUpload.isEmpty()) {
      echo = echo + "Error, must upload data file(fileUpload).\n";
      flag = false;
    }

    if (fileUpload.size() != fileUploadFileName.size()) {
      echo = echo + "Error，please check upload command and retry!\n";
      flag = false;
    }

    //计算数据文件大小
    long fileTotalSize = 0;
    for (File file : fileUpload) {
      fileTotalSize += file.length();
    }
    int i = 0;
    if (fileTotalSize * 1.0 / 1048576 > 100.0) {
      echo = echo + "total file size is " + fileTotalSize * 1.0 / 1048576 + " beyond 100MB, total file " + fileUpload.size();
      for (File file : fileUpload) {
        log.warn(fileUploadFileName.get(i++).trim() + ": " + file.length() * 1.0 / 1048576 + "MB");
      }
      flag = false;
    }
    log.debug("fileTotalSize:" + fileTotalSize * 1.0 / 1048576 + "MB");

    if (flag) {
      try {
        if (sendTime != null && !sendTime.isEmpty()) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
          sendTimeObj = sdf.parse(sendTime);
        }
      } catch (ParseException ex) {
        sendTimeObj = null;
        log.error("parse sendTime: " + sendTime + " error", ex);
      }
      try {
        String dateStr = (String) appmap.get("datestr");
        if (null == dateStr) {
          dateStr = CommonFunction.getUniqueDateStr();
          appmap.put("datestr", dateStr);
          log.debug("has not dateStr:" + dateStr);
        } else {
          log.debug("has dateStr:" + dateStr);
        }

        String rootPath = getText("gwacDataRootDirectory");
        if (rootPath.charAt(rootPath.length() - 1) != '/') {
          rootPath += "/";
        }

        //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7, otlistSub:8, magclb:9, impre:a, 
        if (typeSet.contains(fileType)) {
          //G002_Mon_objt_161219T11523152
          String tfName = fileUploadFileName.get(0);
          String dpmName = tfName.substring(0, tfName.indexOf("_"));
          String destPath = rootPath + dateStr + "/" + dpmName + "/";
          File destDir = new File(destPath);
          if (!destDir.exists()) {
            destDir.mkdirs();
            log.debug("create dir " + destDir);
          }
          char tfileType = '0';
          String tpath = "";
          switch (fileType) {
            case "crsot1":
              tfileType = '1';
              tpath = destPath + getText("gwacDataOtlistDirectory");
              break;
            case "imqty":
              tfileType = '7';
              tpath = destPath + getText("gwacDataImgstatusDirectory");
              break;
            case "subot1":
              tfileType = '8';
              tpath = destPath + getText("gwacDataOtlistsubDirectory");
              break;
            case "impre":
              tfileType = 'a';
              String thead = getText("gwacDataThumbnailDirectory");
              tpath = rootPath + thead + "/" + dateStr + "/" + dpmName;
              break;
            case "magclb":
              tfileType = '9';
              tpath = destPath + getText("gwacDataMagcalibrationDirectory");
              break;
            case "subot2im":
              tfileType = '4';
              tpath = destPath + getText("gwacDataCutimagesDirectory");
              break;
          }
          storeFile(fileUpload, fileUploadFileName, tpath, rootPath, tfileType);
          echo += "success upload " + fileUpload.size() + " files.";
        } else if ("ot2im".equals(fileType) || "ot2imr".equals(fileType) || "ot2ims".equals(fileType)) {
          storeOT2CutImage(fileUpload, fileUploadFileName, rootPath);
          echo += "success upload " + fileUpload.size() + " files.";
        } else {
          echo += "unrecognize fileType:" + fileType;
          for (String fname : fileUploadFileName) {
            echo += ", fname:" + fname;
          }
          log.warn(echo);
        }
        endTime = System.nanoTime();
      } catch (Exception ex) {
        log.error("delete or move file errror ", ex);
      }
    } else {
      result = ERROR;
    }

    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("fileType=" + fileType + ": " + fileUpload.size() + " files, total time: " + time1 + "s, ");

    log.debug(echo);
    sendResultMsg(echo);
    return null;
  }

  public void sendResultMsg(String msg) {

    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out;
    try {
      out = response.getWriter();
      out.print(msg);
    } catch (IOException ex) {
      log.error("response error: ", ex);
    }
  }

  public void storeOT2CutImage(List<File> files, List<String> fnames, String rootPath) {

    int i = 0;
    for (File file : files) {
      String tfilename = fnames.get(i++).trim();
      if (tfilename.isEmpty()) {
        continue;
      }
      log.debug("receive: " + tfilename);

      String tpath = "";
      if ("ot2im".equals(fileType)) {//交叉证认OT2切图，和图像相减OT2切图
        List<FitsFileCut> ffcs = ffcDao.getByName(tfilename.substring(0, tfilename.indexOf('.')));
        if (ffcs.size() > 0) {
          FitsFileCut ffc = ffcs.get(0);
          tpath = rootPath + ffc.getStorePath();

          if (tfilename.indexOf(".jpg") > 0 || tfilename.indexOf(".png") > 0) {
            ffcDao.uploadSuccessCutByName(tfilename.substring(0, tfilename.indexOf('.')));
          }
        }
      } else if ("ot2ims".equals(fileType)) { //M170117_C00033_0139_sub.jpg 交叉证认OT2切图与模板切图相减后的图像
        List<FitsFileCut> ffcs = ffcDao.getByName(tfilename.substring(0, tfilename.indexOf("_sub")));
        if (ffcs.size() > 0) {
          FitsFileCut ffc = ffcs.get(0);
          tpath = rootPath + ffc.getStorePath();
        }
      } else if ("ot2imr".equals(fileType)) {//交叉证认OT2模板切图
        List<FitsFileCutRef> ffcrs = ffcrDao.getByName(tfilename);
        if (ffcrs.size() > 0) {
          FitsFileCutRef ffrc = ffcrs.get(0);
          tpath = rootPath + ffrc.getStorePath();
          try {
            //G170110_C00482_0007_ref_20170110T121901664.jpg
            if (file.exists() && tfilename.endsWith("jpg")) {
              String dateStr = tfilename.substring(tfilename.indexOf("ref_") + 4, tfilename.indexOf(".jpg"));
              // && dateStr.length() == 15
              if (dateStr != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmssSSS");
                Date genDate = sdf.parse(dateStr.replace('T', ' '));

                FitsFileCutRef ffcr = new FitsFileCutRef();
                ffcr.setFileName(tfilename.substring(0, tfilename.indexOf(".jpg")));
                ffcr.setGenerateTime(genDate);
                ffcr.setSuccessCut(Boolean.TRUE);
                ffcrDao.updateByName(ffcr);
              } else {
                log.error("ot2 ref cut file date error, ref file name=" + tfilename);
              }
            }
          } catch (ParseException ex) {
            log.error("parse ref cut image date error.");
          }
        }
      }
      log.debug("receive file " + tfilename + " to " + tpath);

      if (!tpath.isEmpty()) {
        File destFile = new File(tpath, tfilename);
        //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
        try {
          if (destFile.exists()) {
            log.warn(destFile + " already exist, delete it.");
            FileUtils.forceDelete(destFile);
          }
          FileUtils.moveFile(file, destFile);
        } catch (IOException ex) {
          log.error("delete or move file errror ", ex);
        }
      }

      if (fnames.size() > 0) {
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        String unitId = ip.substring(ip.lastIndexOf('.') + 1);
        if (unitId.length() < 3) {
          unitId = "0" + unitId;
        }
        String lastName = fnames.get(0).trim();
        if ("ot2im".equals(fileType)) {//交叉证认OT2切图，和图像相减OT2切图
          ssmDao.updateImgCut(unitId, lastName);
        } else if ("ot2ims".equals(fileType)) { //M170117_C00033_0139_sub.jpg 交叉证认OT2切图与模板切图相减后的图像
          ssmDao.updateImgCutSub(unitId, lastName);
        } else if ("ot2imr".equals(fileType)) {//交叉证认OT2模板切图
          ssmDao.updateImgCutRef(unitId, lastName);
        }
      }
    }
  }

  public void storeFile(List<File> files, List<String> fnames, String path, String rootPath, char fileType) {

    int i = 0;
    for (File file : files) {
      String tfilename = fnames.get(i++).trim();
      if (tfilename.isEmpty()) {
        continue;
      }
      log.debug("receive file " + tfilename);
      if ('a' != fileType) {
        File destFile = new File(path, tfilename);
        //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
        try {
          if (destFile.exists()) {
            log.warn(destFile + " already exist, delete it.");
            FileUtils.forceDelete(destFile);
          }
          FileUtils.moveFile(file, destFile);
        } catch (IOException ex) {
          log.error("delete or move file errror ", ex);
        }
      }

      UploadFileUnstore obj = new UploadFileUnstore();
      obj.setStorePath(path.substring(rootPath.length()));
      obj.setFileName(tfilename);
      obj.setFileType(fileType);   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
      obj.setUploadDate(new Date());
      obj.setUploadSuccess(Boolean.TRUE);
      if (sendTimeObj != null) {
        obj.setSendTime(sendTimeObj);
      }
      ufuDao.save(obj);
      String unitId = tfilename.substring(1, tfilename.indexOf("_"));
      if ('1' == fileType) {
        MessageCreator tmc = new OTListMessageCreator(obj);
        jmsTemplate.send(otlistDest, tmc);
        ssmDao.updateOt1List(unitId, tfilename);
      } else if ('8' == fileType) {
        MessageCreator tmc = new OTListMessageCreator(obj);
        jmsTemplate.send(otlistDest, tmc);
        ssmDao.updateOt1ListSub(unitId, tfilename);
      } else if ('a' == fileType) {
        String tpath = rootPath + getText("gwacMonitorimageDirectory");
        String tname = tfilename.substring(0, tfilename.indexOf("_")) + "_ccdimg.jpg";
        String tnameSub = tfilename.substring(0, tfilename.indexOf("_")) + "_ccdimg_sub.jpg";
        File preFile = new File(tpath, tname);
        try {
          if (preFile.exists()) {
            FileUtils.forceDelete(preFile);
          }
//          if (destFile.exists()) {
//            FileUtils.copyFile(destFile, preFile);
//          }
          FileUtils.moveFile(file, preFile);
          int toWidth = 400;
          int toHeight = 400;
          getThumbnail(tpath, tname, tnameSub, toWidth, toHeight);
        } catch (IOException ex) {
          log.error("delete or move file errror ", ex);
        }

        cameraDao.updateMonitorImageTime(unitId);
        ssmDao.updateThumbnail(unitId, tfilename);
      } else if ('7' == fileType) {
        ssmDao.updateImgParmFile(unitId, tfilename);
      }
    }
  }

  public void getThumbnail(String path, String src, String dest, int toWidth, int toHeight) {

    BufferedImage result = null;
    try {
      File srcfile = new File(path, src);
      if (srcfile.exists()) {
        BufferedImage im = ImageIO.read(srcfile);
        result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_BYTE_GRAY);
        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(result, "jpg", new File(path, dest));
      } else {
        log.error("image " + src + " does not exist.");
      }
    } catch (IOException e) {
      log.error("create thumbnail error:", e);
    }
  }

  public String display() {
    return NONE;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param fileType the fileType to set
   */
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appmap = map;
  }

  /**
   * @param fileUpload the fileUpload to set
   */
  public void setFileUpload(List<File> fileUpload) {
    this.fileUpload = fileUpload;
  }

  /**
   * @param fileUploadContentType the fileUploadContentType to set
   */
  public void setFileUploadContentType(List<String> fileUploadContentType) {
    this.fileUploadContentType = fileUploadContentType;
  }

  /**
   * @param fileUploadFileName the fileUploadFileName to set
   */
  public void setFileUploadFileName(List<String> fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }

  /**
   * @param sendTime the sendTime to set
   */
  public void setSendTime(String sendTime) {
    this.sendTime = sendTime;
  }

}
