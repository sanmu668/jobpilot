import { defineStore } from 'pinia'
import { getToken, setToken, removeToken, getUser, setUser } from '@/utils/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: getToken(),
    user: getUser()
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 'admin',
    userName: (state) => state.user?.name || state.user?.email || 'User'
  },

  actions: {
    login(token, user) {
      this.token = token
      this.user = user
      setToken(token)
      setUser(user)
    },
    logout() {
      this.token = null
      this.user = null
      removeToken()
    },
    updateUser(user) {
      this.user = { ...this.user, ...user }
      setUser(this.user)
    }
  }
})
