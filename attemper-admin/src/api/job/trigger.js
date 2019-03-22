import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
export const SubPath = '/job/trigger'

export const updateReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.UPDATE,
    method: 'put',
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
