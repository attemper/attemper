export default {
  title: '调度中心',
  route: {
    dashboard: '首页',
    sys: '系统管理',
    tenant: '租户管理',
    role: '角色管理',
    calendar: '日历管理',
    dispatch: '调度管理',
    job: '任务管理',
    delay: '延迟任务',
    arg: '参数管理',
    datasource: '数据源管理',
    monitor: '监控管理',
    total: '执行监控',
    retry: '重试监控',
    scheduler: '调度器监控',
    executor: '执行器监控',
    application: '应用管理',
    project: '项目管理',
    gist: '代码片段'
  },
  actions: {
    handle: '操作',
    add: '新增',
    update: '编辑',
    save: '保存',
    search: '查询',
    refresh: '刷新',
    remove: '删除',
    cancel: '取消',
    reset: '重置',
    transferIn: '移入',
    transferOut: '移出',
    import: '导入',
    importModel: '导入模型',
    exportModel: '导出模型',
    exportList: '导出列表',
    last: '上一个',
    next: '下一个',
    ok: '确定',
    download: '下载',
    use: '使用',
    manual: '发起',
    showCurrent: '显示当前',
    showAll: '显示全部',
    highOperation: '高级操作',
    highSearch: '高级查询',
    exclude: '排除',
    connectTest: '连接测试',
    saveDate: '保存（日期修改）',
    testSql: '测试sql',
    retry: '重试',
    terminate: '终止',
    publish: '发布',
    saveAndPublish: '保存并发布',
    enable: '启用',
    disable: '禁用',
    upload: '上传',
    load: '部署',
    unload: '卸载',
    test: '测试',
    changePassword: '修改密码'
  },
  tip: {
    usernameError: '请输入正确的用户名',
    passwordError: '请输入正确的密码',
    search: '请输入查询关键字',
    confirm: '请确认',
    confirmMsg: '确定操作',
    selectData: '请选中要操作的数据',
    saveConfirm: '确定保存',
    publishConfirm: '确定发布',
    saveAndPublishConfirm: '确定保存并发布',
    disabledJobError: '禁用的任务不可被发起',
    adminTenantCannotBeRemoved: '超管租户不能删除',
    preview: '预览',
    selectSql: '必须是查询语句(以select开头)',
    howToPreview: '请双击预览',
    sqlResultAlertInfo: '总条数{}(注意：最多展示前10条)',
    sqlResultNoDataInfo: '执行成功，没有数据返回',
    previewing: '预览中......',
    formatError: '格式不正确',
    pingError: '地址ping不通',
    yes: '是',
    no: '否',
    flowNotStart: '任务流未发起，无法重试或者查看节点执行日志',
    fileSizeOutOfBound: '文件大小不能超过',
    currentFileSize: '该文件大小',
    entryVersion: '请输入版本',
    versionNotBlank: '版本不能为空',
    beforeActivity: '在此节点前发起',
    afterActivity: '在此节点后发起',
    refreshPage: '请等待页面刷新',
    noneScheduler: '未启动调度器',
    noneExecutor: '未启动执行器',
    detail: '详情',
    fileSeparatorLeft: '文件分隔符请用/，例如/data/biz',
    ftpPrefix: '如欲使用参数中的mainFtpIp等，则配置为main（默认使用ftpIp等）',
    dbNameInArg: '在数据源管理中维护数据源名称后在此配置',
    testResult: '测试结果',
    noLongerTriggered: '该触发器将不再被触发',
    tenantFrozen: '租户被冻结，将被禁止登录',
    tenantDisabled: '租户被禁用，任务将跳过执行',
    oldPassword: '原密码',
    newPassword: '新密码',
    doubleNewPassword: '重复新密码',
    differentOldPassword: '新旧密码不能相同',
    differentNewPassword: '两次密码不一致',
    jobNoInstance: '根据查询条件未查询到任何实例',
    noResources: '无菜单权限，请联系超管分配',
    today: '今天',
    yesterday: '昨天',
    lastWeek: '一周前',
    lastMonth: '一月前',
    lastYear: '一年前'
  },
  columns: {
    userName: '用户名',
    password: '密码',
    ip: 'IP',
    displayName: '中文名',
    status: '状态',
    remark: '备注',
    createTime: '创建时间',
    updateTime: '更新时间',
    startTime: '开始时间',
    endTime: '结束时间',
    repeatInterval: '周期',
    prefix: '前缀'
  },
  rules: {
    userName: '用户名不能为空',
    password: '密码不能为空',
    displayName: '中文名不能为空'
  },
  placeholders: {
    userName: '请输入用户名',
    password: '请输入密码',
    displayName: '请输入中文名',
    remark: '请输入备注'
  },
  sys: {
    tenant: {
      columns: {
        email: '邮箱',
        mobile: '手机号',
        sendConfig: '告警开关'
      },
      placeholder: {
        email: '请输入邮箱',
        mobile: '请输入手机号'
      },
      rules: {
        oldPassword: '原密码不能为空',
        newPassword: '新密码不能为空',
        doubleNewPassword: '再次验证的新密码不能为空'
      }
    },
    role: {
      columns: {
        roleName: '角色名称'
      },
      rules: {
        roleName: '角色名称不能为空'
      },
      placeholder: {
        roleName: '请输入角色名称'
      },
      tip: {
        tenant: '分配租户',
        resource: '分配菜单',
        tenantFilterTip: '租户编号或中文名称',
        root: '根节点'
      },
      actions: {
        tenant: '租户',
        resource: '菜单'
      },
      label: {
        allocateTenant: '分配租户',
        allocateResource: '分配菜单'
      }
    }
  },
  dispatch: {
    title: '调度',
    job: {
      rules: {
        jobName: '任务名称不能为空'
      },
      columns: {
        reversion: '模型版本',
        version: '发布版本',
        jobName: '任务名称',
        nextFireTime: '下次执行时间',
        concurrent: '是否支持并行',
        once: '当日仅执行一次'
      },
      placeholder: {
        jobName: '请输入任务名称'
      },
      actions: {
        design: '设计',
        param: '参数',
        trigger: '触发器',
        condition: '条件',
        project: '项目'
      },
      exportConfig: {
        full: '全量',
        increment: '增量'
      }
    },
    flow: {
      btn: {
        xml: 'XML',
        svg: 'SVG'
      },
      title: {
        xml: '以.xml(.bpmn)文件导出',
        svg: '以.svg文件导出'
      },
      tip: {
        copy: '复制',
        exchange: '将当前模型切换为最新的版本',
        copyConfirm: '确定复制',
        exchangeConfirm: '确定将当前模型切换为最新的版本',
        jobNameNotChanged: '复制任务时，任务名称必须修改',
        versionIsLatest: '当前版本已为最新，无需切换'
      }
    },
    trigger: {
      tab: {
        cron: 'Cron表达式',
        calendarOffset: '日程偏移',
        dailyTimeInterval: '每日周期',
        calendarInterval: '日历周期'
      },
      title: {
        preserveDayLight: '维持夏令时',
        skipDayIfNoHour: '不存在时刻跳过',
        triggerName: '触发器名称',
        expression: 'cron表达式',
        timeZone: '时区',
        timeUnit: '时间单位',
        timeRange: '起止时间',
        timeRangeOfDay: '当日起止时刻',
        calendar: '日历',
        startTimeOfDay: '当日开始时刻',
        repeatCount: '重复次数',
        daysOfWeek: '星期',
        innerOffset: '日程内偏移量',
        outerOffset: '日程外偏移量',
        reversed: '是否倒数',
        misfireInstruction: '误点策略'
      },
      placeholder: {
        triggerName: '触发器名称',
        calendar: '日历',
        timeUnit: '时间单位',
        startTimeOfDay: '当日开始时刻',
        endTimeOfDay: '当日结束时刻',
        repeatCount: '重复次数',
        daysOfWeek: '星期',
        innerOffset: '日程内偏移量',
        outerOffset: '日程外偏移量'
      },
      tip: {
        jobNotPublished: '任务未被发布，无法使用触发器'
      }
    },
    condition: {
      tab: {
        sql: 'SQL',
        ftpFile: 'FTP文件',
        localFile: '本地文件'
      },
      title: {
        conditionName: '条件编号',
        filePath: '文件路径',
        fileName: '文件名'
      },
      placeholder: {
        conditionName: '条件编号'
      }
    },
    delay: {
      columns: {
        id: '编号',
        requestTime: '申请时间'
      }
    },
    arg: {
      columns: {
        argName: '参数名称',
        argType: '参数类型',
        argValue: '参数值'
      },
      placeholder: {
        argName: '请输入参数名称',
        argValue: '请输入参数值',
        genericType: '基本数据类型',
        mapKey: '请输入key',
        mapValue: '请输入value',
        calendarName: '请选择日历'
      },
      rules: {
        argName: '参数名称不能为空',
        argValue: '参数值不能为空'
      }
    },
    datasource: {
      columns: {
        dbName: '数据源名称',
        driverClassName: '驱动',
        jdbcUrl: '连接信息',
        attribute: '其他属性'
      },
      placeholder: {
        dbName: '请输入数据源名称',
        jdbcUrl: '请输入连接信息',
        attribute: '请输入其他属性'
      },
      rules: {
        dbName: '数据源名称不能为空',
        jdbcUrl: '连接信息不能为空'
      },
      tip: {
        testResult: '测试结果',
        testSuccess: '成功：',
        testError: '不成功：'
      }
    }
  },
  application: {
    project: {
      title: {
        left: '项目树',
        rightTop: '详情',
        rightBottom: '地址'
      },
      label: {
        projectName: '项目名称',
        contextPath: '项目路径',
        bindExecutor: '绑定执行器',
        executorUri: '执行器路径'
      },
      rules: {
        projectName: '项目名称不能为空'
      },
      placeholder: {
        projectName: '请输入项目名称',
        contextPath: '请输入contextPath'
      },
      tip: {
        searchKey: '项目名称/中文名称',
        projectRemoveWarning: '请先删除子节点'
      }
    },
    gist: {
      title: {
        gistList: '代码片段列表'
      },
      columns: {
        gistName: '代码片段名称'
      },
      rules: {
        gistName: '代码片段名称必须为文件名（比如hello.sh）'
      },
      placeholder: {
        gistName: '请输入代码片段名称'
      }
    }
  },
  monitor: {
    columns: {
      duration: '执行时长',
      msg: '日志',
      schedulerUri: '调度器地址',
      executorUri: '执行器地址'
    },
    label: {
      job: '任务',
      record: '执行记录',
      task: '任务节点'
    }
  },
  serverInfo: {
    base: {
      memory: '内存',
      machine: '主机',
      service: '服务',
      resource: '资源',
      file: '文件系统'
    },
    service: {
      vipAddress: '应用名称',
      homePageUrl: '应用路径',
      status: '状态',
      lastDirtyTimestamp: '最后改动时间',
      lastUpdatedTimestamp: '最后更新时间'
    },
    cpu: {
      coreNum: '核心数',
      appRate: '应用使用率',
      systemRate: '系统使用率',
      usedRate: '总使用率',
      idleRate: '空闲率',
      ioWaitRate: 'IO等待率'
    },
    memory: {
      totalSize: '总量',
      used: '已使用',
      available: '剩余'
    },
    machine: {
      system: '操作系统',
      osName: 'OS名称',
      osArch: 'OS架构',
      osVersion: 'OS版本号'
    },
    jvm: {
      vmName: '名称',
      version: 'Java版本',
      homePath: '安装目录',
      startTime: '启动时刻',
      maxMemorySize: '最大可使用内存',
      totalMemorySize: '初始总内存',
      usedMemory: '已使用内存',
      freeMemory: '剩余内存'
    },
    resource: {
      pid: 'PID',
      userName: '用户名称',
      userHome: '用户主目录',
      userDir: '用户工作目录',
      tempDir: '临时文件目录'
    }
  },
  statistics: {
    chart: '图表',
    instance: '实例',
    job: '任务',
    tenant: '租户'
  },
  chart: {
    instance: '实例状态分布',
    job: '任务状态分布',
    tenant: '租户状态分布',
    instanceDuration: '执行时长趋势图'
  },
  navbar: {
    logOut: '退出',
    docs: '文档',
    theme: '换肤',
    size: '布局大小'
  },
  login: {
    title: '系统登录',
    logIn: '登录'
  },
  tagsView: {
    refresh: '刷新',
    close: '关闭',
    closeOthers: '关闭其它',
    closeAll: '关闭所有'
  },
  settings: {
    title: '系统布局配置',
    theme: '主题色',
    tagsView: '开启 Tags-View',
    sidebarLogo: '侧边栏 Logo'
  }
}
