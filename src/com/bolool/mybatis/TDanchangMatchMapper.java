package com.bolool.mybatis;

import static com.bolool.mybatis.TDanchangMatchDynamicSqlSupport.*;

import com.bolool.model.TDanchangMatch;
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


import static org.mybatis.dynamic.sql.SqlBuilder.*;

@Mapper
public interface TDanchangMatchMapper {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	BasicColumn[] selectList = BasicColumn.columnList(id, matchId, num, issue, leagueName, rq, playtime, home, away,
			matchStatus, halfscore, fullscore, result, sp, sp1, sp2, sp3, w, d, l, h, pan, a, w1, d1, l1);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	long count(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	@DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
	int delete(DeleteStatementProvider deleteStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
	int insert(InsertStatementProvider<TDanchangMatch> insertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	@InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
	int insertMultiple(MultiRowInsertStatementProvider<TDanchangMatch> multipleInsertStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@ResultMap("TDanchangMatchResult")
	Optional<TDanchangMatch> selectOne(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	@SelectProvider(type = SqlProviderAdapter.class, method = "select")
	@Results(id = "TDanchangMatchResult", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR, id = true),
			@Result(column = "match_id", property = "matchId", jdbcType = JdbcType.INTEGER),
			@Result(column = "num", property = "num", jdbcType = JdbcType.INTEGER),
			@Result(column = "issue", property = "issue", jdbcType = JdbcType.VARCHAR),
			@Result(column = "league_name", property = "leagueName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "rq", property = "rq", jdbcType = JdbcType.VARCHAR),
			@Result(column = "playtime", property = "playtime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "home", property = "home", jdbcType = JdbcType.VARCHAR),
			@Result(column = "away", property = "away", jdbcType = JdbcType.VARCHAR),
			@Result(column = "match_status", property = "matchStatus", jdbcType = JdbcType.VARCHAR),
			@Result(column = "halfscore", property = "halfscore", jdbcType = JdbcType.VARCHAR),
			@Result(column = "fullscore", property = "fullscore", jdbcType = JdbcType.VARCHAR),
			@Result(column = "result", property = "result", jdbcType = JdbcType.VARCHAR),
			@Result(column = "sp", property = "sp", jdbcType = JdbcType.DECIMAL),
			@Result(column = "sp1", property = "sp1", jdbcType = JdbcType.DECIMAL),
			@Result(column = "sp2", property = "sp2", jdbcType = JdbcType.DECIMAL),
			@Result(column = "sp3", property = "sp3", jdbcType = JdbcType.DECIMAL),
			@Result(column = "w", property = "w", jdbcType = JdbcType.DECIMAL),
			@Result(column = "d", property = "d", jdbcType = JdbcType.DECIMAL),
			@Result(column = "l", property = "l", jdbcType = JdbcType.DECIMAL),
			@Result(column = "h", property = "h", jdbcType = JdbcType.DECIMAL),
			@Result(column = "pan", property = "pan", jdbcType = JdbcType.VARCHAR),
			@Result(column = "a", property = "a", jdbcType = JdbcType.DECIMAL),
			@Result(column = "w1", property = "w1", jdbcType = JdbcType.DECIMAL),
			@Result(column = "d1", property = "d1", jdbcType = JdbcType.DECIMAL),
			@Result(column = "l1", property = "l1", jdbcType = JdbcType.DECIMAL) })
	List<TDanchangMatch> selectMany(SelectStatementProvider selectStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	@UpdateProvider(type = SqlProviderAdapter.class, method = "update")
	int update(UpdateStatementProvider updateStatement);

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default long count(CountDSLCompleter completer) {
		return MyBatis3Utils.countFrom(this::count, TDanchangMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default int delete(DeleteDSLCompleter completer) {
		return MyBatis3Utils.deleteFrom(this::delete, TDanchangMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default int deleteByPrimaryKey(String id_) {
		return delete(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default int insert(TDanchangMatch record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangMatch,
				c -> c.map(id).toProperty("id").map(matchId).toProperty("matchId").map(num).toProperty("num").map(issue)
						.toProperty("issue").map(leagueName).toProperty("leagueName").map(rq).toProperty("rq")
						.map(playtime).toProperty("playtime").map(home).toProperty("home").map(away).toProperty("away")
						.map(matchStatus).toProperty("matchStatus").map(halfscore).toProperty("halfscore")
						.map(fullscore).toProperty("fullscore").map(result).toProperty("result").map(sp)
						.toProperty("sp").map(sp1).toProperty("sp1").map(sp2).toProperty("sp2").map(sp3)
						.toProperty("sp3").map(w).toProperty("w").map(d).toProperty("d").map(l).toProperty("l").map(h)
						.toProperty("h").map(pan).toProperty("pan").map(a).toProperty("a").map(w1).toProperty("w1")
						.map(d1).toProperty("d1").map(l1).toProperty("l1"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default int insertMultiple(Collection<TDanchangMatch> records) {
		return MyBatis3Utils.insertMultiple(this::insertMultiple, records, TDanchangMatch,
				c -> c.map(id).toProperty("id").map(matchId).toProperty("matchId").map(num).toProperty("num").map(issue)
						.toProperty("issue").map(leagueName).toProperty("leagueName").map(rq).toProperty("rq")
						.map(playtime).toProperty("playtime").map(home).toProperty("home").map(away).toProperty("away")
						.map(matchStatus).toProperty("matchStatus").map(halfscore).toProperty("halfscore")
						.map(fullscore).toProperty("fullscore").map(result).toProperty("result").map(sp)
						.toProperty("sp").map(sp1).toProperty("sp1").map(sp2).toProperty("sp2").map(sp3)
						.toProperty("sp3").map(w).toProperty("w").map(d).toProperty("d").map(l).toProperty("l").map(h)
						.toProperty("h").map(pan).toProperty("pan").map(a).toProperty("a").map(w1).toProperty("w1")
						.map(d1).toProperty("d1").map(l1).toProperty("l1"));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default int insertSelective(TDanchangMatch record) {
		return MyBatis3Utils.insert(this::insert, record, TDanchangMatch, c -> c.map(id)
				.toPropertyWhenPresent("id", record::getId).map(matchId)
				.toPropertyWhenPresent("matchId", record::getMatchId).map(num)
				.toPropertyWhenPresent("num", record::getNum).map(issue)
				.toPropertyWhenPresent("issue", record::getIssue).map(leagueName)
				.toPropertyWhenPresent("leagueName", record::getLeagueName).map(rq)
				.toPropertyWhenPresent("rq", record::getRq).map(playtime)
				.toPropertyWhenPresent("playtime", record::getPlaytime).map(home)
				.toPropertyWhenPresent("home", record::getHome).map(away).toPropertyWhenPresent("away", record::getAway)
				.map(matchStatus).toPropertyWhenPresent("matchStatus", record::getMatchStatus).map(halfscore)
				.toPropertyWhenPresent("halfscore", record::getHalfscore).map(fullscore)
				.toPropertyWhenPresent("fullscore", record::getFullscore).map(result)
				.toPropertyWhenPresent("result", record::getResult).map(sp).toPropertyWhenPresent("sp", record::getSp)
				.map(sp1).toPropertyWhenPresent("sp1", record::getSp1).map(sp2)
				.toPropertyWhenPresent("sp2", record::getSp2).map(sp3).toPropertyWhenPresent("sp3", record::getSp3)
				.map(w).toPropertyWhenPresent("w", record::getW).map(d).toPropertyWhenPresent("d", record::getD).map(l)
				.toPropertyWhenPresent("l", record::getL).map(h).toPropertyWhenPresent("h", record::getH).map(pan)
				.toPropertyWhenPresent("pan", record::getPan).map(a).toPropertyWhenPresent("a", record::getA).map(w1)
				.toPropertyWhenPresent("w1", record::getW1).map(d1).toPropertyWhenPresent("d1", record::getD1).map(l1)
				.toPropertyWhenPresent("l1", record::getL1));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default Optional<TDanchangMatch> selectOne(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectOne(this::selectOne, selectList, TDanchangMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.209+08:00", comments = "Source Table: t_danchang_match")
	default List<TDanchangMatch> select(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectList(this::selectMany, selectList, TDanchangMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source Table: t_danchang_match")
	default List<TDanchangMatch> selectDistinct(SelectDSLCompleter completer) {
		return MyBatis3Utils.selectDistinct(this::selectMany, selectList, TDanchangMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source Table: t_danchang_match")
	default Optional<TDanchangMatch> selectByPrimaryKey(String id_) {
		return selectOne(c -> c.where(id, isEqualTo(id_)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source Table: t_danchang_match")
	default int update(UpdateDSLCompleter completer) {
		return MyBatis3Utils.update(this::update, TDanchangMatch, completer);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source Table: t_danchang_match")
	static UpdateDSL<UpdateModel> updateAllColumns(TDanchangMatch record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalTo(record::getId).set(matchId).equalTo(record::getMatchId).set(num)
				.equalTo(record::getNum).set(issue).equalTo(record::getIssue).set(leagueName)
				.equalTo(record::getLeagueName).set(rq).equalTo(record::getRq).set(playtime)
				.equalTo(record::getPlaytime).set(home).equalTo(record::getHome).set(away).equalTo(record::getAway)
				.set(matchStatus).equalTo(record::getMatchStatus).set(halfscore).equalTo(record::getHalfscore)
				.set(fullscore).equalTo(record::getFullscore).set(result).equalTo(record::getResult).set(sp)
				.equalTo(record::getSp).set(sp1).equalTo(record::getSp1).set(sp2).equalTo(record::getSp2).set(sp3)
				.equalTo(record::getSp3).set(w).equalTo(record::getW).set(d).equalTo(record::getD).set(l)
				.equalTo(record::getL).set(h).equalTo(record::getH).set(pan).equalTo(record::getPan).set(a)
				.equalTo(record::getA).set(w1).equalTo(record::getW1).set(d1).equalTo(record::getD1).set(l1)
				.equalTo(record::getL1);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source Table: t_danchang_match")
	static UpdateDSL<UpdateModel> updateSelectiveColumns(TDanchangMatch record, UpdateDSL<UpdateModel> dsl) {
		return dsl.set(id).equalToWhenPresent(record::getId).set(matchId).equalToWhenPresent(record::getMatchId)
				.set(num).equalToWhenPresent(record::getNum).set(issue).equalToWhenPresent(record::getIssue)
				.set(leagueName).equalToWhenPresent(record::getLeagueName).set(rq).equalToWhenPresent(record::getRq)
				.set(playtime).equalToWhenPresent(record::getPlaytime).set(home).equalToWhenPresent(record::getHome)
				.set(away).equalToWhenPresent(record::getAway).set(matchStatus)
				.equalToWhenPresent(record::getMatchStatus).set(halfscore).equalToWhenPresent(record::getHalfscore)
				.set(fullscore).equalToWhenPresent(record::getFullscore).set(result)
				.equalToWhenPresent(record::getResult).set(sp).equalToWhenPresent(record::getSp).set(sp1)
				.equalToWhenPresent(record::getSp1).set(sp2).equalToWhenPresent(record::getSp2).set(sp3)
				.equalToWhenPresent(record::getSp3).set(w).equalToWhenPresent(record::getW).set(d)
				.equalToWhenPresent(record::getD).set(l).equalToWhenPresent(record::getL).set(h)
				.equalToWhenPresent(record::getH).set(pan).equalToWhenPresent(record::getPan).set(a)
				.equalToWhenPresent(record::getA).set(w1).equalToWhenPresent(record::getW1).set(d1)
				.equalToWhenPresent(record::getD1).set(l1).equalToWhenPresent(record::getL1);
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source Table: t_danchang_match")
	default int updateByPrimaryKey(TDanchangMatch record) {
		return update(c -> c.set(matchId).equalTo(record::getMatchId).set(num).equalTo(record::getNum).set(issue)
				.equalTo(record::getIssue).set(leagueName).equalTo(record::getLeagueName).set(rq).equalTo(record::getRq)
				.set(playtime).equalTo(record::getPlaytime).set(home).equalTo(record::getHome).set(away)
				.equalTo(record::getAway).set(matchStatus).equalTo(record::getMatchStatus).set(halfscore)
				.equalTo(record::getHalfscore).set(fullscore).equalTo(record::getFullscore).set(result)
				.equalTo(record::getResult).set(sp).equalTo(record::getSp).set(sp1).equalTo(record::getSp1).set(sp2)
				.equalTo(record::getSp2).set(sp3).equalTo(record::getSp3).set(w).equalTo(record::getW).set(d)
				.equalTo(record::getD).set(l).equalTo(record::getL).set(h).equalTo(record::getH).set(pan)
				.equalTo(record::getPan).set(a).equalTo(record::getA).set(w1).equalTo(record::getW1).set(d1)
				.equalTo(record::getD1).set(l1).equalTo(record::getL1).where(id, isEqualTo(record::getId)));
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source Table: t_danchang_match")
	default int updateByPrimaryKeySelective(TDanchangMatch record) {
		return update(c -> c.set(matchId).equalToWhenPresent(record::getMatchId).set(num)
				.equalToWhenPresent(record::getNum).set(issue).equalToWhenPresent(record::getIssue).set(leagueName)
				.equalToWhenPresent(record::getLeagueName).set(rq).equalToWhenPresent(record::getRq).set(playtime)
				.equalToWhenPresent(record::getPlaytime).set(home).equalToWhenPresent(record::getHome).set(away)
				.equalToWhenPresent(record::getAway).set(matchStatus).equalToWhenPresent(record::getMatchStatus)
				.set(halfscore).equalToWhenPresent(record::getHalfscore).set(fullscore)
				.equalToWhenPresent(record::getFullscore).set(result).equalToWhenPresent(record::getResult).set(sp)
				.equalToWhenPresent(record::getSp).set(sp1).equalToWhenPresent(record::getSp1).set(sp2)
				.equalToWhenPresent(record::getSp2).set(sp3).equalToWhenPresent(record::getSp3).set(w)
				.equalToWhenPresent(record::getW).set(d).equalToWhenPresent(record::getD).set(l)
				.equalToWhenPresent(record::getL).set(h).equalToWhenPresent(record::getH).set(pan)
				.equalToWhenPresent(record::getPan).set(a).equalToWhenPresent(record::getA).set(w1)
				.equalToWhenPresent(record::getW1).set(d1).equalToWhenPresent(record::getD1).set(l1)
				.equalToWhenPresent(record::getL1).where(id, isEqualTo(record::getId)));
	}
}