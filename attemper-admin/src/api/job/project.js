import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
const SubPath = '/project'

export const treeListReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + '/treeList',
    method: 'get',
    params: params
  })
}

export const saveReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + APIPath.ADD,
    method: 'post',
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

const INFO = '/info'

export const listInfoReq = (params) => {
  return request({
    url: APIPath.SYS + SubPath + INFO + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const saveInfoReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + INFO + APIPath.ADD,
    method: 'post',
    data: data
  })
}

export const removeInfoReq = (data) => {
  return request({
    url: APIPath.SYS + SubPath + INFO + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}
