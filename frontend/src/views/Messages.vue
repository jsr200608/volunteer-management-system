
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
              <el-button type="danger" @click="showSendModal = true" v-if="isAdmin">
                发布通知
              </el-button>
              <el-button type="default" @click="handleMarkAllRead">
                全部已读
              </el-button>
              <el-button type="default" @click="deleteSelected">
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
                <div class="message-list">
                  <el-card 
                    v-for="msg in filteredMessages" 
                    :key="msg.id" 
                    :class="['message-card', { selected: selectedIds.includes(msg.id) }]"
                    @click="toggleSelect(msg.id)"
                  >
                    <div class="message-header">
                      <div class="msg-checkbox">
                        <el-checkbox v-model="selectedIds" :label="msg.id" />
                      </div>
                      <div class="msg-icon" :class="msg.type">
                        <i :class="getIcon(msg.type)"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ formatTime(msg.createdAt) }}</div>
                      </div>
                      <span v-if="!msg.read" class="unread-dot"></span>
                    </div>
                    <p class="msg-content">{{ msg.content }}</p>
                  </el-card>
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
                        <i :class="getIcon(msg.type)"></i>
                      </div>
                      <div class="msg-info">
                        <div class="msg-title">{{ msg.title }}</div>
                        <div class="msg-time">{{ formatTime(msg.createdAt) }}</div>
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
                        <div class="msg-time">{{ formatTime(msg.createdAt) }}</div>
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
                        <div class="msg-time">{{ formatTime(msg.createdAt) }}</div>
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

    <el-dialog title="发布通知" v-model="showSendModal" width="500px">
      <el-form :model="sendForm" label-width="80px">
        <el-form-item label="通知标题" prop="title">
          <el-input v-model="sendForm.title" placeholder="请输入通知标题" />
        </el-form-item>
        <el-form-item label="通知类型">
          <el-select v-model="sendForm.type" placeholder="请选择通知类型">
            <el-option label="系统通知" value="system" />
            <el-option label="活动通知" value="activity" />
            <el-option label="值班通知" value="duty" />
            <el-option label="服务队通知" value="team" />
          </el-select>
        </el-form-item>
        <el-form-item label="通知内容" prop="content">
          <el-input v-model="sendForm.content" type="textarea" placeholder="请输入通知内容" :rows="4" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="sendForm.broadcast">发送给所有用户</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSendModal = false">取消</el-button>
        <el-button type="danger" @click="handleSendNotification">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUser, logout, getRole } from '@/utils/auth'
import { getAllNotifications, markAllAsRead, deleteSelected, broadcastNotification, markAsRead, createNotification } from '@/api/notification'

const user = ref(getUser())
const activeMenu = ref('/messages')
const activeTab = ref('all')
const currentFilter = ref('all')
const selectedIds = ref([])
const messages = ref([])
const showSendModal = ref(false)
const sendForm = ref({
  title: '',
  content: '',
  type: 'system',
  broadcast: true
})

const isAdmin = computed(() => {
  const role = getRole()
  console.log('[DEBUG] 当前用户角色:', role)
  const result = role === 'ADMIN' || role === 'LEADER'
  console.log('[DEBUG] 是否管理员:', result)
  return result
})

const filters = computed(() => [
  { label: '全部', value: 'all', count: messages.value.length },
  { label: '未读', value: 'unread', count: messages.value.filter(m => !m.read).length },
  { label: '系统通知', value: 'system', count: messages.value.filter(m => m.type === 'system').length },
  { label: '活动通知', value: 'activity', count: messages.value.filter(m => m.type === 'activity').length },
  { label: '值班通知', value: 'duty', count: messages.value.filter(m => m.type === 'duty').length },
  { label: '服务队通知', value: 'team', count: messages.value.filter(m => m.type === 'team').length }
])

const filteredMessages = computed(() => {
  if (currentFilter.value === 'all') {
    return messages.value
  } else if (currentFilter.value === 'unread') {
    return messages.value.filter(m => !m.read)
  } else {
    return messages.value.filter(m => m.type === currentFilter.value)
  }
})

const unreadMessages = computed(() => messages.value.filter(m => !m.read))
const systemMessages = computed(() => messages.value.filter(m => m.type === 'system'))
const activityMessages = computed(() => messages.value.filter(m => m.type === 'activity'))

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

const loadMessages = async () => {
  try {
    const response = await getAllNotifications()
    if (response.code === 200) {
      messages.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取消息失败')
    console.error(error)
  }
}

const handleLogout = () => {
  logout()
}

const navigateTo = (path) => {
  window.location.href = path
}

const handleTabClick = () => {
  selectedIds.value = []
}

const toggleSelect = async (id) => {
  const index = selectedIds.value.indexOf(id)
  if (index > -1) {
    selectedIds.value.splice(index, 1)
  } else {
    selectedIds.value.push(id)
  }
  const msg = messages.value.find(m => m.id === id)
  if (msg && !msg.read) {
    msg.read = true
    try {
      await markAsRead(id)
    } catch (error) {
      console.error(error)
    }
  }
}

const handleMarkAllRead = async () => {
  try {
    await markAllAsRead()
    messages.value.forEach(msg => msg.read = true)
    ElMessage.success('已全部标记为已读')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
}

const deleteSelected = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的消息')
    return
  }
  try {
    await deleteSelected(selectedIds.value)
    messages.value = messages.value.filter(m => !selectedIds.value.includes(m.id))
    selectedIds.value = []
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
    console.error(error)
  }
}

const handleSendNotification = async () => {
  console.log('[DEBUG] 发布按钮被点击')
  console.log('[DEBUG] sendForm:', sendForm.value)
  
  if (!sendForm.value.title || !sendForm.value.content) {
    console.log('[DEBUG] 表单不完整')
    ElMessage.warning('请填写完整的通知信息')
    return
  }
  
  try {
    const data = {
      title: sendForm.value.title,
      content: sendForm.value.content,
      type: sendForm.value.type,
      userId: user.value?.id || 1
    }
    console.log('[DEBUG] 发送数据:', data)
    
    if (sendForm.value.broadcast) {
      console.log('[DEBUG] 调用 broadcastNotification')
      await broadcastNotification(data)
    } else {
      console.log('[DEBUG] 调用 createNotification')
      await createNotification(data)
    }
    
    showSendModal.value = false
    sendForm.value = {
      title: '',
      content: '',
      type: 'system',
      broadcast: true
    }
    ElMessage.success('通知发布成功')
    await loadMessages()
  } catch (error) {
    console.error('[DEBUG] 发布失败:', error)
    ElMessage.error('发布失败')
  }
}

onMounted(() => {
  loadMessages()
})
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

.msg-icon.duty {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
}

.msg-icon.team {
  background: linear-gradient(135deg, #dc143c 0%, #ff6b6b 100%);
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
