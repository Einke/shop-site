<template>
  <div class="product-list">
    <!-- 优化搜索区域 -->
    <div class="search-container">
      <h2 class="search-title">商品搜索</h2>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="searchForm.category" placeholder="选择分类" clearable style="width: 100px">
            <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格范围">
          <el-input-number v-model="searchForm.priceMin" placeholder="最小价格"></el-input-number>
          <span>至</span>
          <el-input-number v-model="searchForm.priceMax" placeholder="最大价格"></el-input-number>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchProducts">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 添加结果统计和排序 -->
    <div class="result-header">
      <span class="result-count">共找到 {{ total }} 个商品</span>
      <div class="sort-options">
        <el-select v-model="sort" placeholder="排序方式" style="width: 130px" @change="handleSortChange">
          <el-option label="默认排序" value=""></el-option>
          <el-option label="价格从低到高" value="asc"></el-option>
          <el-option label="价格从高到低" value="desc"></el-option>
        </el-select>
      </div>
    </div>

    <!-- 商品列表 -->
    <div class="products-grid">
      <el-card
        v-for="product in products" 
        :key="product.id" 
        class="product-card"
        :body-style="{ padding: '0px' }"
        @click="viewDetails(product)"
      >
        <div class="product-image-container">
          <img :src="product.image || 'default-image-url'" alt="商品图片">
        </div>
        <div class="product-info">
          <h3 class="product-name">{{ product.shopName }}</h3>
          <el-tag size="small" class="category-tag" type="info" effect="plain">
            {{ product.category }}
          </el-tag>
          <p class="price">¥{{ product.price.toFixed(2) }}</p>
          <p class="description">{{ product.description }}</p>
        </div>
      </el-card>
    </div>
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[12, 24, 36, 48]"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {ElIcon,ElMessage} from 'element-plus'
import request from '@/utils/request' 

const router = useRouter()
const route = useRoute()

// 模拟商品数据
const products = ref([])

const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 商品分类选项
const categories = ref([
  { value: '手机', label: '手机' },
  { value: '电脑', label: '电脑' },
  { value: '耳机', label: '耳机' },
  { value: '平板', label: '平板' },
  { value: '手表', label: '手表' } 
])

// 查询表单数据
const searchForm = ref({
  name: '',
  category: '',
  priceMin: null,
  priceMax: null
})
//价格排序参数
const sort = ref('')

// 获取商品列表数据
const fetchProducts = () => {
  const params = {
    shopName: searchForm.value.name,
    category: searchForm.value.category,
    minPrice: searchForm.value.priceMin,
    maxPrice: searchForm.value.priceMax,
    sort: sort.value,
    page: currentPage.value,
    pageSize: pageSize.value
  }
  
  request.get('/shop', { params })
    .then(res => {
      products.value = res.data.rows
      total.value = res.data.total
    })
}

// 查询商品
const searchProducts = () => {
  currentPage.value = 1  // 重置页码
  fetchProducts()
}

// 重置表单
const resetForm = () => {
  searchForm.value = {
    name: '',
    category: '',
    priceMin: null,
    priceMax: null,
  }
  sort.value=''
  currentPage.value = 1  // 重置页码
  fetchProducts()
}

// 查看详情
const viewDetails = (product) => {
  router.push({
    name: 'ShopDetail',
    params: { id: product.id }
  })
}


// 处理页码改变
const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchProducts()
}

// 处理每页条数改变
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1 // 重置到第一页
  fetchProducts()
}

// 处理排序变化
const handleSortChange = () => {
  currentPage.value = 1 // 切换排序时重置页码
  fetchProducts()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchProducts()
})
</script>

<style scoped>
.product-list {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
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

.search-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.search-title {
  margin-bottom: 20px;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px 0;
  padding: 0 10px;
}

.result-count {
  color: #606266;
  font-size: 14px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
  padding: 10px;
}

.product-card {
  transition: all 0.3s ease;
  overflow: hidden;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}

.product-image-container {
  position: relative;
  height: 220px;
  overflow: hidden;
}

.product-image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-info {
  padding: 15px;
}

.product-name {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}


.price {
  color: #ff6b6b;
  font-size: 18px;
  font-weight: bold;
  margin: 4px 0;
}

.category-tag {
  margin: 8px 0;
  font-size: 12px;
  border-radius: 12px;
  padding: 0 12px;
  height: 24px;
  line-height: 24px;
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  color: #909399;
}

.description {
  color: #606266;
  font-size: 13px;
  margin: 5px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 36px;
}

.cart-button {
  width: 100%;
  margin-top: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-container {
    padding: 10px;
  }

  .result-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 15px;
  }

  .product-card {
    margin-bottom: 10px;
  }

  .product-info {
    padding: 10px;
  }

  .product-name {
    font-size: 14px;
  }

  .price {
    font-size: 16px;
  }

  .description {
    font-size: 12px;
  }

  .pagination-container {
    padding: 10px 0;
  }
}
</style>