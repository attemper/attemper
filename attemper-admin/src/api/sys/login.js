import request from '@/utils/request'
import { APIPath } from '@/settings'

export function login(userName, password) {
  const data = {
    userName,
    password
  }
  return request({
    url: APIPath.SYS + APIPath.LOGIN,
    data,
    method: 'post'
  })
}

export function logout() {
  return request({
    url: APIPath.SYS + APIPath.LOGIN + '/logout',
    method: 'post'
  })
}

export function getInfo() {
  return request({
    url: APIPath.SYS + APIPath.LOGIN + '/info',
    params: {},
    method: 'get'
  })
}

