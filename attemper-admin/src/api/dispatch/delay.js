import request from '@/utils/request'
import { APIPath } from '@/settings'
/**
export const addReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY ,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY,
    method: 'put',
    data: data
  })
}
*/
export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY,
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.DELAY,
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
