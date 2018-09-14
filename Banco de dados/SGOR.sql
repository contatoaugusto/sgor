CREATE DATABASE  IF NOT EXISTS `sgor` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `sgor`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: sgor
-- ------------------------------------------------------
-- Server version	5.7.22-log

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
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrador` (
  `cpf` varchar(15) NOT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `datanasc` datetime DEFAULT NULL,
  `cep` varchar(10) DEFAULT NULL,
  `endereco` varchar(45) DEFAULT NULL,
  `sexo` varchar(10) DEFAULT NULL,
  `idusuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`cpf`),
  KEY `administrador_usuario_idx` (`idusuario`),
  CONSTRAINT `administrador_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guarda`
--

DROP TABLE IF EXISTS `guarda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guarda` (
  `idguarda` int(11) NOT NULL AUTO_INCREMENT,
  `telefone` varchar(15) DEFAULT NULL,
  `endereco` varchar(45) DEFAULT NULL,
  `cep` varchar(10) DEFAULT NULL,
  `datanasc` datetime DEFAULT NULL,
  `sexo` varchar(10) DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `cpf` varchar(15) DEFAULT NULL,
  `idusuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`idguarda`),
  KEY `guarda_usuario_idx` (`idusuario`),
  CONSTRAINT `guarda_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guarda`
--

LOCK TABLES `guarda` WRITE;
/*!40000 ALTER TABLE `guarda` DISABLE KEYS */;
INSERT INTO `guarda` VALUES (1,'(11) 1111-1111','Meu endereco','66666-666','1980-02-01 21:00:00',NULL,'Nome Guarda','999.999.999-99',4),(2,'(54) 5454-5454','Meu endereco','23232-323','1983-02-01 21:00:00',NULL,'Guarda Jaiminho','787.878.787-87',11);
/*!40000 ALTER TABLE `guarda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `infracao`
--

DROP TABLE IF EXISTS `infracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `infracao` (
  `idinfracao` int(11) NOT NULL AUTO_INCREMENT,
  `nivel` varchar(45) DEFAULT NULL,
  `descricao` varchar(500) DEFAULT NULL,
  `idocorrencia` int(11) DEFAULT NULL,
  `cpf` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`idinfracao`),
  KEY `infracao_ocorrencia_idx` (`idocorrencia`),
  KEY `infracao_administrador_idx` (`cpf`),
  CONSTRAINT `infracao_administrador` FOREIGN KEY (`cpf`) REFERENCES `administrador` (`cpf`),
  CONSTRAINT `infracao_ocorrencia` FOREIGN KEY (`idocorrencia`) REFERENCES `ocorrencia` (`idocorrencia`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `infracao`
--

LOCK TABLES `infracao` WRITE;
/*!40000 ALTER TABLE `infracao` DISABLE KEYS */;
/*!40000 ALTER TABLE `infracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `morador`
--

DROP TABLE IF EXISTS `morador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `morador` (
  `idmorador` int(11) NOT NULL AUTO_INCREMENT,
  `cpf` varchar(15) NOT NULL,
  `fatura` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `telefone` varchar(15) DEFAULT NULL,
  `cep` varchar(10) DEFAULT NULL,
  `residencia` varchar(45) DEFAULT NULL,
  `datanasc` varchar(10) DEFAULT NULL,
  `sexo` varchar(10) DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `idresidencia` int(11) DEFAULT NULL,
  `idusuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`idmorador`),
  KEY `morador_residencia_idx` (`idresidencia`),
  KEY `morador_usuario_idx` (`idusuario`),
  CONSTRAINT `morador_residencia` FOREIGN KEY (`idresidencia`) REFERENCES `residencia` (`idresidencia`),
  CONSTRAINT `morador_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `morador`
--

LOCK TABLES `morador` WRITE;
/*!40000 ALTER TABLE `morador` DISABLE KEYS */;
INSERT INTO `morador` VALUES (1,'888.888.888-88',NULL,NULL,'(55) 5555-5555',NULL,'Minha residencia','02/02/1980','Masculino','Morador teste',NULL,3),(2,'999.999.999-99',NULL,NULL,'(55) 5555-5555',NULL,NULL,'01/01/1972','Masculino','João Antonio',10,5),(3,'888.888.888-88',NULL,NULL,'(11) 1111-1111',NULL,NULL,'01/08/1973','Masculino','Antonio Augusto',3,6),(4,'488.888.888-88',NULL,NULL,'(22) 2222-2222',NULL,NULL,'03/07/1970','Masculino','Henrique Souza',7,7),(5,'787.877.887-78',NULL,NULL,'(89) 8989-8998',NULL,NULL,'03/04/1988','Feminino','Joana',1,9),(6,'777.777.777-77',NULL,NULL,'(11) 1111-1111',NULL,NULL,'03/05/1977','Masculino','Maria Lucia',1,10);
/*!40000 ALTER TABLE `morador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ocorrencia`
--

DROP TABLE IF EXISTS `ocorrencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ocorrencia` (
  `idocorrencia` int(11) NOT NULL AUTO_INCREMENT,
  `status` binary(1) DEFAULT NULL,
  `data` datetime DEFAULT CURRENT_TIMESTAMP,
  `descricao` varchar(500) DEFAULT NULL,
  `idguarda` int(11) DEFAULT NULL,
  `idmorador` int(11) DEFAULT NULL,
  PRIMARY KEY (`idocorrencia`),
  KEY `ocorrencia_guarda_idx` (`idguarda`),
  KEY `ocorrencia_morador_idx` (`idmorador`),
  CONSTRAINT `ocorrencia_guarda` FOREIGN KEY (`idguarda`) REFERENCES `guarda` (`idguarda`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ocorrencia_morador` FOREIGN KEY (`idmorador`) REFERENCES `morador` (`idmorador`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ocorrencia`
--

LOCK TABLES `ocorrencia` WRITE;
/*!40000 ALTER TABLE `ocorrencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `ocorrencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil`
--

DROP TABLE IF EXISTS `perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfil` (
  `idperfil` int(11) NOT NULL AUTO_INCREMENT,
  `nmperfil` varchar(100) NOT NULL,
  PRIMARY KEY (`idperfil`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES (1,'Morador'),(2,'Administrador'),(3,'Guarda');
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `residencia`
--

DROP TABLE IF EXISTS `residencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `residencia` (
  `idresidencia` int(11) NOT NULL AUTO_INCREMENT,
  `modulo` varchar(45) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `descricao` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idresidencia`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `residencia`
--

LOCK TABLES `residencia` WRITE;
/*!40000 ALTER TABLE `residencia` DISABLE KEYS */;
INSERT INTO `residencia` VALUES (1,'B',23,'Casa Voltada pros fundos'),(2,'Tess',33,'qweqwe'),(3,'Modulo L',225,'Essa é mais uma residência'),(4,'Mod ww',87,'Teste'),(5,'dew',45,'Vamos ao teste'),(6,'we',22,'ewerwer'),(7,'TModulo Teste',888,'Mais um modulo de teste e residenia'),(8,'Estamos modulo 3',444,'Isso de novo'),(9,'assss',333,'ddddddddddddddddddddddddddddd'),(10,'Modulo 555',233,'So um teste');
/*!40000 ALTER TABLE `residencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `idusuario` int(11) NOT NULL AUTO_INCREMENT,
  `nmusuario` varchar(100) NOT NULL,
  `idperfil` int(11) NOT NULL,
  `deSenha` varchar(20) NOT NULL,
  PRIMARY KEY (`idusuario`),
  KEY `idperfil_idx` (`idperfil`),
  CONSTRAINT `idperfil` FOREIGN KEY (`idperfil`) REFERENCES `perfil` (`idperfil`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'contatoaugusto@gmail.com',2,'123'),(2,'teste',2,'123'),(3,'morador',3,'123'),(4,'guarda',3,'123'),(5,'joao',1,'123'),(6,'lucas',1,'123'),(7,'henr',1,'123'),(8,'henrique',1,'123'),(9,'jo',1,'123'),(10,'malu',1,'123'),(11,'jaime',3,'123');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-13 23:58:25
