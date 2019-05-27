import request from '@/utils/request'
import { APIPath } from '@/settings'

export const treeListReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + '/treeList',
    method: 'get',
    params: params
  })
}

export const saveReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + APIPath.SAVE,
    method: 'post',
    data: data
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

const INFO = '/info'

export const listInfoReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + INFO + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const saveInfoReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + INFO + APIPath.SAVE,
    method: 'post',
    data: data
  })
}

export const removeInfoReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + INFO + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

const EXECUTOR = '/executor'

export const listExecutorReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + EXECUTOR + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const saveExecutorReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.PROJECT + EXECUTOR + APIPath.SAVE,
    method: 'post',
    data: data
  })
}
