/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.model4;

/**
 *
 * @author xy
 */
public class DataTableColumn {
  
  private int data;
  private String name;
  private Boolean searchable;
  private Boolean orderable;
  private DataTableSearch search;

  /**
   * @return the data
   */
  public int getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public void setData(int data) {
    this.data = data;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the searchable
   */
  public Boolean getSearchable() {
    return searchable;
  }

  /**
   * @param searchable the searchable to set
   */
  public void setSearchable(Boolean searchable) {
    this.searchable = searchable;
  }

  /**
   * @return the orderable
   */
  public Boolean getOrderable() {
    return orderable;
  }

  /**
   * @param orderable the orderable to set
   */
  public void setOrderable(Boolean orderable) {
    this.orderable = orderable;
  }

  /**
   * @return the search
   */
  public DataTableSearch getSearch() {
    return search;
  }

  /**
   * @param search the search to set
   */
  public void setSearch(DataTableSearch search) {
    this.search = search;
  }
}
