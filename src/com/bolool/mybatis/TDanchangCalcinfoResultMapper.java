package com.bolool.mybatis;

import static com.bolool.mybatis.TDanchangCalcinfoResultDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.bolool.model.TDanchangCalcinfoResult;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface TDanchangCalcinfoResultMapper {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	BasicColumn[] selectList = BasicColumn.columnList(id, issue, awardType, bdAward, awardNum, winOdds, winBetMoney,
			totalBetMoney, awardWinMoney, totalWinMoney, minOddsInterval, maxOddsInterval, minOddsIntervalRace,
			maxOddsIntervalRace, playtimeMin, playtimeMax, info, remark);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	long count(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	@DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
	int delete(DeleteStatementProvider deleteStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
	int insert(InsertStatementProvider<TDanchangCalcinfoResult> insertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
	int insertMultiple(MultiRowInsertStatementProvider<TDanchangCalcinfoResult> multipleInsertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@ResultMap("TDanchangCalcinfoResultResult")
	Optional<TDanchangCalcinfoResult> selectOne(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@Results(id = "TDanchangCalcinfoResultResult", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR, id = true),
			@Result(column = "issue", property = "issue", jdbcType = JdbcType.VARCHAR),
			@Result(column = "award_type", property = "awardType", jdbcType = JdbcType.VARCHAR),
			@Result(column = "bd_award", property = "bdAward", jdbcType = JdbcType.DECIMAL),
			@Result(column = "award_num", property = "awardNum", jdbcType = JdbcType.INTEGER),
			@Result(column = "win_odds", property = "winOdds", jdbcType = JdbcType.DECIMAL),
			@Result(column = "win_bet_money", property = "winBetMoney", jdbcType = JdbcType.INTEGER),
			@Result(column = "total_bet_money", property = "totalBetMoney", jdbcType = JdbcType.INTEGER),
			@Result(column = "award_win_money", property = "awardWinMoney", jdbcType = JdbcType.DECIMAL),
			@Result(column = "total_win_money", property = "totalWinMoney", jdbcType = JdbcType.DECIMAL),
			@Result(column = "min_odds_interval", property = "minOddsInterval", jdbcType = JdbcType.DECIMAL),
			@Result(column = "max_odds_interval", property = "maxOddsInterval", jdbcType = JdbcType.DECIMAL),
			@Result(column = "min_odds_interval_race", property = "minOddsIntervalRace", jdbcType = JdbcType.DECIMAL),
			@Result(column = "max_odds_interval_race", property = "maxOddsIntervalRace", jdbcType = JdbcType.DECIMAL),
			@Result(column = "playtime_min", property = "playtimeMin", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "playtime_max", property = "playtimeMax", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "info", property = "info", jdbcType = JdbcType.VARCHAR),
			@Result(column = "remark", property = "remark", jdbcType = JdbcType.VARCHAR) })
	List<TDanchangCalcinfoResult> selectMany(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	@UpdateProvider(type = SqlProviderAdapter.class, method = "update")
	int update(UpdateStatementProvider updateStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default long count(CountDSLCompleter completer) {
		return MyBatis3Utils.countFrom(this::count, TDanchangCalcinfoResult, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int delete(DeleteDSLCompleter completer) {
		return MyBatis3Utils.deleteFrom(this::delete, TDanchangCalcinfoResult, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int deleteByPrimaryKey(String id_) {
		return delete(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int insert(TDanchangCalcinfoResult record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfoResult,
				c -> c.map(id).toProperty("id").map(issue).toProperty("issue").map(awardType).toProperty("awardType")
						.map(bdAward).toProperty("bdAward").map(awardNum).toProperty("awardNum").map(winOdds)
						.toProperty("winOdds").map(winBetMoney).toProperty("winBetMoney").map(totalBetMoney)
						.toProperty("totalBetMoney").map(awardWinMoney).toProperty("awardWinMoney").map(totalWinMoney)
						.toProperty("totalWinMoney").map(minOddsInterval).toProperty("minOddsInterval")
						.map(maxOddsInterval).toProperty("maxOddsInterval").map(minOddsIntervalRace)
						.toProperty("minOddsIntervalRace").map(maxOddsIntervalRace).toProperty("maxOddsIntervalRace")
						.map(playtimeMin).toProperty("playtimeMin").map(playtimeMax).toProperty("playtimeMax").map(info)
						.toProperty("info").map(remark).toProperty("remark"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.936+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int insertMultiple(Collection<TDanchangCalcinfoResult> records) {
		return MyBatis3Utils.insertMultiple(this::insertMultiple, records, TDanchangCalcinfoResult,
				c -> c.map(id).toProperty("id").map(issue).toProperty("issue").map(awardType).toProperty("awardType")
						.map(bdAward).toProperty("bdAward").map(awardNum).toProperty("awardNum").map(winOdds)
						.toProperty("winOdds").map(winBetMoney).toProperty("winBetMoney").map(totalBetMoney)
						.toProperty("totalBetMoney").map(awardWinMoney).toProperty("awardWinMoney").map(totalWinMoney)
						.toProperty("totalWinMoney").map(minOddsInterval).toProperty("minOddsInterval")
						.map(maxOddsInterval).toProperty("maxOddsInterval").map(minOddsIntervalRace)
						.toProperty("minOddsIntervalRace").map(maxOddsIntervalRace).toProperty("maxOddsIntervalRace")
						.map(playtimeMin).toProperty("playtimeMin").map(playtimeMax).toProperty("playtimeMax").map(info)
						.toProperty("info").map(remark).toProperty("remark"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int insertSelective(TDanchangCalcinfoResult record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfoResult, c -> c.map(id)
				.toPropertyWhenPresent("id", record::getId).map(issue).toPropertyWhenPresent("issue", record::getIssue)
				.map(awardType).toPropertyWhenPresent("awardType", record::getAwardType).map(bdAward)
				.toPropertyWhenPresent("bdAward", record::getBdAward).map(awardNum)
				.toPropertyWhenPresent("awardNum", record::getAwardNum).map(winOdds)
				.toPropertyWhenPresent("winOdds", record::getWinOdds).map(winBetMoney)
				.toPropertyWhenPresent("winBetMoney", record::getWinBetMoney).map(totalBetMoney)
				.toPropertyWhenPresent("totalBetMoney", record::getTotalBetMoney).map(awardWinMoney)
				.toPropertyWhenPresent("awardWinMoney", record::getAwardWinMoney).map(totalWinMoney)
				.toPropertyWhenPresent("totalWinMoney", record::getTotalWinMoney).map(minOddsInterval)
				.toPropertyWhenPresent("minOddsInterval", record::getMinOddsInterval).map(maxOddsInterval)
				.toPropertyWhenPresent("maxOddsInterval", record::getMaxOddsInterval).map(minOddsIntervalRace)
				.toPropertyWhenPresent("minOddsIntervalRace", record::getMinOddsIntervalRace).map(maxOddsIntervalRace)
				.toPropertyWhenPresent("maxOddsIntervalRace", record::getMaxOddsIntervalRace).map(playtimeMin)
				.toPropertyWhenPresent("playtimeMin", record::getPlaytimeMin).map(playtimeMax)
				.toPropertyWhenPresent("playtimeMax", record::getPlaytimeMax).map(info)
				.toPropertyWhenPresent("info", record::getInfo).map(remark)
				.toPropertyWhenPresent("remark", record::getRemark));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default Optional<TDanchangCalcinfoResult> selectOne(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectOne(this::selectOne, selectList, TDanchangCalcinfoResult, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default List<TDanchangCalcinfoResult> select(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectList(this::selectMany, selectList, TDanchangCalcinfoResult, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default List<TDanchangCalcinfoResult> selectDistinct(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectDistinct(this::selectMany, selectList, TDanchangCalcinfoResult, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default Optional<TDanchangCalcinfoResult> selectByPrimaryKey(String id_) {
		return selectOne(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int update(UpdateDSLCompleter completer) {
		return MyBatis3Utils.update(this::update, TDanchangCalcinfoResult, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	static UpdateDSL<UpdateModel> updateAllColumns(TDanchangCalcinfoResult record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalTo(record::getId).set(issue).equalTo(record::getIssue).set(awardType)
				.equalTo(record::getAwardType).set(bdAward).equalTo(record::getBdAward).set(awardNum)
				.equalTo(record::getAwardNum).set(winOdds).equalTo(record::getWinOdds).set(winBetMoney)
				.equalTo(record::getWinBetMoney).set(totalBetMoney).equalTo(record::getTotalBetMoney).set(awardWinMoney)
				.equalTo(record::getAwardWinMoney).set(totalWinMoney).equalTo(record::getTotalWinMoney)
				.set(minOddsInterval).equalTo(record::getMinOddsInterval).set(maxOddsInterval)
				.equalTo(record::getMaxOddsInterval).set(minOddsIntervalRace).equalTo(record::getMinOddsIntervalRace)
				.set(maxOddsIntervalRace).equalTo(record::getMaxOddsIntervalRace).set(playtimeMin)
				.equalTo(record::getPlaytimeMin).set(playtimeMax).equalTo(record::getPlaytimeMax).set(info)
				.equalTo(record::getInfo).set(remark).equalTo(record::getRemark);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	static UpdateDSL<UpdateModel> updateSelectiveColumns(TDanchangCalcinfoResult record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalToWhenPresent(record::getId).set(issue).equalToWhenPresent(record::getIssue)
				.set(awardType).equalToWhenPresent(record::getAwardType).set(bdAward)
				.equalToWhenPresent(record::getBdAward).set(awardNum).equalToWhenPresent(record::getAwardNum)
				.set(winOdds).equalToWhenPresent(record::getWinOdds).set(winBetMoney)
				.equalToWhenPresent(record::getWinBetMoney).set(totalBetMoney)
				.equalToWhenPresent(record::getTotalBetMoney).set(awardWinMoney)
				.equalToWhenPresent(record::getAwardWinMoney).set(totalWinMoney)
				.equalToWhenPresent(record::getTotalWinMoney).set(minOddsInterval)
				.equalToWhenPresent(record::getMinOddsInterval).set(maxOddsInterval)
				.equalToWhenPresent(record::getMaxOddsInterval).set(minOddsIntervalRace)
				.equalToWhenPresent(record::getMinOddsIntervalRace).set(maxOddsIntervalRace)
				.equalToWhenPresent(record::getMaxOddsIntervalRace).set(playtimeMin)
				.equalToWhenPresent(record::getPlaytimeMin).set(playtimeMax).equalToWhenPresent(record::getPlaytimeMax)
				.set(info).equalToWhenPresent(record::getInfo).set(remark).equalToWhenPresent(record::getRemark);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int updateByPrimaryKey(TDanchangCalcinfoResult record) {
		return update(c -> c.set(issue).equalTo(record::getIssue).set(awardType).equalTo(record::getAwardType)
				.set(bdAward).equalTo(record::getBdAward).set(awardNum).equalTo(record::getAwardNum).set(winOdds)
				.equalTo(record::getWinOdds).set(winBetMoney).equalTo(record::getWinBetMoney).set(totalBetMoney)
				.equalTo(record::getTotalBetMoney).set(awardWinMoney).equalTo(record::getAwardWinMoney)
				.set(totalWinMoney).equalTo(record::getTotalWinMoney).set(minOddsInterval)
				.equalTo(record::getMinOddsInterval).set(maxOddsInterval).equalTo(record::getMaxOddsInterval)
				.set(minOddsIntervalRace).equalTo(record::getMinOddsIntervalRace).set(maxOddsIntervalRace)
				.equalTo(record::getMaxOddsIntervalRace).set(playtimeMin).equalTo(record::getPlaytimeMin)
				.set(playtimeMax).equalTo(record::getPlaytimeMax).set(info).equalTo(record::getInfo).set(remark)
				.equalTo(record::getRemark).where(id, isEqualTo(record::getId)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.937+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	default int updateByPrimaryKeySelective(TDanchangCalcinfoResult record) {
		return update(c -> c.set(issue).equalToWhenPresent(record::getIssue).set(awardType)
				.equalToWhenPresent(record::getAwardType).set(bdAward).equalToWhenPresent(record::getBdAward)
				.set(awardNum).equalToWhenPresent(record::getAwardNum).set(winOdds)
				.equalToWhenPresent(record::getWinOdds).set(winBetMoney).equalToWhenPresent(record::getWinBetMoney)
				.set(totalBetMoney).equalToWhenPresent(record::getTotalBetMoney).set(awardWinMoney)
				.equalToWhenPresent(record::getAwardWinMoney).set(totalWinMoney)
				.equalToWhenPresent(record::getTotalWinMoney).set(minOddsInterval)
				.equalToWhenPresent(record::getMinOddsInterval).set(maxOddsInterval)
				.equalToWhenPresent(record::getMaxOddsInterval).set(minOddsIntervalRace)
				.equalToWhenPresent(record::getMinOddsIntervalRace).set(maxOddsIntervalRace)
				.equalToWhenPresent(record::getMaxOddsIntervalRace).set(playtimeMin)
				.equalToWhenPresent(record::getPlaytimeMin).set(playtimeMax).equalToWhenPresent(record::getPlaytimeMax)
				.set(info).equalToWhenPresent(record::getInfo).set(remark).equalToWhenPresent(record::getRemark)
				.where(id, isEqualTo(record::getId)));
	}
}