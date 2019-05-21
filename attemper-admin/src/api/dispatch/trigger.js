import request from '@/utils/request'
import { APIPath } from '@/settings'

export const SubPath = '/trigger'

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.GET,
    method: 'get',
    params: params
  })
}

const CALENDAR_PATH = '/calendar'

export const getCalendarsReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + CALENDAR_PATH + APIPath.GET,
    method: 'get',
    params: params
  })
}
