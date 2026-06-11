<template>
  <div class="activities">
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
            <h2 class="page-title">活动报名</h2>
            <el-button type="primary" icon="Plus" @click="showAddModal = true">
              报名活动
            </el-button>
          </div>
          
          <div class="search-bar">
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索活动名称" 
              prefix-icon="Search"
              class="search-input"
            />
            <el-select v-model="filterStatus" placeholder="活动状态">
              <el-option label="全部" value="" />
              <el-option label="报名中" value="active" />
              <el-option label="已结束" value="ended" />
            </el-select>
            <el-button type="default" @click="handleSearch">搜索</el-button>
          </div>
          
          <div class="activities-list">
            <el-card v-for="activity in activities" :key="activity.id" class="activity-card">
              <div class="activity-header">
                <h3 class="activity-title">{{ activity.title }}</h3>
                <span :class="['status-tag', activity.status]">
                  {{ activity.status === 'active' ? '报名中' : '已结束' }}
                </span>
              </div>
              <div class="activity-info">
                <div class="info-item">
                  <i class="el-icon-calendar"></i>
                  <span>{{ activity.date }}</span>
                </div>
                <div class="info-item">
                  <i class="el-icon-map-marker"></i>
                  <span>{{ activity.location }}</span>
                </div>
                <div class="info-item">
                  <i class="el-icon-user"></i>
                  <span>已报名: {{ activity.participants }}人</span>
                </div>
              </div>
              <p class="activity-description">{{ activity.description }}</p>
              <div class="activity-footer">
                <el-button 
                  v-if="activity.status === 'active'"
                  type="primary" 
                  size="small"
                  @click="handleSignup(activity)"
                >
                  立即报名
                </el-button>
                <span v-else class="disabled-text">报名已结束</span>
              </div>
            </el-card>
          </div>
          
          <el-pagination
            class="pagination"
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handlePageChange"
          />
        </el-main>
      </el-container>
    </el-container>
    
    <el-dialog title="报名活动" v-model="showAddModal" width="400px">
      <el-form :model="signupForm" label-width="80px">
        <el-form-item label="活动名称">
          <el-select v-model="signupForm.activityId" placeholder="请选择活动">
            <el-option 
              v-for="act in availableActivities" 
              :key="act.id" 
              :label="act.title" 
              :value="act.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="报名理由">
          <el-textarea v-model="signupForm.reason" placeholder="请填写报名理由" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="submitSignup">确认报名</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getUser, logout } from '@/utils/auth'

const user = ref(getUser())
const activeMenu = ref('/activities')
const searchKeyword = ref('')
const filterStatus = ref('')
const showAddModal = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(20)

const activities = ref([
  {
    id: 1,
    title: '社区爱心帮扶活动',
    date: '2024-03-15 09:00',
    location: '阳光社区',
    participants: 25,
    description: '关爱社区孤寡老人，为他们送去温暖和关怀，帮助打扫卫生、聊天陪伴。',
    status: 'active'
  },
  {
    id: 2,
    title: '校园环保志愿行',
    date: '2024-03-16 14:00',
    location: '学校操场',
    participants: 30,
    description: '清理校园垃圾，宣传环保理念，美化校园环境。',
    status: 'active'
  },
  {
    id: 3,
    title: '文化之旅博物馆讲解',
    date: '2024-03-17 10:00',
    location: '市博物馆',
    participants: 15,
    description: '为参观博物馆的游客提供讲解服务，传播历史文化知识。',
    status: 'active'
  },
  {
    id: 4,
    title: '交通安全宣传活动',
    date: '2024-03-10 09:00',
    location: '市中心广场',
    participants: 20,
    description: '宣传交通安全知识，协助交警维持交通秩序。',
    status: 'ended'
  }
])

const availableActivities = ref([
  { id: 1, title: '社区爱心帮扶活动' },
  { id: 2, title: '校园环保志愿行' },
  { id: 3, title: '文化之旅博物馆讲解' }
])

const signupForm = reactive({
  activityId: '',
  reason: ''
})

const handleLogout = () => {
  logout()
}

const navigateTo = (path) => {
  window.location.href = path
}

const handleSearch = () => {
  ElMessage.success('搜索功能已触发')
}

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleSignup = (activity) => {
  signupForm.activityId = activity.id
  showAddModal.value = true
}

const submitSignup = () => {
  if (!signupForm.activityId) {
    ElMessage.warning('请选择活动')
    return
  }
  if (!signupForm.reason) {
    ElMessage.warning('请填写报名理由')
    return
  }
  ElMessage.success('报名成功！')
  showAddModal.value = false
  signupForm.activityId = ''
  signupForm.reason = ''
}
</script>

<style scoped>
.activities {
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

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.activities-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.activity-card {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.activity-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.status-tag {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.status-tag.active {
  background: #E8F5E9;
  color: #2E7D32;
}

.status-tag.ended {
  background: #F5F5F5;
  color: #9E9E9E;
}

.activity-info {
  display: flex;
  gap: 20px;
  margin-bottom: 12px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #666;
}

.activity-description {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
}

.activity-footer {
  display: flex;
  justify-content: flex-end;
}

.disabled-text {
  color: #999;
  font-size: 14px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

@media (max-width: 768px) {
  .activities-list {
    grid-template-columns: 1fr;
  }
  
  .search-bar {
    flex-wrap: wrap;
  }
  
  .search-input {
    width: 100%;
  }
}
</style>