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
