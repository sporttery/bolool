package com.bolool.mybatis;

import static com.bolool.mybatis.TDanchangCalcinfoResultMatchDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.bolool.model.TDanchangCalcinfoResultMatch;
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
public interface TDanchangCalcinfoResultMatchMapper {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	BasicColumn[] selectList = BasicColumn.columnList(id, calcinfoResultId, danchangId, betOdds, betMoney, awardStatus);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	long count(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	@DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
	int delete(DeleteStatementProvider deleteStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
	int insert(InsertStatementProvider<TDanchangCalcinfoResultMatch> insertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
	int insertMultiple(MultiRowInsertStatementProvider<TDanchangCalcinfoResultMatch> multipleInsertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@ResultMap("TDanchangCalcinfoResultMatchResult")
	Optional<TDanchangCalcinfoResultMatch> selectOne(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@Results(id = "TDanchangCalcinfoResultMatchResult", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR, id = true),
			@Result(column = "calcinfo_result_id", property = "calcinfoResultId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "danchang_id", property = "danchangId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "bet_odds", property = "betOdds", jdbcType = JdbcType.DECIMAL),
			@Result(column = "bet_money", property = "betMoney", jdbcType = JdbcType.INTEGER),
			@Result(column = "award_status", property = "awardStatus", jdbcType = JdbcType.VARCHAR) })
	List<TDanchangCalcinfoResultMatch> selectMany(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	@UpdateProvider(type = SqlProviderAdapter.class, method = "update")
	int update(UpdateStatementProvider updateStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default long count(CountDSLCompleter completer) {
		return MyBatis3Utils.countFrom(this::count, TDanchangCalcinfoResultMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int delete(DeleteDSLCompleter completer) {
		return MyBatis3Utils.deleteFrom(this::delete, TDanchangCalcinfoResultMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int deleteByPrimaryKey(String id_) {
		return delete(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int insert(TDanchangCalcinfoResultMatch record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfoResultMatch,
				c -> c.map(id).toProperty("id").map(calcinfoResultId).toProperty("calcinfoResultId").map(danchangId)
						.toProperty("danchangId").map(betOdds).toProperty("betOdds").map(betMoney)
						.toProperty("betMoney").map(awardStatus).toProperty("awardStatus"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int insertMultiple(Collection<TDanchangCalcinfoResultMatch> records) {
		return MyBatis3Utils.insertMultiple(this::insertMultiple, records, TDanchangCalcinfoResultMatch,
				c -> c.map(id).toProperty("id").map(calcinfoResultId).toProperty("calcinfoResultId").map(danchangId)
						.toProperty("danchangId").map(betOdds).toProperty("betOdds").map(betMoney)
						.toProperty("betMoney").map(awardStatus).toProperty("awardStatus"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int insertSelective(TDanchangCalcinfoResultMatch record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfoResultMatch,
				c -> c.map(id).toPropertyWhenPresent("id", record::getId).map(calcinfoResultId)
						.toPropertyWhenPresent("calcinfoResultId", record::getCalcinfoResultId).map(danchangId)
						.toPropertyWhenPresent("danchangId", record::getDanchangId).map(betOdds)
						.toPropertyWhenPresent("betOdds", record::getBetOdds).map(betMoney)
						.toPropertyWhenPresent("betMoney", record::getBetMoney).map(awardStatus)
						.toPropertyWhenPresent("awardStatus", record::getAwardStatus));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default Optional<TDanchangCalcinfoResultMatch> selectOne(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectOne(this::selectOne, selectList, TDanchangCalcinfoResultMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default List<TDanchangCalcinfoResultMatch> select(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectList(this::selectMany, selectList, TDanchangCalcinfoResultMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default List<TDanchangCalcinfoResultMatch> selectDistinct(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectDistinct(this::selectMany, selectList, TDanchangCalcinfoResultMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default Optional<TDanchangCalcinfoResultMatch> selectByPrimaryKey(String id_) {
		return selectOne(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int update(UpdateDSLCompleter completer) {
		return MyBatis3Utils.update(this::update, TDanchangCalcinfoResultMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	static UpdateDSL<UpdateModel> updateAllColumns(TDanchangCalcinfoResultMatch record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalTo(record::getId).set(calcinfoResultId).equalTo(record::getCalcinfoResultId)
				.set(danchangId).equalTo(record::getDanchangId).set(betOdds).equalTo(record::getBetOdds).set(betMoney)
				.equalTo(record::getBetMoney).set(awardStatus).equalTo(record::getAwardStatus);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	static UpdateDSL<UpdateModel> updateSelectiveColumns(TDanchangCalcinfoResultMatch record,
			UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalToWhenPresent(record::getId).set(calcinfoResultId)
				.equalToWhenPresent(record::getCalcinfoResultId).set(danchangId)
				.equalToWhenPresent(record::getDanchangId).set(betOdds).equalToWhenPresent(record::getBetOdds)
				.set(betMoney).equalToWhenPresent(record::getBetMoney).set(awardStatus)
				.equalToWhenPresent(record::getAwardStatus);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int updateByPrimaryKey(TDanchangCalcinfoResultMatch record) {
		return update(c -> c.set(calcinfoResultId).equalTo(record::getCalcinfoResultId).set(danchangId)
				.equalTo(record::getDanchangId).set(betOdds).equalTo(record::getBetOdds).set(betMoney)
				.equalTo(record::getBetMoney).set(awardStatus).equalTo(record::getAwardStatus)
				.where(id, isEqualTo(record::getId)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.217+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	default int updateByPrimaryKeySelective(TDanchangCalcinfoResultMatch record) {
		return update(c -> c.set(calcinfoResultId).equalToWhenPresent(record::getCalcinfoResultId).set(danchangId)
				.equalToWhenPresent(record::getDanchangId).set(betOdds).equalToWhenPresent(record::getBetOdds)
				.set(betMoney).equalToWhenPresent(record::getBetMoney).set(awardStatus)
				.equalToWhenPresent(record::getAwardStatus).where(id, isEqualTo(record::getId)));
	}
}