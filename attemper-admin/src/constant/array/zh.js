export const tenantStatuses = [
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

export const jobStatuses = [
  {
    label: '启用',
    value: 0
  },
  {
    label: '禁用',
    value: 1
  }
]

export const milliSecondTimeUnits = [
  {
    label: '毫秒',
    value: 'MILLISECOND'
  }
]

export const inDayTimeUnits = [
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

export const dayTimeUnit = [
  {
    label: '天',
    value: 'DAY'
  }
]

export const overDayTimeUnits = [
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

export const daysOfWeek = [
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

export const uriTypes = [
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

export const runningInstanceStatus = [
  {
    label: '执行中',
    value: 0
  }
]

export const successInstanceStatus = [
  {
    label: '成功',
    value: 1
  }
]

export const failureInstanceStatus = [
  {
    label: '失败',
    value: 2
  }
]

export const terminatedInstanceStatus = [
  {
    label: '终止',
    value: 3
  }
]

export const unmetInstanceStatus = [
  {
    label: '跳过',
    value: 4
  }
]

export const doingInstanceStatuses = [
  ...runningInstanceStatus
]

export const doneInstanceStatuses = [
  ...successInstanceStatus,
  ...failureInstanceStatus,
  ...terminatedInstanceStatus,
  ...unmetInstanceStatus
]

export const instanceStatuses = [
  ...runningInstanceStatus,
  ...successInstanceStatus,
  ...failureInstanceStatus,
  ...terminatedInstanceStatus,
  ...unmetInstanceStatus
]

export const requestMethods = [
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

export const transferTitles = [
  '移出',
  '移入'
]

export const allocateTitles = [
  '待分配',
  '已分配'
]

export const baseMisfireInstructions = [
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

export const misfireInstructions = [
  ...baseMisfireInstructions,
  {
    label: '不触发',
    value: 2
  }
]

export const simpleTriggerMisfireInstructions = [
  ...baseMisfireInstructions,
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

export const jobCharts = [
  {
    label: '执行时长',
    value: 'durationChart'
  },
  {
    label: '执行状态',
    value: 'statusChart'
  }
]

export const durationOrders = [
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

export const firedSources = [
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

export const sendConfigs = [
  '邮件'
]
