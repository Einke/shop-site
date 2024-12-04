<template>
  <div class="order-management">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="search-area">
        <el-input
          v-model="orderSearch"
          placeholder="输入用户名搜索"
          :prefix-icon="Search"
          clearable
          class="search-input"
          @clear="searchOrders"
        />
        <el-select
          v-model="orderStatus"
          placeholder="订单状态"
          clearable
          class="status-select"
          @clear="searchOrders"
        >
          <el-option label="未发货" value=3 />
          <el-option label="已发货" value=2 />
          <el-option label="已送达" value=1 />
        </el-select>
        <el-button type="primary" @click="searchOrders">
          查询
        </el-button>
      </div>
    </div>

    <!-- 订单表格 -->
    <el-table
      :data="orders"
      stripe
      style="width: 100%"
      :header-cell-style="{ background: '#f5f7fa' }"
    >
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="shopName" label="商品名称" width="200"></el-table-column>
      <el-table-column prop="totalPrice" label="订单金额" >
        <template #default="scope">
          <span class="totalPrice">¥{{ scope.row.totalPrice?.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="state" label="状态">
        <template #default="scope">
          <el-tag :type="getOrderStatusType(scope.row.state)" size="small">
            {{ scope.row.state==1?'已送达':scope.row.state==2?'已发货':scope.row.state==3?'未发货':'未知' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
        <template #default="scope">
          {{ scope.row.createTime ? scope.row.createTime.replace('T', ' ') : '' }}
        </template>
      </el-table-column>
      <el-table-column fixed="right">
        <template #default="scope">
          <div class="operation-buttons">
            <el-button
              size="small"
              type="primary"
              plain
              :icon="View"
              @click="viewOrder(scope.row)"
            >
              查看
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页器 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="订单详情"
      width="500px"
    >
      <div class="order-detail">
        <div class="detail-item">
          <span class="label">订单编号：</span>
          <span>{{ currentOrder.id }}</span>
        </div>
        <div class="detail-item">
          <span class="label">用户名：</span>
          <span>{{ currentOrder.username }}</span>
        </div>
        <div class="detail-item">
          <span class="label">商品名称：</span>
          <span>{{ currentOrder.shopName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">购买数量：</span>
          <span>{{ currentOrder.quantity }}</span>
        </div>
        <div class="detail-item">
          <span class="label">单价：</span>
          <span class="price">¥{{ currentOrder.price?.toFixed(2) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">总价：</span>
          <span class="totalPrice">¥{{ currentOrder.totalPrice?.toFixed(2) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">订单状态：</span>
          <el-tag :type="getOrderStatusType(currentOrder.state)" size="small">
            {{ currentOrder.state==1?'已送达':currentOrder.state==2?'已发货':currentOrder.state==3?'未发货':'未知' }}
          </el-tag>
        </div>
        <div class="detail-item">
          <span class="label">创建时间：</span>
          <span>{{ currentOrder.createTime?.replace('T', ' ') }}</span>
        </div>
      </div>
      <template #footer>
        <span>
          <el-button type="primary" @click="detailDialogVisible = false">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search, View, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const orderSearch = ref('')
const orderStatus = ref('')
const orders = ref([])

const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const detailDialogVisible = ref(false)
const currentOrder = ref({})

const getOrderStatusType = (status) => {
  const statusMap = {
    3: 'warning',
    2: 'info',
    1: 'success'
  }
  return statusMap[status] || 'info'
}

const loadOrders = () => {
  request.get('/admin/orders', {
    params: {
      page: pagination.value.currentPage,
      pageSize: pagination.value.pageSize,
      username: orderSearch.value,
      state: orderStatus.value
    }
  }).then(response => {
    orders.value = response.data.rows
    pagination.value.total = response.data.total
  })
}

const searchOrders = () => {
  pagination.value.currentPage = 1
  loadOrders()
}

const handleSizeChange = (newSize) => {
  pagination.value.pageSize = newSize
  pagination.value.currentPage = 1
  loadOrders()
}

const handleCurrentChange = (newPage) => {
  pagination.value.currentPage = newPage
  loadOrders()
}

// 查看订单详情
const viewOrder = (row) => {
  currentOrder.value = { ...row }
  detailDialogVisible.value = true
  
  // request.get(`/orders/${row.id}`).then(response => {
  //   currentOrder.value = response.data
  //   currentOrder.value.username = row.username
  // })
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-management {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-area {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.status-select {
  width: 150px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.product-image {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  object-fit: cover;
}

.totalPrice {
  color: #f56c6c;
  font-weight: 500;
}

/* 操作按钮默认隐藏 */
.operation-buttons {
  opacity: 0;
  transition: opacity 0.3s;
  display: flex;
  gap: 8px;
}

/* 鼠标悬停在行上时显示按钮 */
.el-table__row:hover .operation-buttons {
  opacity: 1;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px 0;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

/* 美化表格hover效果 */
:deep(.el-table__row:hover) {
  background-color: #f5f7fa !important;
}

/* 美化分页器 */
:deep(.el-pagination) {
  --el-pagination-hover-color: #409EFF;
}

:deep(.el-pagination .el-pager li:not(.is-disabled).is-active) {
  background-color: #409EFF;
  color: white;
}

/* 按钮动画效果 */
.el-button {
  transition: all 0.3s;
}

.el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.order-detail {
  padding: 20px;
}

.detail-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.detail-item .label {
  width: 100px;
  color: #606266;
  font-weight: 500;
}

.price, .totalPrice {
  color: #f56c6c;
  font-weight: 500;
}

:deep(.el-dialog__body) {
  padding: 0;
}
</style>   