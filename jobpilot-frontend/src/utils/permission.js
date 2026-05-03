import { getUser } from './auth'

export const hasRole = (role) => {
  const user = getUser()
  return user?.role === role
}

export const isAdmin = () => hasRole('admin')
export const isUser = () => hasRole('user')

export const checkPermission = (to, router) => {
  const user = getUser()
  if (to.meta?.requiresAdmin && !isAdmin()) {
    router.push('/auth/login')
    return false
  }
  if (to.meta?.requiresAuth && !user) {
    router.push('/auth/login')
    return false
  }
  return true
}
