/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecord;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author xy
 */
public class LineObject {

  public char lineType; //0:未分类；1：多帧单点；2：多帧多点；3：一帧多点

  public PolynomialCurveFitter fitter;
  public WeightedObservedPoints xyList;
  public WeightedObservedPoints txList;
  public WeightedObservedPoints tyList;
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

    fitter = PolynomialCurveFitter.create(2);
    xyList = new WeightedObservedPoints();
    txList = new WeightedObservedPoints();
    tyList = new WeightedObservedPoints();

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
    if (!endLine && lastFrameNumber < frameNumber) {
      this.removeSinglePointOfMultiFrame();
      this.analysis();
      this.updateInfo();
      this.statistic();
      this.findFirstAndLastPoint();
      endLine = true;
    }
    return endLine;
  }

  /**
   * 将目标分为3类，并对framePointMaxNumber大于等于2而且this.avgFramePointNumber小于等于2的目标一帧中的多个点合并为1个点
   * 使用顺序 mvObj.analysis(); mvObj.updateInfo(); mvObj.statistic();
   * mvObj.findFirstAndLastPoint();
   */
  public void analysis() {
    if (this.frameList.size() == 1 && this.avgFramePointNumber > 2) {
      lineType = '3';
    } else {
      if (this.framePointMaxNumber > 2 && this.avgFramePointNumber > 2) {
        lineType = '2';
      } else if (this.framePointMaxNumber > 2 && this.avgFramePointNumber <= 2) {
        if (this.framePointMultiNumber == 1) {
          lineType = '2';
        } else {
          lineType = '1';
        }
      } else if (this.framePointMaxNumber <= 2 && this.avgFramePointNumber > 1 && this.avgFramePointNumber <= 2) {
        lineType = '1';
      } else if (this.avgFramePointNumber <= 1) {
        lineType = '1';
      }
    }

    if (false && lineType == '1' && this.avgFramePointNumber > 1) {

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
          HoughtPoint thp = new HoughtPoint(fp.getpIdx(), fp.getFrameNumber(), avgX.floatValue(), avgY.floatValue(), fp.getDateUtc(), fp.getOorId());

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

  public void statistic() {
    SummaryStatistics xyStat = new SummaryStatistics();
    SummaryStatistics tyStat = new SummaryStatistics();
    SummaryStatistics txStat = new SummaryStatistics();
    for (HoughtPoint hp : this.pointList) {
      double preYDiff = Math.abs(hp.getY() - preNextYByX(hp.getX()));
      double preYDiff2 = Math.abs(hp.getY() - preNextYByT(hp.getDateUtc().getTime()));
      double preXDiff = Math.abs(hp.getX() - preNextXByT(hp.getDateUtc().getTime()));
      xyStat.addValue(preYDiff);
      tyStat.addValue(preYDiff2);
      txStat.addValue(preXDiff);
    }
    xySigma = xyStat.getStandardDeviation();
    tySigma = tyStat.getStandardDeviation();
    txSigma = txStat.getStandardDeviation();
    xySigmaMax = xyStat.getMax();
    tySigmaMax = tyStat.getMax();
    txSigmaMax = txStat.getMax();
    xySigmaMean = xyStat.getMean();
    tySigmaMean = tyStat.getMean();
    txSigmaMean = txStat.getMean();
  }

  public void cloneLine(HoughLine hl) {
    this.frameList = hl.frameList;
    this.pointList = hl.pointList;
    this.pointNumber = hl.pointNumber;
    this.lastFrameNumber = hl.lastFrameNumber;
    this.lastPoint = hl.lastPoint;
    hl.clearAll();

    for (HoughtPoint tp : pointList) {
      xyList.add(tp.getX(), tp.getY());
      txList.add(tp.getDateUtc().getTime(), tp.getX());
      tyList.add(tp.getDateUtc().getTime(), tp.getY());
    }
    xyCoeff = fitter.fit(xyList.toList());
    txCoeff = fitter.fit(txList.toList());
    tyCoeff = fitter.fit(tyList.toList());

    updateInfo();
  }

  public boolean isOnLine(OtObserveRecord ot1) {

    boolean isOnLine = false;
    double preYDiff = Math.abs(ot1.getY() - preNextYByX(ot1.getX()));
    if (preYDiff < 10) {
      if (this.framePointMaxNumber > 2) {
        if (this.frameList.size() >= 2 && ot1.getFfNumber() != this.lastFrameNumber) {
          float xDelta = ot1.getX() - lastPoint.getX();
          float yDelta = ot1.getY() - lastPoint.getY();
          isOnLine = (xDelta * deltaX > 0) && (yDelta * deltaY > 0);
        } else {
          isOnLine = true;
        }
      } else {
        double preXDiff = Math.abs(ot1.getX() - preNextXByT(ot1.getDateUt().getTime()));
        double preYDiff2 = Math.abs(ot1.getY() - preNextYByT(ot1.getDateUt().getTime()));
        if (preXDiff < 10 && preYDiff2 < 10) {
          isOnLine = true;
        }
      }
    }
    return isOnLine;
  }

  public void addPoint(int pIdx, int frameNumber, float x, float y, Date dateUtc, long oorId) {
    this.addPoint(new HoughtPoint(pIdx, frameNumber, x, y, dateUtc, oorId));
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

    xyList.add(hp.getX(), hp.getY());
    txList.add(hp.getDateUtc().getTime(), hp.getX());
    tyList.add(hp.getDateUtc().getTime(), hp.getY());
    xyCoeff = fitter.fit(xyList.toList());
    txCoeff = fitter.fit(txList.toList());
    tyCoeff = fitter.fit(tyList.toList());

    if (frameList.isEmpty() || (lastFrameNumber != hp.getFrameNumber())) {
      lastFrameNumber = hp.getFrameNumber();
      HoughFrame hframe = new HoughFrame(hp, hp.getFrameNumber());
      frameList.add(hframe);
    } else {
      HoughFrame lastFrame = frameList.get(frameList.size() - 1);
      lastFrame.addPoint(hp);
    }

    updateInfo();
  }

  public double preNextYByX(double x) {
    return xyCoeff[0] + xyCoeff[1] * x + xyCoeff[2] * x * x;
  }

  public double preNextYByT(double t) {
    return tyCoeff[0] + tyCoeff[1] * t + tyCoeff[2] * t * t;
  }

  public double preNextXByT(double t) {
    return txCoeff[0] + txCoeff[1] * t + txCoeff[2] * t * t;
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
      HoughtPoint lPoint = pointList.get(this.pointNumber - 1);

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
   * 需要帧数大于等于2
   */
  public void findFirstAndLastPoint() {

    getDelta();
    sort(pointList, new CompareMethod1());
    firstPoint = pointList.get(0);
    lastPoint = pointList.get(this.pointNumber - 1);
    this.firstFrameNumber = firstPoint.getFrameNumber();
  }

  /**
   * 删除多帧多点（type=2）类型中，只有一个点的帧。该操作意义不大，多帧多点中也可能出现只有一个点的帧
   */
  public void removeSinglePointOfMultiFrame() {
    if (avgFramePointNumber > 2) {
      for (int i = 0; i < this.frameList.size(); i++) {
        HoughFrame tframe = this.frameList.get(i);
        if (tframe.pointList.size() < 2) {
          this.frameList.remove(i);
          this.pointNumber -= tframe.pointList.size();
          for (HoughtPoint tpoint : tframe.pointList) {
            this.pointList.remove(tpoint);
          }
          i--;
        }
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

  //添加对List对象进行排序的功能，参考了Java中的Java.util.Collections类的sort()函数
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

  public String getOutLineInfo() {
    String rst = String.format("frameNumber:%3d,pointNumber:%3d,framePointMaxNumber:%3d,framePointMultiNumber:%3d,avgFramePointNumber:%4.1f,xySigma:%4.1f,tySigma:%4.1f,txSigma:%4.1f",
            this.frameList.size(), this.pointNumber, this.framePointMaxNumber, this.framePointMultiNumber, this.avgFramePointNumber, this.xySigma, this.tySigma, this.txSigma);
    return rst;
  }

  public void printInfo(ArrayList<OtObserveRecord> historyOT1s) {

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

}
