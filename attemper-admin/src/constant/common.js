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
    value: 5
  },
  {
    label: 'DateTime',
    value: 6
  }
]

export const rawTypes = [
  {
    label: 'List',
    value: 7
  },
  {
    label: 'Map',
    value: 8
  }
]

export const specTypes = [
  {
    label: 'Sql',
    value: 9
  }
]

export const argTypes = [
  ...genericTypes,
  ...rawTypes,
  ...specTypes
]
