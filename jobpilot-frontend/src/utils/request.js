import axios from 'axios'
import { getToken, removeToken, isTokenExpired } from './auth'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000
})

const redirectToLogin = () => {
  removeToken()
  if (window.location.pathname !== '/auth/login') {
    window.location.href = '/auth/login'
  }
}

request.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      if (isTokenExpired(token)) {
        redirectToLogin()
        return Promise.reject(new Error('登录已过期，请重新登录'))
      }
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== undefined && res.code !== 200) {
      // 只依赖 HTTP 状态码判断是否需要重新登录，不依赖业务 code
      return Promise.reject(new Error(res.message || 'Request failed'))
    }
    return res
  },
  error => {
    // 只有真实 HTTP 401（未认证）才跳转登录；403 是权限不足，不跳转
    if (error.response?.status === 401) {
      redirectToLogin()
    }
    return Promise.reject(error)
  }
)

export default request
