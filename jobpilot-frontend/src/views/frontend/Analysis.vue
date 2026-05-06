<template>
  <div class="analysis-page page-container">

    <!-- Loading overlay -->
    <div v-if="loading" class="state-overlay">
      <div class="state-box">
        <div class="spin-ring"></div>
        <p>AI 分析中，正在等待结果…</p>
      </div>
    </div>

    <!-- Error banner -->
    <div v-if="fetchError" class="error-banner">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
      {{ fetchError }}
      <button @click="fetchError = ''">✕</button>
    </div>

    <div class="page-header">
      <div>
        <h1 class="page-title">AI 分析报告</h1>
        <p class="page-subtitle">{{ pageSubtitle }}</p>
      </div>
      <div class="header-actions">
        <button class="btn-ghost" :disabled="loading" @click="reAnalyze">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="23 4 23 10 17 10"/><polyline points="1 20 1 14 7 14"/>
            <path d="M3.51 9a9 9 0 0114.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0020.49 15"/>
          </svg>
          重新分析
        </button>
        <button class="btn-primary" @click="goOptimize">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
            <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
          </svg>
          优化简历
        </button>
      </div>
    </div>

    <!-- Top Row: Score + Fit + Quick Stats -->
    <div class="top-row">
      <!-- Match Score -->
      <div class="card score-card">
        <div class="score-label">总体匹配度</div>
        <div class="score-ring-wrap">
          <svg viewBox="0 0 160 160" width="160" height="160">
            <circle cx="80" cy="80" r="64" fill="none" stroke="#EAF4FF" stroke-width="12"/>
            <circle cx="80" cy="80" r="64" fill="none"
              stroke="url(#scoreGrad)"
              stroke-width="12"
              stroke-dasharray="402"
              :stroke-dashoffset="402 - (402 * animatedScore / 100)"
              stroke-linecap="round"
              transform="rotate(-90 80 80)"
              style="transition: stroke-dashoffset 1.5s cubic-bezier(0.34,1.56,0.64,1)"/>
            <defs>
              <linearGradient id="scoreGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stop-color="#4DA3FF"/>
                <stop offset="100%" stop-color="#7C3AED"/>
              </linearGradient>
            </defs>
          </svg>
          <div class="score-center">
            <strong class="score-num">{{ animatedScore }}</strong>
            <span>/ 100</span>
          </div>
        </div>
        <div class="score-grade" :class="scoreGrade.class">
          {{ scoreGrade.label }}
        </div>
      </div>

      <!-- Job Fit -->
      <div class="card fit-card">
        <h3 class="card-title">岗位适配度</h3>
        <div class="fit-level" :class="fitData.class">
          <div class="fit-icon">{{ fitData.icon }}</div>
          <div class="fit-text">
            <strong>{{ fitData.label }}</strong>
            <p>{{ fitData.desc }}</p>
          </div>
        </div>
        <div class="fit-breakdown">
          <div v-for="item in fitBreakdown" :key="item.name" class="fit-item">
            <div class="fit-item-header">
              <span>{{ item.name }}</span>
              <span>{{ item.score }}%</span>
            </div>
            <div class="fit-track">
              <div class="fit-fill" :style="{ width: item.score + '%', background: item.color }"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- Quick Stats -->
      <div class="quick-stats">
        <div v-for="stat in quickStats" :key="stat.label" class="stat-card card">
          <div class="stat-icon" :style="{ color: stat.color }">{{ stat.icon }}</div>
          <strong>{{ stat.value }}</strong>
          <span>{{ stat.label }}</span>
        </div>
      </div>
    </div>

    <!-- Middle Row: Radar + Gap Analysis -->
    <div class="mid-row">
      <!-- Skill Radar -->
      <div class="card radar-card">
        <h3 class="card-title">技能维度分析</h3>
        <div class="radar-wrap">
          <svg viewBox="0 0 300 300" width="100%" class="radar-svg">
            <!-- Grid lines -->
            <g v-for="level in [20,40,60,80,100]" :key="level">
              <polygon
                :points="getHexPoints(level * 1.2)"
                fill="none"
                stroke="#E5E7EB"
                stroke-width="1"
              />
            </g>
            <!-- Axes -->
            <line v-for="(axis, i) in radarAxes" :key="i"
              :x1="150" :y1="150"
              :x2="axis.x2" :y2="axis.y2"
              stroke="#E5E7EB" stroke-width="1"/>
            <!-- Data polygon -->
            <polygon
              :points="radarDataPoints"
              fill="rgba(77,163,255,0.15)"
              stroke="#4DA3FF"
              stroke-width="2"
              stroke-linejoin="round"
            />
            <!-- Data dots -->
            <circle v-for="(point, i) in radarDots" :key="i"
              :cx="point.x" :cy="point.y"
              r="4" fill="#4DA3FF" stroke="white" stroke-width="2"/>
            <!-- Labels -->
            <text v-for="(axis, i) in radarAxes" :key="'l'+i"
              :x="axis.lx" :y="axis.ly"
              text-anchor="middle"
              dominant-baseline="middle"
              font-size="11"
              fill="#6B7280"
              font-weight="500">{{ axis.label }}</text>
          </svg>
        </div>
        <div class="radar-legend">
          <div v-for="(skill, i) in radarSkills" :key="skill.name" class="legend-item">
            <span class="legend-dot" style="background: #4DA3FF"></span>
            <span>{{ skill.name }}</span>
            <strong>{{ skill.score ?? skill.value }}%</strong>
          </div>
        </div>
      </div>

      <!-- Gap Analysis -->
      <div class="card gap-card">
        <h3 class="card-title">技能差距分析</h3>

        <div class="gap-section">
          <div class="gap-section-title success">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            已具备技能
          </div>
          <div class="skill-tags">
            <span v-for="s in matchedSkills" :key="s" class="tag tag-green">{{ s }}</span>
          </div>
        </div>

        <div class="gap-section">
          <div class="gap-section-title warning">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            部分匹配
          </div>
          <div class="skill-tags">
            <span v-for="s in partialSkills" :key="s" class="tag tag-yellow">{{ s }}</span>
          </div>
        </div>

        <div class="gap-section">
          <div class="gap-section-title danger">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
            缺少技能
          </div>
          <div class="skill-tags">
            <span v-for="s in missingSkills" :key="s" class="tag tag-red">{{ s }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- AI Suggestions -->
    <div class="card suggestions-card">
      <div class="suggestions-header">
        <h3 class="card-title">
          <span class="ai-sparkle">✨</span>
          AI 优化建议
        </h3>
        <span class="suggestions-count">{{ suggestions.length }} 条建议</span>
      </div>
      <div class="suggestions-grid">
        <div v-for="s in suggestions" :key="s.title" class="suggestion-item">
          <div class="suggestion-icon" :style="{ background: s.bg }">{{ s.icon }}</div>
          <div class="suggestion-content">
            <div class="suggestion-header">
              <strong>{{ s.title }}</strong>
              <span class="impact-badge" :class="s.impact">+{{ s.gain }}%</span>
            </div>
            <p>{{ s.desc }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Action Buttons -->
    <div class="action-row">
      <router-link to="/optimize" class="action-btn primary">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
          <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
        </svg>
        AI 优化简历
      </router-link>
      <router-link to="/jobs" class="action-btn secondary">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 21V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v16"/>
        </svg>
        查看推荐岗位
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { analyzeResume } from '@/api/frontend/resume'

const route = useRoute()
const router = useRouter()

// ── State ──────────────────────────────────────────────────────────────────
const loading = ref(false)
const fetchError = ref('')
const resumeId = ref(null)
const jobDescription = ref('')
const analysisData = ref(null)   // full normalized response from backend
const targetScore = ref(0)
const animatedScore = ref(0)
const pageSubtitle = ref('AI 简历分析报告')

// ── Boot: load from sessionStorage, fall back to API ──────────────────────
onMounted(async () => {
  const cached = sessionStorage.getItem('analysisContext')
  if (cached) {
    try {
      const ctx = JSON.parse(cached)
      applyAnalysisData(ctx.result, ctx.resumeId, ctx.jobDescription)
      pageSubtitle.value = ctx.jobDescription
        ? `目标岗位：${ctx.jobDescription}`
        : `简历 ID：${ctx.resumeId}`
      return
    } catch (_) { /* fall through */ }
  }

  const idParam = route.query.resumeId
  if (idParam) {
    loading.value = true
    try {
      const res = await analyzeResume(Number(idParam), route.query.jd || '')
      applyAnalysisData(res, Number(idParam), route.query.jd || '')
      pageSubtitle.value = route.query.jd ? `目标岗位：${route.query.jd}` : `简历 ID：${idParam}`
    } catch (e) {
      fetchError.value = e.message || '分析数据加载失败'
    } finally {
      loading.value = false
    }
  }
})

// Normalize both new schema {score,level,summary,dimensions,radar,skills,suggestions}
// and legacy schema {score,advantages,disadvantages,suggestions[]}
const normalizeAnalysisData = (raw = {}) => {
  const d = raw.data ?? raw
  return {
    score:      Number(d.score) || 0,
    level:      d.level   || '',
    summary:    d.summary || '',
    dimensions: Array.isArray(d.dimensions) ? d.dimensions : [],
    radar:      Array.isArray(d.radar)      ? d.radar      : [],
    skills: {
      matched: Array.isArray(d.skills?.matched)  ? d.skills.matched
               : Array.isArray(d.advantages)      ? d.advantages    : [],
      partial: Array.isArray(d.skills?.partial)  ? d.skills.partial : [],
      missing: Array.isArray(d.skills?.missing)  ? d.skills.missing
               : Array.isArray(d.disadvantages)  ? d.disadvantages  : [],
    },
    suggestions: Array.isArray(d.suggestions) ? d.suggestions : [],
  }
}

const applyAnalysisData = (result, id, jd) => {
  const n = normalizeAnalysisData(result)
  analysisData.value = n
  resumeId.value = id
  jobDescription.value = jd
  targetScore.value = n.score
  runScoreAnimation(n.score)
}

// ── Score animation ────────────────────────────────────────────────────────
const runScoreAnimation = (target) => {
  animatedScore.value = 0
  let cur = 0
  const step = () => {
    cur += 2
    animatedScore.value = Math.min(cur, target)
    if (cur < target) requestAnimationFrame(step)
  }
  requestAnimationFrame(step)
}

// ── Computed from real data ────────────────────────────────────────────────
const scoreGrade = computed(() => {
  const level = analysisData.value?.level
  if (level) {
    const map = { '高度匹配': 'excellent', '良好匹配': 'good', '中等匹配': 'medium', '匹配较低': 'poor', '匹配较弱': 'poor' }
    return { label: level, class: map[level] ?? 'medium' }
  }
  const s = animatedScore.value
  if (s >= 85) return { label: '高度匹配', class: 'excellent' }
  if (s >= 70) return { label: '良好匹配', class: 'good' }
  if (s >= 55) return { label: '中等匹配', class: 'medium' }
  return { label: '匹配较低', class: 'poor' }
})

const fitData = computed(() => {
  const s = targetScore.value
  const level   = analysisData.value?.level   || ''
  const summary = analysisData.value?.summary || ''
  if (s >= 80 || level.includes('高度'))
    return { icon: '🚀', label: level || '强匹配',   desc: summary || '你的背景与该岗位高度吻合，建议立即投递', class: 'strong' }
  if (s >= 60 || level.includes('良好') || level.includes('中等'))
    return { icon: '✅', label: level || '良好匹配', desc: summary || '基本符合岗位要求，可以尝试投递',         class: 'medium' }
  return       { icon: '⚠️', label: level || '匹配较弱', desc: summary || '建议针对岗位需求进行简历优化后再投递', class: 'weak'   }
})

const DIM_COLORS = [
  'linear-gradient(90deg,#4DA3FF,#7C3AED)',
  'linear-gradient(90deg,#10B981,#4DA3FF)',
  'linear-gradient(90deg,#7C3AED,#4DA3FF)',
  'linear-gradient(90deg,#F59E0B,#EF4444)',
  'linear-gradient(90deg,#EF4444,#F59E0B)',
  'linear-gradient(90deg,#4DA3FF,#10B981)',
]

const fitBreakdown = computed(() => {
  const dims = analysisData.value?.dimensions
  if (dims?.length) {
    return dims.map((d, i) => ({ name: d.name, score: d.score, color: DIM_COLORS[i % DIM_COLORS.length] }))
  }
  const s = targetScore.value
  return [
    { name: '技能匹配', score: Math.min(100, s + 3), color: DIM_COLORS[0] },
    { name: '经验匹配', score: Math.max(0, s - 5),   color: DIM_COLORS[1] },
    { name: '学历匹配', score: Math.min(100, s + 8), color: DIM_COLORS[2] },
    { name: '项目匹配', score: Math.max(0, s - 10),  color: DIM_COLORS[3] },
  ]
})

const matchedSkills = computed(() => analysisData.value?.skills?.matched ?? analysisData.value?.advantages ?? [])
const partialSkills  = computed(() => analysisData.value?.skills?.partial ?? [])
const missingSkills  = computed(() => analysisData.value?.skills?.missing ?? analysisData.value?.disadvantages ?? [])

// Icon/color lookup by suggestion type from backend; fallback cycles through defaults
const SUGGESTION_TYPE_MAP = {
  '技能补强':   { icon: '💪', bg: 'rgba(124,58,237,0.1)',  impact: 'high',   gain: 12 },
  '量化成果':   { icon: '📊', bg: 'rgba(77,163,255,0.1)',  impact: 'high',   gain: 15 },
  '关键词优化': { icon: '🔑', bg: 'rgba(239,68,68,0.1)',   impact: 'medium', gain: 10 },
  '经验描述':   { icon: '✍️', bg: 'rgba(16,185,129,0.1)',  impact: 'medium', gain: 8  },
  '项目亮点':   { icon: '🎯', bg: 'rgba(245,158,11,0.1)',  impact: 'high',   gain: 11 },
  '证书认证':   { icon: '🏆', bg: 'rgba(59,130,246,0.1)',  impact: 'low',    gain: 6  },
  '格式优化':   { icon: '📋', bg: 'rgba(16,185,129,0.1)',  impact: 'low',    gain: 5  },
  '语言表达':   { icon: '🗣️', bg: 'rgba(245,158,11,0.1)',  impact: 'medium', gain: 7  },
}
const SUGGESTION_DEFAULT = [
  { icon: '📊', bg: 'rgba(77,163,255,0.1)',  impact: 'high',   gain: 12 },
  { icon: '💪', bg: 'rgba(124,58,237,0.1)',  impact: 'medium', gain: 8  },
  { icon: '🎯', bg: 'rgba(245,158,11,0.1)',  impact: 'high',   gain: 15 },
  { icon: '✍️', bg: 'rgba(16,185,129,0.1)',  impact: 'low',    gain: 6  },
  { icon: '🔑', bg: 'rgba(239,68,68,0.1)',   impact: 'medium', gain: 10 },
  { icon: '📈', bg: 'rgba(59,130,246,0.1)',  impact: 'high',   gain: 11 },
]

const suggestions = computed(() => {
  const raw = analysisData.value?.suggestions ?? []
  return raw.map((item, i) => {
    const isObj = typeof item === 'object' && item !== null
    const type  = isObj ? (item.type    || '') : ''
    const text  = isObj ? (item.content || '') : (() => {
      const t = String(item)
      const ci = t.search(/[：:。.，,]/)
      return ci > 0 ? t.slice(ci + 1).trim() : t
    })()
    const title = type || ((() => {
      const t = String(isObj ? item.content || '' : item)
      const ci = t.search(/[：:。.，,]/)
      return ci > 0 ? t.slice(0, ci).trim().slice(0, 20) : `建议 ${i + 1}`
    })())
    const meta = SUGGESTION_TYPE_MAP[type] || SUGGESTION_DEFAULT[i % SUGGESTION_DEFAULT.length]
    return { ...meta, title: title.slice(0, 20), desc: text }
  })
})

const quickStats = computed(() => [
  { icon: '🎯', label: '技能覆盖', value: `${matchedSkills.value.length} 项`, color: '#4DA3FF' },
  { icon: '⚡', label: '优化建议', value: `${suggestions.value.length} 条`,  color: '#7C3AED' },
  { icon: '📋', label: '待提升项', value: `${missingSkills.value.length} 项`, color: '#EF4444' },
  { icon: '📈', label: '综合得分', value: `${targetScore.value} 分`,          color: '#F59E0B' },
])

// ── Radar: API-driven; generic axes as fallback when backend omits radar ───
const GENERIC_RADAR = [
  { name: '专业技能', score: 75 },
  { name: '项目经验', score: 70 },
  { name: '学历背景', score: 80 },
  { name: '综合能力', score: 72 },
  { name: '岗位适配', score: 68 },
  { name: '发展潜力', score: 65 },
]
const radarSkills = computed(() => {
  const r = analysisData.value?.radar ?? []
  return r.length >= 3 ? r : GENERIC_RADAR
})

const getAngle = (i, total) => (i / total) * Math.PI * 2 - Math.PI / 2
const cx = 150, cy = 150, maxR = 120

const radarAxes = computed(() =>
  radarSkills.value.map((s, i) => {
    const angle = getAngle(i, radarSkills.value.length)
    return {
      x2: cx + maxR * Math.cos(angle),
      y2: cy + maxR * Math.sin(angle),
      lx: cx + (maxR + 20) * Math.cos(angle),
      ly: cy + (maxR + 20) * Math.sin(angle),
      label: s.name
    }
  })
)

const getHexPoints = (r) =>
  radarSkills.value.map((_, i) => {
    const angle = getAngle(i, radarSkills.value.length)
    return `${cx + r * Math.cos(angle)},${cy + r * Math.sin(angle)}`
  }).join(' ')

const radarDataPoints = computed(() =>
  radarSkills.value.map((s, i) => {
    const angle = getAngle(i, radarSkills.value.length)
    const r = ((s.score ?? s.value ?? 0) / 100) * maxR
    return `${cx + r * Math.cos(angle)},${cy + r * Math.sin(angle)}`
  }).join(' ')
)

const radarDots = computed(() =>
  radarSkills.value.map((s, i) => {
    const angle = getAngle(i, radarSkills.value.length)
    const r = ((s.score ?? s.value ?? 0) / 100) * maxR
    return { x: cx + r * Math.cos(angle), y: cy + r * Math.sin(angle) }
  })
)

// ── Actions ────────────────────────────────────────────────────────────────
const reAnalyze = async () => {
  if (!resumeId.value) return
  loading.value = true
  fetchError.value = ''
  try {
    const res = await analyzeResume(resumeId.value, jobDescription.value)
    applyAnalysisData(res, resumeId.value, jobDescription.value)
    sessionStorage.setItem('analysisContext', JSON.stringify({
      resumeId: resumeId.value,
      jobDescription: jobDescription.value,
      result: analysisData.value,
      createdAt: new Date().toISOString()
    }))
  } catch (e) {
    fetchError.value = e.message || '重新分析失败'
  } finally {
    loading.value = false
  }
}

const goOptimize = () => router.push({ path: '/optimize', query: { resumeId: resumeId.value } })
</script>

<style lang="scss" scoped>
.analysis-page {
  padding-top: 32px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.top-row {
  display: grid;
  grid-template-columns: auto auto 1fr;
  gap: 20px;
  margin-bottom: 20px;
  align-items: start;
}

.score-card {
  padding: 28px 32px;
  text-align: center;
  min-width: 200px;

  .score-label {
    font-size: 13px;
    color: $text-secondary;
    font-weight: 600;
    margin-bottom: 16px;
  }

  .score-ring-wrap {
    position: relative;
    display: inline-block;
    margin-bottom: 14px;

    .score-center {
      position: absolute;
      inset: 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .score-num {
        font-size: 36px;
        font-weight: 900;
        background: linear-gradient(135deg, $primary, $accent);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        line-height: 1;
      }

      span { font-size: 12px; color: $text-muted; }
    }
  }

  .score-grade {
    display: inline-block;
    padding: 4px 14px;
    border-radius: $radius-full;
    font-size: 13px;
    font-weight: 700;

    &.excellent { background: rgba($success, 0.12); color: $success; }
    &.good { background: rgba($primary, 0.12); color: $primary; }
    &.medium { background: rgba($warning, 0.12); color: $warning; }
    &.poor { background: rgba($danger, 0.12); color: $danger; }
  }
}

.fit-card {
  padding: 24px;
  min-width: 280px;

  .fit-level {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 14px;
    border-radius: $radius-md;
    margin-bottom: 16px;

    &.strong { background: rgba($success, 0.08); border: 1px solid rgba($success, 0.2); }
    &.medium { background: rgba($warning, 0.08); border: 1px solid rgba($warning, 0.2); }
    &.weak { background: rgba($danger, 0.08); border: 1px solid rgba($danger, 0.2); }

    .fit-icon { font-size: 24px; }

    .fit-text {
      strong { display: block; font-size: 14px; color: $text-primary; margin-bottom: 2px; }
      p { font-size: 12px; color: $text-secondary; line-height: 1.5; }
    }
  }

  .fit-item {
    margin-bottom: 10px;

    .fit-item-header {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      color: $text-secondary;
      margin-bottom: 4px;
    }

    .fit-track {
      height: 6px;
      background: $bg-base;
      border-radius: $radius-full;
      overflow: hidden;
    }

    .fit-fill {
      height: 100%;
      border-radius: $radius-full;
      transition: width 1s ease;
    }
  }
}

.quick-stats {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.stat-card {
  padding: 18px;
  text-align: center;
  transition: all 0.2s ease;

  &:hover { transform: translateY(-2px); box-shadow: $shadow-md; }

  .stat-icon { font-size: 22px; margin-bottom: 8px; }
  strong { display: block; font-size: 20px; font-weight: 800; color: $text-primary; margin-bottom: 2px; }
  span { font-size: 12px; color: $text-secondary; }
}

.mid-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.card-title {
  font-size: 15px;
  font-weight: 700;
  color: $text-primary;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 6px;

  .ai-sparkle { font-size: 18px; }
}

.radar-card {
  padding: 24px;

  .radar-wrap { margin-bottom: 16px; }

  .radar-legend {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 6px;

    .legend-item {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      color: $text-secondary;

      .legend-dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        flex-shrink: 0;
      }

      strong { margin-left: auto; color: $text-primary; }
    }
  }
}

.radar-svg text { font-family: $font-family; }

.gap-card {
  padding: 24px;

  .gap-section {
    margin-bottom: 16px;

    .gap-section-title {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      font-weight: 700;
      margin-bottom: 8px;
      text-transform: uppercase;
      letter-spacing: 0.05em;

      &.success { color: $success; }
      &.warning { color: $warning; }
      &.danger { color: $danger; }
    }

    .skill-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
    }
  }
}

.suggestions-card {
  padding: 24px;
  margin-bottom: 20px;

  .suggestions-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;

    .suggestions-count {
      font-size: 12px;
      color: $text-muted;
      background: $bg-base;
      padding: 3px 10px;
      border-radius: $radius-full;
    }
  }
}

.suggestions-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}

.suggestion-item {
  display: flex;
  gap: 14px;
  padding: 16px;
  background: $bg-base;
  border-radius: $radius-md;
  border: 1px solid $border-light;
  transition: all 0.2s ease;

  &:hover {
    border-color: rgba($primary, 0.2);
    background: rgba($primary, 0.02);
  }

  .suggestion-icon {
    width: 40px;
    height: 40px;
    border-radius: $radius-md;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    flex-shrink: 0;
  }

  .suggestion-content {
    flex: 1;

    .suggestion-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 4px;

      strong { font-size: 13px; color: $text-primary; }
    }

    p { font-size: 12px; color: $text-secondary; line-height: 1.6; }
  }

  .impact-badge {
    font-size: 11px;
    font-weight: 700;
    padding: 2px 7px;
    border-radius: $radius-full;

    &.high { background: rgba($success, 0.12); color: $success; }
    &.medium { background: rgba($primary, 0.12); color: $primary; }
    &.low { background: rgba($warning, 0.12); color: $warning; }
  }
}

.action-row {
  display: flex;
  gap: 14px;
  justify-content: center;
  padding: 24px 0;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 13px 28px;
  border-radius: $radius-md;
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
  transition: all 0.25s ease;

  &.primary {
    background: linear-gradient(135deg, $primary, $primary-dark);
    color: white;
    box-shadow: 0 4px 14px rgba($primary, 0.35);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 20px rgba($primary, 0.45);
    }
  }

  &.secondary {
    background: white;
    color: $text-primary;
    border: 1px solid $border;
    box-shadow: $shadow-sm;

    &:hover {
      border-color: $primary;
      color: $primary;
      transform: translateY(-1px);
    }
  }
}
</style>
