import request from '@/utils/request'
import { APIPath } from '@/settings'

export const addReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const versionsReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/versions',
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const publishReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/publish',
    method: 'put',
    data: data
  })
}

export const copyReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/copy',
    method: 'put',
    data: data
  })
}

export const exchangeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/exchange',
    method: 'put',
    data: data
  })
}
export const listArgReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ARG + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const addArgReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ARG + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const removeArgReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ARG + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const getProjectReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.PROJECT,
    method: 'get',
    params: params
  })
}

export const saveProjectReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.PROJECT,
    method: 'put',
    data: data
  })
}

export const manualReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/manual',
    method: 'post',
    data: data
  })
}
