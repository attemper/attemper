import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listTimeZoneReq = () => {
  return request({
    url: APIPath.SYS + APIPath.TOOL + '/timeZone' + APIPath.LIST,
    method: 'get'
  })
}

export const pingReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.TOOL + '/ping',
    method: 'get',
    params: params
  })
}
