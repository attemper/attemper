import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listTimeZoneReq = () => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/timeZone' + APIPath.LIST,
    method: 'get'
  })
}

export const listExecutorServiceReq = () => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/executor' + APIPath.LIST,
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
