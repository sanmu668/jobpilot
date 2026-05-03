import request from '@/utils/request'

export const getUserList = (params) => request.get('/admin/users', { params })
export const getUserById = (id) => request.get(`/admin/users/${id}`)
export const updateUser = (id, data) => request.put(`/admin/users/${id}`, data)
export const deleteUser = (id) => request.delete(`/admin/users/${id}`)
export const getUserStats = () => request.get('/admin/users/stats')
