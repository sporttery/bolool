//https://www.310win.com/buy/DanChang.aspx?TypeID=5&issueNum=21054
//从310win网上抓取数据
function convertMatchInfo(arr) {
    var match = {};
    match.matchId = arr[0];
    match.home = arr[17];
    match.away = arr[20];
    if (arr[4] == -1) {
        match.halfscore = arr[7] + "-" + arr[8];
        match.fullscore = arr[5] + "-" + arr[6];
    }
    match.matchStatus = arr[4];

    var lastsp = arr[30].split(",");
    match.sp1 = lastsp[0] == "" ? 0 : lastsp[0];
    match.sp2 = lastsp[1] == "" ? 0 : lastsp[1];
    match.sp3 = lastsp[2] == "" ? 0 : lastsp[2];
    match.playtime = arr[3];
    match.endtime = arr[2];
    match.num = arr[16];
    return match;
}
//
function ajaxGet(url, succ, err) {
    $.ajax({
        type: "get",
        url: url,
        async: false,
        beforeSend: function (request) {

        },
        success: function (d) {
            if (succ) {
                succ(d);
            } else {
                console.log("SUCCESS");
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (err) {
                err(XMLHttpRequest, textStatus, errorThrown);
            } else {
                alert(textStatus);
            }
        }
    });
}

///buy/DanChang.aspx?TypeID=5&issueNum=21054
function loadData(issue, companyId) {
    //bet365 8

    if (!companyId) {
        companyId = 1;
    }
    ajaxGet("/buy/DanChang.aspx?TypeID=5&issueNum=" + issue, function (d) {
        if (typeof M != "undefined") {
            delete M;
        }
        var start = d.indexOf('var M =');
        var end = d.indexOf('var rq2');
        var scriptStr = d.substring(start, end);
        start = d.indexOf('id="MatchTable"');
        end = d.indexOf("<div id=\"divDaohang\"");
        var tableStr = "<table " + d.substring(start, end)
        eval(scriptStr);
        var matchTable = $(tableStr);
        var matchData = [];
        matchTable.find("[id^=row]").each((idx, tr) => {
            var id = tr.id.replace("row_", "");
            var leagueName = $(tr).find("td:eq(1)").text().trim();
            var matchArr = M[parseInt(id)];
            matchInfo = convertMatchInfo(matchArr);
            var rq = $(tr).find("td:eq(5)").text().trim();
            var sp1 = $(tr).find("#sp_" + id + "_1").text().trim();
            var sp2 = $(tr).find("#sp_" + id + "_2").text().trim();
            var sp3 = $(tr).find("#sp_" + id + "_3").text().trim();
            var result, sp;
            if (sp1 != "") {
                result = "胜";
                sp = sp1;
            } else if (sp2 != "") {
                result = "平";
                sp = sp2;
            } else if (sp3 != "") {
                result = "负";
                sp = sp3;
            }
            matchInfo.leagueName = leagueName;
            if (matchInfo.matchStatus == -1) {
                matchInfo.result = result;
                matchInfo.sp = isNaN(sp) ? "0" : sp;
            }
            matchInfo.rq = rq;
            matchInfo.issue = issue;
            matchInfo.id=matchInfo.issue+"-"+matchInfo.num;
            matchData[id] = matchInfo;
        });
        //澳门欧盘
        ajaxGet("/handle/1x2.aspx?issuenum=" + issue + "&typeid=5&companyid=" + companyId + "&" + (+new Date), function (xml) {
            $(xml).find("i").each((idx, ele) => {
                var txt = $(ele).text();
                var arr = txt.split(",");
                var num = arr[1];
                var w = arr[3];
                var d = arr[4];
                var l = arr[5];
                var w1 = arr[6];
                var d1 = arr[7];
                var l1 = arr[8];
                var matchInfo = matchData[num];
                if(matchInfo){
                    matchInfo.w = w;//初
                    matchInfo.d = d;
                    matchInfo.l = l;
                    matchInfo.w1 = w1;//即时
                    matchInfo.d1 = d1;
                    matchInfo.l1 = l1;
                }
            });
        });
        //澳门亚盘
        ajaxGet("/handle/handicap.aspx?issuenum=" + issue + "&typeid=5&companyid=" + companyId + "&" + (+new Date), function (xml) {
            $(xml).find("i").each((idx, ele) => {
                var txt = $(ele).text();
                var arr = txt.split(",");
                var num = arr[1];
                var h = arr[3];
                var pan = arr[4];
                var a = arr[5];
                var matchInfo = matchData[num];
                if(matchInfo){
                    matchInfo.h = h;
                    matchInfo.pan = pan;
                    matchInfo.a = a;
                }
            });
        });
        saveData(matchData);
    });
}
var lastMatchData = [];
function saveData(matchData) {
    if (matchData) {
        lastMatchData = matchData.slice(1);
    }
    // console.log(lastMatchData)
    $.ajax({
        url: "http://127.0.0.1:8080/danchang/save",
        async: false,
        type: 'post',
        data: { data: JSON.stringify(lastMatchData) },
        success: function (d) {
            console.log(d);
        }
    });
}

var issueStr = "21064,21063,21062,21061,21055,21054,21053,21052,21051,21045,21044,21043,21042,21041,21035,21034,21033,21032,21031,21024,21023,21022,21021,21015,21014,21013,21012,21011,20126,20125,20124,20123,20122,20121,20115,20114,20113,20112,20111,20105,20104,20103,20102,20101,20094,20093,20092,20091,20085,20084,20083,20082,20081,20075,20074,20073,20072,20071,20065,20064,20063,20062,20061,20054,20053,20052,20051,20031,20014,20013,20012,20011,191205,191204,191203,191202,191201,191105,191104,191103,191102,191101,191004,191003,191002,191001,190905,190904,190903,190902,190901,190805,190804,190803,190802,190801,190705,190704,190703,190702,190701,190605,190604,190603,190602,190601,190505,190504,190503,190502,190501,190405,190404,190403,190402,190401,190305,190304,190303,190302,190301,190203,190202,190201,190104,190103,190102,190101,181205,181204,181203,181202,181201,181105,181104,181103,181102,181101,181005,181004,181003,181002,181001,180905,180904,180903,180902,180901,180805,180804,180803,180802,180801,180706,180705,180704,180703,180702,180701,180605,180604,180603,180602,180601,180506,180505,180504,180503,180502,180501,180404,180403,180402,180401,180304,180303,180302,180301,180204,180203,180202,180201,180105,180104,180103,180102,180101,171204,171203,171202,171201";
var issueArr = issueStr.split(",");
for (var i = 0; i < issueArr.length; i++) {
    var issue = issueArr[i];
    console.log(issue);
    loadData(issue);
}
     //
