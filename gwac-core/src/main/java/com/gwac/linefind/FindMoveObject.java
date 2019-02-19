/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecord;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xy
 */
public class FindMoveObject {

  // cache of values of sin and cos for different theta values. Has a significant performance improvement. 
  private double[] sinCache;
  private double[] cosCache;

  // the number of points that have been added 
  protected int numOT1s;
  HoughLine[][] houghArray;
  ArrayList<OtObserveRecord> historyOT1s;
  HashSet<Integer> notInLine;
  HashSet<Integer> inLine;
  public ArrayList<LineObject> mvObjs;
  ArrayList<LineObject> fastObjs;
  ArrayList<LineObject> singleFrameObjs;
  int totalLinePointNumber;

  int maxHoughFrameNunmber;
  int objInitMinPoint;
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
  double thetaStep;
  double rhoStep;

  float maxDistance;
  float rhoErrorTimes;
  int validLineMinPoint;

  public FindMoveObject(LineParameterConfig parm) {

    this.imgWidth = parm.imgWidth;
    this.imgHeight = parm.imgHeight;
    this.imgXCenter = parm.imgWidth / 2;
    this.imgYCenter = parm.imgHeight / 2;

    this.maxTheta = Math.PI;
    this.maxHoughFrameNunmber = parm.maxHoughFrameNunmber;
    this.objInitMinPoint = parm.objInitMinPoint;
    this.maxDistance = parm.maxDistance;
    this.maxRho = Math.sqrt(Math.pow(parm.imgWidth, 2) + Math.pow(parm.imgHeight, 2));
    this.halfRho = this.maxRho / 2;
    this.thetaSize = parm.thetaSize;
    this.rhoSize = parm.rhoSize;
    this.thetaStep = this.maxTheta / parm.thetaSize;
    this.rhoStep = this.maxRho / parm.rhoSize;

    this.rhoErrorTimes = parm.rhoErrorTimes;
    this.validLineMinPoint = parm.validLineMinPoint;

    initialise();
  }

  private void initialise() {

    this.historyOT1s = new ArrayList();
    this.notInLine = new HashSet();
    this.inLine = new HashSet();
    this.mvObjs = new ArrayList();
    this.fastObjs = new ArrayList();
    this.singleFrameObjs = new ArrayList();

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
        houghArray[t][r] = new HoughLine((float) (t * thetaStep), (float) (r * rhoStep));
      }
    }

  }

  public void addFrame(List<OtObserveRecord> singleFrame) {

    for (OtObserveRecord ot1 : singleFrame) {
      this.historyAddPoint(ot1);
      this.lineAddPoint(ot1);
    }
    this.endFrame();
  }

  public void findSingleFrameLine(List<OtObserveRecord> singleFrame) {

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
        tline.addPoint(numOT1s - 1, curNumber, ot1.getX(), ot1.getY(), ot1.getDateUt(), ot1.getOorId(), ot1.getRaD(), ot1.getDecD());
        if (tline.validSize() >= this.objInitMinPoint) {
          double tsigma = tline.lineRegression();
          if ((tsigma < 2 && tline.validSize() >= this.objInitMinPoint) || (tline.validSize() >= 10)) {
            LineObject lineObj = new LineObject((float) (t * thetaStep), (float) (r * rhoStep));
            lineObj.cloneLine(tline);
            mvObjs.add(lineObj);
            clearAllPoint(lineObj);
            break;
          }
        }
      }
    }

  }

  public void lineAddPoint(OtObserveRecord ot1) {

    boolean findLine = false;
    int i = 0;
    for (LineObject tline : this.mvObjs) {
      if (!tline.isEndLine(ot1.getFfNumber() - this.maxHoughFrameNunmber + 1)) {
        if (tline.isOnLine(ot1)) {
          tline.addPoint(numOT1s - 1, ot1.getFfNumber(), ot1.getX(), ot1.getY(), ot1.getDateUt(), ot1.getOorId(), ot1.getRaD(), ot1.getDecD());
          findLine = true;
          break;
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
      tmo.endLine();
    }

    mergeLine();

    int i = 1;
    for (LineObject tmo : this.mvObjs) {
//      tmo.statistic();
      tmo.clearPointXY();
      tmo.clearPointTXY();
//      tmo.statistic();
      tmo.analysis();
      tmo.findFirstAndLastPoint();
      i++;
    }

//    getOutLinePointSet();
//    reprocess();
  }

  public void reprocess() {

    getOutLinePointSet();

    int tnum = 0;
    Iterator<Integer> titer = this.notInLine.iterator();
    while (titer.hasNext()) {
      int idx = titer.next();
      OtObserveRecord ot1 = this.historyOT1s.get(idx);
      for (LineObject tline : this.mvObjs) {
        if (tline.isOnLineReprocess(ot1)) {
          tnum++;
          HoughtPoint hp = new HoughtPoint(idx, ot1.getFfNumber(), ot1.getX(), ot1.getY(), ot1.getDateUt(), ot1.getOorId(), ot1.getRaD(), ot1.getDecD());
          tline.addPointReprocess(hp);
          break;
        }
      }
    }
    int i = 1;
    for (LineObject tmo : this.mvObjs) {
      tmo.statistic();
      tmo.analysis();
      tmo.findFirstAndLastPoint();
      i++;
    }

    getOutLinePointSet();
  }

  public void statisticAndAnalysisLine() {

    for (LineObject tobj : this.mvObjs) {
      tobj.statistic();
      tobj.analysis();
    }
  }

  public void mergeLine() {

    int lineNum = this.mvObjs.size();
    Boolean matchFlag[] = new Boolean[lineNum];

    for (int i = 0; i < lineNum; i++) {
      matchFlag[i] = false;
    }

    for (int i = 0; i < lineNum; i++) {
      LineObject tobj = this.mvObjs.get(i);
      if (!matchFlag[i]) {
        for (int j = i + 1; j < lineNum; j++) {
          LineObject tobj2 = this.mvObjs.get(j);
          if (!matchFlag[j] && tobj.matchLine(tobj2)) {
//            tobj.merge(tobj2);
            tobj.addLineObject(tobj2);
            matchFlag[j] = true;
          }
        }
      }
    }

    for (int i = lineNum - 1; i >= 0; i--) {
      if (matchFlag[i]) {
        this.mvObjs.remove(i);
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

    Integer idxArray[] = {};
//    Integer idxArray[] = {14, 16, 60, 99, 100};
    ArrayList<Integer> idxList = new ArrayList(Arrays.asList(idxArray));

    int j = 0;
    int total = 0;
    for (LineObject mvObj : mvObjs) {

      if (mvObj.pointNumber < validLineMinPoint || mvObj.frameList.size() <= 20 || mvObj.lineType != '1') {
        j++;
        continue;
      }

      if (!idxList.isEmpty() && idxList.contains(new Integer(j))) {
        j++;
        continue;
      }

      if (mvObj.framePointMultiNumber != 0) {
        j++;
        continue;
      }

      FileOutputStream out = null;
      try {
        String fname = String.format("%03d-%03d.txt", j, mvObj.pointList.size());
        String fullname = fpath + fname;

        String debugStr = String.format("%s: %s", fname, mvObj.getOutLineInfo());
        System.out.println(debugStr);
        mvObj.mergeType1();
        mvObj.statistic();
        mvObj.updateInfo();
        mvObj.findFirstAndLastPoint();
        debugStr = String.format("%s: %s", fname, mvObj.getOutLineInfo());
        System.out.println(debugStr);

        out = new FileOutputStream(new File(fullname));
        for (HoughtPoint tPoint : mvObj.pointList) {
          OtObserveRecord ot1 = this.historyOT1s.get(tPoint.getpIdx());
          out.write((ot1.toString() + "\n").getBytes());
        }
        out.close();
      } catch (FileNotFoundException ex) {
        Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        try {
          out.close();
        } catch (IOException ex) {
          Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      j++;
      total++;
    }
    System.out.println("total save " + total + " lines.");
  }

  public void saveNotInLine(String fname) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(new File(fname));
      Iterator<Integer> titer = this.notInLine.iterator();
      ArrayList<Integer> idList = new ArrayList();
      while (titer.hasNext()) {
        idList.add(titer.next());
      }
      QuickSort.sort(idList);
      for (Integer idx : idList) {
        OtObserveRecord ot1 = this.historyOT1s.get(idx);
        out.write((ot1.toString() + "\n").getBytes());
      }
      out.close();
    } catch (FileNotFoundException ex) {
      Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        out.close();
      } catch (IOException ex) {
        Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void getOutLinePointSet() {

    inLine.clear();
    notInLine.clear();

    for (LineObject line : this.mvObjs) {
      for (HoughtPoint tpoint : line.pointList) {
        inLine.add(tpoint.getpIdx());
      }
    }

    totalLinePointNumber = inLine.size();
    for (int i = 0; i < this.numOT1s; i++) {
      notInLine.add(i);
    }
    notInLine.removeAll(inLine);
  }

}
