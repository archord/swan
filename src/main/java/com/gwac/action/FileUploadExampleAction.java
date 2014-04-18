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

public class FileUploadExampleAction extends ActionSupport {

  private File[] uploads;
  private String[] fileUploadContentType;
  private String[] fileUploadFileName;

  public String execute() throws Exception {
    System.out.println("\n\n upload2");
    System.out.println("files:");
    for (File u : getUploads()) {
      System.out.println("*** " + u + "\t" + u.length());
    }
    System.out.println("filenames:");
    for (String n : fileUploadFileName) {
      System.out.println("*** " + n);
    }
    System.out.println("content types:");
    for (String c : fileUploadContentType) {
      System.out.println("*** " + c);
    }
    System.out.println("\n\n");
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