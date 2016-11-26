/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecord;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author xy
 */
public class LineObject {

  public ArrayList<HoughFrame> frameList;
  public ArrayList<HoughtPoint> pointList;
  public int pointNumber;
  public float avgFramePointNumber;

  public float imgXCenter;
  public float imgYCenter;
  public float halfRho;

  public int firstFrameNumber;
  public int lastFrameNumber;
  public HoughtPoint firstPoint; //minNumber
  public HoughtPoint lastPoint; //maxNumber

  public float theta;
  public float rho;
  public float lastRho;
  public float lastTheta;
  public float lineRho; //根据直线的第一个和最后一个点算的rho
  public float lineTheta;  //根据直线的第一个和最后一个点算的theta

  public float sinTheta;
  public float cosTheta;

  public float deltaX;
  public float deltaY;

  public float speedX;
  public float speedY;

  public boolean endLine;

  /**
   *
   * @param theta
   * @param rho
   * @param imgXCenter
   * @param imgYCenter
   * @param halfRho
   */
  public LineObject(float theta, float rho, float imgXCenter, float imgYCenter, float halfRho) {
    this.theta = theta;
    this.rho = rho;
    this.imgXCenter = imgXCenter;
    this.imgYCenter = imgYCenter;
    this.halfRho = halfRho;
    this.frameList = new ArrayList();
    this.pointList = new ArrayList();
    this.pointNumber = 0;
    this.avgFramePointNumber = 0;

    this.firstFrameNumber = Integer.MAX_VALUE;
    this.lastFrameNumber = Integer.MIN_VALUE;

    this.sinTheta = (float) Math.sin(this.theta);
    this.cosTheta = (float) Math.cos(this.theta);

    this.endLine = false;
  }

  /**
   * 在判定直线结束后，对直线进行分析
   */
  public void analysis() {
    if (avgFramePointNumber > 2) {
      for (int i = 0; i < this.frameList.size(); i++) {
        HoughFrame tframe = this.frameList.get(i);
        if (tframe.pointList.size() <= 2) {
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

  public void calAllSpeed() {
    int i = 0;
    for (HoughtPoint tp : this.pointList) {
      if (i > 0) {
        HoughtPoint apoint = pointList.get(i - 1);
        if (tp.getFrameNumber() == apoint.getFrameNumber()) {
          if (i - 2 < 0) {
            continue;
          }
          apoint = pointList.get(i - 2);
        }
        tp.calSpeed(apoint);
      }
      i++;
    }
  }

  public void cloneLine(HoughLine hl) {
    this.frameList = hl.frameList;
    this.pointList = hl.pointList;
    this.pointNumber = hl.pointNumber;
    this.lastFrameNumber = hl.lastFrameNumber;
    this.lastPoint = hl.lastPoint;
    hl.clearAll();

    avgFramePointNumber = (float) (this.pointNumber * 1.0 / this.frameList.size());
    findFirstAndLastPoint();

    updateThetaRho();
    calculateSpeed();
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

  public void addPoint(int pIdx, int frameNumber, float theta, float rho, float x, float y, Date dateUtc, long oorId) {
    this.addPoint(new HoughtPoint(pIdx, frameNumber, theta, rho, x, y, dateUtc, oorId));
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

    avgFramePointNumber = (float) (this.pointNumber * 1.0 / this.frameList.size());
    findFirstAndLastPoint();

    updateThetaRho();
    calculateSpeed();
  }

  public boolean isEndLine(int frameNumber) {
    if (!endLine && lastFrameNumber < frameNumber) {
      this.analysis();
      this.avgFramePointNumber = (float) (this.pointNumber * 1.0 / this.frameList.size());
      this.findFirstAndLastPoint();
      endLine = true;
    }
    return endLine;
  }

  public boolean matchLastPoint(OtObserveRecord ot1, float maxDistance) {

    boolean flag = true;
    if (this.pointNumber > 0) {
      double distance = CommonFunction.getLineDistance(ot1.getX(), ot1.getY(), lastPoint.getX(), lastPoint.getY());
      boolean deltaFlag = true;
      if (this.frameList.size() >= 2) {
        float xDelta = ot1.getX() - lastPoint.getX();
        float yDelta = ot1.getY() - lastPoint.getY();
        deltaFlag = (xDelta * deltaX > 0) && (yDelta * deltaY > 0);
      }
      flag = (distance < maxDistance) && deltaFlag;
    }
    return flag;
  }

  public boolean matchLastPoint2(OtObserveRecord ot1, float maxDistance) {

    boolean distFlag = true;
    if (this.frameList.size() == 1) {
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
          //*************************用直线的delta来计算?
//          lastPoint = lastFrame.findLastPointUsingDelta(ot1, this.deltaX, this.deltaY);
        }
      }
    }
    double distance = CommonFunction.getLineDistance(ot1.getX(), ot1.getY(), lastPoint.getX(), lastPoint.getY());
    distFlag = distance < maxDistance;

    boolean deltaFlag = true;
    if (this.frameList.size() >= 2) {
      float xDelta = ot1.getX() - lastPoint.getX();
      float yDelta = ot1.getY() - lastPoint.getY();
      deltaFlag = (xDelta * deltaX > 0) && (yDelta * deltaY > 0);
    }

    boolean speedFlag = true;
    if (this.frameList.size() >= 3 && avgFramePointNumber < 2) {
      float xDelta = ot1.getX() - lastPoint.getX();
      float yDelta = ot1.getY() - lastPoint.getY();
//      int deltaTime = ot1.getFrameNumber() - lastPoint.getFrameNumber();
      long deltaTime = ot1.getDateUt().getTime() - lastPoint.getDateUtc().getTime();
//      speedFlag = (Math.abs(xDelta - this.speedX * deltaTime) < this.speedX * 0.5) && (Math.abs(yDelta - this.speedY * deltaTime) < this.speedY * 0.5);
//      speedFlag = (Math.abs(xDelta - this.speedX * deltaTime) < 5) && (Math.abs(yDelta - this.speedY * deltaTime) < 5);

    }

    boolean flag = distFlag & deltaFlag & speedFlag;
    return flag;
  }

  /**
   * 1，帧数=1，匹配距离; 2，帧数=2，匹配方向; 3，帧数>=3,匹配速度
   *
   * @param ot1
   * @param maxDistance 新目标与直线最后一个点的距离不超过maxDpListstance
   * @return
   */
  public boolean matchLastPoint3(OtObserveRecord ot1, float maxDistance) {

    boolean flag = true;
    boolean deltaFlag = true;
    boolean speedFlag = true;
    if (this.frameList.size() <= 2) {
      double distance = CommonFunction.getLineDistance(ot1.getX(), ot1.getY(), lastPoint.getX(), lastPoint.getY());
      flag = distance < maxDistance;
      if (this.frameList.size() == 2) {
        float xDelta = ot1.getX() - lastPoint.getX();
        float yDelta = ot1.getY() - lastPoint.getY();
        deltaFlag = (xDelta * deltaX > 0) && (yDelta * deltaY > 0);
      }
    } else {
      if (avgFramePointNumber < 2) {
        float xDelta = ot1.getX() - lastPoint.getX();
        float yDelta = ot1.getY() - lastPoint.getY();
        int deltaTime = ot1.getFfNumber()- lastPoint.getFrameNumber();
        speedFlag = (Math.abs(xDelta - this.speedX * deltaTime) < this.speedX * 0.5) && (Math.abs(yDelta - this.speedY * deltaTime) < this.speedY * 0.5);
      } else {
        if (this.frameList.size() >= 2) {
          HoughFrame lastFrame = this.frameList.get(this.frameList.size() - 1);
          HoughFrame lastFrame2 = this.frameList.get(this.frameList.size() - 2);
          int deltaTime = lastFrame.frameNumber - lastFrame2.frameNumber;
          float xDelta;
          float yDelta;
          if (deltaX > 0) {
            xDelta = ot1.getX() - lastFrame2.minX.getX();
          } else {
            xDelta = ot1.getX() - lastFrame2.maxX.getX();
          }
          if (deltaY > 0) {
            yDelta = ot1.getY() - lastFrame2.minY.getX();
          } else {
            yDelta = ot1.getY() - lastFrame2.maxY.getX();
          }
          speedFlag = (Math.abs(xDelta - this.speedX * deltaTime) < this.speedX * 1) && (Math.abs(yDelta - this.speedY * deltaTime) < this.speedY * 1);
        }
      }
    }
    return flag & deltaFlag & speedFlag;
  }

  public void calculateSpeed() {

    if (avgFramePointNumber < 2) {
      HoughtPoint lastPoint2 = pointList.get(this.pointNumber - 2);
      float tdeltaX = lastPoint.getX() - lastPoint2.getX();
      float tdeltaY = lastPoint.getY() - lastPoint2.getY();
//      int deltaTime = lastPoint.getFrameNumber() - lastPoint2.getFrameNumber();
      long deltaTime = lastPoint.getDateUtc().getTime() - lastPoint2.getDateUtc().getTime();
      this.speedX = tdeltaX / deltaTime;
      this.speedY = tdeltaY / deltaTime;
    } else {
      if (this.frameList.size() >= 2) {
        HoughFrame lastFrame = this.frameList.get(this.frameList.size() - 1);
        HoughFrame lastFrame2 = this.frameList.get(this.frameList.size() - 2);
        int deltaTime = lastFrame.frameNumber - lastFrame2.frameNumber;
        float tdeltaX;
        float tdeltaY;
        if (deltaX > 0) {
          tdeltaX = lastFrame.minX.getX() - lastFrame2.minX.getX();
        } else {
          tdeltaX = lastFrame.maxX.getX() - lastFrame2.maxX.getX();
        }
        if (deltaY > 0) {
          tdeltaY = lastFrame.minY.getX() - lastFrame2.minY.getX();
        } else {
          tdeltaY = lastFrame.maxY.getX() - lastFrame2.maxY.getX();
        }
        this.speedX = tdeltaX / deltaTime;
        this.speedY = tdeltaY / deltaTime;
      }
    }
  }

  public boolean lineMatch() {

    return false;
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

  public void updateThetaRho() {

    if (this.pointNumber >= 2) {
      HoughtPoint hp = pointList.get(this.pointNumber - 2);
      hp.calKtheta(pointList.get(this.pointNumber - 1), imgXCenter, imgYCenter, halfRho);
      this.lastTheta = hp.getTheta2();
      this.lastRho = hp.getRho2();

      this.sinTheta = (float) Math.sin(this.lastTheta);
      this.cosTheta = (float) Math.cos(this.lastTheta);
    }
  }

  /**
   * 需要帧数大于等于2
   */
  public void getDelta() {

    if (frameList.size() > 1) {
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
//      deltaX = ffMinPoint.getX() - lfMinPoint.getX();
//      deltaY = ffMinPoint.getY() - lfMinPoint.getY();
    } else {
      HoughFrame firstFrame = frameList.get(0);
      deltaX = firstFrame.deltaX;
      deltaY = firstFrame.deltaY;
    }
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

  public void removeFirstFrame() {
    HoughFrame firstFrame = frameList.get(0);
    this.pointNumber -= firstFrame.pointList.size();
    firstFrame.removeAll();
    frameList.remove(0);
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

  public void printInfo(ArrayList<OtObserveRecord> historyOT1s) {

    int i = 1;
    for (HoughFrame tFrame : frameList) {
      System.out.println(String.format("frame%03d, %4d, %2d", i, tFrame.frameNumber, tFrame.pointList.size()));
      for (HoughtPoint tPoint : tFrame.pointList) {
        tPoint.printInfo();
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

  public void printOT1Info2(ArrayList<OtObserveRecord> historyOT1s) {

    int i = 0;
    for (HoughFrame tFrame : frameList) {
      for (HoughtPoint tPoint : tFrame.pointList) {
        OtObserveRecord ot1 = historyOT1s.get(tPoint.getpIdx());
//        ot1.printInfo();
        i++;
      }
    }
  }

  public void printOT1Info(ArrayList<OtObserveRecord> historyOT1s) {

    int i = 0;
    for (HoughtPoint tPoint : pointList) {
      OtObserveRecord ot1 = historyOT1s.get(tPoint.getpIdx());
//      ot1.printInfo();
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
