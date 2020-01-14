# attemper
<p align="center">
    <a href="https://github.com/spring-projects/spring-boot">
    <img src="https://img.shields.io/badge/spring--boot-2.2.2.RELEASE-brightgreen.svg" alt="spring-boot">
    </a>
    <a href="https://github.com/spring-projects/spring-cloud">
    <img src="https://img.shields.io/badge/spring--cloud-Greenwich.SR4-brightgreen.svg" alt="spring-cloud">
    </a>
    <a href="https://github.com/quartz-scheduler/quartz">
      <img src="https://img.shields.io/badge/quartz-2.3.2-brightgreen.svg" alt="quartz">
    </a>
    <a href="https://github.com/camunda/camunda-bpm-platform">
    <img src="https://img.shields.io/badge/camunda-7.12.0-brightgreen.svg" alt="camunda">
    </a>
    <a href="https://github.com/LMAX-Exchange/disruptor">
      <img src="https://img.shields.io/badge/disruptor-3.4.2-brightgreen.svg" alt="disruptor">
    </a>
</p>

## 简介
- 分布式、多租户的支持流程编排的任务调度应用
  - 支持工作流式的任务编排
  - 基于`Spring Boot`框架开发，对微服务友好
  - 支持`Mysql`/`Oracle`/`Sql Server`/`PostgreSQL`
  - 支持`Eureka`作为注册中心，可自行扩展与`Spring Cloud`集成的其他注册服务的中间件
  - 支持父子、并发、分支判断等任务
  - 支持(父子)任务传参、参数替换
  - 支持延迟任务(预约操作)
  - 支持金融与证券等行业的交易日
  - 支持国际化(中英)与时区

## 模块

[调度中心-前端](./attemper-admin)  
[调度中心-后端](./attemper-web)  
[执行器](./attemper-executor)  
[调度器](./attemper-scheduler)  

## 文档

请参考 [调度中心在线文档](https://attemper.github.io/attemper-document/)

## 交流

QQ群:  
[1029617143](https://jq.qq.com/?_wv=1027&k=5LIPQ4t)