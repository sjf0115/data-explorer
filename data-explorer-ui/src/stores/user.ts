import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

const API_BASE_URL = 'http://localhost:8080/api'

export interface User {
  userId: string
  userName: string
  creator: string
  gmtCreate: string
}

export const useUserStore = defineStore('user', () => {
  // State
  const currentUser = ref<User | null>(null)
  const isLoggedIn = computed(() => currentUser.value !== null)

  // Actions
  async function login(userId: string, password: string): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/user/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ userId, password })
      })
      
      const result = await response.json()
      
      if (result.code === 200 && result.data) {
        currentUser.value = result.data
        return true
      }
      return false
    } catch (error) {
      console.error('Login error:', error)
      return false
    }
  }

  async function logout(): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/user/logout`, {
        method: 'POST',
        credentials: 'include'
      })
      
      const result = await response.json()
      
      if (result.code === 200) {
        currentUser.value = null
        return true
      }
      return false
    } catch (error) {
      console.error('Logout error:', error)
      return false
    }
  }

  async function checkLoginStatus(): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/user/current`, {
        method: 'GET',
        credentials: 'include'
      })
      
      const result = await response.json()
      
      if (result.code === 200 && result.data) {
        currentUser.value = result.data
        return true
      }
      currentUser.value = null
      return false
    } catch (error) {
      console.error('Check login status error:', error)
      currentUser.value = null
      return false
    }
  }

  async function getUserList(): Promise<User[]> {
    try {
      const response = await fetch(`${API_BASE_URL}/user/list`, {
        method: 'GET',
        credentials: 'include'
      })
      
      const result = await response.json()
      
      if (result.code === 200 && result.data) {
        return result.data
      }
      return []
    } catch (error) {
      console.error('Get user list error:', error)
      return []
    }
  }

  async function createUser(userId: string, userName: string, password: string): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/user/create`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ userId, userName, password })
      })
      
      const result = await response.json()
      
      return result.code === 200
    } catch (error) {
      console.error('Create user error:', error)
      return false
    }
  }

  async function disableUser(userId: string): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/user/disable/${userId}`, {
        method: 'POST',
        credentials: 'include'
      })
      
      const result = await response.json()
      
      return result.code === 200
    } catch (error) {
      console.error('Disable user error:', error)
      return false
    }
  }

  return {
    currentUser,
    isLoggedIn,
    login,
    logout,
    checkLoginStatus,
    getUserList,
    createUser,
    disableUser
  }
})
