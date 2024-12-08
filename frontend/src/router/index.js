import { createRouter ,  createWebHistory} from 'vue-router'

import RegisterForm from '../views/RegisterForm.vue'
import LoginForm from '../views/LoginForm.vue'
import MyHome from '../views/MyHome.vue'
import ShopList from '../views/ShopList.vue'
import OrderList from '../views/OrderList.vue'
import AdminPerson from '../views/AdminPerson.vue'
import ShoppingCar from '../views/ShoppingCar.vue'
import UserProfile from '../views/UserProfile.vue'
import TheWelcome from '../views/TheWelcome.vue'
import ShopDetail from '../views/ProductDetail.vue'
import NotFound from '../views/NotFound.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: LoginForm
    },
    {
      path: '/register',
      name: 'Register',
      component: RegisterForm
    },
    {
      path:'/admin',
      name:'Admin',
      component:AdminPerson
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFound
    },
    {
      path: '/',
      name: 'MyHome',
      component: MyHome,
      children: [
        {
          path: '/',
          name: 'TheWelcome',
          component: TheWelcome
        },
        {
          path: '/shop',
          name: 'Shop',
          component: ShopList
        },
        {
          path: '/orders',
          name: 'Orders',
          component: OrderList
        },
        {
          path:'/car',
          name:'ShoppingCar',
          component:ShoppingCar
        },
        {
          path:'/profile',
          name:'Profile',
          component:UserProfile
        },
        {
          path:'/shop/:id',
          name:'ShopDetail',
          component:ShopDetail
        },
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userId = localStorage.getItem('userId')
  // 如果是管理员，除了 /admin 开头的路由，都重定向到 /admin
  if (userId === '5' && !to.path.startsWith('/admin')) {
    next('/admin')
  } 
  // 如果 userId 不是 5，且访问 /admin 路由，禁止访问，并回到首页
  else if (userId && userId !== '5' && to.path.startsWith('/admin')) {
    next('/') 
  }
  else {
    next()
  }
})

export default router