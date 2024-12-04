<template>
  <div class="admin-container">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="logo" @click="router.push('/')">
        <el-icon class="logo-icon"><Shop /></el-icon>
        <span class="logo-text">购物系统</span>
        <el-divider direction="vertical" class="divider" />
        <span class="sub-title">后台管理系统</span>
      </div>
      <div class="nav-right">
        <el-dropdown>
          <span class="admin-info">
            <span class="admin-name">管理员</span>
            <el-icon><CaretBottom /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleLogout">
                <el-icon><SwitchButton /></el-icon>
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 主要内容区 -->
    <div class="main-content">
      <el-tabs 
        v-model="activeTab" 
        class="custom-tabs" 
        type="border-card"
      >
        <!-- 用户管理 -->
        <el-tab-pane name="users">
          <template #label>
            <div class="tab-label">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </div>
          </template>
          <UserManagement v-if="activeTab === 'users'" />
        </el-tab-pane>

        <!-- 商品管理 -->
        <el-tab-pane name="products">
          <template #label>
            <div class="tab-label">
              <el-icon><Goods /></el-icon>
              <span>商品管理</span>
            </div>
          </template>
          <ProductManagement v-if="activeTab === 'products'" />
        </el-tab-pane>

        <!-- 订单管理 -->
        <el-tab-pane name="orders">
          <template #label>
            <div class="tab-label">
              <el-icon><List /></el-icon>
              <span>订单管理</span>
            </div>
          </template>
          <OrderManagement v-if="activeTab === 'orders'" />
        </el-tab-pane>

        <!-- 销售统计报表 -->
        <el-tab-pane name="report">
          <template #label>
            <div class="tab-label">
              <el-icon><TrendCharts /></el-icon>
              <span>销售统计</span>
            </div>
          </template>
          <SalesReport v-if="activeTab === 'report'" />
        </el-tab-pane>

        <!-- 用户日志 -->
        <el-tab-pane name="logs">
          <template #label>
            <div class="tab-label">
              <el-icon><Document /></el-icon>
              <span>用户日志</span>
            </div>
          </template>
          <el-tabs v-model="logType" class="sub-tabs" v-if="activeTab === 'logs'">
            <el-tab-pane label="浏览记录" name="browse">
              <BrowseLogs v-if="logType === 'browse'" />
            </el-tab-pane>
            <el-tab-pane label="购买记录" name="purchase">
              <PurchaseLogs v-if="logType === 'purchase'" />
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, defineAsyncComponent } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessageBox } from 'element-plus'
import { Shop, User, Goods, List, Document, TrendCharts, CaretBottom, SwitchButton } from '@element-plus/icons-vue'

// 懒加载组件
const UserManagement = defineAsyncComponent(() => 
  import('@/components/admin/UserManagement.vue')
)
const ProductManagement = defineAsyncComponent(() => 
  import('@/components/admin/ProductManagement.vue')
)
const OrderManagement = defineAsyncComponent(() => 
  import('@/components/admin/OrderManagement.vue')
)
const SalesReport = defineAsyncComponent(() => 
  import('@/components/admin/SalesReport.vue')
)
const BrowseLogs = defineAsyncComponent(() => 
  import('@/components/admin/BrowseLogs.vue')
)
const PurchaseLogs = defineAsyncComponent(() => 
  import('@/components/admin/PurchaseLogs.vue')
)

const router = useRouter()
const store = useStore()
const activeTab = ref('users')  // 默认显示用户管理
const logType = ref('browse')

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('token')
    store.commit('SET_USER_ID', null)
    router.push('/login')
  })
}
</script>

<style scoped>
.admin-container {
  min-height: 97vh;
  background-color: #f0f2f5;
}

.header {
  background: linear-gradient(90deg, #409EFF, #36cfc9);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  height: 60px;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.logo:hover {
  opacity: 0.9;
}

.logo-icon {
  font-size: 24px;
  margin-right: 8px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
}

.divider {
  margin: 0 12px;
  height: 20px;
  border-color: rgba(255,255,255,0.3);
}

.sub-title {
  font-size: 16px;
  opacity: 0.9;
}

.nav-right {
  display: flex;
  align-items: center;
}

.admin-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 12px;
  border-radius: 4px;
  transition: all 0.3s;
}

.admin-info:hover {
  background: rgba(255,255,255,0.1);
}

.admin-name {
  margin: 0 8px;
  font-size: 14px;
}

.main-content {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.custom-tabs {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.custom-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
  border-bottom: 1px solid #e4e7ed;
  background: #fafafa;
  border-radius: 8px 8px 0 0;
  padding: 0 20px;
}

.custom-tabs :deep(.el-tabs__item) {
  height: 48px;
  line-height: 48px;
  font-size: 14px;
  transition: all 0.3s;
  padding: 0 24px;
}

.custom-tabs :deep(.el-tabs__item.is-active) {
  font-weight: 600;
  background: white;
  border-bottom: 2px solid #409EFF;
}

.custom-tabs :deep(.el-tabs__item:hover) {
  color: #409EFF;
}

.tab-label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tab-label .el-icon {
  font-size: 16px;
}

/* 用户日志子标签页样式 */
.sub-tabs {
  padding: 16px;
}

.sub-tabs :deep(.el-tabs__header) {
  background: transparent;
  margin-bottom: 16px;
}

.sub-tabs :deep(.el-tabs__item) {
  font-size: 14px;
  height: 40px;
  line-height: 40px;
}

:deep(.el-tab-pane) {
  padding: 20px;
}

</style>