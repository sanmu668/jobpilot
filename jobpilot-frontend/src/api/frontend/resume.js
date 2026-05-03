import request from '@/utils/request'

export const uploadResume = (formData) =>
  request.post('/resume/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })

export const getResumeList = () => request.get('/resume/list')
export const getResumeById = (id) => request.get(`/resume/${id}`)
export const deleteResume = (id) => request.delete(`/resume/${id}`)
export const analyzeResume = (id, jobId) => request.post(`/resume/${id}/analyze`, { jobId })
export const optimizeResume = (id) => request.post(`/resume/${id}/optimize`)
