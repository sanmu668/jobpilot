<template>
  <div class="admin-layout" :class="{ 'sidebar-collapsed': appStore.sidebarCollapsed }">
    <Sidebar />
    <Navbar />
    <main class="admin-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'
import { useAppStore } from '@/store/modules/app'

const appStore = useAppStore()
</script>

<style lang="scss" scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: $bg-base;
}

.admin-main {
  margin-left: $sidebar-width;
  margin-top: $navbar-height;
  flex: 1;
  min-height: calc(100vh - #{$navbar-height});
  transition: margin-left 0.3s ease;
}

.admin-layout.sidebar-collapsed {
  .admin-main {
    margin-left: $sidebar-collapsed-width;
  }

  :deep(.admin-navbar) {
    left: $sidebar-collapsed-width;
  }
}
</style>
