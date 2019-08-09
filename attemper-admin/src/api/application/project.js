import request from '@/utils/request'
import { APIPath } from '@/settings'

export const treeListReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT + '/treeList',
    method: 'get',
    params: params
  })
}

export const saveReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT,
    method: 'post',
    data: data
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT,
    method: 'delete',
    data: data
  })
}

const INFO = '/info'

export const listInfoReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT + INFO,
    method: 'get',
    params: params
  })
}

export const saveInfoReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT + INFO,
    method: 'post',
    data: data
  })
}

export const removeInfoReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT + INFO,
    method: 'delete',
    data: data
  })
}

const EXECUTOR = '/executor'

export const listExecutorReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT + EXECUTOR,
    method: 'get',
    params: params
  })
}

export const saveExecutorReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROJECT + EXECUTOR,
    method: 'post',
    data: data
  })
}
