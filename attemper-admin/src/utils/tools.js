import store from '@/store'

/**
 * @param {String|Number} value 要验证的字符串或数值
 * @param {*} validList 用来验证的列表
 */
export const oneOf = (value, validList) => {
  for (let i = 0; i < validList.length; i++) {
    if (value === validList[i]) {
      return true
    }
  }
  return false
}

export const hasAccess = (access, route) => {
  return (route.meta && route.meta.notMenu) || oneOf(route.name, access)
}

/**
 * 资源权限工具方法
 * @param value
 * @returns {boolean}
 */
export const canAccess = (value) => {
  let isExist = false
  const resourceNamesStr = sessionStorage.getItem('resourceNames')
  if (resourceNamesStr) {
    const resourceNames = JSON.parse(resourceNamesStr)
    if (resourceNames && resourceNames.length) {
      for (let i = 0; i < resourceNames.length; i++) {
        if (resourceNames[i] === value) {
          isExist = true
          break
        }
      }
    }
  }
  return isExist
}

export const closeAllTabs = () => {
  store.dispatch('tagsView/delAllViews').then()
}

export const injectIcon = (icon) => {
  return icon + (icon === 'calendar' ? new Date().getDate() : '')
}

export const isBlank = (str) => {
  return !str || str.trim() === ''
}

export const addZero = (key) => {
  return key < 10 ? '0' + key : '' + key
}

export const buildMsg = (vm, array) => {
  const h = vm.$createElement
  return h('p', null, [
    h('ul', array.map(item => {
      return h('li', { style: 'color: #F56C6C' }, item)
    }))
  ])
}

export const getTimeStr = () => {
  const now = new Date()
  const month = now.getMonth() + 1
  const date = now.getDate()
  const hour = now.getHours()
  const min = now.getMinutes()
  const sec = now.getSeconds()
  return '' +
    now.getFullYear() +
    (month < 10 ? '0' + month : month) +
    (date < 10 ? '0' + date : date) +
    '_' +
    (hour < 10 ? '0' + hour : hour) +
    (min < 10 ? '0' + min : min) +
    (sec < 10 ? '0' + sec : sec)
}
