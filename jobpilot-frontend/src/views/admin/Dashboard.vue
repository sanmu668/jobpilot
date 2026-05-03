<template>
  <div class="dashboard page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">控制台</h1>
        <p class="page-subtitle">欢迎回来，{{ authStore.userName }} · {{ today }}</p>
      </div>
      <button class="btn-primary">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="23 4 23 10 17 10"/><polyline points="1 20 1 14 7 14"/>
          <path d="M3.51 9a9 9 0 0114.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0020.49 15"/>
        </svg>
        刷新数据
      </button>
    </div>

    <!-- KPI Cards -->
    <div class="kpi-grid">
      <div v-for="kpi in kpiCards" :key="kpi.label" class="kpi-card card">
        <div class="kpi-icon" :style="{ background: kpi.bg }">
          <component :is="'div'" v-html="kpi.icon" class="icon-wrap" :style="{ color: kpi.color }"></component>
        </div>
        <div class="kpi-info">
          <span class="kpi-label">{{ kpi.label }}</span>
          <strong class="kpi-value">{{ kpi.value }}</strong>
          <div class="kpi-trend" :class="kpi.trendUp ? 'up' : 'down'">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline v-if="kpi.trendUp" points="18 15 12 9 6 15"/>
              <polyline v-else points="6 9 12 15 18 9"/>
            </svg>
            {{ kpi.trend }} 较上周
          </div>
        </div>
      </div>
    </div>

    <!-- Charts Row -->
    <div class="charts-row">
      <!-- Activity Chart (Fake SVG) -->
      <div class="card chart-card">
        <div class="chart-header">
          <h3>用户活跃趋势</h3>
          <div class="chart-period">
            <button :class="{active: period==='7d'}" @click="period='7d'">7天</button>
            <button :class="{active: period==='30d'}" @click="period='30d'">30天</button>
            <button :class="{active: period==='90d'}" @click="period='90d'">90天</button>
          </div>
        </div>
        <div class="activity-chart">
          <svg :viewBox="`0 0 ${chartWidth} 200`" width="100%" height="200" class="line-chart">
            <!-- Grid lines -->
            <line v-for="i in 4" :key="i" x1="40" :x2="chartWidth - 20" :y1="i * 40" :y2="i * 40" stroke="#F3F4F6" stroke-width="1"/>
            <!-- Y labels -->
            <text v-for="i in 4" :key="'y'+i" x="32" :y="i * 40 + 4" text-anchor="end" font-size="10" fill="#9CA3AF">{{ (5-i)*250 }}</text>
            <!-- Line path -->
            <path :d="linePathD" fill="none" stroke="url(#lineGrad)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
            <!-- Fill area -->
            <path :d="areaPathD" fill="url(#areaGrad)" opacity="0.3"/>
            <!-- Dots -->
            <circle v-for="point in chartPoints" :key="point.x" :cx="point.x" :cy="point.y" r="4" fill="#4DA3FF" stroke="white" stroke-width="2"/>
            <!-- X labels -->
            <text v-for="(point, i) in chartPoints" :key="'x'+i" :x="point.x" y="195" text-anchor="middle" font-size="10" fill="#9CA3AF">{{ point.label }}</text>
            <!-- Gradients -->
            <defs>
              <linearGradient id="lineGrad" x1="0%" y1="0%" x2="100%" y2="0%">
                <stop offset="0%" stop-color="#4DA3FF"/><stop offset="100%" stop-color="#7C3AED"/>
              </linearGradient>
              <linearGradient id="areaGrad" x1="0%" y1="0%" x2="0%" y2="100%">
                <stop offset="0%" stop-color="#4DA3FF"/><stop offset="100%" stop-color="transparent"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
      </div>

      <!-- Distribution Pie (fake) -->
      <div class="card chart-card small">
        <div class="chart-header">
          <h3>简历类型分布</h3>
        </div>
        <div class="pie-chart-wrap">
          <svg viewBox="0 0 160 160" width="160" height="160">
            <circle cx="80" cy="80" r="60" fill="none" stroke="#EAF4FF" stroke-width="20"/>
            <circle cx="80" cy="80" r="60" fill="none" stroke="#4DA3FF" stroke-width="20"
              stroke-dasharray="188 188" stroke-dashoffset="0" transform="rotate(-90 80 80)"/>
            <circle cx="80" cy="80" r="60" fill="none" stroke="#7C3AED" stroke-width="20"
              stroke-dasharray="94 282" stroke-dashoffset="-188" transform="rotate(-90 80 80)"/>
            <circle cx="80" cy="80" r="60" fill="none" stroke="#10B981" stroke-width="20"
              stroke-dasharray="47 329" stroke-dashoffset="-282" transform="rotate(-90 80 80)"/>
            <text x="80" y="76" text-anchor="middle" font-size="20" font-weight="800" fill="#1F2937">2,847</text>
            <text x="80" y="90" text-anchor="middle" font-size="10" fill="#9CA3AF">总简历数</text>
          </svg>
        </div>
        <div class="pie-legend">
          <div v-for="item in pieLegend" :key="item.label" class="legend-row">
            <span class="legend-dot" :style="{ background: item.color }"></span>
            <span class="legend-label">{{ item.label }}</span>
            <strong>{{ item.pct }}%</strong>
          </div>
        </div>
      </div>
    </div>

    <!-- Bottom Row: Recent Activity + Top Jobs -->
    <div class="bottom-row">
      <!-- Recent Activity -->
      <div class="card activity-card">
        <div class="card-header-row">
          <h3>最新动态</h3>
          <a href="#" class="view-all">查看全部</a>
        </div>
        <div class="activity-list">
          <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
            <div class="activity-avatar" :style="{ background: activity.color }">{{ activity.user.charAt(0) }}</div>
            <div class="activity-info">
              <span><strong>{{ activity.user }}</strong> {{ activity.action }}</span>
              <span class="activity-time">{{ activity.time }}</span>
            </div>
            <div class="activity-badge" :class="activity.type">{{ activity.typeLabel }}</div>
          </div>
        </div>
      </div>

      <!-- Top Matched Jobs -->
      <div class="card top-jobs-card">
        <div class="card-header-row">
          <h3>热门岗位</h3>
          <router-link to="/admin/jobs" class="view-all">管理</router-link>
        </div>
        <div class="job-rank-list">
          <div v-for="(job, i) in topJobs" :key="job.id" class="job-rank-item">
            <div class="rank-num" :class="{ top: i < 3 }">{{ i + 1 }}</div>
            <div class="job-rank-info">
              <strong>{{ job.title }}</strong>
              <span>{{ job.company }}</span>
            </div>
            <div class="rank-stats">
              <span>{{ job.applicants }} 投递</span>
              <div class="match-mini" :class="getScoreClass(job.avgMatch)">{{ job.avgMatch }}%</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '@/store/modules/auth'

const authStore = useAuthStore()
const period = ref('7d')

const today = new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })

const kpiCards = [
  { label: '总用户数', value: '12,847', trend: '+8.2%', trendUp: true, icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>', bg: 'rgba(77,163,255,0.1)', color: '#4DA3FF' },
  { label: '简历总数', value: '2,847', trend: '+12.4%', trendUp: true, icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>', bg: 'rgba(124,58,237,0.1)', color: '#7C3AED' },
  { label: 'AI 分析次数', value: '38,402', trend: '+23.1%', trendUp: true, icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>', bg: 'rgba(16,185,129,0.1)', color: '#10B981' },
  { label: '平均匹配分', value: '78.3', trend: '+3.5%', trendUp: true, icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>', bg: 'rgba(245,158,11,0.1)', color: '#F59E0B' },
]

const chartWidth = 600
const rawData = [320, 480, 420, 560, 490, 720, 680]
const labels = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']

const chartPoints = computed(() => {
  const maxVal = Math.max(...rawData)
  return rawData.map((v, i) => ({
    x: 40 + (i / (rawData.length - 1)) * (chartWidth - 60),
    y: 20 + (1 - v / maxVal) * 140,
    label: labels[i]
  }))
})

const linePathD = computed(() =>
  chartPoints.value.map((p, i) => `${i === 0 ? 'M' : 'L'}${p.x},${p.y}`).join(' ')
)

const areaPathD = computed(() => {
  const pts = chartPoints.value
  const path = pts.map((p, i) => `${i === 0 ? 'M' : 'L'}${p.x},${p.y}`).join(' ')
  return `${path} L${pts[pts.length-1].x},175 L${pts[0].x},175 Z`
})

const pieLegend = [
  { label: 'Java 方向', pct: 50, color: '#4DA3FF' },
  { label: '前端方向', pct: 25, color: '#7C3AED' },
  { label: '其他', pct: 25, color: '#10B981' },
]

const recentActivities = [
  { id: 1, user: '张明', action: '上传了新简历并完成 AI 分析', time: '2分钟前', type: 'upload', typeLabel: '上传', color: 'linear-gradient(135deg,#4DA3FF,#7C3AED)' },
  { id: 2, user: '李雨晴', action: '使用 AI 简历优化功能', time: '8分钟前', type: 'optimize', typeLabel: '优化', color: 'linear-gradient(135deg,#10B981,#4DA3FF)' },
  { id: 3, user: '王大海', action: '查看了推荐岗位列表', time: '15分钟前', type: 'view', typeLabel: '浏览', color: 'linear-gradient(135deg,#F59E0B,#EF4444)' },
  { id: 4, user: '陈小红', action: '注册成为新用户', time: '23分钟前', type: 'register', typeLabel: '注册', color: 'linear-gradient(135deg,#7C3AED,#4DA3FF)' },
  { id: 5, user: '刘建国', action: '匹配分析：85分，高度匹配', time: '1小时前', type: 'match', typeLabel: '匹配', color: 'linear-gradient(135deg,#10B981,#059669)' },
]

const topJobs = [
  { id: 1, title: 'Java 后端工程师', company: '字节跳动', applicants: 128, avgMatch: 86 },
  { id: 2, title: '高级后端开发', company: '阿里巴巴', applicants: 95, avgMatch: 83 },
  { id: 3, title: '前端开发工程师', company: '腾讯科技', applicants: 87, avgMatch: 79 },
  { id: 4, title: '产品经理', company: '美团', applicants: 74, avgMatch: 75 },
  { id: 5, title: '数据分析师', company: '网易', applicants: 61, avgMatch: 72 },
]

const getScoreClass = (s) => s >= 85 ? 'excellent' : s >= 75 ? 'good' : s >= 65 ? 'medium' : 'low'
</script>

<style lang="scss" scoped>
.dashboard { padding-top: 32px; }

.kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.kpi-card {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.2s ease;

  &:hover { transform: translateY(-2px); box-shadow: $shadow-md; }

  .kpi-icon {
    width: 48px;
    height: 48px;
    border-radius: $radius-md;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;

    .icon-wrap { display: flex; }
  }

  .kpi-info {
    flex: 1;
    min-width: 0;

    .kpi-label {
      display: block;
      font-size: 12px;
      color: $text-muted;
      margin-bottom: 4px;
    }

    .kpi-value {
      display: block;
      font-size: 24px;
      font-weight: 800;
      color: $text-primary;
      margin-bottom: 4px;
    }

    .kpi-trend {
      display: flex;
      align-items: center;
      gap: 3px;
      font-size: 11px;
      font-weight: 600;

      &.up { color: $success; }
      &.down { color: $danger; }
    }
  }
}

.charts-row {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 16px;
  margin-bottom: 20px;
  align-items: start;
}

.chart-card {
  padding: 20px;

  .chart-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;

    h3 {
      font-size: 14px;
      font-weight: 700;
      color: $text-primary;
    }

    .chart-period {
      display: flex;
      gap: 2px;
      background: $bg-base;
      border: 1px solid $border;
      border-radius: $radius-sm;
      padding: 2px;

      button {
        padding: 4px 10px;
        border: none;
        background: none;
        border-radius: 4px;
        font-size: 11px;
        color: $text-secondary;
        cursor: pointer;
        transition: all 0.15s ease;

        &.active {
          background: white;
          color: $primary;
          font-weight: 600;
          box-shadow: $shadow-sm;
        }
      }
    }
  }

  &.small {
    min-width: 260px;
  }
}

.line-chart text { font-family: $font-family; }

.pie-chart-wrap {
  display: flex;
  justify-content: center;
  margin-bottom: 14px;
}

.pie-legend {
  .legend-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;
    font-size: 12px;

    .legend-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      flex-shrink: 0;
    }

    .legend-label { flex: 1; color: $text-secondary; }
    strong { color: $text-primary; }
  }
}

.bottom-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.card-header-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;

  h3 { font-size: 14px; font-weight: 700; color: $text-primary; }

  .view-all {
    font-size: 12px;
    color: $primary;
    text-decoration: none;
    font-weight: 500;
    &:hover { text-decoration: underline; }
  }
}

.activity-card, .top-jobs-card {
  padding: 20px;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 10px;

  .activity-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 13px;
    font-weight: 700;
    flex-shrink: 0;
  }

  .activity-info {
    flex: 1;
    min-width: 0;

    span:first-child { display: block; font-size: 13px; color: $text-secondary; line-height: 1.4; }
    .activity-time { font-size: 11px; color: $text-muted; }
  }

  .activity-badge {
    font-size: 10px;
    font-weight: 700;
    padding: 2px 7px;
    border-radius: $radius-full;
    flex-shrink: 0;

    &.upload { background: rgba($primary, 0.1); color: $primary; }
    &.optimize { background: rgba($success, 0.1); color: $success; }
    &.view { background: rgba($warning, 0.1); color: $warning; }
    &.register { background: rgba($accent, 0.1); color: $accent; }
    &.match { background: rgba($success, 0.1); color: $success; }
  }
}

.job-rank-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.job-rank-item {
  display: flex;
  align-items: center;
  gap: 12px;

  .rank-num {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: 700;
    background: $bg-base;
    color: $text-muted;
    flex-shrink: 0;

    &.top {
      background: linear-gradient(135deg, $primary, $accent);
      color: white;
    }
  }

  .job-rank-info {
    flex: 1;
    strong { display: block; font-size: 13px; color: $text-primary; }
    span { font-size: 11px; color: $text-muted; }
  }

  .rank-stats {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 3px;

    span { font-size: 11px; color: $text-muted; }

    .match-mini {
      font-size: 12px;
      font-weight: 700;
      padding: 2px 7px;
      border-radius: $radius-full;

      &.excellent { background: rgba($success, 0.1); color: $success; }
      &.good { background: rgba($primary, 0.1); color: $primary; }
      &.medium { background: rgba($warning, 0.1); color: $warning; }
    }
  }
}
</style>
