<template>
  <div class="messages">
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span class="logo-text">青志汇</span>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <i class="el-icon-user"></i>
              {{ user?.name || user?.username }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px" class="sidebar">
          <el-menu :default-active="activeMenu" mode="vertical" class="menu">
            <el-menu-item index="/" @click="navigateTo('/')">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/members" @click="navigateTo('/members')">
              <i class="el-icon-user"></i>
              <span>成员管理</span>
            </el-menu-item>
            <el-menu-item index="/teams" @click="navigateTo('/teams')">
              <i class="el-icon-users"></i>
              <span>服务队</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <el-main class="main-content">
          <div class="page-header">
            <h2 class="page-title">消息中心</h2>
            <div class="header-actions">
              <el-button type="default" @click="markAllRead">
                全部已读
              </el-button>
              <el-button type="danger" @click="deleteSelected">
                删除选中
              </el-button>
            </div>
          </div>
          
          <div class="tabs">
            <el-tabs v-model="activeTab" @tab-click="handleTabClick">
              <el-tab-pane label="全部" name="all">
                <div class="message-filters">
                  <el-tag 
                    v-for="filter in filters" 
                    :key="filter.value"
                    :class="['filter-tag', { active: currentFilter === filter.value }]"
                    @click="currentFilter = filter.value"
                  >
                    {{ filter.label }}
                    <span v-if="filter.count > 0" class="tag-count">{{ filter.count }}</span>
                  </el-tag>
                </div>
              </el-tab-pane>
              <el-tab-pane label="未读" name="unread">
                <div class="message-list">
                  <el-card 
                    v-for="msg in unreadMessages" 
                    :key="msg.id" 
                    :class="['message-card', { selected: selectedIds.includes(msg.id) }]"
                    @click="toggleSelect(msg.id)"
                  >
                    <div class="message-header">
                      <div class="msg-checkbox">
                        <el-checkbox v-model="selectedIds" :label="msg.id" />
                      </div>
                      <div class="msg-icon" :class="msg.type">
                        <i :class="msg.icon"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ msg.time }}</div>
                      </div>
                      <span class="unread-dot"></span>
                    </div>
                    <p class="msg-content">{{ msg.content }}</p>
                  </el-card>
                </div>
              </el-tab-pane>
              <el-tab-pane label="系统通知" name="system">
                <div class="message-list">
                  <el-card 
                    v-for="msg in systemMessages" 
                    :key="msg.id" 
                    :class="['message-card', { selected: selectedIds.includes(msg.id), read: msg.read }]"
                    @click="toggleSelect(msg.id)"
                  >
                    <div class="message-header">
                      <div class="msg-checkbox">
                        <el-checkbox v-model="selectedIds" :label="msg.id" />
                      </div>
                      <div class="msg-icon system">
                        <i class="el-icon-bell"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ msg.time }}</div>
                      </div>
                      <span v-if="!msg.read" class="unread-dot"></span>
                    </div>
                    <p class="msg-content">{{ msg.content }}</p>
                  </el-card>
                </div>
              </el-tab-pane>
              <el-tab-pane label="活动通知" name="activity">
                <div class="message-list">
                  <el-card 
                    v-for="msg in activityMessages" 
                    :key="msg.id" 
                    :class="['message-card', { selected: selectedIds.includes(msg.id), read: msg.read }]"
                    @click="toggleSelect(msg.id)"
                  >
                    <div class="message-header">
                      <div class="msg-checkbox">
                        <el-checkbox v-model="selectedIds" :label="msg.id" />
                      </div>
                      <div class="msg-icon activity">
                        <i class="el-icon-calendar"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ msg.time }}</div>
                      </div>
                      <span v-if="!msg.read" class="unread-dot"></span>
                    </div>
                    <p class="msg-content">{{ msg.content }}</p>
                  </el-card>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getUser, logout } from '@/utils/auth'

const user = ref(getUser())
const activeMenu = ref('/messages')
const activeTab = ref('all')
const currentFilter = ref('all')
const selectedIds = ref([])

const messages = ref([
  {
    id: 1,
    title: '新活动通知',
    content: '【爱心家园】社区帮扶活动将于本周末举行，请志愿者们积极报名参加。',
    time: '5分钟前',
    type: 'activity',
    icon: 'el-icon-calendar',
    read: false
  },
  {
    id: 2,
    title: '系统维护通知',
    content: '系统将于今晚22:00-24:00进行维护升级，请提前保存数据。',
    time: '1小时前',
    type: 'system',
    icon: 'el-icon-bell',
    read: false
  },
  {
    id: 3,
    title: '值班安排更新',
    content: '您本周三的值班时间已调整为下午14:00-16:00，请留意查看。',
    time: '3小时前',
    type: 'activity',
    icon: 'el-icon-clock',
    read: true
  },
  {
    id: 4,
    title: '服务队会议',
    content: '本周六上午9点将召开服务队全体会议，请准时参加。',
    time: '昨天',
    type: 'system',
    icon: 'el-icon-user',
    read: true
  },
  {
    id: 5,
    title: '活动报名成功',
    content: '您已成功报名参加"校园环保志愿行"活动，请准时参加。',
    time: '昨天',
    type: 'activity',
    icon: 'el-icon-check',
    read: false
  }
])

const filters = computed(() => [
  { label: '全部', value: 'all', count: messages.value.length },
  { label: '未读', value: 'unread', count: messages.value.filter(m => !m.read).length },
  { label: '系统通知', value: 'system', count: messages.value.filter(m => m.type === 'system').length },
  { label: '活动通知', value: 'activity', count: messages.value.filter(m => m.type === 'activity').length }
])

const unreadMessages = computed(() => messages.value.filter(m => !m.read))
const systemMessages = computed(() => messages.value.filter(m => m.type === 'system'))
const activityMessages = computed(() => messages.value.filter(m => m.type === 'activity'))

const handleLogout = () => {
  logout()
}

const navigateTo = (path) => {
  window.location.href = path
}

const handleTabClick = () => {
  selectedIds.value = []
}

const toggleSelect = (id) => {
  const index = selectedIds.value.indexOf(id)
  if (index > -1) {
    selectedIds.value.splice(index, 1)
  } else {
    selectedIds.value.push(id)
  }
  const msg = messages.value.find(m => m.id === id)
  if (msg && !msg.read) {
    msg.read = true
  }
}

const markAllRead = () => {
  messages.value.forEach(msg => msg.read = true)
  ElMessage.success('已全部标记为已读')
}

const deleteSelected = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的消息')
    return
  }
  messages.value = messages.value.filter(m => !selectedIds.value.includes(m.id))
  selectedIds.value = []
  ElMessage.success('删除成功')
}
</script>

<style scoped>
.messages {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.header-left .logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #ffffff;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #ffffff;
  cursor: pointer;
}

.sidebar {
  min-height: calc(100vh - 60px);
}

.menu {
  border-right: none;
  height: 100%;
}

.main-content {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.tabs {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.message-filters {
  display: flex;
  gap: 12px;
  padding: 16px;
  flex-wrap: wrap;
}

.filter-tag {
  padding: 6px 16px;
  border-radius: 20px;
  background: #f5f5f5;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-tag.active {
  background: #DC143C;
  color: #fff;
}

.tag-count {
  margin-left: 8px;
  background: #fff;
  color: #DC143C;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.filter-tag.active .tag-count {
  background: rgba(255, 255, 255, 0.3);
  color: #fff;
}

.message-list {
  padding: 16px;
}

.message-card {
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.message-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.message-card.selected {
  border: 2px solid #DC143C;
}

.message-card.read {
  opacity: 0.7;
}

.message-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.msg-checkbox {
  flex-shrink: 0;
}

.msg-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  color: #fff;
}

.msg-icon.system {
  background: linear-gradient(135deg, #409eff 0%, #67b8ff 100%);
}

.msg-icon.activity {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.msg-icon.default {
  background: linear-gradient(135deg, #dc143c 0%, #ff6b6b 100%);
}

.msg-info {
  flex: 1;
}

.msg-title {
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.msg-time {
  font-size: 12px;
  color: #999;
}

.unread-dot {
  width: 8px;
  height: 8px;
  background: #DC143C;
  border-radius: 50%;
}

.msg-content {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .header-actions {
    width: 100%;
    justify-content: space-between;
  }
}
</style>