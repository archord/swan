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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xy
 */
public class Main {

  ArrayList<OtObserveRecord> ot1list = new ArrayList();

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
//    Main fmo = new Main();
//    fmo.findMovingObject();
    ResultCompare rc = new ResultCompare();
    rc.compare();
  }

  public void findMovingObject() {

    String[] dates = {"161228-4-32"}; //4-32-debug1  161228-4-32 161228-4-32-0-0-notInLine

    for (String tname : dates) {
      ot1list.clear();

      String ot1File = "E:\\work\\program\\java\\netbeans\\LineFinder\\resources\\161228\\" + tname + ".txt";
      String outImagePoint = "E:\\work\\program\\java\\netbeans\\LineFinder\\resources\\161228\\" + tname + "-point-all.png";

      getOT1(ot1File);

      processOneDay(ot1list, tname, 0, 0);
      
//      LineTest ltest = new LineTest();
//      ltest.fitTest11(ot1list);
    }
  }

  public void processOneDay(List<OtObserveRecord> oors, String dateStr, int dpmId, int skyId) {

    FindMoveObject fmo = new FindMoveObject();

    int lastFrameNumber = 0;
    List<OtObserveRecord> singleFrame = new ArrayList<>();
    for (OtObserveRecord oor : oors) {
      oor.setX(oor.getXTemp());
      oor.setY(oor.getYTemp());
      if (lastFrameNumber != oor.getFfNumber()) {
        lastFrameNumber = oor.getFfNumber();
        fmo.addFrame(singleFrame);
        singleFrame.clear();
      }
      singleFrame.add(oor);
    }
    fmo.addFrame(singleFrame);
    fmo.endAllFrame();
    System.out.println(dateStr + ", total record " + oors.size() + ", added record " + fmo.numOT1s
            + ", not in line " + fmo.notInLine.size() 
            + ", in line " + fmo.totalLinePointNumber);

    String ot2Path = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "\\";
    String imgPath1 = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "1.png";
    String imgPath2 = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "2.png";
    String imgPath3 = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "3.png";
    String imgPath4 = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "4.png";
    String imgPathall = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "-all.png";
    String imgPathOutLine = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "-outLine.png";
    String notInLine = "E:\\" + dateStr + "-" + dpmId + "-" + skyId + "-notInLine.txt";
//    DrawObject dObj = new DrawObject(fmo);
//    dObj.drawObjsAll(imgPath1, '1');
//    dObj.drawObjsAll(imgPath2, '2');
//    dObj.drawObjsAll(imgPath3, '3');
//    dObj.drawObjsAll(imgPath4, '4');
//    dObj.drawPoint(imgPathall);
//    dObj.drawPointNotInLine(imgPathOutLine);
//    fmo.saveNotInLine(notInLine);
    fmo.saveLine(ot2Path);
  }

  public void getOT1(String ot1file) {

    File file = new File(ot1file);
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
  }

}
