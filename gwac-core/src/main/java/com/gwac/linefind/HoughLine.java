/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecordMovObj;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author xy
 */
public class HoughLine {

  public ArrayList<HoughFrame> frameList;
  public ArrayList<HoughtPoint> pointList;
  public int pointNumber;
  public int lastFrameNumber;
  public HoughtPoint lastPoint; //maxNumber

  public float theta;
  public float rho;

  /**
   *
   * @param theta
   * @param rho
   */
  public HoughLine(float theta, float rho) {
    this.theta = theta;
    this.rho = rho;
    this.frameList = new ArrayList();
    this.pointList = new ArrayList();
    this.pointNumber = 0;
    this.lastFrameNumber = Integer.MIN_VALUE;
  }

  public double lineRegression() {

    SimpleRegression reg = new SimpleRegression();
    for (HoughtPoint ot1 : pointList) {
      reg.addData(ot1.getX(), ot1.getY());
    }

    double sigma = Math.sqrt(reg.getSumSquaredErrors() / (pointList.size() - 1));
    if (sigma > 10) {
      double sigma2 = Double.MAX_VALUE;
      while (sigma < sigma2) {
        for (int i = 0; i < pointList.size();) {
          HoughtPoint ot1 = pointList.get(i);
          float preY = (float) reg.predict(ot1.getX());
          double ydiff = preY - ot1.getY();
          if ((Math.abs(ydiff) > 1.3 * sigma)) {
            this.removePoint(ot1);
            reg.removeData(ot1.getX(), ot1.getY());
          } else {
            i++;
          }
        }
        sigma2 = sigma;
        sigma = Math.sqrt(reg.getSumSquaredErrors() / (pointList.size() - 1));
        if (sigma < 10) {
          break;
        }
      }
    }

    return sigma;
  }

  public void clearAll() {
    this.frameList = new ArrayList();
    this.pointList = new ArrayList();
    this.pointNumber = 0;
    this.lastFrameNumber = Integer.MIN_VALUE;
    this.lastPoint = null;
  }

  public void addPoint(int pIdx, int frameNumber, float x, float y, Date dateUtc, String ffName,double ra, double dec) {
    this.addPoint(new HoughtPoint(pIdx, frameNumber, x, y, dateUtc, ffName, ra, dec));
  }

  /**
   * a. 开始新一帧（新点的帧编号） pList. 该直线帧数等于1：与当前帧中（两端）最近的点，距离小于L1（100像素）。 pListpList.
   * 该直线帧数等于2（新点为该帧的第1个点）：与上一帧的帧编号差值小于N1（10）；与上一帧中最近的点，距离小于L1；同时计算直线的方向X1，Y1
   * pListpListpList. 该直线帧数等于2（新点为该帧的第n[n>1]个点）：与上一帧的帧编号差值小于N1；与当前帧中最近的点，距离小于L1；
   * pListv. 该直线帧数大于2：与上一帧的帧编号差值小于N1（10）；与上一帧中最近的点，距离小于L1；同时计算直线的速度Vx1，Vy1 v.
   * 直线的最后帧与当前帧编号差值大于N1，则将该直线标示为识别完成。 b. 帧编号未改变 pList. 帧编号小于N1，距离小于L1，方向和速度满足预测
   *
   * @param hp
   */
  public final void addPoint(HoughtPoint hp) {

    pointNumber++;
    pointList.add(hp);

    if (frameList.isEmpty() || (lastFrameNumber != hp.getFrameNumber())) {
      lastFrameNumber = hp.getFrameNumber();
      HoughFrame hframe = new HoughFrame(hp, hp.getFrameNumber());
      frameList.add(hframe);
    } else {
      HoughFrame lastFrame = frameList.get(frameList.size() - 1);
      lastFrame.addPoint(hp);
    }

  }

  /**
   *
   * @param ot1
   * @param maxDistance 新目标与直线最后一个点的距离不超过maxDpListstance
   * @return
   */
  public boolean matchLastPoint(OtObserveRecordMovObj ot1, float maxDistance) {
    boolean flag = true;
    if (this.pointNumber > 0) {
      if (this.pointNumber == 1) {
        HoughtPoint tPoint = this.pointList.get(0);
        lastPoint = tPoint;
      } else if (this.frameList.size() == 1) {
        HoughFrame tframe = this.frameList.get(0);
        if (tframe.frameNumber == ot1.getFfNumber()) {
          lastPoint = tframe.findNearestPoint(ot1);
        } else {
          lastPoint = tframe.findLastPoint(ot1);
        }
      } else {
        HoughFrame lastFrame = this.frameList.get(this.frameList.size() - 1);
        if (lastFrame.pointList.size() == 1) {
          HoughtPoint tPoint = lastFrame.pointList.get(0);
          lastPoint = tPoint;
        } else {
          if (lastFrame.frameNumber == ot1.getFfNumber()) {
            lastPoint = lastFrame.findNearestPoint(ot1);
          } else {
            lastPoint = lastFrame.findLastPoint(ot1);
          }
        }
      }
      double distance = CommonFunction.getLineDistance(ot1.getX(), ot1.getY(), lastPoint.getX(), lastPoint.getY());
      flag = distance < maxDistance;
    }
    return flag;
  }

  public void removePoint(HoughtPoint hp) {

    for (int k = 0; k < this.pointList.size(); k++) {
      if (hp.getpIdx() == pointList.get(k).getpIdx()) {
        pointList.remove(k);
//        this.pointNumber--;
        break;
      }
    }

    for (int k = 0; k < this.frameList.size(); k++) {
      HoughFrame tFrame = this.frameList.get(k);
      if (tFrame.frameNumber == hp.getFrameNumber()) {
        for (int i = 0; i < tFrame.pointList.size(); i++) {
          if (hp.getpIdx() == tFrame.pointList.get(i).getpIdx()) {
//            System.out.println(String.format("real: t=%5d, idx=%5d, number=%5d", t, tline2.pointList.get(pList), tline2.frameNumberList.get(pList)));
            tFrame.removePoint(i);
            this.pointNumber--;
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

  public void removeOldFrame(int toFrameNumber) {

    while (this.pointList.size() > 0) {
      if (pointList.get(0).getFrameNumber() <= toFrameNumber) {
        this.pointList.remove(0);
      } else {
        break;
      }
    }

    while (this.frameList.size() > 0) {
      HoughFrame hf = this.frameList.get(0);
      if (hf.frameNumber <= toFrameNumber) {
        this.pointNumber -= hf.pointList.size();
        hf.removeAll();
        this.frameList.remove(0);
      } else {
        break;
      }
    }
  }

  public void removeAll() {

    for (HoughFrame hf : frameList) {
      hf.removeAll();
    }
    this.frameList.clear();
  }

  public void printInfo(ArrayList<OtObserveRecordMovObj> historyOT1s) {

    for (HoughFrame tFrame : frameList) {
      for (HoughtPoint tPoint : tFrame.pointList) {
        System.out.println(tPoint.getAllInfo());
      }
    }
  }

  public int size() {
    return this.pointNumber;
  }

  public int validSize() {
    return this.pointNumber;
  }

}
