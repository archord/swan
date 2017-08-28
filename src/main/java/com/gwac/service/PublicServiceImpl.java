/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.service;

import com.gwac.dao.MultimediaResourceDao;
import com.gwac.model.MultimediaResource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service
public class PublicServiceImpl implements PublicService {

  @Resource
  private MultimediaResourceDao mrDao;

  /**
   *
   * @param path
   * "E:\\work\\program\\java\\netbeans\\GWAC\\src\\main\\webapp\\resource\\audio"
   * @return
   */
  List<MultimediaResource> foundAllFiles(String path) {

    String webRoot = "webapp";
    List<MultimediaResource> mrs = new ArrayList<>();
    File folder = new File(path);
    for (File fileEntry : folder.listFiles()) {
      if (!fileEntry.isDirectory()) {
        MultimediaResource mr = new MultimediaResource();
        String fullName = fileEntry.getName();
        String fullPath = fileEntry.getPath().replace("\\", "/");
        String name = fullName.substring(0, fullName.indexOf('.'));
        String relativePath = fullPath.substring(fullPath.indexOf(webRoot) + webRoot.length() + 1);
        mr.setEnName(name);
        mr.setPath(relativePath);
        mr.setType('1');
        mrs.add(mr);
      }
    }
    return mrs;
  }

  @Override
  public void executeService() {
    List<MultimediaResource> mrs = foundAllFiles("E:\\work\\program\\java\\netbeans\\GWAC\\src\\main\\webapp\\resource\\audio");
    for (MultimediaResource tmr : mrs) {
      mrDao.save(tmr);
    }
  }

  /**
   * @param mrDao the mrDao to set
   */
  public void setMrDao(MultimediaResourceDao mrDao) {
    this.mrDao = mrDao;
  }
}
