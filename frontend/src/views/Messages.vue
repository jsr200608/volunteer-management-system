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
          </div>
          
          <div class="tabs">
            <el-tabs v-model="activeTab">
              <el-tab-pane label="全部" name="all">
                <div class="message-list">
                  <el-card v-for="msg in messages" :key="msg.id" class="message-card">
                    <div class="message-header">
                      <div class="msg-icon" :class="msg.type">
                        <i :class="getIcon(msg.type)"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ formatTime(msg.time) }}</div>
                      </div>
                    </div>
                    <p class="msg-content">{{ msg.content }}</p>
                  </el-card>
                </div>
              </el-tab-pane>
              <el-tab-pane label="未读" name="unread">
                <div class="message-list">
                  <el-card v-for="msg in messages.filter(m => !m.read)" :key="msg.id" class="message-card">
                    <div class="message-header">
                      <div class="msg-icon" :class="msg.type">
                        <i :class="getIcon(msg.type)"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ formatTime(msg.time) }}</div>
                      </div>
                    </div>
                    <p class="msg-content">{{ msg.content }}</p>
                  </el-card>
                </div>
              </el-tab-pane>
              <el-tab-pane label="系统通知" name="system">
                <div class="message-list">
                  <el-card v-for="msg in messages.filter(m => m.type === 'system')" :key="msg.id" class="message-card">
                    <div class="message-header">
                      <div class="msg-icon system">
                        <i class="el-icon-bell"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ formatTime(msg.time) }}</div>
                      </div>
                    </div>
                    <p class="msg-content">{{ msg.content }}</p>
                  </el-card>
                </div>
              </el-tab-pane>
              <el-tab-pane label="活动通知" name="activity">
                <div class="message-list">
                  <el-card v-for="msg in messages.filter(m => m.type === 'activity')" :key="msg.id" class="message-card">
                    <div class="message-header">
                      <div class="msg-icon activity">
                        <i class="el-icon-calendar"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ formatTime(msg.time) }}</div>
                      </div>
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
import { ref, computed, onMounted } from 'vue'
import { getUser, logout } from '@/utils/auth'

const user = ref(getUser())
const activeMenu = ref('/messages')
const activeTab = ref('all')

const messages = ref([
  {
    id: 1,
    title: '欢迎加入青志汇',
    content: '感谢您注册成为志愿者，期待您的参与！',
    type: 'system',
    read: true,
    time: '2024-01-15 10:00:00'
  },
  {
    id: 2,
    title: '新活动上线：校园环保行动',
    content: '本周六我们将组织校园环保清洁活动，欢迎大家报名参加！',
    type: 'activity',
    read: false,
    time: '2024-01-14 15:30:00'
  },
  {
    id: 3,
    title: '值班提醒',
    content: '请本周值班志愿者准时到岗，谢谢配合！',
    type: 'duty',
    read: true,
    time: '2024-01-13 09:00:00'
  }
])

const getIcon = (type) => {
  const icons = {
    'system': 'el-icon-bell',
    'activity': 'el-icon-calendar',
    'duty': 'el-icon-clock',
    'team': 'el-icon-users'
  }
  return icons[type] || 'el-icon-info'
}

const formatTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString('zh-CN')
}

const handleLogout = () => {
  logout()
}

const navigateTo = (path) => {
  window.location.href = path
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

.tabs {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.message-list {
  padding: 16px;
}

.message-card {
  margin-bottom: 12px;
}

.message-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
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

.msg-icon.duty {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
}

.msg-icon.team {
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
}
</style>
