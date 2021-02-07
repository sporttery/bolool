/**
 * 
 */
var menus = [];
menus.push('<h3>');
menus.push('<a href="matchlist.html">投注列表</a>&nbsp;&nbsp;&nbsp;');
menus.push('<a href="usercash.html">流水记录</a>&nbsp;&nbsp;&nbsp;');
menus.push('<a href="dailylist.html">日账统计</a>&nbsp;&nbsp;&nbsp;');
menus.push('<a href="report.html">图表</a>&nbsp;&nbsp;&nbsp;');
menus.push('<a href="ticketTest.html">测试发单</a>&nbsp;&nbsp;&nbsp;');
menus.push('</h3>');
$(function(){
	$.get("/?action=getoddstypelist",function(json){
		var html = ["<label for=\"odds_type\">值博类型：</label><select style=\"margin-right:10px\" id='odds_type'>"];
		for(var i=0;i<json.length;i++){
			var obj = json[i];
			html.push("<option value='"+obj["id"]+"'>"+obj["name"]+"</option>");
		}
		html.push("</select>");
		$(html.join("")).insertBefore($(".page-header > .view > label:eq(0)"));
	});
});