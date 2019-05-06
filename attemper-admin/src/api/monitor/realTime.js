import request from '@/utils/request'
import { APIPath } from '@/settings'

export const SubPath = '/monitor/realTime'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.LIST,
    method: 'get',
    params: params
  })
}
