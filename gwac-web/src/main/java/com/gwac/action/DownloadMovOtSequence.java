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
import static com.opensymphony.xwork2.Action.NONE;
import com.opensymphony.xwork2.ActionSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
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
  @Action(value = "/downloadmot", results = {
    @Result(name = "download", type = "stream",
            params = {"contentType", "application/octet-stream",
              "inputName", "inputStream",
              "contentDisposition", "attachment;filename=\"${fileName}\"",
              "bufferSize", "1024"})})
})
public class DownloadMovOtSequence extends ActionSupport {

  private static final long serialVersionUID = 5078264279068327193L;
  private static final Log log = LogFactory.getLog(DownloadMovOtSequence.class);

  private InputStream inputStream;
//  private long contentLength; //该变量要么设置一个对的值，要不不设置
  private String fileName;
  private String contentType;

  private String dateStr;
  private final char moveType = '1';
  private final int minFrameNumber = 20;
  @Resource
  private MoveObjectDao movObjDao = null;

  @Override
  public String execute() {

    contentType = "application/octet-stream";
    fileName = "empty.zip";

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //必须OT名称
    if (null != dateStr && !dateStr.isEmpty()) {
      dateStr = dateStr.trim();
      String dataRootDir = getText("gwacDataRootDirectory");
      fileName = dateStr + ".zip";

      Map<Long, String> movObjs = movObjDao.getMoveObjsInfoByDate(dateStr, moveType, minFrameNumber);

      try {
        if (!movObjs.isEmpty()) {
          fileName = dateStr + "_" + movObjs.size() + ".zip";
          try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Map.Entry<Long, String> entry : movObjs.entrySet()) {
              String fname = String.format("%05d.txt", entry.getKey());
              String records = entry.getValue();
              records = records.substring(3, records.length() - 3).replace(")\",\"(", "\n");
              records = records.replace("\\\"", "");
              zos.putNextEntry(new ZipEntry(fname));
              zos.write(records.getBytes());
              zos.closeEntry();
            }
            zos.flush();
            zos.close();
          }
        }
      } catch (IOException e) {
        log.error("ZIP date=" + dateStr + " info error.", e);
      }

    }
    inputStream = new ByteArrayInputStream(baos.toByteArray());

    return "download";
  }

  public String display() {
    return NONE;
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
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

}
