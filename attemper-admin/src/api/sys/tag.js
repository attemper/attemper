import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const addReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const getTenantsReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.TENANT,
    method: 'get',
    params: params
  })
}

export const updateTagTenantReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.TENANT,
    method: 'put',
    data: data
  })
}

export const getResourcesReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.RESOURCE,
    method: 'get',
    params: params
  })
}

export const updateTagResourceReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.TAG + APIPath.RESOURCE,
    method: 'put',
    data: data
  })
}
