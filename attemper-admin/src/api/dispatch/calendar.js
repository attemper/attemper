import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR,
    method: 'get',
    params: params
  })
}

const DAY = '/day'

export const saveDayReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + DAY,
    method: 'post',
    data: data
  })
}

export const removeDayReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + DAY,
    method: 'delete',
    data: data
  })
}

export const listDayReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + DAY,
    method: 'get',
    params: params
  })
}

export const importDateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.CALENDAR + DAY + '/import',
    method: 'post',
    data: data
  })
}
