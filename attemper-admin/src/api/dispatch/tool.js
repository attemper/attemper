import request from '@/utils/request'
import { APIPath } from '@/settings'

export const listTimeZoneReq = () => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/timeZone',
    method: 'get'
  })
}

export const listExecutorServiceReq = () => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/executor',
    method: 'get'
  })
}

export const pingReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/ping',
    method: 'get',
    params: params
  })
}

export const currentTimeReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/time',
    method: 'get',
    params: params
  })
}

export const argTypesReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/argTypes',
    method: 'get',
    params: params
  })
}

export const tradeDateUnitsReq = (params) => {
  return request({
    url: APIPath.DISPATCH + APIPath.TOOL + '/tradeDateUnits',
    method: 'get',
    params: params
  })
}
