import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + APIPath.LIST,
    method: 'get',
    params: params
  })
}

const DAY = '/day'

export const saveDayReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + DAY + APIPath.SAVE,
    method: 'post',
    data: data
  })
}

export const removeDayReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + DAY + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const listDayReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + DAY + APIPath.LIST,
    method: 'get',
    params: params
  })
}
