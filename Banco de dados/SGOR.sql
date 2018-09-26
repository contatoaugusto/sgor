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
INSERT INTO `administrador` VALUES ('555.555.555-55','Administrador do Sistema',NULL,'66666-666','N alto da Colina, rua 12 casa 33','Masculino',12);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guarda`
--

LOCK TABLES `guarda` WRITE;
/*!40000 ALTER TABLE `guarda` DISABLE KEYS */;
INSERT INTO `guarda` VALUES (1,'(66) 6666-6666','Quadra 410 Conjunto L Casa 48','77777-777','2018-01-31 22:00:00',NULL,'Guarda Jaiminho Teixeira','555.555.555-55',21);
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
  `idinfracao_nivel` int(11) NOT NULL,
  `descricao` varchar(500) DEFAULT NULL,
  `idocorrencia` int(11) NOT NULL,
  `cpf` varchar(15) NOT NULL COMMENT 'CPF do Administrador do sistema que atribuiu essa multa pra ocorrência em questão',
  PRIMARY KEY (`idinfracao`),
  KEY `infracao_ocorrencia_idx` (`idocorrencia`),
  KEY `infracao_administrador_idx` (`cpf`),
  KEY `infracao_nivel_infracao_idx` (`idinfracao_nivel`),
  CONSTRAINT `infracao_administrador` FOREIGN KEY (`cpf`) REFERENCES `administrador` (`cpf`),
  CONSTRAINT `infracao_nivel_infracao` FOREIGN KEY (`idinfracao_nivel`) REFERENCES `infracao_nivel` (`idinfracao_nivel`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `infracao_ocorrencia` FOREIGN KEY (`idocorrencia`) REFERENCES `ocorrencia` (`idocorrencia`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `infracao`
--

LOCK TABLES `infracao` WRITE;
/*!40000 ALTER TABLE `infracao` DISABLE KEYS */;
INSERT INTO `infracao` VALUES (1,1,NULL,3,'555.555.555-55'),(2,3,NULL,3,'555.555.555-55');
/*!40000 ALTER TABLE `infracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `infracao_nivel`
--

DROP TABLE IF EXISTS `infracao_nivel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `infracao_nivel` (
  `idinfracao_nivel` int(11) NOT NULL AUTO_INCREMENT,
  `nminfracao_nivel` varchar(60) NOT NULL,
  `deinfracao_nivel` varchar(200) DEFAULT NULL,
  `vrmulta` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`idinfracao_nivel`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COMMENT='Cadastro dos níveis de infração a serem aplicadas as ocorrências geradas pelos moradores';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `infracao_nivel`
--

LOCK TABLES `infracao_nivel` WRITE;
/*!40000 ALTER TABLE `infracao_nivel` DISABLE KEYS */;
INSERT INTO `infracao_nivel` VALUES (1,'Infracao de Nivel 1','Lixo fora do lugar',NULL),(2,'Infracao de Nivel 2','Som Alto',NULL),(3,'Infracao de Nivel 3','Estacionar carro em lugar proibido',NULL),(4,'Infracao de Nivel 4','Trafegar com veiculo em alta velocidade',NULL);
/*!40000 ALTER TABLE `infracao_nivel` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `morador`
--

LOCK TABLES `morador` WRITE;
/*!40000 ALTER TABLE `morador` DISABLE KEYS */;
INSERT INTO `morador` VALUES (7,'888.888.888-88',NULL,NULL,'(52) 2222-2222',NULL,NULL,'44/44/4444','Masculino','Isaias Alencar da Silva',11,13),(13,'555.555.555-55',NULL,NULL,'(33) 3333-3333',NULL,NULL,'03/03/1977','Feminino','Maria Silva',11,27);
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
  `status` varchar(20) DEFAULT NULL,
  `data` datetime DEFAULT CURRENT_TIMESTAMP,
  `descricao` varchar(500) DEFAULT NULL,
  `idguarda` int(11) DEFAULT NULL,
  `idmorador` int(11) DEFAULT NULL,
  PRIMARY KEY (`idocorrencia`),
  KEY `ocorrencia_guarda_idx` (`idguarda`),
  KEY `ocorrencia_morador_idx` (`idmorador`),
  CONSTRAINT `ocorrencia_guarda` FOREIGN KEY (`idguarda`) REFERENCES `guarda` (`idguarda`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ocorrencia_morador` FOREIGN KEY (`idmorador`) REFERENCES `morador` (`idmorador`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COMMENT='Os status possíveis podem se: \nEm Aberto\nAceito\nRecusado \nFinalizado';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ocorrencia`
--

LOCK TABLES `ocorrencia` WRITE;
/*!40000 ALTER TABLE `ocorrencia` DISABLE KEYS */;
INSERT INTO `ocorrencia` VALUES (1,'Recusado','2018-09-23 13:55:59','Quero que limpe a rua',NULL,7),(2,'Aceito','2018-09-23 14:31:23','Mais uma',NULL,7),(3,'Aceito','2018-09-23 14:41:46','Vaaaaaaaaaa   aad',NULL,7),(4,'Em Aberto','2018-09-23 20:01:35','I have a p:accordionPanel which represents a list of items and in each tab there is a form. Upon submitting any of the repeating forms, it is possible that extra data in needed, which is when a p:dialog is popped up prompting the user to enter some more data. That dialog is defined outside the accordion panel because, unlike the items from the accordion, only one of them can be showing at a time so there is no need to augment the HTML served by repeating it in each tab of the accordion.',NULL,13);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `residencia`
--

LOCK TABLES `residencia` WRITE;
/*!40000 ALTER TABLE `residencia` DISABLE KEYS */;
INSERT INTO `residencia` VALUES (11,'A',1,'Casa do Senhor Isaias'),(12,'A',2,'Casa do senhor Noel'),(13,'A',3,'Casa maria'),(14,'A',4,'Casa e tal');
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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (12,'admin',2,'123'),(13,'morador',1,'123'),(21,'guarda',3,'123'),(27,'maria',1,'123');
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

-- Dump completed on 2018-09-25 23:52:41
