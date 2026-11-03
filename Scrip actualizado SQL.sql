CREATE DATABASE  IF NOT EXISTS bd_taller /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bd_taller`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- ------------------------------------------------------
-- Server version	8.4.11

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


--
-- Table structure for table `bodega`
--

DROP TABLE IF EXISTS `bodega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bodega` (
  `id_Bodega` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) DEFAULT NULL,
  `precio_unitario` decimal(10,2) DEFAULT NULL,
  `existencias` int DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_Bodega`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bodega`
--

LOCK TABLES `bodega` WRITE;
/*!40000 ALTER TABLE `bodega` DISABLE KEYS */;
INSERT INTO `bodega` VALUES (1,'Sensor MAP',345.50,14,'Sensor MAP'),(2,'Válvula IAC',180.00,3,'Válvula IAC'),(3,'Kit empacaduras',520.75,8,'Kit empacaduras'),(4,'Módulo de encendido',890.00,0,'Módulo de encendido'),(5,'Cables de bujía',215.25,15,'Cables de bujía'),(6,'Cojinete embrague',430.00,2,'Cojinete embrague'),(7,'Sensor de oxígeno',675.00,5,'Sensor de oxígeno'),(8,'Kit de inyectores',95.00,25,'Kit de inyectores'),(9,'Bomba de agua',310.80,0,'Bomba de agua'),(10,'Termostato',145.00,18,'Termostato');
/*!40000 ALTER TABLE `bodega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_Cliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `nit` varchar(45) DEFAULT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_Cliente`),
  UNIQUE KEY `nit_UNIQUE` (`nit`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Carlos Gómez','1234567-8','5555-0101'),(2,'Ana Martínez','8765432-1','5555-0202'),(3,'Maria Toledo','7899541-9','6565-8989'),(4,'Juan Morales','1236589-8','7878-4141'),(5,'Jackelyn Mazariegos','7898562-3','4142-3665'),(6,'Angel Cabrera','7412354-1','2545-2514'),(7,'Daniel Molina','8654451-9','4851-8956'),(8,'Leandro Castillo','8945162-5','6512-2356'),(9,'Sofia Hernandez','8545454-7','5842-4747'),(10,'Kevin Martinez','4654187-6','4654-5624'),(11,'Zero','6789','444444'),(13,'Marcos','Juarez','7878-6945');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `factura`
--

DROP TABLE IF EXISTS `factura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factura` (
  `id_factura` int NOT NULL AUTO_INCREMENT,
  `Fecha` date DEFAULT NULL,
  `costoTotal` decimal(10,2) DEFAULT NULL,
  `id_Orden` int NOT NULL,
  PRIMARY KEY (`id_factura`),
  KEY `fk_factura_orden` (`id_Orden`),
  CONSTRAINT `fk_factura_orden` FOREIGN KEY (`id_Orden`) REFERENCES `orden` (`id_orden`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura`
--

LOCK TABLES `factura` WRITE;
/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
INSERT INTO `factura` VALUES (1,'2026-10-30',250.00,1),(2,'2026-10-30',480.00,2),(3,'2026-11-01',380.00,4),(4,'2026-11-02',125.00,12),(5,'2026-11-02',200.00,2);
/*!40000 ALTER TABLE `factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mecanicos`
--

DROP TABLE IF EXISTS `mecanicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mecanicos` (
  `id_Mecanico` int NOT NULL AUTO_INCREMENT,
  `nombre_mecanico` varchar(45) DEFAULT NULL,
  `especialidad` varchar(45) DEFAULT NULL,
  `telefono_mecanico` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_Mecanico`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mecanicos`
--

LOCK TABLES `mecanicos` WRITE;
/*!40000 ALTER TABLE `mecanicos` DISABLE KEYS */;
INSERT INTO `mecanicos` VALUES (1,'Braulio Monterroso','Inyeccion electronica','5512-5890'),(2,'Gonzalo Archila','Transmisiones automaticas','4102-9931'),(3,'Esteban Villagran','Electricidad automotriz','5844-1022'),(4,'Mynor Santizo','Frenos y suspension','3015-7784'),(5,'Gustavo Rodas','Diagnostico y escaner','5290-3311'),(6,'Carlos Mendoza','Aire acondicionado y calefaccion','5348-5142'),(7,'Roberto Morales','Reparación de motores','4215-6780'),(8,'Javier Cifuentes','Sistemas de escape','5987-4514'),(9,'Mario Rodriguez','Alineación y balanceo','5623-3459'),(10,'Alejandro Pineda','Inyección diesel','4553-3596'),(11,'Juan Hurtarte','Hacer nada por ahora','6745'),(12,'Roberto Joseg','Inyección diesel','7878787');
/*!40000 ALTER TABLE `mecanicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orden`
--

DROP TABLE IF EXISTS `orden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orden` (
  `id_orden` int NOT NULL AUTO_INCREMENT,
  `estado_orden` varchar(45) DEFAULT NULL,
  `fecha_entrada` date DEFAULT NULL,
  `fecha_salida` date DEFAULT NULL,
  `costo_total` decimal(10,2) DEFAULT NULL,
  `id_Mecanico` int NOT NULL,
  `No_Placa` varchar(20) NOT NULL,
  PRIMARY KEY (`id_orden`),
  KEY `fk_orden_mecanico` (`id_Mecanico`),
  KEY `fk_orden_vehiculo` (`No_Placa`),
  CONSTRAINT `fk_orden_mecanico` FOREIGN KEY (`id_Mecanico`) REFERENCES `mecanicos` (`id_Mecanico`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_orden_vehiculo` FOREIGN KEY (`No_Placa`) REFERENCES `vehiculo` (`No_Placa`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orden`
--

LOCK TABLES `orden` WRITE;
/*!40000 ALTER TABLE `orden` DISABLE KEYS */;
INSERT INTO `orden` VALUES (1,'EN PROCESO','2026-10-30',NULL,0.00,1,'464kvt'),(2,'FINALIZADA','2026-10-30','2026-11-02',200.00,2,'P123KDG'),(3,'PENDIENTE','2026-10-30',NULL,NULL,2,'P265PDF'),(4,'FINALIZADA','2026-11-01','2026-11-01',380.00,5,'P456BPJ'),(5,'EN PROCESO','2026-11-01',NULL,0.00,1,'P456EDF'),(6,'FINALIZADA','2026-10-28','2026-10-29',450.00,2,'P582JPK'),(7,'EN PROCESO','2026-10-30',NULL,600.00,5,'P582XYZ'),(8,'FINALIZADA','2026-10-27','2026-10-28',320.00,6,'P789JFY'),(9,'PENDIENTE','2026-11-01',NULL,NULL,1,'P923JPN'),(10,'EN PROCESO','2026-11-01',NULL,0.00,8,'P957WKF'),(11,'CANCELADA','2026-11-01',NULL,NULL,2,'P789JFY'),(12,'FINALIZADA','2026-11-01','2026-11-02',125.00,3,'P789JFY'),(13,'PENDIENTE','2026-11-02',NULL,NULL,1,'464kvt'),(14,'CANCELADA','2026-11-02',NULL,NULL,3,'464kvt'),(15,'PENDIENTE','2026-11-02',NULL,NULL,2,'FGHJ5'),(16,'PENDIENTE','2026-11-02',NULL,NULL,11,'FGHJ5'),(17,'PENDIENTE','2026-11-02',NULL,NULL,6,'P789JFY'),(18,'PENDIENTE','2026-11-02',NULL,NULL,3,'464kvt');
/*!40000 ALTER TABLE `orden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orden_repuestos`
--

DROP TABLE IF EXISTS `orden_repuestos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orden_repuestos` (
  `id_DetalleRepuesto` int NOT NULL AUTO_INCREMENT,
  `repuesto_estado` varchar(45) DEFAULT NULL,
  `repuesto_cantidad` int DEFAULT NULL,
  `id_Bodega` int NOT NULL,
  `id_Orden` int NOT NULL,
  PRIMARY KEY (`id_DetalleRepuesto`),
  KEY `fk_orep_bodega` (`id_Bodega`),
  KEY `fk_orep_orden` (`id_Orden`),
  CONSTRAINT `fk_orep_bodega` FOREIGN KEY (`id_Bodega`) REFERENCES `bodega` (`id_Bodega`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_orep_orden` FOREIGN KEY (`id_Orden`) REFERENCES `orden` (`id_orden`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orden_repuestos`
--

LOCK TABLES `orden_repuestos` WRITE;
/*!40000 ALTER TABLE `orden_repuestos` DISABLE KEYS */;
INSERT INTO `orden_repuestos` VALUES (4,'ASIGNADO',1,2,4),(11,'ASIGNADO',2,1,14);
/*!40000 ALTER TABLE `orden_repuestos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orden_servicios`
--

DROP TABLE IF EXISTS `orden_servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orden_servicios` (
  `id_DetalleServicio` int NOT NULL AUTO_INCREMENT,
  `servicio_estado` varchar(45) DEFAULT NULL,
  `id_Servicios` int NOT NULL,
  `id_Orden` int NOT NULL,
  `servicio_valor` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_DetalleServicio`),
  KEY `fk_oserv_servicio` (`id_Servicios`),
  KEY `fk_oserv_orden` (`id_Orden`),
  CONSTRAINT `fk_oserv_orden` FOREIGN KEY (`id_Orden`) REFERENCES `orden` (`id_orden`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_oserv_servicio` FOREIGN KEY (`id_Servicios`) REFERENCES `servicios` (`id_Servicios`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orden_servicios`
--

LOCK TABLES `orden_servicios` WRITE;
/*!40000 ALTER TABLE `orden_servicios` DISABLE KEYS */;
INSERT INTO `orden_servicios` VALUES (1,'ASIGNADO',1,1,150.00),(2,'ASIGNADO',2,2,200.00),(3,'ASIGNADO',2,4,200.00),(4,'ASIGNADO',2,11,200.00),(5,'ASIGNADO',3,12,125.00),(6,'ASIGNADO',1,1,150.00),(7,'ASIGNADO',1,15,150.00);
/*!40000 ALTER TABLE `orden_servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicios`
--

DROP TABLE IF EXISTS `servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicios` (
  `id_Servicios` int NOT NULL AUTO_INCREMENT,
  `tipo_Servicio` varchar(45) DEFAULT NULL,
  `valor_Servicio` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_Servicios`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
INSERT INTO `servicios` VALUES (1,'Cambio de aceite',150.00),(2,'Alineación y balanceo',200.00),(3,'Revisión de frenos',125.00),(4,'Cambio de batería',450.00),(5,'Diagnóstico por computadora',250.00),(6,'Cambio de fajas y mangueras',300.00),(7,'Afinación mayor',600.00),(8,'Cambio de líquido de frenos',175.00),(9,'Limpieza de inyectores',350.00),(10,'Reemplazo de amortiguadores',850.00);
/*!40000 ALTER TABLE `servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculo`
--

DROP TABLE IF EXISTS `vehiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculo` (
  `No_Placa` varchar(20) NOT NULL,
  `modelo` varchar(45) DEFAULT NULL,
  `marca` varchar(45) DEFAULT NULL,
  `color` varchar(45) DEFAULT NULL,
  `id_Cliente` int NOT NULL,
  PRIMARY KEY (`No_Placa`),
  KEY `fk_vehiculo_cliente` (`id_Cliente`),
  CONSTRAINT `fk_vehiculo_cliente` FOREIGN KEY (`id_Cliente`) REFERENCES `clientes` (`id_Cliente`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculo`
--

LOCK TABLES `vehiculo` WRITE;
/*!40000 ALTER TABLE `vehiculo` DISABLE KEYS */;
INSERT INTO `vehiculo` VALUES ('464kvt','Corolla 2020','Toyota','Gris',1),('FGHJ5','corrola','Ferrary','verde',11),('P123KDG','Yaris 2018','Toyota','Blanco',3),('P265PDF','Sportage 2019','Kia','Blanco',8),('P456BPJ','Hilux 2021','Toyota','Gris',9),('P456EDF','Mazda 3 2021','Mazda','Rojo',4),('P582JPK','CR-V 2020','Honda','Negro',7),('P582XYZ','Civic 2019','Honda','Negro',2),('P789JFY','Sentra 2017','Nissan','Azul',5),('P923JPN','Tucson 2022','Hyundai','Plateado',6),('P957WKF','Jetta 2018','Volkswagen','Azul',10);
/*!40000 ALTER TABLE `vehiculo` ENABLE KEYS */;
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

-- Dump completed on 2026-11-02 16:39:28
