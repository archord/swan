<%-- 
    Document   : sidereal-time
    Created on : 2017-9-28, 16:53:23
    Author     : msw
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

    <title>Local Sidereal Time Clock</title>
    <meta name="Author" content="Juergen Giesen">
    <meta name="publisher" content="Juergen Giesen">
    <meta name="copyright" content="(c)2001-2015 Juergen Giesen">
    <meta name="Keywords" content="Sternzeit, Astronomie, Sternzeituhr,
          astronomy, JavaScript, Java Script, Sonne, Sun, local sidereal
          time, sidereal clock, declination, Deklination, Zeitgleichung,
          Equation of Time">
    <meta name="Subject" content="Sternzeit, Astronomie, Sternzeituhr,
          astronomy, JavaScript, Java Script, Sonne, Sun, local sidereal
          time, sidereal clock, declination, Deklination, Zeitgleichung,
          Equation of Time">
    <meta name="Description" content="JavaScript: Local Sidereal Time
          Clock">
    <meta name="Abstract" content="JavaScript: Local Sidereal Time
          Clock">
    <meta name="ROBOTS" content="ALL,INDEX,FOLLOW">
    <meta name="REVISIT-AFTER" content="20 days">
    <meta name="language" content="en,de,at,ch">
    <script language="JavaScript" src="${pageContext.request.contextPath}/resource/js/siderealTime/sidClock.js"></script>
    <script language="JavaScript">
      function JSClock()
      {
        calculate();
        var id = setTimeout("JSClock()", 1000);
      }
    </script>
  </head>
  <body onload="getDateTime();
      calculate();
      JSClock();" style="background-color: rgb(255, 255, 255);">
    <center>

      <form  method="post" name="myform">
        <center>
          <table style="width: 610px;" border="1" cellpadding="5" cellspacing="0">
            <tbody>
              <tr>
                <td>
                  <center>
                    <table bgcolor="#ffff99" border="1" bordercolor="#ffff00" cellpadding="0" cellspacing="0" width="600">
                      <tbody>
                        <tr>
                          <td>
                            <center>&nbsp;<font face="verdana, arial,
                                                helvetica, sans-serif">Date:</font>
                              <select name="Monat" onchange="theDateTime()">
                                <option value="Jan">Jan </option>
                                <option value="Feb">Feb </option>
                                <option value="Mar">Mar </option>
                                <option value="Apr">Apr </option>
                                <option value="May">May </option>
                                <option value="Jun">Jun </option>
                                <option value="Jul">Jul </option>
                                <option value="Aug">Aug </option>
                                <option value="Sep">Sep </option>
                                <option value="Oct">Oct </option>
                                <option value="Nov">Nov </option>
                                <option value="Dec">Dec </option>
                              </select>
                              &nbsp;&nbsp;&nbsp;
                              <select name="Tag" onchange="theDateTime()">
                                <option>01 </option>
                                <option>02 </option>
                                <option>03 </option>
                                <option>04 </option>
                                <option>05 </option>
                                <option>06 </option>
                                <option>07 </option>
                                <option>08 </option>
                                <option>09 </option>
                                <option>10 </option>
                                <option>11 </option>
                                <option>12 </option>
                                <option>13 </option>
                                <option>14 </option>
                                <option>15 </option>
                                <option>16 </option>
                                <option>17 </option>
                                <option>18 </option>
                                <option>19 </option>
                                <option>20 </option>
                                <option>21 </option>
                                <option>22 </option>
                                <option>23 </option>
                                <option>24 </option>
                                <option>25 </option>
                                <option>26 </option>
                                <option>27 </option>
                                <option>28 </option>
                                <option>29 </option>
                                <option>30 </option>
                                <option>31 </option>
                              </select>
                              &nbsp;&nbsp;&nbsp;
                              <select name="Jahr" onchange="theDateTime()">
                                <option value="1992">1992 </option>
                                <option value="1993">1993 </option>
                                <option value="1994">1994 </option>
                                <option value="1995">1995 </option>
                                <option value="1996">1996 </option>
                                <option value="1997">1997 </option>
                                <option value="1998">1998 </option>
                                <option value="1999">1999 </option>
                                <option value="2000">2000 </option>
                                <option value="2001">2001 </option>
                                <option value="2002">2002 </option>
                                <option value="2003">2003 </option>
                                <option value="2004">2004 </option>
                                <option value="2005">2005 </option>
                                <option value="2006">2006 </option>
                                <option value="2007">2007 </option>
                                <option value="2008">2008 </option>
                                <option value="2009">2009 </option>
                                <option value="2010">2010 </option>
                                <option value="2011">2011 </option>
                                <option value="2012">2012 </option>
                                <option value="2013">2013 </option>
                                <option value="2014">2014 </option>
                                <option value="2015">2015 </option>
                                <option value="2016">2016 </option>
                                <option value="2017">2017 </option>
                                <option value="2018">2018 </option>
                                <option value="2019">2019 </option>
                                <option value="2020">2020 </option>
                                <option value="2021">2021 </option>
                                <option value="2022">2022 </option>
                                <option value="2023">2023 </option>
                                <option value="2024">2024 </option>
                                <option value="2025">2025 </option>
                                <option value="2026">2026 </option>
                                <option value="2027">2027 </option>
                                <option value="2028">2028 </option>
                                <option value="2029">2029 </option>
                                <option value="2030">2030 </option>
                                <option value="2031">2031 </option>
                                <option value="2032">2032 </option>
                                <option value="2033">2033 </option>
                                <option value="2034">2034 </option>
                                <option value="2035">2035 </option>
                              </select>
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font face="verdana, arial, helvetica,
                                                                  sans-serif">Local Time:</font><font size="+1" face="verdana, arial,
                                                                  helvetica, sans-serif"> </font>
                              <select name="Stunde" onchange="theDateTime()">
                                <option>00 </option>
                                <option>01 </option>
                                <option>02 </option>
                                <option>03 </option>
                                <option>04 </option>
                                <option>05 </option>
                                <option>06 </option>
                                <option>07 </option>
                                <option>08 </option>
                                <option>09 </option>
                                <option>10 </option>
                                <option>11 </option>
                                <option>12 </option>
                                <option>13 </option>
                                <option>14 </option>
                                <option>15 </option>
                                <option>16 </option>
                                <option>17 </option>
                                <option>18 </option>
                                <option>19 </option>
                                <option>20 </option>
                                <option>21 </option>
                                <option>22 </option>
                                <option>23 </option>
                              </select>
                              &nbsp;&nbsp;&nbsp;
                              <select name="Minute" onchange="theDateTime()">
                                <option>00 </option>
                                <option>01 </option>
                                <option>02 </option>
                                <option>03 </option>
                                <option>04 </option>
                                <option>05 </option>
                                <option>06 </option>
                                <option>07 </option>
                                <option>08 </option>
                                <option>09 </option>
                                <option>10 </option>
                                <option>11 </option>
                                <option>12 </option>
                                <option>13 </option>
                                <option>14 </option>
                                <option>15 </option>
                                <option>16 </option>
                                <option>17 </option>
                                <option>18 </option>
                                <option>19 </option>
                                <option>20 </option>
                                <option>21 </option>
                                <option>22 </option>
                                <option>23 </option>
                                <option>24 </option>
                                <option>25 </option>
                                <option>26 </option>
                                <option>27 </option>
                                <option>28 </option>
                                <option>29 </option>
                                <option>30 </option>
                                <option>31 </option>
                                <option>32 </option>
                                <option>33 </option>
                                <option>34 </option>
                                <option>35 </option>
                                <option>36 </option>
                                <option>37 </option>
                                <option>38 </option>
                                <option>39 </option>
                                <option>40 </option>
                                <option>41 </option>
                                <option>42 </option>
                                <option>43 </option>
                                <option>44 </option>
                                <option>45 </option>
                                <option>46 </option>
                                <option>47 </option>
                                <option>48 </option>
                                <option>49 </option>
                                <option>50 </option>
                                <option>51 </option>
                                <option>52 </option>
                                <option>53 </option>
                                <option>54 </option>
                                <option>55 </option>
                                <option>56 </option>
                                <option>57 </option>
                                <option>58 </option>
                                <option>59 </option>
                              </select>
                              <input name="Absenden" value="Now" onclick="getDateTime();
                                  calculate();" type="button"></center>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                    <table bgcolor="#ffff99" border="1" bordercolor="#ffff00" cellpadding="0" cellspacing="0" width="600">
                      <tbody>
                        <tr>
                          <td rowspan="2">
                            <center><font face="verdana, arial,
                                          helvetica, sans-serif">Location:</font>
                              <p>
                                <select name="location" onchange="getLocation(document.myform.location.options[document.myform.location.selectedIndex].value)">
                                  <option value="">User Input </option>
                                  <option value="40.13/33.00/+2*Ankara">Ankara</option>
                                  <option value="51.51/-0.10/0*London">London</option>
                                  <option value="39.91/116.47/+8*Beijing">Beijing</option>
                                  <option value="40.3959/117.5745/+8*Beijing">XingLong</option>
                                </select>
                              </p>
                            </center>
                          </td>
                          <td>
                            <center><font face="verdana, arial,helvetica, sans-serif">Longitude</font></center>
                          </td>
                          <td>
                            <p><input name="longitude" value="13.41" size="10" type="text">
                              <font size="-1" face="verdana, arial, helvetica,ans-serif">deg</font> </p>
                          </td>
                          <td>
                            <center>
                              <select name="EastWest" onchange="EW()">
                                <option value="E">E </option>
                                <option value="W">W </option>
                              </select>
                              &nbsp;</center>
                          </td>
                          <td rowspan="2">
                            <center><font face="verdana, arial,
                                          helvetica, sans-serif">Time Zone:</font>
                              <p>
                                <select name="timezone" onchange="getTZone()">
                                  <option value="0">0 </option>
                                  <option value="1">+1 </option>
                                  <option value="-1">-1 </option>
                                  <option value="2" selected="selected">+2</option>
                                  <option value="-2">-2 </option>
                                  <option value="3">+3 </option>
                                  <option value="-3">-3 </option>
                                  <option value="4">+4 </option>
                                  <option value="-4">-4 </option>
                                  <option value="5">+5 </option>
                                  <option value="-5">-5 </option>
                                  <option value="6">+6 </option>
                                  <option value="-6">-6 </option>
                                  <option value="7">+7 </option>
                                  <option value="-7">-7 </option>
                                  <option value="8">+8 </option>
                                  <option value="-8">-8 </option>
                                  <option value="9">+9 </option>
                                  <option value="-9">-9 </option>
                                  <option value="10">+10 </option>
                                  <option value="-10">-10 </option>
                                  <option value="11">+11 </option>
                                  <option value="-11">-11 </option>
                                  <option value="12">+12 </option>
                                  <option value="-12">-12 </option>
                                </select>
                              </p>
                            </center>
                          </td>
                        </tr>
                        <tr>
                          <td colspan="3">
                            <center><input name="Absenden" value="Apply
                                           Long." onclick="getLongitude();" type="button"></center>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                    &nbsp;
                    <table bgcolor="#ffffcc" border="1" bordercolor="#ffffcc" cellpadding="0" cellspacing="0" width="600">
                      <tbody>
                        <tr>
                          <td width="200">
                            <p align="right"><font face="verdana, arial,
                                                   helvetica, sans-serif">Local Date and
                                Time: </font> </p>
                          </td>
                          <td width="170">
                            <p><input name="text1" value="" size="30" type="text"> </p>
                          </td>
                        </tr>
                        <tr>
                          <td width="200">
                            <p align="right"><font face="verdana, arial,
                                                   helvetica, sans-serif">UT is: </font> </p>
                          </td>
                          <td width="170">
                            <p><input name="UTime" value="" size="30" type="text"> </p>
                          </td>
                        </tr>
                        <tr>
                          <td width="200">
                            <p align="right"><font face="verdana, arial,
                                                   helvetica, sans-serif">the Julian Day
                                is: </font> </p>
                          </td>
                          <td width="170">
                            <p><input name="JulDayText" value="" size="18" type="text"> </p>
                          </td>
                        </tr>
                        <tr>
                          <td width="200">
                            <p align="right"><font face="verdana, arial,
                                                   helvetica, sans-serif">the Local
                                Sidereal Time is: </font> </p>
                          </td>
                          <td width="170">
                            <p><input name="LMST" value="" size="15" type="text"> </p>
                          </td>
                        </tr>
                        <tr>
                          <td width="200">
                            <p align="right"><font face="verdana, arial,
                                                   helvetica, sans-serif">the Declination
                                of the Sun is: </font> </p>
                          </td>
                          <td width="170">
                            <p><input name="declinText" value="" size="10" type="text"><font size="-1" face="verdana, arial, helvetica,
                                                                                             sans-serif">deg</font> </p>
                          </td>
                        </tr>
                        <tr>
                          <td width="200">
                            <p align="right"><font face="verdana, arial,
                                                   helvetica, sans-serif">the Equation of
                                Time is: </font> </p>
                          </td>
                          <td width="170">
                            <p><input name="eotText" value="" size="12" type="text"><font size="-1" face="verdana, arial, helvetica,
                                                                                          sans-serif">min</font> </p>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </center>
                </td>
              </tr>
            </tbody>
          </table>
          <br>
          <a href="http://www.jgiesen.de/SiderealTime/index.html"><font size="-1"><font face="verdana, arial, helvetica, sans-serif">Sidereal
                Time Java applet</font></font></a><br>
        </center>
      </form>
    </center>


  </body>
</html>
