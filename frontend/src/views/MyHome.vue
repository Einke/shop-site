<template>
  <div class="home">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="logo" @click="goHome" style="cursor: pointer">
        <el-icon class="logo-icon"><Shop /></el-icon>
        <span class="logo-text">购物系统</span>
      </div>
      <div class="nav-right">
        <template v-if="isLoggedIn">
          <el-dropdown @command="handleCommand" class="user-dropdown">
            <span class="user-info">
              <el-avatar :size="32" class="user-avatar">
                {{ userInfo?.username?.charAt(0)?.toUpperCase() || 'U' }}
              </el-avatar>
              <span class="username">{{ userInfo?.username || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" @click="goToLogin" class="login-btn">登录</el-button>
        </template>
      </div>
    </el-header>

    <!-- 主要内容区 -->
    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu
          :default-active="activeMenu"
          class="side-menu"
          router
          :collapse="isCollapse"
          background-color="#ffffff"
          text-color="#2c3e50"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/">
            <el-icon><House /></el-icon>
            <template #title>
              <span>首页</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/shop">
            <el-icon><Goods /></el-icon>
            <template #title>
              <span>商品列表</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/car">
            <el-icon><ShoppingCart /></el-icon>
            <template #title>
              <span>我的购物车</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/orders">
            <el-icon><List /></el-icon>
            <template #title>
              <span>我的订单</span>
            </template>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 内容区 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted ,computed ,watch} from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { Shop, House, Goods, ShoppingCart ,List, ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const store = useStore()

const isLoggedIn = ref(!!localStorage.getItem('token'))
const userInfo = computed(() => store.state.userInfo)
const activeMenu = ref(router.currentRoute.value.path)

// 添加折叠控制
const isCollapse = ref(false)

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    // 清除 token
    localStorage.removeItem('token')
    store.commit('SET_USER_ID', null)
    store.commit('SET_USER_INFO', null)
    ElMessage.success('已退出登录')
    router.push('/login')
  } else if (command === 'profile') {
    router.push('/profile')
  }
}

const goToLogin = () => {
  router.push('/login')
}

const goHome = () => {
  router.push('/')
  activeMenu.value = '/'
}

// 添加路由监听，确保路由变化时菜单高亮同步更新
watch(
  () => router.currentRoute.value.path,
  (newPath) => {
    activeMenu.value = newPath
  }
)

onMounted(() => {
  if(isLoggedIn.value){
    store.dispatch('getUserInfo')
  }
})
</script>

<style scoped>
.home {
  top:0;
  height: 97vh;/*100vh会有滚轮？?*/
  display: flex;
  flex-direction: column;
}

.header {
  background: linear-gradient(135deg, #409EFF 0%, #3178c6 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  position: relative;
  z-index: 1000;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  font-size: 24px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  background: linear-gradient(120deg, #ffffff 0%, #e0e0e0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  padding: 5px 10px;
  border-radius: 20px;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.1);
}

.user-avatar {
  background: linear-gradient(120deg, #e0c3fc 0%, #8ec5fc 100%);
  border: 2px solid rgba(255, 255, 255, 0.8);
}

.username {
  font-weight: 500;
}

.login-btn {
  padding: 8px 20px;
  border-radius: 20px;
  background: linear-gradient(120deg, #ffffff 0%, #e0e0e0 100%);
  color: #409EFF;
  border: none;
  font-weight: 500;
  transition: all 0.3s;
}

.login-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.main-container {
  flex: 1;
  overflow: hidden;
}

.side-menu {
  height: 97%;
  border-right: none;
  box-shadow: 2px 0 8px rgba(0,0,0,0.05);
  transition: all 0.3s;
}

:deep(.el-menu) {
  border-right: none !important;
}

:deep(.el-menu-item) {
  height: 56px;
  line-height: 56px;
  margin: 8px 0;
  border-radius: 0 24px 24px 0;
  margin-right: 16px;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(64,158,255,0.1) 0%, rgba(64,158,255,0) 100%);
  border-left: 3px solid #409EFF;
  color: #409EFF;
  font-weight: 500;
}

:deep(.el-menu-item:hover) {
  background: linear-gradient(90deg, rgba(64,158,255,0.05) 0%, rgba(64,158,255,0) 100%);
  color: #409EFF;
}

:deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 8px;
  transition: all 0.3s;
}

:deep(.el-menu-item:hover .el-icon) {
  transform: translateX(2px);
}

.el-main {
  background-color: #f5f7fa;
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .logo-text,
  .username {
    display: none;
  }
  
  .user-info {
    padding: 5px;
  }
  
  .el-aside {
    width: 64px !important;
  }
  
  :deep(.el-menu-item) {
    padding: 0 20px !important;
    margin-right: 0;
  }
  
  :deep(.el-menu-item .el-icon) {
    margin-right: 0;
  }
  
  :deep(.el-menu--collapse) {
    width: 64px;
  }
  
  :deep(.el-menu-item span) {
    display: none;
  }
}
</style>