package com.bolool.mybatis;

import static com.bolool.mybatis.TDanchangCalcinfoMatchDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import com.bolool.model.TDanchangCalcinfoMatch;


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
import org.apache.ibatis.annotations.SelectKey;
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
public interface TDanchangCalcinfoMatchMapper {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	BasicColumn[] selectList = BasicColumn.columnList(id, calcinfoId, danchangId);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	long count(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	@DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
	int delete(DeleteStatementProvider deleteStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "record.id", before = false, resultType = Integer.class)
	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
	int insert(InsertStatementProvider<TDanchangCalcinfoMatch> insertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
	int insertMultiple(MultiRowInsertStatementProvider<TDanchangCalcinfoMatch> multipleInsertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@ResultMap("TDanchangCalcinfoMatchResult")
	Optional<TDanchangCalcinfoMatch> selectOne(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@Results(id = "TDanchangCalcinfoMatchResult", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR, id = true),
			@Result(column = "calcinfo_id", property = "calcinfoId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "danchang_id", property = "danchangId", jdbcType = JdbcType.VARCHAR) })
	List<TDanchangCalcinfoMatch> selectMany(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	@UpdateProvider(type = SqlProviderAdapter.class, method = "update")
	int update(UpdateStatementProvider updateStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default long count(CountDSLCompleter completer) {
		return MyBatis3Utils.countFrom(this::count, TDanchangCalcinfoMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int delete(DeleteDSLCompleter completer) {
		return MyBatis3Utils.deleteFrom(this::delete, TDanchangCalcinfoMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int deleteByPrimaryKey(String id_) {
		return delete(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int insert(TDanchangCalcinfoMatch record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfoMatch, c -> c.map(id).toProperty("id")
				.map(calcinfoId).toProperty("calcinfoId").map(danchangId).toProperty("danchangId"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int insertMultiple(Collection<TDanchangCalcinfoMatch> records) {
		return MyBatis3Utils.insertMultiple(this::insertMultiple, records, TDanchangCalcinfoMatch, c -> c.map(id)
				.toProperty("id").map(calcinfoId).toProperty("calcinfoId").map(danchangId).toProperty("danchangId"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int insertSelective(TDanchangCalcinfoMatch record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangCalcinfoMatch,
				c -> c.map(id).toPropertyWhenPresent("id", record::getId).map(calcinfoId)
						.toPropertyWhenPresent("calcinfoId", record::getCalcinfoId).map(danchangId)
						.toPropertyWhenPresent("danchangId", record::getDanchangId));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default Optional<TDanchangCalcinfoMatch> selectOne(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectOne(this::selectOne, selectList, TDanchangCalcinfoMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default List<TDanchangCalcinfoMatch> select(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectList(this::selectMany, selectList, TDanchangCalcinfoMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default List<TDanchangCalcinfoMatch> selectDistinct(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectDistinct(this::selectMany, selectList, TDanchangCalcinfoMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default Optional<TDanchangCalcinfoMatch> selectByPrimaryKey(String id_) {
		return selectOne(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int update(UpdateDSLCompleter completer) {
		return MyBatis3Utils.update(this::update, TDanchangCalcinfoMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	static UpdateDSL<UpdateModel> updateAllColumns(TDanchangCalcinfoMatch record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalTo(record::getId).set(calcinfoId).equalTo(record::getCalcinfoId).set(danchangId)
				.equalTo(record::getDanchangId);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	static UpdateDSL<UpdateModel> updateSelectiveColumns(TDanchangCalcinfoMatch record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalToWhenPresent(record::getId).set(calcinfoId).equalToWhenPresent(record::getCalcinfoId)
				.set(danchangId).equalToWhenPresent(record::getDanchangId);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int updateByPrimaryKey(TDanchangCalcinfoMatch record) {
		return update(c -> c.set(calcinfoId).equalTo(record::getCalcinfoId).set(danchangId)
				.equalTo(record::getDanchangId).where(id, isEqualTo(record::getId)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	default int updateByPrimaryKeySelective(TDanchangCalcinfoMatch record) {
		return update(c -> c.set(calcinfoId).equalToWhenPresent(record::getCalcinfoId).set(danchangId)
				.equalToWhenPresent(record::getDanchangId).where(id, isEqualTo(record::getId)));
	}
}