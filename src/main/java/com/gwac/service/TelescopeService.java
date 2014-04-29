/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.model.Telescope;
import java.util.List;

/**
 *
 * @author xy
 */
public interface TelescopeService {
  public Number count();
  public List<Telescope> findAll(int start, int resultSize);
  public List<Telescope> findAll();
}
