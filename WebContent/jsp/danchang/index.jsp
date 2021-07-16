<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.ibatis.session.SqlSessionFactory"%>
<%@page import="org.apache.ibatis.io.Resources"%>
<%@page import="org.apache.ibatis.session.SqlSessionFactoryBuilder"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="org.apache.ibatis.session.SqlSession"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bolool.model.TDanchangMatch"%>
<%@page import="java.util.List"%>
<%@page import="com.bolool.util.DBHelper"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>北单高手这样玩</title>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="../res/bootstrap.min.css" />

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="../res/bootstrap-theme.min.css" />

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="../res/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="../res/bootstrap.min.js"></script>
<script src="../res/layer-v2.3/layer/layer.js"></script>
<link rel="stylesheet" href="../res/datetimepicker.css" />
<script src="../res/bootstrap-datetimepicker.min.js"></script>
<link rel="stylesheet" href="../res/headshrinker.min.css" />
<script src="../res/jquery.headshrinker.min.js"></script>
<script type="text/javascript" src="../res/jc.js"></script>
<style type="text/css">
.selected {
	background-color: #F5AF19;
}

thead {
	position: sticky
}

.noW1 {
	display: none;
}
</style>
</head>
<body>
	<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DecimalFormat df = new DecimalFormat("#.##");
	final long PLAYTIME_INTERVAL = 60 * 1000 * 100;
	void updateMatchResult(TDanchangMatch match) {
		String rq = match.getRq();
		String fullscore = match.getFullscore();
		double rqI = 0;
		if (rq != null && rq.length() != 0) {
			rqI = Double.parseDouble(rq);
		}
		String[] scores = fullscore.split("-");
		int h = Integer.parseInt(scores[0]);
		int a = Integer.parseInt(scores[1]);
		String result = "负";
		if (h + rqI > a) {
			result = "胜";
		} else if (h + rqI == a) {
			result = "平";
		}
		if (!match.getResult().equals(result)) {
			if (DBHelper.insertOrUpdate(
					"update t_danchang_match set result='" + result + "' where id = '" + match.getId() + "'")) {
				System.out.println("ID:" + match.getId() + ",让球" + rq + "，比分：" + fullscore + ",原来彩果："
						+ match.getResult() + ",计算彩果：" + result + " 更新成功");
				match.setResult(result);
			}
		}
	}
	void setData(HashMap<String, List<TDanchangMatch>> issueMap, HashMap<String, List<TDanchangMatch>> issueMapChuan,
			boolean updateResult) {
		System.out.println("load issue data");
		try {
			String columns = "id,match_id,issue,playtime,home,away,league_name,rq,num,fullscore,sp1,sp2,sp3,sp,result,w1,d1,l1,odds,return_race";
			String sql = "SELECT " + columns
					+ " FROM t_danchang_match where result is not null and sp1 is not null order by  playtime asc;";
			int times = 10000;
			int chuan = 15;
			List<HashMap<String, String>> list = DBHelper.selectListSql(sql, columns.split(","));

			for (HashMap<String, String> item : list) {
				TDanchangMatch match = new TDanchangMatch();
				match.setIssue(item.get("issue"));
				try {
					match.setPlaytime(sdf.parse(item.get("playtime")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				match.setHome(item.get("home"));
				match.setAway(item.get("away"));
				match.setLeagueName(item.get("league_name"));
				match.setNum(Integer.valueOf(item.get("num")));
				match.setRq(item.get("rq"));
				match.setFullscore(item.get("fullscore"));
				match.setSp1(new BigDecimal(item.get("sp1")));
				match.setSp2(new BigDecimal(item.get("sp2")));
				match.setSp3(new BigDecimal(item.get("sp3")));
				match.setSp(new BigDecimal(item.get("sp")));
				match.setResult(item.get("result"));
				match.setId(item.get("id"));
				match.setW1(new BigDecimal(item.get("w1") == null ? "1" : item.get("w1")));
				match.setD1(new BigDecimal(item.get("d1") == null ? "1" : item.get("d1")));
				match.setL1(new BigDecimal(item.get("l1") == null ? "1" : item.get("l1")));

				match.setMatchId(Integer.valueOf(item.get("match_id")));
				if (updateResult) {
					updateMatchResult(match);
				}
				List<TDanchangMatch> issueMatchList = issueMap.get(match.getIssue());
				if (issueMatchList == null) {
					issueMatchList = new ArrayList<TDanchangMatch>();
					issueMap.put(match.getIssue(), issueMatchList);
				}
				issueMatchList.add(match);
			}

			for (String issueI : issueMap.keySet()) {
				List<TDanchangMatch> issueList = issueMap.get(issueI);
				List<TDanchangMatch> newList = new ArrayList<TDanchangMatch>();
				TDanchangMatch matchPre = issueList.get(0);
				newList.add(matchPre);
				for (int i = 1; i < issueList.size(); i++) {
					TDanchangMatch match = issueList.get(i);
					if (match.getPlaytime().getTime() - matchPre.getPlaytime().getTime() > PLAYTIME_INTERVAL) {
						newList.add(match);
						matchPre = match;
					}
				}
				issueMapChuan.put(issueI, newList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}%>
	<%
		String issue = request.getParameter("issue");
		String action = request.getParameter("action");
		boolean updateResult = "updateResult".equals(action);
		boolean refreshData = "refreshData".equals(action);
		List<String> issueList = DBHelper.selectListSql(
				"select distinct issue from t_danchang_match order by case when length(issue)=5 then issue+'00' else issue end asc");
		List<TDanchangMatch> issueMatchList = null;
		List<TDanchangMatch> issueMatchChuanList = null;
		if (issue != null) {
			HashMap<String, List<TDanchangMatch>> issueMap = (HashMap<String, List<TDanchangMatch>>) session
					.getAttribute("issueMap");
			HashMap<String, List<TDanchangMatch>> issueMapChuan = (HashMap<String, List<TDanchangMatch>>) session
					.getAttribute("issueMapChuan");
			if (refreshData || updateResult || (issueMap == null || issueMap.keySet().size() == 0)) {
				issueMap = new HashMap<String, List<TDanchangMatch>>();
				issueMapChuan = new HashMap<String, List<TDanchangMatch>>();
				setData(issueMap, issueMapChuan, updateResult);
				session.setAttribute("issueMap", issueMap);
				session.setAttribute("issueMapChuan", issueMapChuan);
			}
			issueMatchList = issueMap.get(issue);
			issueMatchChuanList = issueMapChuan.get(issue);
		}
		Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	%>

	<input type="hidden" id="actionValue" value="<%=action%>" />
	<div class="container-fluid theme-showcase" role="main">
		<div class="page-header">
			<div class="view">
				<form action="" method="post" id="form">
					<!-- location.href = '?issue='+this.value -->
					期号：<select name="issue" onchange="$('#form')[0].submit()"
						style="margin-right: 15px;">
						<option value="">请选择</option>
						<%
							for (String is : issueList) {
						%><option value="<%=is%>"
							<%=(is.equals(issue) ? "selected=\"selected\"" : "")%>><%=is%></option>
						<%
							}
						%>
					</select> 操作：<select name="action" onchange="$('#form')[0].submit()"
						style="margin-right: 15px;">
						<option value="">请选择</option>
						<option value="refreshData">重新获取数据</option>
						<option value="updateResult">重新计算彩果</option>
					</select> <input type="checkbox" id="rq_control" checked="checked"
						onchange="rqData()" /><label for="rq_control"
						style="margin-right: 15px;">让球<%=(issueMatchChuanList == null ? "" : issueMatchChuanList.size())%></label>
					奖金收益:<select name="jjsy" id="jjsy" onchange="chgJjsy(this.value)" style="margin-right: 15px;">
						<option value="1">1</option>
						<option value="0.8">0.8</option>
						<option value="0.9">0.9</option>
						<option value="1.2">1.2</option>
						<option value="1.5">1.5</option>
					</select> 串关计算：<select name="chuanSec" id="chuanSec"
						onchange="chgChuan(this.value)" style="margin-right: 15px;">
						<option value="">请选择</option>
						<option value="all">全部</option>
						<%
							for (int i = 4; i <= 15 && i < (issueMatchChuanList == null ? 15 : issueMatchChuanList.size()); i++) {
						%><option value="<%=i%>"><%=i%>串
						</option>
						<%
							}
						%>
					</select>
				</form>
			</div>
			<!-- <header>
			<div class="view">
				佣金： <select class="selectpicker minfd_control" id="c" title="最低返点设置">
					<option value="0.15">15%</option>
					<option value="0.099">9.9%</option>
					<option value="0.095">9.5%</option>
					<option value="0.09">9%</option>
					<option value="0.085">8.5%</option>
					<option value="0.08" selected="selected">8%</option>
					<option value="0.07">7%</option>
					<option value="0.06">6%</option>
					<option value="0.05">5%</option>
					<option value="0.04">4%</option>
					<option value="0.03">3%</option>
					<option value="0.02">2%</option>
					<option value="0.01">1%</option>
					<option value="0">0%</option>
				</select>
			</div>
			</header> -->
		</div>
		<div class="view" id="info">开奖sp比参考大，彩果为红色，小为绿色</div>
		<div class="view">
			<div class="col-md-6 glyphicon glyphicon-collapse-up"
				style="line-height: 30px; cursor: pointer"
				onclick="upOrDown(this,0)" id="upOrDownDiv">表格1收起</div>
			<div class="col-md-6 glyphicon glyphicon-collapse-up"
				style="line-height: 30px; cursor: pointer"
				onclick="upOrDown(this,1)">表格2收起</div>
		</div>
		<div class="view">
			<div class="col-md-6" id="listDiv0" style="display: none">
				<table class="table table-bordered">
					<thead id="thead">
						<tr>
							<th>场次</th>
							<th>比赛日期</th>
							<th>联赛</th>
							<th>主队</th>
							<th>客队</th>
							<th>比分</th>
							<th>彩果</th>
							<th>开奖sp</th>
							<th>胜</th>
							<th>平</th>
							<th>负</th>
						</tr>
					</thead>
					<tbody id="issueMatchList">
						<%
							int colspan = 7;
							if (issueMatchList != null) {
								BigDecimal sumSp = new BigDecimal(1d);
								BigDecimal minSp = new BigDecimal(100d);
								BigDecimal maxSp = new BigDecimal(0d);
								BigDecimal sumSp1 = new BigDecimal(1d);
								BigDecimal minSp1 = new BigDecimal(100d);
								BigDecimal maxSp1 = new BigDecimal(0d);
								BigDecimal sumSp2 = new BigDecimal(1d);
								BigDecimal minSp2 = new BigDecimal(100d);
								BigDecimal maxSp2 = new BigDecimal(0d);
								BigDecimal sumSp3 = new BigDecimal(1d);
								BigDecimal minSp3 = new BigDecimal(100d);
								BigDecimal maxSp3 = new BigDecimal(0d);
								BigDecimal size = new BigDecimal(issueMatchList.size());

								for (TDanchangMatch match : issueMatchList) {
									if (updateResult) {
										updateMatchResult(match);
									}

									if (match.getSp().compareTo(minSp) == -1) {
										minSp = match.getSp();
									}
									if (match.getSp().compareTo(maxSp) == 1) {
										maxSp = match.getSp();
									}
									sumSp = sumSp.add(match.getSp());

									if (match.getSp1().compareTo(minSp1) == -1) {
										minSp1 = match.getSp1();
									}
									if (match.getSp1().compareTo(maxSp1) == 1) {
										maxSp1 = match.getSp1();
									}
									sumSp1 = sumSp1.add(match.getSp1());

									if (match.getSp2().compareTo(minSp2) == -1) {
										minSp2 = match.getSp2();
									}
									if (match.getSp2().compareTo(maxSp2) == 1) {
										maxSp2 = match.getSp2();
									}
									sumSp2 = sumSp2.add(match.getSp2());

									if (match.getSp3().compareTo(minSp3) == -1) {
										minSp3 = match.getSp3();
									}
									if (match.getSp3().compareTo(maxSp3) == 1) {
										maxSp3 = match.getSp3();
									}
									sumSp3 = sumSp3.add(match.getSp3());

									BigDecimal winSp = match.getSp3();
									if ("胜".equals(match.getResult())) {
										winSp = match.getSp1();
									} else if ("平".equals(match.getResult())) {
										winSp = match.getSp2();
									}
									String bgcolor = "#333";
									if (match.getSp().compareTo(winSp) == 1) {
										bgcolor = "red";
									} else if (match.getSp().compareTo(winSp) == -1) {
										bgcolor = "green";
									}

									match.getPropMap().put("bgcolor", bgcolor);
									String rqClass = "norq";
									if (match.getRq() != null && match.getRq().length() > 0) {
										rqClass = "rq";
									}
									match.getPropMap().put("rqClass", rqClass);

									BigDecimal minOdds = match.getW1();
									if (minOdds.compareTo(match.getD1()) == 1) {
										minOdds = match.getD1();
									}
									if (minOdds.compareTo(match.getL1()) == 1) {
										minOdds = match.getL1();
									}
									boolean aoBoLowOddsWin = (minOdds == match.getW1() && match.getResult().equals("胜"))
											|| (minOdds == match.getD1() && match.getResult().equals("平"))
											|| (minOdds == match.getL1() && match.getResult().equals("负"));
									match.getPropMap().put("aoBoLowOddsWin", aoBoLowOddsWin);
									match.getPropMap().put("aoBoLowOdds", minOdds);

									BigDecimal maxOdds = match.getW1();
									if (maxOdds.compareTo(match.getD1()) == -1) {
										maxOdds = match.getD1();
									}
									if (maxOdds.compareTo(match.getL1()) == -1) {
										maxOdds = match.getL1();
									}
									boolean aoBoHighOddsWin = (maxOdds == match.getW1() && match.getResult().equals("胜"))
											|| (maxOdds == match.getD1() && match.getResult().equals("平"))
											|| (maxOdds == match.getL1() && match.getResult().equals("负"));
									match.getPropMap().put("aoBoHighOddsWin", aoBoHighOddsWin);
									match.getPropMap().put("aoBoHighOdds", maxOdds);
						%>
						<tr class="<%=match.getPropMap().get("rqClass")%>"
							id="issueMatchList<%=match.getId()%>">
							<td><a target="_blank" title="转到这一期<%=match.getIssue()%>"
								href="https://www.310win.com/buy/DanChang.aspx?TypeID=5&issueNum=<%=match.getIssue()%>"><%=match.getNum()%></a></td>
							<td><%=sdf.format(match.getPlaytime())%></td>
							<td><a target="_blank" title="数据分析"
								href="https://www.310win.com/analysis/<%=match.getMatchId()%>.htm"><%=match.getLeagueName()%></a></td>
							<td><%=match.getHome()
							+ (match.getRq() != null && match.getRq().length() > 0 ? "(" + match.getRq() + ")" : "")%></td>
							<td><%=match.getAway()%></td>
							<td><%=match.getFullscore()%></td>
							<td bgcolor="<%=bgcolor%>" style="color: #fff"><%=match.getResult()%></td>
							<td><a target="_blank" title="百家欧指"
								href="https://www.310win.com/1x2/<%=match.getMatchId()%>.html"><%=df.format(match.getSp())%></a></td>
							<td><%=df.format(match.getSp1())%></td>
							<td><%=df.format(match.getSp2())%></td>
							<td><%=df.format(match.getSp3())%></td>
						</tr>
						<%
							}
						%>

						<tr>
							<td colspan="<%=colspan%>" style="text-align: center">最大</td>
							<td><%=df.format(maxSp)%></td>
							<td><%=df.format(maxSp1)%></td>
							<td><%=df.format(maxSp2)%></td>
							<td><%=df.format(maxSp3)%></td>
						</tr>
						<tr>
							<td colspan="<%=colspan%>" style="text-align: center">平均</td>
							<td><%=df.format(sumSp.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumSp1.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumSp2.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumSp3.divide(size, 4, RoundingMode.HALF_UP))%></td>
						</tr>
						<tr>
							<td colspan="<%=colspan%>" style="text-align: center">最小</td>
							<td><%=df.format(minSp)%></td>
							<td><%=df.format(minSp1)%></td>
							<td><%=df.format(minSp2)%></td>
							<td><%=df.format(minSp3)%></td>
						</tr>
						<%
							} else {
						%>
						<tr>
							<td colspan="<%=colspan + 4%>">暂无数据</td>
						<tr>
							<%
								}
							%>
						
					</tbody>
				</table>
			</div>
			<div class="col-md-6" id="listDiv1">
				<table class="table table-bordered">
					<thead id="thead">
						<tr>
							<th rowspan=2>序号</th>
							<th rowspan=2>场次</th>
							<th rowspan=2>比赛日期</th>
							<th rowspan=2>联赛</th>
							<th rowspan=2>主队</th>
							<th rowspan=2>客队</th>
							<th rowspan=2>比分</th>
							<th rowspan=2>彩果</th>
							<th rowspan=2>开奖sp</th>
							<th colspan=3>参考SP</th>
							<th colspan=3>澳博赔率</th>
						</tr>
						<tr>
							<th>胜</th>
							<th>平</th>
							<th>负</th>
							<th>胜</th>
							<th>平</th>
							<th>负</th>
						</tr>
					</thead>
					<tbody id="issueMatchChuanList">
						<%
							colspan = 8;
							if (issueMatchChuanList != null) {
								int index = 1;
								BigDecimal sumSp = new BigDecimal(1d);
								BigDecimal minSp = new BigDecimal(100d);
								BigDecimal maxSp = new BigDecimal(0d);
								BigDecimal sumSp1 = new BigDecimal(1d);
								BigDecimal minSp1 = new BigDecimal(100d);
								BigDecimal maxSp1 = new BigDecimal(0d);
								BigDecimal sumSp2 = new BigDecimal(1d);
								BigDecimal minSp2 = new BigDecimal(100d);
								BigDecimal maxSp2 = new BigDecimal(0d);
								BigDecimal sumSp3 = new BigDecimal(1d);
								BigDecimal minSp3 = new BigDecimal(100d);
								BigDecimal maxSp3 = new BigDecimal(0d);

								BigDecimal sumW1 = new BigDecimal(1d);
								BigDecimal minW1 = new BigDecimal(100d);
								BigDecimal maxW1 = new BigDecimal(0d);
								BigDecimal sumD1 = new BigDecimal(1d);
								BigDecimal minD1 = new BigDecimal(100d);
								BigDecimal maxD1 = new BigDecimal(0d);
								BigDecimal sumL1 = new BigDecimal(1d);
								BigDecimal minL1 = new BigDecimal(100d);
								BigDecimal maxL1 = new BigDecimal(0d);

								BigDecimal size = new BigDecimal(issueMatchChuanList.size());
								for (TDanchangMatch match : issueMatchChuanList) {
									if (match.getSp().compareTo(minSp) == -1) {
										minSp = match.getSp();
									}
									if (match.getSp().compareTo(maxSp) == 1) {
										maxSp = match.getSp();
									}
									sumSp = sumSp.add(match.getSp());

									if (match.getSp1().compareTo(minSp1) == -1) {
										minSp1 = match.getSp1();
									}
									if (match.getSp1().compareTo(maxSp1) == 1) {
										maxSp1 = match.getSp1();
									}
									sumSp1 = sumSp1.add(match.getSp1());

									if (match.getSp2().compareTo(minSp2) == -1) {
										minSp2 = match.getSp2();
									}
									if (match.getSp2().compareTo(maxSp2) == 1) {
										maxSp2 = match.getSp2();
									}
									sumSp2 = sumSp2.add(match.getSp2());

									if (match.getSp3().compareTo(minSp3) == -1) {
										minSp3 = match.getSp3();
									}
									if (match.getSp3().compareTo(maxSp3) == 1) {
										maxSp3 = match.getSp3();
									}
									sumSp3 = sumSp3.add(match.getSp3());

									if (match.getW1().compareTo(minW1) == -1) {
										minW1 = match.getW1();
									}
									if (match.getW1().compareTo(maxW1) == 1) {
										maxW1 = match.getW1();
									}
									sumW1 = sumW1.add(match.getW1());

									if (match.getD1().compareTo(minD1) == -1) {
										minD1 = match.getD1();
									}
									if (match.getD1().compareTo(maxD1) == 1) {
										maxD1 = match.getD1();
									}
									sumD1 = sumD1.add(match.getD1());

									if (match.getL1().compareTo(minL1) == -1) {
										minL1 = match.getL1();
									}
									if (match.getL1().compareTo(maxL1) == 1) {
										maxL1 = match.getL1();
									}
									sumL1 = sumL1.add(match.getL1());
						%>
						<tr class="<%=match.getPropMap().get("rqClass")%>"
							id="issueMatchChuanList<%=match.getId()%>">
							<td><%=index++%></td>
							<td><a target="_blank" title="转到这一期<%=match.getIssue()%>"
								href="https://www.310win.com/buy/DanChang.aspx?TypeID=5&issueNum=<%=match.getIssue()%>"><%=match.getNum()%></a></td>
							<td><%=sdf.format(match.getPlaytime())%></td>
							<td><a target="_blank" title="数据分析"
								href="https://www.310win.com/analysis/<%=match.getMatchId()%>.htm"><%=match.getLeagueName()%></a></td>
							<td><%=match.getHome()
							+ (match.getRq() != null && match.getRq().length() > 0 ? "(" + match.getRq() + ")" : "")%></td>
							<td><%=match.getAway()%></td>
							<td><%=match.getFullscore()%></td>
							<td bgcolor="<%=match.getPropMap().get("bgcolor")%>"
								style="color: #fff"><%=match.getResult()%></td>
							<td><a target="_blank" title="百家欧指"
								href="https://www.310win.com/1x2/<%=match.getMatchId()%>.html"><%=df.format(match.getSp())%></a></td>
							<td
								<%=("胜".equals(match.getResult()) ? "style='color:red'" : "")%>><%=df.format(match.getSp1())%></td>
							<td
								<%=("平".equals(match.getResult()) ? "style='color:red'" : "")%>><%=df.format(match.getSp2())%></td>
							<td
								<%=("负".equals(match.getResult()) ? "style='color:red'" : "")%>><%=df.format(match.getSp3())%></td>
							<td><%=df.format(match.getW1())%></td>
							<td><%=df.format(match.getD1())%></td>
							<td><%=df.format(match.getL1())%></td>
						</tr>
						<%
							}
						%>

						<tr>
							<td colspan="<%=colspan%>" style="text-align: center">最大</td>
							<td><%=df.format(maxSp)%></td>
							<td><%=df.format(maxSp1)%></td>
							<td><%=df.format(maxSp2)%></td>
							<td><%=df.format(maxSp3)%></td>
							<td><%=df.format(maxW1)%></td>
							<td><%=df.format(maxD1)%></td>
							<td><%=df.format(maxL1)%></td>
						</tr>
						<tr>
							<td colspan="<%=colspan%>" style="text-align: center">平均</td>
							<td><%=df.format(sumSp.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumSp1.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumSp2.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumSp3.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumW1.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumD1.divide(size, 4, RoundingMode.HALF_UP))%></td>
							<td><%=df.format(sumL1.divide(size, 4, RoundingMode.HALF_UP))%></td>
						</tr>
						<tr>
							<td colspan="<%=colspan%>" style="text-align: center">最小</td>
							<td><%=df.format(minSp)%></td>
							<td><%=df.format(minSp1)%></td>
							<td><%=df.format(minSp2)%></td>
							<td><%=df.format(minSp3)%></td>
							<td><%=df.format(minW1)%></td>
							<td><%=df.format(minD1)%></td>
							<td><%=df.format(minL1)%></td>
						</tr>
						<%
							} else {
						%>
						<tr>
							<td colspan="<%=colspan + 7%>">暂无数据</td>
						<tr>
							<%
								}
							%>
						
					</tbody>
				</table>
			</div>
		</div>

		<div class="view" >
		<table class="table table-bordered" id="msgTable"  style="display: none">
				<thead>
				<tr>
				<th>情况</th>
				<th> 北单中奖</th>
				<th>北单最高奖金</th>
				<th>北单最低奖</th>
				<th>北单平均奖金</th> 
				<th>澳博中奖</th>
				<th>澳博最大投注</th>
				<th>澳博平均投注</th>
 				<th>整体收益</th>
				</tr>
				</thead>
				</table></div>
		<div class="view">
			<table class="table table-bordered" style="display: none"
				id="popTable"></table>
		</div>
	</div>
	<script type="text/javascript">
		var issueMapList=<%=issueMatchChuanList == null ? "[]" : gson.toJson(issueMatchChuanList)%>;
		var calcIssueMapList;
		
		function upOrDown(obj, index) {
			var text = $(obj).text().trim();
			if (text == "表格" + (index + 1) + "收起") {
				obj.className = "col-md-6 glyphicon glyphicon-collapse-down";
				text = "表格" + (index + 1) + "展开";
				$("#listDiv" + index).hide();
				$("#listDiv" + (index == 0 ? 1 : 0)).attr("class", "col-md-12");
			} else {
				obj.className = "col-md-6 glyphicon glyphicon-collapse-up";
				text = "表格" + (index + 1) + "收起";
				$("#listDiv" + (index == 0 ? 1 : 0)).attr("class", "col-md-6");
				$("#listDiv" + index).show();
			}

			$(obj).text(text);

		}
		function rqData(){
			var rqControl = $("#rq_control");
			var norq_count=$("#issueMatchChuanList .norq").length;
			if(rqControl[0].checked){
				$(".rq").show();
				rqControl.next().text("让球"+($("#issueMatchChuanList tr").length-3));
				$("#chuanSec option:gt(1)").each((idx,el)=>{
					if(parseInt(el.value) < norq_count && el.style.display=='none'){
						el.style.display='block';
					}
				})
			}else{
				$(".rq").hide();
				rqControl.next().text("让球"+norq_count);
				$("#chuanSec option:gt(1)").each((idx,el)=>{
					if(parseInt(el.value) > norq_count){
						el.style.display='none';
					}
				})
			}
		}
		$(function() {
			$("#upOrDownDiv").click();
			var action = $("#actionValue").val();
			if(action!=""){
			$("select[name=action]").find("option").each((idx,el)=>{
				if(el.value==action){
						if(typeof layer!="undefined"){
							layer.alert(el.innerText+"完成")}
						else{
							alert(el.innerText+"完成")}
					}
				});
			}
			var greenCount=0,redCount=0;
			$("#issueMatchList td").each((idx,el)=>{
				var bgcolor=$(el).attr("bgcolor");
				if(bgcolor==='red'){
					redCount++;
				}else if(bgcolor==='green'){
					greenCount++;
				}
			});
			if(greenCount>0)
			$("#info").append('<font color=red>共有'+(greenCount+redCount)+'场，红色：'+redCount+'绿色：'+greenCount+'</font>');
			
		});

		function combination(arr, m) {
			  let r = [];
			  _([], arr, m);
			  r.sort(function(a,b){
				    return a.num - b.num
				 });
			  return r;
			  function _(t, a, m) {
			    //t:临时数组 a:目标数组 m：多少个数进行组合
			    if (m === 0) {
			      r[r.length] = t;//相当于push
			      return;
			    }
			    for (let i = 0; i <= a.length - m; i++) {
			      //从0开始 到n-m
			 
			      let b = t.slice();//将t赋值给b 不能用=赋值，使用slice会形成新的数组赋值
			      b.push(a[i])
			      _(b, a.slice(i + 1), m - 1); 
			    }
			  }
			}
		var	msgDiv;
		var calcIssue={};
		function chgJjsy(){
			var val = $("#chuanSec").val();
			var chuan = parseInt(val);
			if(chuan===""){
				return;
			}
			if(isNaN(chuan) && val!="all"){
				return;
			}
			chgChuan(chuan);
		}
		function chgChuan(val){
			if(val===""){
				return;
			}
			
			var chuan = parseInt(val);
			if(isNaN(chuan) && val!="all"){
				return;
			}
			var jcBet = Math.pow(2,chuan)*2;//北单投注金额，每场双选
			var jjsy = parseFloat($("#jjsy").val());//设置的奖金收益
			$(".glyphicon-collapse-up").click();
			var rqControl = $("#rq_control");
			if(rqControl[0].checked){
				calcIssueMapList=issueMapList.slice(0,issueMapList.length);
			}else{
				calcIssueMapList=[];
				issueMapList.forEach(item=>{
					if(item.rq===""){
						calcIssueMapList.push(item);
					}
				});
			}
			if(val==="all"){
				var minChan =  parseInt($("#chuanSec option:eq(2)").val());
				var maxChan = parseInt( $("#rq_control").next().text().replace("让球",""));;
				if(maxChan>15){
					maxChan=15;
				}
				for(var i=minChan;i<=maxChan && i <calcIssueMapList.length ;i++){
					chgChuan(i);
				}
				return;
			}
			
			var key = chuan+"串1"+jjsy;
			if(calcIssue[key]){
				return;
			}
			var tmpList=[];
			calcIssueMapList.forEach(match=>{
				if(match.w1!=1){
					tmpList.push(match);
				}
			});
			calcIssueMapList=[];
			calcIssue[key] = combination(tmpList,chuan);
			if(!msgDiv){
				msgDiv = $("#msgTable");
			}
			var rs = {"low":{totalWin:0,abWinCount:0},"high":{totalWin:0,abWinCount:0}}
			var bd={"low":{award:0,minAward:1000000,maxAward:0},"high":{award:0,minAward:1000000,maxAward:0}};
			var ab={"low":{bet:0,maxBet:0,minBet:100000000000000},"high":{bet:0,maxBet:0,minBet:100000000000000}};
			calcIssue[key].forEach(arr=>{
				var result = calcByArr(arr,jcBet,jjsy);
				//{ sumBet4Low ,sumBet4High,lowIdx,highIdx,bdAward}
				ab.low.bet += result.low.bet;
				if(ab.low.maxBet <result.low.bet){
					ab.low.maxBet=result.low.bet
				}
				if(ab.low.minBet > result.low.bet){
					ab.low.minBet=result.low.bet
				}

				ab.high.bet += result.high.bet;
				if(ab.high.maxBet < result.high.bet){
					ab.high.maxBet=result.high.bet
				}
				if(ab.high.minBet > result.high.bet){
					ab.high.minBet=result.high.bet
				}
				var win4low = 0,win4high=0;
				if(result.low.idx==-1){//北单中奖
					win4low = result.low.bdAward - result.low.bet;
					bd.low.award += result.low.bdAward;
					if(bd.low.maxAward < result.low.bdAward){
						bd.low.maxAward=result.low.bdAward
					}
					if(bd.low.minAward > result.low.bdAward){
						bd.low.minAward=result.low.bdAward
					}
					
				}else{
					win4low = result.low.bet*(jjsy-1);
					rs.low.abWinCount++;					
				}
				if(result.high.idx==-1){//北单中奖
					win4high = result.high.bdAward - result.high.bet;
					bd.high.award += result.high.bdAward;
					if(bd.high.maxAward < result.high.bdAward){
						bd.high.maxAward=result.high.bdAward
					}
					if(bd.high.minAward > result.high.bdAward){
						bd.high.minAward=result.high.bdAward
					}
				}else{
					win4high = result.high.bet*(jjsy-1);
					rs.high.abWinCount++;
				}
				rs.low.totalWin+= win4low;
				rs.high.totalWin+= win4high;
			});
			var totalDan = calcIssue[key] .length;
			rs.low.bdWinCount = totalDan - rs.low.abWinCount;
			rs.high.bdWinCount = totalDan - rs.high.abWinCount;
			console.log(rs);
			if(rs.low.bdWinCount==0){
				bd.low.maxAward=0;				
				bd.low.minAward=0;
				bd.low.avgAward=0;
			}else{
				bd.low.maxAward=bd.low.maxAward.toFixed(2);		
				bd.low.minAward=bd.low.minAward.toFixed(2);
				bd.low.avgAward=(bd.low.award/rs.low.bdWinCount).toFixed(2);
			}
			ab.low.maxBet=ab.low.maxBet.toFixed(2);	
			ab.low.minBet=ab.low.minBet.toFixed(2);
			ab.low.avgBet=(ab.low.bet/rs.low.abWinCount).toFixed(2);
			if(rs.high.bdWinCount==0){
				bd.high.maxAward=0;				
				bd.high.minAward=0;
				bd.high.avgAward=0;
			}else{
				bd.high.maxAward=bd.high.maxAward.toFixed(2);				
				bd.high.minAward=bd.high.minAward.toFixed(2);
				bd.high.avgAward=(bd.high.award/rs.high.bdWinCount).toFixed(2);
			}
			ab.high.maxBet=ab.high.maxBet.toFixed(2);
			ab.high.minBet=ab.high.minBet.toFixed(2);
			ab.high.avgBet=(ab.high.bet/rs.high.abWinCount).toFixed(2);
			msgDiv.hide();
			msgDiv.append("<tr><td colspan=7>筛选比赛<font color=red>"+tmpList.length+"</font>场，进行<font color=red>"+
			chuan+"串1【"+jjsy+"】</font>组合后结果<a href='javascript:showDetail(\""+key+"\")'>"+totalDan+"</a>单，每单金额："+jcBet+" 共"+jcBet*totalDan+"</td></tr>");
			msgDiv.append("<tr><td>澳博低赔</td><td>"+rs.low.bdWinCount+"单</td><td>"+bd.low.maxAward+" </td><td>"+bd.low.minAward+"</td> <td> "+bd.low.avgAward
			+"</td><td>"+rs.low.abWinCount+"单</td><td>"+ab.low.maxBet+" </td><td>"+ab.low.avgBet+" </td><td>"+rs.low.totalWin.toFixed(2)+"</td></tr>"
			+"<tr><td>澳博高赔</td><td>"+rs.high.bdWinCount+"单</td><td>"+bd.high.maxAward+"</td><td>"+bd.high.minAward+" </td><td> "+bd.high.avgAward
			+"</td><td>"+rs.high.abWinCount+"单</td><td>"+ab.high.maxBet+" </td><td>"+ab.high.avgBet+"</td><td>"+rs.high.totalWin.toFixed(2)+"</td></tr>");
			msgDiv.show();
		}
		function calcByArr(arr,jcBet,jjsy){
			var bdAward=1;
			var rs = {"low":{bet:jcBet,idx:-1,bdAward:0},"high":{bet:jcBet,idx:-1,bdAward:0}};
			for(var i=0;i<arr.length;i++){
				var match = arr[i];
				var lowOdds = match.propMap.aoBoLowOdds;
				var highOdds = match.propMap.aoBoHighOdds;
				if(rs.low.idx==-1){
				var currentBet4Low =  Math.ceil(rs.low.bet*jjsy/(lowOdds-1));
				rs.low.bet += currentBet4Low;
				match.currentBet4Low=currentBet4Low;
				match.sumBet4Low=rs.low.bet;
					}
				if(rs.high.idx==-1){
				var currentBet4High =  Math.ceil(rs.high.bet*jjsy/(highOdds-1));
				rs.high.bet+=currentBet4High;
				match.currentBet4High=currentBet4High;
				match.sumBet4High=rs.high.bet;
					}
				if(match.propMap.aoBoLowOddsWin){
					rs.low.idx=i;
				}
				if(match.propMap.aoBoHighOddsWin){
					rs.high.idx=i;
				}
				bdAward*=match.sp;
			}
			if(rs.low.idx==-1){
				rs.low.bdAward=bdAward;
			}
			if(rs.high.idx==-1){
				rs.high.bdAward=bdAward;
			}
			return rs;
		}
		function showDetail(key){
			var list = calcIssue[key];
			if(!list || list.length==0){
				layer.alert("没有数据");
				return;
			}
			var html=[];
			html.push('<thead>'+$("#listDiv1 #thead").html()+'</thead>');
			var tdLen = 0;
			$("#listDiv1 #thead tr:first th").each((idx,el)=>{
				if($(el).attr("colspan")>"1"){
					tdLen += parseInt($(el).attr("colspan"))
				}else{
					tdLen++;
				}
			});
			
			for(var i=0;i<list.length;i++){
				var trTitle = '第'+(i+1)+'单';
				
				var thtml=[];
				var idx = -1;
				//北单投注高中赔双选，澳博投注低赔
				for(var j=0;j<list[i].length;j++){
					var match = list[i][j];
					var trHtml ='<tr class="no'+(i+1)+' w1">'+ $("#issueMatchChuanList"+match.id).html()+'</tr>';
					if(match.w1==1 && idx == -1){
						idx = j;
					}
					thtml.push(trHtml);
					var maxOdds = match.w1;
					if(maxOdds>match.d1){
						maxOdds=match.d1;
					}
					if(maxOdds>match.l1){
						maxOdds=match.l1;
					}
				}
				thtml=thtml.join('');
				if(idx!=-1 ){
					trTitle += "第"+(idx+1)+"场比赛id="+match.id+"的奥博赔率没有，忽略这单 <a href='javascript:$(\".no"+(i+1)+"\").toggle(\"noW1\")'>显示</a>";
					thtml=thtml.replace(/w1/g,'noW1');
				}
				html.push('<tr><td colspan='+tdLen+'>'+trTitle+'</td></tr>');
				html.push(thtml);
			}
			html.unshift('<tr><td colspan='+tdLen+'>'+key + "组合，共"+list.length+"单"+'</td></tr>');
			$("#popTable").hide().html(html.join("")).show();
			/*layer.open({
				  type: 1, 
				  title: key + "组合，共"+list.length+"单",
				  area: ['95%', '500px'],
				  content: $("#popTable")
			});*/
		}
	</script>
</body>
</html>