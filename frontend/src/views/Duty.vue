<template>
  <div class="duty">
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
            <h2 class="page-title">值班安排</h2>
            <el-button type="primary" icon="Plus" @click="showAddModal = true">
              安排值班
            </el-button>
          </div>
          
          <div class="week-nav">
            <el-button 
              type="text" 
              icon="ArrowLeft" 
              @click="prevWeek"
              :disabled="!hasPrevWeek"
            >上一周</el-button>
            <span class="week-label">{{ currentWeekLabel }}</span>
            <el-button 
              type="text" 
              icon="ArrowRight" 
              @click="nextWeek"
              :disabled="!hasNextWeek"
            >下一周</el-button>
          </div>
          
          <div class="duty-calendar">
            <div class="calendar-header">
              <div 
                v-for="day in weekDays" 
                :key="day.date" 
                :class="['day-header', { 'today': day.isToday, 'selected': day.date === selectedDate }]"
                @click="selectDate(day.date)"
              >
                <div class="day-name">{{ day.name }}</div>
                <div class="day-date">{{ day.day }}</div>
              </div>
            </div>
            <div class="calendar-body">
              <div class="time-slots">
                <div v-for="slot in timeSlots" :key="slot" class="time-slot">{{ slot }}</div>
              </div>
              <div class="duty-grid">
                <div 
                  v-for="day in weekDays" 
                  :key="day.date" 
                  class="day-column"
                >
                  <div 
                    v-for="slot in timeSlots" 
                    :key="slot" 
                    :class="['duty-cell', { 'has-duty': getDuty(day.date, slot) }]"
                  >
                    <div v-if="getDuty(day.date, slot)" class="duty-info">
                      <div class="duty-name">{{ getDuty(day.date, slot).name }}</div>
                      <div class="duty-team">{{ getDuty(day.date, slot).team }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="duty-list-section">
            <h3 class="section-title">今日值班安排</h3>
            <el-table :data="todayDuties" border>
              <el-table-column prop="time" label="时间段" />
              <el-table-column prop="name" label="值班人员" />
              <el-table-column prop="team" label="所属服务队" />
              <el-table-column prop="location" label="值班地点" />
              <el-table-column label="操作">
                <template #default="scope">
                  <el-button size="small" @click="editDuty(scope.row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteDuty(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-main>
      </el-container>
    </el-container>
    
    <el-dialog title="安排值班" v-model="showAddModal" width="400px">
      <el-form :model="dutyForm" label-width="80px">
        <el-form-item label="日期">
          <el-date-picker v-model="dutyForm.date" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="时间段">
          <el-select v-model="dutyForm.time" placeholder="选择时间段">
            <el-option v-for="slot in timeSlots" :key="slot" :label="slot" :value="slot" />
          </el-select>
        </el-form-item>
        <el-form-item label="值班人员">
          <el-select v-model="dutyForm.name" placeholder="选择人员">
            <el-option label="张三" value="张三" />
            <el-option label="李四" value="李四" />
            <el-option label="王五" value="王五" />
            <el-option label="赵六" value="赵六" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务队">
          <el-select v-model="dutyForm.team" placeholder="选择服务队">
            <el-option label="爱心家园" value="爱心家园" />
            <el-option label="绿色环保" value="绿色环保" />
            <el-option label="文化之旅" value="文化之旅" />
            <el-option label="安全使者" value="安全使者" />
          </el-select>
        </el-form-item>
        <el-form-item label="值班地点">
          <el-input v-model="dutyForm.location" placeholder="输入值班地点" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="submitDuty">确认安排</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getUser, logout } from '@/utils/auth'

const user = ref(getUser())
const activeMenu = ref('/duty')
const showAddModal = ref(false)
const selectedDate = ref('')
const currentWeekOffset = ref(0)

const timeSlots = ['08:00-10:00', '10:00-12:00', '14:00-16:00', '16:00-18:00', '18:00-20:00']

const weekDays = computed(() => {
  const days = []
  const today = new Date()
  const startOfWeek = new Date(today)
  startOfWeek.setDate(today.getDate() - today.getDay() + 1 + currentWeekOffset.value * 7)
  
  const dayNames = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  
  for (let i = 0; i < 7; i++) {
    const date = new Date(startOfWeek)
    date.setDate(startOfWeek.getDate() + i)
    const dateStr = date.toISOString().split('T')[0]
    const todayStr = new Date().toISOString().split('T')[0]
    
    days.push({
      date: dateStr,
      day: date.getDate(),
      name: dayNames[i],
      isToday: dateStr === todayStr
    })
  }
  return days
})

const currentWeekLabel = computed(() => {
  if (weekDays.value.length >= 2) {
    const start = weekDays.value[0]
    const end = weekDays.value[6]
    return `${start.date} - ${end.date}`
  }
  return ''
})

const hasPrevWeek = computed(() => currentWeekOffset.value > -52)
const hasNextWeek = computed(() => currentWeekOffset.value < 0)

const dutyForm = reactive({
  date: '',
  time: '',
  name: '',
  team: '',
  location: ''
})

const duties = ref([
  { date: new Date().toISOString().split('T')[0], time: '08:00-10:00', name: '张三', team: '爱心家园', location: '校门口' },
  { date: new Date().toISOString().split('T')[0], time: '10:00-12:00', name: '李四', team: '绿色环保', location: '操场' },
  { date: new Date().toISOString().split('T')[0], time: '14:00-16:00', name: '王五', team: '文化之旅', location: '图书馆' },
  { date: new Date().toISOString().split('T')[0], time: '16:00-18:00', name: '赵六', team: '安全使者', location: '教学楼' },
])

const todayDuties = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return duties.value.filter(d => d.date === today)
})

const getDuty = (date, time) => {
  return duties.value.find(d => d.date === date && d.time === time)
}

const handleLogout = () => {
  logout()
}

const navigateTo = (path) => {
  window.location.href = path
}

const prevWeek = () => {
  if (hasPrevWeek.value) {
    currentWeekOffset.value--
  }
}

const nextWeek = () => {
  if (hasNextWeek.value) {
    currentWeekOffset.value++
  }
}

const selectDate = (date) => {
  selectedDate.value = date
}

const editDuty = (row) => {
  Object.assign(dutyForm, row)
  showAddModal.value = true
}

const deleteDuty = (row) => {
  const index = duties.value.findIndex(d => d.date === row.date && d.time === row.time)
  if (index > -1) {
    duties.value.splice(index, 1)
    ElMessage.success('删除成功')
  }
}

const submitDuty = () => {
  if (!dutyForm.date || !dutyForm.time || !dutyForm.name) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  const existingIndex = duties.value.findIndex(d => d.date === dutyForm.date && d.time === dutyForm.time)
  if (existingIndex > -1) {
    duties.value[existingIndex] = { ...dutyForm }
  } else {
    duties.value.push({ ...dutyForm })
  }
  
  ElMessage.success('安排成功')
  showAddModal.value = false
  Object.keys(dutyForm).forEach(key => dutyForm[key] = '')
}
</script>

<style scoped>
.duty {
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

.week-nav {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.week-label {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.duty-calendar {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  margin-bottom: 20px;
}

.calendar-header {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
  margin-bottom: 16px;
}

.day-header {
  text-align: center;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.day-header:hover {
  background: #FFF5F5;
}

.day-header.today {
  background: #DC143C;
  color: #fff;
}

.day-header.selected {
  background: #FFE4E1;
  border: 2px solid #DC143C;
}

.day-name {
  font-size: 12px;
  margin-bottom: 4px;
}

.day-date {
  font-size: 18px;
  font-weight: bold;
}

.calendar-body {
  display: flex;
}

.time-slots {
  width: 80px;
}

.time-slot {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #666;
  border-bottom: 1px solid #eee;
}

.duty-grid {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.day-column {
  display: flex;
  flex-direction: column;
}

.duty-cell {
  height: 60px;
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 4px;
  transition: all 0.3s;
}

.duty-cell:hover {
  background: #FFF5F5;
}

.duty-cell.has-duty {
  background: #FFF0F0;
  border-color: #DC143C;
}

.duty-info {
  font-size: 11px;
}

.duty-name {
  font-weight: bold;
  color: #DC143C;
}

.duty-team {
  color: #666;
}

.duty-list-section {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .calendar-header {
    grid-template-columns: repeat(7, 1fr);
  }
  
  .day-header {
    padding: 8px 4px;
  }
  
  .day-date {
    font-size: 14px;
  }
  
  .time-slots {
    width: 60px;
  }
  
  .time-slot {
    font-size: 10px;
  }
  
  .duty-info {
    font-size: 9px;
  }
}
</style>