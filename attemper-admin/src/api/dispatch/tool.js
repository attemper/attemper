import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listTimeZoneReq = () => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/timeZone',
    method: 'get'
  })
}

export const listExecutorServiceReq = () => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/executor',
    method: 'get'
  })
}

export const pingReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/ping',
    method: 'get',
    params: params
  })
}
