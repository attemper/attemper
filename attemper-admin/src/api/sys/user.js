import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
const SubPath = '/user'

export function login(userName, password) {
  const data = {
    userName,
    password
  }
  return request({
    url: APIPath.SYS + '/login',
    data,
    method: 'post'
  })
}

export function logout() {
  return request({
    url: APIPath.SYS + '/logout',
    method: 'post'
  })
}

export function getInfo() {
  return request({
    url: APIPath.SYS + SubPath + '/info',
    params: {},
    method: 'get'
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const addReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}

export const getReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.GET,
    method: 'get',
    params: params
  })
}

export const getTagsReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + '/tag',
    method: 'get',
    params: params
  })
}

export const updateUserTagReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + '/tag',
    method: 'put',
    data: data
  })
}

