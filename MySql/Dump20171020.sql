-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: osam
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `wb_book`
--

DROP TABLE IF EXISTS `wb_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wb_book` (
  `num` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `isbn` varchar(100) NOT NULL,
  `imageurl` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`num`),
  UNIQUE KEY `num_UNIQUE` (`num`),
  UNIQUE KEY `isbn_UNIQUE` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wb_book`
--

LOCK TABLES `wb_book` WRITE;
/*!40000 ALTER TABLE `wb_book` DISABLE KEYS */;
INSERT INTO `wb_book` VALUES (1,'변신','9788937460043',''),(2,'안나 카레니나','9791159036996',''),(3,'하늘과 바람과 별과 시','9788998046682',''),(4,'인간 실격','9788937461033',''),(5,'요츠바랑!','9788925286648',''),(6,'쇼와사 1','9788991124011',''),(7,'qwr','243',NULL),(8,'123rt','12',NULL);
/*!40000 ALTER TABLE `wb_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wb_book_like`
--

DROP TABLE IF EXISTS `wb_book_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wb_book_like` (
  `num` int(11) NOT NULL AUTO_INCREMENT,
  `num_pf` int(11) NOT NULL,
  `num_bk` int(11) NOT NULL,
  UNIQUE KEY `num_UNIQUE` (`num`),
  KEY `num_fp_idx` (`num_pf`),
  KEY `num_bk_idx` (`num_bk`),
  CONSTRAINT `num_bk` FOREIGN KEY (`num_bk`) REFERENCES `wb_book` (`num`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `num_pf` FOREIGN KEY (`num_pf`) REFERENCES `wb_profile` (`num`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wb_book_like`
--

LOCK TABLES `wb_book_like` WRITE;
/*!40000 ALTER TABLE `wb_book_like` DISABLE KEYS */;
INSERT INTO `wb_book_like` VALUES (66,6,5),(67,8,7),(68,8,7),(69,8,7),(70,8,7),(71,8,7),(72,8,7),(73,8,4),(74,9,8);
/*!40000 ALTER TABLE `wb_book_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wb_book_review`
--

DROP TABLE IF EXISTS `wb_book_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wb_book_review` (
  `num` int(11) NOT NULL AUTO_INCREMENT,
  `num_pf_s` int(11) NOT NULL,
  `num_bk_s` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` varchar(500) NOT NULL,
  PRIMARY KEY (`num`),
  KEY `num_pf_s_idx` (`num_pf_s`),
  KEY `num_bk_s_idx` (`num_bk_s`),
  CONSTRAINT `num_bk_s` FOREIGN KEY (`num_bk_s`) REFERENCES `wb_book` (`num`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `num_pf_s` FOREIGN KEY (`num_pf_s`) REFERENCES `wb_profile` (`num`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wb_book_review`
--

LOCK TABLES `wb_book_review` WRITE;
/*!40000 ALTER TABLE `wb_book_review` DISABLE KEYS */;
INSERT INTO `wb_book_review` VALUES (39,6,2,'www','qqqq'),(40,6,5,'rrr','  vccc'),(41,6,5,'2rra','rcg'),(43,8,7,'weyhfx','fffdd'),(44,8,7,'gffffg','hhggggvc');
/*!40000 ALTER TABLE `wb_book_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wb_profile`
--

DROP TABLE IF EXISTS `wb_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wb_profile` (
  `num` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(45) NOT NULL,
  `pw` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`num`),
  UNIQUE KEY `num_UNIQUE` (`num`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wb_profile`
--

LOCK TABLES `wb_profile` WRITE;
/*!40000 ALTER TABLE `wb_profile` DISABLE KEYS */;
INSERT INTO `wb_profile` VALUES (6,'1','1','1'),(7,'sws1234','qwer',''),(8,'sws1235','qwer','sws'),(9,'qwe123','qwer','sws');
/*!40000 ALTER TABLE `wb_profile` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-20  5:34:18
