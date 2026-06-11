
import axios from '@/utils/axios'

export function getMembers(params) {
  return axios.get('/members', { params })
}

export function getMember(id) {
  return axios.get(`/members/${id}`)
}

export function createMember(data) {
  return axios.post('/members', data)
}

export function updateMember(id, data) {
  return axios.put(`/members/${id}`, data)
}

export function deleteMember(id) {
  return axios.delete(`/members/${id}`)
}

export function searchMembers(params) {
  return axios.get('/members/search', { params })
}
