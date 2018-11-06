package com.gwac.action;

import com.opensymphony.xwork2.ActionSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class MonitorImagePreview extends ActionSupport {

  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

  /**
   * @param ccdStr the ccdStr to set
   */
  public void setCcdStr(String ccdStr) {
    this.ccdStr = ccdStr;
  }

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(MonitorImagePreview.class);

  private String dataType;
  private String rstData;
  private Integer page;
  private String dateStr;
  private String ccdStr;
  private Integer totalImage = 0;

  @Actions({
    @Action(value = "/image-preview-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    String rootPath = getText("gwacDataRootDirectory");
    String thead = getText("gwacDataThumbnailDirectory");
    if (rootPath.charAt(rootPath.length() - 1) != '/') {
      rootPath += "/";
    }

    if (dataType == null || dataType.isEmpty()) {
      rstData = "dataType error";
    } else if (dataType.equals("dateList")) {
      String imgRootPath = rootPath + thead;
      log.warn(imgRootPath);
      rstData = getAllDirListStr(imgRootPath);
    } else if (dataType.equals("ccdList")) {
      if (dateStr != null && dateStr.length() > 0) {
        String imgRootPath = rootPath + thead + "/" + dateStr;
        log.debug(imgRootPath);
        rstData = getAllCCDListStr(imgRootPath);
      } else {
        rstData = "";
      }
    } else if (dataType.equals("imageList")) {
      if (dateStr != null && ccdStr != null && dateStr.length() > 0 && ccdStr.length() > 0) {
        String tImgPath = rootPath + thead + "/" + dateStr + "/" + ccdStr;
        String urlPath = "http://www.gwac.top/images/" + thead + "/" + dateStr + "/" + ccdStr + "/";
        String fitUrlPath = "http://www.gwac.top/images/gwac_orig_fits/" + dateStr 
                + "/" +ccdStr.charAt(0) + "0" + ccdStr.substring(1, 3) + "_" + ccdStr.substring(1) + "/";
        log.debug(tImgPath);
        log.debug(urlPath);
        rstData = getImageListStr(tImgPath, urlPath, fitUrlPath);
      } else {
        rstData = "";
      }
      if (rstData.isEmpty()) {
        rstData = "{\"result\": [{\"image\": \"\",\"width\": 400,\"height\": 400}],\"total\":0}";
      }
    }
    return "json";
  }

  public String getImageListStr(String path, String urlPath, String fitUrlPath) {

    int PAGE_NUM = 20;
    //G023_mon_objt_181025T12574229_min.jpg
    int imgNameLength = 37;
    String tstr = "";
    String imgList = path + ".txt";
    File tfile = new File(imgList);
    if (tfile.exists()) {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new FileReader(tfile));
        String tempString = null;
        StringBuilder sb1 = new StringBuilder();
        while ((tempString = reader.readLine()) != null) {
          sb1.append(tempString);
        }
        tstr = sb1.toString();
        reader.close();
      } catch (IOException e) {
        log.error("read file content error:" + imgList, e);
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e1) {
            log.error("close file error:" + imgList, e1);
          }
        }
      }
    } else {
      tstr = dirContent2Str(path, imgNameLength);
      try {
        FileWriter fileWritter = new FileWriter(tfile);
        fileWritter.write(tstr);
        fileWritter.close();
      } catch (IOException e) {
        log.error("read write file error:" + imgList, e);
      }
    }
    if (tstr.length() > 0) {
      StringBuilder sb2 = new StringBuilder("{'result': [");
      String[] timgs = tstr.split(",");
      totalImage = timgs.length;
      int startNum = (page - 1) * PAGE_NUM;
      int tnum = 0;
      for (int i = startNum; (i < startNum + PAGE_NUM) && (i < totalImage); i++) {
        //G021_mon_objt_181025T12574240_min.jpg
        String tname = timgs[i].substring(0, timgs[i].indexOf("_min"));
        sb2.append("{'preUrl': '");
        sb2.append(urlPath);
        sb2.append(timgs[i]);
        sb2.append("','fullUrl': '");
        sb2.append(fitUrlPath);
        sb2.append(tname);
        //sb2.append(".jpg','imgName': '");
        sb2.append(".fit.fz','imgName': '");
        sb2.append(tname);
        sb2.append("','width': 400,'height': 400");
        sb2.append("},");
        tnum++;
      }
      sb2.append("],'total':");
      sb2.append(tnum);
      sb2.append("}");
      tstr = sb2.toString();
    }
    return tstr;
  }

  public String getAllDirListStr(String path) {

    //181027
    int dirNameLength = 6;
    String tstr = "";
    String dateList = path + "/thumbnailDateList.txt";
    File tfile = new File(dateList);
    if (tfile.exists()) {
      BasicFileAttributes bAttributes = null;
      try {
        bAttributes = Files.readAttributes(tfile.toPath(), BasicFileAttributes.class);
        FileTime changeTime = bAttributes.lastModifiedTime();
        Date now = new Date();
        double seconds = (now.getTime() - changeTime.toMillis()) * 0.001;
        if (seconds > 600) {
          tstr = dirContent2Str2(path, dirNameLength);
          FileWriter fileWritter = new FileWriter(tfile);
          fileWritter.write(tstr);
          fileWritter.close();
        } else {
          BufferedReader reader = null;
          try {
            reader = new BufferedReader(new FileReader(tfile));
            String tempString = null;
            StringBuilder sb1 = new StringBuilder();
            while ((tempString = reader.readLine()) != null) {
              sb1.append(tempString);
            }
            tstr = sb1.toString();
            reader.close();
          } catch (IOException e) {
            log.error("read file content error:" + dateList, e);
          } finally {
            if (reader != null) {
              try {
                reader.close();
              } catch (IOException e1) {
                log.error("close file error:" + dateList, e1);
              }
            }
          }
        }
      } catch (IOException e) {
        log.error("read file date or write file error:" + dateList, e);
      }
    } else {
      tstr = dirContent2Str(path, dirNameLength);
      try {
//        tfile.createNewFile();
        FileWriter fileWritter = new FileWriter(tfile);
        fileWritter.write(tstr);
        fileWritter.close();
      } catch (IOException e) {
        log.error("read write file error:" + dateList, e);
      }
    }
    return tstr;
  }

  public String getAllCCDListStr(String path) {

    int dirNameLength = 4;
    String tstr = dirContent2Str(path, dirNameLength);
    return tstr;
  }

  public String dirContent2Str2(String path, int nameLength) {
    StringBuilder sb = new StringBuilder("");
    File file = new File(path);
    if (file.exists()) {
      File[] files = file.listFiles();//dateStrs
      for (File file2 : files) {
        if (file2.getName().length() == nameLength) {
          if (file2.isDirectory()) {
            File[] file21 = file2.listFiles(); //ccdNames
            int tnum = 0; //fitsImg Number
            int dirNum = 0; //ccdName Number
            for (File file3 : file21) {
              if (file3.isDirectory()) {
                File[] file31 = file3.listFiles(); //fitsImgs
                tnum = tnum + file31.length;
                dirNum++;
              }
            }
            if (dirNum > 0 && tnum > 0) {
              sb.append(file2.getName());
              sb.append(",");
            }
          }
        }
      }
    }
    String tstr = sb.toString();
    if (tstr.length() > 0) {
      tstr = tstr.substring(0, tstr.length() - 1);
    }
    return tstr;
  }

  public String dirContent2Str(String path, int nameLength) {
    StringBuilder sb = new StringBuilder("");
    File file = new File(path);
    if (file.exists()) {
      File[] files = file.listFiles();
      for (File file2 : files) {
        if (file2.getName().length() == nameLength) {
          if (file2.isDirectory()) {
            File[] tfiles21 = file2.listFiles();
            if (tfiles21.length > 0) {
              sb.append(file2.getName());
              sb.append(",");
            }
          } else {
            sb.append(file2.getName());
            sb.append(",");
          }
        }
      }
    }
    String tstr = sb.toString();
    if (tstr.length() > 0) {
      tstr = tstr.substring(0, tstr.length() - 1);
    }
    return tstr;
  }

  /**
   * @param page the page to set
   */
  public void setPage(Integer page) {
    this.page = page;
  }

  /**
   * @return the totalImage
   */
  public Integer getTotalImage() {
    return totalImage;
  }

  /**
   * @param dataType the dataType to set
   */
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  /**
   * @return the rstData
   */
  public String getRstData() {
    return rstData;
  }

}
