/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.ConfigFileDao;
import com.gwac.model.ConfigFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author xy
 */
public class DataSyncServiceImpl implements DataSyncService {

  private static final Log log = LogFactory.getLog(DataSyncServiceImpl.class);
  private static boolean running = true;
  private ConfigFileDao cfDao;
  private String rootDir;
  private String otLDir;
  private String starLDir;
  private String orgIDir;
  private String cutIDir;
  private String cfgDir;
  private String serverUrl;
  private String uploadUrl;
  private int mchNum;

  public void syncData() {

    if (running == true) {
      log.info("start job fileTransferJob...");
      running = false;
    } else {
      log.info("job fileTransferJob is running, jump this scheduler.");
      return;
    }

    List<ConfigFile> cfs = cfDao.getTopNUnSync(mchNum);
    for (ConfigFile cf : cfs) {
      syncConfigFile(cf);
    }

    if (running == false) {
      running = true;
      log.info("job fileTransferJob is done.");
    }
  }

  private int syncConfigFile(ConfigFile cf) {

    String configPath;
    String configFile;
    String[] otList;
    String[] starList;
    String[] origImage;
    String[] cutImages;

    InputStream input = null;
    int fNum = 0;

    CloseableHttpClient httpclient = HttpClients.createDefault();

    try {
      configPath = rootDir + "/" + cf.getStorePath();
      configFile = cf.getFileName();

      /**
       * 解析配置文件
       */
      File tfile = new File(configPath, configFile);
      if (!tfile.exists()) {
        log.error("file not exist: " + tfile);
        return 0;
      }
      input = new FileInputStream(tfile);
      Properties cfile = new Properties();
      cfile.load(input);

      String dateStr = cfile.getProperty("date").trim();
      String dpmName = cfile.getProperty("dpmname").trim();

      String tmpStr = cfile.getProperty("otlist").trim();
      otList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
      if (otList != null) {
        fNum += otList.length;
      }

      tmpStr = cfile.getProperty("starlist").trim();
      starList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
      if (starList != null) {
        fNum += starList.length;
      }

      tmpStr = cfile.getProperty("origimage").trim();
      origImage = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
      if (origImage != null) {
        fNum += origImage.length;
      }

      tmpStr = cfile.getProperty("cutimages").trim();
      cutImages = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
      if (cutImages != null) {
        fNum += cutImages.length;
      }


      /**
       * 发送配置文件及数据文件
       */
      HttpPost httppost = new HttpPost(serverUrl + uploadUrl);
      MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
      mpEntity.addPart("dpmName", new StringBody(dpmName, ContentType.TEXT_PLAIN));
      mpEntity.addPart("currentDirectory", new StringBody(dateStr, ContentType.TEXT_PLAIN));
      mpEntity.addPart("configFile", new FileBody(tfile));

      String rootPath = rootDir + "/" + dateStr + "/" + dpmName + "/";
      for (String tName : otList) {
        String tpath = rootPath + otLDir + "/" + tName;
        File tfile1 = new File(tpath);
        if(tfile1.exists()){
          mpEntity.addPart("fileUpload", new FileBody(tfile1));
        }
      }
      for (String tName : starList) {
        String tpath = rootPath + otLDir + "/" + tName;
        File tfile1 = new File(tpath);
        if(tfile1.exists()){
          mpEntity.addPart("fileUpload", new FileBody(tfile1));
        }
      }
      for (String tName : origImage) {
        String tpath = rootPath + otLDir + "/" + tName;
        File tfile1 = new File(tpath);
        if(tfile1.exists()){
          mpEntity.addPart("fileUpload", new FileBody(tfile1));
        }
      }
      for (String tName : cutImages) {
        String tpath = rootPath + otLDir + "/" + tName;
        File tfile1 = new File(tpath);
        if(tfile1.exists()){
          mpEntity.addPart("fileUpload", new FileBody(tfile1));
        }
      }

      HttpEntity reqEntity = mpEntity.build();
      httppost.setEntity(reqEntity);

      System.out.println("executing request " + httppost.getRequestLine());
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
          System.out.println("Response content length: " + resEntity.getContentLength());
          System.out.println("Response content: " + IOUtils.toString(resEntity.getContent()));
        }
        EntityUtils.consume(resEntity);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        response.close();
      }

    } catch (Exception ex) {
      log.error("load property file error!");
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        try {
          httpclient.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
    return fNum;
  }

  /**
   * @param cfDao the cfDao to set
   */
  public void setCfDao(ConfigFileDao cfDao) {
    this.cfDao = cfDao;
  }

  /**
   * @param rootDir the rootDir to set
   */
  public void setRootDir(String rootDir) {
    this.rootDir = rootDir;
  }

  /**
   * @param otLDir the otLDir to set
   */
  public void setOtLDir(String otLDir) {
    this.otLDir = otLDir;
  }

  /**
   * @param starLDir the starLDir to set
   */
  public void setStarLDir(String starLDir) {
    this.starLDir = starLDir;
  }

  /**
   * @param orgIDir the orgIDir to set
   */
  public void setOrgIDir(String orgIDir) {
    this.orgIDir = orgIDir;
  }

  /**
   * @param cutIDir the cutIDir to set
   */
  public void setCutIDir(String cutIDir) {
    this.cutIDir = cutIDir;
  }

  /**
   * @param cfgDir the cfgDir to set
   */
  public void setCfgDir(String cfgDir) {
    this.cfgDir = cfgDir;
  }

  /**
   * @param serverUrl the serverUrl to set
   */
  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }
}
