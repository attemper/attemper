import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE,
    method: 'get',
    params: params
  })
}

export const addReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE,
    method: 'put',
    data: data
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE,
    method: 'delete',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const getTenantsReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE + APIPath.TENANT,
    method: 'get',
    params: params
  })
}

export const updateRoleTenantReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE + APIPath.TENANT,
    method: 'put',
    data: data
  })
}

export const getResourcesReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE + APIPath.RESOURCE,
    method: 'get',
    params: params
  })
}

export const updateRoleResourceReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.ROLE + APIPath.RESOURCE,
    method: 'put',
    data: data
  })
}
