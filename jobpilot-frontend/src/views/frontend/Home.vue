<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-bg">
        <div class="orb orb-1"></div>
        <div class="orb orb-2"></div>
        <div class="grid-pattern"></div>
      </div>
      <div class="hero-content">
        <div class="hero-text">
          <div class="hero-badge">
            <span class="badge-dot"></span>
            <span>AI 驱动 · 智能匹配 · 精准求职</span>
          </div>
          <h1 class="hero-title">
            <span>让 AI 帮你</span>
            <span class="gradient-text">赢得梦想工作</span>
          </h1>
          <p class="hero-desc">
            上传简历，AI 秒速分析匹配度，精准优化每一句话，推荐最适合你的岗位。
            <br />从投递到面试，全程 AI 护航。
          </p>
          <div class="hero-actions">
            <router-link to="/upload" class="btn-hero-primary">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/>
              </svg>
              免费上传简历
            </router-link>
            <router-link to="/analysis" class="btn-hero-secondary">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polygon points="5 3 19 12 5 21 5 3"/>
              </svg>
              查看演示
            </router-link>
          </div>
          <div class="hero-stats">
            <div class="stat-item">
              <strong>50,000+</strong>
              <span>简历分析</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <strong>95%</strong>
              <span>用户满意度</span>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <strong>3x</strong>
              <span>面试邀请</span>
            </div>
          </div>
        </div>

        <div class="hero-visual">
          <div class="dashboard-preview">
            <div class="preview-header">
              <div class="preview-dots">
                <span></span><span></span><span></span>
              </div>
              <span class="preview-title">AI 分析报告</span>
            </div>
            <div class="preview-body">
              <div class="score-ring-mini">
                <svg viewBox="0 0 80 80" width="80" height="80">
                  <circle cx="40" cy="40" r="32" fill="none" stroke="#EAF4FF" stroke-width="8"/>
                  <circle cx="40" cy="40" r="32" fill="none" stroke="url(#grad)" stroke-width="8"
                    stroke-dasharray="201" stroke-dashoffset="30" stroke-linecap="round"
                    transform="rotate(-90 40 40)"/>
                  <defs>
                    <linearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="0%">
                      <stop offset="0%" stop-color="#4DA3FF"/>
                      <stop offset="100%" stop-color="#7C3AED"/>
                    </linearGradient>
                  </defs>
                </svg>
                <div class="ring-label">
                  <strong>85</strong>
                  <span>匹配度</span>
                </div>
              </div>
              <div class="preview-skills">
                <div v-for="skill in previewSkills" :key="skill.name" class="skill-bar-item">
                  <span class="skill-name">{{ skill.name }}</span>
                  <div class="skill-track">
                    <div class="skill-fill" :style="{ width: skill.score + '%', background: skill.color }"></div>
                  </div>
                  <span class="skill-val">{{ skill.score }}%</span>
                </div>
              </div>
            </div>
            <div class="preview-tags">
              <span class="tag tag-green">✓ Java</span>
              <span class="tag tag-green">✓ Spring</span>
              <span class="tag tag-yellow">△ K8s</span>
              <span class="tag tag-red">✗ Flink</span>
            </div>
          </div>

          <div class="floating-card card-ai">
            <div class="fc-icon">✨</div>
            <div class="fc-text">
              <strong>AI 优化建议</strong>
              <span>添加量化指标可提升 23%</span>
            </div>
          </div>

          <div class="floating-card card-match">
            <div class="fc-icon">🎯</div>
            <div class="fc-text">
              <strong>高度匹配</strong>
              <span>找到 48 个推荐岗位</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">核心功能</span>
          <h2>一站式 AI 求职解决方案</h2>
          <p>从简历优化到岗位匹配，AI 全程陪伴你的求职之旅</p>
        </div>

        <div class="features-grid">
          <div v-for="feature in features" :key="feature.title" class="feature-card card">
            <div class="feature-icon" :style="{ background: feature.bg }">
              <component :is="'div'" v-html="feature.icon" class="icon-svg"></component>
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.desc }}</p>
            <router-link :to="feature.link" class="feature-link">
              了解更多
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg>
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- How it Works -->
    <section class="how-it-works">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">使用流程</span>
          <h2>三步完成 AI 求职分析</h2>
          <p>简单三步，让 AI 帮你找到最适合的工作</p>
        </div>

        <div class="steps">
          <div v-for="(step, i) in steps" :key="step.title" class="step-item">
            <div class="step-num">{{ String(i + 1).padStart(2, '0') }}</div>
            <div class="step-content">
              <div class="step-icon">{{ step.emoji }}</div>
              <h3>{{ step.title }}</h3>
              <p>{{ step.desc }}</p>
            </div>
            <div class="step-arrow" v-if="i < steps.length - 1">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Testimonials -->
    <section class="testimonials">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">用户评价</span>
          <h2>他们都找到了好工作</h2>
        </div>

        <div class="testimonials-grid">
          <div v-for="t in testimonials" :key="t.name" class="testimonial-card card">
            <div class="stars">★★★★★</div>
            <p class="testimonial-text">"{{ t.text }}"</p>
            <div class="testimonial-author">
              <div class="t-avatar" :style="{ background: t.color }">{{ t.name.charAt(0) }}</div>
              <div>
                <strong>{{ t.name }}</strong>
                <span>{{ t.role }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
      <div class="cta-inner">
        <div class="cta-orb"></div>
        <h2>准备好了吗？开始你的 AI 求职之旅</h2>
        <p>免费注册，立即体验 AI 简历分析与岗位匹配</p>
        <router-link to="/auth/register" class="cta-btn">
          免费开始使用
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
          </svg>
        </router-link>
      </div>
    </section>
  </div>
</template>

<script setup>
const previewSkills = [
  { name: 'Java', score: 90, color: 'linear-gradient(90deg,#4DA3FF,#7C3AED)' },
  { name: 'Spring', score: 80, color: 'linear-gradient(90deg,#4DA3FF,#7C3AED)' },
  { name: 'MySQL', score: 75, color: 'linear-gradient(90deg,#10B981,#4DA3FF)' },
  { name: 'Redis', score: 65, color: 'linear-gradient(90deg,#F59E0B,#EF4444)' },
]

const features = [
  {
    title: '智能简历分析',
    desc: 'AI 深度解析简历结构，提取关键技能，评估与岗位的匹配程度，给出精准评分。',
    icon: `<svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>`,
    bg: 'linear-gradient(135deg,rgba(77,163,255,0.15),rgba(77,163,255,0.05))',
    link: '/analysis'
  },
  {
    title: '技能差距分析',
    desc: '对比目标岗位要求，精准识别缺失技能，提供个性化学习路径建议。',
    icon: `<svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>`,
    bg: 'linear-gradient(135deg,rgba(124,58,237,0.15),rgba(124,58,237,0.05))',
    link: '/analysis'
  },
  {
    title: 'AI 简历优化',
    desc: '智能重写简历内容，强化动词表达，添加量化指标，让每一句话都更有力量。',
    icon: `<svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>`,
    bg: 'linear-gradient(135deg,rgba(16,185,129,0.15),rgba(16,185,129,0.05))',
    link: '/optimize'
  },
  {
    title: '精准岗位推荐',
    desc: '基于简历特征和匹配算法，从海量岗位中筛选最适合你的职位，附带匹配分数。',
    icon: `<svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>`,
    bg: 'linear-gradient(135deg,rgba(245,158,11,0.15),rgba(245,158,11,0.05))',
    link: '/jobs'
  }
]

const steps = [
  { emoji: '📤', title: '上传简历', desc: '支持 PDF / DOCX 格式，拖拽上传，5 秒完成' },
  { emoji: '🤖', title: 'AI 深度分析', desc: '智能提取技能标签，计算岗位匹配分数' },
  { emoji: '🎯', title: '获取结果', desc: '查看分析报告，优化简历，投递推荐岗位' }
]

const testimonials = [
  {
    name: '张小明',
    role: '后端工程师 · 入职腾讯',
    text: 'JobPilot AI 分析出我简历中缺少量化数据，优化后面试邀请直接翻了三倍，强烈推荐！',
    color: 'linear-gradient(135deg,#4DA3FF,#7C3AED)'
  },
  {
    name: '李雨晴',
    role: '产品经理 · 入职字节',
    text: '技能差距分析太准了，精确指出了我不懂数据分析的短板，补强后顺利拿到 offer。',
    color: 'linear-gradient(135deg,#10B981,#4DA3FF)'
  },
  {
    name: '王大海',
    role: '前端开发 · 入职阿里',
    text: 'AI 把我的简历从普通升级成了亮眼，HR 说第一眼就被吸引了，太厉害了！',
    color: 'linear-gradient(135deg,#F59E0B,#EF4444)'
  }
]
</script>

<style lang="scss" scoped>
// ─── Hero ─────────────────────────────────────────────────────────────────
.hero {
  position: relative;
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  overflow: hidden;
  background: linear-gradient(160deg, #EAF4FF 0%, #F7FBFF 60%, #F0EEFF 100%);
}

.hero-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;

  .orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.5;
  }

  .orb-1 {
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, rgba($primary, 0.25), transparent);
    top: -100px;
    left: -100px;
  }

  .orb-2 {
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba($accent, 0.2), transparent);
    bottom: -50px;
    right: 100px;
  }

  .grid-pattern {
    position: absolute;
    inset: 0;
    background-image:
      linear-gradient(rgba($primary, 0.04) 1px, transparent 1px),
      linear-gradient(90deg, rgba($primary, 0.04) 1px, transparent 1px);
    background-size: 40px 40px;
  }
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 24px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
  position: relative;
  z-index: 1;
  width: 100%;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba($primary, 0.08);
  border: 1px solid rgba($primary, 0.2);
  border-radius: $radius-full;
  padding: 6px 14px;
  font-size: 12px;
  font-weight: 600;
  color: $primary;
  margin-bottom: 20px;

  .badge-dot {
    width: 6px;
    height: 6px;
    background: $success;
    border-radius: 50%;
    animation: pulse-dot 2s infinite;
  }
}

@keyframes pulse-dot {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.6; transform: scale(0.8); }
}

.hero-title {
  font-size: 54px;
  font-weight: 900;
  line-height: 1.15;
  color: $text-primary;
  margin-bottom: 20px;

  span { display: block; }

  .gradient-text {
    background: linear-gradient(135deg, $primary, $accent);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
}

.hero-desc {
  font-size: 16px;
  color: $text-secondary;
  line-height: 1.8;
  margin-bottom: 32px;
}

.hero-actions {
  display: flex;
  gap: 14px;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.btn-hero-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  background: linear-gradient(135deg, $primary, $primary-dark);
  color: white;
  border-radius: $radius-md;
  font-size: 15px;
  font-weight: 700;
  text-decoration: none;
  box-shadow: 0 6px 20px rgba($primary, 0.35);
  transition: all 0.25s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 30px rgba($primary, 0.45);
  }
}

.btn-hero-secondary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  background: white;
  color: $text-primary;
  border-radius: $radius-md;
  font-size: 15px;
  font-weight: 600;
  text-decoration: none;
  border: 1px solid $border;
  box-shadow: $shadow-sm;
  transition: all 0.25s ease;

  &:hover {
    border-color: $primary;
    color: $primary;
    transform: translateY(-1px);
    box-shadow: $shadow-md;
  }
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 20px;

  .stat-item {
    display: flex;
    flex-direction: column;

    strong {
      font-size: 22px;
      font-weight: 800;
      color: $text-primary;
      background: linear-gradient(135deg, $primary, $accent);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    span {
      font-size: 12px;
      color: $text-muted;
    }
  }

  .stat-divider {
    width: 1px;
    height: 30px;
    background: $border;
  }
}

// ─── Hero Visual ─────────────────────────────────────────────────────────
.hero-visual {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dashboard-preview {
  background: white;
  border-radius: $radius-xl;
  box-shadow: $shadow-lg;
  border: 1px solid $border-light;
  overflow: hidden;
  width: 320px;

  .preview-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 12px 16px;
    background: $bg-sidebar;
    border-bottom: 1px solid rgba(255,255,255,0.06);

    .preview-dots {
      display: flex;
      gap: 5px;
      span {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #374151;
        &:nth-child(1) { background: #EF4444; }
        &:nth-child(2) { background: #F59E0B; }
        &:nth-child(3) { background: #10B981; }
      }
    }

    .preview-title {
      font-size: 12px;
      color: #94A3B8;
      font-weight: 500;
    }
  }

  .preview-body {
    padding: 20px;
    display: flex;
    gap: 16px;
    align-items: flex-start;
  }
}

.score-ring-mini {
  position: relative;
  flex-shrink: 0;

  .ring-label {
    position: absolute;
    inset: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    strong {
      font-size: 20px;
      font-weight: 800;
      background: linear-gradient(135deg, $primary, $accent);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    span {
      font-size: 10px;
      color: $text-muted;
    }
  }
}

.preview-skills {
  flex: 1;

  .skill-bar-item {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 8px;

    .skill-name {
      font-size: 11px;
      color: $text-secondary;
      width: 36px;
      flex-shrink: 0;
    }

    .skill-track {
      flex: 1;
      height: 5px;
      background: $bg-base;
      border-radius: $radius-full;
      overflow: hidden;
    }

    .skill-fill {
      height: 100%;
      border-radius: $radius-full;
      transition: width 1s ease;
    }

    .skill-val {
      font-size: 10px;
      color: $text-muted;
      width: 26px;
      text-align: right;
    }
  }
}

.preview-tags {
  padding: 12px 16px;
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
  border-top: 1px solid $border-light;
}

.floating-card {
  position: absolute;
  background: white;
  border-radius: $radius-md;
  padding: 12px 14px;
  box-shadow: $shadow-lg;
  border: 1px solid $border-light;
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 180px;

  .fc-icon {
    font-size: 20px;
    flex-shrink: 0;
  }

  .fc-text {
    display: flex;
    flex-direction: column;

    strong {
      font-size: 12px;
      color: $text-primary;
    }

    span {
      font-size: 11px;
      color: $text-secondary;
    }
  }

  &.card-ai {
    top: -20px;
    right: -30px;
    animation: float-y 3s ease-in-out infinite;
  }

  &.card-match {
    bottom: 20px;
    left: -40px;
    animation: float-y 3s ease-in-out infinite 1.5s;
  }
}

@keyframes float-y {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

// ─── Sections ─────────────────────────────────────────────────────────────
.section-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 24px;
}

.section-header {
  text-align: center;
  margin-bottom: 56px;

  .section-tag {
    display: inline-block;
    background: rgba($primary, 0.1);
    color: $primary;
    font-size: 12px;
    font-weight: 700;
    padding: 4px 14px;
    border-radius: $radius-full;
    margin-bottom: 12px;
    text-transform: uppercase;
    letter-spacing: 0.08em;
  }

  h2 {
    font-size: 36px;
    font-weight: 800;
    color: $text-primary;
    margin-bottom: 12px;
  }

  p {
    font-size: 16px;
    color: $text-secondary;
  }
}

// ─── Features ─────────────────────────────────────────────────────────────
.features {
  background: white;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.feature-card {
  padding: 28px 24px;
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: $shadow-lg;
  }

  .feature-icon {
    width: 52px;
    height: 52px;
    border-radius: $radius-md;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 16px;

    .icon-svg {
      color: $primary;
      display: flex;
    }
  }

  h3 {
    font-size: 16px;
    font-weight: 700;
    color: $text-primary;
    margin-bottom: 8px;
  }

  p {
    font-size: 13px;
    color: $text-secondary;
    line-height: 1.7;
    margin-bottom: 16px;
  }

  .feature-link {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: $primary;
    font-weight: 600;
    text-decoration: none;
    transition: gap 0.2s ease;

    &:hover { gap: 8px; }
  }
}

// ─── How it Works ─────────────────────────────────────────────────────────
.how-it-works {
  background: $bg-base;
}

.steps {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  position: relative;

  .step-num {
    font-size: 48px;
    font-weight: 900;
    background: linear-gradient(135deg, rgba($primary,0.15), rgba($accent,0.1));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    margin-bottom: 8px;
    line-height: 1;
  }

  .step-content {
    text-align: center;
    padding: 24px;
    background: white;
    border-radius: $radius-lg;
    box-shadow: $shadow-card;
    border: 1px solid $border-light;
    flex: 1;
    width: 100%;

    .step-icon {
      font-size: 36px;
      margin-bottom: 12px;
    }

    h3 {
      font-size: 16px;
      font-weight: 700;
      color: $text-primary;
      margin-bottom: 8px;
    }

    p {
      font-size: 13px;
      color: $text-secondary;
      line-height: 1.7;
    }
  }

  .step-arrow {
    position: absolute;
    right: -24px;
    top: 50%;
    transform: translateY(-50%);
    color: $primary;
    z-index: 1;
    opacity: 0.5;
  }
}

// ─── Testimonials ─────────────────────────────────────────────────────────
.testimonials {
  background: linear-gradient(180deg, #F7FBFF 0%, white 100%);
}

.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.testimonial-card {
  padding: 28px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-3px);
    box-shadow: $shadow-lg;
  }

  .stars {
    color: #F59E0B;
    font-size: 14px;
    margin-bottom: 14px;
    letter-spacing: 2px;
  }

  .testimonial-text {
    font-size: 14px;
    color: $text-secondary;
    line-height: 1.8;
    margin-bottom: 20px;
  }

  .testimonial-author {
    display: flex;
    align-items: center;
    gap: 10px;

    .t-avatar {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-weight: 700;
      font-size: 14px;
    }

    strong {
      display: block;
      font-size: 13px;
      color: $text-primary;
    }

    span {
      font-size: 12px;
      color: $text-muted;
    }
  }
}

// ─── CTA ─────────────────────────────────────────────────────────────────
.cta-section {
  background: $bg-sidebar;
}

.cta-inner {
  max-width: 640px;
  margin: 0 auto;
  padding: 80px 24px;
  text-align: center;
  position: relative;

  .cta-orb {
    position: absolute;
    width: 300px;
    height: 300px;
    background: radial-gradient(circle, rgba($primary, 0.15), transparent);
    border-radius: 50%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    filter: blur(40px);
  }

  h2 {
    font-size: 34px;
    font-weight: 800;
    color: white;
    margin-bottom: 12px;
    position: relative;
  }

  p {
    font-size: 15px;
    color: #94A3B8;
    margin-bottom: 32px;
    position: relative;
  }
}

.cta-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  background: linear-gradient(135deg, $primary, $accent);
  color: white;
  border-radius: $radius-md;
  font-size: 15px;
  font-weight: 700;
  text-decoration: none;
  box-shadow: 0 6px 24px rgba($primary, 0.4);
  transition: all 0.25s ease;
  position: relative;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 32px rgba($primary, 0.5);
  }
}
</style>
