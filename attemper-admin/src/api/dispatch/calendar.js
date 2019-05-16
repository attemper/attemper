import request from '@/utils/request'
import { APIPath } from '@/settings'

export const SubPath = '/calendar'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.LIST,
    method: 'get',
    params: params
  })
}

const DAY = '/day'

export const saveDayReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + DAY + APIPath.SAVE,
    method: 'post',
    data: data
  })
}

export const removeDayReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + DAY + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const listDayReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + DAY + APIPath.LIST,
    method: 'get',
    params: params
  })
}
