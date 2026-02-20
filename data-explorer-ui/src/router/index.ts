import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import CreateExplorer from '../views/CreateExplorer.vue'
import LoginView from '../views/LoginView.vue'
import UserListView from '../views/UserListView.vue'
import UserCreateView from '../views/UserCreateView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { public: true }
    },
    {
      path: '/',
      name: 'create-explorer',
      component: CreateExplorer,
    },
    {
      path: '/users',
      name: 'user-list',
      component: UserListView,
    },
    {
      path: '/users/create',
      name: 'user-create',
      component: UserCreateView,
    },
  ],
})

// 路由守卫：检查登录状态
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  
  // 如果是公开页面，直接放行
  if (to.meta.public) {
    next()
    return
  }
  
  // 检查登录状态
  const isLoggedIn = await userStore.checkLoginStatus()
  
  if (!isLoggedIn) {
    // 未登录，跳转到登录页
    next('/login')
  } else {
    // 已登录，放行
    next()
  }
})

export default router
