import request from '@/utils/request'
import { APIPath } from '@/settings'

const SubPath = '/login'

export function login(userName, password) {
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
    url: APIPath.SYS + SubPath + '/logout',
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

