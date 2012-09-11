-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- 主機: localhost
-- 建立日期: Sep 11, 2012, 02:07 PM
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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

-- 
-- 列出以下資料庫的數據： `all_book`
-- 

INSERT INTO `all_book` VALUES (1, '那些年我們一起追的女孩', 'book_1.txt', 'img_1.jpg', '故事，應該從那一面牆開始說起', '九把刀', '春天出版社');
INSERT INTO `all_book` VALUES (2, '夜玫瑰', 'book_2.txt', 'img_2.jpg', '在五光十色的台北，交織出屬於他們的傾城之戀', '蔡智恆', '麥田出版社');
INSERT INTO `all_book` VALUES (3, '第一次的親密接觸', 'book_3.txt', 'img_3.jpg', '如果把整個太平洋的水倒出，也澆不熄我對妳愛情的火燄。整個太平洋的水全部倒得出嗎？不行。所以我並不愛妳。', '蔡智恆', '麥田出版社');
INSERT INTO `all_book` VALUES (4, '大唐雙龍傳', 'book_4.txt', 'img_4.jpg', '宇文化及年在三十許間，身形高瘦，手足頎長，臉容古挫，神色冷漠，一對眼神深邃莫測，予人狠冷無情的印象....', '黃易', '萬象出版社');
INSERT INTO `all_book` VALUES (5, '我的黑道男友', 'book_5.txt', 'img_5.jpg', '我遇見流氓了....', '紫月君', '網路出版');
INSERT INTO `all_book` VALUES (6, '傾世皇妃', 'book_6.txt', 'img_6.jpg', '夜闌翩舞雪海心...', '慕容湮兒', '中國畫報出版社');
INSERT INTO `all_book` VALUES (7, '天龍八部', 'book_7.txt', 'img_7.jpg', '青衫磊落險峰行 .....', '金庸', '遠流出版社');
INSERT INTO `all_book` VALUES (8, '神雕俠侶', 'book_8.txt', 'img_8.jpg', '風月無情 ....', '金庸', '遠流出版社');
INSERT INTO `all_book` VALUES (9, '雄霸蠻荒', 'book_9.txt', 'img_9.jpg', '章葉意外融合了前一世的記憶，並從記憶中，獲得一本《道德經》。憑此一書，章葉從底層一步步崛起，以自己的...', '淡定從容的某人', '小說頻道');
INSERT INTO `all_book` VALUES (10, '金剛法神', 'book_10.txt', 'img_10.jpg', '儘管因為殺戮殘暴而不允許上崗，張震卻仍是中南海保鏢中殺神一般的存在！他的隕落，是失去國寶級別的遺憾，...', '白衫盛雪', '小說頻道');
INSERT INTO `all_book` VALUES (11, '魔神召喚士', 'book_11.txt', 'img_11.jpg', '偶老爸是魔神王，偶老媽是天界公主，偶外婆是諸神之王，外公隻魔界之王，偶就是天字第一號的紈褲少爺，誰能...', '網絡騎士', '小說頻道');

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

