package com.bolool.test;

import static org.mybatis.dynamic.sql.SqlBuilder.equalTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import com.bolool.model.TDanchangCalcinfoResult;
import com.bolool.model.TDanchangCalcinfoResultMatch;
import com.bolool.model.TDanchangMatch;
import com.bolool.mybatis.TDanchangCalcinfoDynamicSqlSupport;
import com.bolool.mybatis.TDanchangCalcinfoMatchDynamicSqlSupport;
import com.bolool.mybatis.TDanchangMatchDynamicSqlSupport;
import com.bolool.service.TDanchangCalcinfoResultMatchService;
import com.bolool.service.TDanchangCalcinfoResultService;
import com.bolool.service.TDanchangMatchService;

public class CalcAllLow implements ICalc {

	private String remark = "北单双选，澳博低赔1.6以上";

	// 根据期号进行计算
	public void calcByIssue(String issue, String status) {
		if (status == null) {
			status = "OK";
		}
		TDanchangCalcinfoResultService resultSrv = null;
		TDanchangMatchService srv = null;
		TDanchangCalcinfoResultMatchService resultMatchSrv = null;
		try {
			srv = new TDanchangMatchService();
			SelectStatementProvider selectStatement = select(
					TDanchangMatchDynamicSqlSupport.TDanchangMatch.allColumns())
							.from(TDanchangCalcinfoDynamicSqlSupport.TDanchangCalcinfo)
							.leftJoin(TDanchangCalcinfoMatchDynamicSqlSupport.TDanchangCalcinfoMatch)
							.on(TDanchangCalcinfoDynamicSqlSupport.id,
									equalTo(TDanchangCalcinfoMatchDynamicSqlSupport.calcinfoId))
							.leftJoin(TDanchangMatchDynamicSqlSupport.TDanchangMatch)
							.on(TDanchangCalcinfoMatchDynamicSqlSupport.danchangId,
									equalTo(TDanchangMatchDynamicSqlSupport.id))
							.where(TDanchangCalcinfoDynamicSqlSupport.status, isEqualTo("OK"))
							.and(TDanchangCalcinfoDynamicSqlSupport.issue, isEqualTo(issue))
							.orderBy(TDanchangMatchDynamicSqlSupport.playtime).build()
							.render(RenderingStrategies.MYBATIS3);
			System.err.print(issue);
			List<TDanchangMatch> rows = srv.getMapper().selectMany(selectStatement);

			boolean danchangWin = true;
			// 单场奖金
			BigDecimal danchangAward = new BigDecimal(1);
			// 澳博投注
			BigDecimal totalBetMoney = new BigDecimal(DANCHANG_BETMONEY.intValue());
			// 开奖和参考赔率波动区间
			BigDecimal minOddsIntervalRace = new BigDecimal(100);
			BigDecimal maxOddsIntervalRace = new BigDecimal(-100);
			BigDecimal minOddsInterval = new BigDecimal(100);
			BigDecimal maxOddsInterval = new BigDecimal(-100);
			BigDecimal aoBoWinSp = null; // 中奖时澳博的赔率
			BigDecimal aoBoWinBet = null;// 中奖时澳博的投注金额
			int i = 0;
			List<TDanchangCalcinfoResultMatch> calcinfoResultMatchList = new ArrayList<TDanchangCalcinfoResultMatch>();
			TDanchangCalcinfoResult calcinfoResult = new TDanchangCalcinfoResult();
			calcinfoResult.setId(UUID.randomUUID().toString());
			calcinfoResult.setPlaytimeMin(rows.get(0).getPlaytime());
			int maxLen = rows.size() > 15 ? 15 : rows.size();
			calcinfoResult.setPlaytimeMax(rows.get(maxLen-1).getPlaytime());
			for (; i < maxLen; i++) {
				TDanchangMatch match = rows.get(i);
				String result = match.getResult();
				String aoBoResult = match.getAoBoResult();
				aoBoWinSp = match.getMinWDL1();
				if (aoBoWinSp.compareTo(new BigDecimal(1.6d)) == -1) {// 低于1.6的赔率，直接换成中间的
					aoBoWinSp = match.getD1();
				}
				boolean aoBoWin = aoBoWinSp == match.getW1() && "W".equals(aoBoResult)
						|| aoBoWinSp == match.getD1() && "D".equals(aoBoResult)
						|| aoBoWinSp == match.getL1() && "L".equals(aoBoResult);
				BigDecimal sp = result.equals("胜") ? match.getSp1()
						: result.equals("平") ? match.getSp2() : match.getSp3();
				BigDecimal resultSp = match.getSp();
				if (sp == null || sp.intValue() == 0) {
					sp = resultSp;
				}
				BigDecimal diffSp = resultSp.subtract(sp);// 开奖赔率和参考赔率的差
				BigDecimal diffSpRace = diffSp.divide(sp, 4, BigDecimal.ROUND_HALF_UP);// 相对于参考赔率，开奖赔率的浮动率
				if (diffSp.compareTo(maxOddsInterval) == 1) {
					maxOddsInterval = diffSp;
				}
				if (diffSp.compareTo(minOddsInterval) == -1) {
					minOddsInterval = diffSp;
				}

				if (diffSpRace.compareTo(maxOddsIntervalRace) == 1) {
					maxOddsIntervalRace = diffSpRace;
				}
				if (diffSpRace.compareTo(minOddsIntervalRace) == -1) {
					minOddsIntervalRace = diffSpRace;
				}

				aoBoWinBet = totalBetMoney.multiply(AOBO_WIN_RACE).divide(aoBoWinSp.subtract(SP_VALUE), 0,
						BigDecimal.ROUND_HALF_UP);
				totalBetMoney = totalBetMoney.add(aoBoWinBet);
				TDanchangCalcinfoResultMatch resultMatch = new TDanchangCalcinfoResultMatch();
				resultMatch.setId(UUID.randomUUID().toString());
				resultMatch.setCalcinfoResultId(calcinfoResult.getId());
				resultMatch.setDanchangId(match.getId());
				resultMatch.setAwardStatus(aoBoWin ? "是" : "否");
				resultMatch.setBetMoney(aoBoWinBet.intValue());
				resultMatch.setBetOdds(aoBoWinSp);
				calcinfoResultMatchList.add(resultMatch);
				if (aoBoWin) {
					danchangWin = false;
					break;
				}
				danchangAward = danchangAward.multiply(resultSp);
			}
			BigDecimal winMoney = null;
			BigDecimal totalWinMoney = null;
			String info = null;
			if (danchangWin) {
				danchangAward = danchangAward.multiply(SP_RACE);
				if (danchangAward.compareTo(MAX_AWARD) == 1) {
					danchangAward = MAX_AWARD;
				}

				info = ("北单中奖，奖金为 " + danchangAward.setScale(2, RoundingMode.HALF_DOWN).toString());

				// 北单奖金*0.65-总投注金额
				// 扣税后
				winMoney = danchangAward.multiply(SP_VALUE.subtract(BD_TAX)).subtract(totalBetMoney).setScale(2,
						RoundingMode.HALF_DOWN);

			} else {
				info = ("澳博中奖，第" + (i + 1) + "场中奖 ，当时赔率:" + aoBoWinSp + " 投注金额:" + aoBoWinBet);
				// 澳博投注金额*澳博投注赔率-总投注金额
				winMoney = aoBoWinBet.multiply(aoBoWinSp).subtract(totalBetMoney).setScale(2, RoundingMode.HALF_DOWN);
			}
			winMoney.setScale(2, RoundingMode.HALF_DOWN);
			totalWinMoney = winMoney.add(YJ).setScale(2, RoundingMode.HALF_DOWN);
			
			calcinfoResult.setAwardType(danchangWin ? "北单" : "澳博");
			calcinfoResult.setAwardWinMoney(winMoney);
			calcinfoResult.setIssue(issue);
			calcinfoResult.setMaxOddsInterval(maxOddsInterval.setScale(4, RoundingMode.HALF_DOWN));
			calcinfoResult.setMaxOddsIntervalRace(maxOddsIntervalRace.setScale(6, RoundingMode.HALF_DOWN));
			calcinfoResult.setMinOddsInterval(minOddsInterval.setScale(4, RoundingMode.HALF_DOWN));
			calcinfoResult.setMinOddsIntervalRace(minOddsIntervalRace.setScale(6, RoundingMode.HALF_DOWN));
			calcinfoResult.setTotalBetMoney(totalBetMoney.intValue());
			calcinfoResult.setTotalWinMoney(totalWinMoney);
			if (!danchangWin) {
				calcinfoResult.setWinBetMoney(aoBoWinBet.intValue());
				calcinfoResult.setWinOdds(aoBoWinSp);
				calcinfoResult.setAwardNum(i + 1);
			}else {
				calcinfoResult.setBdAward(danchangAward);
			}
			calcinfoResult.setRemark(remark  + "，澳博中奖收益："+AOBO_WIN_RACE.multiply(new BigDecimal(100)).intValue()+"%,北单返点:" +  DANCHANG_FANDIAN.multiply(new BigDecimal(100)).intValue()+"%");
			
			info += (",累计投注：" + totalBetMoney.intValue() + "，共盈利 " + totalWinMoney.toString() + " (奖金盈利："
					+ winMoney.toString() + ", 佣金:" + YJ.toString() + ")");
			calcinfoResult.setInfo(info);

			System.err.println(calcinfoResult.getRemark() + " " +info);

			resultSrv = new TDanchangCalcinfoResultService();
			resultMatchSrv = new TDanchangCalcinfoResultMatchService();

			boolean commit = (resultSrv.getMapper().insert(calcinfoResult) == 1);

			if (commit) {
				commit = resultMatchSrv.getMapper().insertMultiple(calcinfoResultMatchList) == calcinfoResultMatchList
						.size();
			}

			if (commit) {
				resultSrv.commit();
				resultMatchSrv.commit();
			} else {
				resultSrv.rollback();
				resultMatchSrv.rollback();
			}
		} finally {
			try {
				srv.closeSession();
				resultSrv.closeSession();
				resultMatchSrv.closeSession();
			} catch (Exception e) {
			}
		}

	}

	
}
