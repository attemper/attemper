import request from '@/utils/request'
import { APIPath } from '@/settings'

export const addReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST,
    method: 'post',
    data: data
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST,
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST,
    method: 'delete',
    data: data
  })
}

const INFO = '/info'

export const addInfoReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST + INFO,
    method: 'post',
    data: data
  })
}

export const listInfoReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST + INFO,
    method: 'get',
    params: params
  })
}

export const removeInfoReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST + INFO,
    method: 'delete',
    data: data
  })
}

export const getContentReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST + INFO + '/content',
    method: 'get',
    params: params
  })
}

export const updateContentReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.GIST + INFO + '/content',
    method: 'put',
    data: data
  })
}
