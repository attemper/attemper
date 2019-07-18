import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.INSTANCE,
    method: 'get',
    params: params
  })
}

export const listActReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.INSTANCE + '/act',
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

