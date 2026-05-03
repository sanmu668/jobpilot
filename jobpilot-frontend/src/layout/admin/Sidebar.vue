<template>
  <aside class="sidebar" :class="{ collapsed: appStore.sidebarCollapsed }">
    <div class="sidebar-logo">
      <router-link to="/" class="logo-link">
        <div class="logo-icon">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            <path d="M2 17l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            <path d="M2 12l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
          </svg>
        </div>
        <span v-if="!appStore.sidebarCollapsed" class="logo-text">JobPilot <span>AI</span></span>
      </router-link>
    </div>

    <nav class="sidebar-nav">
      <div class="nav-section">
        <span v-if="!appStore.sidebarCollapsed" class="nav-section-label">概览</span>
        <router-link to="/admin/dashboard" class="nav-item" :title="appStore.sidebarCollapsed ? '控制台' : ''">
          <span class="nav-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/>
              <rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/>
            </svg>
          </span>
          <span v-if="!appStore.sidebarCollapsed" class="nav-label">控制台</span>
        </router-link>
      </div>

      <div class="nav-section">
        <span v-if="!appStore.sidebarCollapsed" class="nav-section-label">数据管理</span>
        <router-link to="/admin/users" class="nav-item">
          <span class="nav-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/>
              <path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/>
            </svg>
          </span>
          <span v-if="!appStore.sidebarCollapsed" class="nav-label">用户管理</span>
        </router-link>

        <router-link to="/admin/resumes" class="nav-item">
          <span class="nav-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/>
              <polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/>
            </svg>
          </span>
          <span v-if="!appStore.sidebarCollapsed" class="nav-label">简历管理</span>
        </router-link>

        <router-link to="/admin/jobs" class="nav-item">
          <span class="nav-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 21V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v16"/>
            </svg>
          </span>
          <span v-if="!appStore.sidebarCollapsed" class="nav-label">岗位管理</span>
        </router-link>

        <router-link to="/admin/matches" class="nav-item">
          <span class="nav-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/>
            </svg>
          </span>
          <span v-if="!appStore.sidebarCollapsed" class="nav-label">匹配管理</span>
        </router-link>
      </div>

      <div class="nav-section">
        <span v-if="!appStore.sidebarCollapsed" class="nav-section-label">系统</span>
        <router-link to="/admin/ai-logs" class="nav-item">
          <span class="nav-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/>
            </svg>
          </span>
          <span v-if="!appStore.sidebarCollapsed" class="nav-label">AI 日志</span>
        </router-link>

        <router-link to="/admin/system" class="nav-item">
          <span class="nav-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="3"/>
              <path d="M19.4 15a1.65 1.65 0 00.33 1.82l.06.06a2 2 0 010 2.83 2 2 0 01-2.83 0l-.06-.06a1.65 1.65 0 00-1.82-.33 1.65 1.65 0 00-1 1.51V21a2 2 0 01-2 2 2 2 0 01-2-2v-.09A1.65 1.65 0 009 19.4a1.65 1.65 0 00-1.82.33l-.06.06a2 2 0 01-2.83-2.83l.06-.06A1.65 1.65 0 004.68 15a1.65 1.65 0 00-1.51-1H3a2 2 0 01-2-2 2 2 0 012-2h.09A1.65 1.65 0 004.6 9a1.65 1.65 0 00-.33-1.82l-.06-.06a2 2 0 012.83-2.83l.06.06A1.65 1.65 0 009 4.68a1.65 1.65 0 001-1.51V3a2 2 0 012-2 2 2 0 012 2v.09a1.65 1.65 0 001 1.51 1.65 1.65 0 001.82-.33l.06-.06a2 2 0 012.83 2.83l-.06.06A1.65 1.65 0 0019.4 9a1.65 1.65 0 001.51 1H21a2 2 0 012 2 2 2 0 01-2 2h-.09a1.65 1.65 0 00-1.51 1z"/>
            </svg>
          </span>
          <span v-if="!appStore.sidebarCollapsed" class="nav-label">系统配置</span>
        </router-link>
      </div>
    </nav>

    <div class="sidebar-footer">
      <router-link to="/" class="back-link">
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
          <polyline points="9 22 9 12 15 12 15 22"/>
        </svg>
        <span v-if="!appStore.sidebarCollapsed">返回前台</span>
      </router-link>
    </div>
  </aside>
</template>

<script setup>
import { useAppStore } from '@/store/modules/app'
const appStore = useAppStore()
</script>

<style lang="scss" scoped>
.sidebar {
  width: $sidebar-width;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  background: $bg-sidebar;
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  z-index: 50;
  overflow: hidden;

  &.collapsed {
    width: $sidebar-collapsed-width;
  }
}

.sidebar-logo {
  padding: 20px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);

  .logo-link {
    display: flex;
    align-items: center;
    gap: 10px;
    text-decoration: none;
  }

  .logo-icon {
    width: 34px;
    height: 34px;
    background: linear-gradient(135deg, $primary, $accent);
    border-radius: 9px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    flex-shrink: 0;
  }

  .logo-text {
    font-size: 15px;
    font-weight: 700;
    color: white;
    white-space: nowrap;

    span {
      background: linear-gradient(135deg, $primary, $accent-light);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }
}

.sidebar-nav {
  flex: 1;
  padding: 12px 10px;
  overflow-y: auto;

  .nav-section {
    margin-bottom: 8px;

    .nav-section-label {
      display: block;
      font-size: 10px;
      font-weight: 700;
      color: #334155;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      padding: 8px 10px 4px;
    }
  }

  .nav-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 9px 10px;
    border-radius: $radius-sm;
    text-decoration: none;
    color: $text-sidebar;
    font-size: 13px;
    font-weight: 500;
    transition: all 0.2s ease;
    white-space: nowrap;
    margin-bottom: 2px;

    .nav-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 20px;
      flex-shrink: 0;
    }

    &:hover {
      background: rgba(255, 255, 255, 0.06);
      color: white;
    }

    &.router-link-active {
      background: $bg-sidebar-active;
      color: $text-sidebar-active;

      .nav-icon {
        color: $primary;
      }
    }
  }
}

.sidebar-footer {
  padding: 12px 10px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);

  .back-link {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 9px 10px;
    border-radius: $radius-sm;
    text-decoration: none;
    color: #475569;
    font-size: 13px;
    transition: all 0.2s ease;
    white-space: nowrap;

    &:hover {
      background: rgba(255, 255, 255, 0.04);
      color: #94A3B8;
    }
  }
}
</style>
