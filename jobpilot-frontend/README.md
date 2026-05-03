# JobPilot 前端

> AI 驱动的智能求职平台前端项目，基于 Vue 3 + Vite 构建。

---

## 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能模块](#功能模块)
- [快速开始](#快速开始)
- [环境变量](#环境变量)
- [路由设计](#路由设计)
- [状态管理](#状态管理)
- [API 封装](#api-封装)

---

## 项目简介

JobPilot 是一个 AI 驱动的智能求职平台，帮助求职者上传简历、获取岗位匹配分析、智能优化简历内容，并推荐合适的职位。平台同时提供完整的管理后台，供管理员对用户、简历、岗位、匹配记录及 AI 调用日志进行统一管理。

---

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | Vue 3 (Composition API + `<script setup>`) |
| 构建工具 | Vite 5 |
| 路由 | Vue Router 4 |
| 状态管理 | Pinia 3 |
| HTTP 请求 | Axios 1 |
| 数据可视化 | ECharts 6 |
| 样式 | Sass / SCSS |

---

## 项目结构

```
jobpilot-frontend/
├── public/                    # 静态资源
├── src/
│   ├── api/                   # 接口封装
│   │   ├── admin/             # 管理端接口
│   │   │   ├── log.js         # AI 日志接口
│   │   │   ├── system.js      # 系统配置接口
│   │   │   └── user.js        # 用户管理接口
│   │   └── frontend/          # 用户端接口
│   │       ├── job.js         # 岗位接口
│   │       ├── match.js       # 匹配接口
│   │       └── resume.js      # 简历接口
│   ├── layout/                # 布局组件
│   │   ├── admin/             # 管理后台布局
│   │   │   ├── AdminLayout.vue
│   │   │   ├── Navbar.vue
│   │   │   └── Sidebar.vue
│   │   └── frontend/          # 用户端布局
│   │       └── FrontLayout.vue
│   ├── router/                # 路由配置
│   │   ├── index.js           # 路由入口 & 导航守卫
│   │   ├── admin.js           # 管理端路由
│   │   └── frontend.js        # 用户端路由
│   ├── store/                 # Pinia 状态
│   │   └── modules/
│   │       ├── app.js         # 全局应用状态
│   │       ├── auth.js        # 认证状态
│   │       └── user.js        # 用户信息状态
│   ├── styles/                # 全局样式
│   │   ├── index.scss         # 样式入口
│   │   └── theme.scss         # 主题变量
│   ├── utils/                 # 工具函数
│   │   ├── auth.js            # Token 读写工具
│   │   ├── permission.js      # 路由权限控制
│   │   └── request.js         # Axios 实例 & 拦截器
│   ├── views/                 # 页面组件
│   │   ├── auth/
│   │   │   ├── Login.vue      # 登录页
│   │   │   └── Register.vue   # 注册页
│   │   ├── admin/             # 管理后台页面
│   │   │   ├── Dashboard.vue  # 控制台
│   │   │   ├── UserManage.vue # 用户管理
│   │   │   ├── ResumeManage.vue # 简历管理
│   │   │   ├── JobManage.vue  # 岗位管理
│   │   │   ├── MatchManage.vue # 匹配管理
│   │   │   ├── AiLog.vue      # AI 日志
│   │   │   └── SystemConfig.vue # 系统配置
│   │   └── frontend/          # 用户端页面
│   │       ├── Home.vue       # 首页
│   │       ├── ResumeUpload.vue # 简历上传
│   │       ├── Analysis.vue   # 匹配分析
│   │       ├── Optimize.vue   # 简历优化
│   │       └── JobRecommend.vue # 岗位推荐
│   ├── App.vue
│   └── main.js
├── package.json
└── vite.config.js
```

---

## 功能模块

### 用户端

| 页面 | 路径 | 说明 |
|------|------|------|
| 首页 | `/` | 平台介绍、功能入口、快速开始引导 |
| 简历上传 | `/upload` | 支持 PDF/Word 上传，解析简历内容 |
| 匹配分析 | `/analysis` | AI 分析简历与岗位匹配度，可视化评分 |
| 简历优化 | `/optimize` | AI 生成优化建议，针对目标岗位改写 |
| 岗位推荐 | `/jobs` | 根据简历智能推荐匹配岗位列表 |

> 以上页面（首页除外）需要登录后访问，未登录会自动跳转至登录页。

### 管理后台

| 页面 | 路径 | 说明 |
|------|------|------|
| 控制台 | `/admin/dashboard` | 平台数据概览、图表统计 |
| 用户管理 | `/admin/users` | 查看/封禁/删除用户，角色管理 |
| 简历管理 | `/admin/resumes` | 查看所有用户简历及解析状态 |
| 岗位管理 | `/admin/jobs` | 维护岗位数据库，上下架管理 |
| 匹配管理 | `/admin/matches` | 查看匹配记录，匹配度分布统计 |
| AI 日志 | `/admin/ai-logs` | AI 调用记录，含 Token 用量、耗时及错误详情 |
| 系统配置 | `/admin/system` | 基础设置、AI 参数、存储方式、邮件通知 |

> 管理后台需要管理员权限，普通用户访问会被重定向。

---

## 快速开始

**前置要求：** Node.js >= 18

```bash
# 1. 安装依赖
npm install

# 2. 启动开发服务器（默认 http://localhost:5173）
npm run dev

# 3. 构建生产包
npm run build

# 4. 预览生产包
npm run preview
```

---

## 环境变量

在项目根目录创建 `.env.local` 文件（不会被提交到 Git）：

```env
# 后端 API 基础地址
VITE_API_BASE_URL=http://localhost:8080/api
```

`src/utils/request.js` 中的 Axios 实例会自动读取该变量作为 `baseURL`。

---

## 路由设计

路由分为三个部分：

```
/               → 用户端（FrontLayout）
  /upload       → 简历上传（requiresAuth）
  /analysis     → 匹配分析（requiresAuth）
  /optimize     → 简历优化（requiresAuth）
  /jobs         → 岗位推荐（requiresAuth）

/login          → 登录页
/register       → 注册页

/admin          → 管理后台（AdminLayout，requiresAdmin）
  /admin/dashboard
  /admin/users
  /admin/resumes
  /admin/jobs
  /admin/matches
  /admin/ai-logs
  /admin/system
```

导航守卫在 `src/utils/permission.js` 中实现，根据 `meta.requiresAuth` 和 `meta.requiresAdmin` 进行访问控制。

---

## 状态管理

使用 Pinia 管理全局状态，分为三个 Store 模块：

| 模块 | 文件 | 职责 |
|------|------|------|
| `auth` | `store/modules/auth.js` | 登录/登出、Token 持久化 |
| `user` | `store/modules/user.js` | 当前用户信息（名称、角色、头像等） |
| `app` | `store/modules/app.js` | 全局 UI 状态（侧边栏折叠、加载状态等） |

---

## API 封装

所有接口调用统一通过 `src/utils/request.js` 的 Axios 实例发出，请求拦截器自动附加 `Authorization: Bearer <token>` 头；响应拦截器统一处理 401（自动登出）和业务错误提示。

接口按端进行模块化组织：

```js
// 示例：src/api/frontend/resume.js
import request from '@/utils/request'

export const uploadResume = (formData) => request.post('/resume/upload', formData)
export const getResumeList = () => request.get('/resume/list')
export const analyzeResume = (id) => request.post(`/resume/${id}/analyze`)
```
