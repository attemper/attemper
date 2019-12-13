import request from '@/utils/request'
import { APIPath } from '@/settings'

export const appPlanReq = () => {
  return request({
    url: APIPath.STATISTICS + APIPath.ANALYSIS + '/plan/app',
    method: 'get'
  })
}

export const instanceDurationReq = (params) => {
  return request({
    url: APIPath.STATISTICS + APIPath.ANALYSIS + '/instance/duration',
    method: 'get',
    params: params
  })
}
