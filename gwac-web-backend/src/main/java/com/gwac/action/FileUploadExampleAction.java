/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import java.io.File;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUploadExampleAction extends ActionSupport {

  private static final Log log = LogFactory.getLog(FileUploadExampleAction.class);
  private File[] uploads;
  private String[] fileUploadContentType;
  private String[] fileUploadFileName;

  public String execute() throws Exception {
    log.debug("\n\n upload2");
    log.debug("files:");
    for (File u : getUploads()) {
      log.debug("*** " + u + "\t" + u.length());
    }
    log.debug("filenames:");
    for (String n : fileUploadFileName) {
      log.debug("*** " + n);
    }
    log.debug("content types:");
    for (String c : fileUploadContentType) {
      log.debug("*** " + c);
    }
    log.debug("\n\n");
    return SUCCESS;

  }

  public String display() {
    return NONE;
  }

  /**
   * @return the fileUploadContentType
   */
  public String[] getFileUploadContentType() {
    return fileUploadContentType;
  }

  /**
   * @param fileUploadContentType the fileUploadContentType to set
   */
  public void setFileUploadContentType(String[] fileUploadContentType) {
    this.fileUploadContentType = fileUploadContentType;
  }

  /**
   * @return the fileUploadFileName
   */
  public String[] getFileUploadFileName() {
    return fileUploadFileName;
  }

  /**
   * @param fileUploadFileName the fileUploadFileName to set
   */
  public void setFileUploadFileName(String[] fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }

  /**
   * @return the uploads
   */
  public File[] getUploads() {
    return uploads;
  }

  /**
   * @param uploads the uploads to set
   */
  public void setUploads(File[] uploads) {
    this.uploads = uploads;
  }
}