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

export const getDatasourceReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + APIPath.DATASOURCE + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const updateArgDatasourceReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.ARG + APIPath.DATASOURCE,
    method: 'put',
    data: data
  })
}
