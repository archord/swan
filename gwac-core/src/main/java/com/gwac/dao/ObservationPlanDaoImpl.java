/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.dao;

import com.gwac.model.ObservationPlan;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository(value = "observationPlanDao")
public class ObservationPlanDaoImpl extends BaseHibernateDaoImpl<ObservationPlan> implements ObservationPlanDao {
    
}
