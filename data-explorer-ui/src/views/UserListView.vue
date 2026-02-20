<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { User } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const userList = ref<User[]>([])

const fetchUserList = async () => {
  loading.value = true
  try {
    const list = await userStore.getUserList()
    userList.value = list
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  router.push('/users/create')
}

const handleDisable = async (user: User) => {
  try {
    await ElMessageBox.confirm(
      `确定要禁用用户 "${user.userName}" 吗？`,
      '确认禁用',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const success = await userStore.disableUser(user.userId)
    if (success) {
      ElMessage.success('禁用成功')
      fetchUserList()
    } else {
      ElMessage.error('禁用失败')
    }
  } catch (error) {
    // 用户取消操作
  }
}

const formatDate = (dateStr: string) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  fetchUserList()
})
</script>

<template>
  <div class="user-list-container">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        创建用户
      </el-button>
    </div>

    <el-card class="user-list-card">
      <el-table
        v-loading="loading"
        :data="userList"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="userId" label="用户ID" min-width="120" />
        <el-table-column prop="userName" label="用户名称" min-width="120" />
        <el-table-column prop="creator" label="创建者" min-width="120" />
        <el-table-column label="创建时间" min-width="160">
          <template #default="{ row }">
            {{ formatDate(row.gmtCreate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="danger"
              size="small"
              @click="handleDisable(row)"
              :disabled="row.userId === userStore.currentUser?.userId"
            >
              禁用
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && userList.length === 0" description="暂无用户数据" />
    </el-card>
  </div>
</template>

<style scoped>
.user-list-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.user-list-card {
  min-height: 400px;
}
</style>
