CREATE DATABASE  IF NOT EXISTS `estimate_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `estimate_management`;
-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: estimate_management
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `availability`
--

DROP TABLE IF EXISTS `availability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `availability` (
  `PRODUCT` int NOT NULL,
  `OPT` int NOT NULL,
  PRIMARY KEY (`PRODUCT`,`OPT`),
  KEY `AVBY` (`OPT`),
  CONSTRAINT `AVBY` FOREIGN KEY (`OPT`) REFERENCES `opt` (`CODE`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `AVFOR` FOREIGN KEY (`PRODUCT`) REFERENCES `product` (`CODE`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `availability`
--

LOCK TABLES `availability` WRITE;
/*!40000 ALTER TABLE `availability` DISABLE KEYS */;
INSERT INTO `availability` VALUES (1,1),(2,1),(1,2),(2,2),(1,3),(2,3),(1,4),(1,5),(2,5),(5,5),(1,6),(2,6),(1,7),(2,7),(3,7),(1,8),(3,8),(3,9),(3,10),(3,11),(3,12),(4,13),(4,14),(4,15),(4,16),(5,17),(5,18),(5,19),(5,20);
/*!40000 ALTER TABLE `availability` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `decor`
--

DROP TABLE IF EXISTS `decor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `decor` (
  `ESTIMATE` int NOT NULL,
  `OPT` int NOT NULL,
  PRIMARY KEY (`ESTIMATE`,`OPT`),
  KEY `DECORATOR` (`OPT`),
  CONSTRAINT `DECORATING` FOREIGN KEY (`ESTIMATE`) REFERENCES `estimate` (`CODE`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `DECORATOR` FOREIGN KEY (`OPT`) REFERENCES `opt` (`CODE`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `decor`
--

LOCK TABLES `decor` WRITE;
/*!40000 ALTER TABLE `decor` DISABLE KEYS */;
INSERT INTO `decor` VALUES (13,1),(15,1),(26,1),(28,1),(4,2),(10,2),(15,2),(26,2),(28,2),(29,2),(35,2),(36,2),(51,2),(72,2),(73,2),(4,3),(15,3),(26,3),(28,3),(50,3),(59,3),(67,3),(10,4),(13,4),(15,4),(26,4),(7,5),(14,5),(15,5),(19,5),(26,5),(28,5),(35,5),(39,5),(41,5),(51,5),(65,5),(73,5),(4,6),(10,6),(13,6),(15,6),(26,6),(28,6),(50,6),(51,6),(72,6),(8,7),(9,7),(11,7),(15,7),(21,7),(26,7),(27,7),(28,7),(29,7),(35,7),(36,7),(37,7),(41,7),(42,7),(69,7),(9,8),(11,8),(15,8),(17,8),(21,8),(26,8),(33,8),(37,8),(42,8),(52,8),(53,8),(68,8),(5,9),(8,9),(9,9),(11,9),(20,9),(27,9),(31,9),(32,9),(34,9),(40,9),(42,9),(47,9),(52,9),(66,9),(71,9),(11,10),(17,10),(18,10),(27,10),(30,10),(33,10),(37,10),(42,10),(43,10),(45,10),(46,10),(49,10),(54,10),(55,10),(56,10),(58,10),(62,10),(68,10),(5,11),(11,11),(17,11),(18,11),(20,11),(21,11),(27,11),(31,11),(34,11),(42,11),(49,11),(52,11),(53,11),(57,11),(71,11),(11,12),(27,12),(30,12),(40,12),(42,12),(69,12),(22,13),(12,14),(16,14),(22,14),(25,14),(38,14),(48,14),(70,14),(12,15),(22,15),(64,15),(22,16),(38,16),(44,16),(48,16),(60,16),(63,16),(14,17),(19,17),(39,17),(6,18),(61,18),(6,19),(14,19),(61,19);
/*!40000 ALTER TABLE `decor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estimate`
--

DROP TABLE IF EXISTS `estimate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estimate` (
  `CODE` int NOT NULL AUTO_INCREMENT,
  `PRODUCT` int NOT NULL,
  `CLIENT` int NOT NULL,
  `EMPLOYEE` int DEFAULT NULL,
  `PRICE` double DEFAULT NULL,
  PRIMARY KEY (`CODE`),
  KEY `PROD` (`PRODUCT`),
  CONSTRAINT `PROD` FOREIGN KEY (`PRODUCT`) REFERENCES `product` (`CODE`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estimate`
--

LOCK TABLES `estimate` WRITE;
/*!40000 ALTER TABLE `estimate` DISABLE KEYS */;
INSERT INTO `estimate` VALUES (4,2,6,2,122222),(5,3,6,2,15),(6,5,6,2,10202),(7,2,6,2,99.99),(8,3,1,2,12),(9,3,1,2,45),(10,1,1,2,12),(11,3,6,2,1),(12,4,1,2,12),(13,1,1,2,12),(14,5,1,2,12),(15,1,1,2,125.67),(16,4,1,2,56),(17,3,1,2,74.34),(18,3,1,2,45),(19,5,1,2,1),(20,3,6,2,5555),(21,3,6,2,9999),(22,4,6,2,12),(25,4,8,2,2000000.56),(26,1,8,2,274566637),(27,3,8,2,1),(28,2,8,2,15),(29,2,1,2,12),(30,3,1,2,255),(31,3,1,2,12),(32,3,1,2,12),(33,3,1,2,12),(34,3,1,2,12),(35,1,8,2,12),(36,2,8,2,12),(37,3,1,2,12),(38,4,1,2,12),(39,5,1,2,65),(40,3,1,2,25),(41,2,1,2,36),(42,3,1,2,12),(43,3,1,2,1222222222),(44,4,8,2,12555555),(45,3,1,2,12),(46,3,1,2,12),(47,3,1,2,12),(48,4,1,2,12),(49,3,1,2,25),(50,2,1,2,123),(51,2,1,2,12),(52,3,1,2,12),(53,3,8,2,12),(54,3,1,2,12),(55,3,1,2,12),(56,3,1,2,12),(57,3,1,2,12),(58,3,1,2,12),(59,2,1,2,12),(60,4,1,2,15),(61,5,1,2,15),(62,3,1,2,12),(63,4,1,2,23),(64,4,1,2,19853),(65,2,1,2,15),(66,3,1,14,15),(67,2,1,NULL,NULL),(68,3,1,NULL,NULL),(69,3,1,NULL,NULL),(70,4,1,NULL,NULL),(71,3,1,NULL,NULL),(72,2,1,NULL,NULL),(73,2,1,NULL,NULL);
/*!40000 ALTER TABLE `estimate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `opt`
--

DROP TABLE IF EXISTS `opt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `opt` (
  `CODE` int NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(45) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `opt`
--

LOCK TABLES `opt` WRITE;
/*!40000 ALTER TABLE `opt` DISABLE KEYS */;
INSERT INTO `opt` VALUES (1,'STANDARD','Long'),(2,'STANDARD','Medium'),(3,'STANDARD','Short'),(4,'SALE','Serrated'),(5,'STANDARD','Red'),(6,'STANDARD','Blue'),(7,'SALE','Green'),(8,'STANDARD','Yellow'),(9,'STANDARD','Italian Layout'),(10,'STANDARD','German Layout'),(11,'STANDARD','France Layout'),(12,'SALE','US Layout'),(13,'STANDARD','Lavender'),(14,'STANDARD','Cinnamon'),(15,'STANDARD','Rose'),(16,'SALE','Chocolate'),(17,'SALE','10 Imperial'),(18,'STANDARD','10 Metric'),(19,'STANDARD','12 Metric'),(20,'STANDARD','14 Metric');
/*!40000 ALTER TABLE `opt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `CODE` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `IMAGE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Knife','kitchen-knife_1f52a.png'),(2,'Hammer','hammer_1f528.png'),(3,'Keyboard','keyboard_2328-fe0f.png'),(4,'Soap','soap_1f9fc.png'),(5,'Wrench','wrench_1f527.png');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(45) NOT NULL,
  `USERNAME` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `USERTYPE` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'aldo84@ristorante.it','aldo84','p@ssw0rd','CLIENT'),(2,'edoardo.bassini@pollimi.it','edobass45','p@ssw0rd','EMPLOYEE'),(14,'dino@pino.dom','dino90','Pollo90!','EMPLOYEE'),(15,'c@m.c','carlo00','Pollo90!','CLIENT'),(16,'gino@tino.it','gino78','Pippo90!r','CLIENT');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-09 10:14:39
