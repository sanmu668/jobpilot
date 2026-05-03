<template>
  <div class="auth-page">
    <div class="auth-bg">
      <div class="bg-orb orb-1"></div>
      <div class="bg-orb orb-2"></div>
      <div class="bg-orb orb-3"></div>
    </div>

    <div class="auth-card glass-card">
      <router-link to="/" class="auth-logo">
        <div class="logo-icon">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            <path d="M2 17l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            <path d="M2 12l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
          </svg>
        </div>
        <span>JobPilot <strong>AI</strong></span>
      </router-link>

      <h1 class="auth-title">欢迎回来</h1>
      <p class="auth-sub">登录账号，继续您的 AI 求职之旅</p>

      <form class="auth-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label>邮箱</label>
          <div class="input-wrap">
            <svg class="input-icon" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
              <polyline points="22,6 12,13 2,6"/>
            </svg>
            <input v-model="form.email" type="email" class="input-field" placeholder="your@email.com" required />
          </div>
        </div>

        <div class="form-group">
          <label>
            密码
            <a href="#" class="forgot-link">忘记密码？</a>
          </label>
          <div class="input-wrap">
            <svg class="input-icon" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0110 0v4"/>
            </svg>
            <input v-model="form.password" :type="showPwd ? 'text' : 'password'" class="input-field" placeholder="请输入密码" required />
            <button type="button" class="toggle-pwd" @click="showPwd = !showPwd">
              <svg v-if="!showPwd" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
              </svg>
              <svg v-else width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19m-6.72-1.07a3 3 0 11-4.24-4.24"/>
                <line x1="1" y1="1" x2="23" y2="23"/>
              </svg>
            </button>
          </div>
        </div>

        <div class="remember-row">
          <label class="checkbox-label">
            <input type="checkbox" v-model="form.remember" />
            <span class="checkmark"></span>
            记住我
          </label>
        </div>

        <div class="error-msg" v-if="errorMsg">{{ errorMsg }}</div>

        <button type="submit" class="auth-btn" :disabled="loading">
          <span v-if="!loading">登录</span>
          <span v-else class="loading-dots">
            <span></span><span></span><span></span>
          </span>
        </button>

        <div class="auth-divider"><span>或者</span></div>

        <button type="button" class="demo-btn" @click="demoLogin">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polygon points="5 3 19 12 5 21 5 3"/>
          </svg>
          体验演示账号
        </button>
      </form>

      <p class="auth-switch">
        还没有账号？
        <router-link to="/auth/register">免费注册</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'
import { login as loginApi } from '@/api/auth'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const showPwd = ref(false)
const errorMsg = ref('')
const form = reactive({
  email: '',
  password: '',
  remember: false
})

const handleLogin = async () => {
  errorMsg.value = ''
  loading.value = true
  try {
    const res = await loginApi({ email: form.email, password: form.password })
    authStore.login(res.token, {
      name: res.username,
      email: form.email,
      role: res.role
    })
    router.push(res.role === 'admin' ? '/admin/dashboard' : '/upload')
  } catch (err) {
    errorMsg.value = err.message || '登录失败，请检查邮箱和密码'
  } finally {
    loading.value = false
  }
}

const demoLogin = async () => {
  errorMsg.value = ''
  loading.value = true
  try {
    const res = await loginApi({ email: 'test@qq.com', password: '123456' })
    authStore.login(res.token, {
      name: res.username,
      email: 'test@qq.com',
      role: res.role
    })
    router.push(res.role === 'admin' ? '/admin/dashboard' : '/upload')
  } catch (err) {
    errorMsg.value = err.message || '演示账号登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #EAF4FF 0%, #F7FBFF 50%, #EEE8FF 100%);
  position: relative;
  overflow: hidden;
  padding: 24px;
}

.auth-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;

  .bg-orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(60px);
    opacity: 0.4;
  }

  .orb-1 {
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba($primary, 0.3), transparent);
    top: -100px;
    left: -100px;
  }

  .orb-2 {
    width: 350px;
    height: 350px;
    background: radial-gradient(circle, rgba($accent, 0.2), transparent);
    bottom: -80px;
    right: -80px;
  }

  .orb-3 {
    width: 200px;
    height: 200px;
    background: radial-gradient(circle, rgba($primary-light, 0.3), transparent);
    top: 40%;
    right: 20%;
  }
}

.auth-card {
  width: 100%;
  max-width: 420px;
  padding: 40px;
  position: relative;
  z-index: 1;
}

.auth-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  margin-bottom: 28px;
  color: $text-primary;
  font-size: 16px;

  .logo-icon {
    width: 34px;
    height: 34px;
    background: linear-gradient(135deg, $primary, $accent);
    border-radius: 9px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
  }

  strong {
    background: linear-gradient(135deg, $primary, $accent);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
}

.auth-title {
  font-size: 26px;
  font-weight: 800;
  color: $text-primary;
  margin-bottom: 6px;
}

.auth-sub {
  font-size: 14px;
  color: $text-secondary;
  margin-bottom: 28px;
}

.auth-form {
  .form-group {
    margin-bottom: 18px;

    label {
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 13px;
      font-weight: 600;
      color: $text-primary;
      margin-bottom: 6px;
    }

    .forgot-link {
      font-size: 12px;
      color: $primary;
      font-weight: 500;
      &:hover { text-decoration: underline; }
    }
  }

  .input-wrap {
    position: relative;

    .input-icon {
      position: absolute;
      left: 12px;
      top: 50%;
      transform: translateY(-50%);
      color: $text-muted;
    }

    .input-field {
      padding-left: 36px;
      padding-right: 36px;
    }

    .toggle-pwd {
      position: absolute;
      right: 12px;
      top: 50%;
      transform: translateY(-50%);
      background: none;
      border: none;
      color: $text-muted;
      cursor: pointer;
      padding: 0;
      display: flex;
      &:hover { color: $text-secondary; }
    }
  }
}

.remember-row {
  margin-bottom: 20px;

  .checkbox-label {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 13px;
    color: $text-secondary;
    cursor: pointer;

    input {
      display: none;

      &:checked + .checkmark {
        background: $primary;
        border-color: $primary;

        &::after { display: block; }
      }
    }

    .checkmark {
      width: 16px;
      height: 16px;
      border: 1.5px solid $border;
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      position: relative;
      transition: all 0.15s ease;

      &::after {
        content: '';
        display: none;
        width: 4px;
        height: 7px;
        border: 2px solid white;
        border-top: none;
        border-left: none;
        transform: rotate(45deg) translateY(-1px);
      }
    }
  }
}

.error-msg {
  background: rgba($danger, 0.08);
  border: 1px solid rgba($danger, 0.25);
  color: $danger;
  font-size: 13px;
  border-radius: $radius-md;
  padding: 10px 12px;
  margin-bottom: 14px;
}

.auth-btn {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, $primary, $primary-dark);
  color: white;
  border: none;
  border-radius: $radius-md;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s ease;
  box-shadow: 0 4px 14px rgba($primary, 0.35);
  margin-bottom: 16px;

  &:hover:not(:disabled) {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba($primary, 0.45);
  }

  &:disabled { opacity: 0.7; cursor: not-allowed; }
}

.loading-dots {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;

  span {
    width: 6px;
    height: 6px;
    background: white;
    border-radius: 50%;
    animation: dot-bounce 0.6s infinite alternate;

    &:nth-child(2) { animation-delay: 0.15s; }
    &:nth-child(3) { animation-delay: 0.3s; }
  }
}

@keyframes dot-bounce {
  from { transform: translateY(0); opacity: 1; }
  to { transform: translateY(-6px); opacity: 0.4; }
}

.auth-divider {
  text-align: center;
  position: relative;
  margin-bottom: 16px;

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    height: 1px;
    background: $border;
  }

  span {
    position: relative;
    background: white;
    padding: 0 12px;
    font-size: 12px;
    color: $text-muted;
  }
}

.demo-btn {
  width: 100%;
  padding: 11px;
  background: rgba($primary, 0.06);
  color: $primary;
  border: 1px solid rgba($primary, 0.2);
  border-radius: $radius-md;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  &:hover {
    background: rgba($primary, 0.1);
    border-color: $primary;
  }
}

.auth-switch {
  text-align: center;
  font-size: 13px;
  color: $text-secondary;
  margin-top: 20px;

  a {
    color: $primary;
    font-weight: 600;
    &:hover { text-decoration: underline; }
  }
}
</style>
