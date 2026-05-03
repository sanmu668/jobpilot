import { createRouter, createWebHistory } from 'vue-router'
import frontendRoutes from './frontend'
import adminRoutes from './admin'
import { isLoggedIn } from '@/utils/auth'
import { isAdmin } from '@/utils/permission'

const authRoutes = [
  {
    path: '/auth',
    children: [
      {
        path: 'login',
        name: 'Login',
        component: () => import('@/views/auth/Login.vue'),
        meta: { title: '登录' }
      },
      {
        path: 'register',
        name: 'Register',
        component: () => import('@/views/auth/Register.vue'),
        meta: { title: '注册' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: [
    ...frontendRoutes,
    ...adminRoutes,
    ...authRoutes,
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition
    return { top: 0, behavior: 'smooth' }
  }
})

router.beforeEach((to, from, next) => {
  document.title = to.meta?.title || 'JobPilot AI'

  if (to.meta?.requiresAdmin) {
    if (!isLoggedIn() || !isAdmin()) {
      return next('/auth/login')
    }
  }

  if (to.meta?.requiresAuth && !isLoggedIn()) {
    return next('/auth/login')
  }

  next()
})

export default router
