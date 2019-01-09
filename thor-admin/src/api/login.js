import request from '@/utils/request'
import config from '@/config'

const { APIPath } = config
export const SubPath = '/login'

export function loginByUsername(userName, password) {
  const data = {
    userName,
    password
  }
  return request({
    url: APIPath.SYS + SubPath,
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

