package com.bolool.mybatis;

import static com.bolool.mybatis.TDanchangCalcinfoDynamicSqlSupport.*;

import com.bolool.model.TDanchangCalcinfo;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;
import java.util.Collection;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;


import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlBuilder;

@Mapper
public interface TDanchangCalcinfoMapper {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	BasicColumn[] selectList = BasicColumn.columnList(id, info, issue, minOdds, status, count, totalCount);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	long count(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	@DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
	int delete(DeleteStatementProvider deleteStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "record.id", before = false, resultType = Integer.class)
	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
	int insert(InsertStatementProvider<TDanchangCalcinfo> insertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
	int insertMultiple(MultiRowInsertStatementProvider<TDanchangCalcinfo> multipleInsertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@ResultMap("TDanchangCalcinfoResult")
	Optional<TDanchangCalcinfo> selectOne(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@Results(id = "TDanchangCalcinfoResult", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR, id = true),
			@Result(column = "info", property = "info", jdbcType = JdbcType.VARCHAR),
			@Result(column = "issue", property = "issue", jdbcType = JdbcType.VARCHAR),
			@Result(column = "min_odds", property = "minOdds", jdbcType = JdbcType.DECIMAL),
			@Result(column = "status", property = "status", jdbcType = JdbcType.VARCHAR),
			@Result(column = "count", property = "count", jdbcType = JdbcType.INTEGER),
			@Result(column = "total_count", property = "totalCount", jdbcType = JdbcType.INTEGER) })
	List<TDanchangCalcinfo> selectMany(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	@UpdateProvider(type = SqlProviderAdapter.class, method = "update")
	int update(UpdateStatementProvider updateStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default long count(CountDSLCompleter completer) {
		return MyBatis3Utils.countFrom(this::count, TDanchangCalcinfo, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int delete(DeleteDSLCompleter completer) {
		return MyBatis3Utils.deleteFrom(this::delete, TDanchangCalcinfo, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int deleteByPrimaryKey(String id_) {
		return delete(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int insert(TDanchangCalcinfo record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfo,
				c -> c.map(id).toProperty("id").map(info).toProperty("info").map(issue).toProperty("issue").map(minOdds)
						.toProperty("minOdds").map(status).toProperty("status").map(count).toProperty("count")
						.map(totalCount).toProperty("totalCount"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int insertMultiple(Collection<TDanchangCalcinfo> records) {
		return MyBatis3Utils.insertMultiple(this::insertMultiple, records, TDanchangCalcinfo,
				c -> c.map(id).toProperty("id").map(info).toProperty("info").map(issue).toProperty("issue").map(minOdds)
						.toProperty("minOdds").map(status).toProperty("status").map(count).toProperty("count")
						.map(totalCount).toProperty("totalCount"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int insertSelective(TDanchangCalcinfo record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfo,
				c -> c.map(id).toPropertyWhenPresent("id", record::getId).map(info)
						.toPropertyWhenPresent("info", record::getInfo).map(issue)
						.toPropertyWhenPresent("issue", record::getIssue).map(minOdds)
						.toPropertyWhenPresent("minOdds", record::getMinOdds).map(status)
						.toPropertyWhenPresent("status", record::getStatus).map(count)
						.toPropertyWhenPresent("count", record::getCount).map(totalCount)
						.toPropertyWhenPresent("totalCount", record::getTotalCount));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default Optional<TDanchangCalcinfo> selectOne(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectOne(this::selectOne, selectList, TDanchangCalcinfo, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default List<TDanchangCalcinfo> select(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectList(this::selectMany, selectList, TDanchangCalcinfo, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default List<TDanchangCalcinfo> selectDistinct(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectDistinct(this::selectMany, selectList, TDanchangCalcinfo, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default Optional<TDanchangCalcinfo> selectByPrimaryKey(String id_) {
		return selectOne(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int update(UpdateDSLCompleter completer) {
		return MyBatis3Utils.update(this::update, TDanchangCalcinfo, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	static UpdateDSL<UpdateModel> updateAllColumns(TDanchangCalcinfo record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalTo(record::getId).set(info).equalTo(record::getInfo).set(issue)
				.equalTo(record::getIssue).set(minOdds).equalTo(record::getMinOdds).set(status)
				.equalTo(record::getStatus).set(count).equalTo(record::getCount).set(totalCount)
				.equalTo(record::getTotalCount);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	static UpdateDSL<UpdateModel> updateSelectiveColumns(TDanchangCalcinfo record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalToWhenPresent(record::getId).set(info).equalToWhenPresent(record::getInfo).set(issue)
				.equalToWhenPresent(record::getIssue).set(minOdds).equalToWhenPresent(record::getMinOdds).set(status)
				.equalToWhenPresent(record::getStatus).set(count).equalToWhenPresent(record::getCount).set(totalCount)
				.equalToWhenPresent(record::getTotalCount);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int updateByPrimaryKey(TDanchangCalcinfo record) {
		return update(c -> c.set(info).equalTo(record::getInfo).set(issue).equalTo(record::getIssue).set(minOdds)
				.equalTo(record::getMinOdds).set(status).equalTo(record::getStatus).set(count).equalTo(record::getCount)
				.set(totalCount).equalTo(record::getTotalCount).where(id, isEqualTo(record::getId)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source Table: t_danchang_calcinfo")
	default int updateByPrimaryKeySelective(TDanchangCalcinfo record) {
		return update(c -> c.set(info).equalToWhenPresent(record::getInfo).set(issue)
				.equalToWhenPresent(record::getIssue).set(minOdds).equalToWhenPresent(record::getMinOdds).set(status)
				.equalToWhenPresent(record::getStatus).set(count).equalToWhenPresent(record::getCount).set(totalCount)
				.equalToWhenPresent(record::getTotalCount).where(id, isEqualTo(record::getId)));
	}
}