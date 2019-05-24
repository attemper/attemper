import request from '@/utils/request'
import { APIPath } from '@/settings'

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TRIGGER + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TRIGGER + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const getCalendarsReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TRIGGER + APIPath.CALENDAR + APIPath.GET,
    method: 'get',
    params: params
  })
}
