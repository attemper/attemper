import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
export const SubPath = '/job/atom'

export const updateJobConfigReq = (data) => {
  return request({
    url: APIPath.DISPATCH + SubPath + APIPath.UPDATE,
    method: 'put',
    data: data
  })
}
