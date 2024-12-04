<template>
  <div class="browse-logs">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="search-area">
        <el-input
          v-model="userSearch"
          placeholder="输入用户名搜索"
          :prefix-icon="Search"
          clearable
          class="search-input"
          @clear="searchUsers"
        >
          <template #append>
            <el-button :icon="Search" @click="searchUsers" />
          </template>
        </el-input>
      </div>
    </div>

    <!-- 浏览日志表格 -->
    <el-table
      :data="browseLogs"
      border
      stripe
      style="width: 100%"
      :header-cell-style="{ background: '#f5f7fa' }"
      v-loading="loading"
    >
      <el-table-column prop="username" label="用户名" ></el-table-column>
      <el-table-column prop="shopName" label="商品名称"></el-table-column>
      <el-table-column prop="operateTime" label="操作时间" >
        <template #default="scope">
          {{ scope.row.operateTime ? scope.row.operateTime.replace('T', ' ') : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="costTime" label="操作耗时">
        <template #default="scope">
          <el-tag size="small" type="info">{{ scope.row.costTime }}ms</el-tag>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const userSearch = ref('')
const browseLogs = ref([])

const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})


const loadBrowseLogs = () => {
  loading.value = true
  request.get('/admin/browse', {
    params: {
      page: pagination.value.currentPage,
      pageSize: pagination.value.pageSize,
      username: userSearch.value
    }
  }).then(response => {
    if (response.code === 1) {
      browseLogs.value = response.data.rows
      pagination.value.total = response.data.total
    }
  }).finally(() => {
    loading.value = false
  })
}

const searchUsers = () => {
  pagination.value.currentPage = 1
  loadBrowseLogs()
}

const handleSizeChange = (newSize) => {
  pagination.value.pageSize = newSize
  pagination.value.currentPage = 1
  loadBrowseLogs()
}

const handleCurrentChange = (newPage) => {
  pagination.value.currentPage = newPage
  loadBrowseLogs()
}

onMounted(() => {
  loadBrowseLogs()
})
</script>

<style scoped>
.browse-logs {
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
</style>    