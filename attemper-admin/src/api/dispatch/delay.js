import request from '@/utils/request'
import { APIPath } from '@/settings'
/**
export const addReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}
*/
export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}
/**
export const getReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY + APIPath.GET,
    method: 'get',
    params: params
  })
}
*/
