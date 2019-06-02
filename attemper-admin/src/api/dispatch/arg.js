import request from '@/utils/request'
import { APIPath } from '@/settings'

export const addReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const testSqlReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + '/testSql',
    method: 'get',
    params: params
  })
}

export const testTradeDateReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + '/testTradeDate',
    method: 'get',
    params: params
  })
}
