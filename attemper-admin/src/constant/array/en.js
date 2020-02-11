export const tenantStatuses = [
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

export const jobStatuses = [
  {
    label: 'Enable',
    value: 0
  },
  {
    label: 'Disable',
    value: 1
  }
]

export const milliSecondTimeUnits = [
  {
    label: 'MilliSecond',
    value: 'MILLISECOND'
  }
]

export const inDayTimeUnits = [
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

export const dayTimeUnit = [
  {
    label: 'Day',
    value: 'DAY'
  }
]

export const overDayTimeUnits = [
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

export const daysOfWeek = [
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

export const uriTypes = [
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

export const runningInstanceStatus = [
  {
    label: 'Running',
    value: 0
  }
]

export const successInstanceStatus = [
  {
    label: 'Success',
    value: 1
  }
]

export const failureInstanceStatus = [
  {
    label: 'Failure',
    value: 2
  }
]

export const terminatedInstanceStatus = [
  {
    label: 'Terminated',
    value: 3
  }
]

export const unmetInstanceStatus = [
  {
    label: 'Unmet',
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
  'pull',
  'push'
]

export const allocateTitles = [
  'exclude',
  'included'
]

export const baseMisfireInstructions = [
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

export const misfireInstructions = [
  ...baseMisfireInstructions,
  {
    label: 'Do nothing',
    value: 2
  }
]

export const simpleTriggerMisfireInstructions = [
  ...baseMisfireInstructions,
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

export const jobCharts = [
  {
    label: 'Duration',
    value: 'durationChart'
  },
  {
    label: 'Status',
    value: 'statusChart'
  }
]

export const durationOrders = [
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

export const firedSources = [
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

export const sendConfigs = [
  'Email',
  'DingTalk',
  'WxWork'
]
