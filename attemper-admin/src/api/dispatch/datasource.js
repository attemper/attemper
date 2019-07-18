import request from '@/utils/request'
import { APIPath } from '@/settings'

export const addReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DATASOURCE,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DATASOURCE,
    method: 'put',
    data: data
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DATASOURCE,
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DATASOURCE,
    method: 'delete',
    data: data
  })
}

export const testConnectionReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DATASOURCE + '/test',
    method: 'post',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DATASOURCE + APIPath.GET,
    method: 'get',
    params: params
  })
}
