
<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo-section">
        <div class="logo">
          <span class="logo-icon">❤</span>
        </div>
        <h1 class="title">青志汇</h1>
        <p class="subtitle">高校志愿者团学组织人员管理系统</p>
      </div>
      
      <el-form ref="loginForm" :model="loginForm" label-width="80px" class="login-form">
        <el-form-item label="学号" prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入学号"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="Lock"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">
            登录
          </el-button>
        </el-form-item>
        
        <div class="tips">
          <span>默认密码：123456</span>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'
import { setToken, setUser } from '@/utils/auth'

const loginForm = ref({
  username: '',
  password: ''
})

const loginFormRef = ref(null)

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入学号和密码')
    return
  }
  
  try {
    const response = await login(loginForm.value)
    if (response.code === 200) {
      setToken(response.data.token)
      setUser(response.data.user)
      ElMessage.success('登录成功')
      window.location.href = '/'
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '登录失败')
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #DC143C 0%, #FF4444 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.login-box {
  background: #ffffff;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.logo-section {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #DC143C 0%, #FF6B6B 100%);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto 20px;
}

.logo-icon {
  font-size: 40px;
  color: #ffffff;
}

.title {
  font-size: 28px;
  font-weight: bold;
  color: #DC143C;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #999999;
}

.login-form {
  margin-top: 20px;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: bold;
}

.tips {
  text-align: center;
  margin-top: 16px;
  font-size: 12px;
  color: #999999;
}
</style>
