/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.OtBaseDao;
import com.gwac.dao.OtNumberDao;
import com.gwac.model.OtBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xy
 */
public class OtNameRequestServiceImpl implements OtNameRequestService {

  private OtBaseDao obDao;
  private OtNumberDao otnDao;

  public List<OtBase> parseParaFile(File paraFile) {

    BufferedReader br = null;
    String line = "";
    String splitBy = " ";
    List<OtBase> objs = new ArrayList<OtBase>();

    try {

      br = new BufferedReader(new FileReader(paraFile));
      while ((line = br.readLine()) != null) {
        if (line.charAt(0) == '#') {
          continue;
        }
        // split on comma(',')  
        String[] strs = line.split(splitBy);
        // create car object to store values  
        OtBase obj = new OtBase();

        // add values from csv to car object  
        obj.setXtemp(Float.parseFloat(strs[0]));
        obj.setYtemp(Float.parseFloat(strs[1]));
        obj.setIdentify(strs[2]);

        objs.add(obj);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
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
    return objs;
  }

  public List<OtBase> getOtNames(File paraFile) {

    List<OtBase> obs = parseParaFile(paraFile);
    for (OtBase ob : obs) {
      //向数据库添加OT名字，需要在此时添加OT的所有相关属性信息，即相当于要先把OT观测列表先传输一遍
      //另一方面，GWAC为实时数据处理，如果处理流程依赖服务器，在服务器出问题时，GWAC流程就中断了。。
      if (!obDao.exist(ob)) {
        String fileDate = ob.getIdentify().substring(6, 12);
        int otNumber = otnDao.getNumberByDate(fileDate);
        String otName = String.format("%s_%05d", fileDate, otNumber);
        ob.setName(otName);
      }
    }
    return obs;
  }

  /**
   * @return the obDao
   */
  public OtBaseDao getObDao() {
    return obDao;
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtBaseDao obDao) {
    this.obDao = obDao;
  }
}
