-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: hotelandinoplaza
-- ------------------------------------------------------
-- Server version	9.7.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'e06cc09b-5efa-11f1-9c17-a41f72531ae3:1-474';

--
-- Table structure for table `habitaciones`
--

DROP TABLE IF EXISTS `habitaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitaciones` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `disponible` bit(1) NOT NULL,
  `precio` double DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `numero` varchar(255) NOT NULL,
  `estado` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitaciones`
--

LOCK TABLES `habitaciones` WRITE;
/*!40000 ALTER TABLE `habitaciones` DISABLE KEYS */;
INSERT INTO `habitaciones` VALUES (54,_binary '',120,'Estándar','101',NULL),(55,_binary '',120,'Estándar','102',NULL),(56,_binary '',150,'Doble','201',NULL),(57,_binary '',150,'Doble','202',NULL),(58,_binary '',180,'Suite','301',NULL),(59,_binary '',180,'Suite','302',NULL),(60,_binary '',250,'Penthouse','401',NULL),(61,_binary '',250,'Penthouse','402',NULL);
/*!40000 ALTER TABLE `habitaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_ingreso` date DEFAULT NULL,
  `fecha_salida` date DEFAULT NULL,
  `nombre_cliente` varchar(255) DEFAULT NULL,
  `habitacion_id` bigint DEFAULT NULL,
  `numero_documento` varchar(255) DEFAULT NULL,
  `tipo_documento` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `motivo_cancelacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj1dyoxal4rnhdcxo4mv6bcivc` (`habitacion_id`),
  CONSTRAINT `FKj1dyoxal4rnhdcxo4mv6bcivc` FOREIGN KEY (`habitacion_id`) REFERENCES `habitaciones` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

LOCK TABLES `reservas` WRITE;
/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
INSERT INTO `reservas` VALUES (201,'2026-06-14','2026-06-15','JESSICA CALVA',54,'98765432','DNI','ACTIVA',NULL),(202,'2026-06-29','2026-06-30','JESSICA CALVA',54,'21313123','DNI','FINALIZADA',NULL);
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(255) NOT NULL,
  `numero_documento` varchar(255) NOT NULL,
  `tipo_documento` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (2,'admin@gmail.com','Administrador','{bcrypt}$2a$10$2uB1QQj.U5jQEQv..kQ4O.pVGFkJSZuetgfZ9cXJHXNF/4gMXrrYW','ADMIN','',''),(3,'juanperez@gmail.com','juan perez','{bcrypt}$2a$10$PlKB2gAoWvrh8T6klEwgF.9Gl7j7A7tG5TgLtuSqvtHriiGfp4xvm','USER','87654321','DNI'),(4,'jesus@gmail.com','Jesus Gabriel','{bcrypt}$2a$10$472sn/acqwzYTRA/7JyTWe2yymOM5yxO61U4V3X3Z0hvFJt00901O','USER','12311111','DNI'),(5,'andrelazo@gmail.com','Andre Lazo','{bcrypt}$2a$10$Ao9tXCVpqwfJ1NakmYMjRe6DpIm2xxGiCADHeMp1EwIeleoU1awi6','USER','44444444','DNI'),(6,'adrian25456@gmail.com','adrian gabriel egusquiza calva','{bcrypt}$2a$10$eRqShSj0okqfPMyQ0tZB5.l4VW.D7iUlFjrPc62UVabQ.d/XJcdjW','USER','61291975','DNI');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-02 21:23:21
