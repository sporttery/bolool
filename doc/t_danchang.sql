/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : bolool

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 16/07/2021 15:08:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_danchang_calcinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_danchang_calcinfo`;
CREATE TABLE `t_danchang_calcinfo`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息',
  `issue` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '期号',
  `min_odds` decimal(8, 2) NOT NULL COMMENT '最小的赔率',
  `status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'OK or FAIL',
  `count` int(11) NOT NULL COMMENT '满足比赛的场次',
  `total_count` int(11) NULL DEFAULT NULL COMMENT '当期总场次',
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '北单赛程按期筛选满足条件的期号' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_danchang_calcinfo_match
-- ----------------------------
DROP TABLE IF EXISTS `t_danchang_calcinfo_match`;
CREATE TABLE `t_danchang_calcinfo_match`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calcinfo_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联的计算id',
  `danchang_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联的比赛id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_danchang_calcinfo_result
-- ----------------------------
DROP TABLE IF EXISTS `t_danchang_calcinfo_result`;
CREATE TABLE `t_danchang_calcinfo_result`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `issue` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期号',
  `award_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '北单中奖还是澳博中奖',
  `bd_award` decimal(10, 2) NULL DEFAULT NULL COMMENT '北单奖金',
  `award_num` int(11) NULL DEFAULT NULL COMMENT '中奖那单第几场',
  `win_odds` decimal(8, 4) NULL DEFAULT NULL COMMENT '中奖那单赔率',
  `win_bet_money` int(11) NULL DEFAULT NULL COMMENT '中奖那单投注金额',
  `total_bet_money` int(11) NULL DEFAULT NULL COMMENT '总投注金额',
  `award_win_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '奖金盈利',
  `total_win_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '总盈利金额',
  `min_odds_interval` decimal(8, 4) NULL DEFAULT NULL COMMENT '最小赔率波动',
  `max_odds_interval` decimal(8, 4) NULL DEFAULT NULL COMMENT '最大赔率波动',
  `min_odds_interval_race` decimal(10, 6) NULL DEFAULT NULL COMMENT '最小赔率波动比例',
  `max_odds_interval_race` decimal(10, 6) NULL DEFAULT NULL COMMENT '最大赔率波动比例',
  `playtime_min` datetime NULL DEFAULT NULL COMMENT '最小时间',
  `playtime_max` datetime NULL DEFAULT NULL COMMENT '最大时间',
  `info` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '信息',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_danchang_calcinfo_result_match
-- ----------------------------
DROP TABLE IF EXISTS `t_danchang_calcinfo_result_match`;
CREATE TABLE `t_danchang_calcinfo_result_match`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calcinfo_result_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的calcinfo_result_id',
  `danchang_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联的比赛id',
  `bet_odds` decimal(8, 4) NULL DEFAULT NULL COMMENT '投注赔率',
  `bet_money` int(11) NULL DEFAULT NULL COMMENT '投注金额',
  `award_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否中奖',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_danchang_match
-- ----------------------------
DROP TABLE IF EXISTS `t_danchang_match`;
CREATE TABLE `t_danchang_match`  (
  `id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `match_id` int(11) NOT NULL COMMENT '比赛id',
  `num` int(11) NULL DEFAULT NULL COMMENT '序号',
  `issue` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期号',
  `league_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赛事',
  `rq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '让球',
  `playtime` datetime NULL DEFAULT NULL COMMENT '比赛时间',
  `home` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主队',
  `away` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客队',
  `match_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 0 未开始 -1 比赛结束',
  `halfscore` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '半场比分',
  `fullscore` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '全场比分',
  `result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '比赛结果',
  `sp` decimal(8, 4) NULL DEFAULT NULL COMMENT '开奖sp',
  `sp1` decimal(8, 4) NULL DEFAULT NULL COMMENT '参考sp',
  `sp2` decimal(8, 4) NULL DEFAULT NULL COMMENT '参考sp',
  `sp3` decimal(8, 4) NULL DEFAULT NULL COMMENT '参考sp',
  `w` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门胜 初盘',
  `d` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门平 初盘',
  `l` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门负 初盘',
  `h` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门主',
  `pan` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '澳门盘口',
  `a` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门客',
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '插入时间',
  `w1` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门胜 即时盘',
  `d1` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门平 即时盘',
  `l1` decimal(8, 2) NULL DEFAULT NULL COMMENT '澳门负 即时盘',
  `odds` decimal(8, 2) NULL DEFAULT NULL COMMENT '对冲的赔率',
  `return_race` decimal(8, 3) NULL DEFAULT NULL COMMENT '返还率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '北单赛程表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
