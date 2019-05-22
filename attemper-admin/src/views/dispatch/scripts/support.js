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

export const getTimeStr = () => {
  const now = new Date()
  const month = now.getMonth() + 1
  const date = now.getDate()
  const hour = now.getHours()
  const min = now.getMinutes()
  const sec = now.getSeconds()
  return '' +
    now.getFullYear() +
    (month < 10 ? '0' + month : month) +
    (date < 10 ? '0' + date : date) +
    ' ' +
    (hour < 10 ? '0' + hour : hour) +
    (min < 10 ? '0' + min : min) +
    (sec < 10 ? '0' + sec : sec)
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
