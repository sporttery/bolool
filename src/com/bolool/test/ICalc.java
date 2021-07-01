package com.bolool.test;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import com.bolool.model.TDanchangCalcinfo;
import com.bolool.model.TDanchangMatch;
import com.bolool.mybatis.TDanchangCalcinfoDynamicSqlSupport;
import com.bolool.mybatis.TDanchangMatchDynamicSqlSupport;
import com.bolool.service.TDanchangCalcinfoService;
import com.bolool.service.TDanchangMatchService;

public interface ICalc {

	// 北单投注金额
	public final BigDecimal DANCHANG_BETMONEY = new BigDecimal(65536);
	// 澳博中奖后，奖金盈利比例，如果中奖只保持本金，设置为1
	public final BigDecimal AOBO_WIN_RACE = new BigDecimal(1.2d);
	// 用于计算投注金额，投注赔率要减掉1
	public final BigDecimal SP_VALUE = new BigDecimal(1);
	// 用于计算投注奖金，中奖赔率乘0.65
	public final BigDecimal SP_RACE = new BigDecimal(0.65d);
	// 用于北单除税后只有0.8
	public final BigDecimal BD_TAX = new BigDecimal(0.2d);
	// 用于计算投注佣金，北单投注金额乘返点
	public final BigDecimal DANCHANG_FANDIAN = new BigDecimal(0.07d);
	// 最高奖金
	public final BigDecimal MAX_AWARD = new BigDecimal(5000000);

	public final BigDecimal YJ = DANCHANG_BETMONEY.multiply(DANCHANG_FANDIAN).setScale(2,
			RoundingMode.HALF_DOWN);

	default List<String> getAllIssue() {
		List<String> issueList = new ArrayList<String>();
		TDanchangMatchService srv = new TDanchangMatchService();
		SelectStatementProvider selectStatement = select(TDanchangMatchDynamicSqlSupport.issue)
				.from(TDanchangMatchDynamicSqlSupport.TDanchangMatch).groupBy(TDanchangMatchDynamicSqlSupport.issue)

				.orderBy(TDanchangMatchDynamicSqlSupport.playtime).build().render(RenderingStrategies.MYBATIS3);
		List<TDanchangMatch> rows = srv.getMapper().selectMany(selectStatement);

		rows.forEach(row -> {
			issueList.add(row.getIssue());
		});
		srv.closeSession();
		return issueList;
	}

	default List<String> getAllCalcIssue(String status) {
		List<String> issueList = new ArrayList<String>();
		TDanchangCalcinfoService srv = new TDanchangCalcinfoService();
		SelectStatementProvider selectStatement = select(TDanchangCalcinfoDynamicSqlSupport.issue)
				.from(TDanchangCalcinfoDynamicSqlSupport.TDanchangCalcinfo)
				.where(TDanchangCalcinfoDynamicSqlSupport.status, isEqualTo(status))
				.groupBy(TDanchangCalcinfoDynamicSqlSupport.issue).orderBy(TDanchangCalcinfoDynamicSqlSupport.issue)
				.build().render(RenderingStrategies.MYBATIS3);
		List<TDanchangCalcinfo> rows = srv.getMapper().selectMany(selectStatement);

		rows.forEach(row -> {
			issueList.add(row.getIssue());
		});
		srv.closeSession();
		// 按期号排序
		rows.sort(new Comparator<TDanchangCalcinfo>() {
			@Override
			public int compare(TDanchangCalcinfo o1, TDanchangCalcinfo o2) {
				String issueO1 = o1.getIssue();
				String issueO2 = o2.getIssue();
				if (issueO1.length() == 5) {
					issueO1 = issueO1 + "00";
				}
				if (issueO2.length() == 5) {
					issueO2 = issueO2 + "00";
				}
				return issueO1.compareTo(issueO2);
			}
		});
		return issueList;
	}

	default void calcByIssue(String issue, String status) {
		System.err.println("calcByIssue 没有实现具体功能,issue:" + issue + ",status:" + status);
	};

	default void calc2db() {
		System.err.println("calc2db 没有实现具体功能");
	}
	default void calc4db(String status) {
		List<String> issueList = getAllCalcIssue(status);
		issueList.forEach(issue -> {
			if (issue != null) {
				calcByIssue(issue, status);
			}
		});
	}
	// 从满足条件的表中拿到数据进行计算
	default void calc4db() {
		String status = "OK";
		calc4db(status);
	}

}
