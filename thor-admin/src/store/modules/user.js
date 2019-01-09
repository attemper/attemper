import { loginByUsername } from '@/api/login'
import { getUserInfo } from '@/api/user'
import { getToken, removeToken, setToken } from '@/utils/auth'

const user = {
  state: {
    user: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    displayName: '',
    avatar: '',
    introduction: '',
    roles: [],
    resourceNames: [],
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_DISPLAY_NAME: (state, displayName) => {
      state.displayName = displayName
    },
    SET_RESOURCE_NAMES: (state, resourceNames) => {
      state.resourceNames = resourceNames
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const userName = userInfo.userName.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(userName, userInfo.password).then(response => {
          const result = response.data.result
          commit('SET_NAME', result.user.userName)
          commit('SET_DISPLAY_NAME', result.user.displayName)
          // commit('setAccess', result.user.access)
          commit('SET_TOKEN', result.token)
          setToken(result.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo().then(response => {
          if (!response.data) { // 由于mockjs 不支持自定义状态码只能这样hack
            reject('error')
          }
          const result = response.data.result
          commit('SET_NAME', result.user.userName)
          commit('SET_DISPLAY_NAME', result.user.displayName)
          const roleNames = []
          if (result.tags && result.tags.length) {
            result.tags.forEach(tag => {
              if (tag.tagType === 0) {
                roleNames.push(tag.tagName)
              }
            })
            commit('SET_ROLES', roleNames)
            sessionStorage.roleNames = JSON.stringify(roleNames)
          }
          const resourceNames = []
          if (result.resources && result.resources.length) {
            result.resources.forEach(resource => {
              resourceNames.push(resource.resourceName)
            })
            commit('SET_RESOURCE_NAMES', resourceNames)
            sessionStorage.resourceNames = JSON.stringify(resourceNames)
          }
          resolve(resourceNames)
          /* const data = response.data

          if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', data.roles)
          } else {
            reject('getInfo: roles must be a non-null array !')
          }

          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          resolve(response)*/
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        /* logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })*/
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        sessionStorage.removeItem('roleNames')
        commit('SET_RESOURCENAMES', [])
        sessionStorage.removeItem('resourceNames')
        removeToken()
        resolve()
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 动态修改权限
    ChangeRoles({ commit, dispatch }, role) {
      return new Promise(resolve => {
        commit('SET_TOKEN', role)
        setToken(role)
        getUserInfo(role).then(response => {
          const data = response.data
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          dispatch('GenerateRoutes', data) // 动态修改权限后 重绘侧边菜单
          resolve()
        })
      })
    }
  }
}

export default user
