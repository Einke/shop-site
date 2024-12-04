<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">个人信息</span>
        </div>
      </template>
      
      <el-descriptions
        :column="1"
        :size="size"
        border
        class="info-descriptions"
      >
        <el-descriptions-item label="用户名" label-class-name="my-label">
          <div class="info-value">{{userInfo?.username}}</div>
        </el-descriptions-item>
        <el-descriptions-item label="密码" label-class-name="my-label">
          <div class="info-value">{{userInfo?.password}}</div>
        </el-descriptions-item>
        <el-descriptions-item label="电子邮箱" label-class-name="my-label">
          <div class="info-value">{{userInfo?.email}}</div>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" label-class-name="my-label">
          <div class="info-value">{{userInfo?.createTime ? userInfo.createTime.replace('T',' '):'-'}}</div>
        </el-descriptions-item>
      </el-descriptions>

      <div class="action-buttons">
        <el-button @click="handleBack" class="action-btn">
          <el-icon><ArrowLeft /></el-icon>返回
        </el-button>
        <el-button type="primary" @click="dialogFormVisible = true" class="action-btn">
          <el-icon><Edit /></el-icon>修改信息
        </el-button>
        <el-button type="danger" @click="logout" class="action-btn">
          <el-icon><SwitchButton /></el-icon>退出登录
        </el-button>
        <el-button type="warning" @click="deleteAccount" class="action-btn">
          <el-icon><Delete /></el-icon>注销账号
        </el-button>
      </div>
    </el-card>
  </div>

  <el-dialog 
    title="修改个人信息" 
    v-model="dialogFormVisible" 
    width="500px"
    class="edit-dialog"
  >
    <el-form :model="form" class="edit-form">
      <el-form-item label="新密码" class="form-item">
        <el-input 
          v-model="form.newPassword" 
          type="password" 
          show-password 
          placeholder="请输入新密码"
        ></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" class="form-item">
        <el-input 
          v-model="form.confirmPassword" 
          type="password" 
          show-password 
          placeholder="请确认新密码"
        ></el-input>
      </el-form-item>
      <el-form-item label="电子邮箱" class="form-item">
        <el-input 
          v-model="form.email" 
          placeholder="请输入新的电子邮箱"
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitChanges">确认更改</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted ,computed} from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Edit, SwitchButton, Delete, UserFilled, ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import request from '@/utils/request'

const router = useRouter()
const store = useStore()

const userInfo = computed(() => store.state.userInfo)

// 模拟用户数据
const dialogFormVisible = ref(false)
const form = ref({
  newPassword: '',
  confirmPassword: '',
  email: userInfo.value.email
})

const handleBack = () => {
  if(window.history.length > 1){
    router.back()
  }else{
    router.push('/')
  }
}

// 提交更改
const submitChanges = () => {
  if (form.value.newPassword.length < 6) {
    ElMessage.error('密码长度不能小于6位')
    return
  }
  if (form.value.newPassword !== form.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!re.test(form.value.email)) {
    ElMessage.error('电子邮箱格式不正确')
    return
  }

  request.put('/user', {
    id: userInfo.value.id,
    password: form.value.newPassword,
    email: form.value.email
  }).then(response => {
    ElMessage.success('信息修改成功')
    dialogFormVisible.value = false
    store.dispatch('getUserInfo')
  }).catch(error => {
    ElMessage.error('信息修改失败')
  })
}

// 退出登录
const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  }).then(() => {
    store.commit('SET_USER_ID', null)
    store.commit('SET_USER_INFO', null)
    localStorage.removeItem('token')
    router.push('/login')
  }).catch(() => {
    ElMessage.info('取消退出登录')
  })
}

// 注销账号
const deleteAccount = () => {
  ElMessageBox.confirm('确定要注销账号吗？', '警告', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  }).then(() => {
    request.delete(`/user/${userInfo.value.id}`).then(response => { 
      store.commit('SET_USER_ID', null)
      store.commit('SET_USER_INFO', null)
      localStorage.removeItem('token')
      router.push('/login')
      ElMessage.success('账号注销成功')
    })
  }).catch(() => {
    ElMessage.info('取消注销账号')
  })
}

onMounted(() => {
  store.dispatch('getUserInfo')
})
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.profile-card {
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  background: white;
  transition: all 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  position: relative;
}

.header-title::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, #409EFF, transparent);
}

.info-descriptions {
  margin-top: 24px;
}

.info-value {
  color: #606266;
  font-size: 14px;
  padding: 4px 0;
}

:deep(.my-label) {
  background: linear-gradient(135deg, #ecf5ff 0%, #f5f7fa 100%) !important;
  font-weight: 600 !important;
  color: #409EFF !important;
}

.action-buttons {
  margin-top: 32px;
  display: flex;
  gap: 16px;
  justify-content: center;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border-radius: 8px;
  transition: all 0.3s;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.edit-dialog {
  :deep(.el-dialog) {
    border-radius: 16px;
    overflow: hidden;
  }
  
  :deep(.el-dialog__header) {
    margin: 0;
    padding: 20px;
    background: #f8f9fa;
    border-bottom: 1px solid #ebeef5;
  }
  
  :deep(.el-dialog__body) {
    padding: 24px;
  }
}

.edit-form {
  .form-item {
    margin-bottom: 24px;
    
    :deep(.el-input__wrapper) {
      border-radius: 8px;
    }
    
    :deep(.el-input__inner) {
      height: 40px;
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-container {
    padding: 0 10px;
  }
  
  .action-buttons {
    flex-wrap: wrap;
  }
  
  .action-btn {
    flex: 1 1 calc(50% - 8px);
    min-width: 120px;
  }
}
</style>