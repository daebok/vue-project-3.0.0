import axios from 'axios'
import { loginAjax } from './ajax.config'
import { Toast } from 'mint-ui'
export default {
  login(username, pwd, cb) {
    cb = arguments[arguments.length - 1]
    if(localStorage.token){
      if(cb) cb(true)
      return
    }
    request(username, pwd, (res) => {
      //if(res.data.resMsg == 'SUCCESS') {
      if(res.authenticated) {
        localStorage.token = res.token
        localStorage.user = JSON.stringify(res.user)
      }else{
        if(cb) cb(false)
      }
    })
  },
  getToken () {
    return localStorage.token
  },
  logout (cb) {
    delete localStorage.token
    if (cb) cb()
  },
  loggedIn () {
    // 登录时效
    // setTimeout(this.logout, 1000*60)
    return !!localStorage.token
  },
}

function request (username, pwd, cb) {
  let formData = new FormData()
  formData.append('loginName', username)
  formData.append('loginPwd', base64Encode(pwd))
  axios.post(loginAjax, formData).then((res) => {
    if (res.data.resMsg == 'SUCCESS') {
      let user = {
         loginName: res.data.resData.username,
         avatarPhoto: res.data.resData.avatarPhoto,
         userId: res.data.resData.userId,
         expiresIn: res.data.resData.expiresIn,
      }
      cb({
        authenticated: true,
        token: Math.random().toString(36).substring(7),
        user: user
      })
    } else {
      Toast(res.data.resMsg)
      cb({ authenticated: false })
    }
  })
}
//base64加密
function base64Encode(str) {
  return btoa(encodeURIComponent(str))
}
//base64解密
function base64Decode(str)  {
  return decodeURIComponent(atob(str))
}
