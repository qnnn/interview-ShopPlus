import {login, logout, getInfo} from '@/api/user'
import {getById} from "@/api/system/user";
import {getToken, setToken, removeToken} from '@/utils/auth'
import {resetRouter} from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    user: {},
    roles: [],
    avatar: '',
    // 第一次加载菜单时用到
    loadMenus: false
  }
}

const state = getDefaultState()

/**
 * this.$store.commit('changeValue',name) 同步操作
 * @type {{SET_TOKEN: mutations.SET_TOKEN, SET_AVATAR: mutations.SET_AVATAR, SET_NAME: mutations.SET_NAME, RESET_STATE: mutations.RESET_STATE}}
 */
const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_USER: (state, user) => {
    state.user = user
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_LOAD_MENUS: (state, loadMenus) => {
    state.loadMenus = loadMenus
  }
}

/**
 * this.$store.dispatch('
 * @type {{logout({commit?: *, state: *}): Promise<unknown>, getInfo({commit?: *, state: *}): Promise<unknown>, resetAvatar({commit: *}, *=): Promise<unknown>, login({commit?: *}, *): Promise<unknown>, resetToken({commit: *}): Promise<unknown>}}
 */
const actions = {
  // user login
  login({commit}, userInfo) {
    const {username, password} = userInfo
    return new Promise((resolve, reject) => {
      login({username: username.trim(), password: password}).then(response => {
        const {data} = response
        getById(data.id).then(response => {
          if (response.data.roles.length === 0) {
            commit('SET_ROLES',['DEFAULT_ROLES'])
          }else {
            console.log('登录载入')
            commit('SET_ROLES',response.data.roles)
          }
        })
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        // 第一次加载菜单时用到， 具体见 src 目录下的 permission.js
        commit('SET_LOAD_MENUS', true)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({commit, state}) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const {data} = response

        if (!data) {
          return reject('无效验证，请重新登录！')
        }

        const {name, avatar} = data

        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)

        getById(data.id).then(response => {
          if (response.data.roles.length === 0) {
            commit('SET_ROLES',['DEFAULT_ROLES'])
          }else {
            commit('SET_ROLES',response.data.roles)
          }
        })

        commit('SET_USER', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({commit, state}) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('SET_ROLES', [])
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({commit}) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  },

  // 设置头像
  resetAvatar({commit}, avatar) {
    return new Promise(resolve => {
      commit('SET_AVATAR', avatar)
      resolve()
    })
  },

  // 刷新菜单
  updateLoadMenus({ commit }) {
    return new Promise((resolve, reject) => {
      commit('SET_LOAD_MENUS', false)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

