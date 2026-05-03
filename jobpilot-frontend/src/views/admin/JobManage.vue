<template>
  <div class="job-manage page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">岗位管理</h1>
        <p class="page-subtitle">管理平台发布的所有招聘岗位</p>
      </div>
      <button class="btn-primary">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        发布岗位
      </button>
    </div>

    <!-- Stats -->
    <div class="stats-row">
      <div class="stat-card card" v-for="s in stats" :key="s.label">
        <div class="stat-icon" :style="{ background: s.bg, color: s.color }">
          <span v-html="s.icon"></span>
        </div>
        <div>
          <div class="stat-value">{{ s.value }}</div>
          <div class="stat-label">{{ s.label }}</div>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="card filter-bar">
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input v-model="searchQuery" type="text" placeholder="搜索岗位名称、公司..." />
      </div>
      <div class="filter-group">
        <select v-model="typeFilter">
          <option value="">全部类型</option>
          <option value="full">全职</option>
          <option value="part">兼职</option>
          <option value="intern">实习</option>
        </select>
        <select v-model="statusFilter">
          <option value="">全部状态</option>
          <option value="open">招聘中</option>
          <option value="paused">暂停</option>
          <option value="closed">已关闭</option>
        </select>
      </div>
    </div>

    <!-- Table -->
    <div class="card table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>岗位名称</th>
            <th>公司</th>
            <th>薪资范围</th>
            <th>类型</th>
            <th>申请数</th>
            <th>发布时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="job in filteredJobs" :key="job.id">
            <td>
              <div class="job-name">
                <span class="job-title">{{ job.title }}</span>
                <span class="job-location">{{ job.location }}</span>
              </div>
            </td>
            <td>
              <div class="company-cell">
                <div class="company-logo" :style="{ background: job.logoColor }">{{ job.company[0] }}</div>
                <span>{{ job.company }}</span>
              </div>
            </td>
            <td class="salary">{{ job.salary }}</td>
            <td>
              <span class="badge" :class="typeClass(job.type)">{{ typeLabel(job.type) }}</span>
            </td>
            <td>
              <span class="apply-count">{{ job.applies }}</span>
            </td>
            <td class="text-muted">{{ job.publishedAt }}</td>
            <td>
              <span class="badge" :class="statusClass(job.status)">{{ statusLabel(job.status) }}</span>
            </td>
            <td>
              <div class="action-btns">
                <button class="btn-icon" title="编辑">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
                    <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
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
        <span class="total-text">共 {{ filteredJobs.length }} 个岗位</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const searchQuery = ref('')
const typeFilter = ref('')
const statusFilter = ref('')

const stats = [
  { label: '在招岗位', value: '128', bg: '#EFF6FF', color: '#3B82F6', icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 21V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v16"/></svg>' },
  { label: '本月新增', value: '34', bg: '#ECFDF5', color: '#059669', icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>' },
  { label: '总申请数', value: '2,847', bg: '#FFF7ED', color: '#D97706', icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>' },
]

const jobs = ref([
  { id: 1, title: '高级前端工程师', location: '北京', company: '字节跳动', logoColor: '#4DA3FF', salary: '25-40k', type: 'full', applies: 156, publishedAt: '2024-04-01', status: 'open' },
  { id: 2, title: '产品经理', location: '上海', company: '阿里巴巴', logoColor: '#FF6B35', salary: '20-35k', type: 'full', applies: 203, publishedAt: '2024-04-02', status: 'open' },
  { id: 3, title: '数据分析实习生', location: '深圳', company: '腾讯', logoColor: '#1DA462', salary: '6-8k', type: 'intern', applies: 89, publishedAt: '2024-04-05', status: 'open' },
  { id: 4, title: 'UI/UX 设计师', location: '广州', company: '网易', logoColor: '#E74C3C', salary: '15-25k', type: 'full', applies: 67, publishedAt: '2024-04-08', status: 'paused' },
  { id: 5, title: '后端开发工程师', location: '杭州', company: '美团', logoColor: '#F39C12', salary: '20-35k', type: 'full', applies: 112, publishedAt: '2024-04-10', status: 'open' },
  { id: 6, title: '运营专员（兼职）', location: '北京', company: '小红书', logoColor: '#FF2D55', salary: '5-8k', type: 'part', applies: 44, publishedAt: '2024-04-12', status: 'closed' },
])

const filteredJobs = computed(() => {
  return jobs.value.filter(j => {
    const matchSearch = !searchQuery.value || j.title.includes(searchQuery.value) || j.company.includes(searchQuery.value)
    const matchType = !typeFilter.value || j.type === typeFilter.value
    const matchStatus = !statusFilter.value || j.status === statusFilter.value
    return matchSearch && matchType && matchStatus
  })
})

function typeClass(t) { return { full: 'badge-blue', part: 'badge-purple', intern: 'badge-orange' }[t] || 'badge-gray' }
function typeLabel(t) { return { full: '全职', part: '兼职', intern: '实习' }[t] || t }
function statusClass(s) { return { open: 'badge-green', paused: 'badge-orange', closed: 'badge-gray' }[s] || 'badge-gray' }
function statusLabel(s) { return { open: '招聘中', paused: '暂停', closed: '已关闭' }[s] || s }
</script>

<style scoped>
.page-container { padding: 24px; }
.page-header { display: flex; align-items: flex-start; justify-content: space-between; margin-bottom: 24px; }
.page-title { font-size: 22px; font-weight: 700; color: #111827; margin: 0 0 4px; }
.page-subtitle { font-size: 14px; color: #6B7280; margin: 0; }

.stats-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 16px; }
.stat-card { display: flex; align-items: center; gap: 16px; padding: 18px 20px; }
.stat-icon { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.stat-value { font-size: 22px; font-weight: 700; color: #111827; line-height: 1.2; }
.stat-label { font-size: 13px; color: #6B7280; margin-top: 2px; }

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
.salary { font-weight: 600; color: #059669; }

.job-name { display: flex; flex-direction: column; gap: 2px; }
.job-title { font-weight: 500; color: #111827; }
.job-location { font-size: 12px; color: #9CA3AF; }

.company-cell { display: flex; align-items: center; gap: 8px; }
.company-logo { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 12px; font-weight: 700; flex-shrink: 0; }

.apply-count { font-weight: 600; color: #374151; }

.badge { display: inline-block; padding: 2px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.badge-blue { background: #EFF6FF; color: #3B82F6; }
.badge-purple { background: #F5F3FF; color: #7C3AED; }
.badge-green { background: #ECFDF5; color: #059669; }
.badge-orange { background: #FFF7ED; color: #D97706; }
.badge-gray { background: #F3F4F6; color: #6B7280; }

.action-btns { display: flex; gap: 4px; }
.btn-icon { width: 30px; height: 30px; border-radius: 6px; border: 1px solid #E5E7EB; background: #fff; display: flex; align-items: center; justify-content: center; cursor: pointer; color: #6B7280; transition: all 0.15s; }
.btn-icon:hover { background: #F3F4F6; color: #374151; }
.btn-icon-danger:hover { background: #FEF2F2; color: #DC2626; border-color: #FECACA; }

.pagination { display: flex; align-items: center; justify-content: space-between; padding: 14px 16px; border-top: 1px solid #F3F4F6; }
.total-text { font-size: 13px; color: #6B7280; }

.btn-primary { display: flex; align-items: center; gap: 6px; padding: 9px 18px; background: #4DA3FF; color: #fff; border: none; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; transition: background 0.15s; }
.btn-primary:hover { background: #3B8FE8; }
</style>
