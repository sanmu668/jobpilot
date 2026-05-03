<template>
  <div class="front-layout">
    <header class="front-header" :class="{ scrolled: isScrolled }">
      <div class="header-inner">
        <router-link to="/" class="logo">
          <div class="logo-icon">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              <path d="M2 17l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              <path d="M2 12l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            </svg>
          </div>
          <span class="logo-text">JobPilot <span class="ai-badge">AI</span></span>
        </router-link>

        <nav class="front-nav">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/upload" class="nav-item">上传简历</router-link>
          <router-link to="/analysis" class="nav-item">分析结果</router-link>
          <router-link to="/optimize" class="nav-item">简历优化</router-link>
          <router-link to="/jobs" class="nav-item">推荐岗位</router-link>
        </nav>
  
        <div class="header-actions">
          <template v-if="authStore.isLoggedIn">
            <div class="user-menu" @click="toggleDropdown" ref="userMenuRef">
              <div class="user-avatar">{{ authStore.userName?.charAt(0)?.toUpperCase() }}</div>
              <span class="user-name">{{ authStore.userName }}</span>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>

              <div class="dropdown-menu" v-if="dropdownOpen">
                <!-- 用户信息头部 -->
                <div class="dropdown-user-header">
                  <div class="dropdown-avatar">{{ authStore.userName?.charAt(0)?.toUpperCase() }}</div>
                  <div class="dropdown-user-info">
                    <div class="dropdown-user-name">{{ authStore.userName }}</div>
                    <div class="dropdown-user-email">{{ authStore.user?.email }}</div>
                  </div>
                </div>
                <div class="dropdown-divider"></div>

                <!-- 快捷功能入口 -->
                <div class="dropdown-grid">
                  <router-link to="/upload" class="dropdown-grid-item" @click="dropdownOpen = false">
                    <div class="grid-icon">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="12" y1="18" x2="12" y2="12"/><line x1="9" y1="15" x2="15" y2="15"/></svg>
                    </div>
                    <span>我的简历</span>
                  </router-link>
                  <router-link to="/analysis" class="dropdown-grid-item" @click="dropdownOpen = false">
                    <div class="grid-icon">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg>
                    </div>
                    <span>分析结果</span>
                  </router-link>
                  <router-link to="/optimize" class="dropdown-grid-item" @click="dropdownOpen = false">
                    <div class="grid-icon">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 013 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
                    </div>
                    <span>简历优化</span>
                  </router-link>
                  <router-link to="/jobs" class="dropdown-grid-item" @click="dropdownOpen = false">
                    <div class="grid-icon">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 7V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v2"/></svg>
                    </div>
                    <span>推荐岗位</span>
                  </router-link>
                </div>
                <div class="dropdown-divider"></div>

                <!-- 管理员专属入口 -->
                <router-link to="/admin/dashboard" v-if="authStore.isAdmin" class="dropdown-item" @click="dropdownOpen = false">
                  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
                  管理后台
                </router-link>
                <div class="dropdown-divider" v-if="authStore.isAdmin"></div>

                <button class="dropdown-item danger" @click="logout">
                  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                  退出登录
                </button>
              </div>
            </div>
          </template>
          <template v-else>
            <router-link to="/auth/login" class="btn-ghost btn-sm">登录</router-link>
            <router-link to="/auth/register" class="btn-primary btn-sm">免费注册</router-link>
          </template>
        </div>
      </div>
    </header>

    <main class="front-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <footer class="front-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <div class="logo">
            <div class="logo-icon">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                <path d="M2 17l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              </svg>
            </div>
            <span class="logo-text">JobPilot AI</span>
          </div>
          <p>AI 驱动的智能求职平台，让每一份简历都精准匹配。</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>产品</h4>
            <router-link to="/upload">上传简历</router-link>
            <router-link to="/analysis">分析报告</router-link>
            <router-link to="/optimize">简历优化</router-link>
            <router-link to="/jobs">岗位推荐</router-link>
          </div>
          <div class="link-group">
            <h4>关于</h4>
            <a href="#">帮助中心</a>
            <a href="#">隐私政策</a>
            <a href="#">服务条款</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2026 JobPilot AI. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'

const router = useRouter()
const authStore = useAuthStore()

const isScrolled = ref(false)
const dropdownOpen = ref(false)
const userMenuRef = ref(null)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 20
}

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value
}

const handleClickOutside = (e) => {
  if (userMenuRef.value && !userMenuRef.value.contains(e.target)) {
    dropdownOpen.value = false
  }
}

const logout = () => {
  authStore.logout()
  dropdownOpen.value = false
  router.push('/')
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style lang="scss" scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.front-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: rgba(248, 250, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid transparent;
  transition: all 0.3s ease;

  &.scrolled {
    background: rgba(255, 255, 255, 0.92);
    border-bottom-color: $border;
    box-shadow: $shadow-sm;
  }
}

.header-inner {
  display: flex;
  align-items: center;
  gap: 32px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;

  .logo-icon {
    width: 36px;
    height: 36px;
    background: linear-gradient(135deg, $primary, $accent);
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
  }

  .logo-text {
    font-size: 18px;
    font-weight: 700;
    color: $text-primary;

    .ai-badge {
      display: inline-block;
      background: linear-gradient(135deg, $primary, $accent);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }
}

.front-nav {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;

  .nav-item {
    padding: 6px 14px;
    border-radius: $radius-md;
    font-size: 14px;
    font-weight: 500;
    color: $text-secondary;
    text-decoration: none;
    transition: all 0.2s ease;

    &:hover {
      color: $primary;
      background: rgba($primary, 0.08);
    }

    &.router-link-active {
      color: $primary;
      background: rgba($primary, 0.1);
      font-weight: 600;
    }
  }
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
  flex-shrink: 0;

  .btn-sm {
    padding: 7px 18px;
    font-size: 13px;
    border-radius: $radius-md;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.2s ease;
    font-weight: 600;
  }

  .btn-ghost.btn-sm {
    background: transparent;
    color: $text-secondary;
    border: 1px solid $border;

    &:hover {
      border-color: $primary;
      color: $primary;
    }
  }

  .btn-primary.btn-sm {
    background: linear-gradient(135deg, $primary, $primary-dark);
    color: white;
    border: none;
    box-shadow: 0 2px 8px rgba($primary, 0.3);

    &:hover {
      box-shadow: 0 4px 14px rgba($primary, 0.4);
      transform: translateY(-1px);
    }
  }
}

.user-menu {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  border-radius: $radius-md;
  cursor: pointer;
  transition: background 0.2s ease;

  &:hover {
    background: rgba($primary, 0.06);
  }

  .user-avatar {
    width: 30px;
    height: 30px;
    background: linear-gradient(135deg, $primary, $accent);
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 13px;
    font-weight: 700;
  }

  .user-name {
    font-size: 13px;
    font-weight: 600;
    color: $text-primary;
  }

  .dropdown-menu {
    position: absolute;
    top: calc(100% + 8px);
    right: 0;
    width: 220px;
    background: white;
    border-radius: $radius-lg;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12), 0 2px 8px rgba(0, 0, 0, 0.06);
    border: 1px solid $border-light;
    padding: 8px;
    z-index: 200;
    overflow: hidden;

    .dropdown-user-header {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px 10px 12px;

      .dropdown-avatar {
        width: 38px;
        height: 38px;
        background: linear-gradient(135deg, $primary, $accent);
        color: white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 15px;
        font-weight: 700;
        flex-shrink: 0;
      }

      .dropdown-user-info {
        min-width: 0;

        .dropdown-user-name {
          font-size: 13px;
          font-weight: 600;
          color: $text-primary;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .dropdown-user-email {
          font-size: 11px;
          color: $text-muted;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          margin-top: 1px;
        }
      }
    }

    .dropdown-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 4px;
      padding: 4px 2px;

      .dropdown-grid-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 6px;
        padding: 10px 6px;
        border-radius: $radius-sm;
        text-decoration: none;
        transition: background 0.15s ease;
        cursor: pointer;

        .grid-icon {
          width: 36px;
          height: 36px;
          background: rgba($primary, 0.08);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: $primary;
          transition: background 0.15s ease;
        }

        span {
          font-size: 11px;
          font-weight: 500;
          color: $text-secondary;
          text-align: center;
        }

        &:hover {
          background: rgba($primary, 0.05);

          .grid-icon {
            background: rgba($primary, 0.14);
          }

          span {
            color: $primary;
          }
        }
      }
    }

    .dropdown-item {
      display: flex;
      align-items: center;
      gap: 8px;
      width: 100%;
      padding: 8px 10px;
      border-radius: $radius-sm;
      font-size: 13px;
      color: $text-primary;
      text-decoration: none;
      border: none;
      background: none;
      cursor: pointer;
      transition: background 0.15s ease;

      &:hover {
        background: $bg-base;
      }

      &.danger {
        color: $danger;
        &:hover { background: rgba($danger, 0.06); }
      }
    }

    .dropdown-divider {
      height: 1px;
      background: $border-light;
      margin: 4px 0;
    }
  }
}

.front-main {
  flex: 1;
  margin-top: 64px;
}

.front-footer {
  background: $bg-sidebar;
  color: $text-sidebar;
  padding: 48px 0 0;
  margin-top: auto;

  .footer-inner {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px 40px;
    display: flex;
    gap: 80px;
  }

  .footer-brand {
    flex: 1;

    .logo {
      margin-bottom: 12px;
      color: white;

      .logo-icon {
        background: linear-gradient(135deg, $primary, $accent);
      }

      .logo-text {
        color: white;
        font-size: 16px;
      }
    }

    p {
      font-size: 13px;
      line-height: 1.7;
      color: #64748B;
      max-width: 260px;
    }
  }

  .footer-links {
    display: flex;
    gap: 60px;

    .link-group {
      h4 {
        font-size: 13px;
        font-weight: 600;
        color: #CBD5E1;
        margin-bottom: 14px;
        text-transform: uppercase;
        letter-spacing: 0.08em;
      }

      a {
        display: block;
        font-size: 13px;
        color: #64748B;
        text-decoration: none;
        margin-bottom: 8px;
        transition: color 0.2s ease;

        &:hover {
          color: $primary;
        }
      }
    }
  }

  .footer-bottom {
    border-top: 1px solid rgba(255, 255, 255, 0.06);
    padding: 16px 24px;
    text-align: center;

    p {
      font-size: 12px;
      color: #475569;
    }
  }
}
</style>
