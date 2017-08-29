/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * /downloadot2
 *
 * @author xy
 */
public class OT2InfoDownlaod extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String otName = request.getParameter("otName");
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out1 = response.getWriter()) {
      if (otName != null && !otName.isEmpty() && otName.length() == 14) {
        response.setContentType("Content-type: text/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=mytest.zip");

        // List of files to be downloaded
        List<File> files = new ArrayList();
        files.add(new File("C:/first.txt"));
        files.add(new File("C:/second.txt"));
        files.add(new File("C:/third.txt"));

        ServletOutputStream out = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));

        for (File file : files) {
          System.out.println("Adding " + file.getName());
          zos.putNextEntry(new ZipEntry(file.getName()));

          // Get the file
          FileInputStream fis = null;
          try {
            fis = new FileInputStream(file);

          } catch (FileNotFoundException fnfe) {
				// If the file does not exists, write an error entry instead of
            // file
            // contents
            zos.write(("ERRORld not find file " + file.getName())
                    .getBytes());
            zos.closeEntry();
            System.out.println("Couldfind file "
                    + file.getAbsolutePath());
            continue;
          }

          BufferedInputStream fif = new BufferedInputStream(fis);

          // Write the contents of the file
          int data = 0;
          while ((data = fif.read()) != -1) {
            zos.write(data);
          }
          fif.close();

          zos.closeEntry();
          System.out.println("Finishedng file " + file.getName());
        }

        zos.close();

      } else {
        out1.println("otName error");
      }
    }
  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>

}
