import request from '@/utils/request'
import { APIPath } from '@/settings'

export const SubPath = '/tool'

export const listTimeZoneReq = () => {
  return request({
    url: APIPath.SYS + SubPath + '/timeZone' + APIPath.LIST,
    method: 'get'
  })
}

export const pingReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + '/ping',
    method: 'get',
    params: params
  })
}
