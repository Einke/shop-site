<template>
  <div class="order-list">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="商品名称">
        <el-input v-model="searchForm.productName" placeholder="请输入商品名称"></el-input>
      </el-form-item>
      <el-form-item label="订单状态">
        <el-select v-model="searchForm.state" placeholder="选择订单状态" clearable style="width: 140px;">
          <el-option label="已送达" :value="1" />
          <el-option label="已发货" :value="2" />
          <el-option label="未发货" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="searchForm.dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchOrders()">查询</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 订单列表 -->
    <el-table :data="orders" style="width: 100%; margin-bottom: 20px;" :header-cell-style="{ background: '#f5f7fa' }">
      <el-table-column prop="shopName" label="商品名称" align="center"></el-table-column>
      <el-table-column prop="quantity" label="购买数量" align="center" width="100"></el-table-column>
      <el-table-column prop="createTime" label="下单时间" align="center">
        <template #default="scope">
            {{scope.row.createTime ? scope.row.createTime.replace('T',' '):''}}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.state === 1 ? 'success' : scope.row.state === 2 ? 'warning' : 'danger'">
            {{ scope.row.state ===1 ? '已送达' : scope.row.state === 2 ? '已发货' : '未发货'}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <el-button size="small" type="primary" plain @click="showOrderDetail(scope.row.id)">查看详情</el-button>
            <el-button 
              v-if="scope.row.state === 2"
              size="small" 
              type="success" 
              plain 
              @click="confirmDelivery(scope.row.id)"
            >
              确认收货
            </el-button>
            <el-button size="small" type="danger" plain @click="deleteOrder(scope.row.id)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改分页组件样式和位置 -->
    <div class="pagination-container">
      <el-pagination
        background
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>

  <el-dialog
    v-model="dialogVisible"
    title="订单详情"
    width="50%"
  >
    <div class="order-detail">
      <div class="detail-item">
        <span class="label">订单号：</span>
        <span>{{ currentOrder.id }}</span>
      </div>
      <div class="detail-item">
        <span class="label">商品名称：</span>
        <span>{{ currentOrder.shopName }}</span>
      </div>
      <div class="detail-item">
        <span class="label">商品数量：</span>
        <span>{{ currentOrder.quantity }}</span>
      </div>
      <div class="detail-item">
        <span class="label">总价：</span>
        <span>¥{{ currentOrder.totalPrice }}</span>
      </div>
      <div class="detail-item">
        <span class="label">下单时间：</span>
        <span>{{ currentOrder.createTime ? currentOrder.createTime.replace('T',' '):'' }}</span>
      </div>
      <div class="detail-item">
        <span class="label">订单状态：</span>
        <span>{{ currentOrder.state === 1 ? '已送达' : currentOrder.state === 2 ? '已发货' : '未发货' }}</span>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确定</el-button>
      </span>
    </template>
  </el-dialog>

</template>

<script setup>
import { ref, computed ,onMounted} from 'vue'
import {ElIcon,ElMessage,ElMessageBox} from 'element-plus'
import { useStore } from 'vuex'
import request from '@/utils/request'

const store = useStore()

// 模拟订单数据
const orders = ref([])

// 弹窗控制
const dialogVisible = ref(false)
const currentOrder = ref({})

// 查询表单数据
const searchForm = ref({
  productName: '',
  dateRange: [],
  state: ''
})

const fetchOrders = () => {
  const params = {
    userId: store.state.userId,
    shopName:searchForm.value.productName,
    state:searchForm.value.state,
    begin:searchForm.value.dateRange?.[0],
    end:searchForm.value.dateRange?.[1],
    page:currentPage.value,
    pageSize:pageSize.value
  }
  request.get('/orders',{params}).then(res => {
    orders.value = res.data.rows
    total.value = res.data.total
  })
}

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchOrders()
}

// 处理每页显示数量变化
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchOrders()
}

// 查询订单
const searchOrders = () => {
  fetchOrders()
}

// 重置表单
const resetForm = () => {
  searchForm.value = {
    productName: '',
    dateRange: [],
    state: ''
  }
  fetchOrders()
  currentPage.value = 1
}

// 显示订单详情
const showOrderDetail = (id) => {
  request.get(`/orders/${id}`).then(res => {
    currentOrder.value = res.data
    dialogVisible.value = true
  })
}

// 确认收货
const confirmDelivery = (id) => {
  ElMessageBox.confirm('确定要确认收货吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.put(`/orders/${id}`).then(res => {
      ElMessage.success('确认收货成功')
      fetchOrders()
    })
  })
}

// 删除订单
const deleteOrder = (id) => {
  ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete(`/orders/${id}`).then(res => {
      ElMessage.success('删除成功')
      fetchOrders()
    })
  })
}

onMounted(()=>{
  fetchOrders()
})

</script>

<style scoped>
.order-list {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px 0;
}

/* 表格行hover时显示操作按钮 */
.el-table__row .action-buttons {
  opacity: 0;
  transition: opacity 0.2s;
}

.el-table__row:hover .action-buttons {
  opacity: 1;
}

/* 订单状态标签样式 */
.el-tag {
  width: 65px;
  text-align: center;
}

/* 分页组件样式优化 */
:deep(.el-pagination) {
  padding: 0;
  font-weight: normal;
}

:deep(.el-pagination .el-select .el-input) {
  width: 110px;
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 4px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-table td) {
  padding: 8px 0;
}

.order-detail {
  padding: 20px;
}

.detail-item {
  margin-bottom: 15px;
  font-size: 14px;
}

.detail-item .label {
  display: inline-block;
  width: 100px;
  color: #606266;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .order-list {
    padding: 10px;
  }
  
  .search-form {
    padding: 15px;
  }
  
  /* 操作按钮响应式 */
  .action-buttons {
    flex-direction: column;
    gap: 5px;
  }
  
  /* 分页器响应式 */
  .pagination-container {
    margin-top: 15px;
  }
}
</style>