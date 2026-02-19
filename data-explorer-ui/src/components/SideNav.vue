<template>
  <div class="side-nav">
    <div class="nav-menu">
      <div
        v-for="item in menuItems"
        :key="item.key"
        :class="['nav-item', { active: activeKey === item.key }]"
        @click="handleClick(item.key)"
      >
        <el-icon class="nav-icon">
          <component :is="item.icon" />
        </el-icon>
        <span class="nav-text">{{ item.label }}</span>
        <span v-if="item.hot" class="hot-tag">HOT</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Compass, Clock, Document, DataLine } from '@element-plus/icons-vue'

const activeKey = ref('create')

const menuItems = [
  { key: 'create', label: '创建探查', icon: Compass },
  { key: 'scheduled', label: '创建定期探查', icon: Clock, hot: true },
  { key: 'results', label: '探查结果', icon: Document },
  { key: 'dynamic', label: '动态探查', icon: DataLine, hot: true },
]

const handleClick = (key: string) => {
  activeKey.value = key
}
</script>

<style scoped>
.side-nav {
  width: 200px;
  height: 100%;
  background-color: #ffffff;
  border-right: 1px solid #e4e7ed;
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
</style>
