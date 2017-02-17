-- 创建数据库，并创建权限用户
CREATE DATABASE ssm CHARACTER SET utf8 COLLATE utf8_general_ci;
-- CREATE USER 'ssm'@'%' IDENTIFIED BY '123456';
-- GRANT ALL PRIVILEGES ON ssm.* TO 'ssm'@'%';
CREATE USER 'ssm'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON ssm.* TO 'ssm'@'localhost';
FLUSH PRIVILEGES;

-- 创建表
USE ssm;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- 插入数据
INSERT INTO `user` VALUES ('1', 'Roin', 'roingeek@qq.com', '2016-8-15', '123456');

CREATE TABLE `zreading` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `publishDate` datetime DEFAULT NULL,
  `acticle` blob,
  `views` int(11),
  PRIMARY KEY (`id`),
  KEY `index_url` (`url`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `zhihu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `answer` blob,
  `author` varchar(255) DEFAULT NULL,
  `vote` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_index` (`url`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

