/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.linefind;

/**
 *
 * @author xy
 */
public class LineFinderConfig {
  
    int thetaSize = 180;
    int rhoSize = 100;
    int thetaRange = 36;
    int rhoRange = 10;
    int maxHoughFrameNunmber = 10;
    int minValidPoint = 3;
    float maxDistance = 100;
    float rhoErrorTimes = (float) 1;
    int validLineMinPoint = 3;
    
    public LineFinderConfig(){
      
    }
}
