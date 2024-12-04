<template>
  <div class="welcome">
    <div class="welcome-section">
      <div class="hero-content">
        <h1 class="title">欢迎来到购物网站</h1>
        <p class="subtitle">在这里，您可以找到各种各样的商品，享受便捷的购物体验</p>
      </div>

      <div class="features">
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Van /></el-icon>
          </div>
          <h3>快速配送</h3>
          <p>全国范围内24小时发货</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Checked /></el-icon>
          </div>
          <h3>安全保障</h3>
          <p>正品保证，假一赔十</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Service /></el-icon>
          </div>
          <h3>优质服务</h3>
          <p>7×24小时客服在线</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Discount /></el-icon>
          </div>
          <h3>特惠活动</h3>
          <p>多重优惠，天天特价</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Van, Checked, Service, Discount } from '@element-plus/icons-vue'
</script>

<style scoped>
.welcome {
  min-height: 80vh;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px 20px;
  box-sizing: border-box;
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


.welcome-section {
  max-width: 1200px;
  width: 100%;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  padding: 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  margin-top: 20px;
}

.hero-content {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 3.2em;
  margin-bottom: 20px;
  background: linear-gradient(45deg, #2196F3, #4CAF50);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: bold;
  letter-spacing: -1px;
}

.subtitle {
  font-size: 1.4em;
  color: #666;
  line-height: 1.6;
  max-width: 800px;
  margin: 0 auto;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-top: 20px;
}

.feature-card {
  background: white;
  padding: 30px;
  border-radius: 16px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: linear-gradient(135deg, #e0f2f1 0%, #b2dfdb 100%);
}

.feature-icon :deep(.el-icon) {
  font-size: 32px;
  color: #009688;
}

.feature-card h3 {
  font-size: 1.4em;
  color: #333;
  margin-bottom: 12px;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome {
    min-height: auto;
  }

  .welcome-section {
    padding: 30px 20px;
    margin-top: 10px;
  }

  .title {
    font-size: 2.4em;
  }

  .subtitle {
    font-size: 1.2em;
    padding: 0 20px;
  }

  .features {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
  }

  .feature-card {
    padding: 20px;
  }
}
</style>