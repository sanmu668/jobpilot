<template>
  <div class="upload-page">
    <div class="upload-bg">
      <div class="orb orb-1"></div>
      <div class="orb orb-2"></div>
    </div>

    <div class="upload-container">
      <div class="upload-header">
        <h1>上传你的简历</h1>
        <p>AI 将在几秒内完成深度分析，给出匹配评分和优化建议</p>
      </div>

      <!-- Upload States -->
      <div v-if="uploadState === 'idle'" class="upload-card glass-card">
        <div class="drop-zone"
          :class="{ dragging: isDragging }"
          @dragover.prevent="isDragging = true"
          @dragleave.prevent="isDragging = false"
          @drop.prevent="handleDrop"
          @click="triggerFileInput">

          <div class="drop-content">
            <div class="drop-icon">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/>
                <polyline points="17 8 12 3 7 8"/>
                <line x1="12" y1="3" x2="12" y2="15"/>
              </svg>
            </div>
            <h3>拖拽简历文件到此处</h3>
            <p>或者 <span class="click-text">点击选择文件</span></p>
            <div class="format-tags">
              <span class="format-tag">PDF</span>
              <span class="format-tag">DOCX</span>
              <span class="format-tag">DOC</span>
            </div>
            <p class="size-hint">文件大小不超过 10MB</p>
          </div>

          <input ref="fileInput" type="file" accept=".pdf,.doc,.docx" @change="handleFileChange" style="display:none" />
        </div>

        <div class="upload-divider">
          <span>或者填写岗位信息，进行精准匹配分析</span>
        </div>

        <div class="job-input-area">
          <label class="job-label">目标岗位描述（可选）</label>
          <textarea v-model="jobDescription"
            class="job-textarea"
            placeholder="粘贴岗位 JD，AI 将分析你的简历与岗位的匹配度..."></textarea>
        </div>
      </div>

      <!-- File Selected -->
      <div v-else-if="uploadState === 'selected'" class="upload-card glass-card">
        <div class="file-selected">
          <div class="file-icon-wrap">
            <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
            </svg>
          </div>
          <div class="file-info">
            <strong>{{ selectedFile?.name }}</strong>
            <span>{{ formatFileSize(selectedFile?.size) }}</span>
          </div>
          <button class="remove-btn" @click="removeFile">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>

        <div class="job-input-area">
          <label class="job-label">目标岗位描述（可选）</label>
          <textarea v-model="jobDescription"
            class="job-textarea"
            placeholder="粘贴岗位 JD，AI 将分析你的简历与岗位的匹配度..."></textarea>
        </div>

        <button class="upload-btn" @click="startUpload">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/>
            <polyline points="17 8 12 3 7 8"/>
            <line x1="12" y1="3" x2="12" y2="15"/>
          </svg>
          开始 AI 分析
        </button>
      </div>

      <!-- Uploading -->
      <div v-else-if="uploadState === 'uploading'" class="upload-card glass-card">
        <div class="uploading-state">
          <div class="progress-ring-wrap">
            <svg viewBox="0 0 100 100" width="120" height="120">
              <circle cx="50" cy="50" r="42" fill="none" stroke="#EAF4FF" stroke-width="8"/>
              <circle cx="50" cy="50" r="42" fill="none"
                stroke="url(#uploadGrad)"
                stroke-width="8"
                stroke-dasharray="264"
                :stroke-dashoffset="264 - (264 * uploadProgress / 100)"
                stroke-linecap="round"
                transform="rotate(-90 50 50)"
                style="transition: stroke-dashoffset 0.3s ease"/>
              <defs>
                <linearGradient id="uploadGrad" x1="0%" y1="0%" x2="100%" y2="0%">
                  <stop offset="0%" stop-color="#4DA3FF"/>
                  <stop offset="100%" stop-color="#7C3AED"/>
                </linearGradient>
              </defs>
            </svg>
            <div class="progress-center">
              <strong>{{ uploadProgress }}%</strong>
            </div>
          </div>

          <h3>{{ uploadStageText }}</h3>
          <p>{{ uploadStageDesc }}</p>

          <div class="upload-stages">
            <div v-for="stage in uploadStages" :key="stage.label"
              class="stage-item"
              :class="{ active: stage.active, done: stage.done }">
              <div class="stage-dot">
                <svg v-if="stage.done" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                  <polyline points="20 6 9 17 4 12"/>
                </svg>
              </div>
              <span>{{ stage.label }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Success -->
      <div v-else-if="uploadState === 'done'" class="upload-card glass-card">
        <div class="success-state">
          <div class="success-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
          </div>
          <h3>上传成功！</h3>
          <p>简历已上传，点击下方按钮查看 AI 分析报告</p>
          <router-link :to="{ path: '/analysis', query: { resumeId, jd: jobDescription || undefined } }" class="view-btn">
            查看分析报告
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
            </svg>
          </router-link>
        </div>
      </div>

      <!-- Error -->
      <div v-else-if="uploadState === 'error'" class="upload-card glass-card">
        <div class="error-state">
          <div class="error-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
          </div>
          <h3>{{ errorTitle }}</h3>
          <p>{{ uploadError }}</p>
          <router-link
            v-if="resumeId"
            :to="{ path: '/analysis', query: { resumeId, jd: jobDescription || undefined } }"
            class="upload-btn"
            style="text-decoration:none; justify-content:center; margin-bottom:10px">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
            </svg>
            前往分析页查看结果
          </router-link>
          <button class="upload-btn retry-btn" @click="retryUpload">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="1 4 1 10 7 10"/>
              <path d="M3.51 15a9 9 0 1 0 .49-3.51"/>
            </svg>
            重新上传
          </button>
        </div>
      </div>

      <!-- Tips -->
      <div class="upload-tips">
        <h4>📌 上传小贴士</h4>
        <ul>
          <li>建议上传最新版本的简历，确保信息准确</li>
          <li>PDF 格式解析效果最佳，建议优先使用</li>
          <li>填写岗位 JD 可获得更精准的匹配分析</li>
          <li>AI 分析结果仅供参考，请根据实际情况调整</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { uploadResume, runParse, runAnalyze } from '@/api/frontend/resume'

const router = useRouter()
const fileInput = ref(null)
const selectedFile = ref(null)
const jobDescription = ref('')
const isDragging = ref(false)
const uploadState = ref('idle')
const uploadProgress = ref(0)
const uploadError = ref('')
const resumeId = ref(null)

const uploadStages = reactive([
  { label: '文件上传', active: false, done: false },
  { label: '简历解析', active: false, done: false },
  { label: 'AI 分析', active: false, done: false },
  { label: '生成报告', active: false, done: false }
])

const uploadStageText = computed(() => {
  const active = uploadStages.find(s => s.active)
  return active ? active.label + '中...' : '处理中...'
})

const uploadStageDesc = computed(() => {
  const stages = ['正在上传文件到服务器...', '智能解析简历内容和结构...', 'AI 深度分析技能与匹配度...', '生成分析报告...']
  const idx = uploadStages.findIndex(s => s.active)
  return stages[idx] || '请稍候...'
})

const triggerFileInput = () => fileInput.value?.click()

const handleDrop = (e) => {
  isDragging.value = false
  const file = e.dataTransfer.files[0]
  if (file) setFile(file)
}

const handleFileChange = (e) => {
  const file = e.target.files[0]
  if (file) setFile(file)
}

const setFile = (file) => {
  selectedFile.value = file
  uploadState.value = 'selected'
}

const removeFile = () => {
  selectedFile.value = null
  uploadState.value = 'idle'
  if (fileInput.value) fileInput.value.value = ''
}

const formatFileSize = (bytes) => {
  if (!bytes) return ''
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

const errorTitle = computed(() => {
  const msg = uploadError.value || ''
  if (msg.includes('403') || msg.toLowerCase().includes('forbidden')) return '权限不足'
  if (msg.includes('401') || msg.includes('登录')) return '登录已过期'
  if (msg.includes('超时') || msg.includes('timeout')) return 'AI 分析超时'
  if (resumeId.value) return 'AI 分析失败'
  return '上传失败'
})

const resetStages = () => {
  uploadStages.forEach(s => { s.active = false; s.done = false })
  uploadProgress.value = 0
}

// Animate progress within [fromPct, toPct*0.88] while task runs, then snap to toPct
const trackStage = (task, fromPct, toPct, estimatedMs = 4000) => {
  let cancelled = false
  const safeEnd = fromPct + (toPct - fromPct) * 0.88
  const steps = 24
  const stepMs = estimatedMs / steps

  ;(async () => {
    for (let i = 0; i <= steps; i++) {
      if (cancelled) return
      uploadProgress.value = Math.round(fromPct + (i / steps) * (safeEnd - fromPct))
      await new Promise(r => setTimeout(r, stepMs))
    }
  })()

  return Promise.resolve(task).finally(() => {
    cancelled = true
    uploadProgress.value = toPct
  })
}

const startUpload = async () => {
  if (!selectedFile.value) return

  uploadState.value = 'uploading'
  uploadError.value = ''
  resumeId.value = null
  resetStages()

  try {
    // Stage 1: 上传文件 (0 → 25%)
    uploadStages[0].active = true
    const formData = new FormData()
    formData.append('file', selectedFile.value)

    const uploadRes = await uploadResume(formData, {
      onUploadProgress: (e) => {
        if (e.total) uploadProgress.value = Math.round((e.loaded / e.total) * 25)
      }
    })

    resumeId.value = uploadRes.resumeId
    uploadStages[0].active = false
    uploadStages[0].done = true
    uploadProgress.value = 25

    // Stage 2: 简历解析 (25 → 50%) — 提交任务 + 轮询直到 done
    uploadStages[1].active = true
    await trackStage(runParse(resumeId.value), 25, 50, 8000)
    uploadStages[1].active = false
    uploadStages[1].done = true

    // Stage 3: AI 分析 (50 → 75%) — 提交任务 + 轮询直到 done，获取 AnalyzeResponse
    uploadStages[2].active = true
    const analyzeResult = await trackStage(
      runAnalyze(resumeId.value, jobDescription.value),
      50, 75, 30000
    )
    uploadStages[2].active = false
    uploadStages[2].done = true

    // 缓存分析结果，Analysis.vue 直接读取，无需重复调用 AI
    sessionStorage.setItem('analysisContext', JSON.stringify({
      resumeId: resumeId.value,
      jobDescription: jobDescription.value,
      result: analyzeResult,
      createdAt: new Date().toISOString()
    }))

    // Stage 4: 生成报告 (75 → 100%)
    uploadStages[3].active = true
    for (let p = uploadProgress.value; p <= 100; p += 5) {
      uploadProgress.value = p
      await new Promise(r => setTimeout(r, 40))
    }
    uploadStages[3].active = false
    uploadStages[3].done = true
    uploadProgress.value = 100

    await new Promise(r => setTimeout(r, 300))
    uploadState.value = 'done'
  } catch (err) {
    uploadStages.forEach(s => { s.active = false })
    const msg = err.code === 'ECONNABORTED' || err.message?.includes('timeout')
      ? '处理超时，AI 分析耗时较长，请稍后在"分析报告"页面查看结果'
      : (err.message || '上传失败，请重试')
    uploadError.value = msg
    uploadState.value = 'error'
  }
}

const retryUpload = () => {
  uploadState.value = 'selected'
  resetStages()
}
</script>

<style lang="scss" scoped>
.upload-page {
  min-height: calc(100vh - 64px);
  background: linear-gradient(160deg, #EAF4FF 0%, #F7FBFF 100%);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
}

.upload-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;

  .orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.4;
  }

  .orb-1 {
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, rgba($primary, 0.2), transparent);
    top: -100px;
    right: -100px;
  }

  .orb-2 {
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba($accent, 0.15), transparent);
    bottom: -80px;
    left: -80px;
  }
}

.upload-container {
  width: 100%;
  max-width: 680px;
  position: relative;
  z-index: 1;
}

.upload-header {
  text-align: center;
  margin-bottom: 32px;

  h1 {
    font-size: 32px;
    font-weight: 800;
    color: $text-primary;
    margin-bottom: 8px;
  }

  p {
    font-size: 15px;
    color: $text-secondary;
  }
}

.upload-card {
  padding: 36px;
  margin-bottom: 20px;
}

.drop-zone {
  border: 2px dashed rgba($primary, 0.3);
  border-radius: $radius-lg;
  padding: 48px 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.25s ease;
  background: rgba($primary, 0.02);

  &:hover, &.dragging {
    border-color: $primary;
    background: rgba($primary, 0.05);
    transform: scale(1.005);
  }

  .drop-content {
    pointer-events: none;
  }

  .drop-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 88px;
    height: 88px;
    background: linear-gradient(135deg, rgba($primary,0.1), rgba($accent,0.08));
    border-radius: $radius-xl;
    color: $primary;
    margin-bottom: 20px;
  }

  h3 {
    font-size: 18px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 6px;
  }

  p {
    font-size: 14px;
    color: $text-secondary;
    margin-bottom: 14px;

    .click-text {
      color: $primary;
      font-weight: 600;
      cursor: pointer;
    }
  }

  .format-tags {
    display: flex;
    gap: 8px;
    justify-content: center;
    margin-bottom: 8px;
  }

  .format-tag {
    padding: 3px 10px;
    background: rgba($primary, 0.1);
    color: $primary;
    border-radius: $radius-full;
    font-size: 12px;
    font-weight: 600;
  }

  .size-hint {
    font-size: 12px;
    color: $text-muted;
    margin: 0;
  }
}

.upload-divider {
  text-align: center;
  position: relative;
  margin: 24px 0;

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    height: 1px;
    background: $border;
  }

  span {
    position: relative;
    background: white;
    padding: 0 14px;
    font-size: 12px;
    color: $text-muted;
  }
}

.job-label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: $text-primary;
  margin-bottom: 8px;
}

.job-textarea {
  width: 100%;
  min-height: 100px;
  padding: 12px 16px;
  background: $bg-base;
  border: 1px solid $border;
  border-radius: $radius-md;
  font-size: 13px;
  color: $text-primary;
  resize: vertical;
  outline: none;
  font-family: $font-family;
  transition: all 0.2s ease;

  &:focus {
    border-color: $primary;
    box-shadow: 0 0 0 3px rgba($primary, 0.08);
    background: white;
  }

  &::placeholder { color: $text-muted; }
}

.file-selected {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px;
  background: rgba($primary, 0.04);
  border: 1px solid rgba($primary, 0.15);
  border-radius: $radius-md;
  margin-bottom: 20px;

  .file-icon-wrap {
    width: 50px;
    height: 50px;
    background: linear-gradient(135deg, rgba($primary,0.15), rgba($accent,0.1));
    border-radius: $radius-md;
    display: flex;
    align-items: center;
    justify-content: center;
    color: $primary;
    flex-shrink: 0;
  }

  .file-info {
    flex: 1;
    strong { display: block; font-size: 14px; color: $text-primary; margin-bottom: 2px; }
    span { font-size: 12px; color: $text-muted; }
  }

  .remove-btn {
    width: 28px;
    height: 28px;
    background: rgba($danger, 0.1);
    border: none;
    border-radius: $radius-sm;
    color: $danger;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background 0.2s ease;
    &:hover { background: rgba($danger, 0.18); }
  }
}

.upload-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, $primary, $primary-dark);
  color: white;
  border: none;
  border-radius: $radius-md;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 16px rgba($primary, 0.35);
  transition: all 0.25s ease;
  margin-top: 20px;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba($primary, 0.45);
  }
}

.uploading-state {
  text-align: center;
  padding: 20px 0;

  .progress-ring-wrap {
    position: relative;
    display: inline-block;
    margin-bottom: 24px;

    .progress-center {
      position: absolute;
      inset: 0;
      display: flex;
      align-items: center;
      justify-content: center;

      strong {
        font-size: 22px;
        font-weight: 800;
        background: linear-gradient(135deg, $primary, $accent);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }
    }
  }

  h3 {
    font-size: 18px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 6px;
  }

  p {
    font-size: 13px;
    color: $text-secondary;
    margin-bottom: 24px;
  }
}

.upload-stages {
  display: flex;
  justify-content: center;
  gap: 0;

  .stage-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    flex: 1;
    max-width: 100px;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      top: 11px;
      left: calc(50% + 11px);
      right: calc(-50% + 11px);
      height: 2px;
      background: $border;
    }

    &:last-child::after { display: none; }

    &.done {
      .stage-dot {
        background: $success;
        border-color: $success;
        color: white;
      }
      span { color: $success; }
      &::after { background: $success; }
    }

    &.active .stage-dot {
      border-color: $primary;
      background: rgba($primary, 0.1);
      animation: pulse-ring 1s infinite;
    }

    .stage-dot {
      width: 22px;
      height: 22px;
      border-radius: 50%;
      border: 2px solid $border;
      display: flex;
      align-items: center;
      justify-content: center;
      background: white;
      transition: all 0.3s ease;
      position: relative;
      z-index: 1;
    }

    span {
      font-size: 11px;
      color: $text-muted;
      text-align: center;
    }
  }
}

@keyframes pulse-ring {
  0%, 100% { box-shadow: 0 0 0 0 rgba($primary, 0.3); }
  50% { box-shadow: 0 0 0 6px rgba($primary, 0); }
}

.success-state {
  text-align: center;
  padding: 20px 0;

  .success-icon {
    width: 72px;
    height: 72px;
    background: linear-gradient(135deg, $success, #059669);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    margin: 0 auto 20px;
    box-shadow: 0 8px 20px rgba($success, 0.3);
    animation: success-pop 0.4s ease;
  }

  h3 {
    font-size: 22px;
    font-weight: 800;
    color: $text-primary;
    margin-bottom: 8px;
  }

  p {
    font-size: 14px;
    color: $text-secondary;
    margin-bottom: 24px;
  }
}

.result-preview {
  background: $bg-base;
  border-radius: $radius-md;
  padding: 20px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 20px;

  .result-score {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-right: 20px;
    border-right: 1px solid $border;

    strong {
      font-size: 36px;
      font-weight: 900;
      background: linear-gradient(135deg, $primary, $accent);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    span { font-size: 12px; color: $text-muted; }
  }

  .result-tags {
    display: flex;
    flex-direction: column;
    gap: 6px;
  }
}

.view-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
  background: linear-gradient(135deg, $primary, $primary-dark);
  color: white;
  border-radius: $radius-md;
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
  box-shadow: 0 4px 14px rgba($primary, 0.35);
  transition: all 0.25s ease;

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba($primary, 0.45);
  }
}

@keyframes success-pop {
  from { transform: scale(0.7); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.error-state {
  text-align: center;
  padding: 20px 0;

  .error-icon {
    width: 72px;
    height: 72px;
    background: linear-gradient(135deg, $danger, #dc2626);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    margin: 0 auto 20px;
    box-shadow: 0 8px 20px rgba($danger, 0.3);
    animation: success-pop 0.4s ease;
  }

  h3 {
    font-size: 22px;
    font-weight: 800;
    color: $text-primary;
    margin-bottom: 8px;
  }

  p {
    font-size: 14px;
    color: $text-secondary;
    margin-bottom: 24px;
  }
}

.retry-btn {
  background: linear-gradient(135deg, $danger, #dc2626) !important;
  box-shadow: 0 4px 16px rgba($danger, 0.35) !important;
  max-width: 200px;
  margin: 0 auto;

  &:hover {
    box-shadow: 0 8px 24px rgba($danger, 0.45) !important;
  }
}

.upload-tips {
  background: white;
  border-radius: $radius-md;
  padding: 20px 24px;
  border: 1px solid $border-light;

  h4 {
    font-size: 13px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 10px;
  }

  ul {
    padding-left: 0;
    list-style: none;

    li {
      font-size: 13px;
      color: $text-secondary;
      padding: 4px 0;
      padding-left: 16px;
      position: relative;

      &::before {
        content: '·';
        position: absolute;
        left: 4px;
        color: $primary;
        font-weight: 900;
      }
    }
  }
}
</style>
