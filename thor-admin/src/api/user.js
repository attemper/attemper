import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
export const SubPath = '/user'

export function getUserInfo() {
  return request({
    url: APIPath.SYS + SubPath + '/info',
    params: {},
    method: 'get'
  })
}

