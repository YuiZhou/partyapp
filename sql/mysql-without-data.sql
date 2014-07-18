-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 2014-07-18 14:00:04
-- 服务器版本： 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `partyapp`
--
CREATE DATABASE IF NOT EXISTS `partyapp` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `partyapp`;

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL COMMENT 'author of the comment',
  `content` varchar(140) CHARACTER SET gb2312 NOT NULL COMMENT '140 character at most',
  PRIMARY KEY (`id`),
  KEY `fk_cmt_usr_idx` (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- 表的结构 `news`
--

DROP TABLE IF EXISTS `news`;
CREATE TABLE IF NOT EXISTS `news` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id for a news. refresh per seminar. int but not bigint',
  `toPartyid` int(10) NOT NULL COMMENT 'the news is to a specific party.0 for all parties.',
  `title` varchar(32) CHARACTER SET gb2312 NOT NULL COMMENT 'the title for a news, cannot be empty',
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'this will be filled automatically',
  `content` text CHARACTER SET gb2312 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- 表的结构 `party`
--

DROP TABLE IF EXISTS `party`;
CREATE TABLE IF NOT EXISTS `party` (
  `partyid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `partyname` varchar(16) CHARACTER SET gb2312 NOT NULL,
  `leader` bigint(20) unsigned DEFAULT NULL,
  `assistant` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`partyid`),
  UNIQUE KEY `partyid` (`partyid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `usrid` bigint(20) unsigned NOT NULL,
  `username` varchar(16) CHARACTER SET gb2312 NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT 'the status of member in the party. 0, 群众; 1, 积极分子; 2, 预备党员; 3, 党员',
  `invoke_date` date DEFAULT NULL,
  `partyid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`usrid`),
  UNIQUE KEY `usrid_UNIQUE` (`usrid`),
  KEY `fk_user_party` (`partyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='user information';

--
-- 触发器 `user`
--
DROP TRIGGER IF EXISTS `pwd_insert`;
DELIMITER //
CREATE TRIGGER `pwd_insert` AFTER INSERT ON `user`
 FOR EACH ROW BEGIN
INSERT INTO `partyapp`.`usr_pwd` (`usrid`, `pwd`) VALUES (NEW.`usrid`, NEW.`usrid`);
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- 表的结构 `usr_pwd`
--

DROP TABLE IF EXISTS `usr_pwd`;
CREATE TABLE IF NOT EXISTS `usr_pwd` (
  `usrid` bigint(20) unsigned NOT NULL COMMENT 'student id or teacher id is the login user id. So it is unique.',
  `pwd` varchar(16) CHARACTER SET gb2312 NOT NULL,
  PRIMARY KEY (`usrid`),
  UNIQUE KEY `usrid` (`usrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='user id and password';

--
-- 限制导出的表
--

--
-- 限制表 `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `fk_cmt_usr` FOREIGN KEY (`userid`) REFERENCES `user` (`usrid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- 限制表 `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_user_party` FOREIGN KEY (`partyid`) REFERENCES `party` (`partyid`);

--
-- 限制表 `usr_pwd`
--
ALTER TABLE `usr_pwd`
  ADD CONSTRAINT `fk_usr` FOREIGN KEY (`usrid`) REFERENCES `user` (`usrid`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
