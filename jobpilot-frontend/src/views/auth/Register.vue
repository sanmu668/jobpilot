<template>
  <div class="auth-page">
    <div class="auth-bg">
      <div class="bg-orb orb-1"></div>
      <div class="bg-orb orb-2"></div>
    </div>

    <div class="auth-card glass-card">
      <router-link to="/" class="auth-logo">
        <div class="logo-icon">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            <path d="M2 17l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
          </svg>
        </div>
        <span>JobPilot <strong>AI</strong></span>
      </router-link>

      <h1 class="auth-title">创建账号</h1>
      <p class="auth-sub">免费注册，开启 AI 智能求职新体验</p>

      <form class="auth-form" @submit.prevent="handleRegister">
        <div class="form-row">
          <div class="form-group">
            <label>姓名</label>
            <input v-model="form.name" type="text" class="input-field" placeholder="请输入姓名" required />
          </div>
        </div>

        <div class="form-group">
          <label>邮箱</label>
          <input v-model="form.email" type="email" class="input-field" placeholder="your@email.com" required />
        </div>

        <div class="form-group">
          <label>密码</label>
          <div class="input-wrap">
            <input v-model="form.password" :type="showPwd ? 'text' : 'password'" class="input-field" placeholder="至少 8 位密码" required />
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
          <div class="pwd-strength" v-if="form.password">
            <div class="strength-bar">
              <div class="strength-fill" :class="pwdStrength.class" :style="{ width: pwdStrength.width }"></div>
            </div>
            <span :class="pwdStrength.class">{{ pwdStrength.label }}</span>
          </div>
        </div>

        <div class="form-group">
          <label>确认密码</label>
          <input v-model="form.confirmPassword" :type="showPwd ? 'text' : 'password'" class="input-field"
            :class="{ 'error': form.confirmPassword && form.password !== form.confirmPassword }"
            placeholder="再次输入密码" required />
          <p class="field-error" v-if="form.confirmPassword && form.password !== form.confirmPassword">两次密码不一致</p>
        </div>

        <label class="agree-label">
          <input type="checkbox" v-model="form.agree" required />
          <span class="checkmark"></span>
          我已阅读并同意 <a href="#">服务条款</a> 和 <a href="#">隐私政策</a>
        </label>

        <div class="error-msg" v-if="errorMsg">{{ errorMsg }}</div>

        <button type="submit" class="auth-btn" :disabled="loading || !form.agree">
          <span v-if="!loading">免费注册</span>
          <span v-else class="loading-dots">
            <span></span><span></span><span></span>
          </span>
        </button>
      </form>

      <p class="auth-switch">
        已有账号？
        <router-link to="/auth/login">立即登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'
import { register as registerApi, login as loginApi } from '@/api/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const showPwd = ref(false)
const errorMsg = ref('')

const form = reactive({
  name: '',
  email: '',
  password: '',
  confirmPassword: '',
  agree: false
})

const pwdStrength = computed(() => {
  const p = form.password
  if (!p) return { width: '0%', class: '', label: '' }
  if (p.length < 6) return { width: '25%', class: 'weak', label: '弱' }
  if (p.length < 8 || !/[A-Z]/.test(p)) return { width: '50%', class: 'medium', label: '中等' }
  if (/[A-Z]/.test(p) && /[0-9]/.test(p)) return { width: '100%', class: 'strong', label: '强' }
  return { width: '75%', class: 'good', label: '良好' }
})

const handleRegister = async () => {
  if (form.password !== form.confirmPassword) return
  errorMsg.value = ''
  loading.value = true
  try {
    await registerApi({ username: form.name, email: form.email, password: form.password })
    const res = await loginApi({ email: form.email, password: form.password })
    authStore.login(res.token, {
      name: res.username,
      email: form.email,
      role: res.role
    })
    router.push('/upload')
  } catch (err) {
    errorMsg.value = err.message || '注册失败，请稍后重试'
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
    background: radial-gradient(circle, rgba($accent, 0.25), transparent);
    top: -80px;
    right: -80px;
  }

  .orb-2 {
    width: 350px;
    height: 350px;
    background: radial-gradient(circle, rgba($primary, 0.25), transparent);
    bottom: -80px;
    left: -60px;
  }
}

.auth-card {
  width: 100%;
  max-width: 440px;
  padding: 40px;
  position: relative;
  z-index: 1;
}

.auth-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  margin-bottom: 24px;
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
  margin-bottom: 24px;
}

.auth-form {
  .form-group {
    margin-bottom: 16px;

    label {
      display: block;
      font-size: 13px;
      font-weight: 600;
      color: $text-primary;
      margin-bottom: 6px;
    }
  }

  .input-wrap {
    position: relative;

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

    .input-field { padding-right: 36px; }
  }

  .input-field.error {
    border-color: $danger;
    box-shadow: 0 0 0 3px rgba($danger, 0.1);
  }

  .field-error {
    font-size: 12px;
    color: $danger;
    margin-top: 4px;
  }
}

.pwd-strength {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 6px;

  .strength-bar {
    flex: 1;
    height: 4px;
    background: $border;
    border-radius: $radius-full;
    overflow: hidden;
  }

  .strength-fill {
    height: 100%;
    border-radius: $radius-full;
    transition: width 0.3s ease, background 0.3s ease;
  }

  span { font-size: 11px; font-weight: 600; }

  .weak { background: $danger; color: $danger; }
  .medium { background: $warning; color: $warning; }
  .good { background: $primary; color: $primary; }
  .strong { background: $success; color: $success; }
}

.agree-label {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 13px;
  color: $text-secondary;
  cursor: pointer;
  margin-bottom: 20px;
  line-height: 1.5;

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
    margin-top: 1px;
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

  a { color: $primary; &:hover { text-decoration: underline; } }
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

  &:hover:not(:disabled) {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba($primary, 0.45);
  }

  &:disabled { opacity: 0.6; cursor: not-allowed; }
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

.auth-switch {
  text-align: center;
  font-size: 13px;
  color: $text-secondary;
  margin-top: 20px;

  a { color: $primary; font-weight: 600; &:hover { text-decoration: underline; } }
}
</style>
