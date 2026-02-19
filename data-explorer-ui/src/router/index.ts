import { createRouter, createWebHistory } from 'vue-router'
import CreateExplorer from '../views/CreateExplorer.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'create-explorer',
      component: CreateExplorer,
    },
  ],
})

export default router
