import IdGenerator from 'ids'
import { isBlank } from '@/utils/tools'

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

export const startAfterEndTime = (startTimeStr, endTimeStr) => {
  return startTimeStr != null && endTimeStr != null && startTimeStr > endTimeStr
}

const seed = [32, 10]
const idGenerator = new IdGenerator(seed)

export const next = () => {
  return idGenerator.next()
}

export const nextPrefixed = (prefix) => {
  return isBlank(prefix) ? next() : idGenerator.nextPrefixed(prefix)
}
