import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.INSTANCE + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const listActReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.INSTANCE + APIPath.LIST + '/act',
    method: 'get',
    params: params
  })
}

export const retryReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.INSTANCE + '/retry',
    method: 'post',
    data: data
  })
}

export const terminateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.INSTANCE + '/terminate',
    method: 'post',
    data: data
  })
}

