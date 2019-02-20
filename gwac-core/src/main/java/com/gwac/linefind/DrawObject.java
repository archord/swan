/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecordMovObj;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
  private final FindMoveObject ht;
  private int drawIdx;
  private ArrayList<Integer> idxList;
  private int outNum = 0;

  public DrawObject(FindMoveObject ht) {

    this.drawIdx = 0;
    this.ht = ht;
    colors = new Color[colorLength];
    Random random = new Random();
    for (int i = 0; i < colorLength; i++) {
      colors[i] = new Color(100 + random.nextInt(156), 100 + random.nextInt(156), 100 + random.nextInt(156));
//      colors[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
  }

  public void drawObjsAll(String fName, char lineType) {

    this.drawIdx = 0;
//    Integer idxArray[] = {57, 75, 99, 108, 113};
//    Integer idxArray[] = {41}; // 
    Integer idxArray[] = {};
    idxList = new ArrayList(Arrays.asList(idxArray));

//    BufferedImage image = new BufferedImage(imgWidth*2, imgHeight*2, BufferedImage.TYPE_INT_ARGB);
    BufferedImage image = new BufferedImage(ht.imgWidth, ht.imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
//    g2d.translate(0, imgHeight);
//    g2d.scale(1, -1);
    g2d.setBackground(Color.WHITE);
    g2d.fillRect(0, 0, ht.imgWidth, ht.imgHeight);
    g2d.drawOval(ht.imgWidth - 40, ht.imgHeight - 40, 20, 20);

    drawObjs(ht.mvObjs, g2d, lineType);
    System.out.println("totalLine=" + ht.mvObjs.size() + ", draw line=" + outNum);

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void drawObjs(ArrayList<LineObject> lineObjs, Graphics2D g2d, char lineType) {

    BasicStroke bs = new BasicStroke(2);
    BasicStroke bs2 = new BasicStroke(4);
    BasicStroke bs3 = new BasicStroke(7);
    Font font1 = new Font("Times New Roman", Font.BOLD, 30);
    Font font2 = new Font("Times New Roman", Font.BOLD, 12);

    int pointSize = 12;
    int pointSize2 = 6;

    for (LineObject mvObj : lineObjs) {

      if (mvObj.pointNumber < ht.validLineMinPoint) {
        this.drawIdx++;
        continue;
      }

//      if (!mvObj.isValidLine()) {
//        this.drawIdx++;
//        continue;
//      }
//      if ((mvObj.avgFramePointNumber > 2)) {
//        this.drawIdx++;
//        continue;
//      }
      if ((mvObj.lineType != lineType)) {
        this.drawIdx++;
        continue;
      }
      
//      if (mvObj.xySigma < 5) {
//        this.drawIdx++;
//        continue;
//      }
      
//      if (mvObj.pointNumber < 10 || !(mvObj.xySigma < 1.1 && mvObj.tySigma < 1.1 && mvObj.txSigma < 1.1)) {
//        this.drawIdx++;
//        continue;
//      }

      if (!idxList.isEmpty() && !(idxList.contains(new Integer(this.drawIdx)))) {
        this.drawIdx++;
        continue;
      }

      outNum++;

      HoughtPoint firstOT1 = mvObj.firstPoint;
      HoughtPoint lastOT1 = mvObj.lastPoint;

      int x1 = (int) firstOT1.getX();
      int y1 = (int) firstOT1.getY();
      int x2 = (int) lastOT1.getX();
      int y2 = (int) lastOT1.getY();
      String drawStr = "";

      if (mvObj.frameList.size() == 1) {
        g2d.setColor(Color.RED);
        g2d.setStroke(bs3);
        g2d.drawLine(x1, y1, x2, y2);
        drawStr = "" + (this.drawIdx);
        g2d.drawString(drawStr, (int) lastOT1.getX() + pointSize, (int) lastOT1.getY() + pointSize);
      } else if (mvObj.avgFramePointNumber > 2) {
        g2d.setColor(colors[this.drawIdx % colorLength]);
        g2d.setStroke(bs2);
        for (HoughFrame tFrame : mvObj.frameList) {
          HoughtPoint minObj;
          HoughtPoint maxObj;
          if (Math.abs(tFrame.deltaX) > Math.abs(tFrame.deltaY)) {
            minObj = tFrame.minX;
            maxObj = tFrame.maxX;
          } else {
            minObj = tFrame.minY;
            maxObj = tFrame.maxY;
          }
          x1 = (int) minObj.getX();
          y1 = (int) minObj.getY();
          x2 = (int) maxObj.getX();
          y2 = (int) maxObj.getY();
          g2d.drawLine(x1, y1, x2, y2);
          drawStr = "" + (this.drawIdx);
          g2d.drawString(drawStr, (int) x2 + pointSize, (int) y2 + pointSize);
        }
      } else {
        g2d.setColor(colors[this.drawIdx % colorLength]);
        g2d.setStroke(bs);
        g2d.drawLine(x1, y1, x2, y2);
        drawStr = "" + (this.drawIdx);
        g2d.drawString(drawStr, (int) lastOT1.getX() + pointSize, (int) lastOT1.getY() + pointSize);
      }

      g2d.setStroke(bs3);
      g2d.drawRect(x1 - pointSize / 2, y1 - pointSize / 2, pointSize, pointSize);
      g2d.drawRect(x2 - pointSize / 2, y2 - pointSize / 2, pointSize, pointSize);

      g2d.setStroke(bs2);

      for (HoughFrame tFrame : mvObj.frameList) {
        for (HoughtPoint tPoint : tFrame.pointList) {
          g2d.setColor(colors[this.drawIdx % colorLength]);
          int x = (int) (tPoint.getX() - pointSize2 / 2);
          int y = (int) (tPoint.getY() - pointSize2 / 2);
          g2d.drawRect(x, y, pointSize2, pointSize2);
//          drawStr = "" + (tPoint.getFrameNumber());
//          g2d.drawString(drawStr, (int) x + pointSize, (int) y + pointSize);
        }
      }

      g2d.setColor(Color.BLACK);
      g2d.setFont(font1);
//      drawStr = "" + (j) + "," + (mvObj.lastPoint.getFrameNumber());

//      String debugStr = String.format("line%03d: %s", this.drawIdx, mvObj.getOutLineInfo());
//      System.out.println(debugStr);
//      System.out.println("movObj record list\n");
//      mvObj.printOT1Info(ht.historyOT1s);
//      mvObj.printInfo2();
      this.drawIdx++;

    }
  }

  /**
   * 画出星表的点位图
   *
   * @param fName
   */
  public void drawPoint(String fName) {

    BufferedImage image = new BufferedImage(ht.imgWidth, ht.imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    BasicStroke bs = new BasicStroke(2);
    g2d.setBackground(Color.WHITE);
    g2d.fillRect(0, 0, ht.imgWidth, ht.imgHeight);
    g2d.setStroke(bs);
    g2d.setColor(Color.RED);
    int pointSize2 = 6;

    for (OtObserveRecordMovObj ot1 : ht.historyOT1s) {
      int x = (int) (ot1.getX() - pointSize2 / 2);
      int y = (int) (ot1.getY() - pointSize2 / 2);
      g2d.drawRect(x, y, pointSize2, pointSize2);
    }

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void drawPointNotInLine(String fName) {

    BufferedImage image = new BufferedImage(ht.imgWidth, ht.imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    BasicStroke bs = new BasicStroke(2);
    g2d.setBackground(Color.WHITE);
    g2d.fillRect(0, 0, ht.imgWidth, ht.imgHeight);
    g2d.setStroke(bs);
    g2d.setColor(Color.RED);
    int pointSize2 = 6;

    Iterator tIter = ht.notInLine.iterator();
    while (tIter.hasNext()) {
      OtObserveRecordMovObj ot1 = ht.historyOT1s.get((int) tIter.next());
      int x = (int) (ot1.getX() - pointSize2 / 2);
      int y = (int) (ot1.getY() - pointSize2 / 2);
      g2d.drawRect(x, y, pointSize2, pointSize2);
    }

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
