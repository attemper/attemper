export const getVersionByDefinition = (procDefId) => {
  return !procDefId ? null : procDefId.split(':')[1]
}
