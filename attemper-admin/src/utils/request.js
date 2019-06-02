import axios from 'axios'
import Qs from 'qs'
import { Notification } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
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
      throw new Error(data.code + ':' + data.msg)
    }
    return { data, status }
  },
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 当code返回如下情况则说明权限有问题，登出并返回到登录页
   * 如想通过 xmlhttprequest 来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  // response => {
  //   const res = response.data
  //   if (res.code !== 20000) {
  //     Message({
  //       message: res.message,
  //       type: 'error',
  //       duration: 5 * 1000
  //     })
  //     // 50008:非法的token; 50012:其他客户端登录了;  50014:Token 过期了;
  //     if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
  //       // 请自行在引入 MessageBox
  //       // import { Message, MessageBox } from 'element-ui'
  //       MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
  //         confirmButtonText: '重新登录',
  //         cancelButtonText: '取消',
  //         type: 'warning'
  //       }).then(() => {
  //         store.dispatch('user/resetToken').then(() => {
  //           location.reload() // 为了重新实例化vue-router对象 避免bug
  //         })
  //       })
  //     }
  //     return Promise.reject('error')
  //   } else {
  //     return response.data
  //   }
  // },
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
