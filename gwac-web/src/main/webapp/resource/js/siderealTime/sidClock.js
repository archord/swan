/*
 JavaScript Sun Table Calculator
 ?2001 Juergen Giesen
 http://www.jgiesen.de
 */



var dat, JD, UT, offset, dIM, RA, EOT
var lat, longit, offset, locOffset;
var monthName = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec');
var dayName = new Array('Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun');

var ok, OK, LatLong, locName, url;
var ns, ew, timezoneString, lengthDayString;
var string1 = "ex1";
var deg = String.fromCharCode(176);

var hours, utHours, minutes, seconds, day, utDay, month, utMonth, year, utYear;

function setMenu(d, m, y, h, min) {
  if (d > 0)
    document.myform.Tag.options[d - 1].selected = true;
  else
    alert(d);
  if (m > 0)
    document.myform.Monat.options[m - 1].selected = true;
  else
    alert(m);
  document.myform.Jahr.options[y - 1992].selected = true;
  document.myform.Stunde.options[h].selected = true;
  document.myform.Minute.options[min].selected = true;
}

function currentTime() {

  var curDat = new Date();
  var curOffset = curDat.getTimezoneOffset();
  if (curOffset >= 1320)
    curOffset = curOffset - 1440;
  var dLocOffset = -curOffset / 60;

  utHours = curDat.getHours() - dLocOffset;
  /*
   LatLong =  document.myform.location.options[document.myform.location.selectedIndex].value;
   if (document.myform.location.selectedIndex != 0) getLocation(LatLong);
   var nn=2*locOffset;
   if (locOffset>0) nn=nn-1;
   document.myform.timezone.options[Math.abs(nn)].selected=true;
   */
  hours = utHours + locOffset;
  if (hours < 0)
    hours += 24;
  if (hours > 23)
    hours -= 24;
  minutes = curDat.getMinutes();
  seconds = curDat.getSeconds();
  year = curDat.getYear();
  if (year < 1900)
    year = year + 1900;
  month = curDat.getMonth() + 1;
  day = curDat.getDate();

  utMinutes = minutes;
  utMonth = month;
  utYear = year;

  document.myform.text1.value = writeDateTime(hours, day, month);
  document.myform.UTime.value = writeDateTime(utHours, utDay, utMonth);

  setMenu(day, month, year, hours, minutes);
}

function writeDateTime(h, d, m) {

  var str = " " + year + ", " + document.myform.Monat.options[m - 1].value + " ";
  if (d < 10)
    str = str + "0" + d;
  else
    str = str + d;
  str = str + " at  ";
  if (h < 10)
    str = str + "0" + h + ":";
  else
    str = str + h + ":";
  if (minutes < 10)
    str = str + "0" + minutes;
  else
    str = str + minutes;
  if (seconds < 10)
    str = str + ":0" + seconds;
  else
    str = str + ":" + seconds;
  return str;
}

function getDateTime() {

  var LatLong = document.myform.location.options[document.myform.location.selectedIndex].value;
  var n = LatLong.length;
  var latStr = "", s = 0, s1 = 0, star = 0;
  for (var i = 0; i < n; i++) {
    if (LatLong.charAt(i) == '/') {
      s = i;
      break
    }
  }
  ;
  for (var i = s + 1; i < n; i++) {
    if (LatLong.charAt(i) == '/') {
      s1 = i;
      break
    }
  }
  ;
  for (var i = s1 + 1; i < n; i++) {
    if (LatLong.charAt(i) == '*') {
      star = i;
      break
    }
  }
  ;
  locName = LatLong.substring(star + 1, n);
  url = this.location.toString();

  OK = "OMED";

  var tzString = LatLong.substring(s1 + 1, star);
  locOffset = Number(tzString);

//getLocation(LatLong);

  var nn = 2 * locOffset;
  if (locOffset > 0)
    nn = nn - 1;
  document.myform.timezone.options[Math.abs(nn)].selected = true;

  //offset=-60*document.myform.timezone.options[document.myform.timezone.selectedIndex].value;
  //locOffset = -offset/60;

//alert(locOffset)

  ok = OK.charAt(3) + OK.charAt(2) + OK.charAt(1) + OK.charAt(0);


  var dat = new Date();

//alert(dat.toString())

  hours = dat.getHours();

  var curOffset = dat.getTimezoneOffset();
  if (curOffset >= 1320)
    curOffset = curOffset - 1440;
  curOffset = -curOffset / 60;



  hours = hours - curOffset + locOffset;
  if (hours >= 24) {
    hours = hours - 24;
    day = day + 1;
  }
  daysInMonth(month, year);
  if (day > dIM) {
    day = 1;
    month = month + 1;
  }
  if (month > 12) {
    month = 1;
    year = year + 1;
  }

  if (hours < 0) {
    hours = hours + 24;
    day = day - 1;
  }
  daysInMonth(month, year);
  if (day < 1) {
    day = dIM;
    month = month - 1;
  }
  if (month < 1) {
    month = 1;
    year = year - 1;
  }



  utHours = hours - locOffset;

  if (utHours >= 24) {
    utHours = utHours - 24;
    utDay = utDay + 1;
  }
  if (utDay > dIM) {
    utDay = 1;
    utMonth = utMonth + 1;
  }
  if (utMonth > 12) {
    utMonth = 1;
    utYear = utYear + 1;
  }


  if (utHours < 0) {
    utHours = utHours + 24;
    utDay = utDay - 1;
  }
  if (utDay < 1) {
    utDay = dIM;
    utMonth = utMonth - 1;
  }
  if (utMonth < 1) {
    utMonth = 1;
    utYear = utYear - 1;
  }


//alert(hours)
//alert(dat.getTimezoneOffset())
  minutes = dat.getMinutes();
  seconds = dat.getSeconds();
  year = dat.getYear();
  if (year < 1900)
    year = year + 1900;
  month = dat.getMonth() + 1;
  day = dat.getDate();

  var millis = dat.getTime();
  millis = millis - locOffset * 3600 * 1000;

  var d = new Date(millis);

//utHours = d.getHours();

  utDay = d.getDate();
  utMonth = d.getMonth() + 1;
  utYear = d.getYear();
  if (utYear < 1900)
    utYear = utYear + 1900;




  UT = utHours + minutes / 60 + seconds / 3600;

  document.myform.text1.value = writeDateTime(hours, day, month);
  document.myform.UTime.value = writeDateTime(utHours, utDay, utMonth);


  setMenu(day, month, year, hours, minutes);

  myMonth = month;

  //EOT = eot(utDay,utMonth,utYear,UT);
  theJulDay();
}

function ut() {
  utHours = hours - locOffset;
}

function setUT() {

  var theDate = new Date(year, month - 1, day, hours, minutes, seconds);
  var millis = theDate.getTime();

  millis = millis - locOffset * 3600 * 1000;

  var d = new Date(millis);
  utHours = d.getHours();
  utMinutes = d.getMinutes();
  utDay = d.getDate();
  utMonth = d.getMonth() + 1;
  utYear = d.getYear();
  if (utYear < 1900)
    utYear = utYear + 1900;

  UT = utHours + utMinutes / 60 + seconds / 3600;

  document.myform.text1.value = writeDateTime(hours, day, month);
  document.myform.UTime.value = writeDateTime(utHours, utDay, utMonth);

  //EOT = eot(utDay,utMonth,utYear,UT);
}


function declination(day, month, year, UT) {

  var K = Math.PI / 180.0;
  var jd = JulDay(day, month, year, UT);
  var T = (jd - 2451545.0) / 36525.0;
  var L0 = 280.46645 + (36000.76983 + 0.0003032 * T) * T;
  var M = 357.52910 + (35999.05030 - (0.0001559 * T + 0.00000048 * T) * T) * T;
  M = K * M;
  var C = (1.914600 - 0.004817 * T - 0.000014 * T * T) * Math.sin(M) + (0.019993 - 0.000101 * T) * Math.sin(2 * M) + 0.000290 * Math.sin(3 * M);
  var theta = L0 + C;
  var omega = 125.04 - 1934.136 * T;
  var lambda = theta - 0.00569 - 0.00478 * Math.sin(K * omega);
  var eps0 = 23.0 + 26.0 / 60.0 + 21.448 / 3600.0 - (46.8150 * T + 0.00059 * T * T - 0.001813 * T * T * T) / 3600;
  var eps = eps0 + 0.00256 * Math.cos(K * omega);
  var declin = Math.sin(K * eps) * Math.sin(K * lambda);
  declin = Math.asin(declin) / K;
  RA = Math.atan2(Math.cos(K * eps) * Math.sin(K * lambda), Math.cos(K * lambda)) / K;
  if (RA < 0)
    RA = RA + 360;
  return declin;
}

function getLocation(LatLong) {

  var n = LatLong.length;
  var latStr = "", s = 0, s1 = 0, star = 0;
  for (var i = 0; i < n; i++) {
    if (LatLong.charAt(i) == '/') {
      s = i;
      break
    }
  }
  ;
  for (var i = s + 1; i < n; i++) {
    if (LatLong.charAt(i) == '/') {
      s1 = i;
      break
    }
  }
  ;
  for (var i = s1 + 1; i < n; i++) {
    if (LatLong.charAt(i) == '*') {
      star = i;
      break
    }
  }
  ;

  var latStr = LatLong.substring(0, s);
  //document.myform.latitude.value=Math.abs(latStr);
  lat = Number(latStr);
  //if (lat>=0) {ns=" N";  document.myform.NorthSouth.options[0].selected=true;}
  //else {ns=" S"; document.myform.NorthSouth.options[1].selected=true;}

  var longStr = LatLong.substring(s + 1, s1);
  document.myform.longitude.value = Math.abs(longStr);
  longit = Number(longStr);
  if (longit >= 0) {
    ew = " E";
    document.myform.EastWest.options[0].selected = true;
  }
  else {
    ew = " W";
    document.myform.EastWest.options[1].selected = true;
  }
  var tzString = LatLong.substring(s1 + 1, star);
  locOffset = Number(tzString);


  var nn = 2 * locOffset;
  if (locOffset > 0)
    nn = nn - 1;
  document.myform.timezone.options[Math.abs(nn)].selected = true;

  locName = LatLong.substring(star + 1, n);

  var millis = Date.UTC(year, utMonth - 1, utDay, utHours, minutes, seconds);
  millis = millis + locOffset * 3600 * 1000;

  var d = new Date();
  var doffset = d.getTimezoneOffset();
  if (doffset >= 1320)
    doffset = doffset - 1440;
  var dlocOffset = -doffset / 60;
  millis = millis - dlocOffset * 3600 * 1000;
  d = new Date(millis);
  hours = d.getHours();
  day = d.getDate();
  month = d.getMonth() + 1;
  year = d.getYear();
  if (year < 1900)
    year = year + 1900;

  setMenu(day, month, year, hours, minutes);

  if (locOffset >= 0)
    timezoneString = "GMT + " + locOffset;
  else
    timezoneString = "GMT  " + locOffset;

  //UT = utHours + minutes/60 + seconds/3600;
  document.myform.text1.value = writeDateTime(hours, day, month);

  calculate();
}


function HoursMinutes(time) {
  var t = time;
  time = Math.abs(time);
  var min = Math.round(60 * (time - Math.floor(time)));
  var str;
  if (min >= 10)
    str = Math.floor(time) + ":" + min;
  else
    str = Math.floor(time) + ":0" + min;
  if (min == 60)
    str = Math.floor(time + 1) + ":00";
  if (t < 0)
    return "-" + str;
  else
    return str;
}

function HoursMinutesSeconds(time) {

  var h = Math.floor(time);
  var min = Math.floor(60.0 * frac(time));
  var secs = Math.round(60.0 * (60.0 * frac(time) - min));

  var str;
  if (min >= 10)
    str = h + ":" + min;
  else
    str = h + ":0" + min;
  //if (min==60) str=(h+1)+":00";
  if (secs < 10)
    str = str + ":0" + secs;
  else
    str = str + ":" + secs;
  return " " + str;

}


function DegreesMinutes(time) {
  var t = time;
  time = Math.abs(time);
  var min = Math.round(60 * (time - Math.floor(time)));
  var str;
  //if (min>=10) str=Math.floor(time)+" deg "+min + " min";
  //else  str=Math.floor(time)+" deg 0"+min + " min";
  if (min >= 10)
    str = Math.floor(time) + deg + " " + min + "'";
  else
    str = Math.floor(time) + deg + " 0" + min + "'";
  if (min == 60)
    str = Math.floor(time + 1) + ":00" + "'";
  if (t < 0)
    return "-" + str;
  else
    return str;
}

function TZone() {

  var index = document.myform.timezone.selectedIndex;
  var delta = document.myform.timezone.options[index].value;

  var dat = new Date(Date.UTC(year, month - 1, utDay, utHours, minutes, seconds));
  var millis = Date.UTC(year, month - 1, day, utHours, minutes, seconds);
  var theOffset = dat.getTimezoneOffset();
  if (theOffset >= 1320)
    theOffset = theOffset - 1440;
  var dLocOffset = -theOffset / 60;

  millis = millis - (dLocOffset - delta) * 3600 * 1000;

  var dat1 = new Date(millis);
  //hours = dat1.getHours();
  minutes = dat1.getMinutes();
  seconds = dat1.getSeconds();
  year = dat1.getYear();
  if (year < 1900)
    year = year + 1900;
  month = dat1.getMonth() + 1;

  day = dat1.getDate();

  if (delta == 0) {
    day = utDay;
    month = utMonth;
  }


  if (delta > 0) {
    if ((month > utMonth) && (Number(utHours) + Number(delta) >= 24))
    {
      month = utMonth;
      day = utDay + 1;
      daysInMonth(month, year);
      if (day > dIM) {
        day = 1;
        month = month + 1;
      }
    }
    if ((month == utMonth) || (Number(utHours) + Number(delta) < 24))
    {
      if (day > utDay + 1) {
        day = day - 1;
      }
      ;
      if (month > utMonth) {
        day = 1
      }
      ;
      if (Number(utHours) + Number(delta) < 24) {
        day = utDay;
        month = utMonth;
        year = utYear;
      }
      if ((year > utYear) && (day > 1)) {
        day = day - 1;
      }
    }
  }


  if (delta < 0) {

    daysInMonth(month, year);
    if ((day == dIM) && (utDay == 2)) {
      day = 1;
      month = utMonth
    }
    ;


    if ((day != dIM) && (utDay != 2)) {
      if (day < utDay - 1)
        day = day + 1;
      if (month < utMonth) {
        daysInMonth(month, year);
        day = dIM
      }

      if (Number(utHours) + Number(delta) >= 0) {
        day = utDay;
        month = utMonth;
      }

      if (year < utYear) {
        if (day < 31)
          day = day + 1;
      }
    }
  }

}

function getTZone() {

  var index = document.myform.timezone.selectedIndex;
  var delta = document.myform.timezone.options[index].value;

  var dat = new Date(Date.UTC(year, month - 1, utDay, utHours, minutes, seconds));
  var millis = Date.UTC(year, month - 1, day, utHours, minutes, seconds);
  var theOffset = dat.getTimezoneOffset();
  if (theOffset >= 1320)
    theOffset = theOffset - 1440;
  var dLocOffset = -theOffset / 60;

  millis = millis - (dLocOffset - delta) * 3600 * 1000;

  var dat1 = new Date(millis);
  hours = dat1.getHours();
  minutes = dat1.getMinutes();
  seconds = dat1.getSeconds();
  year = dat1.getYear();
  if (year < 1900)
    year = year + 1900;
  month = dat1.getMonth() + 1;

  day = dat1.getDate();

  if (delta == 0) {
    day = utDay;
    month = utMonth;
  }

  if (delta > 0) {

    if ((month > utMonth) && (Number(utHours) + Number(delta) >= 24))
    {
      month = utMonth;
      day = utDay + 1;
      daysInMonth(month, year);
      if (day > dIM) {
        day = 1;
        month = month + 1;
      }
      ;
    }
    ;

    if ((month == utMonth) || (Number(utHours) + Number(delta) < 24))
    {
      if (day > utDay + 1) {
        day = day - 1;
      }
      ;
      if (month > utMonth) {
        day = 1
      }
      ;
      if (Number(utHours) + Number(delta) < 24) {
        day = utDay;
        month = utMonth;
        year = utYear;
      }
      if ((year > utYear) && (day > 1)) {
        day = day - 1;
      }
    }
  }


  if (delta < 0) {

    daysInMonth(month, year);
    if ((day == dIM) && (utDay == 2)) {
      day = 1;
      month = utMonth
    }
    ;

    if ((day != dIM) && (utDay != 2)) {
      if (day < utDay - 1)
        day = day + 1;
      if (month < utMonth) {
        daysInMonth(month, year);
        day = dIM
      }

      if (Number(utHours) + Number(delta) >= 0) {
        day = utDay;
        month = utMonth;
      }

      if (year < utYear) {
        if (day < 31)
          day = day + 1;
      }
    }
  }

  setMenu(day, month, year, hours, minutes);
  document.myform.text1.value = writeDateTime(hours, day, month);

  calculate();

}


function daysInMonth(m, y) {
  var n = 31
  m = m - 1
  if ((m == 0) || (m == 2) || (m == 4) || (m == 6) || (m == 7) || (m == 9) || (m == 11))
    n = 31
  if ((m == 3) || (m == 5) || (m == 8) || (m == 10))
    n = 30;
  if (m == 1) {
    n = 28;
    if ((y % 4) == 0)
      n = 29
    if ((y % 100) == 0)
      n = 28
    if ((y % 400) == 0)
      n = 29
  }
  dIM = n;

}



function calculate() {

  TZone();

  var dat1 = new Date();
  seconds = dat1.getSeconds();
  if (seconds == 0) {
    minutes = minutes + 1;
    if (minutes == 60)
    {
      minutes = 0;
      hours = hours + 1;
      utHours = utHours + 1;
      if (hours == 24) {
        hours = 0;
        day = day + 1;
      }
      if (utHours == 24) {
        utHours = 0;
        utDay = utDay + 1;
      }
      daysInMonth(month, year);
      if (day > dIM) {
        day = 1;
        month = month + 1;
      }
      if (utDay > dIM) {
        utDay = 1;
        utMonth = utMonth + 1;
      }
      if (month > 12) {
        month = 1;
        year = year + 1;
      }
      if (utMonth > 12) {
        utMonth = 1;
        utYear = utYear + 1;
      }
    }
  } // if (seconds==0)

  UT = utHours + minutes / 60 + seconds / 3600;

  var str = " " + Math.round(1000 * declination(utDay, utMonth, utYear, UT)) / 1000;
  document.myform.declinText.value = str;

  //lat = document.myform.latitude.value;
  longit = document.myform.longitude.value;
  if (document.myform.EastWest.selectedIndex == 1)
    longit = -Math.abs(longit);
  //if (document.myform.NorthSouth.selectedIndex==1) lat=-Math.abs(lat);

  EOT = eot(utDay, utMonth, utYear, UT);
  str = " " + EOT;
  document.myform.eotText.value = str;

  theJulDay();

  document.myform.text1.value = writeDateTime(hours, day, month);
  document.myform.UTime.value = writeDateTime(utHours, utDay, utMonth);
  document.myform.LMST.value = LM_Sidereal_Time(JulDay(day, month, year, UT), longit);
  
  setMenu(day, month, year, hours, minutes);

}


function theDay() {
  daysInMonth(month, year);
  var index = document.myform.Tag.selectedIndex;

  if (index < dIM)
    day = index + 1;
  else {
    day = dIM;
    document.myform.Tag.options[dIM - 1].selected = true
  }
  ;

  setUT();
  //calculate();
}

function theMonth() {
  month = Number(document.myform.Monat.selectedIndex) + Number(1);
  theDay();
  //calculate();
}

function theYear() {
  year = document.myform.Jahr.selectedIndex + 1992;
  theDay();
  //calculate();
}

function theHour() {
  hours = document.myform.Stunde.selectedIndex;
//alert(hours)
}


function theMinute() {
  minutes = document.myform.Minute.selectedIndex;
  seconds = 0;
}

function theJulDay() {
  jd = JulDay(utDay, utMonth, utYear, UT)
  document.myform.JulDayText.value = " " + Math.round(100000 * jd) / 100000;
}

/*
 function JulDay (date, month, year, UT){
 if (year<1900) year=year+1900
 if (month<=2) {month=month+12; year=year-1}
 A = Math.floor(year/100);
 B = -13;
 JD =  Math.floor(365.25*(year+4716)) + Math.floor(30.6001*(month+1)) + date + B -1524.5 + UT/24.0;
 return JD
 }
 */

function JulDay(d, m, y, u) {
  if (y < 1900)
    y = y + 1900
  if (m <= 2) {
    m = m + 12;
    y = y - 1
  }
  A = Math.floor(y / 100);
  JD = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + d - 13 - 1524.5 + u / 24.0;
  return JD
}


function theDateTime() {

  theDay();
  theMonth();
  theYear();
  theHour();
  theMinute();
  setUT();
  theJulDay();
  calculate();

}


function sunL(T) {
  var L = 280.46645 + 36000.76983 * T + 0.0003032 * T * T
  L = L % 360
  if (L < 0)
    L = L + 360
  return L
}

function deltaPSI(T) {
  var K = Math.PI / 180.0
  var deltaPsi, omega, LS, LM
  LS = sunL(T)
  LM = 218.3165 + 481267.8813 * T
  LM = LM % 360
  if (LM < 0)
    LM = LM + 360
  omega = 125.04452 - 1934.136261 * T + 0.0020708 * T * T + T * T * T / 450000
  deltaPsi = -17.2 * Math.sin(K * omega) - 1.32 * Math.sin(K * 2 * LS) - 0.23 * Math.sin(K * 2 * LM) + 0.21 * Math.sin(K * 2 * omega)
  deltaPsi = deltaPsi / 3600.0
  return deltaPsi
}

function EPS(T) {
  var K = Math.PI / 180.0
  var LS = sunL(T)
  var LM = 218.3165 + 481267.8813 * T
  var eps0 = 23.0 + 26.0 / 60.0 + 21.448 / 3600.0 - (46.8150 * T + 0.00059 * T * T - 0.001813 * T * T * T) / 3600
  var omega = 125.04452 - 1934.136261 * T + 0.0020708 * T * T + T * T * T / 450000
  var deltaEps = (9.20 * Math.cos(K * omega) + 0.57 * Math.cos(K * 2 * LS) + 0.10 * Math.cos(K * 2 * LM) - 0.09 * Math.cos(K * 2 * omega)) / 3600
  return eps0 + deltaEps
}

function eot(date, month, year, UT) {
  var K = Math.PI / 180.0
  var T = (JulDay(date, month, year, UT) - 2451545.0) / 36525.0
  var eps = EPS(T);
  var dummy = declination(date, month, year, UT);
  var LS = sunL(T)
  var deltaPsi = deltaPSI(T)
  var E = LS - 0.0057183 - RA + deltaPsi * Math.cos(K * eps)
  if (E > 5)
    E = E - 360.0
  E = E * 4; // deg. to min		
  E = Math.round(1000 * E) / 1000
  return E
}



function getLongitude() {
//alert(longit)
  str = document.myform.longitude.value;
  var n = str.length;
  for (var i = 0; i < n; i++) {
    c = str.charAt(i);
    if ((c != '0') && (c != '1') && (c != '2') && (c != '3') && (c != '4') && (c != '5') && (c != '6') && (c != '7') && (c != '8') && (c != '9') && (c != '+') && (c != '.')) {
      alert("Error on longitude value !" + "\n" + "Enter decimal degree value, e.g.:" + "\n" + " 8.34 if East, or -72.34 if West.");
      document.myform.longitude.value = 0;
      break;
    }
    ;
  }
  if (Math.abs(Number(str)) > 180) {
    alert("Longitude must less or equal to 180 degrees !");
    document.myform.longitude.value = 0;
  }
  ;
  longit = Number(str);
  //if (document.myform.NorthSouth.selectedIndex==1) lat=-lat;
  if (document.myform.EastWest.selectedIndex == 1)
    longit = -Math.abs(longit);
  if (longit >= 0)
    ew = " E";
  else
    ew = " W";
  document.myform.location.options[0].selected = true;
  calculate();
}




function EW() {
  if (document.myform.EastWest.selectedIndex == 0)
    longit = Math.abs(longit);
  else
    longit = -Math.abs(longit);
  locName = "User Input";
  document.myform.location.options[0].selected = true;
  locName = "User Input";
  calculate()
}


function dayString(jd) {
  var num;
  var dayName = new Array('Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun');
  num = jd + 0.5;
  num = num - Math.floor(num / 7) * 7;

  return dayName[Math.floor(num)];
}

function GM_Sidereal_Time(jd) {
  var t_eph, ut, MJD0, MJD;

  MJD = jd - 2400000.5;
  MJD0 = Math.floor(MJD);
  ut = (MJD - MJD0) * 24.0;
  t_eph = (MJD0 - 51544.5) / 36525.0;
  return  6.697374558 + 1.0027379093 * ut + (8640184.812866 + (0.093104 - 0.0000062 * t_eph) * t_eph) * t_eph / 3600.0;
}

function LM_Sidereal_Time(jd, longitude) {
  var GMST = GM_Sidereal_Time(jd);
  var LMST = 24.0 * frac((GMST + longitude / 15.0) / 24.0);
  return HoursMinutesSeconds(LMST);
}

function frac(X) {
  X = X - Math.floor(X);
  if (X < 0)
    X = X + 1.0;
  return X;
}

