<template>
  <div class="user-manage page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">用户管理</h1>
        <p class="page-subtitle">管理平台所有注册用户</p>
      </div>
      <button class="btn-primary" @click="showAddModal = true">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        新增用户
      </button>
    </div>

    <!-- Filters -->
    <div class="card filter-bar">
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input v-model="searchQuery" type="text" placeholder="搜索用户名、邮箱..." />
      </div>
      <div class="filter-group">
        <select v-model="roleFilter">
          <option value="">全部角色</option>
          <option value="user">普通用户</option>
          <option value="admin">管理员</option>
        </select>
        <select v-model="statusFilter">
          <option value="">全部状态</option>
          <option value="active">正常</option>
          <option value="banned">封禁</option>
        </select>
      </div>
    </div>

    <!-- Table -->
    <div class="card table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th><input type="checkbox" @change="toggleAll" /></th>
            <th>用户</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>注册时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td><input type="checkbox" v-model="selected" :value="user.id" /></td>
            <td>
              <div class="user-cell">
                <div class="avatar" :style="{ background: user.avatarColor }">{{ user.name[0] }}</div>
                <span>{{ user.name }}</span>
              </div>
            </td>
            <td class="text-muted">{{ user.email }}</td>
            <td>
              <span class="badge" :class="user.role === 'admin' ? 'badge-purple' : 'badge-blue'">
                {{ user.role === 'admin' ? '管理员' : '普通用户' }}
              </span>
            </td>
            <td class="text-muted">{{ user.createdAt }}</td>
            <td>
              <span class="badge" :class="user.status === 'active' ? 'badge-green' : 'badge-red'">
                {{ user.status === 'active' ? '正常' : '封禁' }}
              </span>
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

      <!-- Pagination -->
      <div class="pagination">
        <span class="total-text">共 {{ filteredUsers.length }} 条</span>
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
const roleFilter = ref('')
const statusFilter = ref('')
const selected = ref([])
const page = ref(1)
const pageSize = 10
const showAddModal = ref(false)

const users = ref([
  { id: 1, name: '张三', email: 'zhangsan@example.com', role: 'user', status: 'active', createdAt: '2024-01-10', avatarColor: '#4DA3FF' },
  { id: 2, name: '李四', email: 'lisi@example.com', role: 'user', status: 'active', createdAt: '2024-01-15', avatarColor: '#67C23A' },
  { id: 3, name: '王五', email: 'wangwu@example.com', role: 'admin', status: 'active', createdAt: '2024-01-20', avatarColor: '#E6A23C' },
  { id: 4, name: '赵六', email: 'zhaoliu@example.com', role: 'user', status: 'banned', createdAt: '2024-02-01', avatarColor: '#F56C6C' },
  { id: 5, name: '陈七', email: 'chenqi@example.com', role: 'user', status: 'active', createdAt: '2024-02-10', avatarColor: '#9B59B6' },
  { id: 6, name: '刘八', email: 'liuba@example.com', role: 'user', status: 'active', createdAt: '2024-02-15', avatarColor: '#1ABC9C' },
  { id: 7, name: '周九', email: 'zhoujiu@example.com', role: 'user', status: 'active', createdAt: '2024-03-01', avatarColor: '#E74C3C' },
  { id: 8, name: '吴十', email: 'wushi@example.com', role: 'admin', status: 'active', createdAt: '2024-03-05', avatarColor: '#3498DB' },
])

const filteredUsers = computed(() => {
  return users.value.filter(u => {
    const matchSearch = !searchQuery.value ||
      u.name.includes(searchQuery.value) ||
      u.email.includes(searchQuery.value)
    const matchRole = !roleFilter.value || u.role === roleFilter.value
    const matchStatus = !statusFilter.value || u.status === statusFilter.value
    return matchSearch && matchRole && matchStatus
  })
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredUsers.value.length / pageSize)))

function toggleAll(e) {
  selected.value = e.target.checked ? filteredUsers.value.map(u => u.id) : []
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

.user-cell { display: flex; align-items: center; gap: 10px; }
.avatar { width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: 600; font-size: 13px; flex-shrink: 0; }

.badge { display: inline-block; padding: 2px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.badge-blue { background: #EFF6FF; color: #3B82F6; }
.badge-purple { background: #F5F3FF; color: #7C3AED; }
.badge-green { background: #ECFDF5; color: #059669; }
.badge-red { background: #FEF2F2; color: #DC2626; }

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

.btn-primary { display: flex; align-items: center; gap: 6px; padding: 9px 18px; background: #4DA3FF; color: #fff; border: none; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; transition: background 0.15s; }
.btn-primary:hover { background: #3B8FE8; }
</style>
