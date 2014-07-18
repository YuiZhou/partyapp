-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 2014-07-18 13:59:32
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

--
-- 转存表中的数据 `comment`
--

INSERT INTO `comment` (`id`, `userid`, `content`) VALUES
(2, 11212010019, 'hello world'),
(3, 11302010019, '吃了吗，世界'),
(4, 11302010067, '大家好'),
(5, 11302010067, '大家好'),
(6, 11302010067, '大家好我又来了'),
(7, 11302010067, '这是手机上的消息'),
(8, 11302010067, '测试'),
(9, 11302010067, '递减测试'),
(10, 10302010076, '测试一下'),
(11, 11302010019, '我也来测试');

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

--
-- 转存表中的数据 `news`
--

INSERT INTO `news` (`id`, `toPartyid`, `title`, `date`, `content`) VALUES
(1, 0, '大家好', '0000-00-00 00:00:00', '每个党支部都能看到呢'),
(2, 1, '别开枪，自己人', '0000-00-00 00:00:00', '我们是自己人！'),
(3, 0, '叶家杰要回家啦', '2014-07-09 09:56:39', '叶家杰7月11号晚上从浦东飞走啦'),
(4, 0, '10SS滚蛋啦', '2014-07-09 09:57:50', '都没人陪我玩耍了T^T'),
(7, 0, '我在看电影', '2014-07-09 13:45:28', '这个电影太特么清晰了不'),
(8, 1, '今天天气不错', '2014-07-09 13:47:43', '我们来开会吧'),
(9, 0, '今天天气真不错', '2014-07-09 13:48:03', '我们来开会吧');

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

--
-- 转存表中的数据 `party`
--

INSERT INTO `party` (`partyid`, `partyname`, `leader`, `assistant`) VALUES
(1, '软件学院2011级党支部', 11212010019, 11302010019),
(2, '软件学院2010级党支部', NULL, NULL);

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
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`usrid`, `username`, `status`, `invoke_date`, `partyid`) VALUES
(10302010076, '刘斐敏', 2, '2013-10-18', 2),
(11212010019, '何柯君', 3, '2009-03-01', 1),
(11302010019, '徐琛杰', 3, '2012-10-01', 1),
(11302010059, '何文琦', 0, NULL, 1),
(11302010064, '刘芳', 0, NULL, 1),
(11302010067, '周予维', 3, '2012-04-01', 1);

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
-- 转存表中的数据 `usr_pwd`
--

INSERT INTO `usr_pwd` (`usrid`, `pwd`) VALUES
(10302010076, '10302010076'),
(11212010019, '11212010019'),
(11302010019, '11302010019'),
(11302010059, '11302010059'),
(11302010064, '11302010064'),
(11302010067, '11302010067');

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
