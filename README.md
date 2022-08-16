# attemper
<p align="center">
    <a href="https://github.com/spring-projects/spring-boot">
      <img src="https://img.shields.io/badge/spring--boot-2.5.4-brightgreen.svg" alt="spring-boot">
    </a>
    <a href="https://github.com/spring-projects/spring-cloud">
      <img src="https://img.shields.io/badge/spring--cloud-Hoxton.2020.0.4-brightgreen.svg" alt="spring-cloud">
    </a>
    <a href="https://github.com/quartz-scheduler/quartz">
      <img src="https://img.shields.io/badge/quartz-2.3.2-brightgreen.svg" alt="quartz">
    </a>
    <a href="https://github.com/camunda/camunda-bpm-platform">
      <img src="https://img.shields.io/badge/camunda-7.15.0-brightgreen.svg" alt="camunda">
    </a>
    <a href="https://github.com/LMAX-Exchange/disruptor">
      <img src="https://img.shields.io/badge/disruptor-3.4.2-brightgreen.svg" alt="disruptor">
    </a>
</p>

- 基于`quartz`实现分布式任务调度（定时、API）；引入`camunda`作为任务执行框架，支持工作流式任务编排、并发批量调度和参数传递。  

## 简介
- 分布式、多租户的支持流程编排的任务调度应用
  - 同时支持`Redis`分布式锁和Quartz数据库悲观锁
  - 基于`camunda`工作流引擎，支持工作流式的任务编排
  - 基于`Spring Boot`框架开发，对微服务友好
  - 支持`Mysql`/`Oracle`/`Sql Server`/`PostgreSQL`
  - 支持`Eureka`作为注册中心，可自行扩展与`Spring Cloud`集成的其他注册服务的中间件
  - 支持脚本任务(`Shell`/`Python`等)
  - 支持父子、并发、分支判断等任务
  - 支持(父子)任务传参、参数替换
  - 目前支持邮件、钉钉、企业微信三种告警
  - 支持延迟任务(预约操作)
  - 支持金融与证券等行业的交易日
  - 支持国际化(中英)与时区



## 功能

```bash
- 调度
  - 基于数据库锁(quartz 原生)实现分布式任务调度
  - 使用 disruptor 来加速任务分发和执行
  - 支持延迟调度(由业务系统运行时调用 API)
  - 可手工触发任务
  - 可重试(支持从指定的错误节点开始)
  - 可终止任务执行
  - 扩展季度和半年作为 quartz 的周期单位(比如可以每季度(半年)第一个交易日执行)

- 任务
  - 启用/禁用
  - 任务复制/导入/导出
  - 任务流式编排（串行/并发/父子任务）
  - 支持 Http/Shell/Python/Groovy 等
  - Http 任务支持同步和异步
  - 脚本任务支持在线编写和本地文件路径
  - 任务支持超时设置
  - 任务可设置是否能够并行的开关
  - 版本管理（版本迭代、版本切换）
  - 支持手工触发任务(调试、补采)

- 触发器
  - Cron 表达式触发器（quartz）：支持 Linux 的 Crontab 的触发器
  - 每日周期触发器（quartz）：支持形如每个交易日 09:15 到 15:00 每 90s 执行一次
  - 日历周期触发器（quartz）：支持形如每月第 1 天开始，每隔 2 周执行一次
  - 日程偏移触发器（扩展）：支持形如每周第一个交易日 07:00 执行一次

- 参数
  - 支持 String,Boolean,Inetger,Double,Long,Date,Time,DateTime,List,Map,Sql,Gist,TradeDate 等类型
  - 任务及其节点可绑定参数
  - 参数可在任务间传递

- 日历
  - 支持证券交易日、自然日、工作日、法定节假日等
  - 可自定义并导入日历
  - 扩展季度和半年作为 quartz 的周期单位(比如可以每季度(半年)第一个交易日执行)

- 数据源
  - 支持 Mysql、Oracle、Sqlserver、Posgresql 数据库的数据源管理
  - 支持测试数据源的连接

- 监控
  - 支持实时、历史和全量监控
  - 支持终止执行中的任务实例
  - 支持重试失败的任务实例
  - 支持在线查看日志
  - 支持监控调度器和执行器的状态(cpu/内存/jvm/文件系统)
  
- 告警
  - 告警方式支持邮件、钉钉机器人和企业微信机器人
  - 告警条件：执行报错

- 统计与分析
  - 支持任务执行实例的统计

- 多租户
  - 不同业务系统以租户分割权限(数据、菜单)

- 多服务
  - 一个租户下，多个分布式服务均可使用该租户
  - 支持以服务发现、IP 端口和域名的方式对接
```

## 模块

[调度中心-前端](./attemper-admin)  
[调度中心-后端](./attemper-web)  
[执行器](./attemper-executor)  
[调度器](./attemper-scheduler)  

## 文档

请参考 [调度中心在线文档](https://attemper.github.io/attemper-document/)

## 开发

[Spring Boot&MVC系统对接Demo](https://github.com/attemper/attemper-samples)

## 预览

- 任务  
  - 列表  
![任务](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/job/jobs.png)
  - 设计  
![设计](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/job/job-demo-050-parallel.png)

- 触发器  
![触发器](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/trigger/trigger-cron.png)

- 参数  
![参数](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/arg/args.png)

- 项目  
![项目](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/project/projects.png)

- 日历  
![日历](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/calendar/calendars.png)

- 执行实例  
![执行实例](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/monitor/instances.png)

- 任务执行图  
![任务执行图](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/monitor/tasks.png)

- 告警  
  - 邮件  
![邮件](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/alarm/email.png)
  - 钉钉机器人  
![钉钉机器人](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/alarm/dingtalk.png)
  - 企业微信机器人  
![企业微信机器人](https://gitee.com/attemper/attemper-document/raw/master/docs/guide/assets/alarm/wxwork.png)
