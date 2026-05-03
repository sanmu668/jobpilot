import request from '@/utils/request'

export const getAiLogs = (params) => request.get('/admin/ai-logs', { params })
export const getOperationLogs = (params) => request.get('/admin/operation-logs', { params })
export const clearLogs = (type) => request.delete(`/admin/logs/${type}`)
