import request from '@/utils/request'
import { APIPath } from '@/settings'

export const addReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB,
    method: 'put',
    data: data
  })
}

export const updateContentReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/content',
    method: 'put',
    data: data
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB,
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
    url: APIPath.DISPATCH + APIPath.JOB,
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

export const enableReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/enable',
    method: 'put',
    data: data
  })
}

export const disableReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/disable',
    method: 'put',
    data: data
  })
}

export const getContentReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/content',
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

export const manualBatchReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/manual/batch',
    method: 'post',
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

export const exportModelReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/export/model',
    method: 'get',
    params: params,
    responseType: 'blob'
  })
}

export const importModelReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + '/import/model',
    method: 'post',
    data: data
  })
}

export const listArgReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ARG,
    method: 'get',
    params: params
  })
}

export const addArgReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ARG,
    method: 'post',
    data: data
  })
}

export const removeArgReq = (data) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ARG,
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

export const getJsonArgReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.JOB + APIPath.ARG + '/json',
    method: 'get',
    params: params
  })
}

