import request from '@/utils/request'

export const getSystemConfig = () => request.get('/admin/system/config')
export const updateSystemConfig = (data) => request.put('/admin/system/config', data)
export const getDashboardStats = () => request.get('/admin/dashboard/stats')
export const getSystemInfo = () => request.get('/admin/system/info')
