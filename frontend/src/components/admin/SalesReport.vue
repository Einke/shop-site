<template>
  <div class="sales-report">
    <!-- 添加基础数据卡片 -->
    <el-row :gutter="20" class="data-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-content">
            <div class="card-title">总销售额</div>
            <div class="card-value price">¥{{ basicData.ordersTotalSales.toLocaleString() }}</div>
            <div class="card-footer">平均客单价：¥{{ basicData.averageOrderPrice.toFixed(2) }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-content">
            <div class="card-title">总利润</div>
            <div class="card-value profit">¥{{ basicData.ordersTotalProfit.toLocaleString() }}</div>
            <div class="card-footer">利润率：{{ basicData.profitRate }}%</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-content">
            <div class="card-title">总订单数</div>
            <div class="card-value">{{ basicData.totalOrders.toLocaleString() }}</div>
            <div class="card-footer">总销量：{{ basicData.ordersTotalQuantity }}件</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-content">
            <div class="card-title">商品数据</div>
            <div class="card-value">{{ basicData.totalProducts }}种</div>
            <div class="card-footer">活跃商品：{{ basicData.activeProducts }}种</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 商品销售排行和分类占比 -->
    <el-row :gutter="20" class="rank-section">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="rank-header">
              <span>商品销售排行</span>
            </div>
          </template>
          <el-table :data="productRanking" stripe style="width: 100%">
            <el-table-column prop="rank" label="排名" width="80" />
            <el-table-column prop="shopName" label="商品名称">
              <template #default="scope">
                <div class="product-info">
                  <el-image
                    :src="scope.row.image"
                    class="product-image"
                    fit="cover"
                  />
                  <span>{{ scope.row.shopName }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="totalSales" label="销售额" width="150">
              <template #default="scope">
                <span class="price">¥{{ scope.row.totalSales.toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="totalQuantity" label="销量" width="100" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="rank-header">
              <span>分类销售占比</span>
            </div>
          </template>
          <div class="pie-chart">
            <v-chart class="chart" :option="pieChartOption" autoresize />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 商品销售详情表格 -->
    <div class="sales-details">
      <el-card shadow="hover">
        <template #header>
          <div class="details-header">
            <span>商品销售详情</span>
          </div>
        </template>
        
        <el-table 
          :data="productDetails" 
          stripe 
          style="width: 100%"
          :default-sort="{ prop: 'sales', order: 'descending' }"
          v-loading="loading"
        >
          <el-table-column prop="shopName" label="商品名称" min-width="200">
            <template #default="scope">
              <div class="product-info">
                <el-image
                  :src="scope.row.image"
                  class="product-image"
                  fit="cover"
                />
                <div class="product-detail">
                  <div class="product-name">{{ scope.row.shopName }}</div>
                  <div class="product-category">{{ scope.row.category }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="salesCount" label="订单数" width="120" sortable>
            <template #default="scope">
              <el-tag type="info">{{ scope.row.salesCount }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="totalQuantity" label="销量" width="120" sortable>
            <template #default="scope">
              <el-tag type="info">{{ scope.row.totalQuantity }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="totalSales" label="销售额" width="150" sortable>
            <template #default="scope">
              <span class="price">¥{{ scope.row.totalSales.toLocaleString() }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="totalProfit" label="总利润" width="150" sortable>
            <template #default="scope">
              <span class="profit">{{ scope.row.totalProfit.toLocaleString() }}</span>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页器 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="detailsPagination.currentPage"
            v-model:page-size="detailsPagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="detailsPagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleDetailsSizeChange"
            @current-change="handleDetailsCurrentChange"
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import request from '@/utils/request'

// 注册必须的组件
use([CanvasRenderer, PieChart, GridComponent, TooltipComponent, LegendComponent])

// 基础数据
const basicData = ref({
  ordersTotalSales: 0,
  ordersTotalProfit: 0,
  totalOrders: 0,
  ordersTotalQuantity: 0,
  totalProducts: 0,
  activeProducts: 0,
  averageOrderPrice: 0,
  profitRate: 0
})

// 商品销售排行
const productRanking = ref([])

// 饼图配置
const pieChartOption = ref({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)'// 显示百分比
  },
  legend: {
    orient: 'vertical',
    left: 'left',
  },
  series: [
    {
      type: 'pie',
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter: '{b}\n{d}%' // 饼图上显示百分比
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
})

// 商品销售详情相关
const detailsPagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})
const productDetails = ref([])
const loading = ref(false)

const handleDetailsSizeChange = (newSize) => {
  detailsPagination.value.pageSize = newSize
  loadProductDetails()
}

const handleDetailsCurrentChange = (newPage) => {
  detailsPagination.value.currentPage = newPage
  loadProductDetails()
}

const loadBasicData = () => {
  //加载基础数据
  request.get('/admin/form/basic').then(res => {
    basicData.value = res.data
  })
}

const loadProductDetails = () => {
  request.get('/admin/form/detail', {
    params: {
      page: detailsPagination.value.currentPage,
      pageSize: detailsPagination.value.pageSize
    }
  }).then(res => {
    productDetails.value = res.data.rows
    detailsPagination.value.total = res.data.total
  })
}

const loadPieData = () => {
  //加载饼图数据
  request.get('/admin/form/category').then(res => {
    const data = res.data
    // 计算总销售额
    const total = data.reduce((sum, item) => sum + item.totalSales, 0)
      
    pieChartOption.value.series[0].data = data.map(item => ({
      value: item.categoryTotalSales,
      name: item.category,
      percentage: ((item.totalSales / total) * 100).toFixed(1) // 计算百分比
    }))
  })
} 

const loadSalesData = () => {
  //加载排名数据
  request.get('/admin/form/rank').then(res => {
    productRanking.value = res.data.map((item, index) => ({
      ...item,
      rank: index + 1
    }))
  })
}

onMounted(() => {
  loadSalesData()
  loadPieData()
  loadProductDetails()
  loadBasicData()
})
</script>

<style scoped>
.sales-report {
  padding: 20px;
  background: white;
  border-radius: 8px;
}

.data-cards {
  margin-bottom: 20px;
}

.data-card {
  height: 120px;
}

.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.card-footer {
  font-size: 12px;
  color: #606266;
}

.rank-section {
  margin-bottom: 20px;
}

.rank-header {
  font-weight: bold;
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
}

.price {
  color: #f56c6c;
  font-weight: 500;
}

.profit {
  color: #67c23a;
  font-weight: 500;
}

.pie-chart {
  height: 450px;
}

.chart {
  width: 100%;
  height: 100%;
}

.sales-details {
  margin-bottom: 20px;
}

.details-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-detail {
  display: flex;
  flex-direction: column;
}

.product-name {
  font-weight: 500;
}

.product-category {
  font-size: 12px;
  color: #909399;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

:deep(.el-card__header) {
  padding: 12px 20px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style>    