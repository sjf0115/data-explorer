<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const form = reactive({
  userId: '',
  userName: '',
  password: '',
  confirmPassword: ''
})

const formRef = ref()

const validateConfirmPassword = (rule: any, value: string, callback: any) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  userId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' },
    { min: 3, max: 40, message: '长度在 3 到 40 个字符', trigger: 'blur' }
  ],
  userName: [
    { required: true, message: '请输入用户名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 100, message: '长度在 6 到 100 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const success = await userStore.createUser(form.userId, form.userName, form.password)
    if (success) {
      ElMessage.success('创建用户成功')
      router.push('/users')
    } else {
      ElMessage.error('创建用户失败，用户ID可能已存在')
    }
  } catch (error) {
    ElMessage.error('创建用户失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  router.back()
}
</script>

<template>
  <div class="user-create-container">
    <div class="page-header">
      <h2>创建用户</h2>
    </div>

    <el-card class="form-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="max-width: 500px"
      >
        <el-form-item label="用户ID" prop="userId">
          <el-input
            v-model="form.userId"
            placeholder="请输入用户ID"
            clearable
          />
        </el-form-item>

        <el-form-item label="用户名称" prop="userName">
          <el-input
            v-model="form.userName"
            placeholder="请输入用户名称"
            clearable
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            clearable
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            创建
          </el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.user-create-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.form-card {
  max-width: 600px;
}
</style>
