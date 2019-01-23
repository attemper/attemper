import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
const SubPath = '/resource'

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
