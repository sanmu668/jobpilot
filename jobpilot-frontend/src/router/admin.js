import AdminLayout from '@/layout/admin/AdminLayout.vue'

export default [
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '控制台', icon: 'dashboard' }
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('@/views/admin/UserManage.vue'),
        meta: { title: '用户管理', icon: 'users' }
      },
      {
        path: 'resumes',
        name: 'ResumeManage',
        component: () => import('@/views/admin/ResumeManage.vue'),
        meta: { title: '简历管理', icon: 'resume' }
      },
      {
        path: 'jobs',
        name: 'JobManage',
        component: () => import('@/views/admin/JobManage.vue'),
        meta: { title: '岗位管理', icon: 'job' }
      },
      {
        path: 'matches',
        name: 'MatchManage',
        component: () => import('@/views/admin/MatchManage.vue'),
        meta: { title: '匹配管理', icon: 'match' }
      },
      {
        path: 'ai-logs',
        name: 'AiLog',
        component: () => import('@/views/admin/AiLog.vue'),
        meta: { title: 'AI 日志', icon: 'log' }
      },
      {
        path: 'system',
        name: 'SystemConfig',
        component: () => import('@/views/admin/SystemConfig.vue'),
        meta: { title: '系统配置', icon: 'system' }
      }
    ]
  }
]
