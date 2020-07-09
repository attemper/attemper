import axios from 'axios'
import Qs from 'qs'
import { Notification } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 20000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['token'] = getToken()
    }
    /*
    solve the array params
    qs.stringify({ id: ['b', 'c'] }, { arrayFormat: 'indices' })
    // 'id[0]=b&id[1]=c'
    qs.stringify({ id: ['b', 'c'] }, { arrayFormat: 'brackets' })
    // 'id[]=b&id[]=c'
    qs.stringify({ id: ['b', 'c'] }, { arrayFormat: 'repeat' })
    // 'id=b&id=c'
     */
    config.paramsSerializer = function(params) {
      return Qs.stringify(params, { arrayFormat: 'accept' })
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  // response => response,
  res => {
    const { data, status } = res
    if (data.code && data.code !== 200) {
      if (data.code >= 1000 && data.code <= 1010) {
        setTimeout(() => { window.location = '/' }, 500)
      }
      Notification({
        title: data.code,
        message: data.msg,
        type: 'error',
        duration: 5 * 1000,
        showClose: true
      })
      throw new Error(data.code + ':' + data.msg)
    }
    return { data, status }
  },
  error => {
    const errorInfo = error.response
    console.log(errorInfo.status, ':', errorInfo.statusText) // for debug
    const data = errorInfo.data
    if (data.code && data.code !== 200) {
      if (data.code === 1000 || data.code === 1001 || data.code === 1002) {
        window.location = '/'
      }
      Notification({
        title: data.code,
        message: data.msg,
        type: 'error',
        duration: 5 * 1000,
        showClose: true
      })
    }
    return Promise.reject(error)
  }
)

export default service
