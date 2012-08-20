-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- 主機: localhost
-- 建立日期: Aug 20, 2012, 03:57 AM
-- 伺服器版本: 5.0.51
-- PHP 版本: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- 資料庫: `danding_book`
-- 

-- --------------------------------------------------------

-- 
-- 資料表格式： `all_book`
-- 

CREATE TABLE `all_book` (
  `b_id` int(10) unsigned NOT NULL auto_increment,
  `b_name` varchar(20) NOT NULL,
  `b_path` varchar(80) NOT NULL,
  `b_image` varchar(80) default NULL,
  `b_intro` varchar(60) default NULL,
  `b_author` varchar(20) default NULL,
  `b_issue` varchar(20) default NULL,
  PRIMARY KEY  (`b_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- 列出以下資料庫的數據： `all_book`
-- 


-- --------------------------------------------------------

-- 
-- 資料表格式： `user_book`
-- 

CREATE TABLE `user_book` (
  `b_id` int(10) unsigned NOT NULL auto_increment,
  `b_uid` int(11) NOT NULL,
  `b_bkid` int(11) NOT NULL,
  PRIMARY KEY  (`b_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- 列出以下資料庫的數據： `user_book`
-- 


-- --------------------------------------------------------

-- 
-- 資料表格式： `user_info`
-- 

CREATE TABLE `user_info` (
  `b_id` int(10) unsigned NOT NULL auto_increment,
  `b_name` varchar(20) NOT NULL,
  `b_account` varchar(30) NOT NULL,
  `b_pwd` varchar(20) NOT NULL,
  PRIMARY KEY  (`b_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- 列出以下資料庫的數據： `user_info`
-- 

