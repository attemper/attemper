import request from '@/utils/request'
import { APIPath } from '@/settings'

export const addReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM,
    method: 'post',
    data: data
  })
}

export const updateReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM,
    method: 'put',
    data: data
  })
}

export const listReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM,
    method: 'get',
    params: params
  })
}

export const removeReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM,
    method: 'delete',
    data: data
  })
}

const PACKAGE = '/package'

export const listPackageReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE,
    method: 'get',
    params: params
  })
}

export const removePackageReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE,
    method: 'delete',
    data: data
  })
}

export const uploadPackageReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE + '/upload',
    method: 'post',
    data: data
  })
}

export const downloadPackageReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE + '/download',
    method: 'get',
    params: params,
    responseType: 'blob'
  })
}

export const loadPackageReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE + '/load',
    method: 'post',
    data: data
  })
}

export const unloadPackageReq = (data) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE + '/unload',
    method: 'post',
    data: data
  })
}

export const listPackageCategoryReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE + '/category',
    method: 'get',
    params: params
  })
}

export const viewFileReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE + '/file/view',
    method: 'get',
    params: params
  })
}

export const downloadFileReq = (params) => {
  return request({
    url: APIPath.APPLICATION + APIPath.PROGRAM + PACKAGE + '/file/download',
    method: 'get',
    params: params
  })
}
