<template>
  <div class="side-nav">
    <div class="nav-header">
      <div class="logo">
        <el-icon :size="24"><DataLine /></el-icon>
        <span>Data Explorer</span>
      </div>
      <div v-if="userStore.currentUser" class="user-info">
        <el-icon><User /></el-icon>
        <span>{{ userStore.currentUser.userName }}</span>
      </div>
    </div>
    <div class="nav-menu">
      <div
        v-for="item in menuItems"
        :key="item.key"
        :class="['nav-item', { active: activeKey === item.key }]"
        @click="handleClick(item)"
      >
        <el-icon class="nav-icon">
          <component :is="item.icon" />
        </el-icon>
        <span class="nav-text">{{ item.label }}</span>
        <span v-if="item.hot" class="hot-tag">HOT</span>
      </div>
    </div>
    <div class="nav-footer">
      <div class="nav-item logout-item" @click="handleLogout">
        <el-icon class="nav-icon"><SwitchButton /></el-icon>
        <span class="nav-text">退出登录</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { Compass, Clock, Document, DataLine, User, SwitchButton } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const activeKey = ref('create')

const menuItems = [
  { key: 'create', label: '创建探查', icon: Compass, path: '/' },
  { key: 'scheduled', label: '创建定期探查', icon: Clock, hot: true },
  { key: 'results', label: '探查结果', icon: Document },
  { key: 'dynamic', label: '动态探查', icon: DataLine, hot: true },
  { key: 'users', label: '用户管理', icon: User, path: '/users' },
]

// 根据当前路由设置激活的菜单项
watch(() => route.path, (newPath) => {
  const item = menuItems.find(item => item.path === newPath)
  if (item) {
    activeKey.value = item.key
  }
}, { immediate: true })

const handleClick = (item: any) => {
  activeKey.value = item.key
  if (item.path) {
    router.push(item.path)
  }
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const success = await userStore.logout()
    if (success) {
      ElMessage.success('已退出登录')
      router.push('/login')
    } else {
      ElMessage.error('退出登录失败')
    }
  } catch (error) {
    // 用户取消
  }
}
</script>

<style scoped>
.side-nav {
  width: 200px;
  height: 100%;
  background-color: #ffffff;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
}

.nav-header {
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #606266;
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-radius: 6px;
}

.nav-menu {
  padding: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin: 4px 8px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  border-radius: 6px;
  color: #606266;
}

.nav-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
}

.nav-item.active {
  background-color: #ecf5ff;
  color: #409eff;
  font-weight: 500;
}

.nav-item.active::before {
  content: '';
  position: absolute;
  left: -8px;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background-color: #409eff;
  border-radius: 0 2px 2px 0;
}

.nav-icon {
  font-size: 18px;
  margin-right: 12px;
  color: inherit;
}

.nav-text {
  font-size: 14px;
  flex: 1;
  color: inherit;
}

.hot-tag {
  background-color: #ff4d4f;
  color: white;
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 4px;
  font-weight: bold;
}

.nav-footer {
  margin-top: auto;
  padding: 8px;
  border-top: 1px solid #e4e7ed;
}

.logout-item {
  color: #f56c6c;
}

.logout-item:hover {
  background-color: #fef0f0;
  color: #f56c6c;
}
</style>
