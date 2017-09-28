/**
 * 参考小猛的文档，有待调试
 */

  function updateLocalSiderealTime(){
    var xllgt = 117.5745;
    var dateUtc = new Date();
    console.log(dateUtc)
    dateUtc.setHours(10);
    dateUtc.setMinutes(20);
    dateUtc.setSeconds(54);
    console.log(dateUtc)
    dateUtc.setHours(dateUtc.getHours()-8);
    console.log(dateUtc)
    var mjd = getMJD(dateUtc);
    console.log(mjd)
    var t = getT(mjd);
    console.log(t)
    var lmst = getLMST(t, xllgt);
    console.log(lmst);
    console.log(lmst*60%60);
    console.log(lmst*3600%3600);
  }

  //计算修正儒略日
  function getMJD(dateUtc) {

    //var dateUtc = new Date();
    var year = dateUtc.getFullYear();
    var month = dateUtc.getMonth()+1;
    var day = dateUtc.getDate();
    var hour = dateUtc.getHours();
    var minute = dateUtc.getMinutes();
    var second = dateUtc.getSeconds();
    console.log("*********")
    console.log(year)
    console.log(month)
    console.log(day)
    console.log(hour)
    console.log(minute)
    console.log(second)
    console.log("*********")

    var tday = (hour + minute / 60.0 + second / 3600.0) / 24;
    var my = (month - 14) / 12;
    var iypmy = year + my;
    console.log(tday)
    console.log(my)
    console.log(iypmy)
    console.log("*********")
    var mjd = ((1461 * (iypmy + 4800)) / 4 + (367 * parseInt(month - 2 - 12 * my)) / 12 - (3 * ((iypmy + 4900) / 100)) / 4 + day - 2432076) + tday;
    return mjd;
  }

  //计算儒略世纪
  function getT(mjd) {
    return (mjd - 51544.5) / 36525;
  }

  // 计算本地平恒星时（lmst）
  //  输入：儒略世纪（t），本地地理经度（量纲：角度。东经为正，西经为负）
  //输出：本地平恒星时（lmst：量纲：小时）
  //中间量：格林威治平恒星时（gmst：量纲：角度）
  //辅助函数：cycle_mod（模函数，周期：360 -- 圆周角度）
  function getLMST(t, longitude) {
    var gmst = (280.46061837 + t * (13185000.77005374225 + t * (3.87933E-4 - t / 38710000.0))) % 360;
    var lmst = ((gmst + longitude) % 360) / 15;
    return lmst;
  }