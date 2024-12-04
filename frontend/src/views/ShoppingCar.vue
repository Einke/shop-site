<template>
  <div class="cart-container">
    <el-card class="cart-content-card">
      <!-- 查询条件 -->
      <div class="search-section">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="商品名称">
            <el-input 
              v-model="searchForm.productName" 
              placeholder="请输入商品名称"
              prefix-icon="Search">
            </el-input>
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
          <el-form-item class="search-buttons">
            <el-button type="primary" @click="searchCars">查询</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格工具栏 -->
      <div class="table-toolbar">
        <el-button 
          @click="batchDelete" 
          type="danger" 
          :disabled="!selectedItems.length"
          class="batch-delete-btn">
          <el-icon><Delete /></el-icon>
          批量删除 ({{ selectedItems.length }})
        </el-button>
      </div>

      <!-- 购物车列表 -->
      <el-table 
        ref="tableRef"
        :data="cartItems" 
        style="width: 100%" 
        @selection-change="handleSelectionChange"
        :header-cell-style="{ background: '#f5f7fa' }"
        @row-click="handleRowClick">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="shopName" label="商品名称" ></el-table-column>
        <el-table-column prop="quantity" label="数量" align="center" width="100"></el-table-column>
        <el-table-column prop="price" label="单价"  align="center">
          <template #default="scope">
            {{scope.row.price.toFixed(2)}}
          </template> 
        </el-table-column>
        <el-table-column label="总价"  align="center" width="100">
          <template #default="scope">
            {{scope.row.price * scope.row.quantity}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"  align="center">
          <template #default="scope">
            {{scope.row.createTime ? scope.row.createTime.replace('T',' '):''}}
          </template>
        </el-table-column>
        <el-table-column  align="center" fixed="right">
          <template #default="scope">
            <div class="action-buttons" :class="{ 'show-buttons': scope.row.showActions }">
              <el-button @click.stop="editShopCar(scope.row.id)" type="primary" size="small">修改</el-button>
              <el-button @click.stop="purchaseItem(scope.row)" type="success" size="small" :loading="loading">立即购买</el-button>
              <el-button @click.stop="handleDelete(scope.row.id)" type="danger" size="small">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination-container">
        <el-pagination
          background
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20, 30, 50]"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>
    
    <!-- 添加修改对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="修改商品信息" 
      width="450px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="editingItem" label-width="80px" class="edit-form">
        <el-form-item label="商品名称">
          <div class="product-name">{{ editingItem.shopName }}</div>
        </el-form-item>
        <el-form-item label="商品数量">
          <el-input-number 
            v-model="editingItem.quantity" 
            :min="1"
            :precision="0"
            controls-position="right"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="商品价格">
          <div class="product-price">¥{{ editingItem.price?.toFixed(2) }}</div>
        </el-form-item>
        <el-form-item label="总价">
          <div class="total-price">¥{{ (editingItem.quantity * editingItem.price)?.toFixed(2) }}</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmEdit(editingItem)">确定</el-button>
          <el-button type="success" @click="purchaseFromDialog(editingItem)" :loading="loading">立即购买</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref ,computed,onMounted} from 'vue'
import { ElMessage ,ElIcon,ElMessageBox,ElLoading} from 'element-plus'
import { useStore } from 'vuex'
import request from '@/utils/request'

const store = useStore()  

const loading = ref(false)
// 模拟购物车数据
const cartItems = ref([])
const total = ref(0) // 总记录数
const selectedItems = ref([])

// 添加分页相关的响应式变量
const currentPage = ref(1)
const pageSize = ref(10)

// 查询表单数据
const searchForm = ref({
  productName: '',
  dateRange: []
})

// 获取购物车数据的方法
const getCartList = () => {
  request.get('/car', {
    params: {
      userId: store.state.userId,
      page: currentPage.value,
      pageSize: pageSize.value,
      shopName: searchForm.value.productName,
      begin: searchForm.value.dateRange?.[0],
      end: searchForm.value.dateRange?.[1] 
    }
  }).then(response => {
    cartItems.value = response.data.rows
    total.value = response.data.total
  })
}

// 查询订单
const searchCars = () => {
  currentPage.value = 1
  getCartList()
}

// 重置表单
const resetForm = () => {
  searchForm.value = { productName: '', dateRange: [] }
  currentPage.value = 1
  pageSize.value = 10
  getCartList()
}

// 分页处理方法
const handleSizeChange = (val) => {
  pageSize.value = val
  getCartList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getCartList()
}

// 处理选择变化
const handleSelectionChange = (val) => {
  selectedItems.value = val
} 

// 单个删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteItems([id])
  })
}

// 批量删除
const batchDelete = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请选择要删除的商品')
    return
  }
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedItems.value.length} 个商品吗？`, 
    '提示', 
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const ids = selectedItems.value.map(item => item.id)
    deleteItems(ids)
  })
}

// 删除操作的公共方法
const deleteItems = (ids) => {
  request.delete(`/car/${ids.join(',')}`).then(res => {
    ElMessage.success('删除成功')
    selectedItems.value = [] // 清空选中项
    getCartList() // 重新获取列表
  })
}

// 添加新的响应式变量
const dialogVisible = ref(false)
const editingItem = ref({})

// 根据id查询购物车信息
const editShopCar = (id) => {
  dialogVisible.value = true
  request.get(`/car/${id}`).then(res => {
    editingItem.value = res.data
  })
}

// 确认修改
const confirmEdit = (item) => {
  request.put('/car',item).then(res => {
    ElMessage.success('修改成功')
    dialogVisible.value = false
    getCartList()
  })
}

// 购买商品
const purchaseItem = (item) => {
  ElMessageBox.confirm('确定要购买该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 创建订单 传给后端写入新订单的数据
    loading.value = true
    const order = {
      userId: store.state.userId,
      shopId: item.shopId,
      quantity: item.quantity,
      totalPrice: item.price * item.quantity
    }
    // 添加加载提示
    const loadingInstance = ElLoading.service({
      text: '正在处理订单...',
      background: 'rgba(255, 255, 255, 0.8)'
    })

    request.post('/orders',order).then(res => {
      request.delete(`/car/${item.id}`).then(res => { //删除购物车数据
        ElMessage.success('购买成功！邮件已发送至您的邮箱')
        getCartList()
      })
    }).finally(() => {
      loading.value = false
      loadingInstance.close()
    })
  })
}

// 从对话框中购买
const purchaseFromDialog = (item) => {
  ElMessageBox.confirm('确定要购买该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    loading.value = true
    const loadingInstance = ElLoading.service({
      text: '正在处理订单...',
      background: 'rgba(255, 255, 255, 0.8)'
    })

    // 创建订单 传给后端写入新订单的数据
    const order = {
      userId: store.state.userId,
      shopId: item.shopId,
      quantity: item.quantity,
      totalPrice: item.price * item.quantity
    }
    request.post('/orders',order).then(res => {
      request.delete(`/car/${item.id}`).then(res => { 
        ElMessage.success('购买成功！邮件已发送至您的邮箱')
        dialogVisible.value = false
        getCartList()
      })
    }).finally(() => {
      loading.value = false
      loadingInstance.close()
    })
  })
}

// 添加表格ref
const tableRef = ref(null)

// 修改行点击选择功能
const handleRowClick = (row, column, event) => {
  // 防止点击操作按钮时触发行选择
  if (column.type === 'selection' || column.label === '操作') {
    return
  }
  tableRef.value?.toggleRowSelection(row)
}

// 组件挂载时获取数据
onMounted(() => {
  getCartList()
})
</script>

<style scoped>
.cart-container {
  padding: 20px;
  background-color: #f5f7fa;
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

.cart-content-card {
  border-radius: 8px;
}

.search-section {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.table-toolbar {
  margin-bottom: 16px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.quantity {
  font-weight: 600;
}

.price {
  color: #f56c6c;
  font-weight: 600;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

:deep(.el-table__row:hover) .action-buttons {
  opacity: 1;
}

.search-buttons {
  margin-right: 0;
}

:deep(.el-form-item) {
  margin-bottom: 0;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

/* 表格hover效果 */
:deep(.el-table__row:hover) {
  background-color: #f5f7fa !important;
}

/* 按钮图标对齐 */
.el-button [class*="el-icon"] + span {
  margin-left: 4px;
}

:deep(.el-dialog) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  margin: 0;
  padding: 20px 20px 10px;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.el-dialog__title) {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

.edit-form {
  .product-name {
    font-size: 15px;
    color: #606266;
    line-height: 32px;
  }

  .product-price {
    font-size: 15px;
    color: #606266;
    line-height: 32px;
  }

  .total-price {
    font-size: 16px;
    color: #f56c6c;
    font-weight: 600;
    line-height: 32px;
  }
}

:deep(.el-dialog__footer) {
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
  background: #f9f9f9;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .cart-container {
    padding: 10px;
  }
  
  /* 表格操作按钮响应式 */
  .action-buttons {
    flex-direction: column;
    gap: 5px;
  }

  /* 批量删除按钮响应式 */
  .batch-delete-btn {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>