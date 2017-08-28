/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
 * 通过监控文件状态的改变，来将状态改变的文件进行同步，目前未实现
 *
 * @author xy
 */
@Service(value = "fileTransferService")
public class FileTransferServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FileTransferServiceImpl.class);
  private static boolean running = true;
  
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  private WatchService watcher;
  private Boolean isSuccess = false;

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
//    transFile();
    long endTime = System.nanoTime();

    if (running == false) {
      running = true;
      log.debug("job is done.");
    }
    log.debug("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

  void test() {
    try {
      System.out.println("123");
      watcher = FileSystems.getDefault().newWatchService();
      Path dir = Paths.get("E:/TestData/gwacTest");
      dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
      System.out.println("Watch Service registered for dir: " + dir.getFileName());
      isSuccess = true;
    } catch (IOException ex) {
      isSuccess = false;
      ex.printStackTrace();
    }
  }

  public void transFile() {
    System.out.println("123");
    try {
      System.out.println("123");
      watcher = FileSystems.getDefault().newWatchService();
      Path dir = Paths.get("E:/TestData/gwacTest");
      dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
      System.out.println("Watch Service registered for dir: " + dir.getFileName());
      isSuccess = true;
    } catch (IOException ex) {
      isSuccess = false;
      ex.printStackTrace();
    }

    if (isBeiJingServer || !isSuccess) {
      return;
    }

    if (running == true) {
      log.debug("start job fileTransferJob...");
      running = false;
    } else {
      log.warn("job fileTransferJob is running, jump this scheduler.");
      return;
    }
    try {
      WatchKey key = watcher.poll();
      if (key != null) {
        for (WatchEvent<?> event : key.pollEvents()) {
          WatchEvent.Kind<?> kind = event.kind();
          WatchEvent<Path> ev = (WatchEvent<Path>) event;
          Path fileName = ev.context();
          System.out.println(kind.name() + ": " + fileName);

          if (kind == ENTRY_MODIFY) {
            System.out.println("My source file has changed!!!");
          }
        }
      }

      boolean valid = key.reset();
      if (!valid) {
        return;
      }
    } catch (Exception ex) {
    }

    if (running == false) {
      running = true;
      log.debug("job fileTransferJob is done.");
    }
  }

  public void transFile3() {

    if (isBeiJingServer) {
      return;
    }

    if (running == true) {
      log.info("start job fileTransferJob...");
      running = false;
    } else {
      log.info("job fileTransferJob is running, jump this scheduler.");
      return;
    }

    String s = null;

    try {
      String command = "curl  http://159.226.88.94:8077/gwac/realTimeOtDstImageUpload.action "
              + "-F fileUpload=@/home/gwac/data/m1_01_140624_200060_0000_0011.fits";
      log.info("execute command:");
      log.info(command);

      long startMili = System.currentTimeMillis();
      Process p = Runtime.getRuntime().exec(command);
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      log.info("command return result:");
      while ((s = stdInput.readLine()) != null) {
        log.info(s);
      }

      log.info("command return error (if any):");
      while ((s = stdError.readLine()) != null) {
        log.info(s);
      }
      p.waitFor();
      long endMili = System.currentTimeMillis();
      double speed = 1.07 * 1000 / (endMili - startMili);
      log.info("total consume time: " + (endMili - startMili) / 1000 + "s");
      log.info("transmit speed is: " + speed + "MB/s");

      Calendar c1 = Calendar.getInstance();
      c1.setTime(new Date());
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");

      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/home/gwac/transferRecord.log", true)));
      out.println(format.format(c1.getTime()) + "\t" + speed);
      out.close();

    } catch (Exception e) {
      log.info("exception happened - here");
      e.printStackTrace();
    }

    if (running == false) {
      running = true;
      log.info("job fileTransferJob is done.");
    }
  }

}
