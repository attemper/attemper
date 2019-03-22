import { asyncRouterMap, devRouterMap, constantRouterMap } from '@/router'

/**
 * 通过meta.role判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(resourceNames, route) {
  if (!route.name) {
    console.error('the route name must exist:{}', route.path)
  }
  if (route.meta && !route.meta.notMenu) { // 是菜单
    return resourceNames.some(resourceName => route.name === resourceName)
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRouterMap
 * @param roles
 */
function filterAsyncRouter(routes, resourceNames) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(resourceNames, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRouter(tmp.children, resourceNames)
      }
      res.push(tmp)
    }
  })

  return res
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        const roleNames = JSON.parse(sessionStorage.roleNames)
        const resourceNames = JSON.parse(sessionStorage.resourceNames)
        let accessedRouters
        if (roleNames.includes('admin')) {
          accessedRouters = asyncRouterMap.concat(devRouterMap)
        } else {
          accessedRouters = filterAsyncRouter(asyncRouterMap, resourceNames)
        }
        commit('SET_ROUTERS', accessedRouters)
        resolve()
        /* const { roles } = data
        let accessedRouters
        if (roles.includes('admin')) {
          accessedRouters = asyncRouterMap
        } else {
          accessedRouters = filterAsyncRouter(asyncRouterMap, roles)
        }
        commit('SET_ROUTERS', accessedRouters)
        resolve()*/
      })
    }
  }
}

export default permission
