import request from '@/utils/request'
import { getToken } from '@/utils/auth'

/** 给每个请求强制附加 Authorization header */
const authHeaders = () => {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
}

/**
 * 轮询封装：每 interval ms 调用一次 fetchFn，直到 status !== 'processing'
 * maxWait: 最长等待时间（ms），默认 5 分钟
 */
const poll = (fetchFn, interval = 2000, maxWait = 300000) => {
  return new Promise((resolve, reject) => {
    const start = Date.now()
    const run = async () => {
      try {
        if (Date.now() - start > maxWait) {
          return reject(new Error('处理超时，请稍后重试'))
        }
        const data = await fetchFn()
        if (data.status === 'processing') {
          setTimeout(run, interval)
          return
        }
        data.status === 'done'
          ? resolve(data)
          : reject(new Error(data.message || '任务处理失败'))
      } catch (e) {
        reject(e)
      }
    }
    run()
  })
}

// ── Step 1: 上传简历 ────────────────────────────────────────────────────────
export const uploadResume = (formData, config = {}) =>
  request.post('/resume/upload', formData, {
    ...config,
    headers: { ...authHeaders() }
  })

// ── Step 2+3: 提交解析任务 + 轮询直到 done ────────────────────────────────
export const submitParse = (resumeId) =>
  request.post(`/resume/parse/${resumeId}`, null, { headers: authHeaders() })

export const getParseStatus = (resumeId) =>
  request.get(`/resume/parse/status/${resumeId}`, { headers: authHeaders() })

/**
 * 提交解析任务并轮询等待完成，Promise resolve 时解析已就绪
 */
export const runParse = async (resumeId) => {
  await submitParse(resumeId)
  return poll(() => getParseStatus(resumeId))
}

// ── Step 4+5: 提交 AI 分析 + 轮询直到 done，返回 AnalyzeResponse ─────────
export const submitAnalyze = (resumeId, jobDescription = '') =>
  request.post('/resume/analyze',
    { resumeId, job_description: jobDescription },
    { headers: authHeaders() }
  )

export const getAnalyzeResult = (taskId) =>
  request.get(`/resume/ai/result/${taskId}`, { headers: authHeaders() })

/**
 * 提交 AI 分析任务并轮询等待结果，直接返回 AnalyzeResponse 对象
 */
export const runAnalyze = async (resumeId, jobDescription = '') => {
  const res = await submitAnalyze(resumeId, jobDescription)
  const taskId = res.taskId
  if (!taskId) throw new Error('未获取到分析任务 ID')
  const pollResult = await poll(() => getAnalyzeResult(taskId))
  return pollResult.result ?? pollResult
}

// 向后兼容别名（Analysis.vue 直接使用 analyzeResume）
export const analyzeResume = runAnalyze

// ── 其他接口 ────────────────────────────────────────────────────────────────
export const getResumeList = () => request.get('/resume/list', { headers: authHeaders() })
export const getResumeById = (id) => request.get(`/resume/${id}`, { headers: authHeaders() })
export const deleteResume = (id) => request.delete(`/resume/${id}`, { headers: authHeaders() })
