package com.bolool.test;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotNull;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import com.bolool.model.TDanchangCalcinfo;
import com.bolool.model.TDanchangCalcinfoMatch;
import com.bolool.model.TDanchangMatch;
import com.bolool.mybatis.TDanchangMatchDynamicSqlSupport;
import com.bolool.service.TDanchangCalcinfoMatchService;
import com.bolool.service.TDanchangCalcinfoService;
import com.bolool.service.TDanchangMatchService;

public class Calcinfo implements ICalc {
	// 从赛程基本表中获取满足条件的数据保存到数据库中
	public void calc2db() {
		System.out.println("获取数据并计算入库");
		List<String> issueList = getAllIssue();
		TDanchangMatchService srv = new TDanchangMatchService();
		issueList.forEach(issue -> {
			SelectStatementProvider selectStatement1 = select(
					TDanchangMatchDynamicSqlSupport.TDanchangMatch.allColumns())
							.from(TDanchangMatchDynamicSqlSupport.TDanchangMatch)
							.where(TDanchangMatchDynamicSqlSupport.issue, isEqualTo(issue))
							.and(TDanchangMatchDynamicSqlSupport.result, isNotNull())
							.and(TDanchangMatchDynamicSqlSupport.w1, isNotNull())
							.orderBy(TDanchangMatchDynamicSqlSupport.playtime).build()
							.render(RenderingStrategies.MYBATIS3);
			List<TDanchangMatch> listData = srv.getMapper().selectMany(selectStatement1);
			System.out.println(issue + " " + listData.size());
			calc(issue, listData);
		});

		srv.closeSession();

		TDanchangCalcinfoService calcinfoSrv = new TDanchangCalcinfoService();
		TDanchangCalcinfoMatchService calcinfoMatchSrv = new TDanchangCalcinfoMatchService();

		int c = calcinfoSrv.getMapper().insertMultiple(failList);
		boolean commit = true;
		if (c == failList.size()) {
			for (int i = 0; i < failList.size(); i++) {
				TDanchangCalcinfo info = failList.get(i);
				List<TDanchangMatch> matchList = info.getMatchList();
				List<TDanchangCalcinfoMatch> calcMatchList = new ArrayList<TDanchangCalcinfoMatch>(matchList.size());
				matchList.forEach(match -> {
					TDanchangCalcinfoMatch calcinfoMatch = new TDanchangCalcinfoMatch();
					calcinfoMatch.setId(UUID.randomUUID().toString());
					calcinfoMatch.setCalcinfoId(info.getId());
					calcinfoMatch.setDanchangId(match.getId());
					calcMatchList.add(calcinfoMatch);
				});
				int k = calcinfoMatchSrv.getMapper().insertMultiple(calcMatchList);
				if (k != matchList.size()) {
					commit = false;
					break;
				}
			}
		} else {
			commit = false;
		}
		if (commit) {
			calcinfoMatchSrv.commit();
			calcinfoSrv.commit();
		} else {
			calcinfoMatchSrv.rollback();
			calcinfoSrv.rollback();
		}

		c = calcinfoSrv.getMapper().insertMultiple(okList);
		commit = true;
		if (c == okList.size()) {
			for (int i = 0; i < okList.size(); i++) {
				TDanchangCalcinfo info = okList.get(i);
				List<TDanchangMatch> matchList = info.getMatchList();
				List<TDanchangCalcinfoMatch> calcMatchList = new ArrayList<TDanchangCalcinfoMatch>(matchList.size());
				matchList.forEach(match -> {
					TDanchangCalcinfoMatch calcinfoMatch = new TDanchangCalcinfoMatch();
					calcinfoMatch.setId(UUID.randomUUID().toString());
					calcinfoMatch.setCalcinfoId(info.getId());
					calcinfoMatch.setDanchangId(match.getId());
					calcMatchList.add(calcinfoMatch);
				});
				int k = calcinfoMatchSrv.getMapper().insertMultiple(calcMatchList);
				if (k != matchList.size()) {
					commit = false;
					break;
				}
			}
		} else {
			commit = false;
		}
		if (commit) {
			calcinfoMatchSrv.commit();
			calcinfoSrv.commit();
		} else {
			calcinfoMatchSrv.rollback();
			calcinfoSrv.rollback();
		}
		calcinfoSrv.closeSession();
		calcinfoMatchSrv.closeSession();

		System.out.println("数据入库完成！");
	}

	public double G_MIN_ODDS = 8.0d;
	public long PLAYTIME_INTERVAL = 60 * 1000 * 60 * 2;
	public List<TDanchangCalcinfo> okList = new ArrayList<TDanchangCalcinfo>();
	public List<TDanchangCalcinfo> failList = new ArrayList<TDanchangCalcinfo>();

	public List<TDanchangMatch> calc(String issue, List<TDanchangMatch> listData) {
		double minOdds = G_MIN_ODDS;
		System.out.println("期号：" + issue + ",共有比赛：" + listData.size() + " ,最小的高赔值：" + minOdds);
		return calc(issue, listData, minOdds);
	}

	public List<TDanchangMatch> calc(String issue, List<TDanchangMatch> listData, double minOdds) {
		return calc(issue, listData, minOdds, false);
	}

	public List<TDanchangMatch> calc(String issue, List<TDanchangMatch> listData, double minOdds, boolean loop) {
		int totalCount = listData.size();
		if (totalCount < 15) {
			String info = issue + " 比赛少于15场 ，共 " + totalCount;
			TDanchangCalcinfo calcInfo = new TDanchangCalcinfo(info, issue, new BigDecimal(minOdds), "FAIL", totalCount,
					listData);
			calcInfo.setId(UUID.randomUUID().toString());
			failList.add(calcInfo);
			System.err.println("\t\t\t" + info);
			return null;
		}
		// 全局规则 两场时间间隔2小时以上
		// 规则1 澳博高赔赔率大于 4.6 的，
		List<TDanchangMatch> calcList = new ArrayList<TDanchangMatch>();
		int index = 0;
		while (index < totalCount) {
			TDanchangMatch dc = listData.get(index);
			BigDecimal maxWDL1 = dc.getMaxWDL1();
//				System.out.println(maxWDL1.doubleValue());
			if (calcList.size() == 0 && maxWDL1.doubleValue() > minOdds) {
				calcList.add(dc);
			} else if (calcList.size() > 0) {
				TDanchangMatch preDc = calcList.get(calcList.size() - 1);
				if (dc.getPlaytime().getTime() - preDc.getPlaytime().getTime() > PLAYTIME_INTERVAL) {
					if (dc.getMaxWDL().doubleValue() > minOdds) {
						calcList.add(dc);
					}
				}
			}
			index++;
		}
		if (calcList.size() < 15) {
			minOdds -= 0.2;
			if (minOdds < 2.5) {
				String info = "比赛太少，满足条件比赛 " + calcList.size() + " 场，不足15场，不进行计算 " + issue;
				TDanchangCalcinfo calcInfo = new TDanchangCalcinfo(info, issue, new BigDecimal(minOdds), "FAIL",
						totalCount, calcList);
				calcInfo.setId(UUID.randomUUID().toString());
				failList.add(calcInfo);
				System.err.println("\t\t\t" + info);
				return null;
			}
			calcList = calc(issue, listData, minOdds, true);
			if (calcList == null || calcList.size() < 15) {
				return null;
			} else {
				calcData(issue, calcList, minOdds, totalCount);
			}
		} else {
			if (!loop) {
				calcData(issue, calcList, minOdds, totalCount);
			}
			return calcList;
		}
		return null;
	}

	public void calcData(String issue, List<TDanchangMatch> calcList, double minOdds, int totalCount) {
		if (calcList != null) {
			TDanchangCalcinfo calcInfo = new TDanchangCalcinfo("ok", issue, new BigDecimal(minOdds), "OK", totalCount,
					calcList);
			calcInfo.setId(UUID.randomUUID().toString());
			okList.add(calcInfo);
		}

	}
}
