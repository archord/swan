/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OTCatalog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class OTCatalogDaoImpl implements OTCatalogDao {

  private static final Log log = LogFactory.getLog(OTCatalogDaoImpl.class);
  
  @Override
  public List<OTCatalog> getOT1Catalog(String path) {
    BufferedReader br = null;
    String line = "";
    String splitBy = " +";
    List<OTCatalog> otList = new ArrayList<OTCatalog>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    try {
      File tfile = new File(path);
      if(!tfile.exists()){
        log.error("file not exist " + tfile);
        return otList;
      }
      br = new BufferedReader(new FileReader(tfile));
      while ((line = br.readLine()) != null) {
        if(line.charAt(0)=='#'){
          continue;
        }
        // split on comma(',')  
        String[] strs = line.split(splitBy);
        // create car object to store values  
        OTCatalog ot = new OTCatalog();

        // add values from csv to car object  
        ot.setRaD(Float.parseFloat(strs[0]));
        ot.setDecD(Float.parseFloat(strs[1]));
        ot.setX(Float.parseFloat(strs[2]));
        ot.setY(Float.parseFloat(strs[3]));
        ot.setXTemp(Float.parseFloat(strs[4]));
        ot.setYTemp(Float.parseFloat(strs[5]));
        ot.setDateUt(df.parse(strs[6].replace('T', ' ')));
        ot.setImageName(strs[7]);
        ot.setFlux(Float.parseFloat(strs[8]));
        ot.setFlag(Boolean.parseBoolean(strs[9]));
//        ot.setFlagChb(Float.parseFloat(strs[10]));
        ot.setBackground(Float.parseFloat(strs[10]));
        ot.setThreshold(Float.parseFloat(strs[11]));
        ot.setMagAper(Float.parseFloat(strs[12]));
        ot.setMagerrAper(Float.parseFloat(strs[13]));
        ot.setEllipticity(Float.parseFloat(strs[14]));
        ot.setClassStar(Float.parseFloat(strs[15]));
//        ot.setOtFlag(Boolean.parseBoolean(strs[17]));

        otList.add(ot);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return otList;
  }
  
  @Override
  public List<OTCatalog> getOT1VarCatalog(String path) {
    BufferedReader br = null;
    String line = "";
    String splitBy = " +";
    List<OTCatalog> otList = new ArrayList<OTCatalog>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    try {
      File tfile = new File(path);
      if(!tfile.exists()){
        log.error("file not exist " + tfile);
        return otList;
      }
      br = new BufferedReader(new FileReader(tfile));
      while ((line = br.readLine()) != null) {
        if(line.charAt(0)=='#'){
          continue;
        }
        // split on comma(',')  
        String[] strs = line.split(splitBy);
        // create car object to store values  
        OTCatalog ot = new OTCatalog();

        // add values from csv to car object  
        ot.setRaD(Float.parseFloat(strs[0]));
        ot.setDecD(Float.parseFloat(strs[1]));
        ot.setX(Float.parseFloat(strs[2]));
        ot.setY(Float.parseFloat(strs[3]));
        ot.setXTemp(Float.parseFloat(strs[4]));
        ot.setYTemp(Float.parseFloat(strs[5]));
        ot.setDateUt(df.parse(strs[6].replace('T', ' ')));
        ot.setImageName(strs[7]);
        ot.setFlux(Float.parseFloat(strs[8]));
        ot.setFlag(Boolean.parseBoolean(strs[9]));
        ot.setBackground(Float.parseFloat(strs[10]));
        ot.setThreshold(Float.parseFloat(strs[11]));
        ot.setMagAper(Float.parseFloat(strs[12]));
        ot.setMagerrAper(Float.parseFloat(strs[13]));
        ot.setEllipticity(Float.parseFloat(strs[14]));
        ot.setClassStar(Float.parseFloat(strs[15]));
        ot.setDistance(Float.parseFloat(strs[16]));
        ot.setDeltamag(Float.parseFloat(strs[17]));

        otList.add(ot);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return otList;
  }
  
  public List<OTCatalog> getOT2Catalog(String path) {

    BufferedReader br = null;
    String line = "";
    String splitBy = " ";
    List<OTCatalog> otList = new ArrayList<OTCatalog>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    try {
      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        if(line.charAt(0)=='#'){
          continue;
        }
        // split on comma(',')  
        String[] strs = line.split(splitBy);
        // create car object to store values  
        OTCatalog ot = new OTCatalog();

        // add values from csv to car object  
        ot.setRaD(Float.parseFloat(strs[0]));
        ot.setDecD(Float.parseFloat(strs[1]));
        ot.setX(Float.parseFloat(strs[2]));
        ot.setY(Float.parseFloat(strs[3]));
        ot.setXTemp(Float.parseFloat(strs[4]));
        ot.setYTemp(Float.parseFloat(strs[5]));
        ot.setDateUt(df.parse(strs[6].replace('T', ' ')));
        ot.setImageName(strs[7]);
        ot.setFlux(Float.parseFloat(strs[8]));
        ot.setFlag(Boolean.parseBoolean(strs[9]));
        ot.setFlagChb(Float.parseFloat(strs[10]));
        ot.setBackground(Float.parseFloat(strs[11]));
        ot.setThreshold(Float.parseFloat(strs[12]));
        ot.setMagAper(Float.parseFloat(strs[13]));
        ot.setMagerrAper(Float.parseFloat(strs[14]));
        ot.setEllipticity(Float.parseFloat(strs[15]));
        ot.setClassStar(Float.parseFloat(strs[16]));
        ot.setOtFlag(Boolean.parseBoolean(strs[17]));

        otList.add(ot);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return otList;
  }
}
