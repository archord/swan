/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 数据同步 根据传输配置文件，将一级OT列表，切图，模板切图等传输到另一个服务器
 *
 * @author xy
 */
@Service(value = "dataSyncService")
public class DataSyncServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(DataSyncServiceImpl.class);
  private static boolean running = true;
  private static long runCount = 0;

  @Resource
  private FitsFileCutDAO ffcDao;
  @Resource
  private FitsFileCutRefDAO ffcrDao;
  @Value("#{syscfg.gwacDataRootDirectory}")
  private String rootDir;
  @Value("#{syscfg.gwacServerUrlNaoc}")
  private String serverUrl;
  @Value("#{syscfg.gwacServerUploadimageUrl}")
  private String uploadUrl;
  @Value("#{syscfg.gwacMachineNumber}")
  private int mchNum;

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  @Override
  public void startJob() {

    if (isBeiJingServer || isTestServer) {
      return;
    }

    if (running == true) {
      log.debug("start sync job...");
      running = false;
    } else {
      log.warn("job is running, jump this scheduler.");
      return;
    }

    long startTime = System.nanoTime();

    try {
      uploadFfc();
      uploadFfcr();
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }

    }
    runCount++;
    long endTime = System.nanoTime();
    log.debug("run " + runCount + "th, job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

  public void uploadFfc() {

    try {
      List<FitsFileCut> objs = ffcDao.getUnSyncList(10);

      if (!objs.isEmpty()) {
        int fNum = 0;
        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
        for (FitsFileCut obj : objs) {

          String tpath1 = rootDir + "/" + obj.getStorePath() + "/" + obj.getFileName();
          if (tpath1.indexOf('.') == -1) {
            tpath1 += ".fit";
            String tpath2 = rootDir + "/" + obj.getStorePath() + "/" + obj.getFileName() + ".jpg";
            File tfile2 = new File(tpath2);
            if (tfile2.exists()) {
              fNum++;
              mpEntity.addPart("filePaths", new StringBody(obj.getStorePath(), ContentType.TEXT_PLAIN));
              mpEntity.addPart("files", new FileBody(tfile2));
            } else {
              log.warn(tfile2.getAbsolutePath() + " not exist!");
            }
          }
          File tfile1 = new File(tpath1);
          if (tfile1.exists()) {
            fNum++;
            mpEntity.addPart("filePaths", new StringBody(obj.getStorePath(), ContentType.TEXT_PLAIN));
            mpEntity.addPart("files", new FileBody(tfile1));
          } else {
            log.warn(tfile1.getAbsolutePath() + " not exist!");
          }
        }

        if (doUpload(mpEntity)) {
          for (FitsFileCut obj : objs) {
            ffcDao.updateIsRecvOk(obj.getFfcId());
          }
        }
        log.debug("obj: " + objs.size() + ", add ffc files: " + fNum);
      }
    } catch (Exception ex) {
      log.error("upload ffc error:", ex);
    }
  }

  public void uploadFfcr() {

    try {
      List<FitsFileCutRef> objs = ffcrDao.getUnSyncList(10);

      if (!objs.isEmpty()) {
        int fNum = 0;
        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
        for (FitsFileCutRef obj : objs) {

          String tpath1 = rootDir + "/" + obj.getStorePath() + "/" + obj.getFileName() + ".jpg";
          File tfile1 = new File(tpath1);
          if (tfile1.exists()) {
            fNum++;
            mpEntity.addPart("filePaths", new StringBody(obj.getStorePath(), ContentType.TEXT_PLAIN));
            mpEntity.addPart("files", new FileBody(tfile1));
          } else {
            log.warn(tfile1.getAbsolutePath() + " not exist!");
          }
          
          String tpath2 = rootDir + "/" + obj.getStorePath() + "/" + obj.getFileName() + ".fit";
          File tfile2 = new File(tpath2);
          if (tfile2.exists()) {
            fNum++;
            mpEntity.addPart("filePaths", new StringBody(obj.getStorePath(), ContentType.TEXT_PLAIN));
            mpEntity.addPart("files", new FileBody(tfile2));
          } else {
            log.warn(tfile2.getAbsolutePath() + " not exist!");
          }
        }

        if (fNum > 0 && doUpload(mpEntity)) {
          for (FitsFileCutRef obj : objs) {
            ffcrDao.updateIsRecvOk(obj.getFfcrId());
          }
          log.debug("obj: " + objs.size() + ", add ffcr files: " + fNum);
        } else {
          log.debug("cannot find any file.");
        }
      }
    } catch (Exception ex) {
      log.error("upload ffc error:", ex);
    }
  }

  public boolean doUpload(MultipartEntityBuilder mpEntity) {

    boolean flag = false;

    HttpEntity reqEntity = mpEntity.build();
    HttpPost httppost = new HttpPost(serverUrl + uploadUrl);
    httppost.setEntity(reqEntity);

    CloseableHttpClient httpclient = null;
    CloseableHttpResponse response = null;
    try {
      httpclient = HttpClients.createDefault();
      response = httpclient.execute(httppost);
      log.debug(response.getStatusLine());
      HttpEntity resEntity = response.getEntity();
      if (resEntity != null) {
        String rstContent = IOUtils.toString(resEntity.getContent());
        log.debug("response content: " + rstContent);
        if (rstContent.contains("Success")) {
          flag = true;
        }
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
        log.error("close httpclient error", ex);
      }
    }
    return flag;
  }

}
