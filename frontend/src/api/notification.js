import axios from '@/utils/axios'

export function getAllNotifications() {
  return axios({
    url: '/notifications',
    method: 'get'
  })
}

export function getNotificationById(id) {
  return axios({
    url: `/notifications/${id}`,
    method: 'get'
  })
}

export function createNotification(data) {
  return axios({
    url: '/notifications',
    method: 'post',
    data
  })
}

export function broadcastNotification(data) {
  return axios({
    url: '/notifications/broadcast',
    method: 'post',
    data
  })
}

export function markAsRead(id) {
  return axios({
    url: `/notifications/${id}/read`,
    method: 'put'
  })
}

export function markAllAsRead() {
  return axios({
    url: '/notifications/read-all',
    method: 'put'
  })
}

export function deleteNotification(id) {
  return axios({
    url: `/notifications/${id}`,
    method: 'delete'
  })
}

export function deleteSelected(ids) {
  return axios({
    url: '/notifications/batch',
    method: 'delete',
    data: ids
  })
}