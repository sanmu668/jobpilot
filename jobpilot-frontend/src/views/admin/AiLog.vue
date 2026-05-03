<template>
  <div class="ai-log page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">AI 日志</h1>
        <p class="page-subtitle">查看所有 AI 分析与匹配操作的调用记录</p>
      </div>
      <button class="btn-danger" @click="clearLogs">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/>
          <path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4h6v2"/>
        </svg>
        清空日志
      </button>
    </div>

    <!-- Filters -->
    <div class="card filter-bar">
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input v-model="searchQuery" type="text" placeholder="搜索用户、操作类型..." />
      </div>
      <div class="filter-group">
        <select v-model="typeFilter">
          <option value="">全部类型</option>
          <option value="match">简历匹配</option>
          <option value="analyze">简历分析</option>
          <option value="suggest">优化建议</option>
          <option value="score">评分生成</option>
        </select>
        <select v-model="statusFilter">
          <option value="">全部状态</option>
          <option value="success">成功</option>
          <option value="error">失败</option>
        </select>
      </div>
    </div>

    <!-- Log Table -->
    <div class="card table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>时间</th>
            <th>用户</th>
            <th>操作类型</th>
            <th>输入摘要</th>
            <th>耗时</th>
            <th>Token 用量</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in filteredLogs" :key="log.id">
            <td class="text-muted mono">{{ log.time }}</td>
            <td>
              <div class="user-cell">
                <div class="avatar" :style="{ background: log.avatarColor }">{{ log.userName[0] }}</div>
                <span>{{ log.userName }}</span>
              </div>
            </td>
            <td>
              <span class="badge" :class="typeClass(log.type)">{{ typeLabel(log.type) }}</span>
            </td>
            <td class="text-muted summary-cell">{{ log.summary }}</td>
            <td class="text-muted">{{ log.duration }}ms</td>
            <td class="text-muted">{{ log.tokens.toLocaleString() }}</td>
            <td>
              <span class="badge" :class="log.status === 'success' ? 'badge-green' : 'badge-red'">
                {{ log.status === 'success' ? '成功' : '失败' }}
              </span>
            </td>
            <td>
              <button class="btn-icon" title="查看详情" @click="viewDetail(log)">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <span class="total-text">共 {{ filteredLogs.length }} 条</span>
        <div class="page-btns">
          <button class="page-btn" :disabled="page <= 1" @click="page--">«</button>
          <button v-for="p in totalPages" :key="p" class="page-btn" :class="{ active: p === page }" @click="page = p">{{ p }}</button>
          <button class="page-btn" :disabled="page >= totalPages" @click="page++">»</button>
        </div>
      </div>
    </div>

    <!-- Detail Modal -->
    <div v-if="activeLog" class="modal-overlay" @click.self="activeLog = null">
      <div class="modal">
        <div class="modal-header">
          <h3>日志详情</h3>
          <button class="close-btn" @click="activeLog = null">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="detail-row"><span>时间</span><span>{{ activeLog.time }}</span></div>
          <div class="detail-row"><span>用户</span><span>{{ activeLog.userName }}</span></div>
          <div class="detail-row"><span>操作</span><span>{{ typeLabel(activeLog.type) }}</span></div>
          <div class="detail-row"><span>状态</span><span>{{ activeLog.status === 'success' ? '成功' : '失败' }}</span></div>
          <div class="detail-row"><span>耗时</span><span>{{ activeLog.duration }}ms</span></div>
          <div class="detail-row"><span>Token</span><span>{{ activeLog.tokens.toLocaleString() }}</span></div>
          <div class="detail-section">
            <p class="detail-section-title">请求摘要</p>
            <div class="code-block">{{ activeLog.summary }}</div>
          </div>
          <div v-if="activeLog.error" class="detail-section">
            <p class="detail-section-title error-title">错误信息</p>
            <div class="code-block error-block">{{ activeLog.error }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const searchQuery = ref('')
const typeFilter = ref('')
const statusFilter = ref('')
const page = ref(1)
const pageSize = 10
const activeLog = ref(null)

const logs = ref([
  { id: 1, time: '2024-03-08 17:32:10', userName: '张三', type: 'match', summary: '简历《前端工程师》匹配岗位《高级前端工程师》', duration: 1240, tokens: 3200, status: 'success', avatarColor: '#4DA3FF' },
  { id: 2, time: '2024-03-08 16:15:44', userName: '李四', type: 'analyze', summary: '分析简历《Java开发工程师》综合能力', duration: 980, tokens: 2800, status: 'success', avatarColor: '#67C23A' },
  { id: 3, time: '2024-03-08 15:00:21', userName: '王五', type: 'suggest', summary: '为简历《产品经理》生成优化建议', duration: 1560, tokens: 4100, status: 'success', avatarColor: '#E6A23C' },
  { id: 4, time: '2024-03-08 14:22:05', userName: '赵六', type: 'score', summary: '为简历《数据分析师》生成综合评分', duration: 320, tokens: 800, status: 'error', error: 'OpenAI API rate limit exceeded. Please retry after 60 seconds.', avatarColor: '#F56C6C' },
  { id: 5, time: '2024-03-08 13:45:30', userName: '陈七', type: 'match', summary: '简历《UI设计师》匹配岗位《UI/UX设计师》', duration: 1100, tokens: 2900, status: 'success', avatarColor: '#9B59B6' },
  { id: 6, time: '2024-03-08 11:30:18', userName: '刘八', type: 'analyze', summary: '分析简历《DevOps工程师》综合能力', duration: 870, tokens: 2400, status: 'success', avatarColor: '#1ABC9C' },
  { id: 7, time: '2024-03-07 18:05:00', userName: '周九', type: 'suggest', summary: '为简历《算法工程师》生成优化建议', duration: 2010, tokens: 5200, status: 'success', avatarColor: '#E74C3C' },
  { id: 8, time: '2024-03-07 15:20:33', userName: '吴十', type: 'score', summary: '为简历《全栈开发》生成综合评分', duration: 410, tokens: 950, status: 'success', avatarColor: '#3498DB' },
])

const filteredLogs = computed(() => {
  return logs.value.filter(l => {
    const matchSearch = !searchQuery.value ||
      l.userName.includes(searchQuery.value) ||
      l.summary.includes(searchQuery.value)
    const matchType = !typeFilter.value || l.type === typeFilter.value
    const matchStatus = !statusFilter.value || l.status === statusFilter.value
    return matchSearch && matchType && matchStatus
  })
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredLogs.value.length / pageSize)))

function typeLabel(type) {
  return { match: '简历匹配', analyze: '简历分析', suggest: '优化建议', score: '评分生成' }[type] || type
}

function typeClass(type) {
  return { match: 'badge-blue', analyze: 'badge-purple', suggest: 'badge-orange', score: 'badge-teal' }[type] || 'badge-gray'
}

function viewDetail(log) {
  activeLog.value = log
}

function clearLogs() {
  if (confirm('确定要清空所有日志吗？此操作不可恢复。')) {
    logs.value = []
  }
}
</script>

<style scoped>
.page-container { padding: 24px; }
.page-header { display: flex; align-items: flex-start; justify-content: space-between; margin-bottom: 24px; }
.page-title { font-size: 22px; font-weight: 700; color: #111827; margin: 0 0 4px; }
.page-subtitle { font-size: 14px; color: #6B7280; margin: 0; }

.card { background: #fff; border-radius: 12px; border: 1px solid #E5E7EB; }
.filter-bar { display: flex; align-items: center; gap: 16px; padding: 16px 20px; margin-bottom: 16px; flex-wrap: wrap; }
.search-box { display: flex; align-items: center; gap: 8px; flex: 1; min-width: 200px; background: #F9FAFB; border: 1px solid #E5E7EB; border-radius: 8px; padding: 8px 12px; }
.search-box input { border: none; background: transparent; outline: none; font-size: 14px; width: 100%; }
.search-box svg { color: #9CA3AF; flex-shrink: 0; }
.filter-group { display: flex; gap: 8px; }
.filter-group select { border: 1px solid #E5E7EB; border-radius: 8px; padding: 8px 12px; font-size: 14px; background: #F9FAFB; color: #374151; outline: none; cursor: pointer; }

.table-card { overflow: hidden; }
.data-table { width: 100%; border-collapse: collapse; font-size: 14px; }
.data-table th { padding: 12px 16px; text-align: left; font-size: 12px; font-weight: 600; color: #6B7280; text-transform: uppercase; letter-spacing: 0.05em; background: #F9FAFB; border-bottom: 1px solid #E5E7EB; }
.data-table td { padding: 14px 16px; border-bottom: 1px solid #F3F4F6; vertical-align: middle; }
.data-table tbody tr:last-child td { border-bottom: none; }
.data-table tbody tr:hover { background: #F9FAFB; }
.text-muted { color: #6B7280; }
.mono { font-family: 'Courier New', monospace; font-size: 13px; }

.summary-cell { max-width: 240px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

.user-cell { display: flex; align-items: center; gap: 10px; }
.avatar { width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 600; font-size: 13px; flex-shrink: 0; }

.badge { display: inline-block; padding: 2px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.badge-blue { background: #EFF6FF; color: #3B82F6; }
.badge-purple { background: #F5F3FF; color: #7C3AED; }
.badge-orange { background: #FFF7ED; color: #D97706; }
.badge-teal { background: #ECFDF5; color: #0D9488; }
.badge-green { background: #ECFDF5; color: #059669; }
.badge-red { background: #FEF2F2; color: #DC2626; }
.badge-gray { background: #F3F4F6; color: #6B7280; }

.btn-icon { width: 30px; height: 30px; border-radius: 6px; border: 1px solid #E5E7EB; background: #fff; display: flex; align-items: center; justify-content: center; cursor: pointer; color: #6B7280; transition: all 0.15s; }
.btn-icon:hover { background: #F3F4F6; color: #374151; }

.btn-danger { display: flex; align-items: center; gap: 6px; padding: 9px 18px; background: #FEF2F2; color: #DC2626; border: 1px solid #FECACA; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s; }
.btn-danger:hover { background: #FEE2E2; }

.pagination { display: flex; align-items: center; justify-content: space-between; padding: 14px 16px; border-top: 1px solid #F3F4F6; }
.total-text { font-size: 13px; color: #6B7280; }
.page-btns { display: flex; gap: 4px; }
.page-btn { min-width: 32px; height: 32px; padding: 0 8px; border-radius: 6px; border: 1px solid #E5E7EB; background: #fff; font-size: 13px; cursor: pointer; color: #374151; transition: all 0.15s; }
.page-btn:hover:not(:disabled) { background: #F3F4F6; }
.page-btn.active { background: #4DA3FF; color: #fff; border-color: #4DA3FF; }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }

.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: #fff; border-radius: 16px; width: 520px; max-width: 90vw; box-shadow: 0 20px 60px rgba(0,0,0,0.15); }
.modal-header { display: flex; align-items: center; justify-content: space-between; padding: 20px 24px; border-bottom: 1px solid #E5E7EB; }
.modal-header h3 { font-size: 16px; font-weight: 600; color: #111827; margin: 0; }
.close-btn { width: 32px; height: 32px; border-radius: 8px; border: none; background: #F3F4F6; display: flex; align-items: center; justify-content: center; cursor: pointer; color: #6B7280; }
.close-btn:hover { background: #E5E7EB; }
.modal-body { padding: 24px; display: flex; flex-direction: column; gap: 12px; }
.detail-row { display: flex; justify-content: space-between; font-size: 14px; }
.detail-row span:first-child { color: #6B7280; }
.detail-row span:last-child { color: #111827; font-weight: 500; }
.detail-section { margin-top: 8px; }
.detail-section-title { font-size: 12px; font-weight: 600; color: #6B7280; text-transform: uppercase; letter-spacing: 0.05em; margin: 0 0 8px; }
.error-title { color: #DC2626; }
.code-block { background: #F9FAFB; border: 1px solid #E5E7EB; border-radius: 8px; padding: 12px; font-family: 'Courier New', monospace; font-size: 13px; color: #374151; line-height: 1.6; }
.error-block { background: #FEF2F2; border-color: #FECACA; color: #B91C1C; }
</style>
