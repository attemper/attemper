import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
export const SubPath = '/monitor'

export const listReq = (params) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.LIST,
    method: 'get',
    params: params
  })
}
