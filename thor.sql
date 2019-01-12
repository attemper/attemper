/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.22 : Database - thor
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`thor` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `thor`;

/*Table structure for table `thor_group_atom_mapping` */

CREATE TABLE `thor_group_atom_mapping` (
  `GROUP_JOB_NAME` varchar(64) COLLATE utf8_bin NOT NULL,
  `ATOM_JOB_NAME` varchar(64) COLLATE utf8_bin NOT NULL,
  `PRIORITY` int(64) DEFAULT NULL,
  `TENANT_ID` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`GROUP_JOB_NAME`,`ATOM_JOB_NAME`,`TENANT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `thor_group_atom_mapping` */

/*Table structure for table `thor_job` */

CREATE TABLE `thor_job` (
  `JOB_NAME` varchar(64) COLLATE utf8_bin NOT NULL,
  `DISPLAY_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  `JOB_TYPE` int(2) DEFAULT NULL,
  `JOB_CONTENT` text COLLATE utf8_bin,
  `STATUS` int(2) DEFAULT NULL,
  `CATEGORY_NAME` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `REMARK` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`JOB_NAME`,`TENANT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `thor_job` */

insert  into `thor_job`(`JOB_NAME`,`DISPLAY_NAME`,`JOB_TYPE`,`JOB_CONTENT`,`STATUS`,`CATEGORY_NAME`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`TENANT_ID`) values ('1','1',11,NULL,0,NULL,'2019-01-10 21:36:55','2019-01-10 21:36:55','1','natasha'),('NATASHA_ATOM_JOB_1','任务1',11,'{\"requestMethod\":\"GET\",\"uri\":\"/api/job/syncUUMS\"}',1,NULL,'2019-01-11 20:01:37',NULL,'流程中心一个任务','natasha'),('sync_uums','同步UUMS任务',11,NULL,0,NULL,'2019-01-09 20:58:11','2019-01-09 20:58:15','同步UUMS任务111','natasha');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
