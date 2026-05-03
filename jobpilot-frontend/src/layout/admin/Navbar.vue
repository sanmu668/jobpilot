<template>
  <header class="admin-navbar">
    <div class="navbar-left">
      <button class="toggle-btn" @click="appStore.toggleSidebar" title="折叠菜单">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="18" x2="21" y2="18"/>
        </svg>
      </button>

      <div class="breadcrumb">
        <span class="breadcrumb-home">管理后台</span>
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg>
        <span class="breadcrumb-current">{{ currentPageTitle }}</span>
      </div>
    </div>

    <div class="navbar-right">
      <div class="search-bar">
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input type="text" placeholder="搜索..." />
      </div>

      <button class="icon-btn" title="通知">
        <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 01-3.46 0"/>
        </svg>
        <span class="dot"></span>
      </button>

      <div class="admin-user">
        <div class="avatar">{{ authStore.userName?.charAt(0)?.toUpperCase() }}</div>
        <div class="user-info">
          <span class="name">{{ authStore.userName }}</span>
          <span class="role">管理员</span>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/modules/app'
import { useAuthStore } from '@/store/modules/auth'

const route = useRoute()
const appStore = useAppStore()
const authStore = useAuthStore()

const currentPageTitle = computed(() => route.meta?.title || '页面')
</script>

<style lang="scss" scoped>
.admin-navbar {
  position: fixed;
  top: 0;
  right: 0;
  left: $sidebar-width;
  height: $navbar-height;
  background: white;
  border-bottom: 1px solid $border-light;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  z-index: 40;
  transition: left 0.3s ease;
  box-shadow: 0 1px 0 $border-light;
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.toggle-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  background: transparent;
  border: none;
  border-radius: $radius-sm;
  color: $text-secondary;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background: $bg-base;
    color: $text-primary;
  }
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;

  .breadcrumb-home {
    color: $text-muted;
  }

  svg {
    color: $text-muted;
  }

  .breadcrumb-current {
    color: $text-primary;
    font-weight: 600;
  }
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  background: $bg-base;
  border: 1px solid $border;
  border-radius: $radius-md;
  padding: 7px 14px;

  svg { color: $text-muted; }

  input {
    background: none;
    border: none;
    outline: none;
    font-size: 13px;
    color: $text-primary;
    width: 160px;
    &::placeholder { color: $text-muted; }
  }
}

.icon-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  background: transparent;
  border: none;
  border-radius: $radius-sm;
  color: $text-secondary;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover { background: $bg-base; color: $text-primary; }

  .dot {
    position: absolute;
    top: 6px;
    right: 6px;
    width: 7px;
    height: 7px;
    background: $danger;
    border-radius: 50%;
    border: 1.5px solid white;
  }
}

.admin-user {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 10px;
  border-radius: $radius-md;
  cursor: pointer;
  transition: background 0.2s ease;

  &:hover { background: $bg-base; }

  .avatar {
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

  .user-info {
    display: flex;
    flex-direction: column;

    .name {
      font-size: 13px;
      font-weight: 600;
      color: $text-primary;
      line-height: 1.3;
    }

    .role {
      font-size: 11px;
      color: $primary;
      font-weight: 500;
    }
  }
}
</style>
