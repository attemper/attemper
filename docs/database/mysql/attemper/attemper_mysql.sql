/*Table structure for table attemper_arg */

CREATE TABLE attemper_arg (
  ARG_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  ARG_TYPE int(11) NOT NULL,
  ARG_VALUE varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  GENERIC_TYPE int(11) DEFAULT NULL,
  ATTRIBUTE varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  REMARK varchar(255) COLLATE utf8_bin DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (ARG_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_calendar */

CREATE TABLE attemper_calendar (
  CALENDAR_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  DISPLAY_NAME varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (CALENDAR_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_calendar_day */

CREATE TABLE attemper_calendar_day (
  CALENDAR_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  DAY_NUM int(8) NOT NULL,
  REMARK varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (CALENDAR_NAME,DAY_NUM)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_condition */

CREATE TABLE attemper_condition (
  CONDITION_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  CONDITION_TYPE int(11) NOT NULL,
  CONTENT varchar(2000) COLLATE utf8_bin NOT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (CONDITION_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_datasource */

CREATE TABLE attemper_datasource (
  DB_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  DRIVER_CLASS_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  JDBC_URL varchar(255) COLLATE utf8_bin NOT NULL,
  USER_NAME varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PASSWORD varchar(255) COLLATE utf8_bin DEFAULT NULL,
  ATTRIBUTE varchar(255) COLLATE utf8_bin DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (DB_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_delay_job */

CREATE TABLE attemper_delay_job (
  ID varchar(64) COLLATE utf8_bin NOT NULL,
  JOB_NAME varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  STATUS int(11) DEFAULT NULL,
  REQUEST_TIME bigint(13) DEFAULT NULL,
  TENANT_ID varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_execution */

CREATE TABLE attemper_execution (
  ID varchar(64) COLLATE utf8_bin NOT NULL,
  PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  ROOT_PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  SUPER_PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  JOB_NAME varchar(64) COLLATE utf8_bin DEFAULT NULL,
  TRIGGER_NAME varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PROC_DEF_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  START_TIME bigint(13) DEFAULT NULL,
  END_TIME bigint(13) DEFAULT NULL,
  DURATION bigint(20) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  CODE int(11) DEFAULT NULL,
  MSG text COLLATE utf8_bin,
  PARENT_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  SCHEDULER_URI varchar(255) COLLATE utf8_bin DEFAULT NULL,
  EXECUTOR_URI varchar(255) COLLATE utf8_bin DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE KEY IDX_PROC_INST_ID (PROC_INST_ID),
  KEY IDX_JOB_NAME (JOB_NAME),
  KEY IDX_TENANT_ID (TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_gist */

CREATE TABLE attemper_gist (
  GIST_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  CREATE_TIME bigint(13) NOT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (GIST_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_gist_info */

CREATE TABLE attemper_gist_info (
  ID varchar(64) COLLATE utf8_bin NOT NULL,
  GIST_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  VERSION varchar(64) COLLATE utf8_bin NOT NULL,
  CONTENT text COLLATE utf8_bin,
  UPDATE_TIME bigint(13) NOT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_instance */

CREATE TABLE attemper_instance (
  ID varchar(64) COLLATE utf8_bin NOT NULL,
  PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  ROOT_PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  SUPER_PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  JOB_NAME varchar(64) COLLATE utf8_bin DEFAULT NULL,
  TRIGGER_NAME varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PROC_DEF_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  START_TIME bigint(13) DEFAULT NULL,
  END_TIME bigint(13) DEFAULT NULL,
  DURATION bigint(20) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  CODE int(11) DEFAULT NULL,
  MSG text COLLATE utf8_bin,
  PARENT_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  SCHEDULER_URI varchar(255) COLLATE utf8_bin DEFAULT NULL,
  EXECUTOR_URI varchar(255) COLLATE utf8_bin DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (ID),
  UNIQUE KEY IDX_PROC_INST_ID (PROC_INST_ID),
  KEY IDX_JOB_NAME (JOB_NAME),
  KEY IDX_TENANT_ID (TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_instance_act */

CREATE TABLE attemper_instance_act (
  ID varchar(64) COLLATE utf8_bin NOT NULL,
  ACT_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PARENT_ACT_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  ROOT_PROC_INST_ID varchar(64) COLLATE utf8_bin DEFAULT NULL,
  ACT_ID varchar(255) COLLATE utf8_bin DEFAULT NULL,
  ACT_NAME varchar(255) COLLATE utf8_bin DEFAULT NULL,
  ACT_TYPE varchar(255) COLLATE utf8_bin DEFAULT NULL,
  START_TIME bigint(13) DEFAULT NULL,
  END_TIME bigint(13) DEFAULT NULL,
  DURATION bigint(20) DEFAULT NULL,
  STATUS int(11) DEFAULT NULL,
  LOG_KEY varchar(255) COLLATE utf8_bin DEFAULT NULL,
  LOG_TEXT text COLLATE utf8_bin,
  BIZ_URI varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY IDX_PROC_INST_ID (PROC_INST_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_job */

CREATE TABLE attemper_job (
  JOB_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  DISPLAY_NAME varchar(255) COLLATE utf8_bin NOT NULL,
  CONTENT text COLLATE utf8_bin,
  STATUS int(11) DEFAULT NULL,
  CONCURRENT tinyint(1) DEFAULT NULL,
  ONCE tinyint(1) DEFAULT NULL,
  UPDATE_TIME bigint(13) DEFAULT NULL,
  REMARK varchar(255) COLLATE utf8_bin DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (JOB_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_job_arg */

CREATE TABLE attemper_job_arg (
  JOB_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  ARG_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (JOB_NAME,ARG_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_job_condition */

CREATE TABLE attemper_job_condition (
  JOB_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  CONDITION_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (JOB_NAME,CONDITION_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_job_project */

CREATE TABLE attemper_job_project (
  JOB_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  PROJECT_NAME varchar(64) COLLATE utf8_bin DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (JOB_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_project */

CREATE TABLE attemper_project (
  PROJECT_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  PARENT_PROJECT_NAME varchar(64) COLLATE utf8_bin DEFAULT NULL,
  DISPLAY_NAME varchar(255) COLLATE utf8_bin DEFAULT NULL,
  CONTEXT_PATH varchar(255) COLLATE utf8_bin DEFAULT NULL,
  BIND_EXECUTOR tinyint(1) DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (PROJECT_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_project_executor */

CREATE TABLE attemper_project_executor (
  PROJECT_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  EXECUTOR_URI varchar(255) COLLATE utf8_bin DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (PROJECT_NAME,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_project_info */

CREATE TABLE attemper_project_info (
  PROJECT_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  URI varchar(255) COLLATE utf8_bin NOT NULL,
  URI_TYPE int(11) DEFAULT NULL,
  TENANT_ID varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (PROJECT_NAME,URI,TENANT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_role */

CREATE TABLE attemper_role (
  ROLE_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  DISPLAY_NAME varchar(255) COLLATE utf8_bin DEFAULT NULL,
  REMARK varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (ROLE_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_role_resource */

CREATE TABLE attemper_role_resource (
  ROLE_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  RESOURCE_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (ROLE_NAME,RESOURCE_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_role_tenant */

CREATE TABLE attemper_role_tenant (
  ROLE_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  USER_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (ROLE_NAME,USER_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table attemper_tenant */

CREATE TABLE attemper_tenant (
  USER_NAME varchar(64) COLLATE utf8_bin NOT NULL,
  DISPLAY_NAME varchar(255) COLLATE utf8_bin NOT NULL,
  PASSWORD varchar(255) COLLATE utf8_bin NOT NULL,
  EMAIL varchar(255) COLLATE utf8_bin DEFAULT NULL,
  MOBILE varchar(255) COLLATE utf8_bin DEFAULT NULL,
  STATUS int(11) NOT NULL,
  SUPER_ADMIN tinyint(1) DEFAULT NULL,
  SEND_CONFIG varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (USER_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;