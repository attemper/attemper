export const getDefinitions = (bpmnModeler) => {
  return bpmnModeler._definitions
}

export const getMainRootElement = (bpmnModeler) => {
  const definitions = getDefinitions(bpmnModeler)
  if (!definitions || !definitions.rootElements || !definitions.rootElements.length) {
    console.error('the root elements is null or empty', bpmnModeler)
    return null
  }
  return definitions.rootElements[0]
}

export const getProcessId = (bpmnModeler) => {
  const id = getMainRootElement(bpmnModeler).id
  if (!id) {
    console.error('the process id is null or empty', bpmnModeler)
    return null
  }
  return id
}
