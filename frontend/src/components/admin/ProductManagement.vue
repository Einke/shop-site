<template>
  <div class="product-management">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <div class="search-area">
        <el-input
          v-model="productSearch"
          placeholder="输入商品名称搜索"
          :prefix-icon="Search"
          clearable
          class="search-input"
          @clear="searchProducts"
        />
        <el-select
          v-model="selectedCategory"
          placeholder="选择分类"
          clearable
          class="category-select"
          @clear="searchProducts"
        >
          <el-option
            v-for="category in categories"
            :key="category"
            :label="category"
            :value="category"
          />
        </el-select>
        <el-button type="primary" @click="searchProducts">查询</el-button>
      </div>
      <div class="right">
        <el-button type="primary" :icon="Plus" @click="showAddDialog">
          添加商品
        </el-button>
      </div>
    </div>

    <!-- 商品表格 -->
    <el-table :data="products" stripe style="width: 100%">
      <el-table-column prop="shopName" label="商品名称" width="200"></el-table-column>
      <el-table-column prop="category" label="分类">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" >
        <template #default="scope">
          <span class="price">¥{{ scope.row.price?.toFixed(2) }}</span>
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
              @click="editProduct(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="small"
              type="danger"
              plain
              :icon="Delete"
              @click="deleteProduct(scope.row)"
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

    <!-- 添加/编辑商品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加商品' : '编辑商品'"
      width="600px"
    >
      <el-form
        ref="productFormRef"
        :model="productForm"
        :rules="productRules"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="shopName">
          <el-input v-model="productForm.shopName" />
        </el-form-item>
        
        <el-form-item label="商品图片" prop="image">
          <el-upload
            class="product-image-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="productForm.image" :src="productForm.image" class="uploaded-image">
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="商品分类" prop="category">
          <el-select v-model="productForm.category" placeholder="选择分类">
            <el-option
              v-for="category in categories"
              :key="category"
              :label="category"
              :value="category"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="商品价格" prop="price">
          <el-input-number 
            v-model="productForm.price" 
            :precision="2" 
            :step="0.1" 
            :min="0"
          />
        </el-form-item>

        <el-form-item label="商品利润" prop="profit">
          <el-input-number 
            v-model="productForm.profit" 
            :precision="2" 
            :step="0.1" 
            :min="0"
          />
        </el-form-item>

        <el-form-item label="商品描述" prop="description">
          <el-input 
            v-model="productForm.description" 
            type="textarea" 
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
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

const productSearch = ref('')
const selectedCategory = ref(null)
const categories = ref(['手机', '电脑', '耳机', '平板', '手表'])
const products = ref([])

const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const loadProducts = () => {
  request.get('/shop', {
    params: {
      page: pagination.value.currentPage,
      pageSize: pagination.value.pageSize,
      shopName: productSearch.value,
      category: selectedCategory.value
    }
  }).then(response => {
    products.value = response.data.rows
    pagination.value.total = response.data.total
  })
}

const searchProducts = () => {
  pagination.value.currentPage = 1
  loadProducts()
}

const handleSizeChange = (newSize) => {
  pagination.value.pageSize = newSize
  pagination.value.currentPage = 1
  loadProducts()
}

const handleCurrentChange = (newPage) => {
  pagination.value.currentPage = newPage
  loadProducts()
}

onMounted(() => {
  loadProducts()
})

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const productFormRef = ref(null)
const productForm = ref({
  id: '',
  shopName: '',
  image: '',
  category: '',
  price: 0,
  profit: 0,
  description: ''
})

// 表单验证规则
const productRules = {
  shopName: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' }
  ],
  profit: [
    { required: true, message: '请输入商品利润', trigger: 'blur' }
  ]
}

// 显示添加对话框
const showAddDialog = () => {
  dialogType.value = 'add'
  productForm.value = {
    shopName: '',
    image: '',
    category: '',
    price: 0,
    profit: 0,
    description: ''
  }
  dialogVisible.value = true
}

// 显示编辑对话框
const editProduct = (row) => {
  dialogType.value = 'edit'
  productForm.value = {
    id: row.id,
    shopName: row.shopName,
    image: row.image,
    category: row.category,
    price: row.price,
    profit: row.profit,
    description: row.description
  }
  dialogVisible.value = true
}

// 提交表单
const submitForm = () => {
  productFormRef.value.validate((valid) => {
    if (valid) {
      if (dialogType.value === 'add') {
        request.post('/shop', productForm.value).then(response => {
          ElMessage.success('添加成功')
          dialogVisible.value = false
          loadProducts() // 重新加载商品列表
        })
      } else {
        request.put('/shop', productForm.value).then(response => {
          ElMessage.success('修改成功')
          dialogVisible.value = false
          loadProducts() // 重新加载商品列表
        })
      }
    }
  })
}

// 删除商品
const deleteProduct = (row) => {
  ElMessageBox.confirm(
    '确定要删除该商品吗？此操作不可恢复',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    request.delete(`/shop/${row.id}`).then(response => {  
      ElMessage.success('删除成功')
      loadProducts()
    })
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 图片上传相关（没实现）
const uploadUrl = 'your-upload-url' // 替换为实际的上传地址

const handleUploadSuccess = (response) => {
  productForm.value.image = response.url // 假设后端返回 {url: 'image-url'}
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}
</script>

<style scoped>
.product-management {
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

.category-select {
  width: 150px;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-image {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  object-fit: cover;
}

.product-name {
  font-weight: 500;
}

.price {
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

.product-image-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-image-uploader:hover {
  border-color: #409EFF;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.uploaded-image {
  width: 178px;
  height: 178px;
  object-fit: cover;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>