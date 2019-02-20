/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecordMovObj;
import java.util.ArrayList;

/**
 *
 * @author xy
 */
public class HoughFrame {

  int frameNumber;
  ArrayList<HoughtPoint> pointList;

  HoughtPoint minX;
  HoughtPoint maxX;
  HoughtPoint minY;
  HoughtPoint maxY;
  float deltaX;
  float deltaY;

  /**
   * 必须使用该构造函数初始化，然后用addPoint添加点。
   *
   * @param hp
   * @param frameNumber
   */
  public HoughFrame(HoughtPoint hp, int frameNumber) {
    this.frameNumber = frameNumber;
    this.pointList = new ArrayList();
    this.pointList.add(hp);

    minX = hp;
    maxX = hp;
    minY = hp;
    maxY = hp;
  }

  /**
   * 当ot1的帧编号与直线最后一帧的帧编号相同时，找两端的点比较距离不准确，而应该找最近的点来比较
   *
   * @param ot1
   * @return
   */
  public HoughtPoint findNearestPoint(OtObserveRecordMovObj ot1) {

    HoughtPoint nearest = null;
    double distance = Double.MAX_VALUE;
    for (HoughtPoint tpoint : this.pointList) {
      double tDist = CommonFunction.getLineDistance(ot1.getX(), ot1.getY(), tpoint.getX(), tpoint.getY());
      if (tDist < distance) {
        distance = tDist;
        nearest = tpoint;
      }
    }
    return nearest;
  }

  /**
   * 当ot1的帧编号与直线最后一帧的帧编号不同时，可直接与最后一帧中的最小值和最大值比较
   *
   * @param ot1
   * @return
   */
  public HoughtPoint findLastPoint(OtObserveRecordMovObj ot1) {
    HoughtPoint minPoint, maxPoint, lastPoint;
    if (Math.abs(deltaX) > Math.abs(deltaY)) {
      maxPoint = maxX;
      minPoint = minX;
    } else {
      maxPoint = maxY;
      minPoint = minY;
    }
    double distance1 = CommonFunction.getLineDistance(ot1.getX(), ot1.getY(), minPoint.getX(), minPoint.getY());
    double distance2 = CommonFunction.getLineDistance(ot1.getX(), ot1.getY(), maxPoint.getX(), maxPoint.getY());
    lastPoint = distance1 < distance2 ? minPoint : maxPoint;
    return lastPoint;
  }

  public HoughtPoint findLastPointUsingDelta(OtObserveRecordMovObj ot1, float lineDeltaX, float lineDeltaY) {
    HoughtPoint lastPoint;
    if (Math.abs(lineDeltaX) > Math.abs(lineDeltaY)) {
      if (lineDeltaX > 0) {
        lastPoint = maxX;
      } else {
        lastPoint = minX;
      }
    } else {
      if (lineDeltaY > 0) {
        lastPoint = maxY;
      } else {
        lastPoint = minY;
      }
    }
    return lastPoint;
  }

  public final void addPoint(HoughtPoint hp) {
    this.pointList.add(hp);
    findMinAndMaxXY(hp);
  }

  public void removePoint(int i) {
    this.pointList.remove(i);
    if (!pointList.isEmpty()) {
      findMinAndMaxXY();
    }
  }

  public void removeAll() {
    this.pointList.clear();
  }

  public void findMinAndMaxXY() {
    int i = 0;
    for (HoughtPoint hp : pointList) {
      if (i == 0) {
        minX = hp;
        maxX = hp;
        minY = hp;
        maxY = hp;
      } else {
        findMinAndMaxXY(hp);
      }
      i++;
    }
  }

  /**
   * 每新增加一个点，就更新一次X、Y的最大值最小值
   *
   * @param hp
   */
  public void findMinAndMaxXY(HoughtPoint hp) {

    float tx = hp.getX();
    float ty = hp.getY();
    if (tx < minX.getX()) {
      minX = hp;
    }
    if (tx > maxX.getX()) {
      maxX = hp;
    }
    if (ty < minY.getY()) {
      minY = hp;
    }
    if (ty > maxY.getY()) {
      maxY = hp;
    }
    deltaX = maxX.getX() - minX.getX();
    deltaY = maxY.getY() - minY.getY();
  }

}
