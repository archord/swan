/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author xy
 */
public class LineTest {

  private int imgWidth = 3072;
  private int imgHeight = 3072;
  private int thetaSize = 180;
  private int rhoSize = 100;
  private int thetaRange = 36;
  private int rhoRange = 10;
  private int maxHoughFrameNunmber = 10;
  private int validLineMinPoint = 5;
  private float maxDistance = 100;
  private int minValidPoint = 5;
  private float rhoErrorTimes = (float) 0.2; //0.2

  ArrayList<OtObserveRecord> ot1list = new ArrayList();

  /**
   * 一阶拟合
   */
  public void fitTest11(ArrayList<OtObserveRecord> ot1list) {

//      WeightedObservedPoints objs1 = new WeightedObservedPoints();
    List<WeightedObservedPoint> objs1 = new ArrayList();
    WeightedObservedPoints objs2 = new WeightedObservedPoints();
    WeightedObservedPoints objs3 = new WeightedObservedPoints();
    PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);

    for (OtObserveRecord ot1 : ot1list) {
      long t = ot1.getDateUt().getTime();
      objs1.add(new WeightedObservedPoint(1, ot1.getX(), ot1.getY()));
      objs2.add(t, ot1.getY());
      objs3.add(t, ot1.getX());
    }
    final double[] coeff1 = fitter.fit(objs1);
    final double[] coeff2 = fitter.fit(objs2.toList());
    final double[] coeff3 = fitter.fit(objs3.toList());

    int num = 0;
    for (OtObserveRecord ot1 : ot1list) {
      long t = ot1.getDateUt().getTime();
      double preY1 = coeff1[0] + coeff1[1] * ot1.getX() + coeff1[2] * ot1.getX() * ot1.getX();
      double preY2 = coeff2[0] + coeff2[1] * t + coeff2[2] * t * t;
      double preX1 = coeff3[0] + coeff3[1] * t + coeff3[2] * t * t;
      double ydiff1 = preY1 - ot1.getY();
      double ydiff2 = preY2 - ot1.getY();
      double xdiff1 = preX1 - ot1.getX();
      if (Math.abs(ydiff1) > 10 || Math.abs(ydiff2) > 10 || Math.abs(xdiff1) > 10) {
        System.out.println("**********");
      }
//        if (Math.abs(ydiff1) > 10) {
//          System.out.println("**********");
//        }
      String rst = String.format("%5d %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f",
              ot1.getFfNumber(), ot1.getX(), ot1.getY(), preY1, ydiff1, preY2, ydiff2, preX1, xdiff1);
      System.out.println(rst);
    }
    num++;
  }

  /**
   * 三阶拟合
   */
  public void fitTest3(ArrayList<OtObserveRecord> ot1list) {

    WeightedObservedPoints objs1 = new WeightedObservedPoints();
    WeightedObservedPoints objs2 = new WeightedObservedPoints();
    WeightedObservedPoints objs3 = new WeightedObservedPoints();
    PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);

    for (OtObserveRecord ot1 : ot1list) {
      long t = ot1.getDateUt().getTime();
      objs1.add(ot1.getX(), ot1.getY());
      objs2.add(t, ot1.getY());
      objs3.add(t, ot1.getX());
    }
    final double[] coeff1 = fitter.fit(objs1.toList());
    final double[] coeff2 = fitter.fit(objs2.toList());
    final double[] coeff3 = fitter.fit(objs3.toList());

    int num = 0;
    for (OtObserveRecord ot1 : ot1list) {
      long t = ot1.getDateUt().getTime();
      double preY1 = coeff1[0] + coeff1[1] * ot1.getX() + coeff1[2] * ot1.getX() * ot1.getX() + coeff1[3] * ot1.getX() * ot1.getX() * ot1.getX();
      double preY2 = coeff2[0] + coeff2[1] * t + coeff2[2] * t * t + coeff2[3] * t * t * t;
      double preX1 = coeff3[0] + coeff3[1] * t + coeff3[2] * t * t + coeff3[3] * t * t * t;
      double ydiff1 = preY1 - ot1.getY();
      double ydiff2 = preY2 - ot1.getY();
      double xdiff1 = preX1 - ot1.getX();
      if (Math.abs(ydiff1) > 10 || Math.abs(ydiff2) > 10 || Math.abs(xdiff1) > 10) {
        System.out.println("**********");
      }
//          if (Math.abs(ydiff1) > 10) {
//            System.out.println("**********");
//          }
      String rst = String.format("%5d %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f",
              ot1.getFfNumber(), ot1.getX(), ot1.getY(), preY1, ydiff1, preY2, ydiff2, preX1, xdiff1);
      System.out.println(rst);
    }
    num++;
  }

  public void fitTest(ArrayList<OtObserveRecord> ot1list) {

    SimpleRegression reg = new SimpleRegression();
    for (OtObserveRecord ot1 : ot1list) {
      reg.addData(ot1.getX(), ot1.getY());
    }
    double sigma = Math.sqrt(reg.getSumSquaredErrors() / (ot1list.size() - 1));
    System.out.println("sigma=" + sigma);
    double sigma2 = Double.MAX_VALUE;
    int idx = 1;
//      while (sigma < sigma2) {
//        System.out.println("regression " + idx++);
//        for (int i = 0; i < ot1list.size();) {
//          OtObserveRecord ot1 = ot1list.get(i);
//          float preY = (float) reg.predict(ot1.getX());
//          double ydiff = preY - ot1.getY();
//          if ((Math.abs(ydiff) > 2 * sigma)) {
//            System.out.println(ydiff + "\t" + (Math.abs(ydiff) < 2 * sigma) + "\t" + (Math.abs(ydiff) < 3 * sigma));
//            ot1list.remove(i);
//            reg.removeData(ot1.getX(), ot1.getY());
//          } else {
//            i++;
//          }
//        }
//        sigma2 = sigma;
//        sigma = Math.sqrt(reg.getSumSquaredErrors() / (ot1list.size() - 1));
//        if (idx > 10) {
//          break;
//        }
//      }

    for (OtObserveRecord ot1 : ot1list) {
      float preY = (float) reg.predict(ot1.getX());
      double ydiff = preY - ot1.getY();
      System.out.println(ot1.getFfNumber() + ": " + ot1.getX() + "\t" + ot1.getY() + "\t"
              + preY + "\t" + ydiff + "\t" + (Math.abs(ydiff) < 2 * sigma) + "\t" + (Math.abs(ydiff) < 3 * sigma));
    }
  }

  public void fitTest4() {

    WeightedObservedPoints objs1 = new WeightedObservedPoints();
    WeightedObservedPoints objs2 = new WeightedObservedPoints();
    WeightedObservedPoints objs3 = new WeightedObservedPoints();
    PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);

    int num = 0;
    for (OtObserveRecord ot1 : ot1list) {
      long t = ot1.getDateUt().getTime();
      if (num >= 5) {
        final double[] coeff1 = fitter.fit(objs1.toList());
        final double[] coeff2 = fitter.fit(objs2.toList());
        final double[] coeff3 = fitter.fit(objs3.toList());
        double preY1 = coeff1[0] + coeff1[1] * ot1.getX() + coeff1[2] * ot1.getX() * ot1.getX();
        double preY2 = coeff2[0] + coeff2[1] * t + coeff2[2] * t * t;
        double preX1 = coeff3[0] + coeff3[1] * t + coeff3[2] * t * t;
        double ydiff1 = preY1 - ot1.getY();
        double ydiff2 = preY2 - ot1.getY();
        double xdiff1 = preX1 - ot1.getX();
        if (Math.abs(ydiff1) > 10 || Math.abs(ydiff2) > 10 || Math.abs(xdiff1) > 10) {
          System.out.println("**********");
          objs1.clear();
          objs2.clear();
          objs3.clear();
          num = 0;
        } else {
          objs1.add(ot1.getX(), ot1.getY());
          objs2.add(t, ot1.getY());
          objs3.add(t, ot1.getX());
        }
        String rst = String.format("%5d %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f %9.4f",
                ot1.getFfNumber(), ot1.getX(), ot1.getY(), preY1, ydiff1, preY2, ydiff2, preX1, xdiff1);
        System.out.println(rst);
      } else {
        objs1.add(ot1.getX(), ot1.getY());
        objs2.add(t, ot1.getY());
        objs3.add(t, ot1.getX());
      }
      num++;
    }
  }

  public void fitTest30() {
    final WeightedObservedPoints obs = new WeightedObservedPoints();
    obs.add(-1.00, 2.021170021833143);
    obs.add(-0.99, 2.221135431136975);
    obs.add(-0.98, 2.09985277659314);
    obs.add(-0.97, 2.0211192647627025);
    obs.add(0.99, -2.4345814727089854);

    final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
    final double[] coeff = fitter.fit(obs.toList());
    for (int i = 0; i < coeff.length; i++) {
      System.out.println(coeff[i]);
    }
  }

  public void test2(ArrayList<OtObserveRecord> ot1list) {

    for (int i = 0; i < ot1list.size() - 1; i++) {
      OtObserveRecord ot1 = ot1list.get(i);
      OtObserveRecord ot2 = ot1list.get(i + 1);
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
              i, ot1.getFfNumber(), ot1.getX(), ot1.getY(), rho, rho2,
              theta, ktheta * 180 / Math.PI, theta2 * 180 / Math.PI));
    }
  }

  public float getRho(float x, float y, float cenX, float cenY, float theta) {
    float rho = (float) ((((x - cenX) * Math.cos(theta)) + ((y - cenY) * Math.sin(theta)) + 2172.232));
    return rho;
  }
}
