g_max_cell_count = 11;
	var g_timeout_refresh;
	
	///var url_match_list = "/ews/jczq/data/dggp_nspf_spf?page=1&size=200";
	var url_match_list = "/dggp.json";
//	var url_match_list = "/ews/jczq/data/dggp_nspf_spf";
	//var cids="1055,3,348,280";
	var cids="1055";
	var url_europe1="";//"/fenxi/ouzhi-#ID#.shtml?cids="+cids;
	var url_europe="";//"/europe_jczq.shtml";
	var url_europe2="/fenxi1/json/ouzhi.php?fid=#ID#&cid=#CID#&type=europe&t=";
	var asia_europe = "";
	//var asia_europe="/fenxi1/inc/ajax.php?t=yazhi&p=1&r=1&fixtureid=#ID#&companyid=#CID#&updatetime=";
	var comp = {};
	// comp["2"]="立博";
	// comp["3"]="Bet365";
	// comp["5"]="澳门";
	// comp["6"]="伟德";
	// comp["8"]="snai";
	// comp["9"]="易胜博";
	// comp["280"]="皇冠";
	// comp["293"]="威廉希尔";
	// comp["348"]="金宝博";
	// comp["291"]="优胜客";
	comp["1055"]="平博";
	var fanshui={};
	fanshui["280"]=0.02;
	let pan = {len:0};
	var g_match_sort_arr=[];
	var sortBy="单关盈利";
	var g_match_europe_length=0;
	var g_match_asia_length=0;
	var scr="</scr";
	var ipt = "ipt";

	let get_selling_match_list = function(){
		$("#msg")[0].innerHTML="<h1 style='color:red'>数据加载中...</h1>";
		g_match_list = {};
		g_match_list_length = 0;
		g_match_europe_length=0;
		g_match_asia_length=0;
		$.getJSON(url_match_list+"?"+(+new Date),function(json){
			var list = json.data;
			for(var i= 0;i<list.length;i++){
				var match = list[i];
				match = convertToMatch(match);
				if(Math.abs(match["rangqiu"])!=1){
					continue;
				}
				if(((match["had"] && match["had"]["win"])||(match["hhad"] && match["hhad"]["win"])) && !g_match_list["_"+match["fixtureid"]]){
					g_match_list["_"+match["fixtureid"]]=match;
					g_match_list_length++;
				}
			}
			if(url_europe!=""){
				getEurope();
			}else if(url_europe2!=""){
				getEurope2();
			}else if(url_europe1!=""){
				getEurope1();
			}
			getAsia();
			fillData();
		})
	}
	function convertToMatch(match){
		match["iseasy"]=match["mdata"]["iseasy"];
		match["infoid"]=match["mdata"]["fixtureid"];
		match["id"]=match["mdata"]["fixtureid"];
		match["fixtureid"]=match["mdata"]["fixtureid"];
		match["matchnum"]=match["mdata"]["matchnum"];
		match["simpleleague"]=match["mdata"]["simpleleague"];
		match["homesxname"]=match["mdata"]["homesxname"];
		match["homestanding"]=match["mdata"]["homestanding"];
		match["awaysxname"]=match["mdata"]["awaysxname"];
		match["awaystanding"]=match["mdata"]["awaystanding"];
		match["rangqiu"]=match["mdata"]["rangqiu"];
		match["goalline"]=match["mdata"]["rangqiu"];
		match["homename"]=match["mdata"]["homesxname"];
		match["awayname"]=match["mdata"]["awaysxname"];
		match["matchdate"]=match["mdata"]["matchdate"];
		match["matchtime"]=match["mdata"]["endtime"];
		match["homeid"]=match["mdata"]["homeid"];
		match["awayid"]=match["mdata"]["awayid"];
		match["processname"]=match["mdata"]["processname"];
		match["processdate"]=match["mdata"]["matchdate"];
		match["ptggendtime"]=match["mdata"]["matchtime"];
		if(match["pdata"]["nspf_pl"] &&match["pdata"]["nspf_pl"]["win"]){
			match["had"]={"win":match["pdata"]["nspf_pl"]["win"],"draw":match["pdata"]["nspf_pl"]["draw"],"lost":match["pdata"]["nspf_pl"]["lost"]}
		}
		if(match["pdata"]["spf_pl"] &&match["pdata"]["spf_pl"]["win"]){
			match["hhad"]={"win":match["pdata"]["spf_pl"]["win"],"draw":match["pdata"]["spf_pl"]["draw"],"lost":match["pdata"]["spf_pl"]["lost"]}
		}
		match["seasonid"]=match["mdata"]["simpleleague"];;
		return match;
	}
	function getAsia(find_first_company){
		if(asia_europe.length>0){
			let update_time = (new Date()).format("yyyy-MM-dd hh:mm:ss");
			let cid = cids.split(",");
			$.ajaxSettings.async = false;
			for(let key in g_match_list){
				let match = g_match_list[key];
				if(Math.abs(parseInt(match.goalline))>1){
					continue;
				}
				for(let i = 0;i<cid.length;i++){
					let do_break = false;
					if(cid[i]!=1){
						if(!match["asia"]){
							match["asia"]=[];
						}
						url_asia = asia_europe.replace("#ID#",match["id"]).replace("#CID#",cid[i])+update_time;
						$.getJSON(url_asia,function(json){
							for(let k=0;k<json.length;k++){
								let odds = {};
								odds["cn"]=comp[cid[i]];
								odds["win"]=parseFloat(json[k][0]);
								odds["pan"]=json[k][1];
								odds["lost"]=parseFloat(json[k][2]);
								if(pan["len"]==0 || pan[odds["pan"]]){
									match["asia"].push(odds);
									if(find_first_company){
										do_break = true;
									}
								}
							}
						});
					}
					if(do_break){
						break;
					}
				}
				g_match_asia_length++;
			}
			$.ajaxSettings.async = true;
		}else{
			g_match_asia_length=g_match_list_length;
		}
	}
	function getEurope(){
		for(var key in g_match_list){
			g_match_list[key]["europe"]=[];
		}
		$.get(url_europe+"?"+(+new Date),function(txt){
			var index = txt.indexOf("var ouzhiList=");
			if(index!=-1){
				txt = txt.substring(index);
				index = txt.indexOf(scr+ipt);
				txt = txt.substring(0,index);
				eval(txt);
			}
			if(typeof ouzhiList=="object"){
				for(var k in g_match_list){
					var match = g_match_list[k];
					var id = match["fixtureid"];
					match["id"]=id;
					if(!match["europe"]){
						match["europe"] = [];
					}
					if(ouzhiList[id]){
						var ouzhi = ouzhiList[id];
						for(var k in ouzhi){
							if(comp[k]){
								var odds = {};
								odds["cn"]=comp[k];
								odds["win"]=ouzhi[k][0][0];
								odds["draw"]=ouzhi[k][0][1];
								odds["lost"]=ouzhi[k][0][2];
								var fd_value=fanshui[k];
								if(fd_value && fd_value > 0){
									odds["win"]=getOddsWithFd(odds["win"], fd_value);
									odds["draw"]=getOddsWithFd(odds["draw"], fd_value);
									odds["lost"]=getOddsWithFd(odds["lost"], fd_value);
								}
								match["europe"].push(odds);
							}
						}
					}
					g_match_europe_length++;
				}
			}
		});
	}
	function getEurope1(){
		for(var k in g_match_list){
			var match = g_match_list[k];
			var id = match["fixtureid"];
			match["id"]=id;
			//console.log(url_europe.replace("#ID#",id));
			$.get(url_europe1.replace("#ID#",id),function(txt){
				var startidx = txt.indexOf("<div class=\"table_cont\" id=\"table_cont\">");
				txt = txt.substring(startidx);
				var endidx = txt.indexOf("<form name=\"plform\"");
				txt = txt.substring(0,endidx);
				var ididx = txt.indexOf("fixtureid=");
				var fixtureid = txt.substring(ididx).replace("fixtureid=","").substring(0,6);
				var europe = [];
				txt=txt.replace(/\/images\//g,"/res/images/");
				$(txt).find("tr").each(function(idx){
					var odds = {};
					odds["id"]=$(this).attr("id");
					if(odds["id"]){
						odds["cn"] = $($(this).find("td:eq(1)")[0]).attr("title").substring(0,3)+" ";
						var oddstd =$($($(this).find("td:eq(2)")[0]).find("tr:eq(1)")[0]).find("td");
						odds["win"]=$(oddstd[0]).text();
						odds["draw"]=$(oddstd[1]).text();
						odds["lost"]=$(oddstd[2]).text();
						europe.push(odds);
					}
				});
				g_match_list["_"+fixtureid]["europe"]=europe;
				g_match_europe_length++;
			});
		}
	}
	function getEurope2(){
		var cid = cids.split(",");
		$.ajaxSettings.async = false;
		for(var key in g_match_list){
			var match = g_match_list[key];
			g_match_list[key]["europe"]=[];
			for(var i = 0;i<cid.length;i++){
				if(cid[i]!=1){
					url = url_europe2.replace("#ID#",match["id"]).replace("#CID#",cid[i])+(new Date()).format("yyyy-MM-dd hh:mm:ss");
					$.getJSON(url,function(json){
						var odds = {};
						odds["cn"]=comp[cid[i]];
						odds["win"]=parseFloat(json[0][0]);
						odds["draw"]=json[0][1];
						odds["lost"]=parseFloat(json[0][2]);
						g_match_list[key]["europe"].push(odds);
					});
				}
			}
			g_match_europe_length++;
		}
		$.ajaxSettings.async = true;
	}
	function getHighestOdds(){
		for(var k in g_match_list){
			var match = g_match_list[k];
			var europe = match["europe"];
			var asia = match["asia"];
			var h,d,a,h_h,h_d,h_a,had_return_race=0,hhad_return_race=0,max_return_race=0;
			h=false,h_h = false;
			if(match["had"]&& match["had"]["win"]){
				h=match["had"]["win"],d=match["had"]["draw"],a=match["had"]["lost"];
				had_return_race = return_race(h,d,a);
			}
			if(match["hhad"] && match["hhad"]["win"]){
				h_h=match["hhad"]["win"],h_d=match["hhad"]["draw"],h_a=match["hhad"]["lost"];
				hhad_return_race = return_race(h_h,h_d,h_a);
			}
			max_return_race = had_return_race > hhad_return_race?had_return_race:hhad_return_race;
			var goalline = parseInt(match["rangqiu"]);
			var rc = 0;
			if(h && h_h){
				if(goalline==1){
					rc = return_race(h_h,a);;
				}else if(goalline==-1){
					rc = return_race(h,h_a);;
				}
				if(rc > max_return_race){
					max_return_race = rc;
				}
			}
			match["had_return_race"]=had_return_race;
			match["hhad_return_race"]=hhad_return_race;
			match["max_return_race"]=max_return_race;
			match["rc"]=rc;
			var bet_arr=[];
			if(europe && europe.length>0){
				highest=[];
				//胜赔
				var arr_tmp_h=[];
				var arr_tmp_d=[];
				var arr_tmp_a=[];
				for(var i=0;i<europe.length;i++){
					var eur = europe[i];
					arr_tmp_h.push({"名":eur.cn,"赔率":eur.win});
					arr_tmp_d.push({"名":eur.cn,"赔率":eur.draw});
					arr_tmp_a.push({"名":eur.cn,"赔率":eur.lost});
				}
				arr_tmp_h.sort(by_str("赔率"));
				arr_tmp_d.sort(by_str("赔率"));
				arr_tmp_a.sort(by_str("赔率"));
				highest.push(arr_tmp_h[arr_tmp_h.length-1]);
				highest.push(arr_tmp_d[arr_tmp_d.length-1]);
				highest.push(arr_tmp_a[arr_tmp_a.length-1]);
				var highest_odds={"h":Number(highest[0]["赔率"].toFixed(2)),"d":Number(highest[1]["赔率"].toFixed(2)),"a":Number(highest[2]["赔率"].toFixed(2))};
				var highest_cn={"h":highest[0]["名"],"d":highest[1]["名"],"a":highest[2]["名"]};
				if(h){
					bet_arr.push({"j":[h,d,0],"博彩公司":["竞胜","竞平",highest_cn.a+"负"],"名":"竞hd["+h+","+d+"]-"+highest_cn.a+"a["+highest_odds.a+"]","赔率":[h,d,highest_odds.a],"jc赔率":return_race_num(h,d),"外围赔率":highest_odds.a});
					bet_arr.push({"j":[h,0,a],"博彩公司":["竞胜",highest_cn.d+"平","竞负"],"名":"竞ha["+h+","+a+"]-"+highest_cn.d+"d["+highest_odds.d+"]","赔率":[h,highest_odds.d,a],"jc赔率":return_race_num(h,a),"外围赔率":highest_odds.d});
					bet_arr.push({"j":[0,d,a],"博彩公司":[highest_cn.h+"胜","竞平","竞负"],"名":"竞da["+d+","+a+"]-"+highest_cn.h+"h["+highest_odds.h+"]","赔率":[highest_odds.h,d,a],"jc赔率":return_race_num(a,d),"外围赔率":highest_odds.h});
					bet_arr.push({"j":[h,0,0],"博彩公司":["竞胜",highest_cn.d+"平",highest_cn.a+"负"],"名":"竞h["+h+"]-"+highest_cn.d+"d["+highest_odds.d+"]-"+highest_cn.a+"a["+highest_odds.a+"]","赔率":[h,highest_odds.d,highest_odds.a],"jc赔率":h,"外围赔率":return_race_num(highest_odds.a,highest_odds.d)});
					bet_arr.push({"j":[0,0,a],"博彩公司":[highest_cn.h+"胜",highest_cn.d+"平",highest_cn.a+"竞负"],"名":"竞a["+a+"]-"+highest_cn.d+"d["+highest_odds.d+"]-"+highest_cn.h+"h["+highest_odds.h+"]","赔率":[highest_odds.h,highest_odds.d,a],"jc赔率":a,"外围赔率":return_race_num(highest_odds.h,highest_odds.d)});
					bet_arr.push({"j":[0,d,0],"博彩公司":[highest_cn.h+"胜","竞平",highest_cn.a+"负"],"名":"竞d["+d+"]-"+highest_cn.a+"a["+highest_odds.a+"]-"+highest_cn.h+"h["+highest_odds.h+"]","赔率":[highest_odds.h,d,highest_odds.a],"jc赔率":d,"外围赔率":return_race_num(highest_odds.a,highest_odds.h)});
				}
				if(h_h){
					if(goalline==-1){
						bet_arr.push({"j":[0,h_a],"博彩公司":[highest_cn.h+"胜","竞让负"],"名":"竞让a["+h_a+"]-"+highest_cn.h+"h["+highest_odds.h+"]","赔率":[highest_odds.h,h_a],"jc赔率":h_a,"外围赔率":highest_odds.h});
					}else if(goalline==1){
						bet_arr.push({"j":[h_h,0],"博彩公司":["竞让胜",highest_cn.a+"负"],"名":"竞让h["+h_h+"]-"+highest_cn.a+"a["+highest_odds.a+"]","赔率":[h_h,highest_odds.a],"jc赔率":h_h,"外围赔率":highest_odds.a});
					}
				}
				match["highest"]=highest;
				match["highest_cn"]=highest_cn;
				match["highest_odds"]=highest_odds;
			}
			if(asia && asia.length > 0){//只取0.5盘口
				highest=[],highest_odds=[],highest_cn=[];
				//胜赔
				arr_tmp_h=[],arr_tmp_a=[];
				for(var i=0;i<asia.length;i++){
					var as = asia[i];
					if((goalline==-1 && as["pan"]=="半球") || (goalline==1 && as[pan]=="受半球")){
						arr_tmp_h.push({"名":"[亚]"+as.cn,"赔率":as.win});
						arr_tmp_a.push({"名":"[亚]"+as.cn,"赔率":as.lost});
					}
				}
				if(arr_tmp_h.length>0){
					arr_tmp_h.sort(by_str("赔率"));
					arr_tmp_a.sort(by_str("赔率"));
					highest.push(arr_tmp_h[arr_tmp_h.length-1]);
					highest.push(arr_tmp_a[arr_tmp_a.length-1]);
				}
				if(highest.length>0){
					highest_odds={"h":highest[0]["赔率"],"a":highest[1]["赔率"]};
					highest_cn={"h":highest[0]["名"],"a":highest[1]["名"]};
					if(h){
						if(goalline==1){
							bet_arr.push({"j":[-1,a],"博彩公司":[highest_cn.h+"上盘","竞负"],"名":"竞a["+a+"]-"+highest_cn.h+" 上盘["+highest_odds.h+"]","赔率":[highest_odds.h,a],"jc赔率":a,"外围赔率":highest_odds.h});
						}else if(goalline==-1){
							bet_arr.push({"j":[h,-1],"博彩公司":["竞胜",highest_cn.a+"下盘"],"名":"竞h["+h+"]-"+highest_cn.a+" 下盘["+highest_odds.a+"]","赔率":[h,highest_odds.a],"jc赔率":h,"外围赔率":highest_odds.a});
						}
					}
					if(h_h){
						if(goalline==1){
							bet_arr.push({"j":[h_h,0],"博彩公司":["竞胜",highest_cn.a+"下盘"],"名":"竞让h["+h_h+"]-"+highest_cn.a+" 下盘["+highest_odds.a+"]","赔率":[h_h,highest_odds.a],"jc赔率":h_h,"外围赔率":highest_odds.a});
						}else if(goalline==-1){
							bet_arr.push({"j":[0,h_a],"博彩公司":[highest_cn.h+"上盘","竞让负"],"名":"竞让a["+h_a+"]-"+highest_cn.h+" 上盘["+highest_odds.h+"]","赔率":[highest_odds.h,h_a],"jc赔率":h_a,"外围赔率":highest_odds.h});
						}
					}
				}
				match["highest_asia"]=highest;
				match["highest_cn_asia"]=highest_cn;
				match["highest_odds_asia"]=highest_odds;
				match["竞彩返还率"]=max_return_race;
			}
			for(var i=0;i<bet_arr.length;i++){
				bet_arr[i]["返还率"]=return_race_arr(bet_arr[i]["赔率"]);
				bet_arr[i]["返还率1"]=return_race(bet_arr[i]["jc赔率"],bet_arr[i]["外围赔率"]);
				var bet_j = bet_arr[i]["j"];
				var bet_money_arr=[];
				var jc_bet_money = 0;
				for(var n = 0;n < bet_j.length; n++){
					var curr_money = parseInt(g_bet_money*bet_arr[i]["返还率"]/100.0/bet_arr[i]["赔率"][n]);
					if(curr_money%2==1){
						curr_money +=1;
					}
					if(bet_j[n]>0){
						jc_bet_money += curr_money;
					}
					bet_money_arr.push(curr_money);
				}
				bet_arr[i]["m"]= bet_money_arr;
				bet_arr[i]["单关盈利"] = parseFloat((g_bet_money * bet_arr[i]["返还率"]/100.0 + jc_bet_money * g_bet_fandian - g_bet_money).toFixed(3)) ;
				bet_arr[i]["竞彩投"]=jc_bet_money;
				bet_arr[i]["佣金"]=parseFloat(jc_bet_money * g_bet_fandian.toFixed(2));
			}
 			bet_arr.sort(by_str(sortBy));
 			bet_arr.reverse();
			match["bet_arr"]=bet_arr;
		}
	}
	
	function loginfo(id){
		console.log(g_match_list["_"+id]);
	}
	function showdetail(id,tdobj){
		var match = g_match_list["_"+id];
		var bet_arr = match["bet_arr"]||[];
		bet_arr.reverse();
		var myModal = $("#myModal");
		myModal.find("#myModalLabel").text(match["matchnum"]+" "+match["homename"]+"("+match["rangqiu"]+") VS "+match["awayname"]);
		var html=[];
		for(var i = 0;i<bet_arr.length;i++){
			var bet_info = bet_arr[i];
			html.push("<tr>");
			html.push("<td rowspan=\"2\">"+(i+1)+"</td>");
			html.push("<td rowspan=\"2\">"+bet_info["名"]+"</td>");
			var jc_flag_arr = bet_info["j"];
			if(bet_info["赔率"].length==3){
				for(var m = 0;m < 3;m++){
					if(jc_flag_arr[m]>0){
						html.push("<td class=\"selected1\">"+bet_info["赔率"][m]+"</td>");
					}else{
						html.push("<td>"+bet_info["赔率"][m]+"</td>");
					}
				}
			}else if(bet_info["赔率"].length==2){
				if(jc_flag_arr[0]==0||jc_flag_arr[1]==0){
					html.push("<td"+(jc_flag_arr[0]>0?" class=\"selected1\"":"")+">"+bet_info["赔率"][0]+"</td>");
					html.push("<td rowspan=\"2\">让("+match["rangqiu"]+")</td>");
					html.push("<td"+(jc_flag_arr[1]>0?" class=\"selected1\"":"")+">"+bet_info["赔率"][1]+"</td>");
				}else{
					html.push("<td"+(jc_flag_arr[0]>0?" class=\"selected1\"":"")+">"+bet_info["赔率"][0]+"</td>");
					html.push("<td rowspan=\"2\">--</td>");
					html.push("<td"+(jc_flag_arr[1]>0?" class=\"selected1\"":"")+">"+bet_info["赔率"][1]+"</td>");
				}
			}
			html.push("<td rowspan=\"2\">"+bet_info["返还率"]+"</td>");
			html.push("<td rowspan=\"2\">"+bet_info["jc赔率"]+"</td>");
			html.push("<td rowspan=\"2\">"+bet_info["外围赔率"]+"</td>");
			html.push("<td rowspan=\"2\">"+bet_info["返还率1"]+"</td>");
			if(bet_info["单关盈利"]>0){
				html.push("<td rowspan=\"2\" bgcolor=red>"+bet_info["单关盈利"]+"</td>");
			}else{
				html.push("<td rowspan=\"2\">"+bet_info["单关盈利"]+"</td>");
			}
			html.push("<td rowspan=\"2\">"+bet_info["竞彩投"]+"</td>");
			html.push("</tr>");
			
			html.push("<tr>");
			for(var m = 0;m<bet_info["m"].length;m++){
				html.push("<td style='color:#fff;'>"+bet_info["m"][m]+"</td>");
			}
			html.push("</tr>");
		}
		if(tdobj){
			return "<table width=100% border=0 >"+html[0]+"</table>";
		}else{
			myModal.find("#matchbetarr").html(html.join(""));
			myModal.removeClass("hide");
			myModal.modal("show");
		}
	}
	function showhidematchinfo(){
		var obj = $("#matchinfo_control");
		var text = obj.text();
		if(text.indexOf("显示")!=-1){
			obj.text(text.replace("显示","隐藏"));
			$(".matchinfo").show();
		}else{
			obj.text(text.replace("隐藏","显示"));
			$(".matchinfo").hide();
		}
	}
	function onlyPinbet(){
		var obj = $("#pinbetonly")[0];
		comp={};
		if(!obj.checked){
			comp["3"]="Bet365";
			//comp["5"]="澳门";
			//comp["6"]="伟德";
			//comp["8"]="snai";
			//comp["9"]="易胜博";
			comp["280"]="皇冠";
			//comp["293"]="威廉希尔";
			comp["348"]="金宝博";
			//comp["291"]="优胜客";
		}
		cids="1055";
		comp["1055"]="平博";
		getEurope();
		getAsia();
		setTimeout("fillData()",1000);
	}
	function setMoreodds(){
		var obj = $("#moreodds")[0];
		comp={};
		if(obj.checked){
//			comp["5"]="澳门";
//			comp["6"]="伟德";
//			comp["8"]="snai";
//			comp["9"]="易胜博";
//			comp["293"]="威廉希尔";
			comp["348"]="金宝博";
//			comp["291"]="优胜客";
			cids="1055,3,348,280";
		}
		comp["3"]="Bet365";
		comp["280"]="皇冠";
		comp["1055"]="平博";
		getEurope();
		getAsia();
		setTimeout("fillData()",1000);
	}
	var waiting="";
	function fillData(match_list){
		if(!match_list){
			match_list = g_match_list;
		}
		if(g_match_list_length==0 || g_match_list_length>g_match_asia_length || g_match_list_length>g_match_europe_length){
			waiting += "..";
			$("#msg")[0].innerHTML="<h1 style='color:red'>数据加载中[match_list="+g_match_list_length+",match_europe="+g_match_europe_length+",match_asia="+g_match_asia_length+"]"+waiting+"</h1>";
			setTimeout("fillData()",500);
			return;
		}
		waiting="";
		$("#msg").html(waiting);
		var obj = $("#matchinfo_control");
		obj.text("显示比赛数据");
		$(".matchinfo").hide();
		getHighestOdds();
		var l_cn_arr=[];
		g_match_sort_arr=[];
		var fd = Number($(".minfd_control").val());
		if(fd>1){
			fd = fd/100.0;
		}
		for(var key in match_list){
			var match = match_list[key];
			var bet_arr = match["bet_arr"];
			bet_arr.sort(by_str(sortBy));
			n=bet_arr.length-1;
			var match1 = {};
			for(var subkey in match){//克隆一个
				match1[subkey]=match[subkey];
			}
			match1["bet_arr"]=[bet_arr[n]];
			match1["返还率"]=bet_arr[n]["返还率"];
			match1["返还率1"]=bet_arr[n]["返还率1"];
			match1["jc赔率"]=bet_arr[n]["jc赔率"];
			match1["单关盈利"]=bet_arr[n]["单关盈利"];
			match1["竞彩投"]=bet_arr[n]["竞彩投"];
			g_match_sort_arr.push(match1);
		}
		g_match_sort_arr.sort(by_str(sortBy));
		g_match_sort_arr.reverse();		
		match_list = g_match_sort_arr;
		var html = [];
		var i = 0,idx=0;
		var had_key=["h","d","a"];
		var year = new Date().getFullYear()+"-";
		for(var key in match_list){
			var match = match_list[key];
			var bet_info_arr = match["bet_arr"];
			var had_return_race = match["had_return_race"];
			var hhad_return_race = match["hhad_return_race"];
			var goalline = parseInt(match["rangqiu"]);
			var max_return_race = match["max_return_race"];
			var rc = match["rc"];
			
			idx++;
			var n = 0 ;
			var bet_info = bet_info_arr[n];
			if(!l_cn_arr["lid_"+match["seasonid"]]){
				l_cn_arr["lid_"+match["seasonid"]]=match["simpleleague"];
			}
			if(bet_info["单关盈利"]>0){
				html.push("<tr class='lid_"+match["seasonid"]+"'  bgcolor=red>");
				playMax();
			}else{
				html.push("<tr class='lid_"+match["seasonid"]+"'>");
				playmsg();
			}
			html.push("<td rowspan=2>"+idx+"</td>");
			html.push("<td rowspan=2 onclick=\"loginfo("+match["id"]+")\">"+match["matchnum"]+"</td>");
			html.push("<td rowspan=2>"+bet_info["单关盈利"]+"</td>");
			html.push("<td rowspan=2 class=\"matchinfo\">"+match["simpleleague"]+"</td>");
			html.push("<td rowspan=2 class=\"matchinfo\"><a href=\"http://liansai.500.com/team/"+match["homeid"]+"/\" target=_blank>"+match["homename"]+"</a>("+match["goalline"]+") VS <a href=\"http://liansai.500.com/team/"+match["awayid"]+"/\" target=_blank>"+match["awayname"]+"</a></td>");
			html.push("<td rowspan=2 class=\"matchinfo\">"+match["matchtime"].replace(year,"")+"</td>");
			if(match["had"] && match["had"]["win"]){
				html.push("<td class=\"matchinfo\">0</td>");
				html.push("<td class=\"odds matchinfo\">"+match["had"]["win"]+"</td>");
				html.push("<td class=\"odds matchinfo\">"+match["had"]["draw"]+"</td>");
				html.push("<td class=\"odds matchinfo\">"+match["had"]["lost"]+"</td>");
			}else{
				html.push("<td class=\"matchinfo\">--</td>");
				html.push("<td class=\"matchinfo\">--</td>");
				html.push("<td class=\"matchinfo\">--</td>");
				html.push("<td class=\"matchinfo\">--</td>");
			}
			html.push("<td class=\"matchinfo\" rowspan=2 title=\"胜平负返还率:"+had_return_race+",让球胜平负返还率:"+hhad_return_race+",混投返还率:"+rc+"\">"+max_return_race+"</td>");
			
			html.push("<td rowspan=2>"+bet_info["竞彩投"]+"</td>");
			html.push("<td rowspan=2>"+(Number(bet_info["竞彩投"])*fd).toFixed(2)+"</td>");
			for(var m = 0;m<bet_info["赔率"].length;m++){
				html.push("<td>"+bet_info["博彩公司"][m]+":"+bet_info["赔率"][m]+"</td>");
			}
			if(bet_info["赔率"].length==2){
				html.push("<td rowspan=2>--</td>");
			}
			html.push("<td rowspan=2>"+bet_info["返还率"]+"</td>");
//			html.push("<td rowspan=2 class=\"odds\">"+bet_info["jc赔率"]+"</td>");
//			html.push("<td rowspan=2 class=\"odds\">"+bet_info["外围赔率"]+"</td>");
//			html.push("<td rowspan=2>"+bet_info["返还率1"]+"</td>");
			html.push("<td rowspan=2><a href=http://odds.500.com/fenxi/shuju-"+match["id"]+".shtml target=\"_blank\">析</a>&nbsp;");
			html.push("<a href=http://odds.500.com/fenxi/yazhi-"+match["id"]+".shtml?cids="+cids+" target=\"_blank\">亚</a>&nbsp;");
			html.push("<a href=http://odds.500.com/fenxi/ouzhi-"+match["id"]+".shtml?cids="+cids+" target=\"_blank\">欧</a>&nbsp;");
			html.push("<a href=\"javascript:void(0)\" onclick=\"showdetail("+match["id"]+")\">详</a>");
			html.push("</td>");
			html.push("</tr>");
			html.push("<tr class='lid_"+match["seasonid"]+"'>");
			if(match["hhad"] && match["hhad"]["win"]){
				html.push("<td class=\"matchinfo\">"+match["rangqiu"]+"</td>");
				html.push("<td class=\"odds matchinfo\">"+match["hhad"]["win"]+"</td>");
				html.push("<td class=\"odds matchinfo\">"+match["hhad"]["draw"]+"</td>");
				html.push("<td class=\"odds matchinfo\">"+match["hhad"]["lost"]+"</td>");
			}else{
				html.push("<td class=\"matchinfo\">--</td>");
				html.push("<td class=\"matchinfo\">--</td>");
				html.push("<td class=\"matchinfo\">--</td>");
				html.push("<td class=\"matchinfo\">--</td>");
			}
			for(var m = 0;m<bet_info["m"].length;m++){
				html.push("<td>投："+bet_info["m"][m]+"，奖金："+(bet_info["赔率"][m]*bet_info["m"][m]).toFixed(2)+"</td>");
			}
			html.push("</tr>");
		}
		var matchlist = $("#matchlist");
		matchlist.html(html.join(""));
		matchlist.find(".odds").click(function(){
			$(this).toggleClass("selected");
			calcOdds();
		});
		/*var lcnhtml=["赛事选择："];
		lcnhtml.push("<a onclick=changelidqf('q') style=cursor:pointer >全选</a>&nbsp;");
		lcnhtml.push("<a onclick=changelidqf('f') style=cursor:pointer >反选</a>&nbsp;");
		for(var lid in l_cn_arr){
			lcnhtml.push("<input type=checkbox id="+lid+" class=cbk onchange=changelid(this) value="+lid+" checked=checked />");
			lcnhtml.push("<label for="+lid+">");
			lcnhtml.push(l_cn_arr[lid]);
			lcnhtml.push("</label>");
		}
		$(".lcndiv").html(lcnhtml.join("&nbsp;"));*/
		setTimeout("get_selling_match_list()",30000);
	}
	$(function() {
		get_selling_match_list();
	});