import request from '@/utils/request'
import { APIPath } from '@/settings'

const SubPath = '/tag'

export const listReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const addReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.GET,
    method: 'get',
    params: params
  })
}

const TENANT_PATH = '/tenant'

export const getTenantsReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + TENANT_PATH,
    method: 'get',
    params: params
  })
}

export const updateTagTenantReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + TENANT_PATH,
    method: 'put',
    data: data
  })
}

const RESOURCE_PATH = '/resource'

export const getResourcesReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + RESOURCE_PATH,
    method: 'get',
    params: params
  })
}

export const updateTagResourceReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + RESOURCE_PATH,
    method: 'put',
    data: data
  })
}
