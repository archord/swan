/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.linefind;

import com.gwac.model.OtObserveRecord;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xy
 */
public class ResultCompare {

  int minOt1Num = 20;

  public void compare() {
    String allPoint = "E:\\linecompare\\allPoint.png";
    String dirPath1 = "E:\\linecompare\\161228-4-32-0-0";
    String dirPath2 = "E:\\linecompare\\161228-4-32-lxm";

    System.out.println("This program get objs which has more than " + minOt1Num + " ot1s.");
    List<String> flist1 = getFileNameList(dirPath1);
    List<String> flist2 = getFileNameList(dirPath2);
    System.out.println("xy result have " + flist1.size() + " valid files.");
    System.out.println("xm result has " + flist2.size() + " valid files.");

    List<List<OtObserveRecord>> objs1 = getObjList(dirPath1);
    List<List<OtObserveRecord>> objs2 = getObjList(dirPath2);

    Set<Long> set1 = getAllOt1Set(objs1);
    Set<Long> set2 = getAllOt1Set(objs2);
    int num1 = set1.size();
    int num2 = set2.size();
    System.out.println("xy result have " + objs1.size() + " lines, with " + num1 + " ot1.");
    System.out.println("xm result has " + objs2.size() + " lines, with " + num2 + " ot1.");

    set1.retainAll(set2);
    int shareNum = set1.size();
    System.out.println("exist in xy and xm " + shareNum + " ot1.");
    System.out.println("Start match and compare each line");

    List<Set<Long>> setList1 = getObjOt1Set(objs1);
    List<Set<Long>> setList2 = getObjOt1Set(objs2);

    Set<Integer> matchSet1 = new HashSet();
    Set<Integer> matchSet2 = new HashSet();
    int i = 0;
    for (Set<Long> tset1 : setList1) {
      int j = 0;
      boolean mflag = false;
      for (Set<Long> tset2 : setList2) {
        Set<Long> tset = new HashSet();
        tset.addAll(tset2);
        tset.retainAll(tset1);
        if (tset.size() > 10) {
          mflag = true;
          matchSet2.add(j);
          System.out.println(flist1.get(i) + " has " + tset1.size() + " ot1s, "
                  + flist2.get(j) + " has " + tset2.size() + " ot1s, same ot1 " + tset.size() + ".");
        }
        j++;
      }
      if (mflag) {
        matchSet1.add(i);
      }
      i++;
    }
    System.out.println("xy has " + matchSet1.size() + " objs match xm's " + matchSet2.size() + " objs.");

    drawPoint(allPoint, objs1, objs2);
  }

  public void drawPoint(String fName, List<List<OtObserveRecord>> objs1, List<List<OtObserveRecord>> objs2) {

    int shift = 5;
    int imgWidthM = 3056;
    int imgHeightM = 3056;

    BufferedImage image = new BufferedImage(imgWidthM + shift, imgHeightM + shift, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    BasicStroke bs = new BasicStroke(2);
    g2d.setBackground(Color.WHITE);
    g2d.fillRect(0, 0, imgWidthM + shift, imgHeightM + shift);
    g2d.setStroke(bs);
    int pointSize2 = 4;

    g2d.setColor(Color.BLUE);
    int i = 1;
    for (List<OtObserveRecord> obj : objs1) {
      for (OtObserveRecord ot1 : obj) {
        int x = (int) (ot1.getX() - pointSize2 / 2);
        int y = (int) (ot1.getY() - pointSize2 / 2);
        g2d.drawRect(x, y, pointSize2, pointSize2);
      }
      OtObserveRecord firstOT1 = obj.get(0);
      OtObserveRecord lastOT1 = obj.get(obj.size() - 1);
      int x1 = firstOT1.getX().intValue();
      int y1 = firstOT1.getY().intValue();
      int x2 = lastOT1.getX().intValue();
      int y2 = lastOT1.getY().intValue();
      g2d.drawLine(x1, y1, x2, y2);
      String drawStr = "" + (i++);
      g2d.drawString(drawStr, (int) x2 + pointSize2, (int) y2 + pointSize2);
    }

    g2d.setColor(Color.RED);
    for (List<OtObserveRecord> obj : objs2) {
      for (OtObserveRecord ot1 : obj) {
        int x = (int) (ot1.getX() - pointSize2 / 2);
        int y = (int) (ot1.getY() - pointSize2 / 2);
        g2d.drawRect(x + shift, y + shift, pointSize2, pointSize2);
      }
      OtObserveRecord firstOT1 = obj.get(0);
      OtObserveRecord lastOT1 = obj.get(obj.size() - 1);
      int x1 = firstOT1.getX().intValue() + shift;
      int y1 = firstOT1.getY().intValue() + shift;
      int x2 = lastOT1.getX().intValue() + shift;
      int y2 = lastOT1.getY().intValue() + shift;
      g2d.drawLine(x1, y1, x2, y2);
      String drawStr = "" + (i++);
      g2d.drawString(drawStr, (int) x2 + pointSize2, (int) y2 + pointSize2);
    }

    try {
      javax.imageio.ImageIO.write(image, "png", new File(fName));
    } catch (IOException ex) {
      Logger.getLogger(FindMoveObject.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public List<Set<Long>> getObjOt1Set(List<List<OtObserveRecord>> objs) {
    List<Set<Long>> setList = new ArrayList();
    for (List<OtObserveRecord> obj : objs) {
      Set<Long> tset = new HashSet();
      for (OtObserveRecord ot1 : obj) {
        tset.add(ot1.getOorId());
      }
      setList.add(tset);
    }
    return setList;
  }

  public Set<Long> getAllOt1Set(List<List<OtObserveRecord>> objs) {
    Set<Long> tset = new HashSet();
    for (List<OtObserveRecord> obj : objs) {
      for (OtObserveRecord ot1 : obj) {
        tset.add(ot1.getOorId());
      }
    }
    return tset;
  }

  public int getOt1Num(List<List<OtObserveRecord>> objs) {
    int tnum = 0;
    for (List<OtObserveRecord> obj : objs) {
      tnum += obj.size();
    }
    return tnum;
  }

  public List<List<OtObserveRecord>> getObjList(String path) {
    List<List<OtObserveRecord>> objs = new ArrayList();
    File dir1 = new File(path);
    File[] flist1 = dir1.listFiles();
    for (File tfile : flist1) {
      List<OtObserveRecord> tlist = getOT1(tfile);
      if (tlist.size() > minOt1Num) {
        objs.add(tlist);
      }
    }
    return objs;
  }

  public List<String> getFileNameList(String path) {
    List<String> fpath = new ArrayList();
    File dir1 = new File(path);
    File[] flist1 = dir1.listFiles();
    for (File tfile : flist1) {
      List<OtObserveRecord> tlist = getOT1(tfile);
      if (tlist.size() > minOt1Num) {
        fpath.add(tfile.getName());
      }
    }
    return fpath;
  }

  public List<OtObserveRecord> getOT1(File file) {

    List<OtObserveRecord> ot1list = new ArrayList();

    BufferedReader reader = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;
      int tline = 0;
      int tline2 = 0;
      int tlen = "2016/12/28 15:54:17".length();
      //2318.9	381.724	170.284	4.01298	2015/12/18 18:25:28	10.7607	1777	34	1626.55	2273.18
      while ((tempString = reader.readLine()) != null) {
        if (tempString.isEmpty()) {
          continue;
        }
        String[] tstr = tempString.split("\t");
        float x = Float.parseFloat(tstr[0]);
        float y = Float.parseFloat(tstr[1]);  //1
        float ra = Float.parseFloat(tstr[2]); //2
        float dec = Float.parseFloat(tstr[3]);
        String dateStr = tstr[4];
        float mag = Float.parseFloat(tstr[5]);
        int number = Integer.parseInt(tstr[6]);
        long oorId = Long.parseLong(tstr[8]);
        Date tdate = null;
        if (tstr[4].trim().length() == tlen) {
          tdate = sdf.parse(tstr[4]);
        } else {
          tdate = sdf2.parse(tstr[4]);
        }

        OtObserveRecord ot1 = new OtObserveRecord();
        ot1.setX(x);
        ot1.setY(y);
        ot1.setRaD(ra);
        ot1.setDecD(dec);
        ot1.setMagAper(mag);
        ot1.setFfNumber(number);
        ot1.setXTemp(x);
        ot1.setYTemp(y);
        ot1.setOorId(oorId);
        ot1.setDateUt(tdate);
        ot1.setDateStr(dateStr);

        ot1list.add(ot1);
        tline++;
      }
      reader.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    return ot1list;
  }

}
