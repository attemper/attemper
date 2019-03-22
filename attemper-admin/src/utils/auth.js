export const TOKEN_KEY = 'token'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || false
}

export function setToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
  } else {
    this.removeToken()
  }
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}
