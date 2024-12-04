<template>
  <div class="not-found-container">
    <div class="content">
      <div class="error-code">
        <span>4</span>
        <div class="circle"></div>
        <span>4</span>
      </div>
      
      <h1 class="error-title">页面未找到</h1>
      <p class="error-message">抱歉，您访问的页面可能已被移除或暂时无法访问</p>
      
      <div class="actions">
        <el-button type="primary" @click="goBack">返回上一页</el-button>
        <el-button @click="goHome">返回首页</el-button>
      </div>

      <div class="suggestions">
        <h3>您可以尝试：</h3>
        <ul>
          <li>检查输入的网址是否正确</li>
          <li>清除浏览器缓存后重试</li>
          <li>联系客服寻求帮助</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.go(-1)
}

const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.not-found-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.content {
  text-align: center;
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 100%;
}

.error-code {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 120px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 20px;
}

.circle {
  width: 80px;
  height: 80px;
  border: 8px solid #409EFF;
  border-radius: 50%;
  margin: 0 10px;
  animation: pulse 1.5s infinite;
}

.error-title {
  font-size: 28px;
  color: #303133;
  margin-bottom: 15px;
}

.error-message {
  font-size: 16px;
  color: #606266;
  margin-bottom: 30px;
  line-height: 1.6;
}

.actions {
  margin-bottom: 40px;
}

.actions .el-button {
  margin: 0 10px;
  padding: 12px 24px;
}

.suggestions {
  text-align: left;
  border-top: 1px solid #EBEEF5;
  padding-top: 20px;
}

.suggestions h3 {
  font-size: 16px;
  color: #303133;
  margin-bottom: 15px;
}

.suggestions ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.suggestions li {
  color: #606266;
  margin-bottom: 10px;
  padding-left: 20px;
  position: relative;
}

.suggestions li::before {
  content: "•";
  color: #409EFF;
  position: absolute;
  left: 0;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 480px) {
  .error-code {
    font-size: 80px;
  }
  
  .circle {
    width: 60px;
    height: 60px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .actions .el-button {
    margin: 5px;
    width: 80%;
  }
  
  .content {
    padding: 20px;
  }
}
</style>