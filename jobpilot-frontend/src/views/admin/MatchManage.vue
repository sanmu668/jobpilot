<template>
  <div class="match-manage page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">匹配管理</h1>
        <p class="page-subtitle">查看和管理简历与岗位的 AI 匹配记录</p>
      </div>
    </div>

    <!-- Stats -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon" style="background:#EFF6FF">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#3B82F6" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 11-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
        </div>
        <div>
          <p class="stat-value">{{ stats.total }}</p>
          <p class="stat-label">匹配总数</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:#ECFDF5">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#059669" stroke-width="2">
            <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/>
          </svg>
        </div>
        <div>
          <p class="stat-value">{{ stats.highMatch }}</p>
          <p class="stat-label">高匹配（≥80%）</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:#FFF7ED">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#D97706" stroke-width="2">
            <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
        </div>
        <div>
          <p class="stat-value">{{ stats.midMatch }}</p>
          <p class="stat-label">中匹配（50–79%）</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:#FEF2F2">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#DC2626" stroke-width="2">
            <circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/>
          </svg>
        </div>
        <div>
          <p class="stat-value">{{ stats.lowMatch }}</p>
          <p class="stat-label">低匹配（＜50%）</p>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="card filter-bar">
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input v-model="searchQuery" type="text" placeholder="搜索用户名或岗位名称..." />
      </div>
      <div class="filter-group">
        <select v-model="scoreFilter">
          <option value="">全部匹配度</option>
          <option value="high">高匹配（≥80%）</option>
          <option value="mid">中匹配（50–79%）</option>
          <option value="low">低匹配（＜50%）</option>
        </select>
        <select v-model="statusFilter">
          <option value="">全部状态</option>
          <option value="pending">待处理</option>
          <option value="reviewed">已查看</option>
          <option value="applied">已投递</option>
        </select>
      </div>
    </div>

    <!-- Table -->
    <div class="card table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户</th>
            <th>简历</th>
            <th>岗位</th>
            <th>匹配度</th>
            <th>状态</th>
            <th>匹配时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="match in filteredMatches" :key="match.id">
            <td class="text-muted">#{{ match.id }}</td>
            <td>
              <div class="user-cell">
                <div class="avatar" :style="{ background: match.avatarColor }">{{ match.userName[0] }}</div>
                <span>{{ match.userName }}</span>
              </div>
            </td>
            <td class="text-muted">{{ match.resumeTitle }}</td>
            <td>
              <div class="job-cell">
                <span class="job-title">{{ match.jobTitle }}</span>
                <span class="job-company">{{ match.company }}</span>
              </div>
            </td>
            <td>
              <div class="score-cell">
                <div class="score-bar-track">
                  <div class="score-bar-fill" :style="{ width: match.score + '%', background: scoreColor(match.score) }"></div>
                </div>
                <span class="score-text" :style="{ color: scoreColor(match.score) }">{{ match.score }}%</span>
              </div>
            </td>
            <td>
              <span class="badge" :class="statusClass(match.status)">{{ statusLabel(match.status) }}</span>
            </td>
            <td class="text-muted">{{ match.matchedAt }}</td>
            <td>
              <div class="action-btns">
                <button class="btn-icon" title="查看详情">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
                  </svg>
                </button>
                <button class="btn-icon btn-icon-danger" title="删除">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/>
                    <path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4h6v2"/>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <span class="total-text">共 {{ filteredMatches.length }} 条</span>
        <div class="page-btns">
          <button class="page-btn" :disabled="page <= 1" @click="page--">«</button>
          <button v-for="p in totalPages" :key="p" class="page-btn" :class="{ active: p === page }" @click="page = p">{{ p }}</button>
          <button class="page-btn" :disabled="page >= totalPages" @click="page++">»</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const searchQuery = ref('')
const scoreFilter = ref('')
const statusFilter = ref('')
const page = ref(1)
const pageSize = 10

const matches = ref([
  { id: 1, userName: '张三', resumeTitle: '前端工程师简历', jobTitle: '高级前端工程师', company: '字节跳动', score: 92, status: 'applied', matchedAt: '2024-03-01 10:20', avatarColor: '#4DA3FF' },
  { id: 2, userName: '李四', resumeTitle: 'Java 开发简历', jobTitle: 'Java 后端工程师', company: '阿里巴巴', score: 85, status: 'reviewed', matchedAt: '2024-03-02 14:05', avatarColor: '#67C23A' },
  { id: 3, userName: '王五', resumeTitle: '产品经理简历', jobTitle: '产品经理', company: '腾讯', score: 78, status: 'pending', matchedAt: '2024-03-03 09:30', avatarColor: '#E6A23C' },
  { id: 4, userName: '赵六', resumeTitle: '数据分析简历', jobTitle: '数据科学家', company: '百度', score: 65, status: 'reviewed', matchedAt: '2024-03-04 16:45', avatarColor: '#F56C6C' },
  { id: 5, userName: '陈七', resumeTitle: 'UI 设计师简历', jobTitle: 'UI/UX 设计师', company: '美团', score: 43, status: 'pending', matchedAt: '2024-03-05 11:00', avatarColor: '#9B59B6' },
  { id: 6, userName: '刘八', resumeTitle: '运维工程师简历', jobTitle: 'DevOps 工程师', company: '滴滴', score: 88, status: 'applied', matchedAt: '2024-03-06 08:20', avatarColor: '#1ABC9C' },
  { id: 7, userName: '周九', resumeTitle: '算法工程师简历', jobTitle: '机器学习工程师', company: '小米', score: 72, status: 'pending', matchedAt: '2024-03-07 13:15', avatarColor: '#E74C3C' },
  { id: 8, userName: '吴十', resumeTitle: '全栈开发简历', jobTitle: '全栈工程师', company: '京东', score: 55, status: 'reviewed', matchedAt: '2024-03-08 17:30', avatarColor: '#3498DB' },
])

const stats = computed(() => ({
  total: matches.value.length,
  highMatch: matches.value.filter(m => m.score >= 80).length,
  midMatch: matches.value.filter(m => m.score >= 50 && m.score < 80).length,
  lowMatch: matches.value.filter(m => m.score < 50).length,
}))

const filteredMatches = computed(() => {
  return matches.value.filter(m => {
    const matchSearch = !searchQuery.value ||
      m.userName.includes(searchQuery.value) ||
      m.jobTitle.includes(searchQuery.value)
    const matchScore = !scoreFilter.value ||
      (scoreFilter.value === 'high' && m.score >= 80) ||
      (scoreFilter.value === 'mid' && m.score >= 50 && m.score < 80) ||
      (scoreFilter.value === 'low' && m.score < 50)
    const matchStatus = !statusFilter.value || m.status === statusFilter.value
    return matchSearch && matchScore && matchStatus
  })
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredMatches.value.length / pageSize)))

function scoreColor(score) {
  if (score >= 80) return '#059669'
  if (score >= 50) return '#D97706'
  return '#DC2626'
}

function statusClass(status) {
  return { applied: 'badge-green', reviewed: 'badge-blue', pending: 'badge-gray' }[status] || 'badge-gray'
}

function statusLabel(status) {
  return { applied: '已投递', reviewed: '已查看', pending: '待处理' }[status] || status
}
</script>

<style scoped>
.page-container { padding: 24px; }
.page-header { display: flex; align-items: flex-start; justify-content: space-between; margin-bottom: 24px; }
.page-title { font-size: 22px; font-weight: 700; color: #111827; margin: 0 0 4px; }
.page-subtitle { font-size: 14px; color: #6B7280; margin: 0; }

.stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }
.stat-card { background: #fff; border: 1px solid #E5E7EB; border-radius: 12px; padding: 20px; display: flex; align-items: center; gap: 16px; }
.stat-icon { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.stat-value { font-size: 24px; font-weight: 700; color: #111827; margin: 0 0 2px; }
.stat-label { font-size: 13px; color: #6B7280; margin: 0; }

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

.user-cell { display: flex; align-items: center; gap: 10px; }
.avatar { width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 600; font-size: 13px; flex-shrink: 0; }

.job-cell { display: flex; flex-direction: column; gap: 2px; }
.job-title { font-weight: 500; color: #111827; }
.job-company { font-size: 12px; color: #9CA3AF; }

.score-cell { display: flex; align-items: center; gap: 8px; }
.score-bar-track { flex: 1; height: 6px; background: #F3F4F6; border-radius: 3px; overflow: hidden; min-width: 60px; }
.score-bar-fill { height: 100%; border-radius: 3px; transition: width 0.3s; }
.score-text { font-size: 13px; font-weight: 600; min-width: 36px; text-align: right; }

.badge { display: inline-block; padding: 2px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.badge-blue { background: #EFF6FF; color: #3B82F6; }
.badge-green { background: #ECFDF5; color: #059669; }
.badge-gray { background: #F3F4F6; color: #6B7280; }

.action-btns { display: flex; gap: 4px; }
.btn-icon { width: 30px; height: 30px; border-radius: 6px; border: 1px solid #E5E7EB; background: #fff; display: flex; align-items: center; justify-content: center; cursor: pointer; color: #6B7280; transition: all 0.15s; }
.btn-icon:hover { background: #F3F4F6; color: #374151; }
.btn-icon-danger:hover { background: #FEF2F2; color: #DC2626; border-color: #FECACA; }

.pagination { display: flex; align-items: center; justify-content: space-between; padding: 14px 16px; border-top: 1px solid #F3F4F6; }
.total-text { font-size: 13px; color: #6B7280; }
.page-btns { display: flex; gap: 4px; }
.page-btn { min-width: 32px; height: 32px; padding: 0 8px; border-radius: 6px; border: 1px solid #E5E7EB; background: #fff; font-size: 13px; cursor: pointer; color: #374151; transition: all 0.15s; }
.page-btn:hover:not(:disabled) { background: #F3F4F6; }
.page-btn.active { background: #4DA3FF; color: #fff; border-color: #4DA3FF; }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }

@media (max-width: 900px) {
  .stats-row { grid-template-columns: repeat(2, 1fr); }
}
</style>
