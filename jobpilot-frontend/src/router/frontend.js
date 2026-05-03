import FrontLayout from '@/layout/frontend/FrontLayout.vue'

export default [
  {
    path: '/',
    component: FrontLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/frontend/Home.vue'),
        meta: { title: 'JobPilot AI — 智能求职平台' }
      },
      {
        path: 'upload',
        name: 'ResumeUpload',
        component: () => import('@/views/frontend/ResumeUpload.vue'),
        meta: { title: '上传简历', requiresAuth: true }
      },
      {
        path: 'analysis',
        name: 'Analysis',
        component: () => import('@/views/frontend/Analysis.vue'),
        meta: { title: '匹配分析', requiresAuth: true }
      },
      {
        path: 'optimize',
        name: 'Optimize',
        component: () => import('@/views/frontend/Optimize.vue'),
        meta: { title: '简历优化', requiresAuth: true }
      },
      {
        path: 'jobs',
        name: 'JobRecommend',
        component: () => import('@/views/frontend/JobRecommend.vue'),
        meta: { title: '推荐岗位', requiresAuth: true }
      }
    ]
  }
]
