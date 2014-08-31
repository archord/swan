package com.gwac.model;
// Generated 2014-8-30 13:00:35 by Hibernate Tools 3.6.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * FitsFile generated by hbm2java
 */
@Entity
@Table(name = "fits_file", schema = "public"
)
public class FitsFile implements java.io.Serializable {

  private long ffId;
  private Short dpmId;
  private String storePath;
  private String fileName;
  private Short gmbId;
  private Float gmbRa;
  private Float gmbDec;
  private Short tspId;
  private Float tspRa;
  private Float tspDec;
  private Float fieldWidth;
  private Float fieldHeight;
  private Float pixelResolution;
  private Long templateFfId;
  private Long slfId;
  private Boolean isTemplate;

  public FitsFile() {
  }

  public FitsFile(long ffId) {
    this.ffId = ffId;
  }

  public FitsFile(long ffId, Short dpmId, String storePath, String fileName, Short gmbId, Float gmbRa, Float gmbDec, Short tspId, Float tspRa, Float tspDec, Float fieldWidth, Float fieldHeight, Float pixelResolution, Long templateFfId, Long slfId, Boolean isTemplate) {
    this.ffId = ffId;
    this.dpmId = dpmId;
    this.storePath = storePath;
    this.fileName = fileName;
    this.gmbId = gmbId;
    this.gmbRa = gmbRa;
    this.gmbDec = gmbDec;
    this.tspId = tspId;
    this.tspRa = tspRa;
    this.tspDec = tspDec;
    this.fieldWidth = fieldWidth;
    this.fieldHeight = fieldHeight;
    this.pixelResolution = pixelResolution;
    this.templateFfId = templateFfId;
    this.slfId = slfId;
    this.isTemplate = isTemplate;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ff_seq")
  @SequenceGenerator(name = "ff_seq", sequenceName = "ff_id_seq")
  @Column(name = "ff_id", unique = true, nullable = false)
  public long getFfId() {
    return this.ffId;
  }

  public void setFfId(long ffId) {
    this.ffId = ffId;
  }

  @Column(name = "dpm_id")
  public Short getDpmId() {
    return this.dpmId;
  }

  public void setDpmId(Short dpmId) {
    this.dpmId = dpmId;
  }

  @Column(name = "store_path")
  public String getStorePath() {
    return this.storePath;
  }

  public void setStorePath(String storePath) {
    this.storePath = storePath;
  }

  @Column(name = "file_name")
  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Column(name = "gmb_id")
  public Short getGmbId() {
    return this.gmbId;
  }

  public void setGmbId(Short gmbId) {
    this.gmbId = gmbId;
  }

  @Column(name = "gmb_ra", precision = 8, scale = 8)
  public Float getGmbRa() {
    return this.gmbRa;
  }

  public void setGmbRa(Float gmbRa) {
    this.gmbRa = gmbRa;
  }

  @Column(name = "gmb_dec", precision = 8, scale = 8)
  public Float getGmbDec() {
    return this.gmbDec;
  }

  public void setGmbDec(Float gmbDec) {
    this.gmbDec = gmbDec;
  }

  @Column(name = "tsp_id")
  public Short getTspId() {
    return this.tspId;
  }

  public void setTspId(Short tspId) {
    this.tspId = tspId;
  }

  @Column(name = "tsp_ra", precision = 8, scale = 8)
  public Float getTspRa() {
    return this.tspRa;
  }

  public void setTspRa(Float tspRa) {
    this.tspRa = tspRa;
  }

  @Column(name = "tsp_dec", precision = 8, scale = 8)
  public Float getTspDec() {
    return this.tspDec;
  }

  public void setTspDec(Float tspDec) {
    this.tspDec = tspDec;
  }

  @Column(name = "field_width", precision = 8, scale = 8)
  public Float getFieldWidth() {
    return this.fieldWidth;
  }

  public void setFieldWidth(Float fieldWidth) {
    this.fieldWidth = fieldWidth;
  }

  @Column(name = "field_height", precision = 8, scale = 8)
  public Float getFieldHeight() {
    return this.fieldHeight;
  }

  public void setFieldHeight(Float fieldHeight) {
    this.fieldHeight = fieldHeight;
  }

  @Column(name = "pixel_resolution", precision = 8, scale = 8)
  public Float getPixelResolution() {
    return this.pixelResolution;
  }

  public void setPixelResolution(Float pixelResolution) {
    this.pixelResolution = pixelResolution;
  }

  @Column(name = "template_ff_id")
  public Long getTemplateFfId() {
    return this.templateFfId;
  }

  public void setTemplateFfId(Long templateFfId) {
    this.templateFfId = templateFfId;
  }

  @Column(name = "slf_id")
  public Long getSlfId() {
    return this.slfId;
  }

  public void setSlfId(Long slfId) {
    this.slfId = slfId;
  }

  @Column(name = "is_template")
  public Boolean getIsTemplate() {
    return this.isTemplate;
  }

  public void setIsTemplate(Boolean isTemplate) {
    this.isTemplate = isTemplate;
  }

}
