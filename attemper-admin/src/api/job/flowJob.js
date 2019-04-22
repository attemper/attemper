import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
export const SubPath = '/job'

export const addReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const versionsReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/versions',
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const publishReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/publish',
    method: 'put',
    data: data
  })
}

export const copyReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/copy',
    method: 'put',
    data: data
  })
}

export const exchangeReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/exchange',
    method: 'put',
    data: data
  })
}

const PROJECT_PATH = '/project'
export const getProjectReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + PROJECT_PATH,
    method: 'get',
    params: params
  })
}

export const saveProjectReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + PROJECT_PATH,
    method: 'put',
    data: data
  })
}

export const manualReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/manual',
    method: 'post',
    data: data
  })
}
