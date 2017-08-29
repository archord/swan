/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.SkyRegionTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="skyRegionTemplateDao")
public class SkyRegionTemplateDaoImpl  extends BaseHibernateDaoImpl<SkyRegionTemplate> implements SkyRegionTemplateDao{
  
}
