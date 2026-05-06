const TOKEN_KEY = 'jobpilot_token'
const USER_KEY = 'jobpilot_user'

export const getToken = () => localStorage.getItem(TOKEN_KEY)
export const setToken = (token) => localStorage.setItem(TOKEN_KEY, token)
export const removeToken = () => {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

export const getUser = () => {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  } catch {
    return null
  }
}

export const setUser = (user) => localStorage.setItem(USER_KEY, JSON.stringify(user))

export const isTokenExpired = (token) => {
  if (!token) return true
  try {
    // JWT 使用 base64url（无填充），atob 需要标准 base64（有填充）
    const base64url = token.split('.')[1]
    const base64 = base64url.replace(/-/g, '+').replace(/_/g, '/')
    const padded = base64 + '='.repeat((4 - base64.length % 4) % 4)
    const payload = JSON.parse(atob(padded))
    return payload.exp * 1000 < Date.now()
  } catch {
    return true
  }
}

export const isLoggedIn = () => {
  const token = getToken()
  if (!token) return false
  if (isTokenExpired(token)) {
    removeToken()
    return false
  }
  return true
}
