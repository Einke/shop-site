import { createStore } from 'vuex'
import request from '@/utils/request'

export default createStore({
  state: {
    userId: localStorage.getItem('userId'),
    userInfo: null
  },
  
  mutations: {
    SET_USER_ID(state, id) {
      state.userId = id
      if (id) {
        localStorage.setItem('userId', id)
      }
      else{
        localStorage.removeItem('userId')
      }
    },
    SET_USER_INFO(state, info) {
      state.userInfo = info
    }
  },
  
  actions: {
    getUserInfo({ commit, state }) {
      request.get(`/user/${state.userId}`)
        .then(response => {
          commit('SET_USER_INFO', response.data)
        })
        .catch(error => {
          console.error('获取用户信息失败:', error)
        })
    }
  }
})
