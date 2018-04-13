function makeRequest(url) {
    http_request = false;
    if (window.XMLHttpRequest) {
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    } else if (window.ActiveXObject) {
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
            }
        }
    }
    if (!http_request) {
        alert("您的浏览器不支持当前操作，请使用 IE 5.0 以上版本!");
        return false;
    }
    http_request.onreadystatechange = stateChange;
    http_request.open('GET', url, true);
    http_request.setRequestHeader("If-Modified-Since", "0");
    http_request.send(null);
    //setTimeout("makeRequest('" + url + "')", 1000);
}

function stateChange() {
    if (http_request.readyState == 4) {
        if (http_request.status == 0 || http_request.status == 200) {
            var result = http_request.responseText;
            var srcData = result.split(':');
            var data = eval('(' + srcData[1] + ')'); // 利用eval函数将返回的文本流转换成JS对象
            
            var tData = getData1(data[0]);
            drawCurveDay(tData,"#placeholder1");

        } else {// http_request.status != 200
            alert("can't connect to server!");
        }
    }
}

function getData(dataArray) {
    var data = new Array(dataArray.length);
    for (var j = 0; j < dataArray.length; j++){
        for (var i = 0; i < dataArray[j][1].length; ++i)
            dataArray[j][1][i][0] += 8 * 60 * 60 * 1000;
        if(j==0)
            data[j] = { label: dataArray[j][0], color: 'red', data: dataArray[j][1]};
        else
            data[j] = { label: dataArray[j][0], data: dataArray[j][1]};
    }
    return data;
}

function getData1(dataArray) {
    for (var j = 0; j < dataArray.length; j++)
        for (var i = 0; i < dataArray[j][1].length; ++i)
            dataArray[j][1][i][0] += 8 * 60 * 60 * 1000;

    var data = [
    {
        label: "CIN",
        data: dataArray[0][1],
        color: 'red',
        lines: {show: true}
    },
    {
        label: "COUT",
        data: dataArray[1][1],
        color: 'green',
        lines: {show: true}
    },
    {
        label: "CCD off",
        data: dataArray[2][1],
        lines: {show: true,lineWidth: 6}
    },
    {
        label: "CCD on",
        data: dataArray[3][1],
        lines: {show: true}
    }
    ];
    return data;
}

function drawCurveDay(data, classid) {

     var options = {
        xaxes: [{
            axisLabel: 'Time',
            mode: "time",
            timeformat: "%b-%d %0h:%0M", //"%y/%m/%d/%h/%M/%b",
            monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
        }],
        yaxes: [{
            position: 'left',
            axisLabel: 'Temperature(℃)'
        }]
    };
    $.plot($(classid), data, options);
}

/**
 * Comment
 */
function temp(parameters) {

    var data2 = [
        {
            label: "RD POINT",
            data: d2,
            color: 'green',
            points: {show: true, fill:true, fillColor:'blue', radius: 3}
        }
    ];

    var options1 = {
        //legend: { container: $('#legendposition') },
        xaxis: {
            label:'CIN',
            show:true,
            tickDecimals: 0,
            color:'red',
            position: "bottom",
            mode: "time"
        },
        yaxis: {
            label:'COUT',
            show:true,
            tickDecimals: 1,
            color:'blue',
            position: "left",
            ticks: 10,
            labelcolor:'red'
        }, //, min: 0, max: 100
        grid : {
            show:true,
            color: 'red',
            borderColor:'#232323',
            borderWidth: 2,
            markings: function (axes) {
                var markings = [];
                for (var x = Math.floor(axes.xaxis.min); x < axes.xaxis.max; x += 4)
                    markings.push({
                        xaxis: {
                            from: x,
                            to: x + 2
                        }
                    });
                return markings;
            }
        }
    };

}
