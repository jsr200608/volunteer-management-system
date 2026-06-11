
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
            <el-menu-item index="/teams" @click="navigateTo('/teams')">
              <i class="el-icon-users"></i>
              <span>服务队</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <el-main class="main-content">
          <div class="page-header">
            <el-button icon="ArrowLeft" @click="goBack">返回</el-button>
            <h2 class="page-title">成员详情</h2>
          </div>
          
          <div class="detail-card" v-if="member">
            <div class="card-header">
              <div class="avatar">
                <span class="avatar-text">{{ member.name.charAt(0) }}</span>
              </div>
              <div class="basic-info">
                <h3 class="member-name">{{ member.name }}</h3>
                <span class="member-id">{{ member.studentId }}</span>
              </div>
              <el-tag :type="member.status === 'ACTIVE' ? 'success' : 'warning'" class="status-tag">
                {{ member.status === 'ACTIVE' ? '在职' : '离职' }}
              </el-tag>
            </div>
            
            <div class="card-body">
              <div class="info-row">
                <div class="info-label">性别</div>
                <div class="info-value">{{ member.gender || '-' }}</div>
              </div>
              <div class="info-row">
                <div class="info-label">年级</div>
                <div class="info-value">{{ member.grade || '-' }}</div>
              </div>
              <div class="info-row">
                <div class="info-label">学院</div>
                <div class="info-value">{{ member.college || '-' }}</div>
              </div>
              <div class="info-row">
                <div class="info-label">专业</div>
                <div class="info-value">{{ member.major || '-' }}</div>
              </div>
              <div class="info-row">
                <div class="info-label">手机号</div>
                <div class="info-value">{{ member.phone || '-' }}</div>
              </div>
              <div class="info-row">
                <div class="info-label">邮箱</div>
                <div class="info-value">{{ member.email || '-' }}</div>
              </div>
              <div class="info-row">
                <div class="info-label">志愿时长</div>
                <div class="info-value highlight">{{ member.volunteerHours || 0 }} 小时</div>
              </div>
              <div class="info-row">
                <div class="info-label">加入时间</div>
                <div class="info-value">{{ member.joinDate || '-' }}</div>
              </div>
            </div>
            
            <div class="card-footer">
              <el-button type="primary" @click="handleEdit">编辑</el-button>
              <el-button @click="goBack">返回列表</el-button>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUser, logout } from '@/utils/auth'
import { getMember } from '@/api/member'

const user = ref(getUser())
const activeMenu = ref('/members')
const member = ref(null)

const navigateTo = (path) => {
  window.location.href = path
}

const handleLogout = () => {
  logout()
}

const goBack = () => {
  window.history.back()
}

const handleEdit = () => {
  window.location.href = `/members`
}

onMounted(async () => {
  const path = window.location.pathname
  const id = path.split('/').pop()
  
  try {
    const response = await getMember(id)
    if (response.code === 200) {
      member.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取成员信息失败')
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
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  color: #333333;
}

.detail-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #DC143C 0%, #FF6B6B 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-text {
  font-size: 32px;
  color: #ffffff;
  font-weight: bold;
}

.basic-info {
  flex: 1;
}

.member-name {
  font-size: 24px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 4px;
}

.member-id {
  font-size: 14px;
  color: #999999;
}

.status-tag {
  padding: 4px 12px;
}

.card-body {
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-label {
  width: 120px;
  font-weight: bold;
  color: #666666;
}

.info-value {
  flex: 1;
  color: #333333;
}

.info-value.highlight {
  color: #DC143C;
  font-weight: bold;
  font-size: 16px;
}

.card-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}
</style>
