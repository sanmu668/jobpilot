<template>
  <div class="system-config page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">系统配置</h1>
        <p class="page-subtitle">管理平台基础配置与 AI 服务参数</p>
      </div>
      <button class="btn-primary" @click="saveAll">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 21H5a2 2 0 01-2-2V5a2 2 0 012-2h11l5 5v11a2 2 0 01-2 2z"/>
          <polyline points="17 21 17 13 7 13 7 21"/><polyline points="7 3 7 8 15 8"/>
        </svg>
        保存配置
      </button>
    </div>

    <div class="config-layout">
      <!-- Left: nav -->
      <div class="config-nav card">
        <button
          v-for="section in sections"
          :key="section.id"
          class="nav-item"
          :class="{ active: activeSection === section.id }"
          @click="activeSection = section.id"
        >
          <span v-html="section.icon"></span>
          {{ section.label }}
        </button>
      </div>

      <!-- Right: content -->
      <div class="config-content card">

        <!-- Basic -->
        <div v-if="activeSection === 'basic'">
          <h2 class="section-title">基础设置</h2>
          <div class="form-grid">
            <div class="form-item">
              <label>平台名称</label>
              <input v-model="config.siteName" type="text" placeholder="JobPilot" />
            </div>
            <div class="form-item">
              <label>联系邮箱</label>
              <input v-model="config.contactEmail" type="email" placeholder="admin@jobpilot.com" />
            </div>
            <div class="form-item full">
              <label>平台简介</label>
              <textarea v-model="config.siteDesc" rows="3" placeholder="AI 驱动的智能求职平台"></textarea>
            </div>
            <div class="form-item">
              <label>用户注册</label>
              <div class="toggle-row">
                <label class="toggle">
                  <input type="checkbox" v-model="config.allowRegister" />
                  <span class="toggle-slider"></span>
                </label>
                <span class="toggle-label">{{ config.allowRegister ? '开放注册' : '禁止注册' }}</span>
              </div>
            </div>
            <div class="form-item">
              <label>维护模式</label>
              <div class="toggle-row">
                <label class="toggle">
                  <input type="checkbox" v-model="config.maintenanceMode" />
                  <span class="toggle-slider"></span>
                </label>
                <span class="toggle-label">{{ config.maintenanceMode ? '维护中' : '正常运行' }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- AI -->
        <div v-if="activeSection === 'ai'">
          <h2 class="section-title">AI 服务配置</h2>
          <div class="form-grid">
            <div class="form-item full">
              <label>OpenAI API Key</label>
              <div class="secret-input">
                <input :type="showKey ? 'text' : 'password'" v-model="config.openaiKey" placeholder="sk-..." />
                <button class="eye-btn" @click="showKey = !showKey">
                  <svg v-if="!showKey" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
                  </svg>
                  <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19"/>
                    <line x1="1" y1="1" x2="23" y2="23"/>
                  </svg>
                </button>
              </div>
            </div>
            <div class="form-item">
              <label>模型版本</label>
              <select v-model="config.aiModel">
                <option value="gpt-4o">GPT-4o</option>
                <option value="gpt-4-turbo">GPT-4 Turbo</option>
                <option value="gpt-3.5-turbo">GPT-3.5 Turbo</option>
              </select>
            </div>
            <div class="form-item">
              <label>最大 Token 数</label>
              <input v-model.number="config.maxTokens" type="number" min="256" max="8192" step="256" />
            </div>
            <div class="form-item">
              <label>Temperature</label>
              <div class="range-row">
                <input v-model.number="config.temperature" type="range" min="0" max="1" step="0.1" />
                <span class="range-val">{{ config.temperature }}</span>
              </div>
            </div>
            <div class="form-item">
              <label>匹配最低分阈值（%）</label>
              <div class="range-row">
                <input v-model.number="config.matchThreshold" type="range" min="0" max="100" step="5" />
                <span class="range-val">{{ config.matchThreshold }}%</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Storage -->
        <div v-if="activeSection === 'storage'">
          <h2 class="section-title">存储配置</h2>
          <div class="form-grid">
            <div class="form-item full">
              <label>存储方式</label>
              <div class="radio-group">
                <label v-for="opt in storageOptions" :key="opt.value" class="radio-item" :class="{ active: config.storageType === opt.value }">
                  <input type="radio" v-model="config.storageType" :value="opt.value" />
                  <span>{{ opt.label }}</span>
                </label>
              </div>
            </div>
            <template v-if="config.storageType === 'oss'">
              <div class="form-item">
                <label>Endpoint</label>
                <input v-model="config.ossEndpoint" type="text" placeholder="https://oss-cn-hangzhou.aliyuncs.com" />
              </div>
              <div class="form-item">
                <label>Bucket</label>
                <input v-model="config.ossBucket" type="text" placeholder="my-bucket" />
              </div>
              <div class="form-item">
                <label>Access Key ID</label>
                <input v-model="config.ossKeyId" type="text" placeholder="AccessKeyId" />
              </div>
              <div class="form-item">
                <label>Access Key Secret</label>
                <input v-model="config.ossKeySecret" type="password" placeholder="AccessKeySecret" />
              </div>
            </template>
            <div class="form-item" v-if="config.storageType === 'local'">
              <label>本地存储路径</label>
              <input v-model="config.localPath" type="text" placeholder="/data/uploads" />
            </div>
            <div class="form-item">
              <label>最大文件大小（MB）</label>
              <input v-model.number="config.maxFileSize" type="number" min="1" max="100" />
            </div>
          </div>
        </div>

        <!-- Notification -->
        <div v-if="activeSection === 'notification'">
          <h2 class="section-title">通知设置</h2>
          <div class="form-grid">
            <div class="form-item full">
              <label>SMTP 服务器</label>
              <input v-model="config.smtpHost" type="text" placeholder="smtp.example.com" />
            </div>
            <div class="form-item">
              <label>SMTP 端口</label>
              <input v-model.number="config.smtpPort" type="number" placeholder="465" />
            </div>
            <div class="form-item">
              <label>发件人邮箱</label>
              <input v-model="config.smtpFrom" type="email" placeholder="noreply@jobpilot.com" />
            </div>
            <div class="form-item">
              <label>SMTP 用户名</label>
              <input v-model="config.smtpUser" type="text" placeholder="username" />
            </div>
            <div class="form-item">
              <label>SMTP 密码</label>
              <input v-model="config.smtpPass" type="password" placeholder="••••••••" />
            </div>
            <div class="form-item">
              <label>邮件通知</label>
              <div class="toggle-row">
                <label class="toggle">
                  <input type="checkbox" v-model="config.emailNotify" />
                  <span class="toggle-slider"></span>
                </label>
                <span class="toggle-label">{{ config.emailNotify ? '已开启' : '已关闭' }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="save-bar">
          <span v-if="saved" class="saved-hint">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#059669" stroke-width="2">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            已保存
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeSection = ref('basic')
const showKey = ref(false)
const saved = ref(false)

const sections = [
  { id: 'basic', label: '基础设置', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M19.07 4.93a10 10 0 010 14.14M4.93 4.93a10 10 0 000 14.14"/></svg>' },
  { id: 'ai', label: 'AI 配置', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a2 2 0 012 2v1a2 2 0 01-2 2 2 2 0 01-2-2V4a2 2 0 012-2z"/><path d="M12 17a2 2 0 012 2v1a2 2 0 01-2 2 2 2 0 01-2-2v-1a2 2 0 012-2z"/><path d="M4.22 10.22a2 2 0 012.83 0l.7.71a2 2 0 010 2.83l-.7.7a2 2 0 01-2.83 0 2 2 0 010-2.83l.7-.7z"/><path d="M16.24 10.22a2 2 0 012.83 0l.7.71a2 2 0 010 2.83l-.7.7a2 2 0 01-2.83 0 2 2 0 010-2.83l.7-.7z"/></svg>' },
  { id: 'storage', label: '存储配置', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><ellipse cx="12" cy="5" rx="9" ry="3"/><path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/><path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/></svg>' },
  { id: 'notification', label: '通知设置', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 01-3.46 0"/></svg>' },
]

const storageOptions = [
  { value: 'local', label: '本地存储' },
  { value: 'oss', label: '阿里云 OSS' },
  { value: 's3', label: 'AWS S3' },
]

const config = ref({
  siteName: 'JobPilot',
  contactEmail: 'admin@jobpilot.com',
  siteDesc: 'AI 驱动的智能求职平台',
  allowRegister: true,
  maintenanceMode: false,
  openaiKey: '',
  aiModel: 'gpt-4o',
  maxTokens: 2048,
  temperature: 0.7,
  matchThreshold: 60,
  storageType: 'local',
  localPath: '/data/uploads',
  ossEndpoint: '',
  ossBucket: '',
  ossKeyId: '',
  ossKeySecret: '',
  maxFileSize: 10,
  smtpHost: '',
  smtpPort: 465,
  smtpFrom: '',
  smtpUser: '',
  smtpPass: '',
  emailNotify: false,
})

function saveAll() {
  saved.value = true
  setTimeout(() => { saved.value = false }, 3000)
}
</script>

<style scoped>
.page-container { padding: 24px; }
.page-header { display: flex; align-items: flex-start; justify-content: space-between; margin-bottom: 24px; }
.page-title { font-size: 22px; font-weight: 700; color: #111827; margin: 0 0 4px; }
.page-subtitle { font-size: 14px; color: #6B7280; margin: 0; }

.config-layout { display: grid; grid-template-columns: 200px 1fr; gap: 16px; align-items: start; }
.card { background: #fff; border-radius: 12px; border: 1px solid #E5E7EB; }

.config-nav { padding: 8px; display: flex; flex-direction: column; gap: 2px; }
.nav-item { display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 8px; border: none; background: transparent; font-size: 14px; color: #374151; cursor: pointer; text-align: left; transition: all 0.15s; width: 100%; }
.nav-item:hover { background: #F3F4F6; }
.nav-item.active { background: #EFF6FF; color: #3B82F6; font-weight: 500; }

.config-content { padding: 28px; }
.section-title { font-size: 16px; font-weight: 600; color: #111827; margin: 0 0 24px; padding-bottom: 16px; border-bottom: 1px solid #E5E7EB; }

.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
.form-item { display: flex; flex-direction: column; gap: 6px; }
.form-item.full { grid-column: 1 / -1; }
.form-item label { font-size: 13px; font-weight: 500; color: #374151; }
.form-item input, .form-item select, .form-item textarea {
  border: 1px solid #E5E7EB; border-radius: 8px; padding: 9px 12px;
  font-size: 14px; color: #111827; background: #F9FAFB; outline: none;
  transition: border-color 0.15s; resize: vertical; font-family: inherit;
}
.form-item input:focus, .form-item select:focus, .form-item textarea:focus { border-color: #4DA3FF; background: #fff; }

.secret-input { display: flex; align-items: center; position: relative; }
.secret-input input { flex: 1; padding-right: 40px; }
.eye-btn { position: absolute; right: 10px; border: none; background: transparent; cursor: pointer; color: #9CA3AF; display: flex; align-items: center; }
.eye-btn:hover { color: #374151; }

.toggle-row { display: flex; align-items: center; gap: 10px; margin-top: 4px; }
.toggle { position: relative; display: inline-block; width: 44px; height: 24px; flex-shrink: 0; }
.toggle input { opacity: 0; width: 0; height: 0; }
.toggle-slider { position: absolute; inset: 0; background: #D1D5DB; border-radius: 12px; transition: background 0.2s; cursor: pointer; }
.toggle-slider::before { content: ''; position: absolute; width: 18px; height: 18px; left: 3px; top: 3px; background: #fff; border-radius: 50%; transition: transform 0.2s; box-shadow: 0 1px 3px rgba(0,0,0,0.2); }
.toggle input:checked + .toggle-slider { background: #4DA3FF; }
.toggle input:checked + .toggle-slider::before { transform: translateX(20px); }
.toggle-label { font-size: 13px; color: #6B7280; }

.range-row { display: flex; align-items: center; gap: 12px; margin-top: 4px; }
.range-row input[type="range"] { flex: 1; accent-color: #4DA3FF; }
.range-val { min-width: 40px; font-size: 14px; font-weight: 600; color: #374151; }

.radio-group { display: flex; gap: 12px; flex-wrap: wrap; margin-top: 4px; }
.radio-item { display: flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #E5E7EB; border-radius: 8px; cursor: pointer; font-size: 14px; color: #374151; transition: all 0.15s; }
.radio-item.active { border-color: #4DA3FF; background: #EFF6FF; color: #3B82F6; }
.radio-item input { display: none; }

.save-bar { margin-top: 24px; padding-top: 16px; border-top: 1px solid #E5E7EB; display: flex; justify-content: flex-end; }
.saved-hint { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #059669; font-weight: 500; }

.btn-primary { display: flex; align-items: center; gap: 6px; padding: 9px 18px; background: #4DA3FF; color: #fff; border: none; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; transition: background 0.15s; }
.btn-primary:hover { background: #3B8FE8; }

@media (max-width: 768px) {
  .config-layout { grid-template-columns: 1fr; }
  .form-grid { grid-template-columns: 1fr; }
}
</style>
