<template>
  <div class="resume-manage page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">简历管理</h1>
        <p class="page-subtitle">查看与管理所有用户上传的简历</p>
      </div>
    </div>

    <!-- Filters -->
    <div class="card filter-bar">
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input v-model="searchQuery" type="text" placeholder="搜索简历名称、用户..." />
      </div>
      <div class="filter-group">
        <select v-model="statusFilter">
          <option value="">全部状态</option>
          <option value="active">已激活</option>
          <option value="draft">草稿</option>
          <option value="archived">已归档</option>
        </select>
      </div>
    </div>

    <!-- Table -->
    <div class="card table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>简历名称</th>
            <th>所属用户</th>
            <th>目标职位</th>
            <th>完整度</th>
            <th>更新时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in filteredResumes" :key="r.id">
            <td>
              <div class="resume-name-cell">
                <div class="file-icon">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/>
                    <polyline points="14 2 14 8 20 8"/>
                    <line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/>
                    <polyline points="10 9 9 9 8 9"/>
                  </svg>
                </div>
                <span>{{ r.name }}</span>
              </div>
            </td>
            <td class="text-muted">{{ r.user }}</td>
            <td>{{ r.targetJob }}</td>
            <td>
              <div class="progress-cell">
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: r.completeness + '%', background: completenessColor(r.completeness) }"></div>
                </div>
                <span class="progress-text">{{ r.completeness }}%</span>
              </div>
            </td>
            <td class="text-muted">{{ r.updatedAt }}</td>
            <td>
              <span class="badge" :class="statusClass(r.status)">{{ statusLabel(r.status) }}</span>
            </td>
            <td>
              <div class="action-btns">
                <button class="btn-icon" title="查看">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
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
        <span class="total-text">共 {{ filteredResumes.length }} 份简历</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const searchQuery = ref('')
const statusFilter = ref('')

const resumes = ref([
  { id: 1, name: '张三的求职简历', user: '张三', targetJob: '前端工程师', completeness: 92, updatedAt: '2024-04-01', status: 'active' },
  { id: 2, name: '李四简历V2', user: '李四', targetJob: '产品经理', completeness: 75, updatedAt: '2024-04-03', status: 'active' },
  { id: 3, name: '王五的简历草稿', user: '王五', targetJob: '后端开发', completeness: 45, updatedAt: '2024-04-05', status: 'draft' },
  { id: 4, name: '赵六职位申请', user: '赵六', targetJob: 'UI设计师', completeness: 88, updatedAt: '2024-04-07', status: 'archived' },
  { id: 5, name: '陈七求职材料', user: '陈七', targetJob: '数据分析师', completeness: 60, updatedAt: '2024-04-10', status: 'active' },
  { id: 6, name: '刘八技术简历', user: '刘八', targetJob: '全栈工程师', completeness: 95, updatedAt: '2024-04-12', status: 'active' },
])

const filteredResumes = computed(() => {
  return resumes.value.filter(r => {
    const matchSearch = !searchQuery.value || r.name.includes(searchQuery.value) || r.user.includes(searchQuery.value)
    const matchStatus = !statusFilter.value || r.status === statusFilter.value
    return matchSearch && matchStatus
  })
})

function completenessColor(val) {
  if (val >= 80) return '#059669'
  if (val >= 60) return '#D97706'
  return '#DC2626'
}

function statusClass(s) {
  return { active: 'badge-green', draft: 'badge-gray', archived: 'badge-orange' }[s] || 'badge-gray'
}

function statusLabel(s) {
  return { active: '已激活', draft: '草稿', archived: '已归档' }[s] || s
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

.resume-name-cell { display: flex; align-items: center; gap: 10px; }
.file-icon { width: 32px; height: 32px; border-radius: 8px; background: #EFF6FF; display: flex; align-items: center; justify-content: center; color: #3B82F6; flex-shrink: 0; }

.progress-cell { display: flex; align-items: center; gap: 8px; }
.progress-bar { width: 80px; height: 6px; background: #E5E7EB; border-radius: 99px; overflow: hidden; }
.progress-fill { height: 100%; border-radius: 99px; transition: width 0.3s; }
.progress-text { font-size: 12px; color: #6B7280; white-space: nowrap; }

.badge { display: inline-block; padding: 2px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.badge-green { background: #ECFDF5; color: #059669; }
.badge-gray { background: #F3F4F6; color: #6B7280; }
.badge-orange { background: #FFF7ED; color: #D97706; }

.action-btns { display: flex; gap: 4px; }
.btn-icon { width: 30px; height: 30px; border-radius: 6px; border: 1px solid #E5E7EB; background: #fff; display: flex; align-items: center; justify-content: center; cursor: pointer; color: #6B7280; transition: all 0.15s; }
.btn-icon:hover { background: #F3F4F6; color: #374151; }
.btn-icon-danger:hover { background: #FEF2F2; color: #DC2626; border-color: #FECACA; }

.pagination { display: flex; align-items: center; justify-content: space-between; padding: 14px 16px; border-top: 1px solid #F3F4F6; }
.total-text { font-size: 13px; color: #6B7280; }
</style>
