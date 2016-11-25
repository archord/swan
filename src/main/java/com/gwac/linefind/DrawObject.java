/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xy
 */
public class DrawObject {

  private final int colorLength = 1000;
  private final Color colors[]; //colors array for draw line on Descartes Coordinate 
  private final HoughTransform ht;
  private int drawIdx;
  private ArrayList<Integer> idxList;

  public DrawObject(HoughTransform ht) {

    this.drawIdx = 0;
    this.ht = ht;
    colors = new Color[colorLength];
    Random random = new Random();
    for (int i = 0; i < colorLength; i++) {
      colors[i] = new Color(100 + random.nextInt(156), 100 + random.nextInt(156), 100 + random.nextInt(156));
//      colors[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
  }

  public void drawObjsAll(String fName) {

    Integer idxArray[] = {13, 20, 25, 27};
    idxList = new ArrayList(Arrays.asList(idxArray));

//    BufferedImage image = new BufferedImage(imgWidth*2, imgHeight*2, BufferedImage.TYPE_INT_ARGB);
    BufferedImage image = new BufferedImage(ht.imgWidth, ht.imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
//    g2d.translate(0, imgHeight);
//    g2d.scale(1, -1);
    g2d.setBackground(Color.WHITE);
    g2d.fillRect(0, 0, ht.imgWidth, ht.imgHeight);
    g2d.drawOval(ht.imgWidth - 40, ht.imgHeight - 40, 20, 20);

    drawObjs(ht.mvObjs, g2d);
    drawObjs(ht.fastObjs, g2d);
    drawObjs(ht.singleFrameObjs, g2d);

    int singleFrame = ht.singleFrameObjs.size();
    int fastObjNum = ht.fastObjs.size();
    int singlePoint = 0;
    int multiPoint = 0;
    for (LineObject mvObj : ht.mvObjs) {
      if (mvObj.pointNumber < ht.validLineMinPoint) {
        continue;
      }
      if (mvObj.avgFramePointNumber < 1.2) {
        singlePoint++;
      } else {
        multiPoint++;
      }
    }
    System.out.println("totalLine=" + (singleFrame + singlePoint + multiPoint + fastObjNum)
            + ", singleFrame=" + singleFrame + ", fastObjNum=" + fastObjNum + ", singlePoint="
            + singlePoint + ", multiPoint=" + multiPoint);

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(HoughTransform.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void drawObjs(ArrayList<LineObject> lineObjs, Graphics2D g2d) {

    BasicStroke bs = new BasicStroke(2);
    BasicStroke bs2 = new BasicStroke(4);
    BasicStroke bs3 = new BasicStroke(7);
    Font font1 = new Font("Times New Roman", Font.BOLD, 30);
    Font font2 = new Font("Times New Roman", Font.BOLD, 12);

    int pointSize = 12;
    int pointSize2 = 6;

    for (LineObject mvObj : lineObjs) {

      // || !(mvObj.avgFramePointNumber>1.1&&mvObj.frameList.size()>1)  || mvObj.frameList.size() <= 1   || mvObj.frameList.size() <= 20
      if ((mvObj.pointNumber < ht.validLineMinPoint) || (mvObj.avgFramePointNumber < 1.5 && mvObj.frameList.size() < 5)) {
        this.drawIdx++;
        continue;
      }

//      if (!(idxList.contains(new Integer(this.drawIdx)))) {
//        this.drawIdx++;
//        continue;
//      }

      HoughtPoint firstOT1 = mvObj.firstPoint;
      HoughtPoint lastOT1 = mvObj.lastPoint;

      int x1 = (int) firstOT1.getX();
      int y1 = (int) firstOT1.getY();
      int x2 = (int) lastOT1.getX();
      int y2 = (int) lastOT1.getY();

      if (mvObj.frameList.size() == 1) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(bs3);
      } else if (mvObj.avgFramePointNumber > 2) {
        g2d.setColor(Color.RED);
        g2d.setStroke(bs2);
      } else {
        g2d.setColor(colors[this.drawIdx % colorLength]);
        g2d.setStroke(bs);
      }

      g2d.drawLine(x1, y1, x2, y2);
      g2d.setStroke(bs3);
      g2d.drawRect(x1 - pointSize / 2, y1 - pointSize / 2, pointSize, pointSize);
      g2d.drawRect(x2 - pointSize / 2, y2 - pointSize / 2, pointSize, pointSize);

      String drawStr = "";
      g2d.setStroke(bs2);

      for (HoughFrame tFrame : mvObj.frameList) {
        for (HoughtPoint tPoint : tFrame.pointList) {
          g2d.setColor(colors[this.drawIdx % colorLength]);
          int x = (int) (tPoint.getX() - pointSize2 / 2);
          int y = (int) (tPoint.getY() - pointSize2 / 2);
          g2d.drawRect(x, y, pointSize2, pointSize2);
        }
      }

      g2d.setColor(Color.BLACK);
      g2d.setFont(font1);
//      drawStr = "" + (j) + "," + (mvObj.lastPoint.getFrameNumber());
      drawStr = "" + (this.drawIdx);
      g2d.drawString(drawStr, (int) lastOT1.getX() + pointSize, (int) lastOT1.getY() + pointSize);

//      String debugStr = String.format("line%03d: theta=%6.2f Deg, theta=%4.2f arc, theta=%6.2f, "
//              + "rho=%10.5f, rho=%10.5f, lastRho=%10.5f, lastRho=%10.5f, pnumber=%3d, fnumber=%3d, avgfNumber=%5.2f",
//              this.drawIdx, mvObj.theta * 180 / Math.PI, mvObj.theta, mvObj.theta / ht.thetaStep,
//              mvObj.rho / ht.rhoStep, mvObj.rho, mvObj.lastRho / ht.rhoStep, mvObj.lastRho,
//              mvObj.pointNumber, mvObj.frameList.size(), mvObj.avgFramePointNumber);
//      System.out.println(debugStr);
//      mvObj.updateThetaRho();
//      if (idxList.contains(this.drawIdx)) {
//        System.out.println("frameSize=" + mvObj.frameList.size());
//      }
//      mvObj.calAllSpeed();
//
//      System.out.println("pIdx\t frameNumber\t x\t y\t xDelta\t yDelta\t fnDelta\t timeDelta\t xSpeedfn\t ySpeedt\t preX\t preY\t preDeltaX\t preDeltaY\n");
//      mvObj.printInfo2();
//      mvObj.printOT1Info(ht.historyOT1s);

      this.drawIdx++;
    }
  }

  public void drawPoint2(String fName) {

    int colorLength = 1000;
    Color colors[] = new Color[colorLength];
    Random random = new Random();
    for (int i = 0; i < colorLength; i++) {
//      colors[i] = new Color(100 + random.nextInt(156), 100 + random.nextInt(156), 100 + random.nextInt(156));
      colors[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    BufferedImage image = new BufferedImage(ht.imgWidth, ht.imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    BasicStroke bs = new BasicStroke(2);
    BasicStroke bs2 = new BasicStroke(3);
    BasicStroke bs3 = new BasicStroke(7);
    Font font1 = new Font("Times New Roman", Font.BOLD, 30);
    Font font2 = new Font("Times New Roman", Font.BOLD, 12);
//    g2d.translate(0, imgHeight);
//    g2d.scale(1, -1);
    g2d.setBackground(Color.WHITE);
    g2d.fillRect(0, 0, ht.imgWidth, ht.imgHeight);
    g2d.setStroke(bs);
    g2d.setColor(Color.RED);
    g2d.drawOval(ht.imgWidth - 40, ht.imgHeight - 40, 20, 20);

    int pointSize = 12;
    int pointSize2 = 6;
    // draw the lines back onto the image 
    int totalLine = 0;

    Integer idxArray[] = {66, 88};
    ArrayList<Integer> idxList = new ArrayList(Arrays.asList(idxArray));

    int j = 0;
    for (LineObject mvObj : ht.mvObjs) {

      if (mvObj.pointNumber < ht.validLineMinPoint) {
        continue;
      }

//      if (!(idxList.contains(new Integer(j)))) {
//        j++;
//        continue;
//      }
      g2d.setColor(colors[j % colorLength]);

      HoughtPoint firstOT1 = mvObj.firstPoint;
      HoughtPoint lastOT1 = mvObj.lastPoint;

      int x1 = (int) firstOT1.getX();
      int y1 = (int) firstOT1.getY();
      int x2 = (int) lastOT1.getX();
      int y2 = (int) lastOT1.getY();
      g2d.setStroke(bs);
//      g2d.drawLine(x1, y1, x2, y2);
      g2d.setStroke(bs3);
      g2d.drawRect(x1 - pointSize / 2, y1 - pointSize / 2, pointSize, pointSize);
      g2d.drawRect(x2 - pointSize / 2, y2 - pointSize / 2, pointSize, pointSize);

      String drawStr = "";

      g2d.setFont(font2);
      drawStr = "" + firstOT1.getpIdx();
      g2d.drawString(drawStr, (int) x1 + pointSize, (int) y1 - pointSize);
      drawStr = "" + lastOT1.getpIdx();
      g2d.drawString(drawStr, (int) x2 + pointSize, (int) y2 - pointSize);

      g2d.setStroke(bs2);

      for (HoughFrame tFrame : mvObj.frameList) {
        for (HoughtPoint tPoint : tFrame.pointList) {
          g2d.setColor(colors[j % colorLength]);
          int x = (int) (tPoint.getX() - pointSize2 / 2);
          int y = (int) (tPoint.getY() - pointSize2 / 2);
          g2d.drawRect(x, y, pointSize2, pointSize2);

          g2d.setFont(font2);
          drawStr = "" + tPoint.getpIdx();
          g2d.drawString(drawStr, (int) x + pointSize, (int) y - pointSize);
        }
      }
      g2d.setColor(Color.BLACK);
      g2d.setFont(font1);
      drawStr = "" + (j);
      g2d.drawString(drawStr, (int) lastOT1.getX() + 3 * pointSize, (int) lastOT1.getY() + pointSize);

      String debugStr = String.format("line%03d: theta=%6.2f Deg, theta=%4.2f arc, theta=%6.2f, "
              + "rho=%10.5f, rho=%10.5f, lastRho=%10.5f, lastRho=%10.5f, pnumber=%3d, fnumber=%3d, avgfNumber=%5.2f",
              j, mvObj.theta * 180 / Math.PI, mvObj.theta, mvObj.theta / ht.thetaStep,
              mvObj.rho / ht.rhoStep, mvObj.rho, mvObj.lastRho / ht.rhoStep, mvObj.lastRho,
              mvObj.pointNumber, mvObj.frameList.size(), mvObj.avgFramePointNumber);
      System.out.println(debugStr);
//      if (idxList.contains(j)) {
      mvObj.printInfo2();
//      }

      j++;
    }

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(HoughTransform.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
