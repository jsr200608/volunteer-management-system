
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
            <el-menu-item index="/members">
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
            <h2 class="page-title">成员管理</h2>
            <el-button type="primary" icon="Plus" @click="openAddModal">
              添加成员
            </el-button>
          </div>
          
          <div class="search-bar">
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索姓名或学号"
              prefix-icon="Search"
              @keyup.enter="handleSearch"
              class="search-input"
            />
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </div>
          
          <div class="filter-bar">
            <el-select v-model="filterCollege" placeholder="选择学院" class="filter-item">
              <el-option label="全部" value="" />
              <el-option label="计算机学院" value="计算机学院" />
              <el-option label="经济管理学院" value="经济管理学院" />
              <el-option label="文学院" value="文学院" />
              <el-option label="理学院" value="理学院" />
            </el-select>
            
            <el-select v-model="filterGrade" placeholder="选择年级" class="filter-item">
              <el-option label="全部" value="" />
              <el-option label="2021级" value="2021级" />
              <el-option label="2022级" value="2022级" />
              <el-option label="2023级" value="2023级" />
              <el-option label="2024级" value="2024级" />
            </el-select>
            
            <el-select v-model="filterStatus" placeholder="选择状态" class="filter-item">
              <el-option label="全部" value="" />
              <el-option label="在职" value="ACTIVE" />
              <el-option label="离职" value="LEFT" />
            </el-select>
          </div>
          
          <el-table :data="memberList" border class="member-table">
            <el-table-column prop="studentId" label="学号" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="gender" label="性别" />
            <el-table-column prop="grade" label="年级" />
            <el-table-column prop="college" label="学院" />
            <el-table-column prop="major" label="专业" />
            <el-table-column prop="volunteerHours" label="志愿时长" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'warning'">
                  {{ scope.row.status === 'ACTIVE' ? '在职' : '离职' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="small" @click="viewMember(scope.row)">查看</el-button>
                <el-button size="small" type="primary" @click="editMember(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteMember(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination 
            :current-page="currentPage" 
            :page-size="pageSize"
            :total="total"
            @current-change="handlePageChange"
            class="pagination"
          />
          
          <!-- 添加/编辑成员弹窗 -->
          <el-dialog 
            :title="isEdit ? '编辑成员' : '添加成员'" 
            :visible.sync="showModal"
          >
            <el-form :model="formData" label-width="100px">
              <el-form-item label="学号" v-if="!isEdit">
                <el-input v-model="formData.studentId" placeholder="请输入学号" />
              </el-form-item>
              <el-form-item label="姓名">
                <el-input v-model="formData.name" placeholder="请输入姓名" />
              </el-form-item>
              <el-form-item label="性别">
                <el-select v-model="formData.gender" placeholder="请选择性别">
                  <el-option label="男" value="男" />
                  <el-option label="女" value="女" />
                </el-select>
              </el-form-item>
              <el-form-item label="年级">
                <el-input v-model="formData.grade" placeholder="请输入年级" />
              </el-form-item>
              <el-form-item label="学院">
                <el-input v-model="formData.college" placeholder="请输入学院" />
              </el-form-item>
              <el-form-item label="专业">
                <el-input v-model="formData.major" placeholder="请输入专业" />
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="formData.phone" placeholder="请输入手机号" />
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="formData.email" placeholder="请输入邮箱" />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="showModal = false">取消</el-button>
              <el-button type="primary" @click="submitForm">{{ isEdit ? '保存' : '添加' }}</el-button>
            </template>
          </el-dialog>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUser, logout } from '@/utils/auth'
import { getMembers, createMember, updateMember, deleteMember } from '@/api/member'

const user = ref(getUser())
const activeMenu = ref('/members')
const memberList = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const total = ref(0)
const searchKeyword = ref('')
const filterCollege = ref('')
const filterGrade = ref('')
const filterStatus = ref('')
const showModal = ref(false)
const isEdit = ref(false)
const formData = ref({
  studentId: '',
  name: '',
  gender: '',
  grade: '',
  college: '',
  major: '',
  phone: '',
  email: ''
})
const editingId = ref(null)

const navigateTo = (path) => {
  window.location.href = path
}

const handleLogout = () => {
  logout()
}

const loadMembers = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      college: filterCollege.value || undefined,
      grade: filterGrade.value || undefined,
      status: filterStatus.value || undefined
    }
    
    const response = await getMembers(params)
    if (response.code === 200) {
      memberList.value = response.data.content
      total.value = response.data.totalElements
    }
  } catch (error) {
    ElMessage.error('获取成员列表失败')
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value) {
    loadMembers()
    return
  }
  
  try {
    const response = await getMembers({
      page: 0,
      size: pageSize.value,
      keyword: searchKeyword.value
    })
    if (response.code === 200) {
      memberList.value = response.data.content
      total.value = response.data.totalElements
    }
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  filterCollege.value = ''
  filterGrade.value = ''
  filterStatus.value = ''
  currentPage.value = 0
  loadMembers()
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadMembers()
}

const openAddModal = () => {
  isEdit.value = false
  editingId.value = null
  formData.value = {
    studentId: '',
    name: '',
    gender: '',
    grade: '',
    college: '',
    major: '',
    phone: '',
    email: ''
  }
  showModal.value = true
}

const editMember = (row) => {
  isEdit.value = true
  editingId.value = row.id
  formData.value = {
    studentId: row.studentId,
    name: row.name,
    gender: row.gender,
    grade: row.grade,
    college: row.college,
    major: row.major,
    phone: row.phone,
    email: row.email
  }
  showModal.value = true
}

const viewMember = (row) => {
  window.location.href = `/members/${row.id}`
}

const submitForm = async () => {
  if (!formData.value.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  
  try {
    if (isEdit.value) {
      const response = await updateMember(editingId.value, formData.value)
      if (response.code === 200) {
        ElMessage.success('修改成功')
        showModal.value = false
        loadMembers()
      }
    } else {
      if (!formData.value.studentId) {
        ElMessage.warning('请输入学号')
        return
      }
      const response = await createMember(formData.value)
      if (response.code === 200) {
        ElMessage.success('添加成功')
        showModal.value = false
        loadMembers()
      }
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  }
}

const deleteMember = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该成员吗？', '提示', {
      type: 'warning'
    })
    
    const response = await deleteMember(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadMembers()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadMembers()
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
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  color: #333333;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.search-input {
  width: 300px;
}

.filter-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.filter-item {
  width: 150px;
}

.member-table {
  background: #ffffff;
  border-radius: 8px;
  margin-bottom: 16px;
}

.pagination {
  text-align: right;
}
</style>
