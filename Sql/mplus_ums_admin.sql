/*
 Navicat Premium Data Transfer

 Source Server         : docker service nacos
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 192.168.229.150:3306
 Source Schema         : mplus_ums_admin

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 16/03/2021 14:35:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `log_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `request_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `browser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `log_create_time_index`(`create_time`) USING BTREE,
  INDEX `inx_log_type`(`log_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3547 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (3546, '更改个人信息', 'INFO', 'com.kimi.myshop.plus.business.controller.ProfileController.update()', '{\"note\":\"超级管理员\",\"nickName\":\"郭富城\",\"icon\":\"https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/434f4027-5b6d-4454-a0b0-800a321406ec.png\",\"loginTime\":1615874697000,\"createTime\":1538976767000,\"id\":3,\"email\":\"diaxiahu@qq.com\",\"username\":\"admin\",\"status\":1}', '0:0:0:0:0:0:0:1', 33271, '', '广东省广州市 广州大学', 'Chrome 8', NULL, '2021-03-16 14:07:03');
INSERT INTO `sys_log` VALUES (3547, '更改个人信息', 'INFO', 'com.kimi.myshop.plus.business.controller.ProfileController.update()', '{\"note\":\"管理员\",\"nickName\":\"郭富城\",\"icon\":\"https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/434f4027-5b6d-4454-a0b0-800a321406ec.png\",\"loginTime\":1615874697000,\"createTime\":1538976767000,\"id\":3,\"email\":\"diaxiahu@qq.com\",\"username\":\"admin\",\"status\":1}', '0:0:0:0:0:0:0:1', 15573, '', '广东省广州市 广州大学', 'Chrome 8', NULL, '2021-03-16 14:17:32');
INSERT INTO `sys_log` VALUES (3548, '更改个人信息', 'INFO', 'com.kimi.myshop.plus.business.controller.ProfileController.update()', '{\"note\":\"超级管理员\",\"nickName\":\"郭富城\",\"icon\":\"https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/434f4027-5b6d-4454-a0b0-800a321406ec.png\",\"loginTime\":1615874697000,\"createTime\":1538976767000,\"id\":3,\"email\":\"diaxiahu@qq.com\",\"username\":\"admin\",\"status\":1}', '0:0:0:0:0:0:0:1', 3356, '', '广东省广州市 广州大学', 'Chrome 8', NULL, '2021-03-16 14:19:56');
INSERT INTO `sys_log` VALUES (3549, '更改个人信息', 'INFO', 'com.kimi.myshop.plus.business.controller.ProfileController.update()', '{\"note\":\"管理员\",\"nickName\":\"郭富城\",\"icon\":\"https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/434f4027-5b6d-4454-a0b0-800a321406ec.png\",\"loginTime\":1615874697000,\"createTime\":1538976767000,\"id\":3,\"email\":\"diaxiahu@qq.com\",\"username\":\"admin\",\"status\":1}', '0:0:0:0:0:0:0:1', 26504, '', '广东省广州市 广州大学', 'Chrome 8', NULL, '2021-03-16 14:21:12');
INSERT INTO `sys_log` VALUES (3550, '更改个人信息', 'INFO', 'com.kimi.myshop.plus.business.controller.ProfileController.update()', '{\"note\":\"超级管理员\",\"nickName\":\"郭富城\",\"icon\":\"https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/434f4027-5b6d-4454-a0b0-800a321406ec.png\",\"loginTime\":1615874697000,\"createTime\":1538976767000,\"id\":3,\"email\":\"diaxiahu@qq.com\",\"username\":\"admin\",\"status\":1}', '0:0:0:0:0:0:0:1', 978, '', '广东省广州市 广州大学', 'Chrome 8', NULL, '2021-03-16 14:23:11');
INSERT INTO `sys_log` VALUES (3551, '更改个人信息', 'INFO', 'com.kimi.myshop.plus.business.controller.ProfileController.update()', '{\"note\":\"管理员\",\"nickName\":\"郭富城\",\"icon\":\"https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/434f4027-5b6d-4454-a0b0-800a321406ec.png\",\"loginTime\":1615874697000,\"createTime\":1538976767000,\"id\":3,\"email\":\"diaxiahu@qq.com\",\"username\":\"admin\",\"status\":1}', '0:0:0:0:0:0:0:1', 7057, '', '广东省广州市 广州大学', 'Chrome 8', NULL, '2021-03-16 14:26:28');

-- ----------------------------
-- Table structure for ums_admin
-- ----------------------------
DROP TABLE IF EXISTS `ums_admin`;
CREATE TABLE `ums_admin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icon` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` int(1) NULL DEFAULT 1 COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1330115617470132227 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_admin
-- ----------------------------
INSERT INTO `ums_admin` VALUES (3, 'admin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', 'https://data-repository-1304879813.cos.ap-guangzhou.myqcloud.com/434f4027-5b6d-4454-a0b0-800a321406ec.png', 'diaxiahu@qq.com', '郭富城', '管理员', '2018-10-08 13:32:47', '2021-03-16 14:04:57', 1);
INSERT INTO `ums_admin` VALUES (5, 'kimi', '$2a$10$FI7/HQUsO9rDwB5uVKkfDO9AjLeyP5sDAJeKDUPPttjO1pleFQY5e', '', '', '', '', '2020-11-21 13:24:47', '2020-11-21 13:24:47', 0);
INSERT INTO `ums_admin` VALUES (7, 'dddddd', '$2a$10$6jIy7B81vH7retSKFkoGDeHHHuviY3oecmCSJbw3Qwb0XzFktvBha', NULL, NULL, NULL, NULL, '2020-11-21 19:30:36', '2020-11-21 19:30:36', 0);

SET FOREIGN_KEY_CHECKS = 1;
