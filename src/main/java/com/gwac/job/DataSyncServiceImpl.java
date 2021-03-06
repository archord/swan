/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

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
 * 数据同步 根据传输配置文件，将一级OT列表，切图，模板切图等传输到另一个服务器
 *
 * @author xy
 */
public class DataSyncServiceImpl implements DataSyncService {

  private static final Log log = LogFactory.getLog(DataSyncServiceImpl.class);
  private ConfigFileDao cfDao;
  private String rootDir;
  private String otLDir;
  private String starLDir;
  private String orgIDir;
  private String cutIDir;
  private String cfgDir;
  private String serverUrl;
  private String uploadUrl;
  private int mchNum; //gwac.machine.number

  private static boolean running = true;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;

  @Override
  public void startJob() {

    if (isBeiJingServer || isTestServer) {
      return;
    }

    if (running == true) {
      log.debug("start job...");
      running = false;
    } else {
      log.warn("job is running, jump this scheduler.");
      return;
    }

    long startTime = System.nanoTime();
    try {//JDBCConnectionException or some other exception
      List<ConfigFile> cfs = cfDao.getTopNUnSync(mchNum);
      for (ConfigFile cf : cfs) {
        syncConfigFile(cf);
      }
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }
    }
    long endTime = System.nanoTime();
    log.debug("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

  private void syncConfigFile(ConfigFile cf) {

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
        log.warn(tfile + " not exist.");
        return;
      } else {
        log.debug("read config file: " + tfile.getAbsolutePath());
      }
      input = new FileInputStream(tfile);
      Properties cfile = new Properties();
      cfile.load(input);

      String dateStr = cfile.getProperty("date").trim();
      String dpmName = cfile.getProperty("dpmname").trim();

      if (dpmName.isEmpty()) {
        dpmName = configFile.substring(0, 1) + configFile.substring(3, 5);
      }

      String tmpStr = cfile.getProperty("otlist");
      otList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
      if (otList != null) {
        fNum += otList.length;
      }

      tmpStr = cfile.getProperty("starlist");
      starList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
      if (starList != null) {
        fNum += starList.length;
      }

      tmpStr = cfile.getProperty("origimage");
      origImage = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
      if (origImage != null) {
        fNum += origImage.length;
      }

      tmpStr = cfile.getProperty("cutimages");
      cutImages = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
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
      log.debug("rootPath=" + rootPath);

      if (otList != null) {
        for (String tName : otList) {
          String tpath = rootPath + otLDir + "/" + tName.trim();
          File tfile1 = new File(tpath);
          if (tfile1.exists()) {
            mpEntity.addPart("fileUpload", new FileBody(tfile1));
          } else {
            log.warn(tfile1.getAbsolutePath() + " not exist!");
          }
        }
      }

      if (starList != null) {
        for (String tName : starList) {
          String tpath = rootPath + starLDir + "/" + tName.trim();
          File tfile1 = new File(tpath);
          if (tfile1.exists()) {
            mpEntity.addPart("fileUpload", new FileBody(tfile1));
          } else {
            log.warn(tfile1.getAbsolutePath() + " not exist!");
          }
        }
      }

      if (origImage != null) {
        for (String tName : origImage) {
          String tpath = rootPath + orgIDir + "/" + tName.trim();
          File tfile1 = new File(tpath);
          if (tfile1.exists()) {
            mpEntity.addPart("fileUpload", new FileBody(tfile1));
          } else {
            log.warn(tfile1.getAbsolutePath() + " not exist!");
          }
        }
      }

      if (cutImages != null) {
        for (String tName : cutImages) {
          String tpath = rootPath + cutIDir + "/" + tName.trim();
          File tfile1 = new File(tpath);
          if (tfile1.exists()) {
            mpEntity.addPart("fileUpload", new FileBody(tfile1));
          } else {
            log.warn(tfile1.getAbsolutePath() + " not exist!");
          }
        }
      }

      HttpEntity reqEntity = mpEntity.build();
      httppost.setEntity(reqEntity);
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        log.debug(response.getStatusLine());
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
          log.debug("response content: " + IOUtils.toString(resEntity.getContent()));
        }
        EntityUtils.consume(resEntity);
      } catch (IOException ex) {
        log.error("read response error", ex);
      } catch (IllegalStateException ex) {
        log.error("get content error", ex);
      } finally {
        response.close();
      }

    } catch (IOException ex) {
      log.error("read property file or send request error", ex);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException ex) {
          log.error("close properties error", ex);
        }
        try {
          httpclient.close();
        } catch (IOException ex) {
          log.error("close httpclient error", ex);
        }
      }
    }
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

  /**
   * @param mchNum the mchNum to set
   */
  public void setMchNum(int mchNum) {
    this.mchNum = mchNum;
  }

  /**
   * @return the uploadUrl
   */
  public String getUploadUrl() {
    return uploadUrl;
  }

  /**
   * @param uploadUrl the uploadUrl to set
   */
  public void setUploadUrl(String uploadUrl) {
    this.uploadUrl = uploadUrl;
  }

  /**
   * @param isBeiJingServer the isBeiJingServer to set
   */
  public void setIsBeiJingServer(Boolean isBeiJingServer) {
    this.isBeiJingServer = isBeiJingServer;
  }

  /**
   * @param isTestServer the isTestServer to set
   */
  public void setIsTestServer(Boolean isTestServer) {
    this.isTestServer = isTestServer;
  }
}
