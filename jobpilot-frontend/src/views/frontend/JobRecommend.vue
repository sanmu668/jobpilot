<template>
  <div class="jobs-page page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">推荐岗位</h1>
        <p class="page-subtitle">基于 AI 分析，为你精选 <strong>{{ filteredJobs.length }}</strong> 个最匹配岗位</p>
      </div>
      <div class="view-btns">
        <button :class="{ active: viewMode === 'grid' }" @click="viewMode = 'grid'" title="网格视图">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/>
            <rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/>
          </svg>
        </button>
        <button :class="{ active: viewMode === 'list' }" @click="viewMode = 'list'" title="列表视图">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/>
            <line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/>
            <line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- Filters -->
    <div class="filters-bar card">
      <div class="filter-group">
        <label>城市</label>
        <select v-model="filters.city" class="filter-select">
          <option value="">全部城市</option>
          <option v-for="c in cities" :key="c" :value="c">{{ c }}</option>
        </select>
      </div>
      <div class="filter-group">
        <label>薪资范围</label>
        <select v-model="filters.salary" class="filter-select">
          <option value="">不限</option>
          <option value="10-20k">10-20K</option>
          <option value="20-35k">20-35K</option>
          <option value="35k+">35K+</option>
        </select>
      </div>
      <div class="filter-group">
        <label>工作性质</label>
        <select v-model="filters.type" class="filter-select">
          <option value="">全部</option>
          <option value="full">全职</option>
          <option value="remote">远程</option>
        </select>
      </div>
      <div class="filter-group search-group">
        <label>搜索</label>
        <div class="search-input-wrap">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
          </svg>
          <input v-model="filters.keyword" type="text" placeholder="职位 / 公司 / 技能" />
        </div>
      </div>
      <button class="filter-reset" @click="resetFilters">重置</button>
    </div>

    <!-- Match Score Filter Tabs -->
    <div class="score-tabs">
      <button v-for="tab in scoreTabs" :key="tab.value"
        :class="{ active: activeScoreTab === tab.value }"
        @click="activeScoreTab = tab.value">
        {{ tab.label }}
        <span class="tab-count">{{ tab.count }}</span>
      </button>
    </div>

    <!-- Jobs Grid / List -->
    <div class="jobs-container" :class="viewMode">
      <div v-for="job in filteredJobs" :key="job.id"
        class="job-card card"
        :class="{ featured: job.featured }">

        <div class="job-card-top">
          <div class="company-logo" :style="{ background: job.logoColor }">
            {{ job.company.charAt(0) }}
          </div>
          <div class="job-main">
            <h3 class="job-title">{{ job.title }}</h3>
            <div class="company-info">
              <span class="company-name">{{ job.company }}</span>
              <span class="divider">·</span>
              <span class="job-location">{{ job.city }}</span>
              <span class="divider">·</span>
              <span class="job-type">{{ job.type }}</span>
            </div>
          </div>
          <div class="match-score-badge" :class="getScoreClass(job.matchScore)">
            {{ job.matchScore }}%
            <span>匹配</span>
          </div>
        </div>

        <div class="job-salary">{{ job.salary }}</div>

        <div class="job-tags">
          <span v-for="tag in job.tags.slice(0, 5)" :key="tag" class="tag tag-blue">{{ tag }}</span>
        </div>

        <div class="job-footer">
          <span class="job-posted">{{ job.posted }}</span>
          <div class="job-actions">
            <button class="save-btn" :class="{ saved: job.saved }" @click="toggleSave(job)">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z"/>
              </svg>
            </button>
            <button class="apply-btn" @click="applyJob(job)">
              {{ job.applied ? '已投递' : '立即投递' }}
            </button>
          </div>
        </div>

        <div v-if="job.featured" class="featured-badge">🔥 热招</div>
      </div>
    </div>

    <div v-if="filteredJobs.length === 0" class="empty-state">
      <div class="empty-icon">🔍</div>
      <h3>暂无匹配岗位</h3>
      <p>尝试调整筛选条件或上传新的简历</p>
      <router-link to="/upload" class="btn-primary">重新上传简历</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'

const viewMode = ref('grid')
const activeScoreTab = ref('all')
const analysisContext = ref(null)

const filters = reactive({
  city: '',
  salary: '',
  type: '',
  keyword: ''
})

const cities = ['北京', '上海', '深圳', '杭州', '成都', '武汉', '远程']

const jobs = ref([
  { id: 1, title: 'Java 后端工程师', company: '字节跳动', city: '北京', type: '全职', salary: '25-40K · 16薪', matchScore: 92, tags: ['Java', 'Spring Cloud', 'Redis', 'Kafka', 'MySQL'], posted: '3天前', logoColor: 'linear-gradient(135deg,#4DA3FF,#2563EB)', featured: true, saved: false, applied: false },
  { id: 2, title: '高级后端开发工程师', company: '阿里巴巴', city: '杭州', type: '全职', salary: '30-50K · 14薪', matchScore: 88, tags: ['Java', 'Dubbo', 'MySQL', 'Redis', 'Docker'], posted: '1天前', logoColor: 'linear-gradient(135deg,#F59E0B,#D97706)', featured: true, saved: false, applied: false },
  { id: 3, title: '后端开发工程师', company: '腾讯科技', city: '深圳', type: '全职', salary: '25-45K · 16薪', matchScore: 85, tags: ['Java', 'Spring Boot', 'MySQL', 'Redis', 'Linux'], posted: '5天前', logoColor: 'linear-gradient(135deg,#7C3AED,#6D28D9)', featured: false, saved: false, applied: false },
  { id: 4, title: 'Java 开发工程师', company: '美团', city: '北京', type: '全职', salary: '20-35K · 14薪', matchScore: 82, tags: ['Java', 'Spring Boot', 'Redis', 'Elasticsearch'], posted: '2天前', logoColor: 'linear-gradient(135deg,#10B981,#059669)', featured: false, saved: false, applied: false },
  { id: 5, title: '远程 Java 后端', company: '快手', city: '远程', type: '远程', salary: '18-30K', matchScore: 79, tags: ['Java', 'Spring Boot', 'Docker', 'K8s'], posted: '1周前', logoColor: 'linear-gradient(135deg,#EF4444,#DC2626)', featured: false, saved: false, applied: false },
  { id: 6, title: '服务端开发工程师', company: '滴滴出行', city: '北京', type: '全职', salary: '22-38K · 15薪', matchScore: 77, tags: ['Java', 'Golang', 'gRPC', 'MySQL'], posted: '4天前', logoColor: 'linear-gradient(135deg,#3B82F6,#1D4ED8)', featured: false, saved: false, applied: false },
  { id: 7, title: 'Java 工程师', company: '网易', city: '杭州', type: '全职', salary: '20-35K · 13薪', matchScore: 74, tags: ['Java', 'Spring', 'MySQL', 'Redis'], posted: '6天前', logoColor: 'linear-gradient(135deg,#F97316,#EA580C)', featured: false, saved: false, applied: false },
  { id: 8, title: '后端研发工程师', company: 'OPPO', city: '深圳', type: '全职', salary: '18-30K', matchScore: 71, tags: ['Java', 'Spring Boot', 'MySQL', 'RabbitMQ'], posted: '1周前', logoColor: 'linear-gradient(135deg,#6366F1,#4F46E5)', featured: false, saved: false, applied: false },
])

const normalizeWords = (text = '') =>
  (text.match(/[A-Za-z0-9+#.\-]{2,}|[\u4e00-\u9fa5]{2,}/g) || [])
    .map(item => item.toLowerCase())

const getAnalysisKeywords = () => {
  const context = analysisContext.value
  if (!context?.result) return []
  const result = context.result
  const sourceText = [
    context.jobDescription || '',
    ...(result.advantages || []),
    ...(result.disadvantages || []),
    ...(result.suggestions || [])
  ].join(' ')
  return Array.from(new Set(normalizeWords(sourceText))).slice(0, 30)
}

const scoredJobs = computed(() => {
  const keywords = getAnalysisKeywords()
  if (!keywords.length) return [...jobs.value]

  return jobs.value.map(job => {
    const haystack = `${job.title} ${job.company} ${job.tags.join(' ')}`.toLowerCase()
    const hitCount = keywords.reduce((count, keyword) => {
      return count + (haystack.includes(keyword) ? 1 : 0)
    }, 0)
    const bonus = Math.min(18, hitCount * 3)
    return {
      ...job,
      matchScore: Math.min(99, job.matchScore + bonus)
    }
  })
})

const scoreTabs = computed(() => [
  { label: '全部', value: 'all', count: scoredJobs.value.length },
  { label: '90+ 极匹配', value: '90', count: scoredJobs.value.filter(j => j.matchScore >= 90).length },
  { label: '80+ 高匹配', value: '80', count: scoredJobs.value.filter(j => j.matchScore >= 80 && j.matchScore < 90).length },
  { label: '70+ 匹配', value: '70', count: scoredJobs.value.filter(j => j.matchScore >= 70 && j.matchScore < 80).length },
])

const filteredJobs = computed(() => {
  let result = [...scoredJobs.value]

  if (activeScoreTab.value === '90') result = result.filter(j => j.matchScore >= 90)
  else if (activeScoreTab.value === '80') result = result.filter(j => j.matchScore >= 80 && j.matchScore < 90)
  else if (activeScoreTab.value === '70') result = result.filter(j => j.matchScore >= 70 && j.matchScore < 80)

  if (filters.city) result = result.filter(j => j.city === filters.city)
  if (filters.type) result = result.filter(j => filters.type === 'remote' ? j.type === '远程' : j.type === '全职')
  if (filters.keyword) {
    const kw = filters.keyword.toLowerCase()
    result = result.filter(j =>
      j.title.toLowerCase().includes(kw) ||
      j.company.toLowerCase().includes(kw) ||
      j.tags.some(t => t.toLowerCase().includes(kw))
    )
  }

  return result.sort((a, b) => b.matchScore - a.matchScore)
})

const getScoreClass = (score) => {
  if (score >= 90) return 'excellent'
  if (score >= 80) return 'good'
  if (score >= 70) return 'medium'
  return 'low'
}

const toggleSave = (job) => { job.saved = !job.saved }
const applyJob = (job) => { job.applied = true }
const resetFilters = () => { Object.assign(filters, { city: '', salary: '', type: '', keyword: '' }) }

onMounted(() => {
  const raw = sessionStorage.getItem('analysisContext')
  if (!raw) return
  try {
    analysisContext.value = JSON.parse(raw)
  } catch (error) {
    analysisContext.value = null
  }
})
</script>

<style lang="scss" scoped>
.jobs-page { padding-top: 32px; }

.view-btns {
  display: flex;
  gap: 4px;

  button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 34px;
    height: 34px;
    background: white;
    border: 1px solid $border;
    border-radius: $radius-sm;
    color: $text-secondary;
    cursor: pointer;
    transition: all 0.2s ease;

    &.active, &:hover {
      background: rgba($primary, 0.08);
      border-color: rgba($primary, 0.3);
      color: $primary;
    }
  }
}

.filters-bar {
  display: flex;
  gap: 16px;
  align-items: flex-end;
  padding: 16px 20px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 4px;

  label {
    font-size: 11px;
    font-weight: 600;
    color: $text-muted;
    text-transform: uppercase;
    letter-spacing: 0.05em;
  }
}

.filter-select {
  padding: 7px 12px;
  background: $bg-base;
  border: 1px solid $border;
  border-radius: $radius-sm;
  font-size: 13px;
  color: $text-primary;
  outline: none;
  cursor: pointer;
  transition: border 0.2s ease;
  min-width: 120px;

  &:focus { border-color: $primary; }
}

.search-group { flex: 1; min-width: 200px; }

.search-input-wrap {
  position: relative;

  svg {
    position: absolute;
    left: 10px;
    top: 50%;
    transform: translateY(-50%);
    color: $text-muted;
  }

  input {
    width: 100%;
    padding: 7px 12px 7px 30px;
    background: $bg-base;
    border: 1px solid $border;
    border-radius: $radius-sm;
    font-size: 13px;
    color: $text-primary;
    outline: none;
    transition: all 0.2s ease;

    &:focus { border-color: $primary; background: white; }
    &::placeholder { color: $text-muted; }
  }
}

.filter-reset {
  padding: 7px 14px;
  background: none;
  border: 1px solid $border;
  border-radius: $radius-sm;
  font-size: 12px;
  color: $text-secondary;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover { border-color: $danger; color: $danger; }
}

.score-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  flex-wrap: wrap;

  button {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 7px 16px;
    background: white;
    border: 1px solid $border;
    border-radius: $radius-full;
    font-size: 13px;
    font-weight: 500;
    color: $text-secondary;
    cursor: pointer;
    transition: all 0.2s ease;

    &.active {
      background: rgba($primary, 0.1);
      border-color: rgba($primary, 0.3);
      color: $primary;
      font-weight: 600;
    }

    .tab-count {
      background: $bg-base;
      padding: 1px 6px;
      border-radius: $radius-full;
      font-size: 11px;
    }
  }
}

.jobs-container {
  &.grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 16px;
  }

  &.list {
    display: flex;
    flex-direction: column;
    gap: 10px;

    .job-card {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 16px 20px;

      .job-card-top {
        flex: 1;
        margin-bottom: 0;
      }

      .job-salary, .job-tags, .job-footer {
        flex-shrink: 0;
      }

      .job-tags { display: flex; gap: 4px; }
    }
  }
}

.job-card {
  padding: 20px;
  position: relative;
  transition: all 0.3s ease;
  overflow: hidden;

  &:hover {
    transform: translateY(-3px);
    box-shadow: $shadow-lg;
  }

  &.featured {
    border-color: rgba($primary, 0.25);
    background: linear-gradient(145deg, white, rgba($primary, 0.02));
  }
}

.job-card-top {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.company-logo {
  width: 44px;
  height: 44px;
  border-radius: $radius-md;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: 800;
  flex-shrink: 0;
}

.job-main {
  flex: 1;
  min-width: 0;

  .job-title {
    font-size: 15px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 4px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .company-info {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: $text-muted;
    flex-wrap: wrap;

    .divider { opacity: 0.4; }
    .company-name { color: $text-secondary; font-weight: 500; }
  }
}

.match-score-badge {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 6px 10px;
  border-radius: $radius-md;
  font-size: 18px;
  font-weight: 900;
  line-height: 1.2;

  span { font-size: 10px; font-weight: 500; opacity: 0.8; }

  &.excellent { background: rgba($success, 0.1); color: $success; }
  &.good { background: rgba($primary, 0.1); color: $primary; }
  &.medium { background: rgba($warning, 0.1); color: $warning; }
  &.low { background: rgba($danger, 0.1); color: $danger; }
}

.job-salary {
  font-size: 15px;
  font-weight: 700;
  color: $success;
  margin-bottom: 10px;
}

.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-bottom: 14px;
}

.job-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .job-posted {
    font-size: 12px;
    color: $text-muted;
  }

  .job-actions {
    display: flex;
    gap: 8px;
  }
}

.save-btn {
  width: 30px;
  height: 30px;
  background: $bg-base;
  border: 1px solid $border;
  border-radius: $radius-sm;
  display: flex;
  align-items: center;
  justify-content: center;
  color: $text-muted;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover, &.saved {
    border-color: rgba(239, 68, 68, 0.3);
    color: $danger;
    background: rgba($danger, 0.06);
  }

  &.saved { fill: $danger; svg { fill: $danger; } }
}

.apply-btn {
  padding: 6px 16px;
  background: linear-gradient(135deg, $primary, $primary-dark);
  color: white;
  border: none;
  border-radius: $radius-sm;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 2px 6px rgba($primary, 0.25);

  &:hover {
    box-shadow: 0 4px 12px rgba($primary, 0.4);
    transform: translateY(-1px);
  }
}

.featured-badge {
  position: absolute;
  top: 12px;
  right: 56px;
  font-size: 11px;
  font-weight: 700;
  color: $warning;
  background: rgba($warning, 0.1);
  padding: 2px 8px;
  border-radius: $radius-full;
}

.empty-state {
  text-align: center;
  padding: 80px 24px;

  .empty-icon { font-size: 60px; margin-bottom: 16px; }
  h3 { font-size: 18px; font-weight: 700; color: $text-primary; margin-bottom: 8px; }
  p { font-size: 14px; color: $text-secondary; margin-bottom: 24px; }

  a {
    display: inline-flex;
    align-items: center;
    padding: 10px 24px;
    background: linear-gradient(135deg, $primary, $primary-dark);
    color: white;
    border-radius: $radius-md;
    font-size: 14px;
    font-weight: 600;
    text-decoration: none;
  }
}
</style>
