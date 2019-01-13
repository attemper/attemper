import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
export const SubPath = '/job/group'

export const atomAndSubJobsListReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/atomAndSub' + APIPath.LIST,
    method: 'get',
    params: params
  })
}

export const updateSubReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/sub' + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}

export const removeSubReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + '/sub' + APIPath.REMOVE,
    method: 'delete',
    data: data
  })
}
