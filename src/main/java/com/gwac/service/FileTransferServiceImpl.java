/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class FileTransferServiceImpl implements FileTransferService {

  private static final Log log = LogFactory.getLog(FileTransferServiceImpl.class);

  private static boolean running = true;

  public void transFile() {

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
