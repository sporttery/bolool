/**
 * 竞彩计算常用工具
 */
var comp={"327":"利记","365":"金宝博","282":"皇冠","126":"BET365","285":"沙巴","13":"博天堂","83":"平博"};
var g_max_cell_count = 32;
var g_match_list_length = 0;
var g_match_list = {}, g_status = {}, g_timeout_flag = false, g_callbackcount = 0, g_zhibo_info = {};
var g_week = [ "周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日" ];
var g_had_name = {"胜":"h","平":"d","负":"a","h":"胜","d":"平","a":"负"};
var g_bet_fandian = 0.08;//佣金
var g_bet_money = 10000;

function formatNumber(num,digits) { 
	if(!digits){
		digits = 0;
	}
	num = Number(num).toFixed(digits);
	return num.replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, "$1,");  
} 
// 对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.format = function (fmt) { //author: meizz 
 var o = {
     "M+": this.getMonth() + 1, //月份 
     "d+": this.getDate(), //日 
     "h+": this.getHours(), //小时 
     "m+": this.getMinutes(), //分 
     "s+": this.getSeconds(), //秒 
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
     "S": this.getMilliseconds() //毫秒 
 };
 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;
}
function sort_by(name, minor) {
	return function(o, p) {
		var a, b;
		if (typeof o === "object" && typeof p === "object" && o && p) {
			a = o[name];
			b = p[name];
			if (a === b) {
				return typeof minor === 'function' ? minor(o, p) : 0;
			}
			if (typeof a === typeof b) {
				return a < b ? -1 : 1;
			}
			return typeof a < typeof b ? -1 : 1;
		}
	}
}


/**
 * 计算返还率，百分比，按参数列表计算
 */
function return_race() {
	var k = 0;
	for (var i = 0; i < arguments.length; i++) {
		k += 1 / Number(arguments[i]);
	}
	return Number(((1 / k) * 100).toFixed(2));
}
/**
 * 计算返还率，百分比,传数组计算
 */
function return_race_arr(arr) {
	var k = 0;
	for (var i = 0; i < arr.length; i++) {
		k += 1 / Number(arr[i]);
	}
	return Number(((1 / k) * 100).toFixed(2));
}
/**
 * 返还率赔率，按参数列表计算
 * 
 * @returns
 */
function return_race_num() {
	var k = 0;
	for (var i = 0; i < arguments.length; i++) {
		k += 1 / Number(arguments[i]);
	}
	return Number((1 / k).toFixed(2));
}
/**
 * 返还率赔率,传数组计算
 * 
 * @returns
 */
function return_race_num_arr(arr) {
	var k = 0;
	for (var i = 0; i < arr.length; i++) {
		k += 1 / Number(arr[i]);
	}
	return Number((1 / k).toFixed(2));
}


/* 选择返点后重新计算值博赔率 */
function setfd(obj) {
	var fd_value = $(obj).val();
	$(".minfd_control").each(function(idx) {
		$(this).val(fd_value);
	});
	g_bet_fandian = $($(".minfd_control")[0]).val();
	if(g_bet_fandian>1){
		g_bet_fandian = g_bet_fandian/100.0;
	}
	calcOdds();
	fillData(false);
}
/*
 * 算上返点后的赔率= 投注金额*赔率/(投注金额-佣金)
 */
function getOddsWithFd(val,fd_value) {
	if(!fd_value){
		fd_value = parseFloat($(".minfd_control:eq(0)").val());
	}
	if(isNaN(fd_value)){
		fd_value = 0;
	}
	if(fd_value>0.5){
		fd_value = fd_value * 0.01;
	}
	val = Number(val);
	/*var bet_money = 10000;
	var yj_value = bet_money * fd_value;
	val = Number(val);
	val = val * bet_money / (bet_money - yj_value);
	return Number(val.toFixed(3));*/
	return val+fd_value;
}
/* 计算值博赔率 */
function calcOdds(spArr) {
	var return_race = 0;
	if(!spArr){
		spArr = [];
		$(".selected").each(function() {
			var val = $(this).text();
			if (!isNaN(val)) {
				spArr.push(val);
			}
		});
	}
	for(var i=0;i<spArr.length;i++){
		val = Number(spArr[i]);
		val = getOddsWithFd(val)
		return_race += 1 / val;
	}
	var minOdds = 0;
	if (return_race > 0) {
		return_race = 1.0 / return_race;
		minOdds = getZhiBoOdds(return_race, 1);
		return_race = (return_race * 100).toFixed(2) + "%";
	}
	if(minOdds<0){
		minOdds = 0;
	}
	$(".selectedReturnRace").text(return_race);
	$(".selectedMinOdds").text(minOdds);
	return [return_race,minOdds];
}
/**
 * //Z=1; 最终返还率，可以设定为100%,最低96% 才能有盈利 //Q=1/(1/sp1+1/sp2),赔率 //sp3=Z*Q/(Q-Z),值博赔率
 * q 赔率
 * z 最终返还率
 */
function getZhiBoOdds(q, z) {
	var zhiBoOdds = Number((q * z / (q - z)).toFixed(3));
	return zhiBoOdds;
}
/**
 * 根据赔率和投注金额，计算出平手盘下盘最佳投注金额
 * 
 * @param Q
 *            投注金额
 * @param s1
 *            胜赔
 * @param s2
 *            平赔
 * @param s3
 *            负赔
 * @returns 平手盘下盘最佳投注金额
 */
function calc_pinshou_money(Q, s1, s2, s3) {
	if(Q<0){
		Q=10000000000.0;
	}
	return Q * s1 * s2 / (s2 * s3 + (s3 - 1) * s1 + s1 * s2);
}
/**
 * 根据投注金额和胜平赔，计算出平手盘保本最低赔率
 * 
 * @param Q
 *            投注金额
 * @param s1
 *            胜赔
 * @param s2
 *            平赔
 * @returns 平手盘保本最低赔率 (总金额/投注金额)
 */
function calc_pinshou_minodds(Q, s1, s2) {
	if(Q<0){
		Q=10000000000.0;
	}
	var t3 = (Q * s1 * s2 - Q * s1 - Q * s2) / (s1 * s2 - s1);
	return Q / t3;
}

/**
 * 排序，按指定的属性值排序
 * @param name
 * @param minor
 * @returns {Function}
 */
function by_str(name, minor) {
	return function(o, p) {
		var a, b;
		if (typeof o === "object" && typeof p === "object" && o && p) {
			a = o[name];
			b = p[name];
			if (a === b) {
				return typeof minor === 'function' ? minor(o, p) : 0;
			}
			if (typeof a === typeof b) {
				if(isNaN(a) && isNaN(b)){
					return a < b ? -1 : 1;
				}else{
					return Number(a) < Number(b) ? -1 : 1;
				}
			}
			return typeof a < typeof b ? -1 : 1;
		}
	}
}
/**
 * 将对像进行排序
 * @param obj 对象
 * @param name 排序属性名
 * @param minor 子排序
 * @param desc 排序方式 desc 降序 asc 
 * @returns 返回排序好的对象数组
 */
function obj_sort_by(obj,name,minor){
	var tmp = [];
	for(var k in obj){
		tmp.push(obj[k]);
	}
	tmp.sort(by_str(name,minor));
	return tmp;
}
function changelidqf(val) {
	if (val == 'q') {
		$(".cbk").each(function() {
			this.checked = true;
			changelid(this);
		});
	} else if (val == 'f') {
		$(".cbk").each(function() {
			this.checked = !this.checked;
			changelid(this);
		});
	}
}
// 选择指定赛事
function changelid(ckb) {
	if (ckb.checked) {
		$("." + ckb.value).show();
	} else {
		$("." + ckb.value).hide();
	}
}
/*
 * 清除选择
 */
function cleanSelected() {
	$('.selected').toggleClass('selected');
	$('.selectedReturnRace').text('0');
	$('.selectedMinOdds').text('0');
}
function toggle(sel, check) {
	if (check) {
		$(sel).show();
	} else {
		$(sel).hide();
	}
}
// 指定日期的开奖结果
function get_match_list_by_date(date) {
	// callback=callback_get_match_list_by_date
	$.getScript("http://i.sporttery.cn/open_v1_0/fb_match_list/get_fb_match_result/?username=11000000&password=test_passwd&format=jsonp&callback=callback_get_match_list_by_date&date=" + date);
}
function callback_get_his_half_draw_count(json) {
	g_callbackcount++;
	var result = json.result;
	var id = result[0]["m_id"];
	g_status["_" + id] = 1;
	var half_draw_count = 0;
	for (var i = 1; i < result.length; i++) {
		var match = result[i];
		var half = match["half"].split(":");
		if (half[0] == half[1]) {
			half_draw_count++;
		}
	}
	var half_draw_info = "--";
	if (result.length > 1) {
		half_draw_info = half_draw_count + "/" + (result.length - 1) + "=" + (half_draw_count * 100 / (result.length - 1)).toFixed(2);
		;
	}
	if (g_match_list["_" + id]) {
		g_match_list["_" + id]["half_draw_count"] = half_draw_info;
	}
}
/**
 * 在生成数据前处理数据
 * */
function get_his_half_draw_count_by_id(mid) {
	g_callbackcount++;
	// callback=callback_get_his_half_draw_count
	// $.getScript("http://i.sporttery.cn/api/fb_match_info/get_result_his/?f_callback=callback_get_his_half_draw_count&limit=10&mid="+mid);
}
// 加载受注赛程回调函数
function callback_get_selling_match_list(json) {
	g_match_list = json.data;
	g_status = {};
	g_callbackcount = 0;
	var week = g_week[new Date().getDay()];
	g_match_list_length = 0;
	for ( var k in g_match_list) {
		g_match_list[k]["l_bg_color"] = "#" + g_match_list[k]["l_background_color"];
		if(g_match_list[k]["hhad"]){
			g_match_list[k]["goalline"] = g_match_list[k]["hhad"]["fixedodds"];
		}else{
			g_match_list[k]["hhad"]=[];
			g_match_list[k]["hhad"]["p_status"]="N";
		}
		if(!g_match_list[k]["had"]){
			g_match_list[k]["had"]=[];
			g_match_list[k]["had"]["p_status"]="N";
		}
		if(!g_match_list[k]["crs"]){
			g_match_list[k]["crs"]=[];
			g_match_list[k]["crs"]["p_status"]="N";
		}
		if(!g_match_list[k]["hafu"]){
			g_match_list[k]["hafu"]=[];
			g_match_list[k]["hafu"]["p_status"]="N";
		}
		var id = g_match_list[k]["id"];
		// if(g_match_list[k]["num"].indexOf(week)!=-1){
		g_status["_" + id] = 0;
		// 统计历史半场平局
		get_his_half_draw_count_by_id(id);
		// }
		g_match_list_length++;
	}
	g_timeout_flag = setInterval(function() {
		if (g_match_list_length != g_callbackcount) {
			return;
		}
		clearInterval(g_timeout_flag);
		fillData(g_match_list);
	}, 500);
}
// 加载受注赛程
function get_selling_match_list() {
	// callback=callback_get_selling_match_list
	var url = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=callback_get_selling_match_list&poolcode[]=had&poolcode[]=hhad&poolcode[]=hafu&poolcode[]=ttg&poolcode[]=crs";
	$.getScript(url);
}
// 刷新
function dorefresh() {
	var date = $("#date").val();
	var url = location.href.replace("//","");
	var begin = url.indexOf("/");
	var end = url.indexOf(".html");
	if(end!=-1){
		url = url.substring(begin,end)+".html";
	}else{
		url = "/jc.html";
	}
	if (date && date != "") {
		location.href = url+"?date=" + date + "&minodds=" + $("#minodds").val() + "&maxodds=" + $("#maxodds").val();
	} else {
		location.href = url;
	}
}
// 加载开奖和赔率的数据回调函数
function callback_get_result_odds_by_id(json) {
	var match = json.data;
	var key = "_" + match["id"];
	g_match_list[key]["had"] = match["had"];
	g_match_list[key]["hhad"] = match["hhad"];
	g_match_list[key]["hhad"]["p_status"] = "Y";
	g_match_list[key]["hafu"] = match["hafu"];
	g_match_list[key]["hafu"]["p_status"] = "Y";
	g_match_list[key]["crs"] = match["crs"];
	g_match_list[key]["crs"]["p_status"] = "Y";
	g_match_list[key]["ttg"] = match["ttg"];
	g_match_list[key]["ttg"]["p_status"] = "Y";
	g_match_list[key]["goalline"] = match["goalline"];
	if (match["final"]) {
		match["full"] = match["final"];
		g_match_list[key]["full"] = match["final"];
	}
	if (match["hafu_result"] == "" && match["full"] != "" && match["full"] != "undefined" && match["full"] != null) {
		var full = match["full"].split(":");
		var half = match["half"].split(":");
		if (half[0] == half[1]) {
			match["hafu_result"] = "平";
		} else if (half[0] > half[1]) {
			match["hafu_result"] = "胜";
		} else {
			match["hafu_result"] = "负";
		}
		var goalline = parseInt(match["goalline"]);
		full[0] = parseInt(full[0]);
		full[1] = parseInt(full[1]);
		if (full[0] + goalline < full[1]) {
			match["hhad_result"] = "负";
		} else if (full[0] + goalline > full[1]) {
			match["hhad_result"] = "胜";
		} else {
			match["hhad_result"] = "平";
		}
		if (full[0] == full[1]) {
			match["hafu_result"] = match["hafu_result"] + "平";
			match["had_result"] = "平";
		} else if (full[0] > full[1]) {
			match["hafu_result"] = match["hafu_result"] + "胜";
			match["had_result"] = "胜";
		} else {
			match["hafu_result"] = match["hafu_result"] + "负";
			match["had_result"] = "负";
		}
	}
	g_match_list[key]["had_result"] = match["had_result"];
	g_match_list[key]["hhad_result"] = match["hhad_result"];
	g_match_list[key]["ttg_result"] = match["ttg_result"];
	g_match_list[key]["hafu_result"] = match["hafu_result"];
	g_match_list[key]["crs_result"] = match["crs_result"];
	g_match_list[key]["l_bg_color"] = "";
	g_status[key] = 1;
	g_callbackcount++;
}
// 指定日期的赛程列表回调函数
function callback_get_match_list_by_date(json) {
	var match_list = json["data"]["result"];
	var match_list_length = 0;
	g_callbackcount = 0;
	for ( var k in match_list) {
		var id = match_list[k]["id"];
		match_list_length++;
		var key = "_" + id;
		g_match_list[key] = match_list[k];
		g_status[key] = 0;
		// callback=callback_get_result_odds_by_id
		var url = "http://i.sporttery.cn/open_v1_0/fb_match_list/get_fb_result_odds/?username=11000000&password=test_passwd&format=jsonp&callback=callback_get_result_odds_by_id&m_id=" + id;
		$.getScript(url);
	}
	g_timeout_flag = setInterval(function() {
		if (g_callbackcount != match_list_length) {
			return;
		}
		clearInterval(g_timeout_flag);
		g_timeout_flag = false;
		g_status = {};
		g_callbackcount = 0;
		match_list_length = 0;
		for ( var k in g_match_list) {
			var id = g_match_list[k]["id"];
			match_list_length++;
			g_status["_" + id] = 0;
			get_his_half_draw_count_by_id(id);
		}
		g_timeout_flag = setInterval(function() {
			if (match_list_length != g_callbackcount) {
				return;
			}
			clearInterval(g_timeout_flag);
			g_timeout_flag = false;
			fillData(g_match_list);
			initCheckBox();
		}, 500);
	}, 500);
}
function initCheckBox() {
	$("#zjq_control").length > 0 && toggle(".zjq_td", $("#zjq_control")[0].checked);
	$("#fhl_control").length > 0 && toggle(".fhl_td", $("#fhl_control")[0].checked);
	$("#tz_control").length > 0 && toggle(".tz_td", $("#tz_control")[0].checked);
	$("#bqc_control").length > 0 && toggle(".bqc_td", $("#bqc_control")[0].checked);
	$("#tz_control").length > 0 && toggle("#tj1", $("#tz_control")[0].checked);
	$("#tj_control").length > 0 && toggle("#tj_td", $("#tj_control")[0].checked);
}
function showdate(date) {
	$("#matchlist").html("<tr><td colspan=" + g_max_cell_count + " style=color:red>加载中。。。</td></tr>");
	g_match_list = {}, g_status = {};
	get_match_list_by_date(date);
}
function fillData(match_list) {
	$("body").html('<h1>请自己实现fillData方法体</h1>');
}
function fillsp() {
	val = $("#sellMatchList").val();
	if (val && val != "0") {
		match = g_match_list[val];
		if(match["had"] &&match["had"]["p_status"]!="N"){
			$("#sp").val(match["had"]["h"] + " " + match["had"]["d"] + " " + match["had"]["a"]);
		}else{
			$("#sp").val("");
		}
		if(match["hhad"]  &&match["hhad"]["p_status"]!="N" ){
			$("#sp1").val(match["hhad"]["h"] + " " + match["hhad"]["d"] + " " + match["hhad"]["a"]);
		}else{
			$("#sp1").val("");
		}
		document.title=match["matchinfo"];
		currMatch=match;
		getsp();
	}else{
		$("#sp").val("");
		$("#sp1").val("");
	}
}


var mp3_1="/res/4931-niaojiao.mp3";
var mp3_2="/res/msg.mp3";
var mp3_3="/res/4506.mp3";
var flashvars = {
};
var params = {
wmode: "transparent"
};
var attributes = {};
if(typeof swfobject == "object"){
	swfobject.embedSWF("/res/sound.swf", "sound", "1", "1", "9.0.0", "/res/expressInstall.swf", flashvars, params, attributes);
}

function play(c) {
	if(typeof swfobject == "object"){
		var sound = swfobject.getObjectById("sound");
		if (sound) {
		sound.SetVariable("f", c);
		sound.GotoFrame(1);
		}
	}else{
		console.log("swfobject is error");
	}
}
function playniaojao(){
	play(mp3_1);
}
function playmsg(){
	play(mp3_2);
}
function playMax(){
	play(mp3_3);
}