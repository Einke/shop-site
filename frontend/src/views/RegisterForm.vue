<template>
  <div class="register-container">
    <div class="logo-area">
      <img src="@/assets/logo.png" alt="Logo" class="logo">
      <h2 class="title">创建账号</h2>
    </div>
    
    <form @submit.prevent="handleRegister" class="register-form">
      <div class="form-item">
        <label><i class="el-icon-user"></i> 用户名</label>
        <input 
          type="text" 
          v-model="registerForm.username" 
          placeholder="请输入用户名"
        >
        <span class="error-message" v-if="errors.username">{{ errors.username }}</span>
      </div>
      <div class="form-item">
        <label><i class="el-icon-message"></i> 电子邮箱</label>
        <input 
          type="email" 
          v-model="registerForm.email" 
          placeholder="请输入电子邮箱"
        >
        <span class="error-message" v-if="errors.email">{{ errors.email }}</span>
      </div>
      <div class="form-item">
        <label><i class="el-icon-lock"></i> 密码</label>
        <input 
          type="password" 
          v-model="registerForm.password" 
          placeholder="请输入密码"
        >
        <span class="error-message" v-if="errors.password">{{ errors.password }}</span>
      </div>
      <button type="submit" class="register-btn">注册账号</button>
    </form>

    <div class="footer-links">
      <span @click="goToHome" class="home-link">
        <i class="el-icon-back"></i> 回到首页
      </span>
      <a href="/login" class="login-link">
        已有账号？立即登录 <i class="el-icon-right"></i>
      </a>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
  
const router = useRouter()

const registerForm = reactive({
  username: '',
  email: '',
  password: ''
})

const errors = reactive({
  username: '',
  email: '',
  password: ''
})

const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

const validateForm = () => {
  let isValid = true
  errors.username = ''
  errors.email = ''
  errors.password = ''

  if (!registerForm.username.trim()) {
    errors.username = '用户名不能为空'
    isValid = false
  }

  if (!registerForm.email.trim() || !validateEmail(registerForm.email)) {
    errors.email = '请输入有效的电子邮箱'
    isValid = false
  }

  if (!registerForm.password.trim() || registerForm.password.length < 6) {
    errors.password = '密码不能少于六位'
    isValid = false
  }

  return isValid
}

const handleRegister = () => {
  if(validateForm()){
    request.post('/register', registerForm).then(response => {
      ElMessage.success('注册成功！')
      window.location.href = '/login' // 跳转到登录界面
    })
  }
}

const goToHome = () => {
  router.push('/')
}
</script>

<style scoped>
.register-container {
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

.title {
  color: #2c3e50;
  font-size: 24px;
  font-weight: 500;
  margin: 0;
}

.register-form {
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

.register-btn {
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

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.register-btn:active {
  transform: translateY(0);
}

.footer-links {
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.login-link, .home-link {
  color: #409EFF;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s;
  text-decoration: none;
}

.login-link:hover, .home-link:hover {
  color: #3178c6;
  transform: translateX(4px);
}

/* 响应式设计 */
@media (max-width: 480px) {
  .register-container {
    margin: 20px auto;
    padding: 30px 20px;
    border-radius: 12px;
  }

  .logo {
    width: 60px;
    height: 60px;
  }

  .title {
    font-size: 20px;
  }

  .form-item input {
    padding: 10px 14px;
  }
}
</style>