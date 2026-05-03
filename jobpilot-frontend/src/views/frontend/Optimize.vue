<template>
  <div class="optimize-page page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">AI 简历优化</h1>
        <p class="page-subtitle">AI 智能重写，让每一句话都更有力量</p>
      </div>
      <div class="header-actions">
        <div class="view-toggle">
          <button :class="{ active: viewMode === 'split' }" @click="viewMode = 'split'">对比视图</button>
          <button :class="{ active: viewMode === 'diff' }" @click="viewMode = 'diff'">差异视图</button>
        </div>
        <button class="btn-primary apply-btn" @click="applyChanges" :class="{ applied }">
          <svg v-if="!applied" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="20 6 9 17 4 12"/>
          </svg>
          <span>{{ applied ? '✓ 已应用' : '应用修改' }}</span>
        </button>
      </div>
    </div>

    <!-- Improvement Summary -->
    <div class="improvement-bar">
      <div v-for="item in improvements" :key="item.label" class="improve-item">
        <div class="improve-icon" :style="{ color: item.color }">{{ item.icon }}</div>
        <div class="improve-info">
          <strong>{{ item.label }}</strong>
          <span>{{ item.value }}</span>
        </div>
      </div>
    </div>

    <!-- Split / Diff Editor -->
    <div class="editor-area" :class="viewMode">
      <!-- Original -->
      <div class="editor-panel original">
        <div class="panel-header">
          <div class="panel-title">
            <span class="panel-dot red"></span>
            原始简历
          </div>
          <span class="panel-badge">待优化</span>
        </div>
        <div class="panel-content">
          <div v-for="section in resumeSections" :key="section.id" class="resume-section">
            <h4 class="section-title">{{ section.title }}</h4>
            <div v-for="item in section.original" :key="item.id" class="resume-item">
              <p :class="{ 'has-change': item.hasChange }">{{ item.text }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Divider -->
      <div class="editor-divider" v-if="viewMode === 'split'">
        <div class="divider-line"></div>
        <div class="divider-badge">AI 优化</div>
        <div class="divider-line"></div>
      </div>

      <!-- Optimized -->
      <div class="editor-panel optimized">
        <div class="panel-header">
          <div class="panel-title">
            <span class="panel-dot green"></span>
            AI 优化版本
          </div>
          <span class="panel-badge success">已优化</span>
        </div>
        <div class="panel-content">
          <div v-for="section in resumeSections" :key="section.id" class="resume-section">
            <h4 class="section-title">{{ section.title }}</h4>
            <div v-for="item in section.optimized" :key="item.id" class="resume-item">
              <p :class="{ 'is-improved': item.isImproved }">
                <span v-if="item.isImproved" class="improved-mark">✨</span>
                <span v-html="highlightChanges(item.text, item.highlights)"></span>
              </p>
              <div v-if="item.isImproved && item.tip" class="improve-tip">
                <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
                </svg>
                {{ item.tip }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Change Details -->
    <div class="card changes-card">
      <h3 class="card-title">优化详情</h3>
      <div class="changes-list">
        <div v-for="change in changeDetails" :key="change.id" class="change-item">
          <div class="change-type" :class="change.type">{{ change.typeLabel }}</div>
          <div class="change-content">
            <div class="change-before">
              <span class="change-label">原文</span>
              <span>{{ change.before }}</span>
            </div>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="change-arrow">
              <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
            </svg>
            <div class="change-after">
              <span class="change-label">优化</span>
              <span>{{ change.after }}</span>
            </div>
          </div>
          <div class="change-reason">{{ change.reason }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const viewMode = ref('split')
const applied = ref(false)

const improvements = [
  { icon: '📊', label: '量化指标', value: '+3 处', color: '#4DA3FF' },
  { icon: '💪', label: '强动词', value: '+5 处', color: '#7C3AED' },
  { icon: '✂️', label: '精简冗余', value: '-120字', color: '#10B981' },
  { icon: '🎯', label: '关键词密度', value: '+18%', color: '#F59E0B' },
]

const resumeSections = [
  {
    id: 'summary',
    title: '个人简介',
    original: [
      { id: 1, text: '我是一名 Java 开发工程师，有 3 年工作经验，熟悉 Spring Boot 框架，负责过多个项目的开发工作。', hasChange: true }
    ],
    optimized: [
      { id: 1, text: '具备 3 年 Java 后端研发经验，精通 Spring Boot / Spring Cloud 微服务架构，主导过 2 个亿级用户平台的核心模块设计与落地。', isImproved: true, highlights: ['主导', '亿级用户', '核心模块'], tip: '用"主导"替代"负责"，加入量化规模，大幅提升竞争力' }
    ]
  },
  {
    id: 'work',
    title: '工作经历',
    original: [
      { id: 2, text: '• 负责用户模块的开发和维护', hasChange: true },
      { id: 3, text: '• 参与系统的优化工作，提高了性能', hasChange: true },
      { id: 4, text: '• 编写接口文档，和前端对接', hasChange: false }
    ],
    optimized: [
      { id: 2, text: '• 主导用户中心模块重构，支撑 500 万+ 日活用户，接口 P99 延迟从 800ms 优化至 120ms', isImproved: true, highlights: ['500 万+', '120ms', 'P99'], tip: '添加具体数据指标，让优化成果一目了然' },
      { id: 3, text: '• 设计并实现分布式缓存方案（Redis Cluster），系统 QPS 从 2000 提升至 15000，降低数据库压力 70%', isImproved: true, highlights: ['15000', '70%', 'Redis Cluster'], tip: '用量化前后对比，清晰展示技术价值' },
      { id: 4, text: '• 输出 RESTful API 接口文档（Swagger），与前端团队协同，推进迭代效率提升 30%', isImproved: true, highlights: ['30%', 'Swagger'], tip: '细化工具和结果，避免空洞描述' }
    ]
  },
  {
    id: 'skills',
    title: '技能栈',
    original: [
      { id: 5, text: 'Java, Spring Boot, MySQL, Redis, Git, Linux, Docker', hasChange: false }
    ],
    optimized: [
      { id: 5, text: 'Java (熟练) · Spring Boot / Spring Cloud · MySQL / Redis · Docker / K8s (基础) · 微服务 · 消息队列 (Kafka / RabbitMQ)', isImproved: true, highlights: ['Spring Cloud', 'K8s', 'Kafka'], tip: '按熟练程度分层，补充与岗位相关的技能关键词' }
    ]
  }
]

const changeDetails = [
  { id: 1, type: 'verb', typeLabel: '动词强化', before: '负责用户模块的开发', after: '主导用户中心模块重构', reason: '"主导"体现主动性和领导力，比"负责"更有说服力' },
  { id: 2, type: 'metric', typeLabel: '量化指标', before: '提高了性能', after: 'QPS 从 2000 提升至 15000', reason: '具体数字增加可信度，让 HR 直观感受技术价值' },
  { id: 3, type: 'keyword', typeLabel: '关键词补充', before: 'Java, Spring Boot', after: 'Spring Boot / Spring Cloud (微服务)', reason: '补充岗位 JD 中要求的 Spring Cloud 关键词' },
  { id: 4, type: 'trim', typeLabel: '精简冗余', before: '参与系统的优化工作', after: '设计并实现分布式缓存方案', reason: '"参与"过于被动，改为具体技术方案名称' },
]

const highlightChanges = (text, highlights) => {
  if (!highlights?.length) return text
  let result = text
  highlights.forEach(h => {
    result = result.replace(new RegExp(h, 'g'), `<mark>${h}</mark>`)
  })
  return result
}

const applyChanges = () => {
  applied.value = true
  setTimeout(() => applied.value = false, 3000)
}
</script>

<style lang="scss" scoped>
.optimize-page { padding-top: 32px; }

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.view-toggle {
  display: flex;
  background: $bg-base;
  border: 1px solid $border;
  border-radius: $radius-md;
  padding: 3px;
  gap: 2px;

  button {
    padding: 6px 14px;
    border: none;
    background: none;
    border-radius: $radius-sm;
    font-size: 12px;
    font-weight: 600;
    color: $text-secondary;
    cursor: pointer;
    transition: all 0.2s ease;

    &.active {
      background: white;
      color: $primary;
      box-shadow: $shadow-sm;
    }
  }
}

.apply-btn {
  transition: all 0.3s ease;

  &.applied {
    background: linear-gradient(135deg, $success, #059669) !important;
    box-shadow: 0 4px 14px rgba($success, 0.35) !important;
  }
}

.improvement-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;

  .improve-item {
    flex: 1;
    background: white;
    border: 1px solid $border-light;
    border-radius: $radius-md;
    padding: 16px 20px;
    display: flex;
    align-items: center;
    gap: 12px;
    box-shadow: $shadow-card;

    .improve-icon { font-size: 22px; }

    .improve-info {
      strong { display: block; font-size: 15px; font-weight: 800; color: $text-primary; }
      span { font-size: 12px; color: $text-muted; }
    }
  }
}

.editor-area {
  display: grid;
  gap: 20px;
  margin-bottom: 20px;

  &.split {
    grid-template-columns: 1fr auto 1fr;
    align-items: start;
  }

  &.diff {
    grid-template-columns: 1fr;

    .original { display: none; }
    .editor-divider { display: none; }
  }
}

.editor-panel {
  background: white;
  border-radius: $radius-lg;
  border: 1px solid $border-light;
  box-shadow: $shadow-card;
  overflow: hidden;

  .panel-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 20px;
    background: $bg-base;
    border-bottom: 1px solid $border-light;

    .panel-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 13px;
      font-weight: 600;
      color: $text-primary;
    }

    .panel-dot {
      width: 10px;
      height: 10px;
      border-radius: 50%;

      &.red { background: $danger; }
      &.green { background: $success; }
    }

    .panel-badge {
      font-size: 11px;
      padding: 2px 8px;
      border-radius: $radius-full;
      background: rgba($warning, 0.1);
      color: $warning;
      font-weight: 600;

      &.success {
        background: rgba($success, 0.1);
        color: $success;
      }
    }
  }

  .panel-content {
    padding: 20px;
    min-height: 300px;
  }
}

.resume-section {
  margin-bottom: 20px;

  .section-title {
    font-size: 13px;
    font-weight: 700;
    color: $primary;
    margin-bottom: 10px;
    padding-bottom: 6px;
    border-bottom: 1px solid rgba($primary, 0.1);
    text-transform: uppercase;
    letter-spacing: 0.05em;
  }
}

.resume-item {
  margin-bottom: 8px;

  p {
    font-size: 13px;
    line-height: 1.7;
    color: $text-secondary;

    &.has-change {
      background: rgba($danger, 0.05);
      padding: 6px 8px;
      border-radius: $radius-sm;
      border-left: 3px solid rgba($danger, 0.3);
    }

    &.is-improved {
      background: rgba($success, 0.05);
      padding: 6px 8px;
      border-radius: $radius-sm;
      border-left: 3px solid rgba($success, 0.3);
      color: $text-primary;

      :deep(mark) {
        background: rgba($primary, 0.15);
        color: $primary;
        padding: 1px 3px;
        border-radius: 3px;
        font-weight: 600;
      }
    }

    .improved-mark { margin-right: 4px; }
  }

  .improve-tip {
    display: flex;
    align-items: flex-start;
    gap: 6px;
    font-size: 11px;
    color: $text-muted;
    margin-top: 4px;
    padding: 4px 8px;
    background: rgba($primary, 0.04);
    border-radius: $radius-sm;
    line-height: 1.5;

    svg { flex-shrink: 0; color: $primary; margin-top: 1px; }
  }
}

.editor-divider {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 0 4px;

  .divider-line {
    width: 1px;
    flex: 1;
    background: $border;
  }

  .divider-badge {
    font-size: 11px;
    font-weight: 700;
    color: $primary;
    background: rgba($primary, 0.1);
    padding: 6px 10px;
    border-radius: $radius-full;
    writing-mode: horizontal-tb;
    white-space: nowrap;
  }
}

.changes-card {
  padding: 24px;

  .card-title { margin-bottom: 20px; }
}

.changes-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.change-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 14px;
  background: $bg-base;
  border-radius: $radius-md;
  border: 1px solid $border-light;

  .change-type {
    font-size: 11px;
    font-weight: 700;
    padding: 3px 10px;
    border-radius: $radius-full;
    flex-shrink: 0;
    margin-top: 1px;

    &.verb { background: rgba($accent, 0.1); color: $accent; }
    &.metric { background: rgba($success, 0.1); color: $success; }
    &.keyword { background: rgba($primary, 0.1); color: $primary; }
    &.trim { background: rgba($warning, 0.1); color: $warning; }
  }

  .change-content {
    flex: 1;
    display: flex;
    align-items: flex-start;
    gap: 10px;

    .change-before, .change-after {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 3px;

      .change-label {
        font-size: 10px;
        font-weight: 700;
        text-transform: uppercase;
        color: $text-muted;
        letter-spacing: 0.05em;
      }

      span:last-child {
        font-size: 13px;
        color: $text-secondary;
        line-height: 1.5;
      }
    }

    .change-after span:last-child {
      color: $text-primary;
      font-weight: 500;
    }

    .change-arrow {
      color: $primary;
      flex-shrink: 0;
      margin-top: 16px;
    }
  }

  .change-reason {
    font-size: 11px;
    color: $text-muted;
    padding-top: 4px;
    flex-shrink: 0;
    max-width: 180px;
    line-height: 1.5;
    border-left: 1px solid $border;
    padding-left: 12px;
  }
}
</style>
