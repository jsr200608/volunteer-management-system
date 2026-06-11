
<template>
  <div class="dashboard">
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
            <el-menu-item index="/">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/members">
              <i class="el-icon-user"></i>
              <span>成员管理</span>
            </el-menu-item>
            <el-menu-item index="/teams">
              <i class="el-icon-users"></i>
              <span>服务队</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <el-main class="main-content">
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon members-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ memberCount }}</div>
                <div class="stat-label">总成员数</div>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon activity-icon">
                <i class="el-icon-calendar"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">0</div>
                <div class="stat-label">今日活动</div>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon duty-icon">
                <i class="el-icon-clock"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">0</div>
                <div class="stat-label">今日值班</div>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon hours-icon">
                <i class="el-icon-timer"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">0.00</div>
                <div class="stat-label">总志愿时长</div>
              </div>
            </div>
          </div>
          
          <div class="quick-actions">
            <h3 class="section-title">快捷入口</h3>
            <div class="action-grid">
              <el-button 
                type="primary" 
                icon="User" 
                class="action-btn"
                @click="navigateTo('/members')"
              >
                成员管理
              </el-button>
              <el-button 
                type="primary" 
                icon="Calendar" 
                class="action-btn"
                @click="navigateTo('/activities')"
              >
                活动报名
              </el-button>
              <el-button 
                type="primary" 
                icon="Clock" 
                class="action-btn"
                @click="navigateTo('/duty')"
              >
                值班安排
              </el-button>
              <el-button 
                type="primary" 
                icon="Bell" 
                class="action-btn"
                @click="navigateTo('/messages')"
              >
                消息中心
              </el-button>
            </div>
          </div>
          
          <div class="welcome-section">
            <div class="welcome-card">
              <h2>🎉 欢迎回来！</h2>
              <p>今天是 {{ currentDate }}，让我们一起传递温暖，奉献爱心！</p>
              <div class="quote">
                <span>"奉献是用爱心铸成的一道彩虹，五颜六色，清新飘逸，带给人们温馨与快乐。"</span>
              </div>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUser, logout } from '@/utils/auth'
import { getMembers } from '@/api/member'

const user = ref(getUser())
const activeMenu = ref('/')
const memberCount = ref(0)

const currentDate = computed(() => {
  const date = new Date()
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
})

const handleLogout = () => {
  logout()
}

const navigateTo = (path) => {
  window.location.href = path
}

onMounted(async () => {
  try {
    const response = await getMembers({ page: 0, size: 1 })
    if (response.code === 200) {
      memberCount.value = response.data.totalElements
    }
  } catch (error) {
    console.error('获取成员数据失败', error)
  }
})
</script>

<style scoped>
.dashboard {
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  color: #ffffff;
}

.members-icon {
  background: linear-gradient(135deg, #DC143C 0%, #FF6B6B 100%);
}

.activity-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.duty-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
}

.hours-icon {
  background: linear-gradient(135deg, #409eff 0%, #67b8ff 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333333;
}

.stat-label {
  font-size: 14px;
  color: #999999;
}

.quick-actions {
  background: #ffffff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 16px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.action-btn {
  height: 48px;
  font-size: 14px;
}

.welcome-section {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.welcome-card h2 {
  color: #DC143C;
  margin-bottom: 8px;
}

.welcome-card p {
  color: #666666;
  margin-bottom: 16px;
}

.quote {
  background: #FFF5F5;
  border-left: 4px solid #DC143C;
  padding: 12px 16px;
  border-radius: 0 8px 8px 0;
}

.quote span {
  font-style: italic;
  color: #666666;
  font-size: 14px;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .action-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .action-grid {
    grid-template-columns: 1fr;
  }
}
</style>
