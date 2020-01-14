# attemper-admin
<p align="center">
  <a href="https://github.com/vuejs/vue">
    <img src="https://img.shields.io/badge/vue-2.6.11-brightgreen.svg" alt="vue">
  </a>
  <a href="https://github.com/ElemeFE/element">
    <img src="https://img.shields.io/badge/element--ui-2.13.0-brightgreen.svg" alt="element-ui">
  </a>
</p>

## 简介

***`attemper-admin`***是任务调度中心的前端，以[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)为基础修改而来。

## 前序准备

你需要在本地安装 [node](http://nodejs.org/) 和 [git](https://git-scm.com/)。本项目技术栈基于 [ES2015+](http://es6.ruanyifeng.com/)、[vue](https://cn.vuejs.org/index.html)、[vuex](https://vuex.vuejs.org/zh-cn/)、[vue-router](https://router.vuejs.org/zh-cn/) 、[vue-cli](https://github.com/vuejs/vue-cli) 、[axios](https://github.com/axios/axios) 和 [element-ui](https://github.com/ElemeFE/element)，提前了解和学习这些知识会对使用本项目有很大的帮助。

## 开发

```bash
# 克隆项目
git clone https://github.com/attemper/attemper.git

# 进入项目目录
cd attemper/attemper-admin

# 安装依赖
npm install

# 启动服务
npm run dev
```

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 其它

```bash
# 预览发布环境效果
npm run preview

# 预览发布环境效果 + 静态资源分析
npm run preview -- --report

# 代码格式检查
npm run lint

# 代码格式检查并自动修复
npm run lint -- --fix
```

## 浏览器

支持现代浏览器(Chrome/FireFox/IE10+等)

