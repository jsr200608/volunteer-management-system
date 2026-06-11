
import axios from '@/utils/axios'

export function getTeams() {
  return axios.get('/teams')
}

export function getTeam(id) {
  return axios.get(`/teams/${id}`)
}
