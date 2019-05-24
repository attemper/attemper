import request from '@/utils/request'
import { APIPath } from '@/settings'

export const treeListReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.PROJECT + '/treeList',
    method: 'get',
    params: params
  })
}

export const saveReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.PROJECT + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.PROJECT + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

const INFO = '/info'

export const listInfoReq = (params) => {
  return request({
    url: APIPath.SYS + APIPath.PROJECT + INFO + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const saveInfoReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.PROJECT + INFO + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const removeInfoReq = (data) => {
  return request({
    url: APIPath.SYS + APIPath.PROJECT + INFO + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}
