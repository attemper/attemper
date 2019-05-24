import request from '@/utils/request'
import { APIPath } from '@/settings'

export const tenantCountReq = () => {
  return request({
    url: APIPath.STATISTICS + APIPath.COUNT + APIPath.TENANT,
    method: 'get'
  })
}

export const jobCountReq = () => {
  return request({
    url: APIPath.STATISTICS + APIPath.COUNT + APIPath.JOB,
    method: 'get'
  })
}

export const jobInstanceCountReq = () => {
  return request({
    url: APIPath.STATISTICS + APIPath.COUNT + '/instance',
    method: 'get'
  })
}
