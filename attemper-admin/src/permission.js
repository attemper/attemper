import router from './router'
import { asyncRouterMap, devRouterMap, constantRouterMap } from '@/router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // getToken from cookie
import { hasAccess, closeAllTabs } from '@/utils/tools'

NProgress.configure({ showSpinner: false })// NProgress Configuration

function staticAccessJudge(list, name) {
  return list.some(item => {
    if (item.children && item.children.length) {
      return staticAccessJudge(item.children, name)
    } else if (item.name === name) {
      return true
    }
  })
}

function asyncAccessJudge(list, access, name) {
  return list.some(item => {
    if (item.children && item.children.length) {
      return asyncAccessJudge(item.children, access, name)
    } else if (item.name === name) {
      return hasAccess(access, item)
    }
  })
}

// permission judge function
function hasPermission(access, name) {
  // TODO bug:应该根据role来判断，admin才可以校验所有，其他用户只能校验Constant和动态路由
  return staticAccessJudge(constantRouterMap, name) || asyncAccessJudge(asyncRouterMap, access, name) || staticAccessJudge(devRouterMap, name)
}

function turnTo(to, access, next) {
  if (to.name && hasPermission(access, to.name)) {
    next()
  } else if (!to.name) {
    const array = to.path.split('/')
    array.forEach(subPath => {
      if (hasPermission(access, subPath)) {
        next({ path: to.path, replace: true })
      }
    })
  } else {
    next({ path: '/401', replace: true, query: { noGoBack: true }})
  }
}

/* function closeAllTabs() {
  store.dispatch('tagsView/delAllViews').then()
}*/

const whiteList = ['/login', '/auth-redirect']// no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      closeAllTabs()
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.resourceNames.length !== 0) { // 判断当前用户是否已拉取完user_info信息
        turnTo(to, store.getters.resourceNames, next)
      } else {
        store.dispatch('user/getInfo').then(resourceNames => { // 拉取user_info
          store.dispatch('permission/generateRoutes').then((allRoutes) => { // 根据菜单资源权限生成可访问的路由表
            router.addRoutes(allRoutes) // 动态添加可访问路由表
            turnTo(to, resourceNames, next)
          })
        }).catch((error) => {
          // remove token and go to login page to re-login
          store.dispatch('user/resetToken').then()
          Message.error(error || 'Has Error')
          closeAllTabs()
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        })
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      // next(`/login?redirect=${to.path}`)
      closeAllTabs()
      next(`/login`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
