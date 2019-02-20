/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecordMovObj;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author xy
 */
public class LineObject {

  public char lineType; //0:未分类；1：多帧单点；2：多帧多点；3：一帧多点

  public PolynomialCurveFitter fitter1;
  public PolynomialCurveFitter fitter2;
  public PolynomialCurveFitter fitter3;
  public List<WeightedObservedPoint> xyList;
  public List<WeightedObservedPoint> txList;
  public List<WeightedObservedPoint> tyList;
  public double[] xyCoeff;
  public double[] txCoeff;
  public double[] tyCoeff;
  public double xySigma;
  public double txSigma;
  public double tySigma;
  public double xySigmaMax;
  public double txSigmaMax;
  public double tySigmaMax;
  public double xySigmaMean;
  public double txSigmaMean;
  public double tySigmaMean;

  public ArrayList<HoughFrame> frameList;
  public ArrayList<HoughtPoint> pointList;
  public int pointNumber;
  public int framePointMultiNumber;
  public int framePointMaxNumber;
  public float avgFramePointNumber;

  public int firstFrameNumber;
  public int lastFrameNumber;
  public HoughtPoint firstPoint; //minNumber
  public HoughtPoint lastPoint; //maxNumber

  public float theta;
  public float rho;
  public float deltaX;
  public float deltaY;

  public boolean endLine;

  /**
   *
   * @param theta
   * @param rho
   */
  public LineObject(float theta, float rho) {
    this.theta = theta;
    this.rho = rho;
    this.frameList = new ArrayList();
    this.pointList = new ArrayList();
    this.pointNumber = 0;
    this.avgFramePointNumber = 0;
    this.framePointMaxNumber = 0;
    this.framePointMultiNumber = 0;

    this.firstFrameNumber = Integer.MAX_VALUE;
    this.lastFrameNumber = Integer.MIN_VALUE;

    this.endLine = false;

    fitter1 = PolynomialCurveFitter.create(1);
    fitter2 = PolynomialCurveFitter.create(2);
    fitter3 = PolynomialCurveFitter.create(3);
    xyCoeff = null;
    txCoeff = null;
    tyCoeff = null;
    xyList = new ArrayList();
    txList = new ArrayList();
    tyList = new ArrayList();

    lineType = '0';
  }

  public boolean isValidLine() {
    boolean isValid = true;
    if (this.avgFramePointNumber <= 2 && (this.tySigma > 10 || this.txSigma > 10)) {
      isValid = false;
    }
    return isValid;
  }

  public boolean isEndLine(int frameNumber) {
    return lastFrameNumber < frameNumber;
  }

  public void endLine() {
    this.findFirstAndLastPoint();
    this.updateInfo();
//    this.analysis();
//    this.statistic();
  }

  public void clearPointXY() {

    HashSet<Integer> xylist = new HashSet();
    ArrayList<Double> xPreYDiffs = new ArrayList();
    int pNum = pointList.size();

    int loopNum = 0;
    int maxLoopNum = 100;
    double timeSigma = 3.0;

    if (this.pointNumber < 10) {
      for (int i = 0; i < pNum; i++) {
        if (!xylist.contains(i)) {
          HoughtPoint hp = pointList.get(i);
          xyList.add(new WeightedObservedPoint(1, hp.getX(), hp.getY()));
        }
      }

      xyCoeff = fitter2.fit(xyList);
      return;
    }

    while (true) {

      xyList.clear();
      xPreYDiffs.clear();

      for (int i = 0; i < pNum; i++) {
        if (!xylist.contains(i)) {
          HoughtPoint hp = pointList.get(i);
          xyList.add(new WeightedObservedPoint(1, hp.getX(), hp.getY()));
        }
      }

      xyCoeff = fitter2.fit(xyList);
      for (int i = 0; i < pNum; i++) {
        if (!xylist.contains(i)) {
          HoughtPoint hp = pointList.get(i);
          double preYDiff = Math.abs(hp.getY() - preNextYByX2(hp.getX()));
          xPreYDiffs.add(preYDiff);
        }
      }

      SummaryStatistics xyStat = new SummaryStatistics();

      for (int i = 0; i < xPreYDiffs.size(); i++) {
        xyStat.addValue(xPreYDiffs.get(i));
      }
      xySigma = xyStat.getStandardDeviation();
      xySigmaMean = xyStat.getMean();

      if ((loopNum > maxLoopNum) || (xySigma < 10)) {
        break;
      } else {
        boolean rflag = false;
        int tIdx = 0;
        for (int i = 0; i < pNum; i++) {
          if (!xylist.contains(i)) {
            if (Math.abs(xPreYDiffs.get(tIdx) - xySigmaMean) > timeSigma * xySigma) {
              xylist.add(i);
              rflag = true;
            }
            tIdx++;
          }
        }
        while (!rflag) {
          timeSigma = timeSigma - 0.1;
          tIdx = 0;
          for (int i = 0; i < pNum; i++) {
            if (!xylist.contains(i)) {
              if (Math.abs(xPreYDiffs.get(tIdx) - xySigmaMean) > timeSigma * xySigma) {
                xylist.add(i);
                rflag = true;
              }
              tIdx++;
            }
          }
        }
      }
      loopNum++;
    }

    Iterator<Integer> titer = xylist.iterator();
    List<HoughtPoint> removeList = new ArrayList();
    while (titer.hasNext()) {
      int idx = titer.next();
      removeList.add(this.pointList.get(idx));
    }
    for (HoughtPoint hp : removeList) {
      this.removePoint(hp);
    }
  }

  public void clearPointTXY() {

    HashSet<Integer> rIdxList = new HashSet();
    ArrayList<Double> tPreYDiffs = new ArrayList();
    ArrayList<Double> tPreXDiffs = new ArrayList();

    int pNum = pointList.size();
    int loopNum = 0;
    int maxLoopNum = 100;
    double timeSigma = 3.0;

    if ((this.pointNumber < 10) || (this.avgFramePointNumber > 1.2)) {
      for (int i = 0; i < pNum; i++) {
        if (!rIdxList.contains(i)) {
          HoughtPoint hp = pointList.get(i);
          txList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getX()));
          tyList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getY()));
        }
      }

      txCoeff = fitter3.fit(txList);
      tyCoeff = fitter3.fit(tyList);
      return;
    }

    while (true) {

      txList.clear();
      tyList.clear();
      tPreYDiffs.clear();
      tPreXDiffs.clear();

      for (int i = 0; i < pNum; i++) {
        if (!rIdxList.contains(i)) {
          HoughtPoint hp = pointList.get(i);
          txList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getX()));
          tyList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getY()));
        }
      }

      txCoeff = fitter3.fit(txList);
      tyCoeff = fitter3.fit(tyList);
      for (int i = 0; i < pNum; i++) {
        if (!rIdxList.contains(i)) {
          HoughtPoint hp = pointList.get(i);
          double preYDiff2 = Math.abs(hp.getY() - preNextYByT3(hp.getDateUtc().getTime()));
          double preXDiff = Math.abs(hp.getX() - preNextXByT3(hp.getDateUtc().getTime()));
          tPreYDiffs.add(preYDiff2);
          tPreXDiffs.add(preXDiff);
        }
      }

      SummaryStatistics tyStat = new SummaryStatistics();
      SummaryStatistics txStat = new SummaryStatistics();

      for (int i = 0; i < tPreYDiffs.size(); i++) {
        tyStat.addValue(tPreYDiffs.get(i));
        txStat.addValue(tPreXDiffs.get(i));
      }
      tySigma = tyStat.getStandardDeviation();
      txSigma = txStat.getStandardDeviation();
      tySigmaMean = tyStat.getMean();
      txSigmaMean = txStat.getMean();

      if ((loopNum > maxLoopNum) || (tySigma < 10 && txSigma < 10)) {
        if ((loopNum > maxLoopNum)) {
          System.out.println("loopNum=" + loopNum);
        }
        break;
      } else {
        boolean rflag = false;
        int tIdx = 0;
        for (int i = 0; i < pNum; i++) {
          if (!rIdxList.contains(i)) {
            if (Math.abs(tPreYDiffs.get(tIdx) - tySigmaMean) > timeSigma * tySigma) {
              rIdxList.add(i);
              rflag = true;
            }
            if (Math.abs(tPreXDiffs.get(tIdx) - txSigmaMean) > timeSigma * txSigma) {
              rIdxList.add(i);
              rflag = true;
            }
            tIdx++;
          }
        }
        while (!rflag) {
          timeSigma = timeSigma - 0.1;
          tIdx = 0;
          for (int i = 0; i < pNum; i++) {
            if (!rIdxList.contains(i)) {
              if (Math.abs(tPreYDiffs.get(tIdx) - tySigmaMean) > timeSigma * tySigma) {
                rIdxList.add(i);
                rflag = true;
              }
              if (Math.abs(tPreXDiffs.get(tIdx) - txSigmaMean) > timeSigma * txSigma) {
                rIdxList.add(i);
                rflag = true;
              }
              tIdx++;
            }
          }
        }
      }
      loopNum++;
    }

    Iterator<Integer> titer = rIdxList.iterator();
    List<HoughtPoint> removeList = new ArrayList();
    while (titer.hasNext()) {
      int idx = titer.next();
      removeList.add(this.pointList.get(idx));
    }
    for (HoughtPoint hp : removeList) {
      this.removePoint(hp);
    }
  }

  public void statistic() {

    xyList.clear();
    txList.clear();
    tyList.clear();
    for (HoughtPoint hp : pointList) {
      xyList.add(new WeightedObservedPoint(1, hp.getX(), hp.getY()));
      txList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getX()));
      tyList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getY()));
    }

    xyCoeff = fitter2.fit(xyList);
    txCoeff = fitter3.fit(txList);
    tyCoeff = fitter3.fit(tyList);

    SummaryStatistics xyStat = new SummaryStatistics();
    SummaryStatistics tyStat = new SummaryStatistics();
    SummaryStatistics txStat = new SummaryStatistics();
    for (HoughtPoint hp : this.pointList) {
      double preYDiff = Math.abs(hp.getY() - preNextYByX2(hp.getX()));
      double preYDiff2 = Math.abs(hp.getY() - preNextYByT3(hp.getDateUtc().getTime()));
      double preXDiff = Math.abs(hp.getX() - preNextXByT3(hp.getDateUtc().getTime()));
      xyStat.addValue(preYDiff);
      tyStat.addValue(preYDiff2);
      txStat.addValue(preXDiff);
    }
    xySigma = xyStat.getStandardDeviation();
    tySigma = tyStat.getStandardDeviation();
    txSigma = txStat.getStandardDeviation();
    xySigmaMax = xyStat.getMax();
    xySigmaMean = xyStat.getMean();
    tySigmaMax = tyStat.getMax();
    txSigmaMax = txStat.getMax();
    tySigmaMean = tyStat.getMean();
    txSigmaMean = txStat.getMean();
  }

  public boolean matchLine(LineObject tline) {

    HoughtPoint tFirstPoint = tline.firstPoint;
    HoughtPoint tLastPoint = tline.lastPoint;

    boolean fpMatch = isOnLine(tFirstPoint.getX(), tFirstPoint.getY(), tFirstPoint.getDateUtc());
    boolean spMatch = isOnLine(tLastPoint.getX(), tLastPoint.getY(), tLastPoint.getDateUtc());
    return fpMatch && spMatch;
  }

  public boolean isOnLine(OtObserveRecordMovObj ot1) {
    return isOnLine(ot1.getX(), ot1.getY(), ot1.getDateUt());
  }

  public boolean isOnLine(float x, float y, Date dateut) {

    int xPreYDiff = 20;
    int tPreDiff = 100;
    boolean isOnLine = false;
    double preYDiff = Math.abs(y - preNextYByX(x));
    if (preYDiff < xPreYDiff) {
      if (this.framePointMaxNumber > 2) {
        isOnLine = true;
      } else {
        double preXDiff = Math.abs(x - preNextXByT(dateut.getTime()));
        double preYDiff2 = Math.abs(y - preNextYByT(dateut.getTime()));
        if (preXDiff < tPreDiff && preYDiff2 < tPreDiff) {
          isOnLine = true;
        }
      }
    }
    return isOnLine;
  }

  public boolean isOnLineReprocess(OtObserveRecordMovObj ot1) {

    float x = ot1.getX();
    float y = ot1.getY();
    Date dateut = ot1.getDateUt();

    int xPreYDiff = 10;
    int tPreDiff = 10;
    boolean isOnLine = false;
    double preYDiff = Math.abs(y - preNextYByX2(x));

    if (preYDiff < xPreYDiff) {
      if (this.avgFramePointNumber < 1.2) {
        double preXDiff = Math.abs(x - preNextXByT3(dateut.getTime()));
        double preYDiff2 = Math.abs(y - preNextYByT3(dateut.getTime()));
        if (preXDiff < tPreDiff && preYDiff2 < tPreDiff) {
          isOnLine = true;
        }
      } else {
        isOnLine = isOnLineMultiPointCheck(ot1);
      }
    }

    return isOnLine;
  }

  public boolean isOnLineMultiPointCheck(OtObserveRecordMovObj ot1) {
    boolean isOnLine = false;

    float x = ot1.getX();
    float y = ot1.getY();
    int frameNum = ot1.getFfNumber();

    if ((x > this.firstPoint.getX() && x < this.lastPoint.getX())
            || (x < this.firstPoint.getX() && x > this.lastPoint.getX())) {
      for (int i = 0; i < this.pointNumber - 1; i++) {
        if ((this.pointList.get(i).getX() - x) * (this.pointList.get(i + 1).getX() - x) < 0) {
          if ((frameNum >= this.pointList.get(i).getFrameNumber()) && (frameNum <= this.pointList.get(i + 1).getFrameNumber())) {
            isOnLine = true;
          }
        }
      }
    } else if (x < this.firstPoint.getX() && x < this.lastPoint.getX()) {
      if (this.firstPoint.getX() < this.lastPoint.getX()) {
        int tnum = firstPoint.getFrameNumber() - frameNum;
        if (tnum >= 0 && tnum <= LineParameterConfig.maxHoughFrameNunmber) {
//        if (frameNum <= firstPoint.getFrameNumber()) {
          isOnLine = true;
        }
      } else if (this.firstPoint.getX() > this.lastPoint.getX()) {
        int tnum = frameNum - lastPoint.getFrameNumber();
        if (tnum >= 0 && tnum <= LineParameterConfig.maxHoughFrameNunmber) {
//        if (frameNum >= lastPoint.getFrameNumber()) {
          isOnLine = true;
        }
      }
    } else if (x > this.firstPoint.getX() && x > this.lastPoint.getX()) {
      if (this.firstPoint.getX() > this.lastPoint.getX()) {
        int tnum = firstPoint.getFrameNumber() - frameNum;
        if (tnum >= 0 && tnum <= LineParameterConfig.maxHoughFrameNunmber) {
//        if (frameNum <= firstPoint.getFrameNumber()) {
          isOnLine = true;
        }
      } else if (this.firstPoint.getX() < this.lastPoint.getX()) {
        int tnum = frameNum - lastPoint.getFrameNumber();
        if (tnum >= 0 && tnum <= LineParameterConfig.maxHoughFrameNunmber) {
//        if (frameNum >= lastPoint.getFrameNumber()) {
          isOnLine = true;
        }
      }
    }
    return isOnLine;
  }

  public int bQuery(float val) {
    return 0;
  }

  public void cloneLine(HoughLine hl) {
    this.frameList = hl.frameList;
    this.pointList = hl.pointList;
    this.pointNumber = hl.pointNumber;
    this.lastFrameNumber = hl.lastFrameNumber;
    this.lastPoint = hl.lastPoint;
    hl.clearAll();

    for (HoughtPoint tp : pointList) {
      xyList.add(new WeightedObservedPoint(1, tp.getX(), tp.getY()));
      txList.add(new WeightedObservedPoint(1, tp.getDateUtc().getTime(), tp.getX()));
      tyList.add(new WeightedObservedPoint(1, tp.getDateUtc().getTime(), tp.getY()));
    }

    xyfit();
    txyfit();

  }

  public void addPoint(int pIdx, int frameNumber, float x, float y, Date dateUtc, String ffName, double ra, double dec) {
    this.addPoint(new HoughtPoint(pIdx, frameNumber, x, y, dateUtc, ffName, ra, dec));
  }

  /**
   * a. 开始新一帧（新点的帧编号） pList. 该直线帧数等于1：与当前帧中（两端）最近的点，距离小于L1（100像素）。 pListpList.
   * 该直线帧数等于2（新点为该帧的第1个点）：与上一帧的帧编号差值小于N1（10）；与上一帧中最近的点，距离小于L1；同时计算直线的方向X1，Y1
   * pListpListpList. 该直线帧数等于2（新点为该帧的第n[n>1]个点）：与上一帧的帧编号差值小于N1；与当前帧中最近的点，距离小于L1；
   * pListv. 该直线帧数大于2：与上一帧的帧编号差值小于N1（10）；与上一帧中最近的点，距离小于L1；同时计算直线的速度Vx1，Vy1 v.
   * 直线的最后帧与当前帧编号差值大于N1，则将该直线标示为识别完成。 b. 帧编号未改变 pList. 帧编号小于N1，距离小于L1，方向和速度满足预测
   *
   * 多帧多点，注意事项： 1，delta的计算：delta的计算至少需要两帧数据
   * 2，计算下一个点与最后一个点的delta时，如果一帧中有多个点，先来的一个点是距离上一帧较远的点A，则新来的较近的点B计算的delta是与A的，结果出错
   * 3，解决方案是：新一帧的点与上一帧的点求delta，这里将添加点（pointList.add(hp);）放到求上一帧的最后一个点之后。
   *
   * @param hp
   */
  public final void addPoint(HoughtPoint hp) {

    xyList.add(new WeightedObservedPoint(1, hp.getX(), hp.getY()));
    txList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getX()));
    tyList.add(new WeightedObservedPoint(1, hp.getDateUtc().getTime(), hp.getY()));

    xyfit();
    txyfit();

    if (frameList.isEmpty() || (lastFrameNumber < hp.getFrameNumber())) {
      lastFrameNumber = hp.getFrameNumber();
      HoughFrame hframe = new HoughFrame(hp, hp.getFrameNumber());
      frameList.add(hframe);
    } else if (lastFrameNumber == hp.getFrameNumber()) {
      HoughFrame lastFrame = frameList.get(frameList.size() - 1);
      lastFrame.addPoint(hp);
    } else {
      for (HoughFrame tframe : frameList) {
        if (tframe.frameNumber == hp.getFrameNumber()) {
          tframe.addPoint(hp);
        }
      }
    }

    pointNumber++;
    pointList.add(hp);

    updateInfo();
  }

  public void addPointReprocess(HoughtPoint hp) {

    if (frameList.isEmpty() || (hp.getFrameNumber() > lastFrameNumber)) {
      lastFrameNumber = hp.getFrameNumber();
      HoughFrame hframe = new HoughFrame(hp, hp.getFrameNumber());
      frameList.add(hframe);
    } else if (lastFrameNumber == hp.getFrameNumber()) {
      HoughFrame lastFrame = frameList.get(frameList.size() - 1);
      lastFrame.addPoint(hp);
    } else {
      boolean isAdd = false;
      for (HoughFrame tframe : frameList) {
        if (tframe.frameNumber == hp.getFrameNumber()) {
          tframe.addPoint(hp);
          isAdd = true;
          break;
        }
      }
      if (!isAdd) {
        HoughFrame hframe = new HoughFrame(hp, hp.getFrameNumber());
        frameList.add(hframe);
      }
    }

    pointNumber++;
    pointList.add(hp);
  }

  public void xyfit() {
    while (xyList.size() > 20) {
      xyList.remove(0);
    }
    xyCoeff = fitter1.fit(xyList);
  }

  public void txyfit() {
    while (txList.size() > 20) {
      txList.remove(0);
    }
    txCoeff = fitter1.fit(txList);
    while (tyList.size() > 20) {
      tyList.remove(0);
    }
    tyCoeff = fitter1.fit(tyList);
  }

  public double preNextYByX(double x) {
    return xyCoeff[0] + xyCoeff[1] * x;
  }

  public double preNextYByX2(double x) {
    return xyCoeff[0] + xyCoeff[1] * x + xyCoeff[2] * x * x;
  }

  public double preNextYByX3(double x) {
    return xyCoeff[0] + xyCoeff[1] * x + xyCoeff[2] * x * x + xyCoeff[3] * x * x * x;
  }

  public double preNextYByT(double t) {
    return tyCoeff[0] + tyCoeff[1] * t;
  }

  public double preNextXByT(double t) {
    return txCoeff[0] + txCoeff[1] * t;
  }

  public double preNextYByT3(double t) {
    return tyCoeff[0] + tyCoeff[1] * t + tyCoeff[2] * t * t + tyCoeff[3] * t * t * t;
  }

  public double preNextXByT3(double t) {
    return txCoeff[0] + txCoeff[1] * t + txCoeff[2] * t * t + txCoeff[3] * t * t * t;
  }

  public void addLineObject(LineObject lineObj) {

    for (HoughtPoint tp : lineObj.pointList) {
      this.addPoint(tp);
    }
    lineObj.pointList.clear();
    lineObj.removeAll();
    lineObj.firstPoint = null;
    lineObj.lastPoint = null;
  }

  public void analysis() {
    if (this.frameList.size() == 1 && this.avgFramePointNumber > 2) {
      lineType = '4';
    } else {
      if (this.framePointMaxNumber > 2 && this.avgFramePointNumber > 2) {
        lineType = '3';
      } else if (this.framePointMaxNumber > 2 && this.avgFramePointNumber <= 2) {
        if (this.framePointMultiNumber == 1) {
          lineType = '3';
        } else {
          lineType = '2';
        }
      } else if (this.framePointMaxNumber <= 2 && this.avgFramePointNumber > 1.2 && this.avgFramePointNumber <= 2) {
        lineType = '2';
      } else if (this.avgFramePointNumber <= 1.2) {
        lineType = '1';
      }
    }
  }

  //对framePointMaxNumber大于等于2而且this.avgFramePointNumber小于等于2的目标一帧中的多个点合并为1个点
  public void mergeType1() {

    if (lineType == '1' && this.avgFramePointNumber > 1) {
//    if (false && lineType == '1' && this.avgFramePointNumber > 1) {

      for (HoughFrame hf : this.frameList) {
        int tnum = hf.pointList.size();
        if (tnum > 1) {
          Double avgX = 0.0;
          Double avgY = 0.0;
          for (HoughtPoint hp : hf.pointList) {
            avgX += hp.getX();
            avgY += hp.getY();
          }
          avgX /= tnum;
          avgY /= tnum;
          HoughtPoint fp = hf.pointList.get(0);
          HoughtPoint thp = new HoughtPoint(fp.getpIdx(), fp.getFrameNumber(), avgX.floatValue(), avgY.floatValue(), fp.getDateUtc(), fp.getFfName(), fp.getRa(), fp.getDec());

          for (HoughtPoint hp : hf.pointList) {
            for (int k = 0; k < this.pointList.size(); k++) {
              if (hp.getpIdx() == this.pointList.get(k).getpIdx()) {
                this.pointList.remove(k);
                this.pointNumber--;
                break;
              }
            }
          }
          hf.pointList.clear();
          hf.pointList.add(thp);
          this.pointList.add(thp);
          this.pointNumber++;
        }
      }
    }
  }

  public void updateInfo() {

    this.avgFramePointNumber = (float) (this.pointNumber * 1.0 / this.frameList.size());

    this.framePointMultiNumber = 0;
    for (HoughFrame hf : this.frameList) {
      int tnum = hf.pointList.size();
      if (this.framePointMaxNumber < tnum) {
        this.framePointMaxNumber = tnum;
      }
      if (tnum > 1) {
        this.framePointMultiNumber++;
      }
    }
  }

  public void findFirstAndLastPoint() {

    getDelta();
    //快速排序的递归实现在数据量大时会报堆栈溢出错误StackOverflowError，这里的排序主要是为了得到最大值和最小值。
    //解决方案：去掉快排，而直接循环找最大值最小值即可。
//    sort(pointList, new CompareMethod1()); 
    firstPoint = pointList.get(0);
    lastPoint = pointList.get(0);
    PointCompare pcmp = new CompareMethod1();
    for (HoughtPoint tpoint : pointList) {
      if (pcmp.compare(tpoint, firstPoint) < 0) {
        firstPoint = tpoint;
      }
      if (pcmp.compare(lastPoint, tpoint) < 0) {
        lastPoint = tpoint;
      }
    }
    this.firstFrameNumber = firstPoint.getFrameNumber();
  }

  /**
   * 需要帧数大于等于2
   */
  public void getDelta() {

    if (frameList.size() > 1) {

      updateTheta();

      HoughFrame firstFrame = frameList.get(0);
      HoughFrame lastFrame = frameList.get(frameList.size() - 1);
      HoughtPoint ffMinPoint, lfMinPoint;

      //PI/4=0.7854, PI*3/4=2.3562
      if (theta > 0.7854 && theta < 2.3562) {
        ffMinPoint = firstFrame.minY;
        lfMinPoint = lastFrame.minY;
      } else {
        ffMinPoint = firstFrame.minX;
        lfMinPoint = lastFrame.minX;
      }

      deltaX = lfMinPoint.getX() - ffMinPoint.getX();
      deltaY = lfMinPoint.getY() - ffMinPoint.getY();
    } else {
      HoughFrame firstFrame = frameList.get(0);
      deltaX = firstFrame.deltaX;
      deltaY = firstFrame.deltaY;
    }
  }

  public void updateTheta() {

    if (this.pointNumber >= 2) {
      HoughtPoint fPoint = pointList.get(0);
      HoughtPoint lPoint = pointList.get(pointList.size() - 1);

      double xDelta = fPoint.getX() - lPoint.getX();
      double yDelta = fPoint.getY() - lPoint.getY();
      float ktheta = (float) (Math.atan2(yDelta, xDelta));

      if (ktheta < 0) {
        ktheta += Math.PI;
      }

      if (ktheta < Math.PI / 2) {
        this.theta = (float) (ktheta + Math.PI / 2);
      } else {
        this.theta = (float) (ktheta - Math.PI / 2);
      }
    }
  }

  /**
   * 对点列表进行排序：1，编号序列（时间）排序；2，按照X或Y变化快的排序；3，根据
   */
  private class CompareMethod1 implements PointCompare<HoughtPoint> {

    /**
     * 正方向排序,p1是否排在p2前
     *
     * @param p1 比较点
     * @param p2 被比较点
     * @return 1:p1排在p2前，-1:p1排在p2后
     */
    @Override
    public int compare(HoughtPoint p1, HoughtPoint p2) {
      int result = 1;
      if (p1.getFrameNumber() < p2.getFrameNumber()) {
        result = -1;
      } else if (Math.abs(p1.getFrameNumber() - p2.getFrameNumber()) == 0) {
        //if (theta > 0.7854 && theta < 2.3562)
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
          if (deltaX > 0) {
            if (p1.getX() < p2.getX()) {
              result = -1;
            }
          } else {
            if (p1.getX() > p2.getX()) {
              result = -1;
            }
          }
        } else {
          if (deltaY > 0) {
            if (p1.getY() < p2.getY()) {
              result = -1;
            }
          } else {
            if (p1.getY() > p2.getY()) {
              result = -1;
            }
          }
        }
      }
      return result;
    }
  }

  /**
   * 增序排序
   *
   * @param in
   * @param begin
   * @param end
   * @param pcmp
   */
  private void quickSort(Object[] in, int begin, int end, PointCompare pcmp) {
    if (begin == end || begin == (end - 1)) {
      return;
    }
    Object p = in[begin];
    int a = begin + 1;
    int b = a;
    for (; b < end; b++) {
      //该对象类型数组必须实现Comparable接口，这样才能使用compareTo函数进行比较 
      if (pcmp.compare(in[b], p) < 0) {
        if (a == b) {
          a++;
          continue;
        }
        Object temp = in[a];
        in[a] = in[b];
        in[b] = temp;
        a++;
      }
    }
    in[begin] = in[a - 1];
    in[a - 1] = p;
    if (a - 1 > begin) {
      quickSort(in, begin, a, pcmp);
    }
    if (end - 1 > a) {
      quickSort(in, a, end, pcmp);
    }
  }

  /**
   * 添加对List对象进行排序的功能，参考了Java中的Java.util.Collections类的sort()函数
   * 采用递归排序，在数据量大的时候会报堆栈溢出错误StackOverflowError
   *
   * @param list
   * @param pcmp
   */
  public void sort(List<HoughtPoint> list, PointCompare pcmp) {
    Object[] t = list.toArray();//将列表转换为数组
    quickSort(t, 0, t.length, pcmp); //对数组进行排序
    //数组排序完成后再写回到列表中
    ListIterator<HoughtPoint> pList = list.listIterator();
    for (Object t1 : t) {
      pList.next();
      pList.set((HoughtPoint) t1);
    }
  }

  public void removeAll() {

    for (HoughFrame hf : frameList) {
      hf.removeAll();
    }
    this.frameList.clear();
  }

  public void removePoint(HoughtPoint hp) {

    for (int k = 0; k < this.pointList.size(); k++) {
      if (hp.getpIdx() == pointList.get(k).getpIdx()) {
        pointList.remove(k);
        pointNumber--;
        break;
      }
    }

    for (int k = 0; k < this.frameList.size(); k++) {
      HoughFrame tFrame = this.frameList.get(k);
      if (tFrame.frameNumber == hp.getFrameNumber()) {
        for (int i = 0; i < tFrame.pointList.size(); i++) {
          if (hp.getpIdx() == tFrame.pointList.get(i).getpIdx()) {
            tFrame.removePoint(i);
            if (tFrame.pointList.isEmpty()) {
              this.frameList.remove(k);
            }
            break;
          }
        }
        break;
      }
    }
    if (this.frameList.isEmpty()) {
      this.lastFrameNumber = Integer.MIN_VALUE;
    } else {
      this.lastFrameNumber = this.frameList.get(this.frameList.size() - 1).frameNumber;
    }
  }

  public String getOutLineInfo() {
    String rst = String.format("frameNumber:%3d,pointNumber:%3d,framePointMaxNumber:%3d,framePointMultiNumber:%3d,avgFramePointNumber:%4.1f,"
            + "xyMean:%4.1f,xySigma:%4.1f,tyMean:%4.1f,tySigma:%4.1f,txMean:%4.1f,txSigma:%4.1f",
            this.frameList.size(), this.pointNumber, this.framePointMaxNumber, this.framePointMultiNumber, this.avgFramePointNumber,
            this.xySigmaMean, this.xySigma, this.tySigmaMean, this.tySigma, this.txSigmaMean, this.txSigma);
    return rst;
  }

  public void printInfo(ArrayList<OtObserveRecordMovObj> historyOT1s) {

    int i = 1;
    for (HoughFrame tFrame : frameList) {
      System.out.println(String.format("frame%03d, %4d, %2d", i, tFrame.frameNumber, tFrame.pointList.size()));
      for (HoughtPoint tPoint : tFrame.pointList) {
        System.out.println(tPoint.getAllInfo());;
      }
      i++;
    }
  }

  public void printInfo2() {

    int i = 0;
    for (HoughtPoint tPoint : pointList) {
      System.out.println(tPoint.getAllInfo());
      i++;
    }
  }

  public int size() {
    return this.pointNumber;
  }

  public int validSize() {
    return this.pointNumber;
  }

  public int getLastFrameNumber() {
    return this.lastFrameNumber;
  }

  public int getLastOT1Idx() {
    return 0;
  }

  public void printOT1Info(ArrayList<OtObserveRecordMovObj> historyOT1s) {
    int i = 0;
    for (HoughtPoint tPoint : pointList) {
      OtObserveRecordMovObj ot1 = historyOT1s.get(tPoint.getpIdx());
      ot1.printInfo();
      i++;
    }
  }
}
