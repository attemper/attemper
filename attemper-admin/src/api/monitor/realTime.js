import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.MONITOR + '/realTime' + APIPath.LIST,
    method: 'get',
    params: params
  })
}
