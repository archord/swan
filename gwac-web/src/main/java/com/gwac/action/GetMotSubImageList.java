/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.MoveObjectDao;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* wget command example: */
/* wget http://localhost:8080/gwac/downloadot2.action?otName=M151207_C00163*/
/**
 * @author xy
 */
@Actions({
  @Action(value = "/getMotSubImageList", results = {
    @Result(name = "download", type = "stream",
            params = {"contentType", "application/octet-stream",
              "inputName", "inputStream",
              "contentDisposition", "attachment;filename=\"${fileName}\"",
              "bufferSize", "1024"})})
})
public class GetMotSubImageList extends ActionSupport {

  private static final long serialVersionUID = 5078264279068327193L;
  private static final Log log = LogFactory.getLog(GetMotSubImageList.class);

  @Resource
  MoveObjectDao motDao;

  private InputStream inputStream;
  private String fileName;
  private String contentType;

  private int motId;
  private int cropW;
  private int cropH;
  private int cmodel; //0 background move; 1 mot move;
  private int labelW; //是否在移动目标的位置画个正方形：=0不画；大于0画一个边长为labelW的正方形。

  @Override
  public String execute() {

    contentType = "application/octet-stream";
    fileName = "empty.zip";

    log.debug(motId);
    log.debug(cropW);
    log.debug(cropH);
    log.debug(cmodel);
    log.debug(labelW);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //必须OT名称
    if (motId > 0) {
      if (cropW < 100) {
        cropW = 100;
      }
      if (cropH < 100) {
        cropH = 100;
      }
      if (cmodel < 0) {
        setCmodel(0);
      }
      if (labelW < 0) {
        labelW = 0;
      }
      String dataRootDir = getText("gwacDataRootDirectory");
      String thead = getText("gwacDataThumbnailDirectory");

      try {
        Map<String, Float[]> files = motDao.getMotFitsList(motId);
        if (files.size() > 0) {
          fileName = motId + ".zip";
          String fullPath = "";
          try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            int fIdx = 1;
            for (Map.Entry<String, Float[]> entry : files.entrySet()) {
              String imgName = entry.getKey();
              Float[] coor = entry.getValue();
              if (fullPath.isEmpty()) {
                fullPath = dataRootDir + "/" + thead + "/20" + imgName.substring(14, 20) + "/" + imgName.substring(0, 4) + "/";
              }
              String tImgPath = fullPath + imgName.substring(0, 29) + ".jpg";
//              log.debug(tImgPath);
              File fullImg = new File(tImgPath);
              if (fullImg.exists()) {
                BufferedImage im = ImageIO.read(fullImg);
                BufferedImage timg = CommonFunction.getSubImage(im, coor[0], coor[1], cropW, cropH, labelW);
//                String imgNameInZip = String.format("%05d_%s.jpg", fIdx++, imgName.substring(0, 29));
                String imgNameInZip = String.format("%s.jpg", imgName.substring(0, 29));
                zos.putNextEntry(new ZipEntry(imgNameInZip));
                ImageIO.write(timg, "jpg", zos);
                zos.closeEntry();
              }
            }
            zos.flush();
            zos.close();
          }
        }
      } catch (IOException e) {
        log.error("get motId=" + motId + " sub image error.", e);
      }
    }
    inputStream = new ByteArrayInputStream(baos.toByteArray());

    return "download";
  }

  public BufferedImage getSubImage(BufferedImage src, double cx, double cy, int w, int h, int labelW) {
    int imw = src.getWidth();
    int imh = src.getHeight();
    int cxi = (int) Math.round(cx);
    int cyi = (int) Math.round(cy);
    int tx = cxi - w / 2;
    int ty = cyi - h / 2;
    BufferedImage cropImg = src.getSubimage(tx, ty, w, h);
    BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
    Graphics tg = result.getGraphics();
    tg.drawImage(cropImg, 0, 0, null);
    if (labelW > 0) {
      tg.setColor(new Color(0, 255, 0));
      tg.drawRect(w / 2 - labelW / 2, h / 2 - labelW / 2, labelW, labelW);
    }
    return result;
  }

  /**
   * @return the inputStream
   */
  public InputStream getInputStream() throws Exception {
    return inputStream;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param cropW the cropW to set
   */
  public void setCropW(int cropW) {
    this.cropW = cropW;
  }

  /**
   * @param cropH the cropH to set
   */
  public void setCropH(int cropH) {
    this.cropH = cropH;
  }

  /**
   * @param labelW the labelW to set
   */
  public void setLabelW(int labelW) {
    this.labelW = labelW;
  }

  /**
   * @param motId the motId to set
   */
  public void setMotId(int motId) {
    this.motId = motId;
  }

  /**
   * @param cmodel the cmodel to set
   */
  public void setCmodel(int cmodel) {
    this.cmodel = cmodel;
  }

}
