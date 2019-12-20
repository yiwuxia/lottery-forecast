/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50703
 Source Host           : localhost:3306
 Source Schema         : lijin

 Target Server Type    : MySQL
 Target Server Version : 50703
 File Encoding         : 65001

 Date: 20/12/2019 16:47:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_prize_base_info
-- ----------------------------
DROP TABLE IF EXISTS `t_prize_base_info`;
CREATE TABLE `t_prize_base_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运营商：1移动 2联通 3电信',
  `prize_no1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `prize_no2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ICCID',
  `prize_no3` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IMEI',
  `prize_no4` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `prize_no5` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ICCID',
  `prize_no6` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IMEI',
  `prize_no7` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `prize_no8` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ICCID',
  `prize_no9` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IMEI',
  `prize_no10` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `open_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `term_no`(`term_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33207 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基本信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_prize_base_info
-- ----------------------------
INSERT INTO `t_prize_base_info` VALUES (33187, '2', '08', '03', '07', '09', '04', '05', '06', '01', '02', '10', '2019-12-20 14:59:47');
INSERT INTO `t_prize_base_info` VALUES (33188, '3', '09', '04', '06', '05', '10', '08', '03', '02', '01', '07', '2019-12-20 14:59:47');
INSERT INTO `t_prize_base_info` VALUES (33189, '4', '09', '03', '10', '06', '08', '02', '04', '07', '01', '05', '2019-12-20 14:59:47');
INSERT INTO `t_prize_base_info` VALUES (33190, '5', '07', '01', '06', '02', '03', '08', '10', '04', '09', '05', '2019-12-20 14:59:47');
INSERT INTO `t_prize_base_info` VALUES (33191, '6', '03', '01', '09', '05', '08', '04', '02', '07', '06', '10', '2019-12-20 14:59:47');
INSERT INTO `t_prize_base_info` VALUES (33192, '7', '08', '10', '01', '03', '02', '07', '09', '06', '05', '04', '2019-12-20 14:59:47');
INSERT INTO `t_prize_base_info` VALUES (33193, '8', '09', '10', '04', '03', '08', '05', '07', '02', '06', '01', '2019-12-20 14:59:47');
INSERT INTO `t_prize_base_info` VALUES (33194, '9', '06', '01', '03', '07', '09', '04', '05', '10', '02', '08', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33195, '10', '07', '02', '06', '09', '10', '04', '03', '08', '05', '01', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33196, '11', '10', '05', '03', '01', '09', '04', '08', '02', '06', '07', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33197, '12', '04', '05', '10', '08', '03', '06', '09', '07', '02', '01', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33198, '13', '06', '08', '02', '07', '04', '10', '05', '09', '01', '03', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33199, '14', '06', '10', '08', '09', '03', '05', '01', '02', '04', '07', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33200, '15', '05', '03', '10', '01', '07', '02', '08', '09', '04', '06', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33201, '16', '09', '08', '04', '03', '01', '02', '10', '07', '05', '06', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33202, '17', '09', '08', '06', '02', '05', '07', '01', '03', '10', '04', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33203, '18', '03', '01', '10', '06', '02', '09', '04', '05', '07', '08', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33204, '19', '06', '03', '09', '05', '07', '01', '02', '04', '10', '08', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33205, '20', '02', '08', '07', '01', '05', '03', '10', '09', '04', '06', '2019-12-20 14:59:48');
INSERT INTO `t_prize_base_info` VALUES (33206, '21', '02', '08', '01', '05', '03', '06', '10', '09', '04', '07', '2019-12-20 14:59:48');

-- ----------------------------
-- Table structure for t_prize_info_1
-- ----------------------------
DROP TABLE IF EXISTS `t_prize_info_1`;
CREATE TABLE `t_prize_info_1`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运营商：1移动 2联通 3电信',
  `prize_no1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `prize_no2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ICCID',
  `prize_no3` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IMEI',
  `num_region1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布1',
  `num_region2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布2',
  `num_region3` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布3',
  `num_region4` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布4',
  `num_region5` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布5',
  `num_region6` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布6',
  `num_region7` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布7',
  `num_region8` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布8',
  `num_region9` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布9',
  `num_region10` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期码分布10',
  `first_1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_6` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_7` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_8` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_9` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第1位位置',
  `first_10` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_6` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_7` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_8` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_9` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `second_10` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第2位位置',
  `third_1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_6` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_7` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_8` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_9` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `third_10` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第3位位置',
  `open_time` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `term_no`(`term_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33187 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基本走势' ROW_FORMAT = Compact;

-- ----------------------------
-- Function structure for func_split
-- ----------------------------
DROP FUNCTION IF EXISTS `func_split`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `func_split`(f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN
        declare result varchar(255) default '';

        set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));

        return result;

END
;;
delimiter ;

-- ----------------------------
-- Function structure for func_split_TotalLength
-- ----------------------------
DROP FUNCTION IF EXISTS `func_split_TotalLength`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `func_split_TotalLength`(f_string varchar(1000),f_delimiter varchar(5)) RETURNS int(11)
BEGIN

    return 1+(length(f_string) - length(replace(f_string,f_delimiter,'')));

END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
