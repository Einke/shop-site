import { createApp } from 'vue'
import App from './App.vue'
import store from './store'

import ElementPlus from 'element-plus'  // 添加 Element Plus UI框架
import 'element-plus/dist/index.css'    // Element Plus 的样式
import router from './router'

const app = createApp(App)

// 注册插件
app.use(ElementPlus)
app.use(router)
app.use(store)

app.mount('#app')
