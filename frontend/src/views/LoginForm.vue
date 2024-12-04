<template>
  <div class="login-container">
    <div class="logo-area">
      <img src="@/assets/logo.png" alt="Logo" class="logo">
      <h2>欢迎登录</h2>
    </div>
    <form @submit.prevent="handleLogin" class="login-form">
      <div class="form-item">
        <label><i class="el-icon-user"></i>用户名</label>
        <input 
          type="text" 
          v-model="loginForm.username" 
          placeholder="请输入用户名"
        >
        <span class="error-message" v-if="errors.username">{{ errors.username }}</span>
      </div>
      <div class="form-item">
        <label><i class="el-icon-lock"></i>密码</label>
        <input 
          type="password" 
          v-model="loginForm.password" 
          placeholder="请输入密码"
        >
        <span class="error-message" v-if="errors.password">{{ errors.password }}</span>
      </div>
      <button type="submit" class="login-btn">登 录</button>
    </form>
    <div class="footer-links">
      <span @click="goToHome" class="home-link">
        <i class="el-icon-back"></i> 回到首页
      </span>
      <a href="/register" class="register-link">
        没有账号？立即注册 <i class="el-icon-right"></i>
      </a>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { jwtDecode } from 'jwt-decode'
import { useStore } from 'vuex'
import request from '@/utils/request'

const router = useRouter()
const store = useStore()

const loginForm = reactive({
  username: '',
  password: ''
})

const errors = reactive({
  username: '',
  password: ''
})

const validateForm = () => {
  let isValid = true
  // 重置错误信息
  errors.username = ''
  errors.password = ''

  if (!loginForm.username.trim()) {
    errors.username = '用户名不能为空'
    isValid = false
  }

  if (!loginForm.password.trim()) {
    errors.password = '密码不能为空'
    isValid = false
  }

  return isValid
}

const handleLogin = () => {
  if(validateForm()){
    request.post('/login', loginForm).then(response => {
      localStorage.setItem('token', response.data)
      const decoded = jwtDecode(response.data)
      store.commit('SET_USER_ID', decoded.id)
      if (loginForm.username === 'admin') {
        router.push('/admin')
      } else {
        router.push('/')
      }
      console.log('登录成功！', loginForm.username)
      ElMessage.success('登录成功！')
    })
  }
}

const goToHome = () => {
  router.push('/')
}

</script>

<style scoped>
.login-container {
  max-width: 420px;
  margin: 60px auto;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  background: white;
}

.logo-area {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 80px;
  height: 80px;
  margin-bottom: 16px;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-area h2 {
  color: #2c3e50;
  font-size: 24px;
  font-weight: 500;
  margin: 0;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-item label {
  font-weight: 500;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 8px;
}

.form-item input {
  padding: 12px 16px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
  background-color: #f8fafc;
}

.form-item input:focus {
  border-color: #409EFF;
  background-color: white;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
  outline: none;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}

.login-btn {
  background: linear-gradient(135deg, #409EFF 0%, #3178c6 100%);
  color: white;
  padding: 12px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s;
  margin-top: 8px;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.login-btn:active {
  transform: translateY(0);
}

.footer-links {
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.register-link, .home-link {
  color: #409EFF;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s;
  text-decoration: none;
}

.register-link:hover, .home-link:hover {
  color: #3178c6;
  transform: translateX(4px);
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-container {
    margin: 20px auto;
    padding: 30px 20px;
    border-radius: 12px;
  }

  .logo {
    width: 60px;
    height: 60px;
  }

  .logo-area h2 {
    font-size: 20px;
  }
}
</style>
