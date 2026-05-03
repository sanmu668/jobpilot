import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebarCollapsed: false,
    loading: false,
    theme: 'light',
    notifications: []
  }),

  actions: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    setLoading(val) {
      this.loading = val
    },
    addNotification(notification) {
      this.notifications.unshift({ id: Date.now(), ...notification })
      setTimeout(() => this.removeNotification(notification.id), 4000)
    },
    removeNotification(id) {
      this.notifications = this.notifications.filter(n => n.id !== id)
    }
  }
})
