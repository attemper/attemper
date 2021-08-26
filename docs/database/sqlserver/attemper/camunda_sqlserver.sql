
create table ACT_GE_PROPERTY (
    NAME_ nvarchar(64),
    VALUE_ nvarchar(300),
    REV_ int,
    primary key (NAME_)
);

insert into ACT_GE_PROPERTY
values ('schema.version', 'fox', 1);

insert into ACT_GE_PROPERTY
values ('schema.history', 'create(fox)', 1);

insert into ACT_GE_PROPERTY
values ('next.dbid', '1', 1);

insert into ACT_GE_PROPERTY
values ('deployment.lock', '0', 1);

insert into ACT_GE_PROPERTY
values ('history.cleanup.job.lock', '0', 1);

insert into ACT_GE_PROPERTY
values ('startup.lock', '0', 1);

create table ACT_GE_BYTEARRAY (
    ID_ nvarchar(64),
    REV_ int,
    NAME_ nvarchar(255),
    DEPLOYMENT_ID_ nvarchar(64),
    BYTES_ image,
    GENERATED_ tinyint,
    TENANT_ID_ nvarchar(64),
    TYPE_ integer,
    CREATE_TIME_ datetime2,
    ROOT_PROC_INST_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);

/*
create table ACT_GE_SCHEMA_LOG (
    ID_ nvarchar(64),
    TIMESTAMP_ datetime2,
    VERSION_ nvarchar(255),
    primary key (ID_)
);

insert into ACT_GE_SCHEMA_LOG
values ('0', CURRENT_TIMESTAMP, '7.12.0');
*/

create table ACT_RE_DEPLOYMENT (
    ID_ nvarchar(64),
    NAME_ nvarchar(255),
    DEPLOY_TIME_ datetime2,
    SOURCE_ nvarchar(255),
    TENANT_ID_ nvarchar(64),
    primary key (ID_)
);

create table ACT_RU_EXECUTION (
    ID_ nvarchar(64),
    REV_ int,
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    BUSINESS_KEY_ nvarchar(255),
    PARENT_ID_ nvarchar(64),
    PROC_DEF_ID_ nvarchar(64),
    SUPER_EXEC_ nvarchar(64),
    SUPER_CASE_EXEC_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    ACT_ID_ nvarchar(255),
    ACT_INST_ID_ nvarchar(64),
    IS_ACTIVE_ tinyint,
    IS_CONCURRENT_ tinyint,
    IS_SCOPE_ tinyint,
    IS_EVENT_SCOPE_ tinyint,
    SUSPENSION_STATE_ tinyint,
    CACHED_ENT_STATE_ int,
    SEQUENCE_COUNTER_ numeric(19,0),
    TENANT_ID_ nvarchar(64),
    primary key (ID_)
);

create table ACT_RU_JOB (
    ID_ nvarchar(64) NOT NULL,
    REV_ int,
    TYPE_ nvarchar(255) NOT NULL,
    LOCK_EXP_TIME_ datetime2,
    LOCK_OWNER_ nvarchar(255),
    EXCLUSIVE_ bit,
    EXECUTION_ID_ nvarchar(64),
    PROCESS_INSTANCE_ID_ nvarchar(64),
    PROCESS_DEF_ID_ nvarchar(64),
    PROCESS_DEF_KEY_ nvarchar(255),
    RETRIES_ int,
    EXCEPTION_STACK_ID_ nvarchar(64),
    EXCEPTION_MSG_ nvarchar(4000),
    DUEDATE_ datetime2 NULL,
    REPEAT_ nvarchar(255),
    REPEAT_OFFSET_ numeric(19,0) DEFAULT 0,
    HANDLER_TYPE_ nvarchar(255),
    HANDLER_CFG_ nvarchar(4000),
    DEPLOYMENT_ID_ nvarchar(64),
    SUSPENSION_STATE_ tinyint NOT NULL DEFAULT 1,
    PRIORITY_ numeric(19,0) NOT NULL DEFAULT 0,
    JOB_DEF_ID_ nvarchar(64),
    SEQUENCE_COUNTER_ numeric(19,0),
    TENANT_ID_ nvarchar(64),
    CREATE_TIME_ datetime2,
    primary key (ID_)
);

create table ACT_RU_JOBDEF (
    ID_ nvarchar(64) NOT NULL,
    REV_ integer,
    PROC_DEF_ID_ nvarchar(64),
    PROC_DEF_KEY_ nvarchar(255),
    ACT_ID_ nvarchar(255),
    JOB_TYPE_ nvarchar(255) NOT NULL,
    JOB_CONFIGURATION_ nvarchar(255),
    SUSPENSION_STATE_ tinyint,
    JOB_PRIORITY_ numeric(19,0),
    TENANT_ID_ nvarchar(64),
    primary key (ID_)
);

create table ACT_RE_PROCDEF (
    ID_ nvarchar(64) not null,
    REV_ int,
    CATEGORY_ nvarchar(255),
    NAME_ nvarchar(255),
    KEY_ nvarchar(255) not null,
    VERSION_ int not null,
    DEPLOYMENT_ID_ nvarchar(64),
    RESOURCE_NAME_ nvarchar(4000),
    DGRM_RESOURCE_NAME_ nvarchar(4000),
    HAS_START_FORM_KEY_ tinyint,
    SUSPENSION_STATE_ tinyint,
    TENANT_ID_ nvarchar(64),
    VERSION_TAG_ nvarchar(64),
    HISTORY_TTL_ int,
    STARTABLE_ bit NOT NULL default 1,
    primary key (ID_)
);

create table ACT_RU_TASK (
    ID_ nvarchar(64),
    REV_ int,
    EXECUTION_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    PROC_DEF_ID_ nvarchar(64),
    CASE_EXECUTION_ID_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    CASE_DEF_ID_ nvarchar(64),
    NAME_ nvarchar(255),
    PARENT_TASK_ID_ nvarchar(64),
    DESCRIPTION_ nvarchar(4000),
    TASK_DEF_KEY_ nvarchar(255),
    OWNER_ nvarchar(255),
    ASSIGNEE_ nvarchar(255),
    DELEGATION_ nvarchar(64),
    PRIORITY_ int,
    CREATE_TIME_ datetime2,
    DUE_DATE_ datetime2,
    FOLLOW_UP_DATE_ datetime2,
    SUSPENSION_STATE_ int,
    TENANT_ID_ nvarchar(64),
    primary key (ID_)
);
/*
create table ACT_RU_IDENTITYLINK (
    ID_ nvarchar(64),
    REV_ int,
    GROUP_ID_ nvarchar(255),
    TYPE_ nvarchar(255),
    USER_ID_ nvarchar(255),
    TASK_ID_ nvarchar(64),
    PROC_DEF_ID_ nvarchar(64),
    TENANT_ID_ nvarchar(64),
    primary key (ID_)
);
*/
create table ACT_RU_VARIABLE (
    ID_ nvarchar(64) not null,
    REV_ int,
    TYPE_ nvarchar(255) not null,
    NAME_ nvarchar(255) not null,
    EXECUTION_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    CASE_EXECUTION_ID_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    TASK_ID_ nvarchar(64),
    BYTEARRAY_ID_ nvarchar(64),
    DOUBLE_ double precision,
    LONG_ numeric(19,0),
    TEXT_ nvarchar(4000),
    TEXT2_ nvarchar(4000),
    VAR_SCOPE_ nvarchar(64) not null,
    SEQUENCE_COUNTER_ numeric(19,0),
    IS_CONCURRENT_LOCAL_ tinyint,
    TENANT_ID_ nvarchar(64),
    primary key (ID_)
);

create table ACT_RU_EVENT_SUBSCR (
    ID_ nvarchar(64) not null,
    REV_ int,
    EVENT_TYPE_ nvarchar(255) not null,
    EVENT_NAME_ nvarchar(255),
    EXECUTION_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    ACTIVITY_ID_ nvarchar(255),
    CONFIGURATION_ nvarchar(255),
    CREATED_ datetime2 not null,
    TENANT_ID_ nvarchar(64),
    primary key (ID_)
);

create table ACT_RU_INCIDENT (
  ID_ nvarchar(64) not null,
  REV_ int not null,
  INCIDENT_TIMESTAMP_ datetime2 not null,
  INCIDENT_MSG_ nvarchar(4000),
  INCIDENT_TYPE_ nvarchar(255) not null,
  EXECUTION_ID_ nvarchar(64),
  ACTIVITY_ID_ nvarchar(255),
  PROC_INST_ID_ nvarchar(64),
  PROC_DEF_ID_ nvarchar(64),
  CAUSE_INCIDENT_ID_ nvarchar(64),
  ROOT_CAUSE_INCIDENT_ID_ nvarchar(64),
  CONFIGURATION_ nvarchar(255),
  TENANT_ID_ nvarchar(64),
  JOB_DEF_ID_ nvarchar(64),
  primary key (ID_)
);
/*
create table ACT_RU_AUTHORIZATION (
  ID_ nvarchar(64) not null,
  REV_ int,
  TYPE_ int not null,
  GROUP_ID_ nvarchar(255),
  USER_ID_ nvarchar(255),
  RESOURCE_TYPE_ int not null,
  RESOURCE_ID_ nvarchar(255),
  PERMS_ int,
  primary key (ID_)
);

create table ACT_RU_FILTER (
  ID_ nvarchar(64) not null,
  REV_ integer not null,
  RESOURCE_TYPE_ nvarchar(255) not null,
  NAME_ nvarchar(255) not null,
  OWNER_ nvarchar(255),
  QUERY_ nvarchar(max) not null,
  PROPERTIES_ nvarchar(max),
  primary key (ID_)
);
*/
create table ACT_RU_METER_LOG (
  ID_ nvarchar(64) not null,
  NAME_ nvarchar(64) not null,
  REPORTER_ nvarchar(255),
  VALUE_ numeric(19,0),
  TIMESTAMP_ datetime2,
  MILLISECONDS_ numeric(19,0) DEFAULT 0,
  primary key (ID_)
);
/*
create table ACT_RU_EXT_TASK (
  ID_ nvarchar(64) not null,
  REV_ integer not null,
  WORKER_ID_ nvarchar(255),
  TOPIC_NAME_ nvarchar(255),
  RETRIES_ int,
  ERROR_MSG_ nvarchar(4000),
  ERROR_DETAILS_ID_ nvarchar(64),
  LOCK_EXP_TIME_ datetime2,
  SUSPENSION_STATE_ tinyint,
  EXECUTION_ID_ nvarchar(64),
  PROC_INST_ID_ nvarchar(64),
  PROC_DEF_ID_ nvarchar(64),
  PROC_DEF_KEY_ nvarchar(255),
  ACT_ID_ nvarchar(255),
  ACT_INST_ID_ nvarchar(64),
  TENANT_ID_ nvarchar(64),
  PRIORITY_ numeric(19,0) NOT NULL DEFAULT 0,
  primary key (ID_)
);
*/
create table ACT_RU_BATCH (
  ID_ nvarchar(64) not null,
  REV_ int not null,
  TYPE_ nvarchar(255),
  TOTAL_JOBS_ int,
  JOBS_CREATED_ int,
  JOBS_PER_SEED_ int,
  INVOCATIONS_PER_JOB_ int,
  SEED_JOB_DEF_ID_ nvarchar(64),
  BATCH_JOB_DEF_ID_ nvarchar(64),
  MONITOR_JOB_DEF_ID_ nvarchar(64),
  SUSPENSION_STATE_ tinyint,
  CONFIGURATION_ nvarchar(255),
  TENANT_ID_ nvarchar(64),
  CREATE_USER_ID_ nvarchar(255),
  primary key (ID_)
);

create index ACT_IDX_EXEC_ROOT_PI on ACT_RU_EXECUTION(ROOT_PROC_INST_ID_);
create index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION(BUSINESS_KEY_);
create index ACT_IDX_EXEC_TENANT_ID on ACT_RU_EXECUTION(TENANT_ID_);
create index ACT_IDX_TASK_CREATE on ACT_RU_TASK(CREATE_TIME_);
create index ACT_IDX_TASK_ASSIGNEE on ACT_RU_TASK(ASSIGNEE_);
create index ACT_IDX_TASK_TENANT_ID on ACT_RU_TASK(TENANT_ID_);
/*
create index ACT_IDX_IDENT_LNK_USER on ACT_RU_IDENTITYLINK(USER_ID_);
create index ACT_IDX_IDENT_LNK_GROUP on ACT_RU_IDENTITYLINK(GROUP_ID_);
*/
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on ACT_RU_EVENT_SUBSCR(CONFIGURATION_);
create index ACT_IDX_EVENT_SUBSCR_TENANT_ID on ACT_RU_EVENT_SUBSCR(TENANT_ID_);
create index ACT_IDX_VARIABLE_TASK_ID on ACT_RU_VARIABLE(TASK_ID_);
create index ACT_IDX_VARIABLE_TENANT_ID on ACT_RU_VARIABLE(TENANT_ID_);
/*
create index ACT_IDX_ATHRZ_PROCEDEF on ACT_RU_IDENTITYLINK(PROC_DEF_ID_);
*/
create index ACT_IDX_INC_CONFIGURATION on ACT_RU_INCIDENT(CONFIGURATION_);
create index ACT_IDX_INC_TENANT_ID on ACT_RU_INCIDENT(TENANT_ID_);
-- CAM-5914
create index ACT_IDX_JOB_EXECUTION_ID on ACT_RU_JOB(EXECUTION_ID_);
create index ACT_IDX_JOB_PROCINST on ACT_RU_JOB(PROCESS_INSTANCE_ID_);
create index ACT_IDX_JOB_TENANT_ID on ACT_RU_JOB(TENANT_ID_);
create index ACT_IDX_JOBDEF_TENANT_ID on ACT_RU_JOBDEF(TENANT_ID_);
/*
create unique index ACT_UNIQ_AUTH_USER on ACT_RU_AUTHORIZATION (TYPE_,USER_ID_,RESOURCE_TYPE_,RESOURCE_ID_) where USER_ID_ is not null;
create unique index ACT_UNIQ_AUTH_GROUP on ACT_RU_AUTHORIZATION (TYPE_,GROUP_ID_,RESOURCE_TYPE_,RESOURCE_ID_) where GROUP_ID_ is not null;
*/
create unique index ACT_UNIQ_VARIABLE on ACT_RU_VARIABLE(VAR_SCOPE_, NAME_);

-- new metric milliseconds column
CREATE INDEX ACT_IDX_METER_LOG_MS ON ACT_RU_METER_LOG(MILLISECONDS_);
CREATE INDEX ACT_IDX_METER_LOG_NAME_MS ON ACT_RU_METER_LOG(NAME_, MILLISECONDS_);
CREATE INDEX ACT_IDX_METER_LOG_REPORT ON ACT_RU_METER_LOG(NAME_, REPORTER_, MILLISECONDS_);

-- old metric timestamp column
CREATE INDEX ACT_IDX_METER_LOG_TIME ON ACT_RU_METER_LOG(TIMESTAMP_);
CREATE INDEX ACT_IDX_METER_LOG ON ACT_RU_METER_LOG(NAME_, TIMESTAMP_);
/*
create index ACT_IDX_EXT_TASK_TOPIC on ACT_RU_EXT_TASK(TOPIC_NAME_);
create index ACT_IDX_EXT_TASK_TENANT_ID on ACT_RU_EXT_TASK(TENANT_ID_);
create index ACT_IDX_EXT_TASK_PRIORITY ON ACT_RU_EXT_TASK(PRIORITY_);
create index ACT_IDX_EXT_TASK_ERR_DETAILS ON ACT_RU_EXT_TASK(ERROR_DETAILS_ID_);
create index ACT_IDX_AUTH_GROUP_ID on ACT_RU_AUTHORIZATION(GROUP_ID_);
*/
create index ACT_IDX_JOB_JOB_DEF_ID on ACT_RU_JOB(JOB_DEF_ID_);
/*
alter table ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
    foreign key (DEPLOYMENT_ID_)
    references ACT_RE_DEPLOYMENT (ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PARENT
    foreign key (PARENT_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_SUPER
    foreign key (SUPER_EXEC_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF
    foreign key (PROC_DEF_ID_)
    references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_TSKASS_TASK
    foreign key (TASK_ID_)
    references ACT_RU_TASK (ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_ATHRZ_PROCEDEF
    foreign key (PROC_DEF_ID_)
    references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_EXE
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
  add constraint ACT_FK_TASK_PROCDEF
  foreign key (PROC_DEF_ID_)
  references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_EXE
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION(ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_BYTEARRAY
    foreign key (BYTEARRAY_ID_)
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXCEPTION
    foreign key (EXCEPTION_STACK_ID_)
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_EVENT_SUBSCR
    add constraint ACT_FK_EVENT_EXEC
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION(ID_);

alter table ACT_RU_INCIDENT
    add constraint ACT_FK_INC_EXE
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_INCIDENT
    add constraint ACT_FK_INC_PROCINST
    foreign key (PROC_INST_ID_)
    references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_INCIDENT
    add constraint ACT_FK_INC_PROCDEF
    foreign key (PROC_DEF_ID_)
    references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_INCIDENT
    add constraint ACT_FK_INC_CAUSE
    foreign key (CAUSE_INCIDENT_ID_)
    references ACT_RU_INCIDENT (ID_);

alter table ACT_RU_INCIDENT
    add constraint ACT_FK_INC_RCAUSE
    foreign key (ROOT_CAUSE_INCIDENT_ID_)
    references ACT_RU_INCIDENT (ID_);

alter table ACT_RU_EXT_TASK
    add constraint ACT_FK_EXT_TASK_ERROR_DETAILS
    foreign key (ERROR_DETAILS_ID_)
    references ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_INCIDENT_JOB_DEF on ACT_RU_INCIDENT(JOB_DEF_ID_);
alter table ACT_RU_INCIDENT
    add constraint ACT_FK_INC_JOB_DEF
    foreign key (JOB_DEF_ID_)
    references ACT_RU_JOBDEF (ID_);

alter table ACT_RU_EXT_TASK
    add constraint ACT_FK_EXT_TASK_EXE
    foreign key (EXECUTION_ID_)
    references ACT_RU_EXECUTION (ID_);

create index ACT_IDX_BATCH_SEED_JOB_DEF ON ACT_RU_BATCH(SEED_JOB_DEF_ID_);
alter table ACT_RU_BATCH
    add constraint ACT_FK_BATCH_SEED_JOB_DEF
    foreign key (SEED_JOB_DEF_ID_)
    references ACT_RU_JOBDEF (ID_);

create index ACT_IDX_BATCH_MONITOR_JOB_DEF ON ACT_RU_BATCH(MONITOR_JOB_DEF_ID_);
alter table ACT_RU_BATCH
    add constraint ACT_FK_BATCH_MONITOR_JOB_DEF
    foreign key (MONITOR_JOB_DEF_ID_)
    references ACT_RU_JOBDEF (ID_);

create index ACT_IDX_BATCH_JOB_DEF ON ACT_RU_BATCH(BATCH_JOB_DEF_ID_);
alter table ACT_RU_BATCH
    add constraint ACT_FK_BATCH_JOB_DEF
    foreign key (BATCH_JOB_DEF_ID_)
    references ACT_RU_JOBDEF (ID_);
*/
-- indexes for concurrency problems - https://app.camunda.com/jira/browse/CAM-1646 --
create index ACT_IDX_EXECUTION_PROC on ACT_RU_EXECUTION(PROC_DEF_ID_);
create index ACT_IDX_EXECUTION_PARENT on ACT_RU_EXECUTION(PARENT_ID_);
create index ACT_IDX_EXECUTION_SUPER on ACT_RU_EXECUTION(SUPER_EXEC_);
create index ACT_IDX_EXECUTION_PROCINST on ACT_RU_EXECUTION(PROC_INST_ID_);
create index ACT_IDX_EVENT_SUBSCR_EXEC on ACT_RU_EVENT_SUBSCR(EXECUTION_ID_);
create index ACT_IDX_BA_DEPLOYMENT on ACT_GE_BYTEARRAY(DEPLOYMENT_ID_);
/*
create index ACT_IDX_IDENT_LNK_TASK on ACT_RU_IDENTITYLINK(TASK_ID_);
*/
create index ACT_IDX_INCIDENT_EXEC on ACT_RU_INCIDENT(EXECUTION_ID_);
create index ACT_IDX_INCIDENT_PROCINST on ACT_RU_INCIDENT(PROC_INST_ID_);
create index ACT_IDX_INCIDENT_PROC_DEF_ID on ACT_RU_INCIDENT(PROC_DEF_ID_);
create index ACT_IDX_INCIDENT_CAUSE on ACT_RU_INCIDENT(CAUSE_INCIDENT_ID_);
create index ACT_IDX_INCIDENT_ROOT_CAUSE on ACT_RU_INCIDENT(ROOT_CAUSE_INCIDENT_ID_);
create index ACT_IDX_JOB_EXCEPTION_STACK on ACT_RU_JOB(EXCEPTION_STACK_ID_);
create index ACT_IDX_VARIABLE_BA on ACT_RU_VARIABLE(BYTEARRAY_ID_);
create index ACT_IDX_VARIABLE_EXEC on ACT_RU_VARIABLE(EXECUTION_ID_);
create index ACT_IDX_VARIABLE_PROCINST on ACT_RU_VARIABLE(PROC_INST_ID_);
create index ACT_IDX_TASK_EXEC on ACT_RU_TASK(EXECUTION_ID_);
create index ACT_IDX_TASK_PROCINST on ACT_RU_TASK(PROC_INST_ID_);
create index ACT_IDX_TASK_PROC_DEF_ID on ACT_RU_TASK(PROC_DEF_ID_);
-- index for deadlock problem - https://app.camunda.com/jira/browse/CAM-4440 --
/*
create index ACT_IDX_AUTH_RESOURCE_ID on ACT_RU_AUTHORIZATION(RESOURCE_ID_);
-- index to prevent deadlock on fk constraint - https://app.camunda.com/jira/browse/CAM-5440 --
create index ACT_IDX_EXT_TASK_EXEC on ACT_RU_EXT_TASK(EXECUTION_ID_);
*/
-- indexes to improve deployment
create index ACT_IDX_BYTEARRAY_ROOT_PI on ACT_GE_BYTEARRAY(ROOT_PROC_INST_ID_);
create index ACT_IDX_BYTEARRAY_RM_TIME on ACT_GE_BYTEARRAY(REMOVAL_TIME_);
create index ACT_IDX_BYTEARRAY_NAME on ACT_GE_BYTEARRAY(NAME_);
create index ACT_IDX_DEPLOYMENT_NAME on ACT_RE_DEPLOYMENT(NAME_);
create index ACT_IDX_DEPLOYMENT_TENANT_ID on ACT_RE_DEPLOYMENT(TENANT_ID_);
create index ACT_IDX_JOBDEF_PROC_DEF_ID ON ACT_RU_JOBDEF(PROC_DEF_ID_);
create index ACT_IDX_JOB_HANDLER_TYPE ON ACT_RU_JOB(HANDLER_TYPE_);
create index ACT_IDX_EVENT_SUBSCR_EVT_NAME ON ACT_RU_EVENT_SUBSCR(EVENT_NAME_);
create index ACT_IDX_PROCDEF_DEPLOYMENT_ID ON ACT_RE_PROCDEF(DEPLOYMENT_ID_);
create index ACT_IDX_PROCDEF_TENANT_ID ON ACT_RE_PROCDEF(TENANT_ID_);
create index ACT_IDX_PROCDEF_VER_TAG ON ACT_RE_PROCDEF(VERSION_TAG_);






















create table ACT_HI_PROCINST (
    ID_ nvarchar(64) not null,
    PROC_INST_ID_ nvarchar(64) not null,
    BUSINESS_KEY_ nvarchar(255),
    PROC_DEF_KEY_ nvarchar(255),
    PROC_DEF_ID_ nvarchar(64) not null,
    START_TIME_ datetime2 not null,
    END_TIME_ datetime2,
    REMOVAL_TIME_ datetime2,
    DURATION_ numeric(19,0),
    START_USER_ID_ nvarchar(255),
    START_ACT_ID_ nvarchar(255),
    END_ACT_ID_ nvarchar(255),
    SUPER_PROCESS_INSTANCE_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    SUPER_CASE_INSTANCE_ID_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    DELETE_REASON_ nvarchar(4000),
    TENANT_ID_ nvarchar(64),
    STATE_ nvarchar(255),
    primary key (ID_),
    unique (PROC_INST_ID_)
);

create table ACT_HI_ACTINST (
    ID_ nvarchar(64) not null,
    PARENT_ACT_INST_ID_ nvarchar(64),
    PROC_DEF_KEY_ nvarchar(255),
    PROC_DEF_ID_ nvarchar(64) not null,
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64) not null,
    EXECUTION_ID_ nvarchar(64) not null,
    ACT_ID_ nvarchar(255) not null,
    TASK_ID_ nvarchar(64),
    CALL_PROC_INST_ID_ nvarchar(64),
    CALL_CASE_INST_ID_ nvarchar(64),
    ACT_NAME_ nvarchar(255),
    ACT_TYPE_ nvarchar(255) not null,
    ASSIGNEE_ nvarchar(64),
    START_TIME_ datetime2 not null,
    END_TIME_ datetime2,
    DURATION_ numeric(19,0),
    ACT_INST_STATE_ tinyint,
    SEQUENCE_COUNTER_ numeric(19,0),
    TENANT_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);
/*
create table ACT_HI_TASKINST (
    ID_ nvarchar(64) not null,
    TASK_DEF_KEY_ nvarchar(255),
    PROC_DEF_KEY_ nvarchar(255),
    PROC_DEF_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    EXECUTION_ID_ nvarchar(64),
    CASE_DEF_KEY_ nvarchar(255),
    CASE_DEF_ID_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    CASE_EXECUTION_ID_ nvarchar(64),
    ACT_INST_ID_ nvarchar(64),
    NAME_ nvarchar(255),
    PARENT_TASK_ID_ nvarchar(64),
    DESCRIPTION_ nvarchar(4000),
    OWNER_ nvarchar(255),
    ASSIGNEE_ nvarchar(255),
    START_TIME_ datetime2 not null,
    END_TIME_ datetime2,
    DURATION_ numeric(19,0),
    DELETE_REASON_ nvarchar(4000),
    PRIORITY_ int,
    DUE_DATE_ datetime2,
    FOLLOW_UP_DATE_ datetime2,
    TENANT_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);
*/
create table ACT_HI_VARINST (
    ID_ nvarchar(64) not null,
    PROC_DEF_KEY_ nvarchar(255),
    PROC_DEF_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    EXECUTION_ID_ nvarchar(64),
    CASE_DEF_KEY_ nvarchar(255),
    CASE_DEF_ID_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    CASE_EXECUTION_ID_ nvarchar(64),
    ACT_INST_ID_ nvarchar(64),
    TASK_ID_ nvarchar(64),
    NAME_ nvarchar(255) not null,
    VAR_TYPE_ nvarchar(100),
    CREATE_TIME_ datetime2,
    REV_ int,
    BYTEARRAY_ID_ nvarchar(64),
    DOUBLE_ double precision,
    LONG_ numeric(19,0),
    TEXT_ nvarchar(4000),
    TEXT2_ nvarchar(4000),
    TENANT_ID_ nvarchar(64),
    STATE_ nvarchar(20),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);

create table ACT_HI_DETAIL (
    ID_ nvarchar(64) not null,
    TYPE_ nvarchar(255) not null,
    PROC_DEF_KEY_ nvarchar(255),
    PROC_DEF_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    EXECUTION_ID_ nvarchar(64),
    CASE_DEF_KEY_ nvarchar(255),
    CASE_DEF_ID_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    CASE_EXECUTION_ID_ nvarchar(64),
    TASK_ID_ nvarchar(64),
    ACT_INST_ID_ nvarchar(64),
    VAR_INST_ID_ nvarchar(64),
    NAME_ nvarchar(255) not null,
    VAR_TYPE_ nvarchar(255),
    REV_ int,
    TIME_ datetime2 not null,
    BYTEARRAY_ID_ nvarchar(64),
    DOUBLE_ double precision,
    LONG_ numeric(19,0),
    TEXT_ nvarchar(4000),
    TEXT2_ nvarchar(4000),
    SEQUENCE_COUNTER_ numeric(19,0),
    TENANT_ID_ nvarchar(64),
    OPERATION_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);
/*
create table ACT_HI_IDENTITYLINK (
    ID_ nvarchar(64) not null,
    TIMESTAMP_ datetime2 not null,
    TYPE_ nvarchar(255),
    USER_ID_ nvarchar(255),
    GROUP_ID_ nvarchar(255),
    TASK_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_DEF_ID_ nvarchar(64),
    OPERATION_TYPE_ nvarchar(64),
    ASSIGNER_ID_ nvarchar(64),
    PROC_DEF_KEY_ nvarchar(255),
    TENANT_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);
create table ACT_HI_COMMENT (
    ID_ nvarchar(64) not null,
    TYPE_ nvarchar(255),
    TIME_ datetime2 not null,
    USER_ID_ nvarchar(255),
    TASK_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    ACTION_ nvarchar(255),
    MESSAGE_ nvarchar(4000),
    FULL_MSG_ image,
    TENANT_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);

create table ACT_HI_ATTACHMENT (
    ID_ nvarchar(64) not null,
    REV_ integer,
    USER_ID_ nvarchar(255),
    NAME_ nvarchar(255),
    DESCRIPTION_ nvarchar(4000),
    TYPE_ nvarchar(255),
    TASK_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    URL_ nvarchar(4000),
    CONTENT_ID_ nvarchar(64),
    TENANT_ID_ nvarchar(64),
    CREATE_TIME_ datetime2,
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);

create table ACT_HI_OP_LOG (
    ID_ nvarchar(64) not null,
    DEPLOYMENT_ID_ nvarchar(64),
    PROC_DEF_ID_ nvarchar(64),
    PROC_DEF_KEY_ nvarchar(255),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    EXECUTION_ID_ nvarchar(64),
    CASE_DEF_ID_ nvarchar(64),
    CASE_INST_ID_ nvarchar(64),
    CASE_EXECUTION_ID_ nvarchar(64),
    TASK_ID_ nvarchar(64),
    JOB_ID_ nvarchar(64),
    JOB_DEF_ID_ nvarchar(64),
    BATCH_ID_ nvarchar(64),
    USER_ID_ nvarchar(255),
    TIMESTAMP_ datetime2 not null,
    OPERATION_TYPE_ nvarchar(64),
    OPERATION_ID_ nvarchar(64),
    ENTITY_TYPE_ nvarchar(30),
    PROPERTY_ nvarchar(64),
    ORG_VALUE_ nvarchar(4000),
    NEW_VALUE_ nvarchar(4000),
    TENANT_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
	CATEGORY_ nvarchar(64),
	EXTERNAL_TASK_ID_ nvarchar(64),
	ANNOTATION_ nvarchar(4000),
    primary key (ID_)
);
*/
create table ACT_HI_INCIDENT (
  ID_ nvarchar(64) not null,
  PROC_DEF_KEY_ nvarchar(255),
  PROC_DEF_ID_ nvarchar(64),
  ROOT_PROC_INST_ID_ nvarchar(64),
  PROC_INST_ID_ nvarchar(64),
  EXECUTION_ID_ nvarchar(64),
  CREATE_TIME_ datetime2 not null,
  END_TIME_ datetime2,
  INCIDENT_MSG_ nvarchar(4000),
  INCIDENT_TYPE_ nvarchar(255) not null,
  ACTIVITY_ID_ nvarchar(255),
  CAUSE_INCIDENT_ID_ nvarchar(64),
  ROOT_CAUSE_INCIDENT_ID_ nvarchar(64),
  CONFIGURATION_ nvarchar(255),
  HISTORY_CONFIGURATION_ nvarchar(255),
  INCIDENT_STATE_ integer,
  TENANT_ID_ nvarchar(64),
  JOB_DEF_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
  primary key (ID_)
);

create table ACT_HI_JOB_LOG (
    ID_ nvarchar(64) not null,
    TIMESTAMP_ datetime2 not null,
    JOB_ID_ nvarchar(64) not null,
    JOB_DUEDATE_ datetime2,
    JOB_RETRIES_ integer,
    JOB_PRIORITY_ numeric(19,0) NOT NULL DEFAULT 0,
    JOB_EXCEPTION_MSG_ nvarchar(4000),
    JOB_EXCEPTION_STACK_ID_ nvarchar(64),
    JOB_STATE_ integer,
    JOB_DEF_ID_ nvarchar(64),
    JOB_DEF_TYPE_ nvarchar(255),
    JOB_DEF_CONFIGURATION_ nvarchar(255),
    ACT_ID_ nvarchar(255),
    EXECUTION_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROCESS_INSTANCE_ID_ nvarchar(64),
    PROCESS_DEF_ID_ nvarchar(64),
    PROCESS_DEF_KEY_ nvarchar(255),
    DEPLOYMENT_ID_ nvarchar(64),
    SEQUENCE_COUNTER_ numeric(19,0),
    TENANT_ID_ nvarchar(64),
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);

create table ACT_HI_BATCH (
    ID_ nvarchar(64) not null,
    TYPE_ nvarchar(255),
    TOTAL_JOBS_ int,
    JOBS_PER_SEED_ int,
    INVOCATIONS_PER_JOB_ int,
    SEED_JOB_DEF_ID_ nvarchar(64),
    MONITOR_JOB_DEF_ID_ nvarchar(64),
    BATCH_JOB_DEF_ID_ nvarchar(64),
    TENANT_ID_  nvarchar(64),
    CREATE_USER_ID_ nvarchar(255),
    START_TIME_ datetime2 not null,
    END_TIME_ datetime2,
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);
/*
create table ACT_HI_EXT_TASK_LOG (
    ID_ nvarchar(64) not null,
    TIMESTAMP_ datetime2 not null,
    EXT_TASK_ID_ nvarchar(64) not null,
    RETRIES_ integer,
    TOPIC_NAME_ nvarchar(255),
    WORKER_ID_ nvarchar(255),
    PRIORITY_ numeric(19,0) NOT NULL DEFAULT 0,
    ERROR_MSG_ nvarchar(4000),
    ERROR_DETAILS_ID_ nvarchar(64),
    ACT_ID_ nvarchar(255),
    ACT_INST_ID_ nvarchar(64),
    EXECUTION_ID_ nvarchar(64),
    ROOT_PROC_INST_ID_ nvarchar(64),
    PROC_INST_ID_ nvarchar(64),
    PROC_DEF_ID_ nvarchar(64),
    PROC_DEF_KEY_ nvarchar(255),
    TENANT_ID_ nvarchar(64),
    STATE_ integer,
    REMOVAL_TIME_ datetime2,
    primary key (ID_)
);
*/
create index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST(END_TIME_);
create index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST(BUSINESS_KEY_);
create index ACT_IDX_HI_PRO_INST_TENANT_ID on ACT_HI_PROCINST(TENANT_ID_);
create index ACT_IDX_HI_PRO_INST_PROC_DEF_KEY on ACT_HI_PROCINST(PROC_DEF_KEY_);
create index ACT_IDX_HI_PRO_INST_PROC_TIME on ACT_HI_PROCINST(START_TIME_, END_TIME_);
create index ACT_IDX_HI_PI_PDEFID_END_TIME on ACT_HI_PROCINST(PROC_DEF_ID_, END_TIME_);
create index ACT_IDX_HI_PRO_INST_ROOT_PI on ACT_HI_PROCINST(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_PRO_INST_RM_TIME on ACT_HI_PROCINST(REMOVAL_TIME_);

create index ACT_IDX_HI_ACTINST_ROOT_PI on ACT_HI_ACTINST(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_ACT_INST_START_END on ACT_HI_ACTINST(START_TIME_, END_TIME_);
create index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST(END_TIME_);
create index ACT_IDX_HI_ACT_INST_PROCINST on ACT_HI_ACTINST(PROC_INST_ID_, ACT_ID_);
create index ACT_IDX_HI_ACT_INST_COMP on ACT_HI_ACTINST(EXECUTION_ID_, ACT_ID_, END_TIME_, ID_);
create index ACT_IDX_HI_ACT_INST_STATS on ACT_HI_ACTINST(PROC_DEF_ID_, PROC_INST_ID_, ACT_ID_, END_TIME_, ACT_INST_STATE_);
create index ACT_IDX_HI_ACT_INST_TENANT_ID on ACT_HI_ACTINST(TENANT_ID_);
create index ACT_IDX_HI_ACT_INST_PROC_DEF_KEY on ACT_HI_ACTINST(PROC_DEF_KEY_);
create index ACT_IDX_HI_AI_PDEFID_END_TIME on ACT_HI_ACTINST(PROC_DEF_ID_, END_TIME_);
create index ACT_IDX_HI_ACT_INST_RM_TIME on ACT_HI_ACTINST(REMOVAL_TIME_);
/*
create index ACT_IDX_HI_TASKINST_ROOT_PI on ACT_HI_TASKINST(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_TASK_INST_TENANT_ID on ACT_HI_TASKINST(TENANT_ID_);
create index ACT_IDX_HI_TASK_INST_PROC_DEF_KEY on ACT_HI_TASKINST(PROC_DEF_KEY_);
create index ACT_IDX_HI_TASKINST_PROCINST on ACT_HI_TASKINST(PROC_INST_ID_);
create index ACT_IDX_HI_TASKINSTID_PROCINST on ACT_HI_TASKINST(ID_,PROC_INST_ID_);
create index ACT_IDX_HI_TASK_INST_RM_TIME on ACT_HI_TASKINST(REMOVAL_TIME_);
create index ACT_IDX_HI_TASK_INST_START on ACT_HI_TASKINST(START_TIME_);
create index ACT_IDX_HI_TASK_INST_END on ACT_HI_TASKINST(END_TIME_);

create index ACT_IDX_HI_IDENT_LNK_ROOT_PI on ACT_HI_IDENTITYLINK(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_IDENT_LNK_USER on ACT_HI_IDENTITYLINK(USER_ID_);
create index ACT_IDX_HI_IDENT_LNK_GROUP on ACT_HI_IDENTITYLINK(GROUP_ID_);
create index ACT_IDX_HI_IDENT_LNK_TENANT_ID on ACT_HI_IDENTITYLINK(TENANT_ID_);
create index ACT_IDX_HI_IDENT_LNK_PROC_DEF_KEY on ACT_HI_IDENTITYLINK(PROC_DEF_KEY_);
create index ACT_IDX_HI_IDENT_LINK_TASK on ACT_HI_IDENTITYLINK(TASK_ID_);
create index ACT_IDX_HI_IDENT_LINK_RM_TIME on ACT_HI_IDENTITYLINK(REMOVAL_TIME_);
create index ACT_IDX_HI_IDENT_LNK_TIMESTAMP on ACT_HI_IDENTITYLINK(TIMESTAMP_);
*/
create index ACT_IDX_HI_DETAIL_ROOT_PI on ACT_HI_DETAIL(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL(PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL(ACT_INST_ID_);
create index ACT_IDX_HI_DETAIL_CASE_INST on ACT_HI_DETAIL(CASE_INST_ID_);
create index ACT_IDX_HI_DETAIL_CASE_EXEC on ACT_HI_DETAIL(CASE_EXECUTION_ID_);
create index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL(TIME_);
create index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL(NAME_);
create index ACT_IDX_HI_DETAIL_TASK_ID on ACT_HI_DETAIL(TASK_ID_);
create index ACT_IDX_HI_DETAIL_TENANT_ID on ACT_HI_DETAIL(TENANT_ID_);
create index ACT_IDX_HI_DETAIL_PROC_DEF_KEY on ACT_HI_DETAIL(PROC_DEF_KEY_);
create index ACT_IDX_HI_DETAIL_BYTEAR on ACT_HI_DETAIL(BYTEARRAY_ID_);
create index ACT_IDX_HI_DETAIL_RM_TIME on ACT_HI_DETAIL(REMOVAL_TIME_);
create index ACT_IDX_HI_DETAIL_TASK_BYTEAR on ACT_HI_DETAIL(BYTEARRAY_ID_, TASK_ID_);
create index ACT_IDX_HI_DETAIL_VAR_INST_ID on ACT_HI_DETAIL(VAR_INST_ID_);

create index ACT_IDX_HI_VARINST_ROOT_PI on ACT_HI_VARINST(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_PROC_INST on ACT_HI_VARINST(PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_NAME_TYPE on ACT_HI_VARINST(NAME_, VAR_TYPE_);
create index ACT_IDX_HI_CASEVAR_CASE_INST on ACT_HI_VARINST(CASE_INST_ID_);
create index ACT_IDX_HI_VAR_INST_TENANT_ID on ACT_HI_VARINST(TENANT_ID_);
create index ACT_IDX_HI_VAR_INST_PROC_DEF_KEY on ACT_HI_VARINST(PROC_DEF_KEY_);
create index ACT_IDX_HI_VARINST_BYTEAR on ACT_HI_VARINST(BYTEARRAY_ID_);
create index ACT_IDX_HI_VARINST_RM_TIME on ACT_HI_VARINST(REMOVAL_TIME_);

create index ACT_IDX_HI_INCIDENT_TENANT_ID on ACT_HI_INCIDENT(TENANT_ID_);
create index ACT_IDX_HI_INCIDENT_PROC_DEF_KEY on ACT_HI_INCIDENT(PROC_DEF_KEY_);
create index ACT_IDX_HI_INCIDENT_ROOT_PI on ACT_HI_INCIDENT(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_INCIDENT_PROCINST on ACT_HI_INCIDENT(PROC_INST_ID_);
create index ACT_IDX_HI_INCIDENT_RM_TIME on ACT_HI_INCIDENT(REMOVAL_TIME_);

create index ACT_IDX_HI_JOB_LOG_ROOT_PI on ACT_HI_JOB_LOG(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_JOB_LOG_PROCINST on ACT_HI_JOB_LOG(PROCESS_INSTANCE_ID_);
create index ACT_IDX_HI_JOB_LOG_PROCDEF on ACT_HI_JOB_LOG(PROCESS_DEF_ID_);
create index ACT_IDX_HI_JOB_LOG_TENANT_ID on ACT_HI_JOB_LOG(TENANT_ID_);
create index ACT_IDX_HI_JOB_LOG_JOB_DEF_ID on ACT_HI_JOB_LOG(JOB_DEF_ID_);
create index ACT_IDX_HI_JOB_LOG_PROC_DEF_KEY on ACT_HI_JOB_LOG(PROCESS_DEF_KEY_);
create index ACT_IDX_HI_JOB_LOG_EX_STACK on ACT_HI_JOB_LOG(JOB_EXCEPTION_STACK_ID_);
create index ACT_IDX_HI_JOB_LOG_RM_TIME on ACT_HI_JOB_LOG(REMOVAL_TIME_);
create index ACT_IDX_HI_JOB_LOG_JOB_CONF on ACT_HI_JOB_LOG(JOB_DEF_CONFIGURATION_);

create index ACT_HI_BAT_RM_TIME on ACT_HI_BATCH(REMOVAL_TIME_);
/*
create index ACT_HI_EXT_TASK_LOG_ROOT_PI on ACT_HI_EXT_TASK_LOG(ROOT_PROC_INST_ID_);
create index ACT_HI_EXT_TASK_LOG_PROCINST on ACT_HI_EXT_TASK_LOG(PROC_INST_ID_);
create index ACT_HI_EXT_TASK_LOG_PROCDEF on ACT_HI_EXT_TASK_LOG(PROC_DEF_ID_);
create index ACT_HI_EXT_TASK_LOG_PROC_DEF_KEY on ACT_HI_EXT_TASK_LOG(PROC_DEF_KEY_);
create index ACT_HI_EXT_TASK_LOG_TENANT_ID on ACT_HI_EXT_TASK_LOG(TENANT_ID_);
create index ACT_IDX_HI_EXTTASKLOG_ERRORDET on ACT_HI_EXT_TASK_LOG(ERROR_DETAILS_ID_);
create index ACT_HI_EXT_TASK_LOG_RM_TIME on ACT_HI_EXT_TASK_LOG(REMOVAL_TIME_);

create index ACT_IDX_HI_OP_LOG_ROOT_PI on ACT_HI_OP_LOG(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_OP_LOG_PROCINST on ACT_HI_OP_LOG(PROC_INST_ID_);
create index ACT_IDX_HI_OP_LOG_PROCDEF on ACT_HI_OP_LOG(PROC_DEF_ID_);
create index ACT_IDX_HI_OP_LOG_TASK on ACT_HI_OP_LOG(TASK_ID_);
create index ACT_IDX_HI_OP_LOG_RM_TIME on ACT_HI_OP_LOG(REMOVAL_TIME_);
create index ACT_IDX_HI_OP_LOG_TIMESTAMP on ACT_HI_OP_LOG(TIMESTAMP_);
create index ACT_IDX_HI_OP_LOG_USER_ID on ACT_HI_OP_LOG(USER_ID_);
create index ACT_IDX_HI_OP_LOG_OP_TYPE on ACT_HI_OP_LOG(OPERATION_TYPE_);
create index ACT_IDX_HI_OP_LOG_ENTITY_TYPE on ACT_HI_OP_LOG(ENTITY_TYPE_);

create index ACT_IDX_HI_COMMENT_TASK on ACT_HI_COMMENT(TASK_ID_);
create index ACT_IDX_HI_COMMENT_ROOT_PI on ACT_HI_COMMENT(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_COMMENT_PROCINST on ACT_HI_COMMENT(PROC_INST_ID_);
create index ACT_IDX_HI_COMMENT_RM_TIME on ACT_HI_COMMENT(REMOVAL_TIME_);

create index ACT_IDX_HI_ATTACHMENT_CONTENT on ACT_HI_ATTACHMENT(CONTENT_ID_);
create index ACT_IDX_HI_ATTACHMENT_ROOT_PI on ACT_HI_ATTACHMENT(ROOT_PROC_INST_ID_);
create index ACT_IDX_HI_ATTACHMENT_PROCINST on ACT_HI_ATTACHMENT(PROC_INST_ID_);
create index ACT_IDX_HI_ATTACHMENT_TASK on ACT_HI_ATTACHMENT(TASK_ID_);
create index ACT_IDX_HI_ATTACHMENT_RM_TIME on ACT_HI_ATTACHMENT(REMOVAL_TIME_);
*/

/* 7.13 */
-- https://jira.camunda.com/browse/CAM-10953
create index ACT_IDX_HI_VAR_PI_NAME_TYPE on ACT_HI_VARINST(PROC_INST_ID_, NAME_, VAR_TYPE_);

-- https://app.camunda.com/jira/browse/CAM-10784
ALTER TABLE ACT_HI_JOB_LOG
  ADD HOSTNAME_ nvarchar(255) default null;

-- https://jira.camunda.com/browse/CAM-10378
ALTER TABLE ACT_RU_JOB
  ADD FAILED_ACT_ID_ nvarchar(255);

ALTER TABLE ACT_HI_JOB_LOG
  ADD FAILED_ACT_ID_ nvarchar(255);

ALTER TABLE ACT_RU_INCIDENT
  ADD FAILED_ACTIVITY_ID_ nvarchar(255);

ALTER TABLE ACT_HI_INCIDENT
  ADD FAILED_ACTIVITY_ID_ nvarchar(255);

-- https://jira.camunda.com/browse/CAM-11188
ALTER TABLE ACT_RU_JOBDEF
  ADD DEPLOYMENT_ID_ nvarchar(64);

-- https://jira.camunda.com/browse/CAM-10978

ALTER TABLE ACT_RU_VARIABLE
  ADD PROC_DEF_ID_ nvarchar(64);

ALTER TABLE ACT_HI_DETAIL
  ADD INITIAL_ bit;

/** 7.14 */

-- https://jira.camunda.com/browse/CAM-12304
ALTER TABLE ACT_RU_VARIABLE
    ADD BATCH_ID_ nvarchar(64);
CREATE INDEX ACT_IDX_BATCH_ID ON ACT_RU_VARIABLE(BATCH_ID_);
ALTER TABLE ACT_RU_VARIABLE
    ADD CONSTRAINT ACT_FK_VAR_BATCH
        FOREIGN KEY (BATCH_ID_)
            REFERENCES ACT_RU_BATCH (ID_);

-- https://jira.camunda.com/browse/CAM-12411
create index ACT_IDX_VARIABLE_TASK_NAME_TYPE on ACT_RU_VARIABLE(TASK_ID_, NAME_, TYPE_);

/** 7.15 */

-- https://jira.camunda.com/browse/CAM-13013

create table ACT_RU_TASK_METER_LOG (
                                       ID_ nvarchar(64) not null,
                                       ASSIGNEE_HASH_ numeric(19,0),
                                       TIMESTAMP_ datetime2,
                                       primary key (ID_)
);

create index ACT_IDX_TASK_METER_LOG_TIME on ACT_RU_TASK_METER_LOG(TIMESTAMP_);

-- https://jira.camunda.com/browse/CAM-13060
ALTER TABLE ACT_RU_INCIDENT
    ADD ANNOTATION_ nvarchar(4000);

ALTER TABLE ACT_HI_INCIDENT
    ADD ANNOTATION_ nvarchar(4000);
