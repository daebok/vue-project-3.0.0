export default {
  methods: {
    base64Encode(str) {  //base64加密
      return btoa(encodeURIComponent(str))
    },
    base64Decode(str)  {   //base64解密
      return decodeURIComponent(atob(str))
    }
  }
}
