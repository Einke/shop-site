import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'  // 引入路由实例

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000  // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    let token = localStorage.getItem('token')
    if(token){
      config.headers['token'] = token;
    }
    //console.log('请求头:', config.headers)
    return config
  },
  error => {
    //console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 根据后端的返回码进行处理
    // 处理未登录情况
    if (res.code === 0 && res.msg === 'NOT_LOGIN') {
      ElMessage.error('请先登录')
      localStorage.removeItem('token')  // 清除token
      router.push('/login')  // 跳转到登录页
      return Promise.reject(new Error('未登录'))
    }
    
    if (res.code === 1) {
      return res
    } else {
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:  // token过期或未登录
          ElMessage.error('请重新登录')
          localStorage.removeItem('token')  // 清除token
          router.push('/login')  // 跳转到登录页
          break
        case 403:  // 权限不足
          ElMessage.error('没有权限')
          break
        case 404:  // 请求的资源不存在
          ElMessage.error('请求的资源不存在')
          break
        case 500:  // 服务器错误
          ElMessage.error('服务器错误')
          break
        default:
          ElMessage.error('网络错误')
      }
    } else {
      ElMessage.error('网络连接失败')
    }
    return Promise.reject(error)
  }
)

export default request
