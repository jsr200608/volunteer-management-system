
import axios from '@/utils/axios'

export function login(data) {
  return axios.post('/auth/login', data)
}

export function logout() {
  return axios.post('/auth/logout')
}

export function changePassword(data) {
  return axios.put('/auth/password', data)
}

export function getCurrentUser() {
  return axios.get('/auth/me')
}
