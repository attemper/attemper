import { asyncRouterMap, devRouterMap, constantRouterMap } from '@/router'

/**
 * 通过meta.role判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(resourceNames, route) {
  if (route.meta && !route.meta.notMenu) { // 是菜单
    return resourceNames.some(resourceName => (route.name || route.path.substring(1)) === resourceName)
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRouterMap
 * @param roles
 */
function filterAsyncRoutes(routes, resourceNames) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(resourceNames, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, resourceNames)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: constantRouterMap,
  addRoutes: []
}
const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRouterMap.concat(routes)
  }
}
const actions = {
  generateRoutes({ commit }) {
    return new Promise(resolve => {
      const roleNames = JSON.parse(sessionStorage.roleNames)
      const resourceNames = JSON.parse(sessionStorage.resourceNames)
      let accessedRouter
      if (roleNames.includes('admin')) {
        accessedRouter = asyncRouterMap.concat(devRouterMap)
      } else {
        accessedRouter = filterAsyncRoutes(asyncRouterMap, resourceNames)
      }
      commit('SET_ROUTES', accessedRouter)
      resolve(constantRouterMap.concat(accessedRouter))
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}


