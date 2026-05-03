import request from '@/utils/request'

export const getJobList = (params) => request.get('/jobs', { params })
export const getJobById = (id) => request.get(`/jobs/${id}`)
export const getRecommendedJobs = (resumeId) =>
  request.get('/jobs/recommend', { params: { resumeId } })
