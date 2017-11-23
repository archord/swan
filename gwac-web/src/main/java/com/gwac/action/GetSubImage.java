/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import static com.opensymphony.xwork2.Action.NONE;
import com.opensymphony.xwork2.ActionSupport;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
  @Action(value = "/getSubImage", results = {
    @Result(name = "download", type = "stream",
            params = {"contentType", "image/jpeg",
              "inputName", "inputStream",
              "contentDisposition", "attachment;filename=\"${fileName}\"",
              "bufferSize", "1024"})})
})
public class GetSubImage extends ActionSupport {

  private static final long serialVersionUID = 5078264279068327193L;
  private static final Log log = LogFactory.getLog(GetSubImage.class);

  private InputStream inputStream;
  private String fileName;
  private String contentType;

  private String imgPath;
  private float centerX;
  private float centerY;
  private int cropW;
  private int cropH;
  private int labelW;

  @Override
  public String execute() {

    contentType = "image/jpeg";
    fileName = "empty.jpg";

    log.debug(imgPath);
    log.debug(centerX);
    log.debug(centerY);
    log.debug(cropW);
    log.debug(cropH);
    log.debug(labelW);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //必须OT名称
    if (null != imgPath && !imgPath.isEmpty() && centerX > 0 && centerY > 0 && cropW > 0 && cropH > 0) {
      String dataRootDir = getText("gwacDataRootDirectory");
      String tImgPath = imgPath.trim();
      //images/thumbnail/20171122/G044/G044_mon_objt_171122T14122837.jpg
      if (tImgPath.charAt(0) == '/') {
        //remove "/images"
        tImgPath = tImgPath.substring(7);
      } else {
        //remove "images"
        tImgPath = tImgPath.substring(6);
      }
      tImgPath = dataRootDir + tImgPath;
      fileName = tImgPath.substring(tImgPath.lastIndexOf('/') + 1);

      try {
        log.debug(tImgPath);
        log.debug(fileName);
        File fullImg = new File(tImgPath);
        if (fullImg.exists()) {
          BufferedImage im = ImageIO.read(fullImg);
          BufferedImage timg = getSubImage(im, centerX, centerY, cropW, cropH, labelW);
          ImageIO.write(timg, "jpg", baos);
        } else {
          fileName = "GWAC_ccdimg_sub.jpg";
          BufferedImage im = ImageIO.read(new File("realTimeOtDistribution/GWAC_ccdimg_sub.jpg"));
          ImageIO.write(im, "jpg", baos);
        }
      } catch (IOException e) {
        log.error("read image " + imgPath + " info error.", e);
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
   * @param imgPath the imgPath to set
   */
  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  /**
   * @param centerX the centerX to set
   */
  public void setCenterX(float centerX) {
    this.centerX = centerX;
  }

  /**
   * @param centerY the centerY to set
   */
  public void setCenterY(float centerY) {
    this.centerY = centerY;
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

}
