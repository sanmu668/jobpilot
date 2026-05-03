import axios from 'axios'
import { getToken, removeToken } from './auth'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000
})

request.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
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
      return Promise.reject(new Error(res.message || 'Request failed'))
    }
    return res
  },
  error => {
    if (error.response?.status === 401) {
      removeToken()
      window.location.href = '/auth/login'
    }
    return Promise.reject(error)
  }
)

export default request
