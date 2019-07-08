export const statuses = [
  {
    value: 0,
    label: 'Normal'
  },
  {
    value: 1,
    label: 'Frozen'
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

export const runningJobInstanceStatus = [
  {
    label: 'Running',
    value: 0
  }
]

export const successJobInstanceStatus = [
  {
    label: 'Success',
    value: 1
  }
]

export const failureJobInstanceStatus = [
  {
    label: 'Failure',
    value: 2
  }
]

export const terminatedJobInstanceStatus = [
  {
    label: 'Terminated',
    value: 3
  }
]

export const pausedJobInstanceStatus = [
  {
    label: 'Paused',
    value: 4
  }
]

export const doingJobInstanceStatuses = [
  ...runningJobInstanceStatus,
  ...pausedJobInstanceStatus
]

export const doneJobInstanceStatuses = [
  ...successJobInstanceStatus,
  ...failureJobInstanceStatus,
  ...terminatedJobInstanceStatus
]

export const jobInstanceStatuses = [
  ...runningJobInstanceStatus,
  ...successJobInstanceStatus,
  ...failureJobInstanceStatus,
  ...terminatedJobInstanceStatus,
  ...pausedJobInstanceStatus
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

export const calendarTypes = [
  {
    label: 'date',
    value: 0
  },
  {
    label: 'time',
    value: 1
  }
]

export const sendConfigs = [
  'Email'
]
