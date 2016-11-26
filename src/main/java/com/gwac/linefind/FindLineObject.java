/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 *
 * @author xy
 */
public class FindLineObject {

  int imgWidth = 3072;
  int imgHeight = 3072;
  ArrayList<OT1> ot1list = new ArrayList();

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    FindLineObject fmo = new FindLineObject();
    fmo.findMovingObject();
//    fmo.test3();
  }

  public void findMovingObject() {
    int thetaSize = 180;
    int rhoSize = 100;
    int thetaRange = 36;
    int rhoRange = 10;
    int maxHoughFrameNunmber = 10;
    int validLineMinPoint = 5;
    float maxDistance = 100;
    int minValidPoint = 3;
    float rhoErrorTimes = (float) 0.2; //0.2

//    String[] dates = {"151218-2-34","151218-3-5", "151218-3-36", "151218-6-12",
//      "151218-6-13", "151218-7-15", "151218-8-15", "151218-9-21", "151218-11-34"};
    //debug2line  151218-2-34 debug-line-114-120 160928-12-5
    String[] dates = {"160928-1-5", "160928-3-10", "160928-5-11", "160928-6-11", "160928-7-12",
      "160928-7-16", "160928-8-12", "160928-8-16", "160928-11-5", "160928-12-5", "160928-1-5"};
//    String[] dates = {"160928-12-5"};
//    String[] dates = {"160928-5-11"};

    for (String tname : dates) {
      ot1list.clear();
      System.out.println("process " + tname);

      HoughTransform ht = new HoughTransform(imgWidth, imgHeight, thetaSize, rhoSize, thetaRange, rhoRange, maxHoughFrameNunmber, minValidPoint, maxDistance, rhoErrorTimes, validLineMinPoint);

      String ot1File = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\160928-source-list\\" + tname + ".txt";
      String outImage = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\160928-source-list\\" + tname + "-outline-all-speed.png";
      String outImage2 = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\160928-source-list\\" + tname + "-outline-singleframe.png";
      String outImagePoint = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\160928-source-list\\" + tname + "-point-all.png";
      String houghImage = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\" + tname + "\\hough.png";
      String outPath = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\160928-source-result\\" + tname + "\\";

      getOT1(ot1File);
//      drawPoint(outImagePoint);

      int lastFrameNumber = 0;
      int frameCount = 0;
      int pNum = 0;
      for (OT1 ot1 : ot1list) {
        if (lastFrameNumber != ot1.getFrameNumber()) {
          lastFrameNumber = ot1.getFrameNumber();
          ht.endFrame();
        }
//        ht.historyAddPoint(ot1);
//        ht.lineAddPoint(ot1);

        pNum++;
      }

      ht.endAllFrame();
      ht.saveLine2(outPath);

//      DrawObject dObj = new DrawObject(ht);
//      dObj.drawObjsAll(outImage);
    }
  }

  public void getOT1(String ot1file) {

    File file = new File(ot1file);
    BufferedReader reader = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;
      int tline = 0;
      int tline2 = 0;
      //2318.9	381.724	170.284	4.01298	2015/12/18 18:25:28	10.7607	1777	34	1626.55	2273.18
      while ((tempString = reader.readLine()) != null) {
        if (tempString.isEmpty()) {
          continue;
        }
        String[] tstr = tempString.split("\t");
        float x = Float.parseFloat(tstr[0]);
        float y = Float.parseFloat(tstr[1]);  //1
        float ra = Float.parseFloat(tstr[2]); //2
        float dec = Float.parseFloat(tstr[3]);
        float mag = Float.parseFloat(tstr[5]);
        int number = Integer.parseInt(tstr[6]);
//        float xTemp = Float.parseFloat(tstr[8]);
//        float yTemp = Float.parseFloat(tstr[9]);
        float xTemp = 0;
        float yTemp = 0;
        Date tdate = null;
        if (tstr[4].length() == "2016/9/28 13:49:37".length()) {
          tdate = sdf.parse(tstr[4]);
        } else {
          tdate = sdf2.parse(tstr[4]);
        }

//        if (!(x > 2345 && x < 2370 && y > 715 && y < 750)) {
        ot1list.add(new OT1(number, x, y, xTemp, yTemp, ra, dec, mag, tdate, tstr[4]));
        tline++;
//        } else {
//          tline2++;
//        }
      }
      reader.close();
//      System.out.println("total points:" + tline + ", remove:" + tline2);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException ex) {
      Logger.getLogger(FindLineObject.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
  }

  public void drawPoint(String fName) {

    BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    BasicStroke bs = new BasicStroke(2);
    g2d.setBackground(Color.WHITE);
    g2d.fillRect(0, 0, imgWidth, imgHeight);
    g2d.setStroke(bs);
    g2d.setColor(Color.RED);
    int pointSize2 = 6;

    for (OT1 ot1 : ot1list) {
      int x = (int) (ot1.getX() - pointSize2 / 2);
      int y = (int) (ot1.getY() - pointSize2 / 2);
      g2d.drawRect(x, y, pointSize2, pointSize2);
    }

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(HoughTransform.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void test() {

    ArrayList<PointTest> pointList = new ArrayList();

    for (int x = -10; x <= 10; x++) {
      double y = -1.732 * x + 1;
      pointList.add(new PointTest(x, y));
    }

    for (int i = 0; i < pointList.size() - 1; i++) {
      PointTest tPoint = pointList.get(i);
      tPoint.getKTheta(pointList.get(i + 1));
      tPoint.printInfo();
    }

  }

  public void test2() {
    String[] dates = {"debug2line"}; //debug2line  151218-2-34
    String ot1File = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\source-list-old\\debug2line.txt";
    getOT1(ot1File);

    for (int i = 0; i < ot1list.size() - 1; i++) {
      OT1 ot1 = ot1list.get(i);
      OT1 ot2 = ot1list.get(i + 1);
      float rho = -1;
      float theta = 0;
      if (i <= 9) {
        theta = 80;
        rho = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, (float) (Math.PI * theta / 180));
      } else if (i >= 14 && i <= 21) {
        theta = 1;
        rho = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, (float) (Math.PI * theta / 180));
      } else if (i <= 30) {
        theta = 0;
        rho = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, (float) (Math.PI * theta / 180));
      } else if (i <= 44) {
        theta = 0;
        rho = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, (float) (Math.PI * theta / 180));
      } else if (i <= 54) {
        theta = 1;
        rho = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, (float) (Math.PI * theta / 180));
      } else if (i <= 64) {
        theta = 0;
        rho = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, (float) (Math.PI * theta / 180));
      } else if (i <= 71) {
        theta = 0;
        rho = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, (float) (Math.PI * theta / 180));
      }

      double xDelta = ot2.getX() - ot1.getX();
      double yDelta = ot2.getY() - ot1.getY();
      float ktheta = (float) (Math.atan2(yDelta, xDelta));

      if (ktheta < 0) {
        ktheta += Math.PI;
      }

      float theta2 = 0;
      if (ktheta < Math.PI / 2) {
        theta2 = (float) (ktheta + Math.PI / 2);
      } else {
        theta2 = (float) (ktheta - Math.PI / 2);
      }

//      float rho2 = (float) ((ot1.getX()) * Math.cos(theta2) + (ot1.getY()) * Math.sin(theta2));
      float rho2 = getRho(ot1.getX(), ot1.getY(), imgWidth / 2, imgWidth / 2, theta2);
      System.out.println(String.format("%4d %5d %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f",
              i, ot1.getFrameNumber(), ot1.getX(), ot1.getY(), rho, rho2,
              theta, ktheta * 180 / Math.PI, theta2 * 180 / Math.PI));
    }
  }

  public void test3() {

    int thetaSize = 180;
    int rhoSize = 2000;
    int thetaRange = 36;
    int rhoRange = 10;
    int maxHoughFrameNunmber = 10;
    int minValidPoint = 5;
    float maxDistance = 100;
    float rhoErrorTimes = (float) 1;
    int validLineMinPoint = 5;

    String ot1File = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\source-list-old\\debug2line.txt";
    String houghImage = "E:\\work\\program\\java\\netbeans\\JavaApplication2\\resources\\debug2line\\hough.png";
    getOT1(ot1File);

    HoughTransformOrig ht = new HoughTransformOrig(imgWidth, imgHeight, thetaSize, rhoSize, thetaRange, rhoRange, maxHoughFrameNunmber, minValidPoint, maxDistance, rhoErrorTimes, validLineMinPoint);

    for (int i = 0; i < ot1list.size(); i++) {
//      if (i > 10) {
//        break;
//      }
      OT1 ot1 = ot1list.get(i);
      ht.houghAddPoint(ot1);
    }
    ht.drawHoughImage(houghImage);
  }

  public float getRho(float x, float y, float cenX, float cenY, float theta) {
    float rho = (float) ((((x - cenX) * Math.cos(theta)) + ((y - cenY) * Math.sin(theta)) + 2172.232));
    return rho;
  }
}
