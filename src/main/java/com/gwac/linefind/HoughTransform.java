/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecord;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xy
 */
public class HoughTransform {

  // cache of values of sin and cos for different theta values. Has a significant performance improvement. 
  private double[] sinCache;
  private double[] cosCache;

  // the number of points that have been added 
  protected int numOT1s;
  HoughLine[][] houghArray;
  ArrayList<OtObserveRecord> historyOT1s;
  private ArrayList<LineObject> mvObjs;
  private ArrayList<LineObject> fastObjs;
  private ArrayList<LineObject> singleFrameObjs;

  int maxHoughFrameNunmber;
  int minValidPoint;
  int minValidFrame = 2; //完成第minValidFrame帧的添加之后，在添加下一帧的第一个点之前，计算所有直线的速度

  int imgWidth;
  int imgHeight;
  float imgXCenter;
  float imgYCenter;

  double maxTheta;
  double maxRho;
  double halfRho;
  int thetaSize;
  int rhoSize;
  int thetaRange;
  int rhoRange;
  double thetaStep;
  double rhoStep;

  float maxDistance;
  float rhoErrorTimes;
  int validLineMinPoint;

  /**
   *
   * @param imgWidth 图像宽
   * @param imgHeight 图像高
   * @param thetaSize hough变换中，theta取值个数
   * @param rhoSize hough变换中，rho取值个数
   * @param thetaRange 在hough变化矩阵中，搜寻极大值时，theta方向的搜寻半径
   * @param rhoRange 在hough变化矩阵中，搜寻极大值时，rho方向的搜寻半径
   * @param maxHoughFrameNunmber
   * hough变换中，一条hough直线的中间能够缺失的最大帧数量；如果超过这个数量，则后续的点被处理为一条新的直线。
   * @param minValidPoint hough变换中，有效hough直线中最小有效点的个数
   * @param maxDistance
   * hough变换中，一条hough直线的中间能够缺失的最大直线距离；如果超过这个数量，则后续的点被处理为一条新的直线。
   * @param rhoErrorTimes
   * 取值最好不要大于1，一个新的点P，使用一条直线L（theta0,rho0）的theta0计算过P点的直线rho，如果abs(rho0-rho)小于rhoErrorTimes*rhoStep，则认为这个点P是直线L上的点。
   */
  public HoughTransform(int imgWidth, int imgHeight, int thetaSize, int rhoSize, int thetaRange, int rhoRange,
          int maxHoughFrameNunmber, int minValidPoint, float maxDistance, float rhoErrorTimes, int validLineMinPoint) {
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.maxTheta = Math.PI;
    this.thetaSize = thetaSize;
    this.rhoSize = rhoSize;
    this.thetaRange = thetaRange;
    this.rhoRange = rhoRange;
    this.maxHoughFrameNunmber = maxHoughFrameNunmber;
    this.minValidPoint = minValidPoint;
    this.maxDistance = maxDistance;
    this.rhoErrorTimes = rhoErrorTimes;
    this.validLineMinPoint = validLineMinPoint;

    this.imgXCenter = this.imgWidth / 2;
    this.imgYCenter = this.imgHeight / 2;
    this.maxRho = Math.sqrt(Math.pow(imgWidth, 2) + Math.pow(imgHeight, 2));
    this.halfRho = this.maxRho / 2;
    this.thetaStep = this.maxTheta / this.thetaSize;
    this.rhoStep = this.maxRho / this.rhoSize;

    this.historyOT1s = new ArrayList();
    this.mvObjs = new ArrayList();
    this.fastObjs = new ArrayList();
    this.singleFrameObjs = new ArrayList();

    initialise();
    this.rhoErrorTimes = rhoErrorTimes;
    this.validLineMinPoint = validLineMinPoint;

  }

  private void initialise() {
    houghArray = new HoughLine[thetaSize][rhoSize];
    // Count how many points there are 
    numOT1s = 0;

    // cache the values of sin and cos for faster processing 
    sinCache = new double[thetaSize];
    cosCache = sinCache.clone();
    for (int t = 0; t < thetaSize; t++) {
      double realTheta = t * thetaStep;
      sinCache[t] = Math.sin(realTheta);
      cosCache[t] = Math.cos(realTheta);
    }

    for (int t = 0; t < thetaSize; t++) {
      for (int r = 0; r < rhoSize; r++) {
        houghArray[t][r] = new HoughLine((float) (t * thetaStep), (float) (r * rhoStep), imgXCenter, imgYCenter, (float) halfRho);
//        houghArray[t][r] = new HoughLine((float) (t), (float) (r));
      }
    }

  }

  public void historyAddPoint(OtObserveRecord ot1) {
    historyOT1s.add(ot1);
    numOT1s++;
  }

  public void houghAddPoint(OtObserveRecord ot1) {

    for (int t = 0; t < thetaSize; t++) {

      float fr = (float) ((((ot1.getX() - imgXCenter) * cosCache[t]) + ((ot1.getY() - imgYCenter) * sinCache[t]) + halfRho) / rhoStep);
      int r = Math.round(fr);

      if (r < 0 || r >= this.rhoSize) {
        System.out.println("x=" + ot1.getX() + ",y=" + ot1.getY() + ",theta=" + t + ",rho=" + r);
        continue;
      }

      HoughLine tline = houghArray[t][r];
      int curNumber = ot1.getFfNumber();
      tline.removeOldFrame(curNumber - this.maxHoughFrameNunmber);

      if (tline.matchLastPoint(ot1, maxDistance)) {
        tline.addPoint(numOT1s - 1, curNumber, (float) (thetaStep * t), (float) (fr * rhoStep), ot1.getX(), ot1.getY(), ot1.getDateUt());
        if (tline.validSize() >= this.minValidPoint) {
          LineObject lineObj = new LineObject((float) (t * thetaStep), (float) (r * rhoStep), imgXCenter, imgYCenter, (float) halfRho);
          lineObj.cloneLine(tline);
          mvObjs.add(lineObj);
          clearAllPoint(lineObj);
          break;
        }
      }
    }

  }

  /**
   * 每添加一个点，都要同时更新直线的rho和theta，目标只更新了rho
   *
   * @param ot1
   */
  public void lineAddPoint(OtObserveRecord ot1) {

    boolean findLine = false;
    int i = 0;
    for (LineObject tmo : this.mvObjs) {

      if (!tmo.isEndLine(ot1.getFfNumber() - this.maxHoughFrameNunmber + 1)) {

        double trho = ((ot1.getX() - imgXCenter) * tmo.cosTheta + (ot1.getY() - imgYCenter) * tmo.sinTheta + halfRho); // / rhoStep
        if ((Math.abs(trho - tmo.lastRho) < rhoErrorTimes * this.rhoStep)) {  // 范围是不是太大  this.rhoStep
          boolean matchLastPoint = tmo.matchLastPoint2(ot1, maxDistance);
          if (matchLastPoint) {
            tmo.addPoint(numOT1s - 1, ot1.getFfNumber(), tmo.theta, (float) (trho), ot1.getX(), ot1.getY(), ot1.getDateUt(), ot1.getOorId());
            findLine = true;
            break;
          }
        }
      }
    }
    if (!findLine) {
      this.houghAddPoint(ot1);
    }
  }

  public void endFrame() {

  }

  public void endAllFrame() {

    for (LineObject tmo : this.mvObjs) {
      if (!tmo.endLine) {
        tmo.isEndLine(Integer.MAX_VALUE);
      }
      if (tmo.frameList.size() == 1) {
        singleFrameObjs.add(tmo);
      }
    }

    for (LineObject tmo : this.singleFrameObjs) {
      mvObjs.remove(tmo);
    }

    analysisSingleFrameObjs();
  }

  public void analysisSingleFrameObjs() {

    int rhoErrorTimes2 = 1;
    ArrayList<LineObject> tObjs = new ArrayList();

    while (singleFrameObjs.size() > 0) {

      LineObject tobj = singleFrameObjs.get(0);
      tobj.firstPoint.calKtheta(tobj.lastPoint, imgXCenter, imgYCenter, imgXCenter); //根据直线的第一个和最后一个点算的rho和theta
      singleFrameObjs.remove(0);

      int idx = 0;
      boolean isMatch = false;
      double lastFrameRho = tobj.firstPoint.getRho2();
      double lastFrameTheta = tobj.firstPoint.getTheta2();
      while (singleFrameObjs.size() > 0 && idx < singleFrameObjs.size()) {
        LineObject tobj2 = singleFrameObjs.get(idx);
        tobj2.firstPoint.calKtheta(tobj2.lastPoint, imgXCenter, imgYCenter, imgXCenter);

        double rhoSub = Math.abs(lastFrameRho - tobj2.firstPoint.getRho2());
        double thetaSub = Math.abs(lastFrameTheta - tobj2.firstPoint.getTheta2());
        int frameSub = Math.abs(tobj.lastFrameNumber - tobj2.firstFrameNumber);

        if ((rhoSub < rhoErrorTimes2 * this.rhoStep) && (thetaSub < rhoErrorTimes2 * this.thetaStep) && (frameSub < 5)) {
          lastFrameRho = tobj2.firstPoint.getRho2();
          lastFrameTheta = tobj2.firstPoint.getTheta2();
          tobj.addLineObject(tobj2);
          singleFrameObjs.remove(idx);
          isMatch = true;
        } else {
          idx++;
        }
      }
      if (isMatch) {
        fastObjs.add(tobj);
      } else {
        tObjs.add(tobj);
      }
    }

    singleFrameObjs.addAll(tObjs);
    tObjs.clear();

    for (int i = 0; i < fastObjs.size();) {
      LineObject tobj = fastObjs.get(i);
      if (tobj.frameList.size() == 1) {
        singleFrameObjs.add(tobj);
        fastObjs.remove(i);
      } else {
        i++;
      }
    }
  }

  public void clearAllPoint(LineObject tline) {

    for (HoughFrame tFrame : tline.frameList) {
      ArrayList<HoughtPoint> tPoints = (ArrayList<HoughtPoint>) tFrame.pointList;
      for (HoughtPoint tPoint : tPoints) {
//      System.out.println(String.format("target %5d %5d", tPoint.getpIdx(), ot1.getFrameNumber()));
        for (int t = 0; t < thetaSize; t++) {
          int r = Math.round((float) ((((tPoint.getX() - imgXCenter) * cosCache[t]) + ((tPoint.getY() - imgYCenter) * sinCache[t]) + halfRho) / rhoStep));
          HoughLine tline2 = houghArray[t][r];
          tline2.removePoint(tPoint);
        }
      }
    }
  }

  public void saveLine(String fpath) {

    File root = new File(fpath);
    if (root.exists()) {
      root.delete();
    }
    root.mkdirs();

    Integer idxArray[] = {14, 16, 60, 99, 100};
    ArrayList<Integer> idxList = new ArrayList(Arrays.asList(idxArray));

    int j = 0;
    for (LineObject mvObj : mvObjs) {

      if (mvObj.pointNumber < validLineMinPoint || mvObj.frameList.size() <= 20) {
        j++;
        continue;
      }

//      if (!(idxList.contains(new Integer(j)))) {
//        j++;
//        continue;
//      }
      mvObj.calAllSpeed();

      FileOutputStream out = null;
      try {
        String fname = fpath + String.format("%03d.txt", j);
        out = new FileOutputStream(new File(fname));
//        out.write("pIdx, frameNumber, x, y, xDelta, yDelta, fnDelta, timeDelta, xSpeedfn, ySpeedfn, xSpeedt, ySpeedt\n".getBytes());
        out.write("pIdx\t fmNum\t x\t y\t xDelta\t yDelta\t fnDelta\t timeDelta\t xSpeedt\t ySpeedt\t preX\t preY\t preDeltaX\t preDeltaY\n".getBytes());
        int i = 0;
        for (HoughtPoint tPoint : mvObj.pointList) {
//          OtObserveRecord tp = this.historyOT1s.get(tPoint.getpIdx());
//              out.write(String.format("%4d, %s, %10.6f, %10.6f, %11.6f, %11.6f, %6.3f\n", 
//                      tp.number, tp.dateStr, tp.ra, tp.dec, tp.x, tp.y, tp.mag).getBytes());
//          out.write(String.format("%4d\t%s\t%10.6f\t%10.6f\t%11.6f\t%11.6f\t%6.3f\n",
//                  tp.getFrameNumber(), tp.getDateStr(), tp.getRa(), tp.getDec(), tp.getX(), tp.getY(), tp.getMag()).getBytes());
//          System.out.println(tPoint.getAllInfo());
          out.write((tPoint.getAllInfo() + "\n").getBytes());
        }
        out.close();
      } catch (FileNotFoundException ex) {
        Logger.getLogger(HoughTransform.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(HoughTransform.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        try {
          out.close();
        } catch (IOException ex) {
          Logger.getLogger(HoughTransform.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      j++;
    }
  }

  public void saveLine2(String fpath) {

    File root = new File(fpath);
    if (!root.exists()) {
      root.mkdirs();
    }

    int j = 0;
    for (LineObject mvObj : mvObjs) {
      if (mvObj.pointNumber < validLineMinPoint) {
        j++;
        continue;
      }
      String fname = fpath + String.format("1f1p_%04d_%04d_%04d.txt", j, mvObj.frameList.size(), mvObj.pointNumber);
//      saveLineObj(mvObj, fname);
      j++;
    }

    j = 0;
    for (LineObject mvObj : fastObjs) {
      if (mvObj.pointNumber < validLineMinPoint) {
        j++;
        continue;
      }
      String fname = fpath + String.format("1fnp_%04d_%04d_%04d.txt", j, mvObj.frameList.size(), mvObj.pointNumber);
//      saveLineObj(mvObj, fname);
      j++;
    }

    j = 0;
    for (LineObject mvObj : singleFrameObjs) {
      if (mvObj.pointNumber < validLineMinPoint) {
        j++;
        continue;
      }
      String fname = fpath + String.format("1f00_%04d_%04d_%04d.txt", j, mvObj.frameList.size(), mvObj.pointNumber);
//      saveLineObj(mvObj, fname);
      j++;
    }
  }

  /**
   * @return the mvObjs
   */
  public ArrayList<LineObject> getMvObjs() {
    return mvObjs;
  }

  /**
   * @return the fastObjs
   */
  public ArrayList<LineObject> getFastObjs() {
    return fastObjs;
  }

  /**
   * @return the singleFrameObjs
   */
  public ArrayList<LineObject> getSingleFrameObjs() {
    return singleFrameObjs;
  }

}
