
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
            <el-menu-item index="/" @click="navigateTo('/')">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/members" @click="navigateTo('/members')">
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
          <div class="page-header">
            <h2 class="page-title">服务队</h2>
          </div>
          
          <div class="teams-grid">
            <div 
              class="team-card" 
              v-for="team in teams" 
              :key="team.id"
              @click="showTeamDetail(team)"
            >
              <div class="team-icon" :class="getTeamIconClass(team.name)">
                <i class="el-icon-users"></i>
              </div>
              <h3 class="team-name">{{ team.name }}</h3>
              <p class="team-desc">{{ team.description }}</p>
              <el-button type="primary" size="small" class="join-btn">
                加入服务队
              </el-button>
            </div>
          </div>
          
          <!-- 服务队详情弹窗 -->
          <el-dialog 
            :title="selectedTeam?.name" 
            :visible.sync="showDetailModal"
            width="500px"
          >
            <div v-if="selectedTeam" class="team-detail">
              <div class="detail-icon" :class="getTeamIconClass(selectedTeam.name)">
                <i class="el-icon-users"></i>
              </div>
              <p class="detail-desc">{{ selectedTeam.description }}</p>
              
              <div class="detail-section">
                <h4>服务队成员</h4>
                <p class="empty-text">暂无成员数据</p>
              </div>
              
              <div class="detail-section">
                <h4>近期活动</h4>
                <p class="empty-text">暂无活动数据</p>
              </div>
            </div>
            
            <template #footer>
              <el-button @click="showDetailModal = false">关闭</el-button>
              <el-button type="primary">申请加入</el-button>
            </template>
          </el-dialog>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUser, logout } from '@/utils/auth'
import { getTeams } from '@/api/team'

const user = ref(getUser())
const activeMenu = ref('/teams')
const teams = ref([])
const showDetailModal = ref(false)
const selectedTeam = ref(null)

const navigateTo = (path) => {
  window.location.href = path
}

const handleLogout = () => {
  logout()
}

const getTeamIconClass = (name) => {
  const classMap = {
    '爱心家园服务队': 'icon-heart',
    '绿色环保服务队': 'icon-leaf',
    '文化之旅服务队': 'icon-culture',
    '安全使者服务队': 'icon-shield'
  }
  return classMap[name] || 'icon-default'
}

const showTeamDetail = (team) => {
  selectedTeam.value = team
  showDetailModal.value = true
}

onMounted(async () => {
  try {
    const response = await getTeams()
    if (response.code === 200) {
      teams.value = response.data
    }
  } catch (error) {
    console.error('获取服务队列表失败', error)
  }
})
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
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
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  color: #333333;
}

.teams-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.team-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.team-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.team-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto 16px;
  font-size: 32px;
  color: #ffffff;
}

.icon-heart {
  background: linear-gradient(135deg, #DC143C 0%, #FF6B6B 100%);
}

.icon-leaf {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.icon-culture {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
}

.icon-shield {
  background: linear-gradient(135deg, #409eff 0%, #67b8ff 100%);
}

.team-name {
  font-size: 18px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 8px;
}

.team-desc {
  font-size: 14px;
  color: #666666;
  margin-bottom: 16px;
  line-height: 1.5;
}

.join-btn {
  width: 100%;
}

.team-detail {
  text-align: center;
}

.detail-icon {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto 16px;
  font-size: 40px;
  color: #ffffff;
}

.detail-desc {
  font-size: 14px;
  color: #666666;
  margin-bottom: 20px;
  line-height: 1.6;
}

.detail-section {
  background: #f5f5f5;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
  text-align: left;
}

.detail-section h4 {
  font-size: 14px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 8px;
}

.empty-text {
  font-size: 14px;
  color: #999999;
  text-align: center;
}

@media (max-width: 1200px) {
  .teams-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .teams-grid {
    grid-template-columns: 1fr;
  }
}
</style>
