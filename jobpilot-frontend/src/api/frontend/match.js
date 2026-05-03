import request from '@/utils/request'

export const getMatchResult = (resumeId, jobId) =>
  request.get(`/match/result`, { params: { resumeId, jobId } })

export const getMatchHistory = () => request.get('/match/history')
export const getSkillGap = (resumeId, jobId) =>
  request.get('/match/skill-gap', { params: { resumeId, jobId } })
