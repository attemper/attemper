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

export const getUsersReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + '/user',
    method: 'get',
    params: params
  })
}

export const updateTagUserReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + '/user',
    method: 'put',
    data: data
  })
}

export const getResourcesReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + '/resource',
    method: 'get',
    params: params
  })
}

export const updateTagResourceReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + '/resource',
    method: 'put',
    data: data
  })
}
