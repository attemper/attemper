export default {
  title: '调度中心',
  route: {
    dashboard: '首页',
    profile: '简介',
    sys: '系统管理',
    tenant: '租户管理',
    tag: '角色管理',
    calendar: '日历管理',
    dispatch: '调度管理',
    job: '任务编排',
    delay: '延迟任务',
    arg: '参数管理',
    datasource: '数据源管理',
    project: '服务信息',
    monitor: '监控管理',
    realTime: '实时监控',
    history: '历史监控',
    total: '全量监控',
    introduction: '简述',
    documentation: '文档',
    guide: '引导页',
    permission: '权限测试页',
    rolePermission: '角色权限',
    pagePermission: '页面权限',
    directivePermission: '指令权限',
    icons: '图标',
    components: '组件',
    componentIndex: '介绍',
    markdown: 'Markdown',
    jsonEditor: 'JSON编辑器',
    dndList: '列表拖拽',
    splitPane: 'Splitpane',
    avatarUpload: '头像上传',
    dropzone: 'Dropzone',
    sticky: 'Sticky',
    countTo: 'CountTo',
    componentMixin: '小组件',
    backToTop: '返回顶部',
    dragDialog: '拖拽 Dialog',
    dragSelect: '拖拽 Select',
    dragKanban: '可拖拽看板',
    example: '综合实例',
    nested: '路由嵌套',
    menu1: '菜单1',
    'menu1-1': '菜单1-1',
    'menu1-2': '菜单1-2',
    'menu1-2-1': '菜单1-2-1',
    'menu1-2-2': '菜单1-2-2',
    'menu1-3': '菜单1-3',
    menu2: '菜单2',
    Table: 'Table',
    dynamicTable: '动态Table',
    dragTable: '拖拽Table',
    inlineEditTable: 'Table内编辑',
    complexTable: '综合Table',
    treeTable: '树形表格',
    customTreeTable: '自定义树表',
    tab: 'Tab',
    form: '表单',
    createArticle: '创建文章',
    editArticle: '编辑文章',
    articleList: '文章列表',
    errorPages: '错误页面',
    page401: '401',
    page404: '404',
    errorLog: '错误日志',
    excel: 'Excel',
    exportExcel: '导出 Excel',
    selectExcel: '导出 已选择项',
    mergeHeader: '导出 多级表头',
    uploadExcel: '上传 Excel',
    zip: 'Zip',
    pdf: 'PDF',
    exportZip: 'Export Zip',
    theme: '换肤',
    clipboardDemo: 'Clipboard',
    i18n: '国际化',
    externalLink: '外链'
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
    importJob: '导出任务',
    exportJob: '导入任务',
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
    saveOrPublish: '保存或发布'
  },
  tip: {
    usernameError: '请输入正确的用户名',
    passwordError: '请输入正确的密码',
    search: '请输入查询关键字',
    confirm: '请确认',
    confirmMsg: '确定操作',
    selectData: '请选中要操作的数据',
    saveConfirm: '确定保存',
    saveOrPublishConfirm: '确定保存或发布',
    manualWithNoVersion: '流程未被发布，不能手动发起',
    disabledJobError: '禁用的任务不可被发起',
    adminTenantCannotBeRemoved: '超管租户不能删除',
    preview: '预览',
    notSelectSql: '必须是查询语句(以select开头)',
    howToPreview: '请双击预览',
    sqlResultAlertInfo: '总条数{}(注意：最多展示前10条)',
    sqlResultNoDataInfo: '执行成功，没有数据返回',
    previewing: '预览中......',
    formatError: '格式不正确',
    pingError: '地址ping不通',
    yes: '是',
    no: '否'
  },
  columns: {
    displayName: '中文名',
    status: '状态',
    remark: '备注',
    startTime: '开始时间',
    endTime: '结束时间',
    interval: '周期'
  },
  rules: {
    displayName: '中文名不能为空'
  },
  placeholders: {
    displayName: '请输入中文名',
    remark: '请输入备注'
  },
  sys: {
    tenant: {
      columns: {
        userName: '编号',
        password: '密码',
        email: '邮箱',
        mobile: '手机号',
        sendConfig: '告警开关'
      },
      rules: {
        userName: '租户编号不能为空',
        password: '密码不能为空'
      },
      placeholder: {
        userName: '请输入租户编号',
        password: '请输入密码',
        email: '请输入邮箱',
        mobile: '请输入手机号'
      }
    },
    tag: {
      columns: {
        tagName: '角色名称'
      },
      rules: {
        tagName: '角色名称不能为空'
      },
      placeholder: {
        tagName: '请输入角色名称'
      },
      tip: {
        tenant: '分配租户',
        resource: '分配菜单',
        tenantFilterTip: '租户编号或中文名称'
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
        timeout: '超时时间',
        concurrent: '是否支持并行',
        createTime: '创建时间',
        updateTime: '最近修改时间',
        deploymentTime: '最近发布时间',
        remark: '备注'
      },
      placeholder: {
        jobName: '请输入任务名称',
        remark: '请输入备注'
      },
      actions: {
        design: '设计',
        param: '参数',
        trigger: '触发器',
        project: '项目'
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
        jobNameNotChanged: '复制任务时，任务名称必须修改'
      }
    },
    trigger: {
      tab: {
        time: {
          title: '时间',
          cron: 'CronTab',
          calendarOffset: '日程偏移',
          dailyInterval: '每日周期',
          calendarInterval: '日历周期'
        },
        event: {
          title: '事件'
        }
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
        userName: '用户名',
        password: '密码'
      },
      placeholder: {
        dbName: '请输入数据源名称',
        jdbcUrl: '请输入连接信息',
        userName: '请输入用户名',
        password: '请输入密码'
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
    },
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
        executorUri: '执行器路径',
        position: '位置'
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
    }
  },
  monitor: {
    columns: {
      duration: '执行时长',
      msg: '错误信息'
    },
    label: {
      record: '记录',
      log: '日志'
    }
  },
  statistics: {
    instance: '实例',
    job: '任务',
    tenant: '租户'
  },
  chart: {
    instance: '实例状态分布',
    job: '任务状态分布'
  },
  navbar: {
    logOut: '退出登录',
    docs: '文档',
    theme: '换肤',
    size: '布局大小'
  },
  login: {
    title: '系统登录',
    logIn: '登录',
    username: '账号',
    password: '密码',
    any: '随便填',
    thirdparty: '第三方登录',
    thirdpartyTips: '本地不能模拟，请结合自己业务进行模拟！！！'
  },
  documentation: {
    documentation: '文档',
    github: 'Github 地址'
  },
  permission: {
    addRole: '新增角色',
    editPermission: '编辑权限',
    roles: '你的权限',
    switchRoles: '切换权限',
    tips: '在某些情况下，不适合使用 v-permission。例如：Element-UI 的 Tab 组件或 el-table-column 以及其它动态渲染 dom 的场景。你只能通过手动设置 v-if 来实现。',
    delete: '删除',
    confirm: '确定',
    cancel: '取消'
  },
  guide: {
    description: '引导页对于一些第一次进入项目的人很有用，你可以简单介绍下项目的功能。本 Demo 是基于',
    button: '打开引导'
  },
  components: {
    documentation: '文档',
    dropzoneTips: '由于我司业务有特殊需求，而且要传七牛 所以没用第三方，选择了自己封装。代码非常的简单，具体代码你可以在这里看到 @/components/Dropzone',
    stickyTips: '当页面滚动到预设的位置会吸附在顶部',
    backToTopTips1: '页面滚动到指定位置会在右下角出现返回顶部按钮',
    backToTopTips2: '可自定义按钮的样式、show/hide、出现的高度、返回的位置 如需文字提示，可在外部使用Element的el-tooltip元素',
    imageUploadTips: '由于我在使用时它只有vue@1版本，而且和mockjs不兼容，所以自己改造了一下，如果大家要使用的话，优先还是使用官方版本。'
  },
  table: {
    dynamicTips1: '固定表头, 按照表头顺序排序',
    dynamicTips2: '不固定表头, 按照点击顺序排序',
    dragTips1: '默认顺序',
    dragTips2: '拖拽后顺序',
    title: '标题',
    importance: '重要性',
    type: '类型',
    remark: '点评',
    search: '搜索',
    add: '添加',
    export: '导出',
    reviewer: '审核人',
    id: '序号',
    date: '时间',
    author: '作者',
    readings: '阅读数',
    status: '状态',
    actions: '操作',
    edit: '编辑',
    publish: '发布',
    draft: '草稿',
    delete: '删除',
    cancel: '取 消',
    confirm: '确 定'
  },
  errorLog: {
    tips: '请点击右上角bug小图标',
    description: '现在的管理后台基本都是spa的形式了，它增强了用户体验，但同时也会增加页面出问题的可能性，可能一个小小的疏忽就导致整个页面的死锁。好在 Vue 官网提供了一个方法来捕获处理异常，你可以在其中进行错误处理或者异常上报。',
    documentation: '文档介绍'
  },
  excel: {
    export: '导出',
    selectedExport: '导出已选择项',
    placeholder: '请输入文件名(默认excel-list)'
  },
  zip: {
    export: '导出',
    placeholder: '请输入文件名(默认file)'
  },
  pdf: {
    tips: '这里使用   window.print() 来实现下载pdf的功能'
  },
  theme: {
    change: '换肤',
    documentation: '换肤文档',
    tips: 'Tips: 它区别于 navbar 上的 theme-pick, 是两种不同的换肤方法，各自有不同的应用场景，具体请参考文档。'
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
    fixedHeader: '固定 Header',
    sidebarLogo: '侧边栏 Logo'
  }
}
