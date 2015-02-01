/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.SyncFileDao;
import com.gwac.model.SyncFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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
 * 对监测图像进行同步
 *
 * @author xy
 */
public class MonitorImageSyncServiceImpl implements MonitorImageSyncService {

  private static final Log log = LogFactory.getLog(MonitorImageSyncServiceImpl.class);
  private static boolean running = true;
  private SyncFileDao sfDao;
  private String rootDir;
  private String statusImageLDir;
  private String serverUrl;
  private String uploadUrl;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;

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
    List<SyncFile> sfs = sfDao.getUnSyncFile();
    syncSyncFile(sfs);
//    addImage();
    long endTime = System.nanoTime();

    if (running == false) {
      running = true;
      log.debug("job is done.");
    }
    log.debug("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

  private void syncSyncFile(List<SyncFile> sfs) {

    if (rootDir.charAt(rootDir.length() - 1) != '/') {
      setRootDir(rootDir + "/");
    }

    if (sfs != null && sfs.size() > 0) {

      CloseableHttpClient httpclient = HttpClients.createDefault();
      HttpPost httppost = new HttpPost(serverUrl + uploadUrl);
      MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();

      for (SyncFile sf : sfs) {

        String sfPath = sf.getPath();
        if (sfPath.charAt(sfPath.length() - 1) != '/') {
          sfPath += "/";
        }

        String tpath = rootDir + sfPath + sf.getFileName().trim();
        File tfile1 = new File(tpath);
        if (tfile1.exists()) {
          mpEntity.addPart("fileUpload", new FileBody(tfile1));
        } else {
          log.warn(tfile1.getAbsolutePath() + " not exist!");
        }
      }

      HttpEntity reqEntity = mpEntity.build();
      httppost.setEntity(reqEntity);
      CloseableHttpResponse response = null;
      try {
        response = httpclient.execute(httppost);
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
        try {
          if (response != null) {
            response.close();
          }
          if (httpclient != null) {
            httpclient.close();
          }
        } catch (IOException ex) {
          log.error("close response or httpclient error", ex);
        }
      }
    }

  }

  private void addImage() {

    for (int i = 1; i < 13; i++) {
      String tfilename = "M" + String.format("%02d", i) + "_bgbright.jpg";
      SyncFile tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_ccdimg.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_diffmag.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_fwhm.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_limitmag.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_objnum.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_timeneed.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_track.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);

      tfilename = "M" + String.format("%02d", i) + "_xyrms.jpg";
      tsf = new SyncFile();
      tsf.setFileName(tfilename);
      tsf.setIsSync(false);
      tsf.setIsSyncSuccess(false);
      tsf.setPath("realTimeOtDistribution");
      tsf.setStoreTime(new Date());
      sfDao.save(tsf);
    }
  }

  /**
   * @param sfDao the sfDao to set
   */
  public void setSfDao(SyncFileDao sfDao) {
    this.sfDao = sfDao;
  }

  /**
   * @param rootDir the rootDir to set
   */
  public void setRootDir(String rootDir) {
    this.rootDir = rootDir;
  }

  /**
   * @param statusImageLDir the statusImageLDir to set
   */
  public void setStatusImageLDir(String statusImageLDir) {
    this.statusImageLDir = statusImageLDir;
  }

  /**
   * @param serverUrl the serverUrl to set
   */
  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
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
