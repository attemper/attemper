export default {
  route: {
    dashboard: '首页',
    sys: '系统管理',
    tenant: '租户管理',
    user: '用户管理',
    tag: '标签管理',
    resource: '资源管理',
    job: '任务管理',
    project: '服务信息',
    jobs: '任务设计',
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
    tinymce: '富文本编辑器',
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
    charts: '图表',
    keyboardChart: '键盘图表',
    lineChart: '折线图',
    mixChart: '混合图表',
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
    import: '导入',
    export: '导出',
    last: '上一个',
    next: '下一个',
    ok: '确定',
    download: '下载',
    use: '使用',
    manual: '发起'
  },
  tip: {
    search: '请输入查询关键字',
    confirm: '请确认',
    confirmMsg: '确定操作',
    selectData: '请选中要操作的数据',
    saveConfirm: '确定保存',
    manualWithNoVersion: '流程未被发布，不能手动发起'
  },
  sys: {
    tenant: {
      columns: {
        id: '编号',
        name: '名称',
        sign: '认证码',
        admin: '管理员'
      },
      rules: {
        id: '租户编号不能为空',
        name: '租户名称不能为空',
        admin: '管理员不能为空'
      },
      placeholder: {
        id: '请输入租户编号',
        name: '请输入租户名称',
        admin: '请选择管理员'
      },
      oper: {
        instance: '应用系统地址列表'
      }
    },
    user: {
      columns: {
        userName: '用户名',
        displayName: '中文名',
        password: '密码',
        email: '邮箱',
        mobile: '手机号',
        status: '状态'
      },
      rules: {
        userName: '用户名不能为空',
        displayName: '中文名不能为空',
        email: '邮箱非法',
        mobile: '手机号长度必须为11位'
      },
      placeholder: {
        userName: '请输入用户名',
        displayName: '请输入中文名',
        password: '请输入密码',
        email: '请输入邮箱',
        mobile: '请输入手机号'
      },
      tip: {
        tag: '分配标签',
        tagFilterTip: '标签名称或中文名称'
      },
      actions: {
        tag: '标签'
      }
    },
    tag: {
      columns: {
        tagName: '标签名称',
        displayName: '中文名称',
        tagType: '标签类型',
        remark: '备注'
      },
      rules: {
        tagName: '标签名称不能为空',
        displayName: '中文名不能为空',
        tagType: '标签类型不能为空'
      },
      placeholder: {
        tagName: '请输入标签名称',
        displayName: '请输入中文名',
        remark: '请输入备注'
      },
      tip: {
        user: '分配用户',
        resource: '分配资源',
        userFilterTip: '用户名或中文名称'
      },
      actions: {
        user: '用户',
        resource: '资源'
      },
      label: {
        allocateUser: '分配用户',
        allocateResource: '分配资源'
      }
    },
    resource: {
      title: {
        left: '资源树',
        right: '详情'
      },
      label: {
        resourceName: '资源名称',
        displayName: '中文名称',
        resourceType: '资源类型',
        uri: 'URI',
        icon: '图标',
        position: '位置'
      },
      rules: {
        resourceName: '资源名称不能为空',
        displayName: '中文名称不能为空'
      },
      placeholder: {
        resourceName: '请输入资源名称',
        displayName: '请输入中文名称',
        uri: '请输入URI',
        icon: '请输入图标'
      },
      tip: {
        searchKey: '资源名称/中文名称',
        resourceRemoveWarning: '请先删除子节点'
      }
    }
  },
  job: {
    title: '任务',
    sort: {
      nameAsc: '按任务名称升序',
      nameDesc: '按任务名称降序'
    },
    rules: {
      jobName: '任务名称不能为空',
      displayName: '中文名称不能为空',
      status: '状态不能为空'
    },
    columns: {
      reversion: '模型版本',
      version: '发布版本',
      jobName: '任务名称',
      displayName: '中文名称',
      status: '状态',
      createTime: '创建时间',
      updateTime: '最近修改时间',
      deploymentTime: '最近发布时间',
      remark: '备注'
    },
    placeholder: {
      jobName: '请输入任务名称',
      displayName: '请输入中文名称',
      status: '请选择状态',
      remark: '请输入备注'
    },
    actions: {
      design: '设计',
      param: '参数',
      trigger: '触发器',
      project: '项目'
    },
    flowJob: {
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
          cron: 'Cron表达式触发器',
          calendarOffset: '日程偏移触发器',
          dailyInterval: '日间周期触发器',
          calendarInterval: '日历周期触发器'
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
        // calendar: 'Calendar',
        interval: '周期',
        startTimeOfDay: '当日开始时刻',
        repeatCount: '重复次数',
        daysOfWeek: '星期',
        innerOffset: '日程内偏移量',
        outerOffset: '日程外偏移量',
        reversed: '是否倒数'
      },
      placeholder: {
        triggerName: '触发器名称',
        calendar: '日历',
        timeUnit: '时间单位',
        startTime: '开始时间',
        endTime: '结束时间',
        interval: '周期',
        startTimeOfDay: '当日开始时刻',
        endTimeOfDay: '当日结束时刻',
        repeatCount: '重复次数',
        daysOfWeek: '星期',
        innerOffset: '日程内偏移量',
        outerOffset: '日程外偏移量'
      },
      tip: {
        noTrigger: '没有触发器需要保存',
        triggerNameNotBlank: '触发器名称不能为空',
        startAfterEndTime: '开始时间不能晚于结束时间',
        cronExpressionInvalid: 'cron表达式格式非法',
        jobNotPublished: '任务未被发布，无法使用触发器'
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
        displayName: '中文名称',
        contextPath: 'contextPath',
        position: '位置'
      },
      rules: {
        projectName: '项目名称不能为空',
        displayName: '中文名称不能为空'
      },
      placeholder: {
        projectName: '请输入项目名称',
        displayName: '请输入中文名称',
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
      status: '状态',
      startTime: '开始时间',
      endTime: '结束时间'
    }
  },
  navbar: {
    logOut: '退出登录',
    dashboard: '首页',
    github: '项目地址',
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
    tinymceTips: '富文本是管理后台一个核心的功能，但同时又是一个有很多坑的地方。在选择富文本的过程中我也走了不少的弯路，市面上常见的富文本都基本用过了，最终权衡了一下选择了Tinymce。更详细的富文本比较和介绍见',
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
