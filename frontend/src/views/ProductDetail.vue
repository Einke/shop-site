<template>
  <div class="product-detail">
    <div class="detail-container">
      <!-- 左侧商品图片 -->
      <div class="product-image">
        <img :src="product.image" :alt="product.name">
      </div>
      
      <!-- 右侧商品信息 -->
      <div class="product-info">
        <h1>{{ product.name }}</h1>
        <div class="info-item category">
          <span class="label">商品分类：</span>
          <span>{{ product.category }}</span>
        </div>
        <div class="info-item price">
          <span class="label">商品价格：</span>
          <span class="price-value">¥{{ product.price }}</span>
        </div>
        <div class="info-item stock">
          <span class="label">库存状态：</span>
          <span :class="{ 'in-stock': product.stock > 0 }">有货</span>
        </div>
        <div class="description">
          <h3>商品描述</h3>
          <p>{{ product.description }}</p>
        </div>
        
        <!-- 购买数量选择 -->
        <div class="quantity-selector">
          <span class="label">购买数量：</span>
          <el-input-number 
            v-model="quantity" 
            :min="1" 
            :max="product.stock"
            :disabled="product.stock <= 0"
          ></el-input-number>
        </div>
        
        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button @click="goBack">取消</el-button>
          <el-button 
            type="primary" 
            @click="addToCart"
            :disabled="product.stock <= 0"
          >
            加入购物车
          </el-button>
          <el-button 
            type="danger" 
            @click="buyNow"
            :disabled="product.stock <= 0"
            :loading="loading"
          >
            立即购买
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage,ElMessageBox,ElLoading } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const store = useStore()

const loading = ref(false)
const quantity = ref(1)
const product = ref({})

//获取商品详情
onMounted(() => {
  const productId = route.params.id
  request.get(`/shop/${productId}`)
    .then(response => {
      product.value = response.data
    })
})

// 返回上一页
const goBack = () => {
  router.back()
}

// 加入购物车
const addToCart = () => {
  request.post('/car', {
    userId: store.state.userId,
    shopId: product.value.id,
    quantity: quantity.value
  }).then(response => {
    ElMessage.success('已加入购物车')
    router.back()
  })
}

// 立即购买
const buyNow = () => {
  ElMessageBox.confirm('确定要购买吗？', '提示', {
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
      shopId: product.value.id,
      quantity: quantity.value,
      totalPrice: product.value.price * quantity.value
    }
    request.post('/orders',order).then(res => {
      ElMessage.success('购买成功！邮件已发送至您的邮箱')
      router.push('/shop')
    }).finally(() => {
      loading.value = false
      loadingInstance.close()
    })
  })
}
</script>

<style scoped>
.product-detail {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
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

.detail-container {
  display: flex;
  gap: 40px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.product-image {
  flex: 0 0 400px;
}

.product-image img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.product-info {
  flex: 1;
}

.product-info h1 {
  margin: 0 0 20px 0;
  font-size: 24px;
}

.info-item {
  margin-bottom: 15px;
  font-size: 16px;
}

.label {
  color: #666;
  margin-right: 10px;
}

.price-value {
  color: #ff4949;
  font-size: 24px;
  font-weight: bold;
}

.description {
  margin: 20px 0;
  padding: 20px 0;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
}

.description h3 {
  margin-bottom: 10px;
}

.quantity-selector {
  margin: 20px 0;
}

.action-buttons {
  margin-top: 30px;
  display: flex;
  gap: 15px;
}

.in-stock {
  color: #67c23a;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .product-detail {
    padding: 10px;
  }

  .detail-container {
    flex-direction: column;
    gap: 20px;
  }

  .product-image {
    flex: none;
    width: 100%;
  }

  .product-image img {
    height: 300px;
    width: 100%;
  }

  .product-info {
    padding: 0 10px;
  }

  .action-buttons {
    flex-direction: column;
    gap: 10px;
  }

  .action-buttons .el-button {
    width: 100%;
    margin-left: 0;
  }

  .quantity-selector {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .quantity-selector .el-input-number {
    width: 120px;
  }
}
</style> 