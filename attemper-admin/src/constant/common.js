export const genericTypes = [
  {
    label: 'String',
    value: 0
  },
  {
    label: 'Boolean',
    value: 1
  },
  {
    label: 'Integer',
    value: 2
  },
  {
    label: 'Double',
    value: 3
  },
  {
    label: 'Long',
    value: 4
  },
  {
    label: 'Date',
    value: 10
  },
  {
    label: 'Time',
    value: 11
  },
  {
    label: 'DateTime',
    value: 12
  }
]

export const rawTypes = [
  {
    label: 'List',
    value: 20
  },
  {
    label: 'Map',
    value: 21
  }
]

export const specTypes = [
  {
    label: 'Sql',
    value: 30
  }
]

export const argTypes = [
  ...genericTypes,
  ...rawTypes,
  ...specTypes
]

export const dbInfos = [
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
  },
  {
    label: 'DB2',
    value: 'com.ibm.db2.jdbc.app.DB2Driver'
  },
  {
    label: 'Sybase',
    value: 'com.sybase.jdbc.SybDriver'
  },
  {
    label: 'Access',
    value: 'sun.jdbc.odbc.JdbcOdbcDriver'
  }
]
