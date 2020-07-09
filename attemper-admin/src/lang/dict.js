// zh
export const tenantStatuses_zh = [
  {
    value: 0,
    label: '正常'
  },
  {
    value: 1,
    label: '冻结'
  },
  {
    value: 2,
    label: '禁用'
  }
]

export const jobStatuses_zh = [
  {
    label: '启用',
    value: 0
  },
  {
    label: '禁用',
    value: 1
  }
]

export const milliSecondTimeUnits_zh = [
  {
    label: '毫秒',
    value: 'MILLISECOND'
  }
]

export const inDayTimeUnits_zh = [
  {
    label: '秒',
    value: 'SECOND'
  },
  {
    label: '分钟',
    value: 'MINUTE'
  },
  {
    label: '小时',
    value: 'HOUR'
  }
]

export const dayTimeUnit_zh = [
  {
    label: '天',
    value: 'DAY'
  }
]

export const overDayTimeUnits_zh = [
  {
    label: '周',
    value: 'WEEK'
  },
  {
    label: '月',
    value: 'MONTH'
  },
  {
    label: '季度',
    value: 'SEASON'
  },
  {
    label: '半年',
    value: 'HALF_YEAR'
  },
  {
    label: '年',
    value: 'YEAR'
  }
]

export const daysOfWeek_zh = [
  {
    label: '星期天',
    value: '1'
  },
  {
    label: '星期一',
    value: '2'
  },
  {
    label: '星期二',
    value: '3'
  },
  {
    label: '星期三',
    value: '4'
  },
  {
    label: '星期四',
    value: '5'
  },
  {
    label: '星期五',
    value: '6'
  },
  {
    label: '星期六',
    value: '7'
  }
]

export const uriTypes_zh = [
  {
    label: '服务发现',
    value: 0
  },
  {
    label: 'IP地址和端口',
    value: 1
  },
  {
    label: '域名',
    value: 2
  }
]

export const runningInstanceStatus_zh = [
  {
    label: '执行中',
    value: 0
  }
]

export const successInstanceStatus_zh = [
  {
    label: '成功',
    value: 1
  }
]

export const failureInstanceStatus_zh = [
  {
    label: '失败',
    value: 2
  }
]

export const terminatedInstanceStatus_zh = [
  {
    label: '终止',
    value: 3
  }
]

export const unmetInstanceStatus_zh = [
  {
    label: '跳过',
    value: 4
  }
]

export const doingInstanceStatuses_zh = [
  ...runningInstanceStatus_zh
]

export const doneInstanceStatuses_zh = [
  ...successInstanceStatus_zh,
  ...failureInstanceStatus_zh,
  ...terminatedInstanceStatus_zh,
  ...unmetInstanceStatus_zh
]

export const instanceStatuses_zh = [
  ...runningInstanceStatus_zh,
  ...successInstanceStatus_zh,
  ...failureInstanceStatus_zh,
  ...terminatedInstanceStatus_zh,
  ...unmetInstanceStatus_zh
]

export const requestMethods_zh = [
  {
    label: 'POST',
    value: 'POST'
  },
  {
    label: 'GET',
    value: 'GET'
  },
  {
    label: 'PUT',
    value: 'PUT'
  },
  {
    label: 'DELETE',
    value: 'DELETE'
  }
]

export const transferTitles_zh = [
  '移出',
  '移入'
]

export const allocateTitles_zh = [
  '待分配',
  '已分配'
]

export const baseMisfireInstructions_zh = [
  {
    label: '忽略',
    value: -1
  },
  {
    label: '灵活',
    value: 0
  },
  {
    label: '立即触发',
    value: 1
  }
]

export const misfireInstructions_zh = [
  ...baseMisfireInstructions_zh,
  {
    label: '不触发',
    value: 2
  }
]

export const simpleTriggerMisfireInstructions_zh = [
  ...baseMisfireInstructions_zh,
  {
    label: '立即触发(以当前重复次数)',
    value: 2
  },
  {
    label: '立即触发(以剩余重复次数)',
    value: 3
  },
  {
    label: '下次触发(以剩余重复次数)',
    value: 4
  },
  {
    label: '下次触发(以当前重复次数)',
    value: 5
  }
]

export const jobCharts_zh = [
  {
    label: '执行时长',
    value: 'durationChart'
  },
  {
    label: '执行状态',
    value: 'statusChart'
  }
]

export const durationOrders_zh = [
  {
    label: '按开始时间升序',
    value: 'START_TIME'
  },
  {
    label: '按开始时间降序',
    value: 'START_TIME DESC'
  },
  {
    label: '按执行时长升序',
    value: 'DURATION'
  },
  {
    label: '按执行时长降序',
    value: 'DURATION DESC'
  }
]

export const firedSources_zh = [
  {
    label: '触发器触发',
    value: 'IS NOT NULL'
  },
  {
    label: '手动执行',
    value: 'IS NULL'
  },
  {
    label: '全部',
    value: ''
  }
]

export const sendConfigs_zh = [
  '邮件',
  '钉钉',
  '企业微信'
]

// en
export const tenantStatuses_en = [
  {
    value: 0,
    label: 'Normal'
  },
  {
    value: 1,
    label: 'Frozen'
  },
  {
    value: 2,
    label: 'Disabled'
  }
]

export const jobStatuses_en = [
  {
    label: 'Enable',
    value: 0
  },
  {
    label: 'Disable',
    value: 1
  }
]

export const milliSecondTimeUnits_en = [
  {
    label: 'MilliSecond',
    value: 'MILLISECOND'
  }
]

export const inDayTimeUnits_en = [
  {
    label: 'Second',
    value: 'SECOND'
  },
  {
    label: 'Minute',
    value: 'MINUTE'
  },
  {
    label: 'Hour',
    value: 'HOUR'
  }
]

export const dayTimeUnit_en = [
  {
    label: 'Day',
    value: 'DAY'
  }
]

export const overDayTimeUnits_en = [
  {
    label: 'Week',
    value: 'WEEK'
  },
  {
    label: 'Month',
    value: 'MONTH'
  },
  {
    label: 'Season',
    value: 'SEASON'
  },
  {
    label: 'Half-Year',
    value: 'HALF_YEAR'
  },
  {
    label: 'Year',
    value: 'YEAR'
  }
]

export const daysOfWeek_en = [
  {
    label: 'Sunday',
    value: '1'
  },
  {
    label: 'Monday',
    value: '2'
  },
  {
    label: 'Tuesday',
    value: '3'
  },
  {
    label: 'Wednesday',
    value: '4'
  },
  {
    label: 'Thursday',
    value: '5'
  },
  {
    label: 'Friday',
    value: '6'
  },
  {
    label: 'Saturday',
    value: '7'
  }
]

export const uriTypes_en = [
  {
    label: 'Discovery Client',
    value: '0'
  },
  {
    label: 'ip:port',
    value: '1'
  },
  {
    label: 'Domain Name',
    value: '2'
  }
]

export const runningInstanceStatus_en = [
  {
    label: 'Running',
    value: 0
  }
]

export const successInstanceStatus_en = [
  {
    label: 'Success',
    value: 1
  }
]

export const failureInstanceStatus_en = [
  {
    label: 'Failure',
    value: 2
  }
]

export const terminatedInstanceStatus_en = [
  {
    label: 'Terminated',
    value: 3
  }
]

export const unmetInstanceStatus_en = [
  {
    label: 'Unmet',
    value: 4
  }
]

export const doingInstanceStatuses_en = [
  ...runningInstanceStatus_en
]

export const doneInstanceStatuses_en = [
  ...successInstanceStatus_en,
  ...failureInstanceStatus_en,
  ...terminatedInstanceStatus_en,
  ...unmetInstanceStatus_en
]

export const instanceStatuses_en = [
  ...runningInstanceStatus_en,
  ...successInstanceStatus_en,
  ...failureInstanceStatus_en,
  ...terminatedInstanceStatus_en,
  ...unmetInstanceStatus_en
]

export const requestMethods_en = [
  {
    label: 'POST',
    value: 'POST'
  },
  {
    label: 'GET',
    value: 'GET'
  },
  {
    label: 'PUT',
    value: 'PUT'
  },
  {
    label: 'DELETE',
    value: 'DELETE'
  }
]

export const transferTitles_en = [
  'pull',
  'push'
]

export const allocateTitles_en = [
  'exclude',
  'included'
]

export const baseMisfireInstructions_en = [
  {
    label: 'Ignore misfire policy',
    value: -1
  },
  {
    label: 'Smart policy',
    value: 0
  },
  {
    label: 'Fire once now',
    value: 1
  }
]

export const misfireInstructions_en = [
  ...baseMisfireInstructions_en,
  {
    label: 'Do nothing',
    value: 2
  }
]

export const simpleTriggerMisfireInstructions_en = [
  ...baseMisfireInstructions_en,
  {
    label: 'Reschedule now with existing repeat count',
    value: 2
  },
  {
    label: 'Reschedule now with remaining repeat count',
    value: 3
  },
  {
    label: 'Reschedule next with remaining count',
    value: 4
  },
  {
    label: 'Reschedule next with existing count',
    value: 5
  }
]

export const jobCharts_en = [
  {
    label: 'Duration',
    value: 'durationChart'
  },
  {
    label: 'Status',
    value: 'statusChart'
  }
]

export const durationOrders_en = [
  {
    label: 'start time asc',
    value: 'START_TIME'
  },
  {
    label: 'start time desc',
    value: 'START_TIME DESC'
  },
  {
    label: 'duration asc',
    value: 'DURATION'
  },
  {
    label: 'duration desc',
    value: 'DURATION DESC'
  }
]

export const firedSources_en = [
  {
    label: 'Trigger fired',
    value: 'IS NOT NULL'
  },
  {
    label: 'Manual start',
    value: 'IS NULL'
  },
  {
    label: 'All',
    value: ''
  }
]

export const sendConfigs_en = [
  'Email',
  'DingTalk',
  'WxWork'
]

export const databases = [
  {
    label: 'MySQL',
    value: 'com.mysql.jdbc.Driver'
  },
  {
    label: 'Oracle',
    value: 'oracle.jdbc.driver.OracleDriver'
  },
  {
    label: 'Sql Server',
    value: 'com.microsoft.sqlserver.jdbc.SQLServerDriver'
  },
  {
    label: 'PostgreSql',
    value: 'org.postgresql.Driver'
  }
]

export const modes = [
  {
    value: 'text/x-java',
    label: '.java'
  },
  {
    value: 'text/css',
    label: '.css'
  },
  {
    value: 'text/x-dockerfile',
    label: 'Dockerfile'
  },
  {
    value: 'text/x-go',
    label: '.go'
  },
  {
    value: 'text/x-groovy',
    label: '.groovy'
  },
  {
    value: 'text/html',
    label: '.html'
  },
  {
    value: 'text/javascript',
    label: '.js'
  },
  {
    value: 'application/json',
    label: '.json'
  },
  {
    value: 'application/x-jsp',
    label: '.jsp'
  },
  {
    value: 'text/x-less',
    label: '.less'
  },
  {
    value: 'text/x-markdown',
    label: '.md'
  },
  {
    value: 'application/x-httpd-php',
    label: '.php'
  },
  {
    value: 'text/x-perl',
    label: '.pl'
  },
  {
    value: 'text/x-python',
    label: '.py'
  },
  {
    value: 'text/x-ruby',
    label: '.rb'
  },
  {
    value: 'text/x-sass',
    label: '.sass'
  },
  {
    value: 'text/x-scss',
    label: '.scss'
  },
  {
    value: 'text/x-sh',
    label: '.sh'
  },
  {
    value: 'text/x-mysql',
    label: '.sql'
  },
  {
    value: 'application/xml',
    label: '.xml'
  },
  {
    value: 'x-vue',
    label: '.vue'
  }
]

export const themes = [
  {
    value: '3024-day'
  },
  {
    value: '3024-night'
  },
  {
    value: 'abcdef'
  },
  {
    value: 'ambiance'
  },
  {
    value: 'base16-dark'
  },
  {
    value: 'base16-light'
  },
  {
    value: 'bespin'
  },
  {
    value: 'blackboard'
  },
  {
    value: 'cobalt'
  },
  {
    value: 'colorforth'
  },
  {
    value: 'darcula'
  },
  {
    value: 'dracula'
  },
  {
    value: 'duotone-dark'
  },
  {
    value: 'duotone-light'
  },
  {
    value: 'eclipse'
  },
  {
    value: 'elegant'
  },
  {
    value: 'erlang-dark'
  },
  {
    value: 'gruvbox-dark'
  },
  {
    value: 'hopscotch'
  },
  {
    value: 'icecoder'
  },
  {
    value: 'idea'
  },
  {
    value: 'isotope'
  },
  {
    value: 'lesser-dark'
  },
  {
    value: 'liquibyte'
  },
  {
    value: 'lucario'
  },
  {
    value: 'material'
  },
  {
    value: 'mbo'
  },
  {
    value: 'mdn-like'
  },
  {
    value: 'midnight'
  },
  {
    value: 'monokai'
  },
  {
    value: 'neat'
  },
  {
    value: 'neo'
  },
  {
    value: 'night'
  },
  {
    value: 'nord'
  },
  {
    value: 'oceanic-next'
  },
  {
    value: 'panda-syntax'
  },
  {
    value: 'paraiso-dark'
  },
  {
    value: 'paraiso-light'
  },
  {
    value: 'pastel-on-dark'
  },
  {
    value: 'railscasts'
  },
  {
    value: 'rubyblue'
  },
  {
    value: 'seti'
  },
  {
    value: 'shadowfox'
  },
  {
    value: 'solarized'
  },
  {
    value: 'ssms'
  },
  {
    value: 'the-matrix'
  },
  {
    value: 'tomorrow-night-bright'
  },
  {
    value: 'tomorrow-night-eighties'
  },
  {
    value: 'ttcn'
  },
  {
    value: 'twilight'
  },
  {
    value: 'vibrant-ink'
  },
  {
    value: 'xq-dark'
  },
  {
    value: 'xq-light'
  },
  {
    value: 'yeti'
  },
  {
    value: 'yonce'
  },
  {
    value: 'zenburn'
  }
]
