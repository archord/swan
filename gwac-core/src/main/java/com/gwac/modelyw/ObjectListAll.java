/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.modelyw;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *
 * @author xy
 */
@Entity
@Table(name = "object_list_all", schema = "public"
)
public class ObjectListAll implements Serializable {

  private int id;
  private String obj_id;
  private String object_list = "object_list_all";
  private String objsour = "GWAC_followup";
  private String observer = "GWAC";
  private String objepoch = "2000";
  private String objerror = "0.0|0.0";
  private String objrank = "0.0";
  private String group_id = "XL002";
  private String unit_id = "001|002";
  private String obs_type = "goa";
  private String obs_stra = "pointing";
  private String day_int = "0";
  private String imgtype = "object";
  private String run_name = "1";
  private String mode = "observation";

  private String followname= "G190830_C0001_001";
  private String obj_name = "G190830_C0001";
  private float objra = (float) 100.0;
  private float objdec = (float) -60.0;
  private String date_beg = "2019/09/09";
  private String date_end = "2019/09/09";
  private String filter_band = "R";
  private int expdur = 180;
  private int delay = 0;
  private int frmcnt = 10;
  private int priority = 40;
  
  
  @Id
  @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
    @Parameter(name = "max_lo", value = "49"),
    @Parameter(name = "sequence", value = "object_list_all_id_seq")})
  @GeneratedValue(generator = "generator")
  @Column(name = "id", unique = true, nullable = false)
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the obj_id
   */
  @Column(name = "obj_id")
  public String getObj_id() {
    return obj_id;
  }

  /**
   * @param obj_id the obj_id to set
   */
  public void setObj_id(String obj_id) {
    this.obj_id = obj_id;
  }

  /**
   * @return the object_list
   */
  @Column(name = "object_list")
  public String getObject_list() {
    return object_list;
  }

  /**
   * @param object_list the object_list to set
   */
  public void setObject_list(String object_list) {
    this.object_list = object_list;
  }

  /**
   * @return the objsour
   */
  @Column(name = "objsour")
  public String getObjsour() {
    return objsour;
  }

  /**
   * @param objsour the objsour to set
   */
  public void setObjsour(String objsour) {
    this.objsour = objsour;
  }

  /**
   * @return the observer
   */
  @Column(name = "observer")
  public String getObserver() {
    return observer;
  }

  /**
   * @param observer the observer to set
   */
  public void setObserver(String observer) {
    this.observer = observer;
  }

  /**
   * @return the objepoch
   */
  @Column(name = "objepoch")
  public String getObjepoch() {
    return objepoch;
  }

  /**
   * @param objepoch the objepoch to set
   */
  public void setObjepoch(String objepoch) {
    this.objepoch = objepoch;
  }

  /**
   * @return the objerror
   */
  @Column(name = "objerror")
  public String getObjerror() {
    return objerror;
  }

  /**
   * @param objerror the objerror to set
   */
  public void setObjerror(String objerror) {
    this.objerror = objerror;
  }

  /**
   * @return the objrank
   */
  @Column(name = "objrank")
  public String getObjrank() {
    return objrank;
  }

  /**
   * @param objrank the objrank to set
   */
  public void setObjrank(String objrank) {
    this.objrank = objrank;
  }

  /**
   * @return the group_id
   */
  @Column(name = "group_id")
  public String getGroup_id() {
    return group_id;
  }

  /**
   * @param group_id the group_id to set
   */
  public void setGroup_id(String group_id) {
    this.group_id = group_id;
  }

  /**
   * @return the unit_id
   */
  @Column(name = "unit_id")
  public String getUnit_id() {
    return unit_id;
  }

  /**
   * @param unit_id the unit_id to set
   */
  public void setUnit_id(String unit_id) {
    this.unit_id = unit_id;
  }

  /**
   * @return the obs_type
   */
  @Column(name = "obs_type")
  public String getObs_type() {
    return obs_type;
  }

  /**
   * @param obs_type the obs_type to set
   */
  public void setObs_type(String obs_type) {
    this.obs_type = obs_type;
  }

  /**
   * @return the obs_stra
   */
  @Column(name = "obs_stra")
  public String getObs_stra() {
    return obs_stra;
  }

  /**
   * @param obs_stra the obs_stra to set
   */
  public void setObs_stra(String obs_stra) {
    this.obs_stra = obs_stra;
  }

  /**
   * @return the day_int
   */
  @Column(name = "day_int")
  public String getDay_int() {
    return day_int;
  }

  /**
   * @param day_int the day_int to set
   */
  public void setDay_int(String day_int) {
    this.day_int = day_int;
  }

  /**
   * @return the imgtype
   */
  @Column(name = "imgtype")
  public String getImgtype() {
    return imgtype;
  }

  /**
   * @param imgtype the imgtype to set
   */
  public void setImgtype(String imgtype) {
    this.imgtype = imgtype;
  }

  /**
   * @return the run_name
   */
  @Column(name = "run_name")
  public String getRun_name() {
    return run_name;
  }

  /**
   * @param run_name the run_name to set
   */
  public void setRun_name(String run_name) {
    this.run_name = run_name;
  }

  /**
   * @return the mode
   */
  @Column(name = "mode")
  public String getMode() {
    return mode;
  }

  /**
   * @param mode the mode to set
   */
  public void setMode(String mode) {
    this.mode = mode;
  }

  /**
   * @return the obj_name
   */
  @Column(name = "obj_name")
  public String getObj_name() {
    return obj_name;
  }

  /**
   * @param obj_name the obj_name to set
   */
  public void setObj_name(String obj_name) {
    this.obj_name = obj_name;
  }

  /**
   * @return the objra
   */
  @Column(name = "objra")
  public float getObjra() {
    return objra;
  }

  /**
   * @param objra the objra to set
   */
  public void setObjra(float objra) {
    this.objra = objra;
  }

  /**
   * @return the objdec
   */
  @Column(name = "objdec")
  public float getObjdec() {
    return objdec;
  }

  /**
   * @param objdec the objdec to set
   */
  public void setObjdec(float objdec) {
    this.objdec = objdec;
  }

  /**
   * @return the date_beg
   */
  @Column(name = "date_beg")
  public String getDate_beg() {
    return date_beg;
  }

  /**
   * @param date_beg the date_beg to set
   */
  public void setDate_beg(String date_beg) {
    this.date_beg = date_beg;
  }

  /**
   * @return the date_end
   */
  @Column(name = "date_end")
  public String getDate_end() {
    return date_end;
  }

  /**
   * @param date_end the date_end to set
   */
  public void setDate_end(String date_end) {
    this.date_end = date_end;
  }

  /**
   * @return the filter_band
   */
  @Column(name = "filter_band")
  public String getFilter_band() {
    return filter_band;
  }

  /**
   * @param filter_band the filter_band to set
   */
  public void setFilter_band(String filter_band) {
    this.filter_band = filter_band;
  }

  /**
   * @return the expdur
   */
  @Column(name = "expdur")
  public int getExpdur() {
    return expdur;
  }

  /**
   * @param expdur the expdur to set
   */
  public void setExpdur(int expdur) {
    this.expdur = expdur;
  }

  /**
   * @return the delay
   */
  @Column(name = "delay")
  public int getDelay() {
    return delay;
  }

  /**
   * @param delay the delay to set
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }

  /**
   * @return the frmcnt
   */
  @Column(name = "frmcnt")
  public int getFrmcnt() {
    return frmcnt;
  }

  /**
   * @param frmcnt the frmcnt to set
   */
  public void setFrmcnt(int frmcnt) {
    this.frmcnt = frmcnt;
  }

  /**
   * @return the priority
   */
  @Column(name = "priority")
  public int getPriority() {
    return priority;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }

  /**
   * @return the followup_name
   */
  @Column(name = "followname")
  public String getFollowname() {
    return this.followname;
  }

  /**
   * @param followup_name the followup_name to set
   */
  public void setFollowname(String followname) {
    this.followname = followname;
  }

}
