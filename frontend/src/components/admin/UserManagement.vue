<template>
  <div class="user-management">
    <!-- 搜索栏 -->
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
      <el-button type="primary" :icon="Plus" @click="showAddDialog">添加用户</el-button>
    </div>

    <!-- 用户表格 -->
    <el-table
      :data="users"
      stripe
      style="width: 100%"
      :header-cell-style="{ background: '#f5f7fa' }"
    >
      <el-table-column prop="username" label="用户名" width="180" />
      <el-table-column prop="email" label="邮箱"  />
      <el-table-column prop="createTime" label="注册时间" >
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
              :icon="Edit"
              @click="editUser(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="small"
              type="danger"
              plain
              :icon="Delete"
              @click="deleteUser(scope.row)"
            >
              删除
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

    <!-- 编辑用户对话框 --> 
    <el-dialog
      v-model="editDialogVisible"
      title="编辑用户信息"
      width="500px"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <span>{{ editForm.password }}</span>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="创建时间">
          <span>{{ editForm.createTime?.replace('T', ' ') }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 新用户对话框 -->
    <el-dialog
      v-model="addDialogVisible"
      title="添加用户"
      width="500px"
    >
      <el-form
        ref="addFormRef"
        :model="addForm"
        :rules="addRules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="addForm.password" 
            type="password" 
            show-password
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAdd">确定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search, Edit, Delete, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const userSearch = ref('')
const users = ref([])

const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 编辑相关
const editDialogVisible = ref(false)
const editForm = ref({
  id: '',
  username: '',
  password: '',
  email: '',
  createTime: ''
})
const editRules = {
  username: [
    { message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 添加用户相关
const addDialogVisible = ref(false)
const addFormRef = ref(null)
const addForm = ref({
  username: '',
  password: '',
  email: ''
})
// 添加用户的验证规则
const addRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const loadUsers = () => {
  request.get('/user', {
    params: {
      page: pagination.value.currentPage,
      pageSize: pagination.value.pageSize,
      username: userSearch.value
    }
  }).then(response => {
    users.value = response.data.rows
    pagination.value.total = response.data.total
  })
}

const searchUsers = () => {
  pagination.value.currentPage = 1
  loadUsers()
}

const handleSizeChange = (newSize) => {
  pagination.value.pageSize = newSize
  pagination.value.currentPage = 1
  loadUsers()
}

const handleCurrentChange = (newPage) => {
  pagination.value.currentPage = newPage
  loadUsers()
}

// 编辑用户
const editUser = (row) => {
  editForm.value = {
    id: row.id,
    username: row.username,
    password: row.password,
    email: row.email,
    createTime: row.createTime
  }
  editDialogVisible.value = true
}

// 提交编辑
const submitEdit = () => {
  request.put('/user', {
    id: editForm.value.id,
    username: editForm.value.username,
    email: editForm.value.email
  }).then(response => {
    ElMessage.success('修改成功')
    editDialogVisible.value = false
    loadUsers() // 重新加载用户列表
  })
}

// 删除用户
const deleteUser = (row) => {
  ElMessageBox.confirm(
    '确定要删除该用户吗？此操作不可恢复',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    request.delete(`/user/${row.id}`).then(response => {
      ElMessage.success('删除成功')
      loadUsers() // 重新加载用户列表
    })
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 显示添加对话框
const showAddDialog = () => {
  addForm.value = {
    username: '',
    password: '',
    email: ''
  }
  addDialogVisible.value = true
}

// 提交添加用户
const submitAdd = () => {
  addFormRef.value.validate((valid) => {
    if (valid) {
      request.post('/user', addForm.value)
        .then(response => {
          ElMessage.success('添加成功')
          addDialogVisible.value = false
          loadUsers() // 重新加载用户列表
        })
    }
  })
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-management {
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
  width: 300px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  font-weight: 500;
}

/* 操作按钮默认隐藏 */
.operation-buttons {
  opacity: 0;
  transition: opacity 0.3s;
  display: flex;
  gap: 8px;  /* 按钮之间的间距 */
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

/* 添加对话框相关样式 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-dialog__body) {
  padding: 20px 40px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style>    