import { login, getInfo } from '@/api/sys/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router from '@/router'
import { closeAllTabs } from '@/utils/tools'
import { Message } from 'element-ui'
import langSelect from '../../lang/index'

const state = {
  token: getToken(),
  userName: '',
  displayName: '',
  superAdmin: null,
  roles: [],
  resourceNames: []
}
const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_NAME: (state, userName) => {
    state.userName = userName
  },
  SET_DISPLAY_NAME: (state, displayName) => {
    state.displayName = displayName
  },
  SET_SUPER_ADMIN: (state, superAdmin) => {
    state.superAdmin = superAdmin
  },
  SET_RESOURCE_NAMES: (state, resourceNames) => {
    state.resourceNames = resourceNames
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}
const actions = {
  // user login
  login({ commit /*, dispatch*/ }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login(username.trim(), password).then(response => {
        const result = response.data.result
        commit('SET_TOKEN', result.token)
        setToken(result.token)
        // dispatch('getInfo')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response
        const { result } = data
        commit('SET_USER_NAME', result.tenant.userName)
        commit('SET_DISPLAY_NAME', result.tenant.displayName)
        commit('SET_SUPER_ADMIN', result.tenant.superAdmin)
        if (result.tenant.status === 1) {
          Message.warning(langSelect.t('tip.tenantFrozen'))
        }
        const roleNames = []
        if (result.roles && result.roles.length) {
          result.roles.forEach(role => {
            roleNames.push(role.roleName)
          })
          commit('SET_ROLES', roleNames)
          sessionStorage.roleNames = JSON.stringify(roleNames)
        }
        const resourceNames = []
        if (result.resources && result.resources.length) {
          result.resources.forEach(resourceName => {
            resourceNames.push(resourceName)
          })
          commit('SET_RESOURCE_NAMES', resourceNames)
          sessionStorage.resourceNames = JSON.stringify(resourceNames)
        }
        resolve(resourceNames)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      /* logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })*/
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      sessionStorage.removeItem('roleNames')
      commit('SET_RESOURCE_NAMES', [])
      sessionStorage.removeItem('resourceNames')
      removeToken()
      closeAllTabs()
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // Dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
