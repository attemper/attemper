import request from '@/utils/request'
import { APIPath } from '@/settings'

export const SubPath = '/count'

export const tenantCountReq = () => {
  return request({
    url: APIPath.STATISTICS + SubPath + '/tenant',
    method: 'get'
  })
}

export const jobCountReq = () => {
  return request({
    url: APIPath.STATISTICS + SubPath + '/job',
    method: 'get'
  })
}

export const jobInstanceCountReq = () => {
  return request({
    url: APIPath.STATISTICS + SubPath + '/instance',
    method: 'get'
  })
}
