<template>
  <div class="create-explorer">
    <!-- 选择探查对象 -->
    <div class="section">
      <div class="section-title">选择探查对象</div>
      <div class="section-content">
        <el-form :model="form" label-width="120px" class="explorer-form">
          <el-form-item label="数据源类型:" required>
            <el-select 
              v-model="form.dataSourceType" 
              placeholder="请选择" 
              class="form-select"
              @change="onTypeChange"
            >
              <el-option 
                v-for="type in dataSourceTypes" 
                :key="type.code" 
                :label="type.name" 
                :value="type.code" 
              />
            </el-select>
          </el-form-item>

          <el-form-item label="数据源/引擎:" required>
            <el-select 
              v-model="form.dataSource" 
              placeholder="请选择数据源" 
              class="form-select"
              @change="onSourceChange"
              :disabled="!dataSources.length"
            >
              <el-option 
                v-for="source in dataSources" 
                :key="source.id" 
                :label="source.name" 
                :value="source.id" 
              />
            </el-select>
          </el-form-item>

          <el-form-item label="库名:" required>
            <el-select 
              v-model="form.database" 
              placeholder="选择库" 
              class="form-select"
              @change="onDatabaseChange"
              :disabled="!databases.length"
            >
              <el-option 
                v-for="db in databases" 
                :key="db.id" 
                :label="db.name" 
                :value="db.id" 
              />
            </el-select>
          </el-form-item>

          <el-form-item label="表名:" required>
            <el-select 
              v-model="form.table" 
              placeholder="选择表" 
              class="form-select"
              :disabled="!tables.length"
            >
              <el-option 
                v-for="table in tables" 
                :key="table.id" 
                :label="table.name" 
                :value="table.id" 
              />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 探查设置 -->
    <div class="section">
      <div class="section-title">探查设置</div>
      <div class="section-content">
        <el-checkbox v-model="form.fieldExplorer" class="field-checkbox">
          字段探查：包含各字段NULL值，0值，数值分布，枚举值等指标。
          <el-link type="primary" :underline="false" class="detail-link">详细设置</el-link>
        </el-checkbox>
      </div>
    </div>

    <!-- 高级参数设置 -->
    <div class="section">
      <div class="advanced-header" @click="showAdvanced = !showAdvanced">
        <el-icon class="arrow-icon" :class="{ expanded: showAdvanced }">
          <ArrowRight />
        </el-icon>
        <span>高级参数设置</span>
      </div>
      <div v-show="showAdvanced" class="advanced-content">
        <el-form :model="advancedForm" label-width="120px">
          <el-form-item label="采样比例:">
            <el-slider v-model="advancedForm.sampleRate" :max="100" show-input />
          </el-form-item>
          <el-form-item label="并行度:">
            <el-input-number v-model="advancedForm.parallelism" :min="1" :max="10" />
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 提示信息 -->
    <el-alert
      type="warning"
      :closable="false"
      class="warning-alert"
    >
      <template #title>
        <el-icon><Warning /></el-icon>
        <span>探查将消耗一定计算资源，若数量较大，探查运行时间将会变长，运行完成后，会对你发送Lark通知。</span>
      </template>
    </el-alert>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button @click="resetForm">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submitForm">确定</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ArrowRight, Warning } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const API_BASE = 'http://localhost:8080/api'

// 数据列表
const dataSourceTypes = ref<any[]>([])
const dataSources = ref<any[]>([])
const databases = ref<any[]>([])
const tables = ref<any[]>([])
const submitting = ref(false)

const form = reactive({
  dataSourceType: '',
  dataSource: '',
  database: '',
  table: '',
  fieldExplorer: true,
})

const showAdvanced = ref(false)

const advancedForm = reactive({
  sampleRate: 100,
  parallelism: 4,
})

// 获取数据源类型
const fetchDataSourceTypes = async () => {
  try {
    const res = await fetch(`${API_BASE}/datasource/types`)
    const result = await res.json()
    if (result.code === 200) {
      dataSourceTypes.value = result.data
      if (result.data.length > 0) {
        form.dataSourceType = result.data[0].code
        await fetchDataSources(result.data[0].code)
      }
    }
  } catch (error) {
    ElMessage.error('获取数据源类型失败')
  }
}

// 获取数据源
const fetchDataSources = async (typeCode: string) => {
  try {
    const res = await fetch(`${API_BASE}/datasource/list?typeCode=${typeCode}`)
    const result = await res.json()
    if (result.code === 200) {
      dataSources.value = result.data
      form.dataSource = ''
      form.database = ''
      form.table = ''
      databases.value = []
      tables.value = []
    }
  } catch (error) {
    ElMessage.error('获取数据源失败')
  }
}

// 获取数据库
const fetchDatabases = async (dataSourceId: string) => {
  try {
    const res = await fetch(`${API_BASE}/datasource/${dataSourceId}/databases`)
    const result = await res.json()
    if (result.code === 200) {
      databases.value = result.data
      form.database = ''
      form.table = ''
      tables.value = []
    }
  } catch (error) {
    ElMessage.error('获取数据库失败')
  }
}

// 获取表
const fetchTables = async (databaseId: string) => {
  try {
    const res = await fetch(`${API_BASE}/datasource/database/${databaseId}/tables`)
    const result = await res.json()
    if (result.code === 200) {
      tables.value = result.data
      form.table = ''
    }
  } catch (error) {
    ElMessage.error('获取表失败')
  }
}

// 事件处理
const onTypeChange = (typeCode: string) => {
  fetchDataSources(typeCode)
}

const onSourceChange = (sourceId: string) => {
  fetchDatabases(sourceId)
}

const onDatabaseChange = (dbId: string) => {
  fetchTables(dbId)
}

// 提交表单
const submitForm = async () => {
  if (!form.dataSource || !form.database || !form.table) {
    ElMessage.warning('请完整填写表单')
    return
  }
  
  submitting.value = true
  try {
    const res = await fetch(`${API_BASE}/task/create`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        dataSourceId: form.dataSource,
        databaseId: form.database,
        tableId: form.table,
        fieldExplorer: form.fieldExplorer,
        sampleRate: advancedForm.sampleRate,
        parallelism: advancedForm.parallelism,
      })
    })
    const result = await res.json()
    if (result.code === 200) {
      ElMessage.success('创建探查任务成功')
      resetForm()
    } else {
      ElMessage.error(result.message || '创建失败')
    }
  } catch (error) {
    ElMessage.error('创建探查任务失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  form.dataSource = ''
  form.database = ''
  form.table = ''
  form.fieldExplorer = true
  advancedForm.sampleRate = 100
  advancedForm.parallelism = 4
  dataSources.value = []
  databases.value = []
  tables.value = []
  if (dataSourceTypes.value.length > 0) {
    form.dataSourceType = dataSourceTypes.value[0].code
    fetchDataSources(form.dataSourceType)
  }
}

onMounted(() => {
  fetchDataSourceTypes()
})
</script>

<style scoped>
.create-explorer {
  padding: 24px;
  max-width: 800px;
}

.section {
  margin-bottom: 32px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e4e7ed;
}

.section-content {
  padding-left: 16px;
}

.explorer-form {
  max-width: 600px;
}

.form-select {
  width: 320px;
}

.field-checkbox {
  font-size: 14px;
  color: #606266;
}

.detail-link {
  margin-left: 8px;
  font-size: 14px;
}

.advanced-header {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 12px 0;
  color: #606266;
  font-size: 14px;
  user-select: none;
}

.advanced-header:hover {
  color: #409eff;
}

.arrow-icon {
  margin-right: 8px;
  transition: transform 0.3s;
}

.arrow-icon.expanded {
  transform: rotate(90deg);
}

.advanced-content {
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  margin-top: 8px;
}

.warning-alert {
  margin: 24px 0;
  background-color: #fdf6ec;
  border-color: #faecd8;
}

.warning-alert :deep(.el-alert__title) {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #e6a23c;
  font-size: 14px;
}

.action-buttons {
  display: flex;
  justify-content: flex-start;
  gap: 12px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e4e7ed;
}
</style>
